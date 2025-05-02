// 📦 Importações necessárias
const { prisma } = require("../prismaFunctions/prisma");
const { CustomError } = require("../class/class");

// 🛡️ Middleware para verificar se o usuário autenticado é administrador
const verifyAdmin = async (req, res, next) => {
  try {
    // 🔹 Extrai o objeto `user` da requisição (definido pelo middleware de autenticação)
    const user = req.user;

    // 🔹 Busca as informações completas do usuário no banco de dados
    const userInfo = await prisma.tb_users.findUnique({
      where: {
        id_user: Number(user.id_user),
      },
    });

    // 🔐 Verifica se o tipo de usuário é diferente de "admin"
    if (userInfo.type_user !== "admin")
      throw new CustomError("Acesso negado", 403); // Retorna erro 403 (forbidden)

    // ✅ Se for admin, permite o prosseguimento para a próxima rota
    next();
  } catch (error) {
    // ❌ Tratamento de erro
    console.error({ error });

    return res.status(error.status || 500).json({ error: error.message });
  }
};

// 🚀 Exporta o middleware para ser usado em rotas que exigem permissão de administrador
module.exports = verifyAdmin;
