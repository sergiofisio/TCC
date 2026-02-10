const { updateUserService } = require("../services/updateUser.service");

module.exports = async function updateUserController(req, res, next) {
  const id = req.params.id || req.user.id_user;

  let body;

  if (req.params.id) body = { active_user: false };
  else body = req.body;

  try {
    const user = await updateUserService(id, body);

    res.status(200).json(user);
  } catch (err) {
    next(err);
  }
};
