const { prisma } = require("../../../config/prisma");
const CustomError = require("../../../core/errors/CustomError");

const models = {
  breath: prisma.tb_breath,
  meditate: prisma.tb_meditation,
};

module.exports.addActivityService = async (id_user, activity, data) => {
  console.log({ activity });

  const model = models[activity];
  if (!model) throw new CustomError("Atividade inválida", 400);

  return model.create({
    data: {
      users_id: id_user,
      ...data,
    },
  });
};
