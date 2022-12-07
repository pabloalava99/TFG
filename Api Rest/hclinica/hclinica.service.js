const db = require('_helpers/db');

module.exports = {
    getAll,
    create,
    buscarPorPersonal,
    buscarPorEpisodio
};

async function getAll() {
    return await db.HClinica.findAll();
}

async function buscarPorPersonal(params) {
    const personal = await db.HClinica.findAll({ where: { cod_profesional: params } });
    console.log(personal);
    if (personal == "[]") throw 'No se encuentran datos ';
    return personal;
}

async function buscarPorEpisodio(params) {
    const episodio = await db.HClinica.findAll({ where: { episodio: params } });
    console.log(episodio);
    if (episodio == "[]") throw 'No se encuentran datos ';
    return episodio;
}

async function create(params) {
    // Guardar HClinica
    await db.HClinica.create(params);
}

async function getByPersonal(personal) {
    return await buscarPersonal(personal);
}

async function getByEpisodio(episodio) {
    return await buscarEpisodio(episodio);
}


