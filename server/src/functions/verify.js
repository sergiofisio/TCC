// ✅ Função para verificar se todos os campos obrigatórios foram preenchidos
// Retorna o nome do primeiro campo vazio encontrado ou `false` se todos estiverem preenchidos
const verifyInput = (inputs) => {
  for (let input in inputs) {
    if (!inputs[input]) {
      return input; // Retorna o nome do campo que está vazio
    }
  }
  return false; // Nenhum campo vazio
};

// 📧 Função para validar formato de e-mail usando regex
const verifyEmail = (email) => {
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(email)) {
    return false; // Formato inválido
  }
  return true; // Formato válido
};

// 🇧🇷 Função para validar CPF (Cadastro de Pessoa Física)
const verifyCPF = (cpf) => {
  // 🔹 Remove caracteres não numéricos
  cpf = cpf.replace(/\D/g, "");

  // 🔹 Verifica se o CPF tem 11 dígitos
  if (cpf.length !== 11) return false;

  // 🔹 Rejeita CPFs com todos os dígitos iguais (ex: 111.111.111-11)
  if (/^(\d)\1{10}$/.test(cpf)) return false;

  // 🔹 Função auxiliar para cálculo dos dígitos verificadores
  const calcDigit = (slice) => {
    let sum = slice
      .split("")
      .reduce((acc, num, index) => acc + num * (slice.length + 1 - index), 0);
    let remainder = (sum * 10) % 11;
    return remainder === 10 ? 0 : remainder;
  };

  // 🔹 Calcula e compara os dígitos verificadores
  const digit1 = calcDigit(cpf.slice(0, 9));
  const digit2 = calcDigit(cpf.slice(0, 10));

  return digit1 == cpf[9] && digit2 == cpf[10]; // Retorna true se CPF for válido
};

// 🚀 Exporta todas as funções para uso em outros módulos
module.exports = { verifyInput, verifyEmail, verifyCPF };
