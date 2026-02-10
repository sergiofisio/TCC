const { addHabitService } = require("../services/addHabit.service");

module.exports = async (req, res, next) => {
  try {
    const result = await addHabitService(req.user.id_user, req.body);
    res.status(201).json(result);
  } catch (err) {
    next(err);
  }
};
