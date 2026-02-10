const { prisma } = require("../../../config/prisma");

module.exports.addHabitService = async (id_user, data) => {
  console.log({ id_user, data });

  return prisma.tb_habits.create({
    data: {
      users_id: id_user,
      name_habit: data.name_habit,
      last_time_habit: new Date(data.last_time_habit),
    },
  });
};
