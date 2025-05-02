// 📦 Importações necessárias
const { CustomError } = require("../../class/class");
const { extractUserToken } = require("../../functions/token");

// 🔐 Controlador para verificar se o token do usuário é válido e se a senha foi alterada
async function verifyTokenLogged(req, res) {
  try {
    // 🔹 Captura o cabeçalho de autorização (Authorization: Bearer <token>)
    const authHeader = req.headers["authorization"];
    const token = authHeader ? authHeader.split(" ")[1] : null;

    // 🔹 Verifica se o token foi enviado
    if (!token) throw new CustomError("Token inválido", 404);

    // 🔹 Extrai e valida os dados do usuário a partir do token
    const user = await extractUserToken(token);

    // 🔹 Retorna sucesso e informa se a senha já foi alterada
    res.json({ verifyToken: true, passwordChanged: user.password_changed });
  } catch (error) {
    // 🔹 Em caso de erro, exibe no console e retorna erro genérico para o cliente
    console.error("Erro no middleware de autenticação:", error);
    return res
      .status(500)
      .json({ verifyToken: false, error: "Erro interno no servidor" });
  }
}

// 🚀 Exporta a função para uso em rotas
module.exports = verifyTokenLogged;
