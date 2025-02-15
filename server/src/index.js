require("dotenv").config();
const express = require("express");
const cors = require("cors");
const allRoutes = require("./routes");

const app = express();

app.use(express.json());
app.use(cors());
app.use(function (_, res, next) {
  res.header("Access-Control-Allow-Origin", "*");
  res.header(
    "Access-Control-Allow-Headers",
    "Origin, X-Requested-With, Content-Type, Accept, Authorization"
  );
  res.header(
    "Access-Control-Allow-Methods",
    "GET, HEAD,POST, PUT, DELETE, OPTIONS"
  );
  next();
});

app.use(allRoutes);

app.listen(process.env.PORT || 3000, () => {
  console.log(`Servidor rodando http://localhost:${process.env.PORT || 3000}`);
});
