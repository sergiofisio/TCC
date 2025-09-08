const path = require("path");
const fs = require("fs");
const sendMail = require("../utils/sendMail");
const { prisma } = require("../config/prisma");
const nodemailer = require("nodemailer");

const transporter = nodemailer.createTransport({
  service: process.env.SERVICE_EMAIL,
  auth: {
    user: process.env.SMTP_USER,
    pass: process.env.SMTP_PASS,
  },
});

async function performBackup() {
  try {
    const users = await prisma.tb_users.findMany({
      include: {
        phone_user: true,
        today_user: true,
        tb_meditation: true,
        breath_user: true,
        habits_user: true,
      },
    });
    const currentDate = new Date();
    const thirtyDaysAgo = new Date(
      currentDate.setDate(currentDate.getDate() - 30)
    );

    for (const user of users) {
      if (user.last_login_date_user === null) {
        await prisma.tb_users.update({
          where: { id_user: user.id_user },
          data: { active_user: false },
        });
        console.log(`Usuário ${user.name_user} inativado por nunca acessar.`);
        continue;
      }
      if (user.last_login_date_user < thirtyDaysAgo) {
        await prisma.tb_users.update({
          where: { id_user: user.id_user },
          data: { active_user: false },
        });
        console.log(
          `Usuário ${user.name_user} inativado por não acessar nos últimos 30 dias.`
        );
      }
    }

    console.log("🔄 Iniciando backup do banco de dados...");
    const backupData = { users };
    const backupPath = path.join(__dirname, "../../backup/backup.json");

    fs.writeFileSync(backupPath, JSON.stringify(backupData));

    const date = new Date().toISOString().replace(/:/g, "-");

    const mailOptions = {
      from: `"emotion harmony Backup" <${process.env.SMTP_USER}>`,
      to: "sergiobastosfisio@yahoo.com.br",
      subject: `Database Backup - Emotion harmony DATA: ${date}`,
      text: "Please find attached the latest database backup.",
      attachments: [{ path: backupPath }],
    };

    transporter.sendMail(mailOptions, (error, info) => {
      if (error) {
        console.error("❌ Erro ao enviar o e-mail de backup:", error.message);
      } else {
        console.log("✅ E-mail de backup enviado com sucesso!");
        console.log("📤 Resposta do servidor de e-mail:", info.response);
        console.log("📧 ID da mensagem:", info.messageId);

        fs.writeFileSync(backupPath, "");
        console.log("🗑️ Arquivo de backup esvaziado com sucesso.");
      }
    });
    console.log("Backup completed.");
    return;
  } catch (err) {
    console.error("Erro no backup:", err);
    throw err;
  }
}

module.exports = performBackup;
