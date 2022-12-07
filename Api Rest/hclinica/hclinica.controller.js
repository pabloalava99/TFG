const express = require('express');
const router = express.Router();
const Joi = require('joi');

const validateRequest = require('_middleware/validate-request');
const authorize = require('_middleware/authorize')
const hclinicaService = require('./hclinica.service');

// rutas
router.post('/crear', authorize(), crearEvolucion, crear);
router.get('/:cod_profesional', authorize(), getByPersonal);
router.get('/episodio/:episodio', authorize(), getByEpisodio);
router.get('/', authorize(), getAll);

module.exports = router;

function crearEvolucion(req, res, next) {
    const schema = Joi.object({
        fecha: Joi.string(),
        cod_profesional: Joi.number().required(),
        nom_profesional: Joi.string().required(),
        evolucion: Joi.string().required(),
        episodio: Joi.string().required(),

    });
    validateRequest(req, next, schema);
}

function crear(req, res, next) {
    hclinicaService.create(req.body)
        .then(() => res.json({ message: 'Se ha creado la evolución correctamente' }))
        .catch(next);
}

function getByPersonal(req, res, next) {
    hclinicaService.buscarPorPersonal(req.params.cod_profesional)
        .then(hclinica => res.json(hclinica))
        .catch(next);
}

function getByEpisodio(req, res, next) {
    hclinicaService.buscarPorEpisodio(req.params.episodio)
        .then(hclinica => res.json(hclinica))
        .catch(next);
}

function getAll(req, res, next) {
    hclinicaService.getAll()
        .then(hclinica => res.json(hclinica))
        .catch(next);
}





