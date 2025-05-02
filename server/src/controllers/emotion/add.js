// 📦 Importação do Prisma Client para acesso ao banco de dados
const { prisma } = require("../../prismaFunctions/prisma");

// 🔧 Controlador para registrar uma nova emoção do usuário
async function addEmotion(req, res) {
  try {
    // 🔹 Extrai o ID do usuário autenticado
    const { id_user } = req.user;

    // 🔹 Extrai os dados enviados no corpo da requisição
    const { emotion, description, day_time } = req.body;

    // 🔹 Verifica se os campos obrigatórios foram informados
    if (!emotion || !day_time) throw new CustomError("Dados incompletos", 400);

    // 🔹 Insere uma nova emoção na tabela `tb_today`
    await prisma.tb_today.create({
      data: {
        users_id: Number(id_user),
        emotion_today: emotion,
        description_today: description,
        morning_afternoon_evening: day_time,
      },
    });

    // 🔹 Retorna resposta de sucesso
    return res.status(201).json({ message: "Emoção adicionada com sucesso" });
  } catch (error) {
    // 🔹 Tratamento de erros
    console.error({ error });

    return res.status(error.status || 500).json({ error: error.message });
  }
}

// 🚀 Exporta a função para uso nas rotas
module.exports = addEmotion;
