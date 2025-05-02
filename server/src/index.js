// 📦 Carrega variáveis de ambiente do arquivo .env
require("dotenv").config();

// 📚 Importações principais
const express = require("express");
const cors = require("cors");
const os = require("os");
const allRoutes = require("./routes");

// 🚀 Inicializa a aplicação Express
const app = express();

// 🛠️ Configurações globais do servidor
app.use(express.json());
app.use(cors());

// 🔧 Middleware para configurar headers de CORS manualmente
app.use(function (_, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header(
    "Access-Control-Allow-Headers",
    "Origin, X-Requested-With, Content-Type, Accept, Authorization"
  );
  res.header(
    "Access-Control-Allow-Methods",
    "GET, HEAD,POST, PUT, DELETE, OPTIONS"
  );
  next();
});

// 🌐 Variável global para armazenar a URL do servidor detectada
global.SERVER_URL = "";

// 🔍 Middleware para detectar e registrar dinamicamente a URL do servidor
app.use((req, _, next) => {
  if (!global.SERVER_URL) {
    const protocol = req.protocol;
    const host = req.get("host");
    global.SERVER_URL = `${protocol}://${host}`;
    console.log("🌐 URL detectada dinamicamente:", global.SERVER_URL);
  }
  next();
});

// 📂 Usa todas as rotas configuradas no projeto
app.use(allRoutes);

// ✅ Rota simples para verificar se o servidor está online
app.get("/ping", (_, res) => {
  res.send(`Servidor online em: ${global.SERVER_URL || "não detectado ainda"}`);
});

// 🚪 Define a porta do servidor (via .env ou padrão 3000)
const SERVER_PORT = process.env.PORT || 3000;

// ▶️ Inicializa o servidor para escutar requisições
app.listen(SERVER_PORT, "0.0.0.0", () => {
  console.log(`🚀 Servidor rodando na porta ${SERVER_PORT}`);
});
