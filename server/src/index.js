require("dotenv").config();
const express = require("express");
const cors = require("cors");
const os = require("os");
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

global.SERVER_URL = "";

app.use((req, res, next) => {
  if (!global.SERVER_URL) {
    const protocol = req.protocol;
    const host = req.get("host");
    global.SERVER_URL = `${protocol}://${host}`;
    console.log("🌐 URL detectada dinamicamente:", global.SERVER_URL);
  }
  next();
});

app.use(allRoutes);

app.get("/ping", (req, res) => {
  res.send(`Servidor online em: ${global.SERVER_URL || "não detectado ainda"}`);
});

const SERVER_PORT = process.env.PORT || 3000;
app.listen(SERVER_PORT, "0.0.0.0", () => {
  console.log(`🚀 Servidor rodando na porta ${SERVER_PORT}`);
});
