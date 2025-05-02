// 📦 Importações necessárias
const { CustomError } = require("../../class/class");
const { prisma } = require("../../prismaFunctions/prisma");

// 🔧 Controlador para obter informações gerais do banco de dados (acesso exclusivo para administradores)
async function getInfoDb(req, res) {
  try {
    // 🔹 Extrai o ID do usuário autenticado (via token)
    const { id_user } = req.user;

    // 🔹 Busca os dados completos do usuário no banco, incluindo relacionamentos
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

    // 🔹 Verifica se o usuário existe
    if (!userInfo) throw new CustomError("Usuário não encontrado", 404);

    // 🔹 Permite acesso apenas se o usuário for do tipo 'admin'
    if (userInfo.type_user !== "admin")
      throw new CustomError("Usuário não autorizado", 403);

    // 🔹 Busca todos os outros usuários do sistema (exceto o atual)
    const users = await prisma.tb_users.findMany({
      where: {
        NOT: {
          id_user: Number(id_user),
        },
      },
      include: {
        phones_user: true,
        meditations_user: true,
        breaths_user: true,
        todays_user: true,
      },
    });

    // 🔹 Busca registros auxiliares no banco
    const breaths = await prisma.tb_breath.findMany();
    const meditations = await prisma.tb_meditation.findMany();
    const emotions = await prisma.tb_today.findMany();

    // 🔹 Retorna todas as informações agrupadas
    return res.status(200).json({ users, breaths, meditations, emotions });
  } catch (error) {
    // 🔹 Tratamento de erro
    console.error({ error });

    return res.status(error.status || 500).json({ error: error.message });
  }
}

// 🚀 Exporta a função para uso nas rotas
module.exports = getInfoDb;
