const { findActivityService } = require("../services/findActivity.service");

module.exports = async (req, res, next) => {
  try {
    const result = await findActivityService(req.user.id_user, req.params);
    res.status(200).json(result);
  } catch (err) {
    next(err);
  }
};
