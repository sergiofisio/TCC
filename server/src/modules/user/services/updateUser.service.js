const prisma = require("../../../config/prisma");
const { verifyEmail, verifyCPF } = require("../../../utils/validation");
const CustomError = require("../../../core/errors/CustomError");
const bcrypt = require("bcrypt");

module.exports.updateUserService = async (id_user, body) => {
  const { nome, email, cpf, senha } = body;
  const user = await prisma.tb_users.findUnique({ where: { id_user } });
  if (!user) throw new CustomError("Usuário não encontrado", 404);

  const data = {};
  if (nome && nome !== user.name_user) data.name_user = nome;
  if (email && email !== user.email_user) {
    if (!verifyEmail(email)) throw new CustomError("Email inválido", 400);
    data.email_user = email;
  }
  if (cpf && cpf !== user.cpf_user) {
    if (!verifyCPF(cpf)) throw new CustomError("CPF inválido", 400);
    data.cpf_user = cpf.replace(/\D/g, "");
  }
  if (senha) data.password_user = await bcrypt.hash(senha, 10);

  return prisma.tb_users.update({ where: { id_user }, data });
};
