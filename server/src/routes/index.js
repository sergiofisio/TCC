const express = require("express");
const authRoutes = require("../modules/user/routes/auth.routes.js");
const userRoutes = require("../modules/user/routes/user.routes");

const router = express.Router();

router.get(["/", "", "/init"], (_, res) => {
  res.json({ init: true, message: "Servidor rodando" });
});

router.use("/auth", authRoutes);
router.use("/user", userRoutes);

// Teste rápido
router.get("/ping", (_, res) => res.send("pong"));

module.exports = router;
