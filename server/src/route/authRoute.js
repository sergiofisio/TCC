// 📦 Importações
const express = require("express");
const add = require("../controllers/activty/add");
const find = require("../controllers/activty/find");
const verifyAdmin = require("../middleware/admin");
const deleteUser = require("../controllers/user/delete");
const getUserInfo = require("../controllers/user/info");
const addEmotion = require("../controllers/emotion/add");
const backupDatabase = require("../controllers/backup");
const update = require("../controllers/user/update");
const getInfoDb = require("../controllers/user/infoDb");
const verifyTokenLogged = require("../controllers/user/verifyToken");

// 🚀 Inicializa o roteador para rotas protegidas por autenticação
const authRoute = express.Router();

// 🔐 Rotas acessíveis por qualquer usuário autenticado

// ✅ Verifica se o token está válido e se a senha já foi alterada
authRoute.get("/verify", verifyTokenLogged);

// 🔍 Busca atividades (meditação ou respiração) por tipo e opcionalmente por ID
authRoute.get("/find/:activity/:id_activity?", find);

// 👤 Retorna dados do próprio usuário ou de um ID específico
authRoute.get("/user/:id?", getUserInfo);

// ➕ Adiciona uma nova atividade (meditação ou respiração)
authRoute.post("/add/:activity", add);

// 😌 Adiciona uma nova emoção diária
authRoute.post("/addEmotion", addEmotion);

// ✏️ Atualiza os dados do usuário autenticado
authRoute.patch("/update", update);

// ❌ Desativa a conta do próprio usuário
authRoute.delete("/deleteUser", deleteUser);

// 🛡️ Middleware: verifica se o usuário é um administrador antes das rotas abaixo
authRoute.use(verifyAdmin);

// 🔐 Rotas acessíveis apenas por administradores

// 💾 Executa o backup manualmente (apenas para admin)
authRoute.get("/backup", backupDatabase);

// 🗃️ Retorna todos os dados do sistema (usuários + atividades)
authRoute.get("/infoDb", getInfoDb);

// 🔍 Busca os dados de um usuário específico
authRoute.get("/users/:id", getUserInfo);

// ❌ Exclui qualquer usuário (apenas admin pode fazer isso por ID)
authRoute.delete("/user/:id", deleteUser);

// 🚀 Exporta as rotas autenticadas para serem usadas no roteador principal
module.exports = authRoute;
