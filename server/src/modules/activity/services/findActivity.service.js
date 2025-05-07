const { prisma } = require("../../../config/prisma");
const CustomError = require("../../../core/errors/CustomError");

const models = {
  breath: prisma.tb_breath,
  meditate: prisma.tb_meditation,
};

module.exports.findActivityService = async (id_user, params) => {
  const model = models[params.activity];

  if (!model) throw new CustomError("Atividade inválida", 400);

  const id = Number(params.id) || null;

  let where;
  if (id) {
    const idField =
      params.activity === "meditate" ? "id_meditation" : "id_breath";

    where = {
      users_id: id_user,
      [idField]: id,
    };

    const data = await model.findUnique({
      where,
    });
    if (!data) throw new CustomError("Atividade não encontrada", 404);
    return data;
  }

  const data = await model.findMany({
    where: {
      users_id: id_user,
    },
  });

  return data;
};
