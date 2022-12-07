const { DataTypes } = require('sequelize');

module.exports = model;

function model(sequelize) {
    const attributes = {
        episodio: { type: DataTypes.STRING, allowNull: true },
        fecha: {type: DataTypes.DATE, allowNull: true},
        n_paciente: {type: DataTypes.INTEGER, allowNull:true},
        nom_paciente: {type: DataTypes.STRING, allowNull: true},
        apell_paciente: {type: DataTypes.STRING, allowNull: true},
        fecha_nac: {type: DataTypes.DATE, allowNull: true},
        sexo: {type: DataTypes.STRING, allowNull: true},
        diagnostico: {type: DataTypes.STRING, allowNull: true},
        doctor: {type: DataTypes.STRING, allowNull: true},
        cama: {type: DataTypes.STRING, allowNull: true},
        fecha_alta: {type: DataTypes.DATE, allowNull: true},
        alergia: {type: DataTypes.STRING, allowNull: true},
        medicacion: {type: DataTypes.STRING, allowNull: true},
    };

    return sequelize.define('Episodios', attributes, {timestamps: false});
}