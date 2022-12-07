package com.viamedsalud.gvp.api.response


import com.google.gson.annotations.SerializedName
import java.util.*

data class EpisodioResponse(
    @SerializedName("id")
    var id: Int,
    @SerializedName("episodio")
    var episodio: String,
    @SerializedName("fecha")
    var fecha: Date,
    @SerializedName("n_paciente")
    var nPaciente: Int,
    @SerializedName("nom_paciente")
    var nomPaciente: String,
    @SerializedName("apell_paciente")
    var apellPaciente: String,
    @SerializedName("fecha_nac")
    var fechaNac: Date,
    @SerializedName("sexo")
    var sexo: String,
    @SerializedName("diagnostico")
    var diagnostico: String,
    @SerializedName("doctor")
    var doctor: String,
    @SerializedName("cama")
    var cama: String,
    @SerializedName("fecha_alta")
    var fechaAlta: Date,
    @SerializedName("alergia")
    var alergia: String,
    @SerializedName("medicacion")
    var medicacion: String,
)
