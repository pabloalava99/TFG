const db = require('_helpers/db');

module.exports = {
    getAll,
    getByEpisodio
};

async function getAll() {
    return await db.Episodio.findAll();
}

async function getByEpisodio(episodio) {
    return await getEpisodio(episodio);
}

// Funciones de ayuda

async function getEpisodio(params) {
    const episodio = await db.Episodio.findOne({ where: { episodio: params } });
    console.log(episodio);
    if (!episodio) throw 'Episodio no encontrado';
    return episodio;
}