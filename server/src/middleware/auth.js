const { prisma } = require("../prismaFunctions/prisma");

const jwt = require("jsonwebtoken");

const verifyToken = (req, res, next) => {
  const authHeader = req.headers["authorization"];
  const token = authHeader ? authHeader.split(" ")[1] : null;

  try {
    if (!token) {
      throw new Error("Token não fornecido");
    }

    jwt.verify(token, process.env.JWT_SECRET, async (err, decoded) => {
      if (err) {
        throw new Error("Token inválido");
      } else {
        const user = await prisma.tb_users.findUnique({
          where: {
            id_user: decoded.id,
          },
        });

        if (!user) {
          throw new Error("Token inválido");
        }
        delete user.password;

        req.user = user;

        next();
      }
    });
  } catch (error) {
    return res.status(408).json({ error: error.message });
  }
};

module.exports = verifyToken;
