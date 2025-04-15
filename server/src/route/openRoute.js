const express = require("express");
const register = require("../controllers/user/register");
const login = require("../controllers/user/login");
const verifyToken = require("../middleware/auth");
const sendRcovery = require("../controllers/user/recovery");
const lostPassword = require("../controllers/user/lostPassword");

const openRoute = express.Router();

openRoute.get(["/", ""], (_, res) => res.json({ init: true }));
openRoute.get("/verify", verifyToken);
openRoute.post("/register", register);
openRoute.post("/login", login);
openRoute.post("/recovery", sendRcovery);
openRoute.post("/lostpassword/:id_user/:token", lostPassword);

schedule.scheduleJob("0 11 * * *", backup);

module.exports = openRoute;
