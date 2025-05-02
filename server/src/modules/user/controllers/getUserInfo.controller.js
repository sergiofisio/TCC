const { getUserInfoService } = require("../services/getUserInfo.service");

module.exports = async (req, res, next) => {
  try {
    const id_user = req.params.id ? Number(req.params.id) : req.user.id_user;
    const result = await getUserInfoService(id_user);
    res.status(200).json(result);
  } catch (err) {
    next(err);
  }
};
