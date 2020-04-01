require('dotenv').config();
const express = require('express');
const cors = require('cors');
const morgan = require('morgan');
const bodyParser = require('body-parser');
const routes = require('./routes');
const notFoundHandler = require('./utils/handlers/not-found-handler');
const errorHandler = require('./utils/handlers/error-handler');

// Configure the middleware.
const app = express();
const logger = morgan('dev');
const jsonParser = bodyParser.json();
const urlencodedParser = bodyParser.urlencoded({ extended: false });

// Configure the application.
app.disable('x-powered-by');
app.use(cors());
app.use(jsonParser);
app.use(urlencodedParser);
app.use(logger);
app.use(routes);
app.use(notFoundHandler);
app.use(errorHandler);

// Start the application.
app.listen(process.env.PORT || 3000);
