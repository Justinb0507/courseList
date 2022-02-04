package fr.juju.myapplication

import java.lang.reflect.Constructor
import java.util.*
import kotlin.collections.ArrayList

class DBModel (
    val categorie : MutableMap<String, CategorieModel> = mutableMapOf<String, CategorieModel>(
        "categorie22" to  CategorieModel("categorie22","Plats Préparés")),

    val ingredients : MutableMap<String, IngredientModel> = mutableMapOf<String, IngredientModel>(
"9020d05e-1a46-4800-8ca0-68fbba202130" to  IngredientModel("9020d05e-1a46-4800-8ca0-68fbba202130","Raviole","e909b217-b4c7-4bd8-a3ce-3ed7cf0d539f","categorie22", "1",0)),



val repas : MutableMap<String, RepasModel> = mutableMapOf<String, RepasModel>(
        "e909b217-b4c7-4bd8-a3ce-3ed7cf0d539f" to  RepasModel("e909b217-b4c7-4bd8-a3ce-3ed7cf0d539f","Raviole","Le repas de la flemme tu coco","https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imagee909b217-b4c7-4bd8-a3ce-3ed7cf0d539f?alt=media&token=fc1a2d5f-7aac-481a-b900-3e0e2306263d", "","Ben du coup... ouvrir le paquet...oui..et euh..cuire dans l'eau qui bout...voilaaaaa", arrayListOf( "Plat", "Rapide", "Flemme" ), "3 min")),

    val semainier : MutableMap<String, SemainierModel> = mutableMapOf<String, SemainierModel>(
        "lundi" to  SemainierModel("None", "None", "None", "lundi"),
        "mardi" to  SemainierModel("None", "None", "None", "mardi"),
        "mercredi" to  SemainierModel("None", "None", "None", "mercredi"),
        "jeudi" to  SemainierModel("None", "None", "None", "jeudi"),
        "vendredi" to  SemainierModel("None", "None", "None", "vendredi"),
        "samedi" to  SemainierModel("None", "None", "None", "samedi"),
        "dimanche" to  SemainierModel("None", "None", "None", "dimanche")
    ),
    val semainierSuivant : MutableMap<String, SemainierModel> = mutableMapOf<String, SemainierModel>(
        "lundi" to  SemainierModel("None", "None", "None", "lundi"),
        "mardi" to  SemainierModel("None", "None", "None", "mardi"),
        "mercredi" to  SemainierModel("None", "None", "None", "mercredi"),
        "jeudi" to  SemainierModel("None", "None", "None", "jeudi"),
        "vendredi" to  SemainierModel("None", "None", "None", "vendredi"),
        "samedi" to  SemainierModel("None", "None", "None", "samedi"),
        "dimanche" to  SemainierModel("None", "None", "None", "dimanche")
    )

    )