const express = require("express");
const add = require("../controllers/activty/add");
const find = require("../controllers/activty/find");
const verifyAdmin = require("../middleware/admin");
const deleteUser = require("../controllers/user/delete");
const getUserInfo = require("../controllers/user/info");
const addEmotion = require("../controllers/emotion/add");
const backupDatabase = require("../controllers/backup");
const update = require("../controllers/user/update");
const getInfoDb = require("../controllers/user/infoDb");
const verifyTokenLogged = require("../controllers/user/verifyToken");

const authRoute = express.Router();

authRoute.get("/verify", verifyTokenLogged);
authRoute.get("/find/:activity/:id_activity?", find);
authRoute.get("/user/:id?", getUserInfo);
authRoute.post("/add/:activity", add);
authRoute.post("/addEmotion", addEmotion);
authRoute.patch("/update", update);
authRoute.delete("/deleteUser", deleteUser);

authRoute.use(verifyAdmin);

authRoute.get("/backup", backupDatabase);
authRoute.get("/infoDb", getInfoDb);
authRoute.get("/users/:id", getUserInfo);
authRoute.delete("/user/:id", deleteUser);

module.exports = authRoute;
