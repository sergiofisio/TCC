const { prisma } = require("../../prismaFunctions/prisma");

async function addEmotion(req, res) {
  try {
    const { id_user } = req.user;
    const { emotion, description, day_time } = req.body;

    if (!emotion || !day_time) throw new CustomError("Dados incompletos", 400);

    await prisma.tb_today.create({
      data: {
        users_id: Number(id_user),
        emotion_today: emotion,
        description_today: description,
        morning_afternoon_evening: day_time,
      },
    });

    return res.status(201).json({ message: "Emoção adicionada com sucesso" });
  } catch (error) {
    console.error({ error });

    return res.status(error.status || 500).json({ error: error.message });
  }
}

module.exports = addEmotion;
