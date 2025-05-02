// Middleware para rotas não encontradas
module.exports = (req, res, next) => {
  res.status(404).json({
    error: true,
    message: `Rota não encontrada: [${req.method}] ${req.originalUrl}`,
  });
};
