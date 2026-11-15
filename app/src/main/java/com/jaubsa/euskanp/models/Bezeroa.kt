package com.jaubsa.euskanp.models

import kotlinx.serialization.Serializable

@Serializable
data class Bezeroa(
    val id : String = "",
    val izena : String = "",
    val abizena : String = "",
    val emaila : String = "",
    val telefonoa : String = "",
    val ostatu_mota : String = "",
    val sortzeEguna : String = ""
){

}
