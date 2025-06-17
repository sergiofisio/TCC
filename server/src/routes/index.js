const express = require("express");
const authRoutes = require("../modules/user/routes/auth.routes");
const userRoutes = require("../modules/user/routes/user.routes");
const emotionRoutes = require("../modules/emotion/routes/emotion.routes");
const activityRoutes = require("../modules/activity/routes/activity.routes");
const habitRoutes = require("../modules/habit/routes/habits.routes");
const schedule = require("node-schedule");

const router = express.Router();

router.get(["/", "", "/init"], (_, res) => {
  res.json({ init: true, message: "Servidor rodando" });
});

router.use("/auth", authRoutes);
router.use("/user", userRoutes);
router.use("/emotion", emotionRoutes);
router.use("/activity", activityRoutes);
router.use("/habit", habitRoutes);

// Teste rápido
router.get("/ping", (_, res) => res.send("pong"));

schedule.scheduleJob("0 11 * * *", async () => {
  const performBackup = require("../jobs/backupService");
  try {
    await performBackup();
    console.log("Backup realizado com sucesso!");
  } catch (error) {
    console.error("Erro ao realizar backup:", error);
  }
});

module.exports = router;
