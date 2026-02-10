const deleteUserService = require("../services/deleteUser.service");

module.exports = async function deleteUserController(req, res, next) {
  const id = req.params.id;

  try {
    await deleteUserService(Number(id));
    res.status(200).json({ message: "Usuário deletado com sucesso" });
  } catch (error) {
    console.log({ error });

    next(error);
  }
};
