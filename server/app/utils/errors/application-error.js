class ApplicationError extends Error {
  constructor(code, message) {
    super();
    Error.captureStackTrace(this, this.constructor);
    this.code = code || 500;
    this.name = this.constructor.name;
    this.message = message || 'Internal Server Error.';
  }
}

module.exports = ApplicationError;
