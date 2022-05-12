package fr.juju.myapplication

import android.util.Log
import android.util.Log.INFO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import fr.juju.myapplication.CategorieRepository.Singleton.categorieList
import fr.juju.myapplication.CourseRepository.Singleton.authUid
import fr.juju.myapplication.CourseRepository.Singleton.courseList
import fr.juju.myapplication.CourseRepository.Singleton.databaseRef
import java.lang.Math.round
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import java.util.logging.Level.INFO
import kotlin.collections.ArrayList
import kotlin.math.ceil
import kotlin.math.roundToInt
import kotlin.math.roundToLong

class CourseRepository {
    object Singleton {

        private val BUCKET_URL: String = "gs://naturecollection-c9efc.appspot.com"
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)
        var authUid =  FirebaseAuth.getInstance().uid
        var databaseRef = FirebaseDatabase.getInstance().getReference(authUid.toString() + "/course")
        val courseList = arrayListOf<CourseModel>()
    }
    fun removeLink(){
        authUid =  FirebaseAuth.getInstance().uid
        databaseRef = FirebaseDatabase.getInstance().getReference(Singleton.authUid.toString() + "/course")
        courseList.clear()
    }


    fun updateData(callback: () -> Unit) {
        databaseRef.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                courseList.clear()
                for (ds in snapshot.children) {
                    val courseItem = ds.getValue(CourseModel::class.java)
                    if (courseItem != null) {
                        courseList.add(courseItem)
                    }
                }
                callback()
            }

            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    private fun formattage(string: String): String {
        var returnValue: String
        returnValue = string.lowercase(Locale.getDefault())
        returnValue = returnValue.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
        return returnValue
    }

    fun deleteCourseItem(courseModel: CourseModel) {
        databaseRef.child(courseModel.id).removeValue()
    }

    fun updateCourseItem(courseModel: CourseModel) {
        databaseRef.child(courseModel.id).setValue(courseModel)
    }

    fun insertCourseItem(courseModel: CourseModel) {
        databaseRef.child(courseModel.id).setValue(courseModel)
    }

    fun addIngredientCourse(ingredients: ArrayList<IngredientModel>) {
        var repo = CourseRepository()
        for (ingredient in ingredients) {
            if (courseList.filter { s -> s.name == ingredient.name }.isNotEmpty()) {
                val oldItem = courseList.filter { s -> s.name == ingredient.name }[0]
                if(isDigit(ingredient.quantite) && isDigit(oldItem.quantite)){
                    repo.updateCourseItem(CourseModel(
                        oldItem.id,
                        oldItem.name,
                        ceil(getQuantite(ingredient.quantite) + getQuantite(oldItem.quantite)).roundToInt().toString(),
                        oldItem.categorie,
                        oldItem.ok,
                        oldItem.ajoutExterieur
                    ))
                } else {
                    if(checkUnite(ingredient, oldItem)){
                        repo.updateCourseItem(CourseModel(
                            oldItem.id,
                            oldItem.name,
                            calculQuantite(ingredient.quantite, oldItem.quantite) + " " + convertisseurUnite(getUnite(ingredient.quantite)),
                            oldItem.categorie,
                            oldItem.ok,
                            oldItem.ajoutExterieur
                        ))
                    }
                    else {
                        repo.insertCourseItem(CourseModel(
                            UUID.randomUUID().toString(),
                            ingredient.name,
                            ingredient.quantite,
                            if (ingredient.id_categorie != "None") categorieList.filter { s -> s.id == ingredient.id_categorie }[0].name else "Autres",
                            "false",
                            "false"
                        ))
                    }
                }
            } else {
                repo.insertCourseItem(CourseModel(
                    UUID.randomUUID().toString(),
                    ingredient.name,
                    ingredient.quantite,
                    if (ingredient.id_categorie != "None") categorieList.filter { s -> s.id == ingredient.id_categorie }[0].name else "Autres",
                    "false",
                    "false"
                ))
            }
        }
    }

   /* fun deleteIngredientCourse(ingredients: ArrayList<IngredientModel>) {
        var repo = CourseRepository()
        for (ingredient in ingredients) {
            var courseItem = CourseModel(
                UUID.randomUUID().toString(),
                ingredient.name,
                ingredient.quantite,
                if (ingredient.id_categorie != "None") categorieList.filter { s -> s.id == ingredient.id_categorie }[0].name else "Autres",
                "false",
                "false"
            )

            if (ingredient.quantite.contains("/")) {
                var value3: String = ""
                for (lettre in ingredient.quantite.substring(ingredient.quantite.indexOf("/"))
                    .replace(" ", "")) {
                    if (lettre.isDigit()) {
                    } else {
                        if (lettre.toString() != "/") {
                            value3 = value3 + lettre
                        }
                    }
                }
                courseItem.quantite = "1  " + value3
                ingredient.quantite = "1 " + value3
            }


            if (courseList.filter { s -> s.name == ingredient.name }.isNotEmpty()) {
                var oldItemList = courseList.filter { s -> s.name == ingredient.name }
                var isDigit = true
                var isDigit2 = true
                var value = 0
               /* if (checkUnite(courseItem, oldItemList[0])) {
                    for (oldItem in oldItemList) {
                        if (checkUnite(courseItem, oldItem)) {
                            for (lettre in oldItem.quantite) {
                                if (!lettre.isDigit()) {
                                    isDigit = false
                                }
                            }

                            for (lettre in ingredient.quantite) {
                                if (!lettre.isDigit()) {
                                    isDigit2 = false
                                }
                            }

                            if (isDigit && isDigit2) {
                                oldItem.quantite =
                                    (oldItem.quantite.toInt() - ingredient.quantite.toInt()).toString()
                            }

//Traitement ancienne valeur
//Traitement Litres Cl Ml
                            if (oldItem.quantite.contains("cl")) {
                                value =
                                    oldItem.quantite.substring(0, oldItem.quantite.indexOf("cl"))
                                        .replace(" ", "").toInt()
                            } else if (oldItem.quantite.contains("l") && !oldItem.quantite.contains(
                                    "cl"
                                ) && !oldItem.quantite.contains(
                                    "ml"
                                )
                            ) {
                                value = oldItem.quantite.substring(0, oldItem.quantite.indexOf("l"))
                                    .replace(" ", "").toInt() * 100
                            } else if (oldItem.quantite.contains("ml")) {
                                if (oldItem.quantite.substring(0, oldItem.quantite.indexOf("ml"))
                                        .replace(" ", "").toInt() / 10 < 1
                                ) {
                                    value = 1
                                } else value =
                                    oldItem.quantite.substring(0, oldItem.quantite.indexOf("ml"))
                                        .replace(" ", "").toInt() / 10
                            }

//Traitements Grammes kg mg
                            else if (oldItem.quantite.contains("g") && !oldItem.quantite.contains("kg") && !oldItem.quantite.contains(
                                    "au jugé"
                                ) && !oldItem.quantite.contains("mg")
                            ) {
                                value = oldItem.quantite.substring(0, oldItem.quantite.indexOf("g"))
                                    .replace(" ", "").toInt()
                            } else if (oldItem.quantite.contains("kg")) {
                                value =
                                    oldItem.quantite.substring(0, oldItem.quantite.indexOf("kg"))
                                        .replace(" ", "").toInt() * 1000
                            } else if (oldItem.quantite.contains("mg")) {
                                if (oldItem.quantite.substring(0, oldItem.quantite.indexOf("mg"))
                                        .replace(" ", "").toInt() / 1000 < 1
                                ) {
                                    value = 1
                                } else value =
                                    oldItem.quantite.substring(0, oldItem.quantite.indexOf("mg"))
                                        .replace(" ", "").toInt() / 1000
                            }

//Traitement Petit pot
                            else if (oldItem.quantite.contains("petit pot")) {
                                value = oldItem.quantite.substring(
                                    0,
                                    oldItem.quantite.indexOf("petit pot")
                                )
                                    .replace(" ", "").toInt()
                            } else if (oldItem.quantite.contains("petits pots")) {
                                value =
                                    oldItem.quantite.substring(
                                        0,
                                        oldItem.quantite.indexOf("petits pots")
                                    )
                                        .replace(" ", "").toInt()
                            }
                            else if (oldItem.quantite.contains("cac")
                            ) {
                                value = oldItem.quantite.substring(
                                    0,
                                    oldItem.quantite.indexOf("cac")
                                )
                                    .replace(" ", "").toInt()
                            } else if (oldItem.quantite.contains("cas")) {
                                value = oldItem.quantite.substring(
                                    0,
                                    oldItem.quantite.indexOf("cas")
                                )
                                    .replace(" ", "").toInt()
                            }


//Traitement Sachet
                            else if (oldItem.quantite.contains("sachet") && !oldItem.quantite.contains(
                                    "sachets"
                                )
                            ) {
                                value = oldItem.quantite.substring(
                                    0,
                                    oldItem.quantite.indexOf("sachet")
                                )
                                    .replace(" ", "").toInt()
                            } else if (oldItem.quantite.contains("sachets")) {
                                value = oldItem.quantite.substring(
                                    0,
                                    oldItem.quantite.indexOf("sachets")
                                )
                                    .replace(" ", "").toInt()
                            }
                            else if (oldItem.quantite.contains("pincée") && !oldItem.quantite.contains(
                                    "pincées"
                                )
                            ) {
                                value = oldItem.quantite.substring(
                                    0,
                                    oldItem.quantite.indexOf("pincée")
                                )
                                    .replace(" ", "").toInt()
                            } else if (oldItem.quantite.contains("pincées")) {
                                value = oldItem.quantite.substring(
                                    0,
                                    oldItem.quantite.indexOf("pincées")
                                )
                                    .replace(" ", "").toInt()
                            }
//Traitement tranche
                            else if (oldItem.quantite.contains("tranche") && !oldItem.quantite.contains(
                                    "tranches"
                                )
                            ) {
                                value = oldItem.quantite.substring(
                                    0,
                                    oldItem.quantite.indexOf("tranche")
                                )
                                    .replace(" ", "").toInt()
                            } else if (oldItem.quantite.contains("tranches")) {
                                value = oldItem.quantite.substring(
                                    0,
                                    oldItem.quantite.indexOf("tranches")
                                )
                                    .replace(" ", "").toInt()
                            }
//Traitement bottes
                            else if (oldItem.quantite.contains("botte") && !oldItem.quantite.contains(
                                    "bottes"
                                )
                            ) {
                                value =
                                    oldItem.quantite.substring(0, oldItem.quantite.indexOf("botte"))
                                        .replace(" ", "").toInt()
                            } else if (oldItem.quantite.contains("bottes")) {
                                value = oldItem.quantite.substring(
                                    0,
                                    oldItem.quantite.indexOf("bottes")
                                )
                                    .replace(" ", "").toInt()
                            }
                            else if (oldItem.quantite.contains("pavé") && !oldItem.quantite.contains(
                                    "pavés"
                                )
                            ) {
                                value =
                                    oldItem.quantite.substring(0, oldItem.quantite.indexOf("pavé"))
                                        .replace(" ", "").toInt()
                            } else if (oldItem.quantite.contains("pavés")) {
                                value = oldItem.quantite.substring(
                                    0,
                                    oldItem.quantite.indexOf("pavés")
                                )
                                    .replace(" ", "").toInt()
                            }


//Traitement nouvelle valeur
//Traitement cl l ml
                            if (ingredient.quantite.contains("cl")) {
                                var newValue =
                                    ingredient.quantite.substring(
                                        0,
                                        ingredient.quantite.indexOf("cl")
                                    )
                                        .replace(" ", "").toInt()
                                oldItem.quantite = ((value - newValue).toString()) + " cl"
                            } else if (ingredient.quantite.contains("l") && !ingredient.quantite.contains(
                                    "cl"
                                ) && !ingredient.quantite.contains(
                                    "ml"
                                )
                            ) {
                                var newValue =
                                    ingredient.quantite.substring(
                                        0,
                                        ingredient.quantite.indexOf("l")
                                    )
                                        .replace(" ", "").toInt() * 100
                                oldItem.quantite = ((value - newValue).toString()) + " cl"
                            } else if (ingredient.quantite.contains("ml")) {
                                var newValue = 0
                                if (ingredient.quantite.substring(
                                        0,
                                        ingredient.quantite.indexOf("ml")
                                    )
                                        .replace(" ", "").toInt() / 10 < 1
                                ) {
                                    newValue = 1
                                } else newValue = ingredient.quantite.substring(
                                    0,
                                    ingredient.quantite.indexOf("ml")
                                )
                                    .replace(" ", "").toInt() / 10

                                oldItem.quantite = ((value - newValue).toString()) + " cl"
                            }

//Traitement kg g mg
                            else if (ingredient.quantite.contains("kg")) {
                                var newValue =
                                    ingredient.quantite.substring(
                                        0,
                                        ingredient.quantite.indexOf("kg")
                                    )
                                        .replace(" ", "").toInt() * 1000
                                oldItem.quantite = ((value - newValue).toString()) + " g"
                            } else if (ingredient.quantite.contains("g") && !ingredient.quantite.contains(
                                    "kg"
                                ) && !ingredient.quantite.contains(
                                    "mg"
                                ) && !ingredient.quantite.contains("au jugé")
                            ) {
                                var newValue =
                                    ingredient.quantite.substring(
                                        0,
                                        ingredient.quantite.indexOf("g")
                                    )
                                        .replace(" ", "").toInt()
                                oldItem.quantite = ((value - newValue).toString()) + " g"
                            } else if (ingredient.quantite.contains("mg")) {
                                var newValue = 0
                                if (ingredient.quantite.substring(
                                        0,
                                        ingredient.quantite.indexOf("mg")
                                    )
                                        .replace(" ", "").toInt() / 1000 < 1
                                ) {
                                    newValue = 1
                                } else newValue = ingredient.quantite.substring(
                                    0,
                                    ingredient.quantite.indexOf("mg")
                                )
                                    .replace(" ", "").toInt() / 1000
                                oldItem.quantite = ((value - newValue).toString()) + " g"
                            }


//Au jugé
                            else if (ingredient.quantite.contains("au jugé")) {
                                if (oldItem.quantite.contains(" *")) {
                                    value = oldItem.quantite.substring(
                                        0,
                                        oldItem.quantite.indexOf(" *")
                                    )
                                        .replace(" ", "").toInt() - 1
                                    oldItem.quantite = value.toString() + " *" + " au jugé"
                                } else
                                    oldItem.quantite = "0 * " + oldItem.quantite
                            }

//Boites
                            else if (ingredient.quantite.contains("boites")) {
                                var newValue = ingredient.quantite.substring(
                                    0,
                                    ingredient.quantite.indexOf("boites")
                                ).replace(" ", "").toInt()
                                if (oldItem.quantite.contains(" boites")) {
                                    value = oldItem.quantite.substring(
                                        0,
                                        oldItem.quantite.indexOf(" boites")
                                    ).replace(" ", "").toInt()
                                    oldItem.quantite = ((value - newValue).toString()) + " boites"
                                } else value = oldItem.quantite.substring(
                                    0,
                                    oldItem.quantite.indexOf(" boite")
                                ).replace(" ", "").toInt()
                                oldItem.quantite = ((value - newValue).toString()) + " boites"
                            } else if (ingredient.quantite.contains("boite")) {
                                var newValue = ingredient.quantite.substring(
                                    0,
                                    ingredient.quantite.indexOf("boite")
                                ).replace(" ", "").toInt()
                                if (oldItem.quantite.contains(" boites")) {
                                    value = oldItem.quantite.substring(
                                        0,
                                        oldItem.quantite.indexOf(" boites")
                                    ).replace(" ", "").toInt()
                                    oldItem.quantite = ((value - newValue).toString()) + " boites"
                                } else value = oldItem.quantite.substring(
                                    0,
                                    oldItem.quantite.indexOf(" boite")
                                ).replace(" ", "").toInt()
                                oldItem.quantite = ((value - newValue).toString()) + " boites"
                            }


//Petit pots
                            else if (ingredient.quantite.contains("petit pot")) {
                                var newValue = ingredient.quantite.substring(
                                    0,
                                    ingredient.quantite.indexOf("petit pot")
                                ).replace(" ", "").toInt()
                                oldItem.quantite = ((value - newValue).toString()) + " petits pots"

                            } else if (ingredient.quantite.contains("petits pots")) {
                                var newValue = ingredient.quantite.substring(
                                    0,
                                    ingredient.quantite.indexOf("petit pots")
                                ).replace(" ", "").toInt()
                                oldItem.quantite = ((value - newValue).toString()) + " petits pots"
                            }

//Sachet
                            else if (ingredient.quantite.contains("sachet") && !ingredient.quantite.contains(
                                    "sachets"
                                )
                            ) {
                                var newValue =
                                    ingredient.quantite.substring(
                                        0,
                                        ingredient.quantite.indexOf("sachet")
                                    )
                                        .replace(" ", "").toInt()
                                oldItem.quantite = ((value - newValue).toString()) + " sachets"
                            } else if (ingredient.quantite.contains("sachets")) {
                                var newValue =
                                    ingredient.quantite.substring(
                                        0,
                                        ingredient.quantite.indexOf("sachets")
                                    )
                                        .replace(" ", "").toInt()
                                oldItem.quantite = ((value - newValue).toString()) + " sachets"
                            }

                            else if (ingredient.quantite.contains("pincée") && !ingredient.quantite.contains(
                                    "pincées"
                                )
                            ) {
                                var newValue =
                                    ingredient.quantite.substring(
                                        0,
                                        ingredient.quantite.indexOf("pincée")
                                    )
                                        .replace(" ", "").toInt()
                                oldItem.quantite = ((value - newValue).toString()) + " pincées"
                            } else if (ingredient.quantite.contains("pincées")) {
                                var newValue =
                                    ingredient.quantite.substring(
                                        0,
                                        ingredient.quantite.indexOf("pincées")
                                    )
                                        .replace(" ", "").toInt()
                                oldItem.quantite = ((value - newValue).toString()) + " pincées"
                            }
//tranche
                            else if (ingredient.quantite.contains("tranche") && !ingredient.quantite.contains(
                                    "tranches"
                                )
                            ) {
                                var newValue =
                                    ingredient.quantite.substring(
                                        0,
                                        ingredient.quantite.indexOf("tranche")
                                    )
                                        .replace(" ", "").toInt()
                                oldItem.quantite = ((value - newValue).toString()) + " tranches"
                            } else if (ingredient.quantite.contains("tranches")) {
                                var newValue =
                                    ingredient.quantite.substring(
                                        0,
                                        ingredient.quantite.indexOf("tranches")
                                    )
                                        .replace(" ", "").toInt()
                                oldItem.quantite = ((value - newValue).toString()) + " tranches"
                            }

//Botte
                            else if (ingredient.quantite.contains("botte") && !ingredient.quantite.contains(
                                    "bottes"
                                )
                            ) {
                                var newValue =
                                    ingredient.quantite.substring(
                                        0,
                                        ingredient.quantite.indexOf("botte")
                                    )
                                        .replace(" ", "").toInt()

                                oldItem.quantite = ((value - newValue).toString()) + " bottes"

                            } else if (ingredient.quantite.contains("bottes")) {
                                var newValue =
                                    ingredient.quantite.substring(
                                        0,
                                        ingredient.quantite.indexOf("bottes")
                                    )
                                        .replace(" ", "").toInt()
                                oldItem.quantite = ((value - newValue).toString()) + " bottes"
                            }

                            else if (ingredient.quantite.contains("pavé") && !ingredient.quantite.contains(
                                    "pavés"
                                )
                            ) {
                                var newValue =
                                    ingredient.quantite.substring(
                                        0,
                                        ingredient.quantite.indexOf("pavé")
                                    )
                                        .replace(" ", "").toInt()
                                oldItem.quantite = ((value - newValue).toString()) + " pavés"
                            } else if (ingredient.quantite.contains("pavés")) {
                                var newValue =
                                    ingredient.quantite.substring(
                                        0,
                                        ingredient.quantite.indexOf("pavés")
                                    )
                                        .replace(" ", "").toInt()
                                oldItem.quantite = ((value - newValue).toString()) + " pavés"
                            }
                            else if (ingredient.quantite.contains("cas")
                            ) {
                                var newValue =
                                    ingredient.quantite.substring(
                                        0,
                                        ingredient.quantite.indexOf("cas")
                                    )
                                        .replace(" ", "").toInt()

                                oldItem.quantite = ((value - newValue).toString()) + " cas"

                            }
                            else if (ingredient.quantite.contains("cac")) {
                                var newValue =
                                    ingredient.quantite.substring(
                                        0,
                                        ingredient.quantite.indexOf("cac")
                                    )
                                        .replace(" ", "").toInt()
                                oldItem.quantite = ((value - newValue).toString()) + " cac"
                            }

                            if (oldItem.quantite[0].toString() == "0") {
                                repo.deleteCourseItem(oldItem)
                            } else repo.updateCourseItem(oldItem)

                        }
                    }
                }*/
            }
        }
    }

    fun deleteIngredientCourse(ingredients: ArrayList<IngredientModel>) {
        var repo = CourseRepository()
    }
    */
    fun getQuantite(quantiteInput: String): Float {
        var quantite = ""
        for (lettre in quantiteInput) {
            if (lettre.isDigit() || lettre==',' || lettre=='.' || lettre=='/') {
                quantite = quantite + lettre
            }
        }
        if(quantite.contains(",")){
            quantite = quantite.replace(',' ,  '.')
        }
        if(quantite.contains("/")){
            quantite = ceil(quantite.split("/")[0].toFloat() / quantite.split("/")[1].toFloat()).toString()
        }
        return quantite.toFloat()
    }

    fun calculQuantite(quantiteInput1: String, quantiteInput2: String): String {
        var value = (getQuantite(convertisseur(quantiteInput1).toString()) + getQuantite(convertisseur(quantiteInput2).toString()))
        return if (value.toString().split('.')[1] == "0"){
            value.toString().split('.')[0]
        } else {
            val df = DecimalFormat("#.#")
            df.roundingMode = RoundingMode.CEILING
            return df.format(value)
        }
    }

    fun convertisseur(quantiteInput: String): Float {
        var value = getQuantite(quantiteInput)
        var unite = getUnite(quantiteInput)

        //CL
        if (unite.contains("cl")) {
        }
        else if (unite.contains("l") && !unite.contains("cl") && !unite.contains("ml")) {
            value *= 100
        }
        else if (unite.contains("ml")) {
             value /= 10
        }
        //Grammes
        else if (unite.contains("g") && !unite.contains("kg") && !unite.contains("au jugé") && !unite.contains("mg")
        ) {
        } else if (unite.contains("kg")) {
            value *= 1000
        } else if (unite.contains("mg")) {
            value /= 1000
        }
        return value
    }

    fun convertisseurUnite(uniteInput: String): String{
        var uniteNew = ""
        if (uniteInput.contains("cl") || (uniteInput.contains("l") && !uniteInput.contains("cl") && !uniteInput.contains("ml")) || (uniteInput.contains("ml"))) {
            uniteNew = "cl"
        }
        else if ((uniteInput.contains("g") && !uniteInput.contains("kg") && !uniteInput.contains("au jugé") && !uniteInput.contains("mg")) || (uniteInput.contains("kg")) || (uniteInput.contains("mg"))
        ) {
            uniteNew = "g"
        }
        else if (uniteInput.contains("petit pot") || uniteInput.contains("petits pots") || uniteInput.contains("petits pot") || uniteInput.contains("petit pots") || uniteInput.contains("petit pôt") || uniteInput.contains("petits pôts") || uniteInput.contains("petits pôt") || uniteInput.contains("petit pôts")) {
            uniteNew = "petits pots"
        }

        else if (uniteInput.contains("cac") || uniteInput == "cc") {
            uniteNew = "cac"
        }
        else if (uniteInput.contains("cas") || uniteInput == "cs") {
            uniteNew = "cas"
        }
        else if (uniteInput.contains("sachet")) {
            uniteNew = "sachets"
        }
        else if (uniteInput.contains("pincée") || uniteInput.contains("pincee")){
            uniteNew = "pincées"
        }
        else if (uniteInput.contains("tranche")) {
            uniteNew = "tranches"
        }
        else if (uniteInput.contains("botte")){
            uniteNew = "bottes"
        }
        else if (uniteInput.contains("pavé")){
            uniteNew = "pavés"
        }
        else if (uniteInput.contains("au jugé")){
            uniteNew = "au jugé"
        }
        else if (uniteInput.contains("boite")){
            uniteNew = "boites"
        }
        else if (uniteInput.contains("conserve")){
            uniteNew = "conserves"
        }

        return uniteNew
    }

    fun getUnite(quantite: String): String{
        var unite = ""
        for (lettre in quantite) {
            if (!lettre.isDigit() && lettre!=',' && lettre!='.' && lettre!='/') {
                unite = unite + lettre
            }
        }
        return unite
    }

    fun isDigit(quantite: String): Boolean{
        return getUnite(quantite) == ""
    }

    fun checkUnite(ingredient: IngredientModel, oldItem: CourseModel): Boolean {
            var uniteOld = getUnite(ingredient.quantite)
            var uniteNew = getUnite(oldItem.quantite)

             if (uniteOld.replace(" ", "") == "cl" || uniteOld.replace(
                    " ",
                    ""
                ) == "ml" || uniteOld.replace(" ", "") == "l"
            ) {
                if (uniteNew.replace(" ", "") == "cl" || uniteNew.replace(
                        " ",
                        ""
                    ) == "ml" || uniteNew.replace(" ", "") == "l"
                )
                    return true
            } else if (uniteOld.replace(" ", "") == "mg" || uniteOld.replace(
                    " ",
                    ""
                ) == "g" || uniteOld.replace(" ", "") == "kg"
            ) {
                if (uniteNew.replace(" ", "") == "mg" || uniteNew.replace(
                        " ",
                        ""
                    ) == "g" || uniteNew.replace(" ", "") == "kg"
                )
                    return true
            }
            //au jugé
            else if (uniteOld.replace(" ", "").contains("aujugé")) {
                if (uniteNew.replace(" ", "").contains("aujugé"))
                    return true
            }
            //boite
            else if (uniteOld.replace(" ", "").contains("boîte") || uniteOld.replace(" ", "").contains("boite")) {
                if (uniteNew.replace(" ", "").contains("boîte") || uniteOld.replace(" ", "").contains("boite"))
                    return true
            }
            //petits pots
            else if (uniteOld.replace(" ", "").contains("petitspots") || uniteOld.replace(" ", "")
                    .contains("petitpot")
            ) {
                if (uniteNew.replace(" ", "").contains("petitspots") || uniteNew.replace(" ", "")
                        .contains("petitpot")
                )
                    return true
            }
            //sachet
            else if (uniteOld.replace(" ", "").contains("sachet")) {
                if (uniteNew.replace(" ", "").contains("sachet"))
                    return true
            }
            //pincé
            else if (uniteOld.replace(" ", "").contains("pincé")) {
                if (uniteNew.replace(" ", "").contains("pincé"))
                    return true
            }
            //pavés
            else if (uniteOld.replace(" ", "").contains("pavé")) {
                if (uniteNew.replace(" ", "").contains("pavé"))
                    return true
            }
            //tranche
            else if (uniteOld.replace(" ", "").contains("tranche")) {
                if (uniteNew.replace(" ", "").contains("tranche"))
                    return true
            }
            //cas
            else if (uniteOld.replace(" ", "").equals("cas")) {
                if (uniteNew.replace(" ", "").equals("cas"))
                    return true
            }
            //cac
            else if (uniteOld.replace(" ", "").equals("cac")) {
                if (uniteNew.replace(" ", "").equals("cac"))
                    return true
            }
            //botte
            else if (uniteOld.replace(" ", "").contains("botte")) {
                if (uniteNew.replace(" ", "").contains("botte"))
                    return true
            } else return false

        return false
    }

}