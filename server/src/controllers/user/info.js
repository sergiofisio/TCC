// Importação das classes e instâncias necessárias
const { CustomError } = require("../../class/class");
const { prisma } = require("../../prismaFunctions/prisma");

// Função para obter informações completas de um usuário
async function getUserInfo(req, res) {
  try {
    // 🔹 Identificação do usuário:
    // Se houver `id` nos parâmetros da rota, usa ele. Caso contrário, usa o `id_user` do token JWT
    let id_user = req.params.id || req.user.id_user;

    // 🔹 Busca no banco de dados:
    // Busca o usuário com base no ID e inclui informações relacionadas (relacionamentos do Prisma)
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

    // 🔹 Tratamento de erro caso o usuário não seja encontrado
    if (!userInfo) throw new CustomError("Usuário não encontrado", 404);

    // 🔹 Resposta de sucesso
    return res.status(200).json(userInfo);
  } catch (error) {
    // 🔹 Tratamento de erros
    console.error({ error });

    // Resposta de erro personalizada ou genérica
    return res.status(error.status || 500).json({ error: error.message });
  }
}

// Exporta a função para ser usada em rotas
module.exports = getUserInfo;
