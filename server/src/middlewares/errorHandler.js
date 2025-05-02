module.exports = (err, req, res, next) => {
  const status = err.status || 500;
  res.status(status).json({
    error: true,
    message: err.message || "Erro interno no servidor",
  });
};
