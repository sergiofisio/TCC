// 📦 Importações
const { CustomError } = require("../class/class");
const { extractUserToken } = require("../functions/token");
const jwt = require("jsonwebtoken");

// 🔐 Middleware para verificar e validar o token JWT de autenticação
const verifyToken = async (req, res, next) => {
  try {
    // 🔹 Extrai o header de autorização no formato "Bearer <token>"
    const authHeader = req.headers["authorization"];
    const token = authHeader ? authHeader.split(" ")[1] : null;

    // 🔹 Verifica se o token foi fornecido
    if (!token) {
      throw new CustomError("Token não fornecido", 401);
    }

    // 🔹 Extrai e valida os dados do usuário a partir do token
    const user = await extractUserToken(token);

    // 🔹 Anexa o objeto do usuário à requisição para uso nos próximos middlewares/controladores
    req.user = user;

    // 🔹 Continua para a próxima função da rota
    next();
  } catch (error) {
    // ❌ Em caso de erro, registra e retorna resposta genérica
    console.error("Erro no middleware de autenticação:", error);

    return res
      .status(500)
      .json({ verifyToken: false, error: "Erro interno no servidor" });
  }
};

// 🚀 Exporta o middleware para uso nas rotas protegidas
module.exports = verifyToken;
