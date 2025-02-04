const express = require("express");
const add = require("../controllers/activty/add");
const find = require("../controllers/activty/find");
const verifyAdmin = require("../middleware/admin");
const deleteUser = require("../controllers/user/delete");

const authRoute = express.Router();

authRoute.get("/find/:activity/:id_activity?", find);
authRoute.post("/add/:activity", add);

authRoute.use(verifyAdmin);

authRoute.delete("/user/:id", deleteUser);

module.exports = authRoute;
