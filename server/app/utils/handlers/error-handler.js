const errorHandler = (error, req, res, next) => {
  // TODO: Secure for production.
  res
    .status(error.code || 500)
    .json({ error })
    .end();
};

module.exports = errorHandler;
