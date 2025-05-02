const prisma = require("../../../config/prisma");
const jwt = require("jsonwebtoken");
const bcrypt = require("bcrypt");
const CustomError = require("../../../core/errors/CustomError");

module.exports.lostPasswordService = async ({ id_user, token }, password) => {
  const user = await prisma.tb_users.findFirst({
    where: { id_user: Number(id_user), lost_pasword_token_user: token },
  });

  if (!user) throw new CustomError("Usuário ou token inválido", 404);

  try {
    const decoded = jwt.verify(token, process.env.JWT_SECRET);
    if (decoded.id !== Number(id_user)) throw new Error();
  } catch {
    throw new CustomError("Token inválido ou expirado", 401);
  }

  const hash = await bcrypt.hash(password, 10);

  await prisma.tb_users.update({
    where: { id_user: Number(id_user) },
    data: {
      password_user: hash,
      password_changed: true,
      lost_pasword_token_user: null,
    },
  });
};
