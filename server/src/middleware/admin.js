const { prisma } = require("../prismaFunctions/prisma");
const { CustomError } = require("../class/class");


const verifyAdmin = (req, res, next) => {
    try {
        const user = req.user;

        const userInfo = prisma.tb_users.findUnique({
            where: {
                id_user: Number(user.id_user)
            }
        })

        if (userInfo.type_user !== 'admin') throw new CustomError("Acesso negado", 403);

        next();
    } catch (error) {
        console.error({ error });

        return res.status(error.status || 500).json({ error: error.message });

    }
};

module.exports = verifyAdmin;