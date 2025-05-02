// 📦 Importação do módulo nodemailer para envio de e-mails
const nodemailer = require("nodemailer");
const path = require("path");
const fs = require("fs");
const { resend } = require("./resendConfig"); // Presume que você tem um arquivo separado com a instância do Resend

// 📧 Função para envio de e-mails genéricos (ex: recuperação de senha, notificações)
async function sendMail(to, subject, html, attachments = []) {
  // 🔹 Cria o transporte SMTP usando credenciais do Gmail
  const transporter = nodemailer.createTransport({
    service: process.env.SERVICE_EMAIL,
    auth: {
      user: process.env.SMTP_USER,
      pass: process.env.SMTP_PASS,
    },
  });

  // 🔹 Define as opções do e-mail a ser enviado
  const mailOptions = {
    from: `"Suporte - Emotion Harmony" <${process.env.SMTP_USER}>`, // Nome + e-mail do remetente
    to, // Destinatário
    subject, // Assunto
    html, // Corpo do e-mail em HTML
    attachments, // Anexos (opcional)
  };

  // 📤 Envia o e-mail com as opções configuradas
  await transporter.sendMail(mailOptions);
}

// 💾 Função para envio de backup de banco de dados utilizando o serviço Resend
async function sendBackup(toEmail, filePath) {
  try {
    // 🔹 Obtém o nome do arquivo e o conteúdo em base64
    const fileName = path.basename(filePath);
    const fileBuffer = fs.readFileSync(filePath);

    // 📤 Envia o e-mail com o backup como anexo
    await resend.emails.send({
      from: "Emotion Harmony <no-reply@emotionharmony.com>", // Remetente padrão do Resend
      to: toEmail, // Destinatário
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

    console.log("✅ Backup enviado com sucesso via Resend.");
  } catch (error) {
    // ❌ Em caso de erro durante o envio
    console.error("Erro ao enviar backup com Resend:", error);
    throw error;
  }
}

// 🚀 Exporta as funções para uso em outros módulos
module.exports = { sendMail, sendBackup };
