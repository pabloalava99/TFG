const { DataTypes } = require('sequelize');

module.exports = model;

function model(sequelize) {
    const attributes = {
        username: { type: DataTypes.STRING, allowNull: false },
        hash: { type: DataTypes.STRING, allowNull: false },
        fechaCreacion: { type: DataTypes.DATE, allowNull: false },
        fechaModificacion: { type: DataTypes.DATE, allowNull: false }
    };

    const options = {
        timestamps: false,
        defaultScope: {
            // Exclumos el hash por defecto
            attributes: { exclude: ['hash'] }
        },
        scopes: {
            // Incluimos el hash con este alcanze
            withHash: { attributes: {}, }
        }
    };

    return sequelize.define('User', attributes, options);
}