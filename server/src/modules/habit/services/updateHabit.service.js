const { prisma } = require("../../../config/prisma");
const CustomError = require("../../../core/errors/CustomError");

const models = {
  breath: prisma.tb_breath,
  meditate: prisma.tb_meditation,
};

module.exports.updateHabitService = async (id_habit, body) => {
  const { last_time_habit } = body;
  const habit = await prisma.tb_habits.findUnique({ where: { id_habit } });
  if (!habit) throw new CustomError("Hábito não encontrado", 404);

  return prisma.tb_habits.update({
    where: { id_user },
    data: { last_time_habit },
  });
};
