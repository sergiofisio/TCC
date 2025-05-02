const { prisma } = require("../config/prisma");

module.exports = {
  breath: prisma.tb_breath,
  meditate: prisma.tb_meditation,
};
