// 📦 Importa a função que retorna o modelo (tabela) correspondente à atividade
const getModel = require("../../functions/model");

// 🔍 Controlador genérico para buscar dados de atividades (meditação ou respiração)
const find = async (req, res) => {
  try {
    // 🔹 Extrai o ID do usuário autenticado e os parâmetros da rota
    const { id_user } = req.user;
    const { activity, id_activity } = req.params;

    // 🔹 Obtém dinamicamente o modelo do Prisma com base na atividade ("breath" ou "meditation")
    const model = getModel(activity);

    // 🔹 Define a cláusula de filtro específica conforme o tipo de atividade
    where =
      activity === "breath"
        ? { id_breath: Number(id_activity) }
        : { id_meditation: Number(id_activity) };

    let result;

    // 🔹 Se nenhum ID for passado, busca todas as atividades do tipo para o usuário
    if (!id_activity) {
      result = await model.findMany({
        where: {
          users_id: id_user,
        },
      });
    } else {
      // 🔹 Caso um ID de atividade seja passado, busca apenas o registro específico
      result = await model.findFirst({
        where: {
          ...where,
          users_id: id_user,
        },
      });
    }

    // 🔹 Retorna o(s) resultado(s) encontrado(s)
    res.status(200).json(result);
  } catch (error) {
    // 🔹 Tratamento de erro
    console.error({ error });

    return res.status(error.status || 500).json({ error: error.message });
  }
};

// 🚀 Exporta a função para uso nas rotas
module.exports = find;
