const ApplicationError = require('./application-error');

class ValidationError extends ApplicationError {
  constructor(message) {
    super(422, message || 'The request inputs are invalid.');
  }
}

module.exports = ValidationError;
