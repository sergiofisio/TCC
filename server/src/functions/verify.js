const { PrismaClient } = require("@prisma/client");

const prisma = new PrismaClient();

const verifyInput = (inputs) => {
    for (let input in inputs) {
        if (!inputs[input]) {
            return input;
        }
    }
    return false;
};

const verifyEmail = (email) => {
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
        return false;
    }
    return true;
}

const verifyCPF = (cpf) => {
    cpf = cpf.replace(/\D/g, '');
    if (cpf.length !== 11) return false;

    if (/^(\d)\1{10}$/.test(cpf)) return false;

    const calcDigit = (slice) => {
        let sum = slice.split('').reduce((acc, num, index) => acc + num * (slice.length + 1 - index), 0);
        let remainder = (sum * 10) % 11;
        return remainder === 10 ? 0 : remainder;
    };

    const digit1 = calcDigit(cpf.slice(0, 9));
    const digit2 = calcDigit(cpf.slice(0, 10));

    return digit1 == cpf[9] && digit2 == cpf[10];
}

module.exports = { verifyInput, verifyEmail, verifyCPF };
