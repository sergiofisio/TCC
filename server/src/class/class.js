// 📦 Define uma classe de erro personalizada para incluir o código de status HTTP
class CustomError extends Error {
  // 🔹 Propriedade opcional para armazenar o código de status HTTP
  status;

  // 🔹 Construtor recebe a mensagem do erro e o status HTTP (ex: 400, 404, 500)
  constructor(message, status) {
    super(message); // Chama o construtor da classe Error (define a mensagem)
    this.status = status; // Armazena o status HTTP personalizado
  }
}

// 🚀 Exporta a classe para ser utilizada em outros arquivos
module.exports = { CustomError };
