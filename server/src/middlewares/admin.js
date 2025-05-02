const { prisma } = require("../config/prisma");
const CustomError = require("../core/errors/CustomError");

module.exports = async (req, res, next) => {
  const user = await prisma.tb_users.findUnique({
    where: { id_user: req.user.id_user },
  });
  if (user.type_user !== "admin") throw new CustomError("Acesso negado", 403);
  next();
};
