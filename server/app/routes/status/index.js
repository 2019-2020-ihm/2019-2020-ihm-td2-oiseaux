const { Router } = require('express');
const models = require('../../models');

const router = new Router();

router.get('/', (req, res, next) => {
  // Check the database connection.
  models.sequelize
    .authenticate()
    .then(() => {
      // Success.
      res.status(200);
      res.json('ok');
    })
    // Errors.
    .catch(next);
});

module.exports = router;
