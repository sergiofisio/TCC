const { findHabitService } = require("../services/findHabit.service");

module.exports = async (req, res, next) => {
  try {
    const result = await findHabitService(req.user.id_user, req.params);
    res.status(200).json(result);
  } catch (err) {
    next(err);
  }
};
