const express = require("express");
const verifyToken = require("../../../middlewares/auth");
const addActivityController = require("../controllers/addActivity.controller");
const findActivityController = require("../controllers/findActivity.controller");

const router = express.Router();

router.post("/add/:activity", verifyToken, addActivityController);
router.get("/find/:activity/:id?", verifyToken, findActivityController);

module.exports = router;
