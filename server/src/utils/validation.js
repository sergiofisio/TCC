const verifyInput = (inputs) => {
  for (let input in inputs) {
    if (!inputs[input]) return input;
  }
  return false;
};

const verifyEmail = (email) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email);

const verifyCPF = (cpf) => {
  cpf = cpf.replace(/\D/g, "");
  if (cpf.length !== 11 || /^(\d)\1{10}$/.test(cpf)) return false;
  const calcDigit = (slice) => {
    const sum = slice
      .split("")
      .reduce((acc, num, i) => acc + num * (slice.length + 1 - i), 0);
    const mod = (sum * 10) % 11;
    return mod === 10 ? 0 : mod;
  };
  return (
    calcDigit(cpf.slice(0, 9)) == cpf[9] &&
    calcDigit(cpf.slice(0, 10)) == cpf[10]
  );
};

module.exports = { verifyInput, verifyEmail, verifyCPF };
