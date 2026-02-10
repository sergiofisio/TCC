const { prisma } = require("../../../config/prisma");
const bcrypt = require("bcrypt");
const CustomError = require("../../../core/errors/CustomError");
const {
  verifyEmail,
  verifyCPF,
  verifyInput,
} = require("../../../utils/validation");

module.exports.registerUserService = async (data) => {
  let { nome, email, cpf, senha, telefones = [] } = data;
  console.log({ nome, email, cpf, senha, telefones });

  const missing = verifyInput({ nome, email, cpf, senha });
  if (missing)
    throw new CustomError(`Campo obrigatório ausente: ${missing}`, 400);

  if (!verifyEmail(email)) throw new CustomError("Email inválido", 400);
  if (!verifyCPF(cpf)) throw new CustomError("CPF inválido", 400);

  cpf = cpf.replace(/\D/g, "");

  const existisEmail = await prisma.tb_users.findUnique({
    where: { email_user: email },
  });

  const existingCPF = await prisma.tb_users.findUnique({
    where: { cpf_user: cpf },
  });

  if (existisEmail) throw new CustomError("Email já cadastrado", 409);

  if (existingCPF) throw new CustomError("CPF já cadastrado", 409);

  const hash = await bcrypt.hash(senha, 10);

  await prisma.tb_users.create({
    data: {
      name_user: nome,
      email_user: email,
      cpf_user: cpf,
      password_user: hash,
      phone_user: {
        create: telefones.map((tel) => ({
          type_phone: tel.tipo,
          country_code_phone: 55,
          area_code_phone: Number(tel.telefone.replace(/\D/g, "").slice(0, 2)),
          phone_number: Number(tel.telefone.replace(/\D/g, "").slice(2)),
        })),
      },
    },
  });

  return { message: "Usuário cadastrado com sucesso!" };
};
