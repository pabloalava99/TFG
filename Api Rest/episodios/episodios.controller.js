const express = require('express');
const router = express.Router();
const Joi = require('joi');

const authorize = require('_middleware/authorize')
const episodioService = require('./episodio.service');

// Rutas
router.get('/', authorize(), getAll);
router.get('/:episodio', authorize(), getByEpisodio);
module.exports = router;


function getAll(req, res, next) {
    episodioService.getAll()
        .then(Episodio => res.json(Episodio))
        .catch(next);
}

function getByEpisodio(req, res, next) {
    episodioService.getByEpisodio(req.params.episodio)
        .then(episodio => res.json(episodio))
        .catch(next);
}
