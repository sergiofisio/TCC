// 🎨 Códigos ANSI para colorir o console
const colors = {
  reset: "\x1b[0m", // Reseta para cor padrão
  bright: "\x1b[1m", // Negrito/brilhante
  dim: "\x1b[2m", // Escurecido
  underscore: "\x1b[4m", // Sublinhado
  blink: "\x1b[5m", // Pisca (nem sempre funciona)

  // Cores de texto
  black: "\x1b[30m",
  red: "\x1b[31m",
  green: "\x1b[32m",
  yellow: "\x1b[33m",
  blue: "\x1b[34m",
  magenta: "\x1b[35m",
  cyan: "\x1b[36m",
  white: "\x1b[37m",

  // Cores de fundo
  bgBlack: "\x1b[40m",
  bgRed: "\x1b[41m",
  bgGreen: "\x1b[42m",
  bgYellow: "\x1b[43m",
  bgBlue: "\x1b[44m",
  bgMagenta: "\x1b[45m",
  bgCyan: "\x1b[46m",
  bgWhite: "\x1b[47m",
};

module.exports = { colors };
