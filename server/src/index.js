require("dotenv").config();
const express = require("express");
const cors = require("cors");
const routes = require("./routes");
const errorHandler = require("./middlewares/errorHandler");
const notFound = require("./middlewares/notFound");
const os = require("os");

const app = express();

// Middlewares globais
app.use(express.json());
app.use(cors());

app.use((req, res, next) => {
  const method = req.method;
  const url = req.url;
  console.log(`[${new Date().toISOString()}] ${method} ${url}`);
  next();
});

// Rotas
app.use(routes);

// Tratamento de erros
app.use(notFound);
app.use(errorHandler);

// Função para obter IP local da máquina
function getLocalIP() {
  const interfaces = os.networkInterfaces();
  for (const name of Object.keys(interfaces)) {
    for (const iface of interfaces[name]) {
      if (iface.family === "IPv4" && !iface.internal) {
        return iface.address;
      }
    }
  }
  return "localhost";
}

// Inicialização do servidor
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  const ip = getLocalIP();
  console.log(`🚀 Servidor rodando na porta ${PORT}`);
  console.log(`🌐 IP local: http://${ip}:${PORT}`);
});
