require('rootpath')();
const express = require('express');
const app = express();
const cors = require('cors');

const errorHandler = require('_middleware/error-handler');

app.use(express.json());
app.use(express.urlencoded({ extended: true }));
app.use(cors());

// Rutas de la api
app.use('/users', require('./users/users.controller'));
app.use('/episodios', require('./episodios/episodios.controller'))
app.use('/hclinica', require('./hclinica/hclinica.controller'))

// Errores globales
app.use(errorHandler);

// Iniciar servidor
const port = process.env.NODE_ENV === 'production' ? (process.env.PORT || 80) : 4000;
app.listen(port, () => console.log('Server listening on port ' + port));