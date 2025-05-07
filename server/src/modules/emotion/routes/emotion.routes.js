const express = require("express");
const verifyToken = require("../../../middlewares/auth");
const addEmotionController = require("../controllers/addEmotion.controller");

const router = express.Router();

router.post("/add", verifyToken, addEmotionController);

module.exports = router;
