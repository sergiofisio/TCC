// 📦 Importações necessárias
const { prisma } = require("../../prismaFunctions/prisma");
const { sendMail } = require("../../functions/mailer");
const { generateToken } = require("../../functions/token");
const { CustomError } = require("../../class/class");

// 🔧 Função responsável por enviar o e-mail de recuperação de senha
async function sendRcovery(req, res) {
  try {
    // 🔹 Extrai o e-mail enviado no corpo da requisição
    const { email } = req.body;

    // 🔹 Verifica se o campo foi informado
    if (!email) throw new CustomError("Campo obrigatório ausente: email", 400);

    // 🔹 Busca o usuário no banco de dados com base no e-mail
    const user = await prisma.tb_users.findUnique({
      where: {
        email_user: email,
      },
    });

    // 🔹 Caso o usuário não exista, retorna silenciosamente (boa prática de segurança)
    if (!user) return;

    // 🔹 Gera o token de recuperação com validade de 1 hora
    const token = generateToken(Number(user.id_user), "1h");

    // 🔹 Monta o link de redefinição com base no token e no ID do usuário
    const link = `${process.env.FRONTEND_URL}/lostpassword/${user.id_user}/${token}`;

    // 🔹 Define o assunto do e-mail
    const subject = "Recuperação de senha";

    // 🔹 Cria o HTML do e-mail a ser enviado
    const html = `
      <h2>Olá, ${user.name_user}!</h2>
      <p>Recebemos uma solicitação para redefinir sua senha.</p>
      <p>Clique no botão abaixo para criar uma nova senha:</p>
      <div style="text-align: center; margin: 30px 0;">
        <a href="${link}" style="background-color: #213e70; color: white; padding: 12px 24px; text-decoration: none; border-radius: 6px;">Redefinir Senha</a>
      </div>
      <p>Se você não solicitou a recuperação, faça a mudança da sua senha mesmo assim.</p>
      <hr />
      <p style="font-size: 12px; color: #aaa;">© ${new Date().getFullYear()} - Sua Empresa. Todos os direitos reservados.</p>
    `;

    // 🔹 Atualiza o banco com o token de recuperação vinculado ao usuário
    await prisma.tb_users.update({
      where: { id_user: user.id_user },
      data: { lost_pasword_token_user: token },
    });

    // 🔹 Envia o e-mail de recuperação com link e mensagem personalizada
    await sendMail(user.email_user, subject, html);

    // 🔹 Responde com sucesso
    return res.json({ send: true });
  } catch (error) {
    // 🔹 Tratamento de erros
    console.error({ error });
    return res.status(error.status || 500).json({ error: error.message });
  }
}

// 🚀 Exporta a função para uso nas rotas
module.exports = sendRcovery;
