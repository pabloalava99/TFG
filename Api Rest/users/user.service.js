const jwt = require('jsonwebtoken');
const bcrypt = require('bcryptjs');

const { secret } = require('config.json');
const db = require('_helpers/db');

module.exports = {
    authenticate,
    getAll,
    getById,
    create,
    update,
    delete: _delete
};

async function authenticate({ username, password }) {
    const user = await db.User.scope('withHash').findOne({ where: { username } });

    if (!user || !(await bcrypt.compare(password, user.hash)))
        throw 'Usuario o contraseña incorrecta';

    // Autentificación correcta
    const token = jwt.sign({ sub: user.id }, secret, { expiresIn: '15d' });
    return { ...omitHash(user.get()), token };
}

async function getAll() {
    return await db.User.findAll();
}

async function getById(id) {
    return await getUser(id);
}

async function create(params) {
    // Validamos
    if (await db.User.findOne({ where: { username: params.username } })) {
        throw 'Usuario "' + params.username + '" ya existe';
    }

    // Hasheamos la password
    if (params.password) {
        params.hash = await bcrypt.hash(params.password, 10);
    }

    // Guardamos el usuario
    await db.User.create(params);
}

async function update(id, params) {
    const user = await getUser(id);

    // Validamos
    const usernameChanged = params.username && user.username !== params.username;
    if (usernameChanged && await db.User.findOne({ where: { username: params.username } })) {
        throw 'Usuario "' + params.username + '" ya existe';
    }

    // Haseamos el pasword
    if (params.password) {
        params.hash = await bcrypt.hash(params.password, 10);
    }

    // Copiamos los parametros y guardamos
    Object.assign(user, params);
    await user.save();

    return omitHash(user.get());
}

async function _delete(id) {
    const user = await getUser(id);
    await user.destroy();
}

// Otras funciones

async function getUser(id) {
    const user = await db.User.findByPk(id);
    if (!user) throw 'Usuario no encontrado';
    return user;
}

function omitHash(user) {
    const { hash, ...userWithoutHash } = user;
    return userWithoutHash;
}