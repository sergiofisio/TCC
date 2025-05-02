// 📦 Importações principais
const express = require("express");
const verifyToken = require("./middleware/auth");

// 🚀 Inicialização do roteador principal
const allRoutes = express.Router();

// 🔹 Habilita o uso de JSON nas requisições
allRoutes.use(express.json());

// 🛠️ Middleware para logar informações da requisição (URL e método)
allRoutes.use((req, _, next) => {
  const url = req.protocol + "://" + req.get("host") + req.originalUrl;
  console.log("url: ", url);
  console.log("metodo: ", req.method);
  next();
});

// 📂 Rotas públicas (sem autenticação)
const openRoute = require("./route/openRoute");
allRoutes.use(openRoute); // Ex: cadastro, login, recuperar senha...

// 🔐 Rotas protegidas (com verificação de token JWT)
const authRoute = require("./route/authRoute");
allRoutes.use("/auth", verifyToken, authRoute); // Prefixa com "/auth" e protege com middleware

// ❌ Middleware para tratar rotas inválidas (não encontradas)
allRoutes.use((_, res) => {
  res.status(404).json({ error: "Rota inválida" });
});

// 🚀 Exporta todas as rotas para uso no servidor principal (app.js / server.js)
module.exports = allRoutes;
