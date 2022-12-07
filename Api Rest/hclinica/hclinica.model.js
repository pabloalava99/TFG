const { DataTypes } = require('sequelize');

module.exports = model;

function model(sequelize) {
    const attributes = {
        fecha: {type: DataTypes.STRING, allowNull: true},
        cod_profesional: {type: DataTypes.INTEGER, allowNull:true},
        nom_profesional: {type: DataTypes.STRING, allowNull: true},
        evolucion: {type: DataTypes.STRING, allowNull: true},
        episodio: {type: DataTypes.STRING, allowNull: true},
    };

    return sequelize.define('HClinica', attributes, {timestamps: false});
}