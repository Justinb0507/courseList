package fr.juju.myapplication.repository

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import fr.juju.myapplication.repository.CategorieRepository.Singleton.categorieList
import fr.juju.myapplication.repository.CourseRepository.Singleton.authUid
import fr.juju.myapplication.repository.CourseRepository.Singleton.courseList
import fr.juju.myapplication.repository.CourseRepository.Singleton.databaseRef
import fr.juju.myapplication.model.CourseModel
import fr.juju.myapplication.model.IngredientModel
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.ceil

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
                    repo.updateCourseItem(
                        CourseModel(
                        oldItem.id,
                        oldItem.name,
                        (getQuantite(ingredient.quantite) + getQuantite(oldItem.quantite)).toString(),
                        oldItem.categorie,
                        oldItem.ok,
                        oldItem.ajoutExterieur
                    )
                    )
                } else {
                    if(checkUnite(ingredient.quantite, oldItem)){
                        repo.updateCourseItem(
                            CourseModel(
                            oldItem.id,
                            oldItem.name,
                            addQuantite(ingredient.quantite, oldItem.quantite).toString() + " " + convertisseurUnite(oldItem.quantite),
                            oldItem.categorie,
                            oldItem.ok,
                            oldItem.ajoutExterieur
                        )
                        )

                    }
                    else {
                        repo.insertCourseItem(
                            CourseModel(
                            UUID.randomUUID().toString(),
                            ingredient.name,
                            ingredient.quantite,
                            if (ingredient.id_categorie != "None") categorieList.filter { s -> s.id == ingredient.id_categorie }[0].name else "Autres",
                            "false",
                            "false"
                        )
                        )
                    }
                }
            } else {
                repo.insertCourseItem(
                    CourseModel(
                    UUID.randomUUID().toString(),
                    ingredient.name,
                    ingredient.quantite,
                    if (ingredient.id_categorie != "None") categorieList.filter { s -> s.id == ingredient.id_categorie }[0].name else "Autres",
                    "false",
                    "false"
                )
                )
            }
        }
    }

    fun addItemCourse(item: CourseModel) {
        var repo = CourseRepository()
            if (courseList.filter { s -> s.name == item.name }.isNotEmpty()) {
                val oldItem = courseList.filter { s -> s.name == item.name }[0]
                if(isDigit(item.quantite) && isDigit(oldItem.quantite)){
                    repo.updateCourseItem(
                        CourseModel(
                        oldItem.id,
                        oldItem.name,
                        (getQuantite(item.quantite) + getQuantite(oldItem.quantite)).toString(),
                        oldItem.categorie,
                        oldItem.ok,
                        oldItem.ajoutExterieur
                    )
                    )
                } else {
                    if(checkUnite(item.quantite, oldItem)){
                        repo.updateCourseItem(
                            CourseModel(
                            oldItem.id,
                            oldItem.name,
                            addQuantite(item.quantite, oldItem.quantite).toString() + " " + convertisseurUnite(getUnite(item.quantite)),
                            oldItem.categorie,
                            oldItem.ok,
                            oldItem.ajoutExterieur
                        )
                        )
                    }
                    else {
                        repo.insertCourseItem(
                            CourseModel(
                            UUID.randomUUID().toString(),
                            item.name,
                            item.quantite,
                            if (item.categorie != "None" && categorieList.filter { s -> s.id == item.categorie }.isNotEmpty()){
                                categorieList.filter { s -> s.id == item.categorie }[0].name
                            } else if(item.categorie != "None" && categorieList.filter { s -> s.id == item.categorie }.isEmpty()) {
                                item.categorie
                            } else "Autres",
                            "false",
                            "false"
                        )
                        )
                    }
                }
            } else {

                repo.insertCourseItem(
                    CourseModel(
                    UUID.randomUUID().toString(),
                    item.name,
                    item.quantite,
                    if (categorieList.filter { s -> s.id == item.categorie }.isNotEmpty()) {
                        categorieList.filter { s -> s.id == item.categorie }[0].name
                    } else if(item.categorie != "None"){
                        item.categorie
                    }
                    else "Autres",
                    "false",
                    "false"
                )
                )
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
                        repo.updateCourseItem(
                            CourseModel(
                            oldItem.id,
                            oldItem.name,
                            (getQuantite(oldItem.quantite) - getQuantite(ingredient.quantite)).toString(),
                            oldItem.categorie,
                            oldItem.ok,
                            oldItem.ajoutExterieur
                        )
                        )
                    } else deleteCourseItem(oldItem)
                } else {
                    if(checkUnite(ingredient.quantite, oldItem)){
                      if(removeQuantite(ingredient.quantite, oldItem.quantite) > 0) {
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
        if (quantite != "") {
            return quantite.toFloat()
        } else return 1f
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

        return value
    }

    fun convertisseurUnite(uniteInput: String): String{
        var uniteNew = getUnite(uniteInput)
        if (uniteInput.contains("cl") || (uniteInput.contains("l") && !uniteInput.contains("cl") && !uniteInput.contains("ml")) || (uniteInput.contains("ml"))) {
            uniteNew = "cl"
        }
        else if ((uniteInput.contains("g") && !uniteInput.contains("gousse") && !uniteInput.contains("kg") && !uniteInput.contains("au jug??") && !uniteInput.contains("mg")) || (uniteInput.contains("kg")) || (uniteInput.contains("mg"))
        ) {
            uniteNew = "g"
        }
        else if (uniteInput.contains("petit pot") || uniteInput.contains("petits pots") || uniteInput.contains("petits pot") || uniteInput.contains("petit pots") || uniteInput.contains("petit p??t") || uniteInput.contains("petits p??ts") || uniteInput.contains("petits p??t") || uniteInput.contains("petit p??ts")) {
            uniteNew = "petits pots"
        }
        else if (uniteInput.contains("gros pot") || uniteInput.contains("gros pots") || uniteInput.contains("gros p??t") || uniteInput.contains("gros p??ts")) {
            uniteNew = "gros pots"
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
        else if (uniteInput.contains("pinc??e") || uniteInput.contains("pincee")){
            uniteNew = "pinc??es"
        }
        else if (uniteInput.contains("tranche")) {
            uniteNew = "tranches"
        }
        else if (uniteInput.contains("botte")){
            uniteNew = "bottes"
        }
        else if (uniteInput.contains("pav??") || uniteInput.contains("pave")){
            uniteNew = "pav??s"
        }
        else if (uniteInput.contains("au jug??")){
            uniteNew = "au jug??"
        }
        else if (uniteInput.contains("boite")){
            uniteNew = "boites"
        }
        else if (uniteInput.contains("conserve")){
            uniteNew = "conserves"
        }
        else if (uniteInput.contains("gousse")){
            uniteNew = "gousses"
        }
        else if (uniteInput.contains("conserve")){
            uniteNew = "conserves"
        }
        else if (uniteInput.contains("un peu")){
            uniteNew = "un peu"
        }
        else if (uniteInput.contains("paquet")){
            uniteNew = "paquet"
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
        if(unite.isNotEmpty() &&  unite[0] != ' '){
            return unite
        } else if (unite.isNotEmpty() && unite[0] == ' '){
                return unite.drop(0)
            }
            else return unite
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
            //au jug??
            else if (uniteOld.replace(" ", "").contains("aujug??")) {
                if (uniteNew.replace(" ", "").contains("aujug??"))
                    return true
            }
             //un peu
             else if (uniteOld.replace(" ", "").contains("unpeu")) {
                 if (uniteNew.replace(" ", "").contains("unpeu"))
                     return true
             }
             //conserve
             else if (uniteOld.replace(" ", "").contains("conserve")) {
                 if (uniteNew.replace(" ", "").contains("conserve"))
                     return true
             }
            //boite
            else if (uniteOld.replace(" ", "").contains("bo??te") || uniteOld.replace(" ", "").contains("boite")) {
                if (uniteNew.replace(" ", "").contains("bo??te") || uniteOld.replace(" ", "").contains("boite"))
                    return true
            }
             //gousse
             else if (uniteOld.replace(" ", "").contains("gousse")) {
                 if (uniteNew.replace(" ", "").contains("gousse"))
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
             else if (uniteOld.replace(" ", "").contains("grospot") || uniteOld.replace(" ", "")
                     .contains("grosp??t")
             ) {
                 if (uniteNew.replace(" ", "").contains("grospot") || uniteNew.replace(" ", "")
                         .contains("grosp??t")
                 )
                     return true
             }
            //sachet
            else if (uniteOld.replace(" ", "").contains("sachet")) {
                if (uniteNew.replace(" ", "").contains("sachet"))
                    return true
            }
            //pinc??
            else if (uniteOld.replace(" ", "").contains("pinc??")) {
                if (uniteNew.replace(" ", "").contains("pinc??"))
                    return true
            }
            //pav??s
            else if (uniteOld.replace(" ", "").contains("pav??") || uniteOld.replace(" ", "").contains("pave")) {
                if (uniteNew.replace(" ", "").contains("pav??") || uniteNew.replace(" ", "").contains("pave"))
                    return true
            }
            //tranche
            else if (uniteOld.replace(" ", "").contains("tranche")) {
                if (uniteNew.replace(" ", "").contains("tranche"))
                    return true
            }
            //cas
            else if (uniteOld.replace(" ", "").equals("cas") || uniteOld.replace(" ", "").equals("cs")) {
                if (uniteNew.replace(" ", "").equals("cas") || uniteNew.replace(" ", "").equals("cs"))
                    return true
            }
            //cac
            else if (uniteOld.replace(" ", "").equals("cac") || uniteOld.replace(" ", "").equals("cc")) {
                if (uniteNew.replace(" ", "").equals("cac") || uniteNew.replace(" ", "").equals("cc"))
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