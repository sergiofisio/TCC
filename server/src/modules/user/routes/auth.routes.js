const express = require("express");
const loginUser = require("../controllers/loginUser.controller");
const registerUser = require("../controllers/registerUser.controller");
const sendRecovery = require("../controllers/recovery.controller");
const lostPassword = require("../controllers/lostPassword.controller");
const verifyTokenLogged = require("../controllers/verifyToken.controller");

const router = express.Router();

router.post("/login", loginUser);
router.post("/register", registerUser);
router.post("/recovery", sendRecovery);
router.post("/lostpassword/:id_user/:token", lostPassword);
router.get("/verify", verifyTokenLogged);

module.exports = router;
