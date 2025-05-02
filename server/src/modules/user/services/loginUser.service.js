const bcrypt = require("bcrypt");
const { generateToken } = require("../../../config/jwt");
const CustomError = require("../../../core/errors/CustomError");
const { verifyInput } = require("../../../utils/validation");
const { prisma } = require("../../../config/prisma");

module.exports.loginUserService = async ({ email, senha }) => {
  const missing = verifyInput({ email, senha });
  if (missing)
    throw new CustomError(`Campo obrigatório ausente: ${missing}`, 400);

  const user = await prisma.tb_users.findFirst({
    where: { email_user: email },
  });
  if (!user || !(await bcrypt.compare(senha, user.password_user))) {
    throw new CustomError("Email e/ou senha incorretos", 401);
  }

  const token = generateToken(user.id_user);
  return {
    message: "Login efetuado com sucesso!",
    token,
    id: user.id_user,
    type_user: user.type_user,
  };
};
