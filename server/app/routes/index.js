const { Router } = require('express');
const StatusRouter = require('./status');

const router = new Router();

// Status routes.
router.use('/status', StatusRouter);

module.exports = router;
