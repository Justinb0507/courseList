package fr.juju.myapplication

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.juju.myapplication.CourseRepository.Singleton.courseList
import fr.juju.myapplication.IngredientRepository.Singleton.ingredientList
import fr.juju.myapplication.SemainierSuivantRepository.Singleton.semainierSuivantList
import fr.juju.myapplication.adapter.PopupIngredientOptionnelAdapter
import fr.juju.myapplication.fragments.SemainierFragment
import java.util.*
import kotlin.collections.ArrayList


class IngredientOptionnelPopup(
    private val context: MainActivity,
    private val ingredients: ArrayList<IngredientModel>,
    private val currentRepas: RepasModel,
    private val time: String,
    private val selectedDay: String
)
    : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        //setCanceledOnTouchOutside(false)
        setContentView(R.layout.ingredient_optionnel_popup)
        val listIngredientView = findViewById<RecyclerView>(R.id.recycler)
        listIngredientView.adapter = PopupIngredientOptionnelAdapter(context,ingredients, R.layout.item_ingredient_optionnel_popup)
        listIngredientView.layoutManager = LinearLayoutManager(context)
        setupCloseButton()
        setupValidButton()
    }

    private fun setupValidButton() {
        val repoSuivant = SemainierSuivantRepository()
        findViewById<ImageView>(R.id.valid).setOnClickListener{
            addIngredientCourse(ingredientList.filter { s->s.id_repas == currentRepas.id }.filter{ s->!s.name.contains("optionnel")} as ArrayList<IngredientModel>)
            addIngredientCourse(ingredientList.filter { s->s.id_repas == currentRepas.id }.filter{ s->s.name.contains("optionnel")} as ArrayList<IngredientModel>)
            if(semainierSuivantList.filter{ s->s.id_semainier == selectedDay}[0].midi != "None"){
                deleteIngredientCourse(ingredientList.filter { s->s.id_repas == semainierSuivantList.filter{ s->s.id_semainier == selectedDay}[0].midi } as ArrayList<IngredientModel>)
            }
            repoSuivant.setMidi(time, selectedDay, currentRepas.id)
            Toast.makeText(context, "Repas ajouté pour le $selectedDay $time!", Toast.LENGTH_SHORT).show()
            context.loadFragment(SemainierFragment(context, selectedDay,"suivant" ))
            dismiss()
        }
    }

    private fun setupCloseButton(){
        findViewById<ImageView>(R.id.close_button).setOnClickListener{
            dismiss()
        }
    }

    private fun addIngredientCourse(ingredients: ArrayList<IngredientModel>){
        var repo = CourseRepository()

        for (ingredient in ingredients){
            var courseItem = CourseModel(
                UUID.randomUUID().toString(),
                ingredient.name,
                ingredient.quantite,
                if (ingredient.id_categorie!="None") CategorieRepository.Singleton.categorieList.filter { s->s.id == ingredient.id_categorie }[0].name else "Autres",
                "false"
            )
            if(courseList.filter { s->s.name == ingredient.name }.isNotEmpty()){
                var isDigit = true
                var value = 0
                var oldItem = courseList.filter { s->s.name == ingredient.name }[0]
                for(lettre in oldItem.quantite){
                    if(!lettre.isDigit()){
                        isDigit = false
                    }
                }

                if (isDigit){
                    oldItem.quantite = (oldItem.quantite.toInt() + ingredient.quantite.toInt()).toString()
                }else{
                    if (oldItem.quantite.contains("cl")){
                        value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("cl")).replace(" ", "").toInt()
                    }
                    else if (oldItem.quantite.contains("l") && !oldItem.quantite.contains("cl")){
                        value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("l")).replace(" ", "").toInt()*100
                    }
                    else if (oldItem.quantite.contains("g")  && !oldItem.quantite.contains("kg") && !oldItem.quantite.contains("au jugé")){
                        value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("g")).replace(" ", "").toInt()
                    }
                    else if (oldItem.quantite.contains("kg")){
                        value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("kg")).replace(" ", "").toInt()*1000
                    }
                    else if (oldItem.quantite.contains("petit pot")){
                        value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("petit pot")).replace(" ", "").toInt()
                    }
                    else if (oldItem.quantite.contains("petits pots")){
                        value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("petits pots")).replace(" ", "").toInt()
                    }

                    if (ingredient.quantite.contains("cl"))
                    {
                        var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("cl")).replace(" ", "").toInt()
                        oldItem.quantite = ((value + newValue).toString()) + " cl"
                    }
                    else if (ingredient.quantite.contains("l") && !ingredient.quantite.contains("cl"))
                    {
                        var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("l")).replace(" ", "").toInt()*100
                        oldItem.quantite = ((value + newValue).toString()) + " cl"
                    }
                    else if (ingredient.quantite.contains("kg"))
                    {
                        var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("kg")).replace(" ", "").toInt()*1000
                        oldItem.quantite = ((value + newValue).toString()) + " g"
                    }
                    else if (ingredient.quantite.contains("g") && !ingredient.quantite.contains("kg") && !ingredient.quantite.contains("au jugé"))
                    {
                        var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("g")).replace(" ", "").toInt()
                        oldItem.quantite = ((value + newValue).toString()) + " g"
                    }
                    else if (ingredient.quantite.contains("au jugé")){
                        if(oldItem.quantite.contains(" *")){
                            value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" *")).replace(" ", "").toInt()+1
                            oldItem.quantite = value.toString() + " *" + " au jugé"
                        }else
                            oldItem.quantite = "2 * " + oldItem.quantite
                    }
                    else if (ingredient.quantite.contains("boites")){
                        var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("boites")).replace(" ", "").toInt()
                        if(oldItem.quantite.contains(" boites")){
                            value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" boites")).replace(" ", "").toInt()
                            oldItem.quantite = ((value + newValue).toString()) + " boites"
                        }else
                            value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" boite")).replace(" ", "").toInt()
                        oldItem.quantite = ((value + newValue).toString()) + " boites"
                    }
                    else if (ingredient.quantite.contains("petit pot")){
                        var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("petit pot")).replace(" ", "").toInt()
                        if(oldItem.quantite.contains(" petits pots")){
                            value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" petits pots")).replace(" ", "").toInt()
                            oldItem.quantite = ((value + newValue).toString()) + " boites"
                        }else
                            value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" petit pot")).replace(" ", "").toInt()
                        oldItem.quantite = ((value + newValue).toString()) + " petits pots"
                    }
                    else if (ingredient.quantite.contains("petits pots")){
                        var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("petit pots")).replace(" ", "").toInt()
                        if(oldItem.quantite.contains(" petits pots")){
                            value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" petits pots")).replace(" ", "").toInt()
                            oldItem.quantite = ((value + newValue).toString()) + " boites"
                        }else
                            value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" petit pot")).replace(" ", "").toInt()
                        oldItem.quantite = ((value + newValue).toString()) + " petits pots"
                    }
                }

            }

            else {
                courseList.add(courseItem)
            }
        }

        for (item in courseList){
            repo.insertCourseItem(item)
        }
    }
    
    private fun deleteIngredientCourse(ingredients: ArrayList<IngredientModel>){
        var repo = CourseRepository()
        for (ingredient in ingredients){
            if(courseList.filter { s->s.name == ingredient.name }.isNotEmpty()){
                var courseListTemp = courseList.filter { s->s.name == ingredient.name }
                for(item in courseListTemp){
                    var isDigit = true
                    var value = 0
                    var oldItem = courseList.filter { s->s.name == ingredient.name }[0]
                    for(lettre in oldItem.quantite){
                        if(!lettre.isDigit()){
                            isDigit = false
                        }
                    }
                    if (isDigit){
                        oldItem.quantite = (oldItem.quantite.toInt() - ingredient.quantite.toInt()).toString()
                    }else{
                        if (oldItem.quantite.contains("cl")){
                            value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("cl")).replace(" ", "").toInt()
                        }
                        else if (oldItem.quantite.contains("l") && !oldItem.quantite.contains("cl")){
                            value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("l")).replace(" ", "").toInt()*100
                        }
                        else if (oldItem.quantite.contains("g")  && !oldItem.quantite.contains("kg") && !oldItem.quantite.contains("au jugé")){
                            value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("g")).replace(" ", "").toInt()
                        }
                        else if (oldItem.quantite.contains("kg")){
                            value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("kg")).replace(" ", "").toInt()*1000
                        }
                        else if (oldItem.quantite.contains("petit pot")){
                            value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("petit pot")).replace(" ", "").toInt()
                        }
                        else if (oldItem.quantite.contains("petits pots")){
                            value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("petits pots")).replace(" ", "").toInt()
                        }
                        else if (oldItem.quantite.contains("sachet") && !oldItem.quantite.contains("sachets")){
                            value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("sachet")).replace(" ", "").toInt()
                        }
                        else if (oldItem.quantite.contains("sachets")){
                            value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("sachets")).replace(" ", "").toInt()
                        }

                        if (ingredient.quantite.contains("cl"))
                        {
                            var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("cl")).replace(" ", "").toInt()
                            oldItem.quantite = ((value - newValue).toString()) + " cl"
                        }
                        else if (ingredient.quantite.contains("l") && !ingredient.quantite.contains("cl"))
                        {
                            var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("l")).replace(" ", "").toInt()*100
                            oldItem.quantite = ((value - newValue).toString()) + " cl"
                        }
                        else if (ingredient.quantite.contains("kg"))
                        {
                            var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("kg")).replace(" ", "").toInt()*1000
                            oldItem.quantite = ((value - newValue).toString()) + " g"
                        }
                        else if (ingredient.quantite.contains("g") && !ingredient.quantite.contains("kg") && !ingredient.quantite.contains("au jugé"))
                        {
                            var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("g")).replace(" ", "").toInt()
                            oldItem.quantite = ((value - newValue).toString()) + " g"
                        }
                        else if (ingredient.quantite.contains("au jugé")){
                            if(oldItem.quantite.contains(" *")){
                                value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" *")).replace(" ", "").toInt()-1
                                oldItem.quantite = value.toString() + " *" + " au jugé"
                            }else if (oldItem.quantite.contains("au jugé")){
                                oldItem.quantite = "2 * au jugé"
                            }
                        }
                        else if (ingredient.quantite.contains("boites")){
                            var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("boites")).replace(" ", "").toInt()
                            if(oldItem.quantite.contains(" boites")){
                                value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" boites")).replace(" ", "").toInt()
                                oldItem.quantite = ((value - newValue).toString()) + " boites"
                            }else
                                value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" boite")).replace(" ", "").toInt()
                            oldItem.quantite = ((value - newValue).toString()) + " boites"
                        }
                        else if (ingredient.quantite.contains("petit pot")){
                            var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("petit pot")).replace(" ", "").toInt()
                            if(oldItem.quantite.contains(" petits pots")){
                                value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" petits pots")).replace(" ", "").toInt()
                                oldItem.quantite = ((value - newValue).toString()) + " boites"
                            }else
                                value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" petit pot")).replace(" ", "").toInt()
                            oldItem.quantite = ((value - newValue).toString()) + " petits pots"
                        }
                        else if (ingredient.quantite.contains("petits pots")){
                            var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("petit pots")).replace(" ", "").toInt()
                            if(oldItem.quantite.contains(" petits pots")){
                                value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" petits pots")).replace(" ", "").toInt()
                                oldItem.quantite = ((value - newValue).toString()) + " boites"
                            }else
                                value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" petit pot")).replace(" ", "").toInt()
                            oldItem.quantite = ((value - newValue).toString()) + " petits pots"
                        }
                        else if (ingredient.quantite.contains("sachet") && !ingredient.quantite.contains("sachets")){
                            var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("sachet")).replace(" ", "").toInt()
                            oldItem.quantite = ((value - newValue).toString()) + " sachets"
                        }
                        else if (ingredient.quantite.contains("sachets")){
                            var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("sachets")).replace(" ", "").toInt()
                            oldItem.quantite = ((value - newValue).toString()) + " sachets"
                        }

                    }
                    if(item.quantite[0].toString() == "0"){
                        repo.deleteCourseItem(item)
                    }else repo.updateCourseItem(item)
                }
            }
        }
    }

}
