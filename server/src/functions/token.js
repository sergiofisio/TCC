const jwt = require("jsonwebtoken");
const { prisma } = require("../prismaFunctions/prisma");
const util = require("util");
const { CustomError } = require("../class/class");

const verifyAsync = util.promisify(jwt.verify);

const generateToken = (id, time) =>
  jwt.sign({ id }, process.env.JWT_SECRET, {
    expiresIn: time || "30d",
  });

const extractUserToken = async (token) => {
  try {
    const { id } = await verifyAsync(token, process.env.JWT_SECRET);

    const user = await prisma.tb_users.findUnique({
      where: { id_user: Number(id) },
    });

    if (!user) throw new CustomError("Usuário e/ou Token inválido", 401);

    delete user.password_user;
    delete user.lost_pasword_token_user;

    return user;
  } catch (err) {
    throw new CustomError("Token inválido ou erro na extração do usuário", 401);
  }
};

module.exports = { generateToken, extractUserToken };
