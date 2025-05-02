// 📦 Importação das dependências necessárias
const { CustomError } = require("../../class/class");
const { prisma } = require("../../prismaFunctions/prisma");

// 🔧 Função para desativar ou reativar um usuário (soft delete)
const deleteUser = async (req, res) => {
  try {
    // 🔹 Coleta os dados da requisição
    const { id } = req.params;
    const { id_user } = req.user;
    const { active } = req.body;

    // 🔹 Evita que o usuário delete a si mesmo (caso o ID do token seja igual ao da URL)
    if (id && id === id_user) throw new CustomError("ação inválida", 400);

    // 🔹 Determina o ID do usuário alvo (ou o do próprio usuário autenticado, se não passado)
    const targetUserId = Number(id || id_user);

    // 🔹 Verifica se o usuário alvo existe no banco de dados
    const user = await prisma.tb_users.findUnique({
      where: { id_user: targetUserId },
    });

    if (!user) throw new CustomError("Usuário não encontrado", 404);

    // 🔹 Se o usuário for administrador, verifica se há pelo menos outro admin ativo
    if (user.type_user === "admin") {
      const activeAdmins = await prisma.tb_users.findMany({
        where: {
          type_user: "admin",
          active_user: true,
          NOT: { id_user: targetUserId }, // Exclui o admin que será desativado
        },
      });

      if (activeAdmins.length === 0) {
        throw new CustomError(
          "É necessário ter ao menos um administrador ativo no sistema.",
          400
        );
      }
    }

    // 🔹 Atualiza o status de ativação do usuário (desativa ou reativa)
    const response = await prisma.tb_users.update({
      where: {
        id_user: targetUserId,
      },
      data: {
        active_user: active,
      },
    });

    // 🔹 Resposta de sucesso
    return res.json({ delete: true });
  } catch (error) {
    // 🔹 Tratamento de erros
    console.error({ error });

    return res.status(error.status || 500).json({ error: error.message });
  }
};

// 🚀 Exporta a função para uso nas rotas
module.exports = deleteUser;
