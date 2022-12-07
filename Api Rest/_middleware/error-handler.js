module.exports = errorHandler;

function errorHandler(err, req, res, next) {
    switch (true) {
        case typeof err === 'string':
            // Error de la api
            const is404 = err.toLowerCase().endsWith('No funciona');
            const statusCode = is404 ? 404 : 400;
            return res.status(statusCode).json({ message: err });
        case err.name === 'UnauthorizedError':
            // Error a la autorización de token
            return res.status(401).json({ message: 'No autorizado' });
        default:
            return res.status(500).json({ message: err.message });
    }
}