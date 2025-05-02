// 📦 Importações necessárias
const bcrypt = require("bcrypt");
const { CustomError } = require("../../class/class");
const { prisma } = require("../../prismaFunctions/prisma");
const {
  verifyInput,
  verifyEmail,
  verifyCPF,
} = require("../../functions/verify");

// 🔧 Controlador responsável pelo cadastro de um novo usuário
async function register(req, res) {
  try {
    // 🔹 Extrai os dados enviados no corpo da requisição
    let { nome, email, cpf, telefones, senha } = req.body;

    // 🔹 Verifica se todos os campos obrigatórios estão presentes
    const missingInput = verifyInput({ nome, email, cpf, telefones, senha });

    if (missingInput)
      throw new CustomError(`Campo obrigatório ausente: ${missingInput}`, 400);

    // 🔹 Valida o formato do e-mail e CPF
    if (!verifyEmail(email)) throw new CustomError("Email inválido", 400);
    if (!verifyCPF(cpf)) throw new CustomError("CPF inválido", 400);

    // 🔹 Aplica hash na senha antes de salvar no banco
    senha = await bcrypt.hash(senha, 10);

    // 🔹 Verifica se o e-mail ou CPF já estão cadastrados no banco
    const findUser = await prisma.tb_users.findFirst({
      where: {
        OR: [{ email_user: email }, { cpf_user: cpf }],
      },
    });

    // 🔹 Validações específicas dos telefones
    for (const item of telefones) {
      // Tipo de telefone obrigatório
      if (!item.tipo)
        throw new CustomError("Campo obrigatório ausente: Tipo Telefone", 400);

      // Remove caracteres não numéricos do número
      item.telefone = item.telefone.replace(/\D/g, "");

      // Verifica o tamanho do número (mínimo 10 e máximo 11 dígitos)
      if (item.telefone.length < 10 || item.telefone.length > 11)
        throw new CustomError("Telefone inválido", 400);
    }

    // 🔹 Impede o cadastro caso o usuário já exista
    if (findUser) {
      throw new CustomError("Usuário já cadastrado", 409);
    }

    // 🔹 Criação do novo usuário no banco de dados
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

    // 🔹 Retorna sucesso
    return res.status(201).json({ message: "Usuário cadastrado com sucesso!" });
  } catch (error) {
    // 🔹 Tratamento de erro genérico
    return res.status(error.status || 500).json({ error: error.message });
  }
}

// 🚀 Exporta a função para uso em rotas
module.exports = register;
