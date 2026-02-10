const express = require("express");
const verifyToken = require("../../../middlewares/auth");
const addHabitController = require("../controllers/addHabit.controller");
const findHabitController = require("../controllers/findHabit.controller");
const updateHabitController = require("../controllers/updateHabit.controller");

const router = express.Router();

router.post("/add/", verifyToken, addHabitController);
router.get("/find/:id?", verifyToken, findHabitController);
router.patch("/update/:id?", verifyToken, updateHabitController);

module.exports = router;
