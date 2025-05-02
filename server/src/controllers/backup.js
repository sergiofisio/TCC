const { prisma } = require("../prismaFunctions/prisma");
const fs = require("fs");
const path = require("path");
const { sendMail } = require("../functions/mailer");

// Controlador para fazer o backup do banco de dados e enviar por e-mail
async function backupDatabase(_, res) {
  try {
    const now = new Date();
    const THIRTY_DAYS_MS = 30 * 24 * 60 * 60 * 1000;

    //extrai os usuários ativos do banco de dados
    const activeUsers = await prisma.tb_users.findMany({
      where: { active_user: true },
    });

    // Atualiza o status dos usuários que não acessaram o aplicativo nos últimos 30 dias
    const updates = activeUsers.map(async (user) => {
      const lastLogin = user.last_login_date_user;
      if (lastLogin && now - new Date(lastLogin) > THIRTY_DAYS_MS) {
        await prisma.tb_users.update({
          where: { id_user: user.id_user },
          data: { active_user: false },
        });
      }
    });

    await Promise.all(updates);

    // Extrai os usuários ativos e inativos do banco de dados
    const backup = await prisma.tb_users.findMany({
      include: {
        phones_user: true,
        meditations_user: true,
        breaths_user: true,
        todays_user: true,
      },
    });

    //criação do arquivo de backup
    const dataString =
      "module.exports = users = " +
      JSON.stringify(backup, null, 2)
        .replace(/"([^"]+)":/g, "$1:")
        .replace(/\\n/g, "\n")
        .replace(/\\\"/g, '"')
        .replace(/\\\\/g, "\\") +
      ";\n";

    const outputPath = path.join(__dirname, "../../prisma/data.js");

    fs.writeFileSync(outputPath, dataString, "utf-8");

    // Envia o backup por e-mail
    await sendMail(
      "sergiobastosfisio@gmail.com",
      "Backup do Banco de Dados - Emotion Harmony",
      "<p>Segue em anexo o backup do banco de dados (.json).</p>",
      [
        {
          filename: "data.json",
          path: outputPath,
        },
      ]
    );

    console.log("Backup realizado e enviado com sucesso");
  } catch (error) {
    console.error({ error });
    res.status(error.status || 500).json({ error: error.message });
  } finally {
    await prisma.$disconnect();
    if (res) {
      res.json({ message: "Backup realizado e enviado com sucesso" });
    }
  }
}

module.exports = backupDatabase;
