package fr.juju.myapplication.model

class SemainierModel (
    var midi:String = "None",
    var soir:String = "None",
    var apero:String = "None",
    var autres: ArrayList<String> = arrayListOf<String>(),
    val id_semainier:String = "lundi"
)