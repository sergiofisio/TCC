const nodemailer = require("nodemailer");

async function sendMail(to, subject, html, attachments = []) {
  const transporter = nodemailer.createTransport({
    service: "gmail",
    auth: {
      user: process.env.SMTP_USER,
      pass: process.env.SMTP_PASS,
    },
  });

  const mailOptions = {
    from: `"Suporte - Emotion Harmony" <${process.env.SMTP_USER}>`,
    to,
    subject,
    html,
    attachments,
  };

  await transporter.sendMail(mailOptions);
}

async function sendBackup(toEmail, filePath) {
  try {
    const fileName = path.basename(filePath);
    const fileBuffer = fs.readFileSync(filePath);

    await resend.emails.send({
      from: "Emotion Harmony <no-reply@emotionharmony.com>",
      to: toEmail,
      subject: "Backup do Banco de Dados - Emotion Harmony",
      html: "<p>Segue em anexo o backup do banco de dados realizado com sucesso.</p>",
      attachments: [
        {
          filename: fileName,
          content: fileBuffer.toString("base64"),
          type: "text/plain",
          disposition: "attachment",
        },
      ],
    });

    console.log("Backup enviado com sucesso via Resend.");
  } catch (error) {
    console.error("Erro ao enviar backup com Resend:", error);
    throw error;
  }
}

module.exports = { sendMail, sendBackup };
