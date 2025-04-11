const { CustomError } = require("../../class/class");
const { prisma } = require("../../prismaFunctions/prisma");

const deleteUser = async (req, res) => {
  try {
    const { id } = req.params;
    const { id_user } = req.user;
    const { active } = req.body;

    if (id && id === id_user) throw new CustomError("ação inválida", 400);

    const targetUserId = Number(id || id_user);

    const user = await prisma.tb_users.findUnique({
      where: { id_user: targetUserId },
    });

    if (!user) throw new CustomError("Usuário não encontrado", 404);

    if (user.type_user === "admin") {
      const activeAdmins = await prisma.tb_users.findMany({
        where: {
          type_user: "admin",
          active_user: true,
          NOT: { id_user: targetUserId },
        },
      });

      if (activeAdmins.length === 0) {
        throw new CustomError(
          "É necessário ter ao menos um administrador ativo no sistema.",
          400
        );
      }
    }
    const response = await prisma.tb_users.update({
      where: {
        id_user: targetUserId,
      },
      data: {
        active_user: active,
      },
    });

    return res.json({ delete: true });
  } catch (error) {
    console.error({ error });

    return res.status(error.status || 500).json({ error: error.message });
  }
};

module.exports = deleteUser;
