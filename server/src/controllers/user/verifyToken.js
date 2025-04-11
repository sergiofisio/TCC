const { CustomError } = require("../../class/class");
const { extractUserToken } = require("../../functions/token");

async function verifyTokenLogged(req, res) {
  try {
    const authHeader = req.headers["authorization"];
    const token = authHeader ? authHeader.split(" ")[1] : null;

    if (!token) throw new CustomError("Token inválido", 404);

    const user = await extractUserToken(token);

    res.json({ verifyToken: true, passwordChanged: user.password_changed });
  } catch (error) {
    console.error("Erro no middleware de autenticação:", error);
    return res
      .status(500)
      .json({ verifyToken: false, error: "Erro interno no servidor" });
  }
}

module.exports = verifyTokenLogged;
