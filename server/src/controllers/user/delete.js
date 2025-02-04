const { CustomError } = require("../../class/class");


const deleteUser = async (req, res) => {
    try {
        const { id } = req.params;
        const { id_user } = req.user;

        if (id === id_user) throw new CustomError("ação inválida", 400);

        await prisma.tb_users.delete({
            where: {
                id_user: Number(id)
            }
        });
    } catch (error) {
        console.error({ error });

        return res.status(error.status || 500).json({ error: error.message });
    }
}

module.exports = deleteUser;