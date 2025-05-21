const CustomError = require("../../../core/errors/CustomError");
const { getUserInfoService } = require("../services/getUserInfo.service");

module.exports = async (req, res, next) => {
  try {
    const isAdmin = req.user.type_user === "admin";
    const requestedId = Number(req.params.id);
    const ownId = req.user.id_user;

    if (!isAdmin && requestedId && requestedId !== ownId)
      throw new CustomError("Acesso não autorizado", 403);

    const idToFetch = requestedId || ownId;

    const result = await getUserInfoService(idToFetch);

    res.status(200).json(result);
  } catch (err) {
    next(err);
  }
};
