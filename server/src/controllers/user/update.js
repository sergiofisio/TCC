const bcrypt = require("bcrypt");
const { CustomError } = require("../../class/class");
const { prisma } = require("../../prismaFunctions/prisma");
const { verifyEmail, verifyCPF } = require("../../functions/verify");

async function update(req, res) {
  try {
    const { id_user } = req.user;
    let { nome, email, cpf, senha, telefones } = req.body;

    const user = await prisma.tb_users.findUnique({
      where: { id_user },
    });

    if (!user) throw new CustomError("Usuário não encontrado", 404);

    if (email && email !== user.email_user) {
      if (!verifyEmail(email)) throw new CustomError("Email inválido", 400);

      const emailExists = await prisma.tb_users.findFirst({
        where: { email_user: email, id_user: { not: id_user } },
      });

      if (emailExists) throw new CustomError("Email já está em uso", 400);
    }

    if (cpf && cpf !== user.cpf_user) {
      if (!verifyCPF(cpf)) throw new CustomError("CPF inválido", 400);

      const cpfExists = await prisma.tb_users.findFirst({
        where: { cpf_user: cpf, id_user: { not: id_user } },
      });

      if (cpfExists) throw new CustomError("CPF já está em uso", 400);
    }

    if (senha && senha !== user.password_user) {
      senha = await bcrypt.hash(senha, 10);
    }

    const dataToUpdate = {};

    if (nome && nome !== user.name_user) dataToUpdate.name_user = nome;
    if (email && email !== user.email_user) dataToUpdate.email_user = email;
    if (cpf && cpf !== user.cpf_user)
      dataToUpdate.cpf_user = cpf.replace(/\D/g, "");
    if (senha) dataToUpdate.password_user = senha;
    if (telefones) {
      dataToUpdate.phones_user = {
        upsert: telefones.map((telefone) => ({
          where: { id_phone: telefone.id_phone || 0 },
          update: {
            type_phone: telefone.type_phone,
            country_code_phone: 55,
            area_code_phone: Number(
              telefone.telefone.replace(/\D/g, "").slice(0, 2)
            ),
            phone_number: Number(telefone.telefone.replace(/\D/g, "").slice(3)),
          },
          create: {
            type_phone: telefone.type_phone,
            country_code_phone: 55,
            area_code_phone: Number(
              telefone.telefone.replace(/\D/g, "").slice(0, 2)
            ),
            phone_number: Number(telefone.telefone.replace(/\D/g, "").slice(3)),
          },
        })),
      };
    }

    if (Object.keys(dataToUpdate).length === 0) {
      return res.status(200).json({ message: "Nenhuma alteração realizada." });
    }

    const updatedUser = await prisma.tb_users.update({
      where: { id_user: Number(id_user) },
      data: dataToUpdate,
    });

    return res.status(200).json(updatedUser);
  } catch (error) {
    console.error(error);
    return res.status(error.status || 500).json({ error: error.message });
  }
}

module.exports = update;
