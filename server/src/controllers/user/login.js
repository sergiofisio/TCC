// 📦 Importações necessárias
const { CustomError } = require("../../class/class");
const { generateToken } = require("../../functions/token");
const { verifyInput } = require("../../functions/verify");
const { prisma } = require("../../prismaFunctions/prisma");
const bcrypt = require("bcrypt");

// 🔐 Controlador responsável por realizar o login do usuário
async function login(req, res) {
  try {
    // 🔹 Extrai e valida os dados do corpo da requisição
    const { email, senha } = req.body;
    const today = new Date();

    // 🔹 Verifica se todos os campos obrigatórios foram enviados
    const missingInput = verifyInput({ email, senha });

    if (missingInput)
      throw new CustomError(`Campo obrigatório ausente: ${missingInput}`, 400);

    // 🔹 Busca o usuário no banco pelo e-mail
    const user = await prisma.tb_users.findFirst({
      where: {
        email_user: email,
      },
    });

    // 🔹 Caso o usuário não exista, retorna erro de autenticação
    if (!user) throw new CustomError("Email e/ou Senha incorreta", 401);

    // 🔹 Extrai dados necessários do usuário
    const { id_user, type_user, password_user } = user;

    // 🔹 Verifica se a senha informada é compatível com a armazenada (hash)
    const match = await bcrypt.compare(senha, password_user);

    if (!id_user || !match)
      throw new CustomError("Email e/ou Senha incorreta", 401);

    // 🔹 Gera o token JWT para autenticação
    const token = generateToken(id_user);

    // 🔹 Atualiza dados do usuário: data do último login, zera token de recuperação, marca senha como não alterada
    await prisma.tb_users.update({
      where: { id_user: Number(id_user) },
      data: {
        password_changed: false,
        last_login_date_user: today,
        lost_pasword_token_user: null,
      },
    });

    // 🔹 Retorna sucesso com token e informações relevantes do usuário
    return res.status(200).json({
      message: "Login efetuado com sucesso!",
      token,
      id: id_user,
      type_user,
    });
  } catch (error) {
    // 🔹 Tratamento de erro
    console.error({ error });

    return res.status(error.status || 500).json({ error: error.message });
  }
}

// 🚀 Exporta a função para uso nas rotas
module.exports = login;
