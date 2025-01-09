const bcrypt = require("bcrypt");
const { CustomError } = require("../../class/class");
const { prisma } = require("../../prismaFunctions/prisma");

async function register(req, res) {
  try {
    let { name, email, cpf, phone, password } = req.body;

    password = await bcrypt.hash(password, 10);

    phone = phone.replace(/\D/g, "");

    const findUser = await prisma.tb_users.findUnique({
      where: {
        OR: [{ email }, { cpf }],
      },
    });

    if (findUser) {
      throw new CustomError("Usuário já cadastrado", 409);
    }

    const { id } = await prisma.tb_users.create({
      data: {
        name,
        email,
        cpf,
        phones_user: {
          create: phone.map((ph) => ({
            country_code: ph.slice(0, 2),
            area_code: ph.slice(2, 4),
            number: ph.slice(4),
          })),
        },
        password,
      },
    });
  } catch (error) {
    console.error({ error });

    if (error.missingInput)
      return res
        .status(error.status || 500)
        .json({ missingInput: error.missingInput });
    return res.status(error.status || 500).json({ error: error.message });
  }
}

module.exports = register;
