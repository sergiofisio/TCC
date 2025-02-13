const { prisma } = require("../prismaFunctions/prisma");
const jwt = require("jsonwebtoken");

const verifyToken = async (req, res, next) => {

  const authHeader = req.headers["authorization"];
  const token = authHeader ? authHeader.split(" ")[1] : null;

  if (!token) {
    return res.status(401).json({ verifyToken: false, error: "Token não fornecido" });
  }

  jwt.verify(token, process.env.JWT_SECRET, async (err, decoded) => {
    if (err) {
      return res.status(401).json({ verifyToken: false, error: "Token inválido" });
    }

    try {
      const user = await prisma.tb_users.findUnique({
        where: { id_user: decoded.id },
      });

      if (!user) {
        return res.status(401).json({ verifyToken: false, error: "Usuário não encontrado" });
      }

      delete user.password;

      req.user = user;

      if (req.path === "/verify") {
        return res.status(200).json({ verifyToken: true, user });
      }

      next();
    } catch (error) {
      console.error("Erro no middleware de autenticação:", error);
      return res.status(500).json({ verifyToken: false, error: "Erro interno no servidor" });
    }
  });
};

module.exports = verifyToken;
