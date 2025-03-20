require("dotenv").config();
const express = require("express");
const cors = require("cors");
const allRoutes = require("./routes");

const app = express();

app.use(express.json());
app.use(cors());
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

app.use(allRoutes);

const SERVER_PORT = process.env.PORT || 3000;
const SERVER_HOST =
  process.env.NODE_ENV === "production"
    ? process.env.HOST || "0.0.0.0"
    : "localhost";

const SERVER_URL =
  process.env.NODE_ENV === "production"
    ? `https://${process.env.HOST || "seu-servidor.com"}`
    : `http://localhost:${SERVER_PORT}`;

app.listen(SERVER_PORT, SERVER_HOST, () => {
  console.log(`🚀 Servidor rodando em ${SERVER_URL}:${SERVER_PORT}`);
});
