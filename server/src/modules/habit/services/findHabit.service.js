const { prisma } = require("../../../config/prisma");
const CustomError = require("../../../core/errors/CustomError");

module.exports.findHabitService = async (id_user, params) => {
  const id = Number(params.id) || null;
  let data;
  if (id) {
    data = await prisma.tb_habits.findUnique({
      where: {
        id_habit: Number(id),
      },
    });
    if (!data) throw new CustomError("Hábito não encontrada", 404);
    return data;
  }
  data = await prisma.tb_habits.findMany({
    where: {
      users_id: id_user,
    },
  });

  return data;
};
