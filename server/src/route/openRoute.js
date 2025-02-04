const express = require("express");

const schedule = require("node-schedule");
const register = require("../controllers/user/register");
const login = require("../controllers/user/login");
// const serveFavicon = require("serve-favicon");

const openRoute = express.Router();

// openRoute.use(serveFavicon(path.join(__dirname, "..", "..", "favicon.ico")));

openRoute.get(["/", ""], (_, res) => res.json({ init: true }));
openRoute.post("/register", register);
openRoute.post("/login", login)

module.exports = openRoute;
