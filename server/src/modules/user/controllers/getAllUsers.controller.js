const { getAllUsersService } = require("../services/getAllUsers.service");

module.exports = async (req, res, next) => {
  try {
    const result = await getAllUsersService(req.user.id_user);
    res.status(200).json(result);
  } catch (err) {
    next(err);
  }
};
