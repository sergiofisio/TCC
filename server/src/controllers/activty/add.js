// 📦 Importações necessárias
const { CustomError } = require("../../class/class");
const getModel = require("../../functions/model");

// ➕ Controlador responsável por adicionar uma nova atividade (respiração ou meditação)
const add = async (req, res) => {
  try {
    // 🔹 Extrai o ID do usuário autenticado
    const { id_user } = req.user;

    // 🔹 Extrai o tipo da atividade (ex: "breath" ou "meditate") e os dados enviados no corpo da requisição
    const { activity } = req.params;
    const data = req.body;

    // 🔹 Valida se o tipo de atividade é permitido
    if (activity !== "breath" && activity !== "meditate")
      throw new CustomError("Atividade inválida", 400);

    // 🔹 Obtém dinamicamente o modelo do Prisma com base na atividade
    const model = getModel(activity);

    // 🔹 Cria o novo registro da atividade no banco, vinculando ao usuário
    const result = await model.create({
      data: {
        users_id: id_user,
        ...data,
      },
    });

    // 🔹 Retorna sucesso com o objeto criado
    return res.status(201).json(result);
  } catch (error) {
    // 🔹 Tratamento de erro
    console.error({ error });

    return res.status(error.status || 500).json({ error: error.message });
  }
};

// 🚀 Exporta a função para uso nas rotas
module.exports = add;
