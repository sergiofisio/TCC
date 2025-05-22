const { prisma } = require("../../../config/prisma");
const CustomError = require("../../../core/errors/CustomError");

module.exports.getAllUsersService = async (admin_id) => {
  const admin = await prisma.tb_users.findUnique({
    where: { id_user: admin_id },
  });

  if (!admin || admin.type_user !== "admin") {
    throw new CustomError(
      "Acesso negado. Apenas administradores podem ver todos os usuários.",
      403
    );
  }

  const users = await prisma.tb_users.findMany({
    where: {
      NOT: { id_user: admin_id },
    },
    include: {
      phone_user: true,
      meditation_user: true,
      breath_user: true,
      today_user: true,
    },
  });

  return users;
};
