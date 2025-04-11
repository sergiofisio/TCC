const { prisma } = require("../../prismaFunctions/prisma");
const { sendMail } = require("../../functions/mailer");
const { generateToken } = require("../../functions/token");
const { CustomError } = require("../../class/class");

async function sendRcovery(req, res) {
  try {
    const { email } = req.body;

    if (!email) throw new CustomError("Campo obrigatório ausente: email", 400);

    const user = await prisma.tb_users.findUnique({
      where: {
        email_user: email,
      },
    });

    if (!user) return;

    const token = await generateToken({ id_user: user.id_user });
    const link = `${process.env.FRONTEND_URL}/lostpassword/${user.id_user}/${token}`;
    const subject = "Recuperação de senha";

    const html = `
      <h2>Olá, ${user.name_user}!</h2>
      <p>Recebemos uma solicitação para redefinir sua senha.</p>
      <p>Clique no botão abaixo para criar uma nova senha:</p>
      <div style="text-align: center; margin: 30px 0;">
        <a href="${link}" style="background-color: #213e70; color: white; padding: 12px 24px; text-decoration: none; border-radius: 6px;">Redefinir Senha</a>
      </div>
      <p>Se você não solicitou a recuperação, apenas ignore este e-mail.</p>
      <hr />
      <p style="font-size: 12px; color: #aaa;">© ${new Date().getFullYear()} - Sua Empresa. Todos os direitos reservados.</p>
    `;

    await prisma.tb_users.update({
      where: { id_user: user.id_user },
      data: { lost_pasword_token_user: token },
    });

    await sendMail(user.email_user, subject, html);
    return res.json({ send: true });
  } catch (error) {
    console.error({ error });
    return res.status(error.status || 500).json({ error: error.message });
  }
}

module.exports = sendRcovery;
