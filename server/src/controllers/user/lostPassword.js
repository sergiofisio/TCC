const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");
const { CustomError } = require("../../class/class");
const { prisma } = require("../../prismaFunctions/prisma");

async function lostPassword(req, res) {
  try {
    const { id_user, token } = req.params;
    const { password } = req.body;

    if (!id_user || !token || !password) {
      throw new CustomError("Campo obrigatório ausente", 400);
    }

    let user = await prisma.tb_users.findFirst({
      where: {
        AND: [{ id_user: Number(id_user) }, { lost_pasword_token_user: token }],
      },
    });

    if (!user) throw new CustomError("Usuário e/ou token inválido", 404);

    let decoded;
    try {
      decoded = jwt.verify(token, process.env.JWT_SECRET);
    } catch (err) {
      throw new CustomError("Token inválido ou expirado", 401);
    }

    if (decoded.id.id_user !== Number(id_user)) {
      throw new CustomError("Usuário e/ou token inválido", 403);
    }

    const hashPassword = await bcrypt.hash(password, 10);

    await prisma.tb_users.update({
      where: { id_user: Number(id_user) },
      data: {
        password_user: hashPassword,
        lost_pasword_token_user: null,
        password_changed: true,
      },
    });

    return res.status(200).json({
      message: "Senha alterada com sucesso",
    });
  } catch (error) {
    console.error({ error });

    return res.status(error.status || 500).json({ error: error.message });
  }
}

module.exports = lostPassword;
