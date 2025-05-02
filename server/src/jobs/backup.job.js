const schedule = require("node-schedule");
const path = require("path");
const fs = require("fs");
const archiver = require("archiver");
const sendMail = require("../utils/sendMail");

const prisma = require("../config/prisma");

module.exports = function startBackupJob() {
  schedule.scheduleJob("0 11 * * *", async () => {
    try {
      const backupPath = path.join(
        __dirname,
        "../../backup",
        `backup-${Date.now()}.zip`
      );
      const output = fs.createWriteStream(backupPath);
      const archive = archiver("zip", { zlib: { level: 9 } });

      archive.pipe(output);
      archive.directory(path.join(__dirname, "../../prisma"), "prisma");
      await archive.finalize();

      output.on("close", async () => {
        const html = "<p>Backup do banco realizado com sucesso.</p>";
        await sendMail(
          process.env.ADMIN_EMAIL,
          "Backup do Banco de Dados - Emotion Harmony",
          html,
          [{ filename: "backup.zip", path: backupPath }]
        );
        console.log("✅ Backup enviado com sucesso");
      });
    } catch (err) {
      console.error("Erro no backup:", err);
    }
  });
};
