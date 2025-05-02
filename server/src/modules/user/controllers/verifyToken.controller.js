const { extractUserToken } = require("../../../config/jwt");

module.exports = async (req, res, next) => {
  try {
    const token = req.headers["authorization"]?.split(" ")[1];
    const user = await extractUserToken(token);
    res.json({ verifyToken: true, passwordChanged: user.password_changed });
  } catch (err) {
    next(err);
  }
};
