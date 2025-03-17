const { CustomError } = require("../../class/class");
const { prisma } = require("../../prismaFunctions/prisma");

async function getUserInfo(req, res) {
  try {
    const { id_user } = req.user;

    const userInfo = await prisma.tb_users.findUnique({
      where: {
        id_user: Number(id_user),
      },
      include: {
        phones_user: true,
        meditations_user: true,
        breaths_user: true,
        todays_user: true,
      },
    });

    if (!userInfo) throw new CustomError("Usuário não encontrado", 404);

    return res.status(200).json(userInfo);
  } catch (error) {
    console.error({ error });

    return res.status(error.status || 500).json({ error: error.message });
  }
}

module.exports = getUserInfo;
