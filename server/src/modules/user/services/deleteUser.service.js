const { prisma } = require("../../../config/prisma");
const CustomError = require("../../../core/errors/CustomError");

module.exports = async function deleteUserService(id) {
  console.log({ id });

  const user = await prisma.tb_users.findUnique({ where: { id_user: id } });
  if (!user) throw new CustomError("Usuário não encontrado", 404);
  return prisma.tb_users.update({
    where: { id_user: id },
    data: { active_user: false },
  });
};
