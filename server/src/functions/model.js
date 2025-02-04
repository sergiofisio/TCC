const { CustomError } = require("../class/class");
const { prisma } = require("../prismaFunctions/prisma");

const getModel = (activity) => {
    if (activity !== "breath" && activity !== "meditate") throw new CustomError("Atividade inválida", 400);

    if (activity === "breath") {
        return prisma.tb_breath;
    } else if (activity === "meditate") {
        return prisma.tb_meditation;
    }
}

module.exports = getModel;