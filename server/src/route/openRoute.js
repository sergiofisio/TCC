// 📦 Importações
const express = require("express");
const register = require("../controllers/user/register");
const login = require("../controllers/user/login");
const verifyToken = require("../middleware/auth");
const sendRcovery = require("../controllers/user/recovery");
const lostPassword = require("../controllers/user/lostPassword");
const schedule = require("node-schedule");
const backupDatabase = require("../controllers/backup");

// 🚀 Inicialização do router para rotas públicas (sem autenticação obrigatória)
const openRoute = express.Router();

// 🔹 Rota raiz para checagem simples de funcionamento
openRoute.get(["/", ""], (_, res) => res.json({ init: true }));

// 🔐 Rota para verificar se um token JWT é válido
openRoute.get("/verify", verifyToken);

// 🧑‍💻 Rota para cadastrar novo usuário
openRoute.post("/register", register);

// 🔐 Rota para login de usuário
openRoute.post("/login", login);

// 📧 Rota para envio de e-mail de recuperação de senha
openRoute.post("/recovery", sendRcovery);

// 🔁 Rota para redefinir a senha com token (via link de e-mail)
openRoute.post("/lostpassword/:id_user/:token", lostPassword);

// 🗓️ Agendamento diário às 11h para executar backup do banco de dados
schedule.scheduleJob("0 11 * * *", backupDatabase);

// 🚀 Exporta as rotas abertas para serem usadas no roteador principal
module.exports = openRoute;
