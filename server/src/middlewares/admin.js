const { prisma } = require("../config/prisma");
const CustomError = require("../core/errors/CustomError");

module.exports = async (req, _, next) => {
  const user = await prisma.tb_users.findUnique({
    where: { id_user: req.user.id_user },
  });
  console.log({ user });

  if (user.type_user !== "admin") throw new CustomError("Acesso negado", 403);
  next();
};
