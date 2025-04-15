const { CustomError } = require("../../class/class");
const { generateToken } = require("../../functions/token");
const { verifyInput } = require("../../functions/verify");
const { prisma } = require("../../prismaFunctions/prisma");
const bcrypt = require("bcrypt");

async function login(req, res) {
  try {
    const { email, senha } = req.body;
    const today = new Date();

    const missingInput = verifyInput({ email, senha });

    if (missingInput)
      throw new CustomError(`Campo obrigatório ausente: ${missingInput}`, 400);

    const user = await prisma.tb_users.findFirst({
      where: {
        email_user: email,
      },
    });

    if (!user) throw new CustomError("Email e/ou Senha incorreta", 401);

    const { id_user, type_user, password_user } = user;

    const match = await bcrypt.compare(senha, password_user);

    if (!id_user || !match)
      throw new CustomError("Email e/ou Senha incorreta", 401);

    const token = generateToken(id_user);

    await prisma.tb_users.update({
      where: { id_user: Number(id_user) },
      data: {
        password_changed: false,
        last_login_date_user: today,
        lost_pasword_token_user: null,
      },
    });

    return res.status(200).json({
      message: "Login efetuado com sucesso!",
      token,
      id: id_user,
      type_user,
    });
  } catch (error) {
    console.error({ error });

    return res.status(error.status || 500).json({ error: error.message });
  }
}

module.exports = login;
