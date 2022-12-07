const tedious = require('tedious');
const { Sequelize } = require('sequelize');

const { dbName, dbConfig } = require('config.json');

module.exports = db = {};

initialize();

async function initialize() {
    const dialect = 'mssql';
    const host = dbConfig.server;
    const { userName, password } = dbConfig.authentication.options;

    // Conexion a la base de datos
    const sequelize = new Sequelize(dbName, userName, password, { host, dialect });

    // Inicializamos los modelos
    db.User = require('../users/user.model')(sequelize);
    db.Episodio = require('../episodios/episodios.model')(sequelize);
    db.HClinica = require('../hclinica/hclinica.model')(sequelize);


    // Sincronizamos los modelos con la base de datos
    await sequelize.sync({ alter: false });
}

