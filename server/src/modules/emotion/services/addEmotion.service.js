const { prisma } = require("../../../config/prisma");
const CustomError = require("../../../core/errors/CustomError");

module.exports.addEmotionService = async (
  id_user,
  { emotion, description, day_time }
) => {
  if (!emotion || !day_time)
    throw new CustomError("Emoção e período do dia são obrigatórios", 400);

  await prisma.tb_today.create({
    data: {
      users_id: id_user,
      emotion_today: emotion,
      description_today: description,
      morning_afternoon_evening: day_time,
    },
  });

  return { message: "Emoção registrada com sucesso" };
};
