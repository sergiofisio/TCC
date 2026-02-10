const { updateUserService } = require("../../user/services/updateUser.service");

module.exports = async (req, res, next) => {
  const id = req.params.id;
  const { last_time_habit } = req.body;

  try {
    const habit = await updateUserService(id, { last_time_habit });

    res.status(200).json(habit);
  } catch (err) {
    next(err);
  }
};
