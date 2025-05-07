// 📦 Importa o PrismaClient da biblioteca do Prisma
const { PrismaClient } = require("@prisma/client");

// 🔧 Cria uma instância do Prisma Client para interagir com o banco de dados
const prisma = new PrismaClient();

// 🧹 Garante o encerramento da conexão com o banco quando o processo for finalizado
process.on("SIGINT", async () => {
  await prisma.$disconnect();
  process.exit(0);
});

// 🚀 Exporta o cliente Prisma para ser utilizado em outros arquivos do projeto
module.exports = {
  prisma,
};
