const { prisma } = require("../prismaFunctions/prisma");
const fs = require("fs");

async function backupDatabase(_, res) {
  let backup;

  try {
    backup = await prisma.tb_users.findMany({
      include: {
        phones_user: true,
        meditations_user: true,
        breaths_user: true,
        todays_user: true,
      },
    });

    fs.writeFileSync("../../backup/DBTCC/backup.json", JSON.stringify(backup));

    res.status(200).json({ message: "Backup realizado com sucesso" });
  } catch (error) {
    console.error({ error });
    res.status(error.status || 500).json({ error: error.message });
  }
}

module.exports = backupDatabase;
