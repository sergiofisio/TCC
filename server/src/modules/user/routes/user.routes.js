const express = require("express");
const verifyToken = require("../../../middlewares/auth");
const verifyAdmin = require("../../../middlewares/admin");
const updateUserController = require("../controllers/updateUser.controller");
const getUserInfoController = require("../controllers/getUserInfo.controller");
const getAllUsersController = require("../controllers/getAllUsers.controller");

const router = express.Router();

router.patch("/update", verifyToken, updateUserController);
router.get("/info", verifyToken, verifyAdmin, (_, res) =>
  res.send("Área Admin")
);
router.get("/:id?", verifyToken, getUserInfoController);
router.get("/admin/all", verifyToken, verifyAdmin, getAllUsersController);

module.exports = router;
