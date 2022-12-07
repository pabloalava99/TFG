package com.viamedsalud.gvp.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import java.lang.Exception
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

class Helper {
    companion object {


        /**
         *  Funcion para ocultar el teclado
         */
        fun hideKeyboard(view: View){
            try {
                val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view.windowToken, 0)
            }catch (e: Exception){

            }
        }

        /**
         * Funcion para Obtener la edad
         */
        fun obtenerEdad(fecha: Date): Int {
            val formatter = SimpleDateFormat("dd/MM/yyyy")//Formato de fecha
            val fmt: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")

            val ahora: LocalDate = LocalDate.now()
            val fechaNac = LocalDate.parse(formatter.format(fecha), fmt)
            val periodo: Period = Period.between(fechaNac, ahora)

            return periodo.getYears()
        }
    }

}