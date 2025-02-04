const express = require("express");
const verifyToken = require("./middleware/auth");

const allRoutes = express.Router();

allRoutes.use(express.json());

allRoutes.use((req, _, next) => {
  const url = req.protocol + "://" + req.get("host") + req.originalUrl;
  console.log("url: ", url);
  console.log("metodo: ", req.method);
  next();
});

const openRoute = require("./route/openRoute");
allRoutes.use(openRoute);

const authRoute = require("./route/authRoute");
allRoutes.use("/auth", verifyToken, authRoute);

allRoutes.use((_, res) => {
  res.status(404).json({ error: "Rota inválida" });
})

module.exports = allRoutes;
