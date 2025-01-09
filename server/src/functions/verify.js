const { PrismaClient } = require("@prisma/client");

const prisma = new PrismaClient();

const verifyInput = (inputs) => {
    for (let input in inputs) {
        if (!inputs[input]) {
            return { missingInput: input };
        }
    }
    return false;
};

module.exports = { verifyInput };
