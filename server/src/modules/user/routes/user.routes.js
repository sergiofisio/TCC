const express = require("express");
const verifyToken = require("../../../middlewares/auth");
const verifyAdmin = require("../../../middlewares/admin");
const updateUserController = require("../controllers/updateUser.controller");
const getUserInfoController = require("../controllers/getUserInfo.controller");
const getAllUsersController = require("../controllers/getAllUsers.controller");
const deleteUsersController = require("../controllers/deleteUsers.controller");
const performBackup = require("../../../jobs/backupService");

const router = express.Router();

router.get("/:id?", verifyToken, getUserInfoController);
router.get("/info", verifyToken, verifyAdmin, (_, res) =>
  res.send("Área Admin")
);
router.get("/admin/all", verifyToken, verifyAdmin, getAllUsersController);
router.post("/backup", verifyToken, verifyAdmin, async (req, res) => {
  console.log("🔄 Iniciando backup do banco de dados...");

  try {
    await performBackup();
    res
      .status(200)
      .json({ message: "Backup realizado com sucesso e enviado por e-mail!" });
  } catch (err) {
    console.log({ err });

    res.status(500).json({ error: "Erro ao realizar o backup." });
  }
});
router.patch("/update", verifyToken, updateUserController);
router.delete("/delete/:id", verifyToken, deleteUsersController);

module.exports = router;
