// 📦 Importações necessárias
const { CustomError } = require("../class/class");
const { prisma } = require("../prismaFunctions/prisma");

// 🧠 Função utilitária que retorna o modelo do Prisma correspondente à atividade solicitada
const getModel = (activity) => {
  // 🔹 Verifica se a atividade fornecida é válida
  if (activity !== "breath" && activity !== "meditate")
    throw new CustomError("Atividade inválida", 400);

  // 🔹 Retorna o modelo do Prisma de acordo com o tipo de atividade
  if (activity === "breath") {
    return prisma.tb_breath;
  } else if (activity === "meditate") {
    return prisma.tb_meditation;
  }
};

// 🚀 Exporta a função para uso em controladores e middlewares
module.exports = getModel;
