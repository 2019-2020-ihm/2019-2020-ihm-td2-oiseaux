const NotFoundError = require('../errors/not-found-error');

const notFoundHandler = (req, res, next) => {
  next(new NotFoundError());
};

module.exports = notFoundHandler;
