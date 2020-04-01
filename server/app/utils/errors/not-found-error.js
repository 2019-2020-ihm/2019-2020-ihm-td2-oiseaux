const ApplicationError = require('./application-error');

class NotFoundError extends ApplicationError {
  constructor() {
    super(404, 'The requested resource was not found.');
  }
}

module.exports = NotFoundError;
