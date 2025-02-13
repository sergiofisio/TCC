const express = require("express");
const register = require("../controllers/user/register");
const login = require("../controllers/user/login");
const verifyToken = require("../middleware/auth");

const openRoute = express.Router();

openRoute.get(["/", ""], (_, res) => res.json({ init: true }));
openRoute.get("/verify", verifyToken);
openRoute.post("/register", register);
openRoute.post("/login", login);

module.exports = openRoute;
