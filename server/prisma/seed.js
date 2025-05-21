// 📦 Importações de módulos do Node.js e do Prisma
const fs = require("fs");
const path = require("path");
const { prisma } = require("../src/config/prisma");

// 🌱 Função responsável por executar todos os arquivos de seed da pasta "seeds"
async function seed() {
  try {
    // 🔹 Define o caminho da pasta onde estão os arquivos de seed
    const seedsPath = path.join(__dirname, "seeds");

    // 🔹 Lê todos os arquivos da pasta "seeds"
    const files = fs.readdirSync(seedsPath);

    // 🔹 Para cada arquivo, importa e executa a função de seed passando o Prisma Client
    for (const file of files) {
      const seedFilePath = path.join(seedsPath, file);
      const seed = require(seedFilePath);
      await seed(prisma); // Executa a função de seed
    }

    // 🔹 Encerra a conexão com o banco de dados
    await prisma.$disconnect();
  } catch (e) {
    // 🔹 Em caso de erro durante a execução dos seeds
    console.error(e);
  }
}

// 🚀 Executa a função seed e captura qualquer erro não tratado
seed().catch((e) => {
  console.error(e);
});
