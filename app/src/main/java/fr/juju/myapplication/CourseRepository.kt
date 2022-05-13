package fr.juju.myapplication

import android.util.Log
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
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
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
                        (getQuantite(ingredient.quantite) + getQuantite(oldItem.quantite)).toString(),
                        oldItem.categorie,
                        oldItem.ok,
                        oldItem.ajoutExterieur
                    ))
                } else {
                    if(checkUnite(ingredient.quantite, oldItem)){
                        repo.updateCourseItem(CourseModel(
                            oldItem.id,
                            oldItem.name,
                            addQuantite(ingredient.quantite, oldItem.quantite).toString() + " " + convertisseurUnite(oldItem.quantite),
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

    fun addItemCourse(item: CourseModel) {
        var repo = CourseRepository()
            if (courseList.filter { s -> s.name == item.name }.isNotEmpty()) {
                val oldItem = courseList.filter { s -> s.name == item.name }[0]
                if(isDigit(item.quantite) && isDigit(oldItem.quantite)){
                    repo.updateCourseItem(CourseModel(
                        oldItem.id,
                        oldItem.name,
                        (getQuantite(item.quantite) + getQuantite(oldItem.quantite)).toString(),
                        oldItem.categorie,
                        oldItem.ok,
                        oldItem.ajoutExterieur
                    ))
                } else {
                    if(checkUnite(item.quantite, oldItem)){
                        repo.updateCourseItem(CourseModel(
                            oldItem.id,
                            oldItem.name,
                            addQuantite(item.quantite, oldItem.quantite).toString() + " " + convertisseurUnite(getUnite(item.quantite)),
                            oldItem.categorie,
                            oldItem.ok,
                            oldItem.ajoutExterieur
                        ))
                    }
                    else {
                        repo.insertCourseItem(CourseModel(
                            UUID.randomUUID().toString(),
                            item.name,
                            item.quantite,
                            if (item.categorie != "None") categorieList.filter { s -> s.id == item.categorie }[0].name else "Autres",
                            "false",
                            "false"
                        ))
                    }
                }
            } else {
                repo.insertCourseItem(CourseModel(
                    UUID.randomUUID().toString(),
                    item.name,
                    item.quantite,
                    if (item.categorie != "None") categorieList.filter { s -> s.id == item.quantite }[0].name else "Autres",
                    "false",
                    "false"
                ))
        }
    }

    fun deleteIngredientCourse(ingredients: ArrayList<IngredientModel>) {
        var repo = CourseRepository()
        for (ingredient in ingredients) {
            if (courseList.filter { s -> s.name == ingredient.name }.isNotEmpty()) {
                val oldItem = courseList.filter { s -> s.name == ingredient.name }[0]
                if(isDigit(ingredient.quantite) && isDigit(oldItem.quantite)){
                    if(getQuantite(oldItem.quantite) - getQuantite(ingredient.quantite) > 0)
                    {
                        repo.updateCourseItem(CourseModel(
                            oldItem.id,
                            oldItem.name,
                            (getQuantite(oldItem.quantite) - getQuantite(ingredient.quantite)).toString(),
                            oldItem.categorie,
                            oldItem.ok,
                            oldItem.ajoutExterieur
                        ))
                    } else deleteCourseItem(oldItem)
                } else {
                    if(checkUnite(ingredient.quantite, oldItem)){
                        var ingr = oldItem.quantite
                        var ingre = getQuantite(convertisseur(oldItem.quantite).toString())
                        Log.i("tag", " nouveau : $ingr")
                        Log.i("tag", " ancien : $ingre")

                        if(removeQuantite(ingredient.quantite, oldItem.quantite).toFloat() > 0) {
                            repo.updateCourseItem(
                                CourseModel(
                                    oldItem.id,
                                    oldItem.name,
                                    removeQuantite(
                                        ingredient.quantite,
                                        oldItem.quantite
                                    ).toString() + " " + convertisseurUnite(getUnite(ingredient.quantite)),
                                    oldItem.categorie,
                                    oldItem.ok,
                                    oldItem.ajoutExterieur
                                )
                            )
                        } else deleteCourseItem(oldItem)
                    }
                }
            }
        }
    }

    fun getQuantite(quantiteInput: String): Float {
        var quantite = ""
        for (lettre in quantiteInput) {
            if (lettre.isDigit() || lettre==',' || lettre=='.' || lettre=='/') {
                quantite += lettre
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

    fun addQuantite(quantiteInput1: String, quantiteInput2: String): Float {
        return (convertisseur(quantiteInput1) + convertisseur(quantiteInput2))
    }

    fun removeQuantite(newItem: String, oldItem: String): Float {
        return convertisseur(oldItem) - convertisseur(newItem)
    }

    fun convertisseur(quantiteInput: String): Float {
        var value = getQuantite(quantiteInput)
        var unite = getUnite(quantiteInput)

        //CL
        if (unite.contains("l") && !unite.contains("cl") && !unite.contains("ml")) {
            value *= 100
        }
        else if (unite.contains("ml")) {
             value /= 10
        }
        //Grammes
        else if (unite.contains("kg")) {
            value *= 1000
        } else if (unite == "mg") {
            value /= 1000
        }

        return String.format("%.3f", value).toFloat()
    }

    fun convertisseurUnite(uniteInput: String): String{
        var uniteNew = getUnite(uniteInput)
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
        else if (uniteInput.contains("pavé") || uniteInput.contains("pave")){
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

        return unite.replace(" ", "")
    }

    fun isDigit(quantite: String): Boolean{
        return getUnite(quantite) == ""
    }

    fun checkUnite(ingredient: String, oldItem: CourseModel): Boolean {
            var uniteOld = getUnite(ingredient)
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
            else if (uniteOld.replace(" ", "").contains("pavé") || uniteOld.replace(" ", "").contains("pave")) {
                if (uniteNew.replace(" ", "").contains("pavé") || uniteNew.replace(" ", "").contains("pave"))
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