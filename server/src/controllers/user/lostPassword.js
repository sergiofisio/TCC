// 📦 Importações necessárias
const bcrypt = require("bcrypt");
const jwt = require("jsonwebtoken");
const { CustomError } = require("../../class/class");
const { prisma } = require("../../prismaFunctions/prisma");

// 🔧 Função responsável por redefinir a senha do usuário via token
async function lostPassword(req, res) {
  try {
    // 🔹 Extrai parâmetros da URL e do corpo da requisição
    const { id_user, token } = req.params;
    const { password } = req.body;

    // 🔹 Verifica se todos os campos obrigatórios estão presentes
    if (!id_user || !token || !password) {
      throw new CustomError("Campo obrigatório ausente", 400);
    }

    // 🔹 Busca o usuário no banco com base no ID e token de recuperação
    let user = await prisma.tb_users.findFirst({
      where: {
        AND: [{ id_user: Number(id_user) }, { lost_pasword_token_user: token }],
      },
    });

    // 🔹 Se não encontrar o usuário com o token, retorna erro
    if (!user) throw new CustomError("Usuário e/ou token inválido", 404);

    let decoded;
    try {
      // 🔹 Verifica a validade do token JWT
      decoded = jwt.verify(token, process.env.JWT_SECRET);
    } catch (err) {
      throw new CustomError("Token inválido ou expirado", 401);
    }

    // 🔹 Garante que o ID do token corresponde ao do usuário
    if (decoded.id !== Number(id_user)) {
      throw new CustomError("Usuário e/ou token inválido", 403);
    }

    // 🔹 Gera a nova senha criptografada
    const hashPassword = await bcrypt.hash(password, 10);

    // 🔹 Atualiza o usuário no banco com a nova senha e limpa o token
    await prisma.tb_users.update({
      where: { id_user: Number(id_user) },
      data: {
        password_user: hashPassword,
        lost_pasword_token_user: null,
        password_changed: true,
      },
    });

    // 🔹 Retorna resposta de sucesso
    return res.status(200).json({
      message: "Senha alterada com sucesso",
    });
  } catch (error) {
    // 🔹 Tratamento de erros
    console.error({ error });

    return res.status(error.status || 500).json({ error: error.message });
  }
}

// 🚀 Exporta a função para uso nas rotas
module.exports = lostPassword;
