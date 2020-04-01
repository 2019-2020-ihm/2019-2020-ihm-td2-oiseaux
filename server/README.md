# TD2 Oiseaux : Back-End Application
## Requirements
1. Install NodeJS.
```bash
pacman -S nodejs npm
```
2. Install the dependencies.
```bash
npm install
```
3. Rename the `.env.example` to `.env`.

## Development
1. Install Docker Compose.
```bash
sudo pacman -S docker docker-compose
```
2. Run Docker.
```bash
sudo systemctl start docker
```
3. Run the PostgreSQL database.
```bash
sudo docker-compose up
```
4. Run the database migrations.
```bash
npx sequelize db:migrate
```
5. Run the server.
```bash
npm run dev
```

## Production
1. Set up a PostgreSQL database.
2. Update the variables in the `.env` file.
3. Run the database migrations.
```bash
npx sequelize db:migrate
```
4. Run the server.
```bash
npm run start
```

## Linter
1. Run the linter.
```bash
npm run lint
```
2. Fix files with the linter.
```bash
npm run lint:fix
```

## Project Structure
- /app : The application folder.
    - /config : The configuration files.
    - /controllers : The logic of the application.
    - /migrations : The migrations of the database.
    - /models : The models of the application.
    - /routes : The routes of the application.
    - /utils : The utilities of the application.
        - /errors : The custom errors.
        - /handlers : The custom error handlers.
    - index.js : The application entry point.

## Dependencies
- [Express](https://www.npmjs.com/package/express) : Framework for building web applications and APIs.
- [Body Parser](https://www.npmjs.com/package/body-parser) : Middleware for parsing request bodies.
- [Multer](https://www.npmjs.com/package/multer) : Middleware for handling multipart/form-data.
- [Morgan](https://www.npmjs.com/package/morgan) : Middleware for logging requests.
- [CORS](https://www.npmjs.com/package/cors) : Middleware for enabling cross origin headers.
- [Sequelize](https://www.npmjs.com/package/sequelize) : Library for managing SQL databases.
- [PG](https://www.npmjs.com/package/pg) : PostgreSQL client.
- [PG Hstore](https://www.npmjs.com/package/pg-hstore) : Library for managing Hstore format.
- [Bcrypt](https://www.npmjs.com/package/bcrypt) : Library for hashing passwords.
- [Json Web Token](https://www.npmjs.com/package/jsonwebtoken) : Library for handling request tokens.
- [Dotenv](https://www.npmjs.com/package/dotenv) : Library for loading environment variables from a `.env` file.
