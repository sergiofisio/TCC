const { updateUserService } = require("../services/updateUser.service");

module.exports = async function updateUserController(req, res, next) {
  try {
    const user = await updateUserService(req.user.id_user, req.body);
    res.status(200).json(user);
  } catch (err) {
    next(err);
  }
};
