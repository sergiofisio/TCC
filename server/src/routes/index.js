const express = require("express");
const authRoutes = require("../modules/user/routes/auth.routes");
const userRoutes = require("../modules/user/routes/user.routes");
const emotionRoutes = require("../modules/emotion/routes/emotion.routes");
const activityRoutes = require("../modules/activity/routes/activity.routes");

const router = express.Router();

router.get(["/", "", "/init"], (_, res) => {
  res.json({ init: true, message: "Servidor rodando" });
});

router.use("/auth", authRoutes);
router.use("/user", userRoutes);
router.use("/emotion", emotionRoutes);
router.use("/activity", activityRoutes);

// Teste rápido
router.get("/ping", (_, res) => res.send("pong"));

module.exports = router;
