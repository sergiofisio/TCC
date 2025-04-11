const { CustomError } = require("../class/class");
const { extractUserToken } = require("../functions/token");
const jwt = require("jsonwebtoken");

const verifyToken = async (req, res, next) => {
  try {
    const authHeader = req.headers["authorization"];
    const token = authHeader ? authHeader.split(" ")[1] : null;

    if (!token) {
      throw new CustomError("Token não fornecido", 401);
    }

    const user = await extractUserToken(token);

    req.user = user;

    next();
  } catch (error) {
    console.error("Erro no middleware de autenticação:", error);
    return res
      .status(500)
      .json({ verifyToken: false, error: "Erro interno no servidor" });
  }
};

module.exports = verifyToken;
