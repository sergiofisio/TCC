const { extractUserToken } = require("../config/jwt");
const CustomError = require("../core/errors/CustomError");

module.exports = async (req, res, next) => {
  try {
    const token = req.headers["authorization"]?.split(" ")[1];
    if (!token) throw new CustomError("Token não fornecido", 401);
    req.user = await extractUserToken(token);
    next();
  } catch (err) {
    next(err);
  }
};
