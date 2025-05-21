const { prisma } = require("../../../config/prisma");
const CustomError = require("../../../core/errors/CustomError");

module.exports.getUserInfoService = async (id_user) => {
  try {
    const user = await prisma.tb_users.findUnique({
      where: { id_user },
      include: {
        phone_user: true,
        meditation_user: true,
        breath_user: true,
        today_user: true,
      },
    });

    if (!user) throw new CustomError("Usuário não encontrado", 404);

    delete user.password_user;
    delete user.lost_pasword_token_user;

    return user;
  } catch (error) {
    console.log({ error });
  }
};
