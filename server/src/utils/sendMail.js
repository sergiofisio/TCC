// 📦 Importações
const nodemailer = require("nodemailer");
const CustomError = require("../core/errors/CustomError");

/**
 * Envia um e-mail usando o serviço SMTP configurado (Gmail por padrão).
 *
 * @param {string} to - E-mail de destino
 * @param {string} subject - Assunto do e-mail
 * @param {string} html - Corpo do e-mail em HTML
 * @param {Array} attachments - Lista de anexos opcionais
 */
async function sendMail(to, subject, html, attachments = []) {
  try {
    // 🔧 Configura o transporte usando Gmail e variáveis de ambiente
    const transporter = nodemailer.createTransport({
      service: process.env.SERVICE_EMAIL,
      auth: {
        user: process.env.SMTP_USER,
        pass: process.env.SMTP_PASS,
      },
    });

    // 📧 Monta os dados do e-mail
    const mailOptions = {
      from: `"Emotion Harmony" <${process.env.SMTP_USER}>`,
      to,
      subject,
      html,
      attachments,
    };

    // 📤 Envia o e-mail
    await transporter.sendMail(mailOptions);
    console.log(`📨 E-mail enviado para ${to}`);
  } catch (err) {
    console.error("❌ Erro ao enviar e-mail:", err);
    throw new CustomError("Erro ao enviar e-mail", 500);
  }
}

module.exports = sendMail;
