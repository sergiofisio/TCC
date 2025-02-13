const bcrypt = require("bcrypt");
const { CustomError } = require("../../class/class");
const { prisma } = require("../../prismaFunctions/prisma");
const { verifyInput, verifyEmail, verifyCPF } = require("../../functions/verify");

async function register(req, res) {
  try {
    let { nome, email, cpf, telefones, senha } = req.body;

    const missingInput = verifyInput({ nome, email, cpf, telefones, senha });

    if (missingInput) throw new CustomError(`Campo obrigatório ausente: ${missingInput}`, 400);

    if (!verifyEmail(email)) throw new CustomError("Email inválido", 400);

    if (!verifyCPF(cpf)) throw new CustomError("CPF inválido", 400);

    senha = await bcrypt.hash(senha, 10);

    const findUser = await prisma.tb_users.findFirst({
      where: {
        OR: [{ email_user: email }, { cpf_user: cpf }],
      },
    });

    for (const item of telefones) {
      if (!item.tipo) throw new CustomError("Campo obrigatório ausente: Tipo Telefone", 400);

      item.telefone = item.telefone.replace(/\D/g, "");

      if (item.telefone.length < 10 || item.telefone.length > 11)
        throw new CustomError("Telefone inválido", 400);
    }
    if (findUser) {
      throw new CustomError("Usuário já cadastrado", 409);
    }

    await prisma.tb_users.create({
      data: {
        name_user: nome,
        email_user: email,
        cpf_user: cpf.replace(/\D/g, ""),
        phones_user: {
          create: telefones.map(({ tipo, telefone }) => ({
            type_phone: tipo,
            country_code_phone: telefone.pais || 55,
            area_code_phone: Number(telefone.slice(0, 2)),
            phone_number: Number(telefone.slice(2)),
          })),
        },
        password_user: senha,
      },
    });

    return res.status(201).json({ message: "Usuário cadastrado com sucesso!" });
  } catch (error) {

    return res.status(error.status || 500).json({ error: error.message });
  }
}

module.exports = register;
