const { CustomError } = require("../../class/class");
const generateToken = require("../../functions/token");
const { verifyInput } = require("../../functions/verify");
const { prisma } = require("../../prismaFunctions/prisma");
const bcrypt = require("bcrypt");

async function login(req, res) {
    try {
        const { email, senha } = req.body;

        const missingInput = verifyInput({ email, senha });

        if (missingInput) throw new CustomError(`Campo obrigatório ausente: ${missingInput}`, 400);

        const { id_user, password_user } = await prisma.tb_users.findFirst({
            where: {
                email_user: email,
            },
        });


        const match = await bcrypt.compare(senha, password_user);

        if (!id_user || !match) throw new CustomError("Email e/ou Senha incorreta", 401);

        const token = generateToken(id_user);

        return res.status(200).json({ message: "Login efetuado com sucesso!", token, id: id_user });

    } catch (error) {
        console.error({ error });

        return res.status(error.status || 500).json({ error: error.message });
    }
}

module.exports = login;