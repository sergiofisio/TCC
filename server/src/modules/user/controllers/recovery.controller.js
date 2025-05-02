const { recoveryService } = require("../services/recovery.service");

module.exports = async (req, res, next) => {
  try {
    await recoveryService(req.body.email);
    res.json({ message: "E-mail enviado com sucesso" });
  } catch (err) {
    next(err);
  }
};
