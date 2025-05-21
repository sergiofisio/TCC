const { loginUserService } = require("../services/loginUser.service");

module.exports = async (req, res, next) => {
  try {
    const result = await loginUserService(req.body);

    res.status(200).json(result);
  } catch (err) {
    console.log({ err });

    next(err);
  }
};
