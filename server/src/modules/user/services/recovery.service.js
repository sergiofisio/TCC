const prisma = require("../../../config/prisma");
const { generateToken } = require("../../../config/jwt");
const sendMail = require("../../../utils/sendMail");

module.exports.recoveryService = async (email) => {
  const user = await prisma.tb_users.findUnique({
    where: { email_user: email },
  });
  if (!user) return;

  const token = generateToken(user.id_user, "1h");
  const link = `${process.env.FRONTEND_URL}/lostpassword/${user.id_user}/${token}`;

  const html = `
    <p>Olá, ${user.name_user}</p>
    <p>Clique no botão abaixo para redefinir sua senha:</p>
    <a href="${link}" style="padding: 10px; background: #2c3e50; color: white;">Redefinir Senha</a>
  `;

  await prisma.tb_users.update({
    where: { id_user: user.id_user },
    data: { lost_pasword_token_user: token },
  });

  await sendMail(email, "Recuperação de Senha - Emotion Harmony", html);
};
