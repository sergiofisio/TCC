const express = require("express");
// const path = require("path");

const schedule = require("node-schedule");
// const serveFavicon = require("serve-favicon");

const openRoute = express.Router();

// openRoute.use(serveFavicon(path.join(__dirname, "..", "..", "favicon.ico")));

openRoute.get(["/", ""], (_, res) => res.json({ init: true }));
openRoute.post("/user");

module.exports = openRoute;
