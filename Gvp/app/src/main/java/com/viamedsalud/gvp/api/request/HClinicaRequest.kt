package com.viamedsalud.gvp.api.request


import com.google.gson.annotations.SerializedName

data class HClinicaRequest(
    @SerializedName("fecha")
    var fecha: String,
    @SerializedName("cod_profesional")
    var codProfesional: Int,
    @SerializedName("nom_profesional")
    var nomProfesional: String,
    @SerializedName("evolucion")
    var evolucion: String,
    @SerializedName("episodio")
    var episodio: String
)