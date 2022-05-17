package fr.juju.myapplication

import java.util.*
import kotlin.collections.ArrayList

class RepasCommunModel (
    var id:String = UUID.randomUUID().toString(),
    var name:String = "",
    var createur: String = "",
    var description:String = "",
    var imageUri: String = "",
    var recette:String = "",
    var quantite:String = "",
    var tags: ArrayList<String> = arrayListOf(),
    var duree: String = "",
    var ingredientsList : ArrayList<IngredientModel> = arrayListOf()
)