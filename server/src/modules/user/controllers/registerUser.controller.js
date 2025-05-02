const { registerUserService } = require("../services/registerUser.service");

module.exports = async (req, res, next) => {
  try {
    const result = await registerUserService(req.body);
    res.status(201).json(result);
  } catch (err) {
    next(err);
  }
};
