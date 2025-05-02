// 📦 Importações
const jwt = require("jsonwebtoken");
const { prisma } = require("../prismaFunctions/prisma");
const util = require("util");
const { CustomError } = require("../class/class");

// 🔧 Converte a função jwt.verify para usar async/await com Promises
const verifyAsync = util.promisify(jwt.verify);

// 🔐 Função para gerar um token JWT com ID do usuário
// time (opcional) define o tempo de expiração, padrão = 30 dias
const generateToken = (id, time) =>
  jwt.sign({ id }, process.env.JWT_SECRET, {
    expiresIn: time || "30d",
  });

// 🔍 Função para extrair o usuário a partir de um token JWT
const extractUserToken = async (token) => {
  try {
    // 🔹 Verifica e decodifica o token
    const { id } = await verifyAsync(token, process.env.JWT_SECRET);

    // 🔹 Busca o usuário correspondente no banco de dados
    const user = await prisma.tb_users.findUnique({
      where: { id_user: Number(id) },
    });

    // 🔹 Caso o usuário não seja encontrado, lança erro
    if (!user) throw new CustomError("Usuário e/ou Token inválido", 401);

    // 🔒 Remove dados sensíveis do objeto do usuário
    delete user.password_user;
    delete user.lost_pasword_token_user;

    // 🔹 Retorna os dados do usuário autenticado
    return user;
  } catch (err) {
    // ❌ Lança erro em caso de token inválido ou falha na extração
    throw new CustomError("Token inválido ou erro na extração do usuário", 401);
  }
};

// 🚀 Exporta as funções para uso em middlewares e controladores
module.exports = { generateToken, extractUserToken };
