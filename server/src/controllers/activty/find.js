const getModel = require("../../functions/model");


const find = async (req, res) => {
    console.log("find");

    try {
        const { id_user } = req.user;
        const { activity, id_activity } = req.params;

        const model = getModel(activity);

        where = activity === "breath" ? { id_breath: Number(id_activity) } : { id_meditation: Number(id_activity) };

        let result;
        if (!id_activity) {
            result = await model.findMany({
                where: {
                    users_id: id_user
                }
            })
        } else {
            result = await model.findFirst({
                where: {
                    ...where,
                    users_id: id_user
                }
            });
        }
        res.status(200).json(result);
    } catch (error) {
        console.error({ error });

        return res.status(error.status || 500).json({ error: error.message });
    }
}

module.exports = find;