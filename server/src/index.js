require("dotenv").config();
const express = require("express");
const cors = require("cors");
const routes = require("./routes");
const errorHandler = require("./middlewares/errorHandler");
const notFound = require("./middlewares/notFound");

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

// Inicialização do servidor
const PORT = process.env.PORT || 3000;
app.listen(PORT, () => {
  console.log(`🚀 Servidor rodando na porta ${PORT}`);
});
