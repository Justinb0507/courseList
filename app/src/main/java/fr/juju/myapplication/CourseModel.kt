package fr.juju.myapplication

import java.util.*

class CourseModel(
    var id:String = UUID.randomUUID().toString(),
    var name:String = "",
    var quantite:String = "",
    var categorie:String = "Autre",
    var ok: String = "false",
    var ajoutExterieur: String = "false"
)