const { lostPasswordService } = require("../services/lostPassword.service");

module.exports = async (req, res, next) => {
  try {
    await lostPasswordService(req.params, req.body.password);
    res.json({ message: "Senha redefinida com sucesso!" });
  } catch (err) {
    next(err);
  }
};
