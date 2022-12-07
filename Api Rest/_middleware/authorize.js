const jwt = require('express-jwt');

const { secret } = require('config.json');
const db = require('_helpers/db');

module.exports = authorize;

function authorize() {
    return [
        // Actenticacion del token con HS246
        jwt({ secret, algorithms: ['HS256'] }),

        async (req, res, next) => {
            //Obtenemos el usuario con el Id
            const user = await db.User.findByPk(req.user.sub);

            //Comprobamos que el usuario existe
            if (!user)
                return res.status(401).json({ message: 'No autorizado' });

            // Autorización correcta
            req.user = user.get();
            next();
        }
    ];
}