const { addActivityService } = require("../services/addActivity.service");

module.exports = async (req, res, next) => {
  try {
    const result = await addActivityService(
      req.user.id_user,
      req.params.activity,
      req.body
    );
    res.status(201).json(result);
  } catch (err) {
    next(err);
  }
};
