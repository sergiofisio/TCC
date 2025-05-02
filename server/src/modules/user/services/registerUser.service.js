const prisma = require("../../../config/prisma");
const bcrypt = require("bcrypt");
const CustomError = require("../../../core/errors/CustomError");
const {
  verifyEmail,
  verifyCPF,
  verifyInput,
} = require("../../../utils/validation");

module.exports.registerUserService = async (data) => {
  const { nome, email, cpf, senha, telefones = [] } = data;

  const missing = verifyInput({ nome, email, cpf, senha });
  if (missing)
    throw new CustomError(`Campo obrigatório ausente: ${missing}`, 400);

  if (!verifyEmail(email)) throw new CustomError("Email inválido", 400);
  if (!verifyCPF(cpf)) throw new CustomError("CPF inválido", 400);

  const existing = await prisma.tb_users.findFirst({
    where: { OR: [{ email_user: email }, { cpf_user: cpf }] },
  });
  if (existing) throw new CustomError("Email ou CPF já cadastrado", 409);

  const hash = await bcrypt.hash(senha, 10);

  await prisma.tb_users.create({
    data: {
      name_user: nome,
      email_user: email,
      cpf_user: cpf.replace(/\D/g, ""),
      password_user: hash,
      phones_user: {
        create: telefones.map((tel) => ({
          type_phone: tel.tipo,
          country_code_phone: 55,
          area_code_phone: Number(tel.telefone.slice(0, 2)),
          phone_number: Number(tel.telefone.slice(2)),
        })),
      },
    },
  });

  return { message: "Usuário cadastrado com sucesso!" };
};
