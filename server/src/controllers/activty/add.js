const { CustomError } = require("../../class/class");
const getModel = require("../../functions/model");


const add = async (req, res) => {
    try {
        const { id_user } = req.user;
        const { activity } = req.params;
        const data = req.body;

        if (activity !== "breath" && activity !== "meditate") throw new CustomError("Atividade inválida", 400);

        const model = getModel(activity);

        const result = await model.create({
            data: {
                users_id: id_user,
                ...data,
            },
        });

        return res.status(201).json(result);

    } catch (error) {
        console.error({ error });

        return res.status(error.status || 500).json({ error: error.message });
    }
}

module.exports = add;