package fr.juju.myapplication

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.juju.myapplication.CategorieRepository.Singleton.categorieList
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
            if(time == "midi"){
                if(semainierSuivantList.filter{ s->s.id_semainier == selectedDay}[0].midi != "None"){
                    deleteIngredientCourse(ingredientList.filter { s->s.id_repas == semainierSuivantList.filter{ s->s.id_semainier == selectedDay}[0].midi } as ArrayList<IngredientModel>)
                }
                repoSuivant.setMidi(selectedDay, currentRepas.id)
            }
            if(time == "apero"){
                if(semainierSuivantList.filter{ s->s.id_semainier == selectedDay}[0].apero != "None"){
                    deleteIngredientCourse(ingredientList.filter { s->s.id_repas == semainierSuivantList.filter{ s->s.id_semainier == selectedDay}[0].apero } as ArrayList<IngredientModel>)
                }
                repoSuivant.setApero(selectedDay, currentRepas.id)
            }
            if(time == "soir"){
                if(semainierSuivantList.filter{ s->s.id_semainier == selectedDay}[0].soir != "None"){
                    deleteIngredientCourse(ingredientList.filter { s->s.id_repas == semainierSuivantList.filter{ s->s.id_semainier == selectedDay}[0].soir } as ArrayList<IngredientModel>)
                }
                repoSuivant.setSoir(selectedDay, currentRepas.id)
            }
            if(time == "autres"){
                repoSuivant.setAutres(selectedDay, currentRepas.id)
            }
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

    private fun addIngredientCourse(ingredients: ArrayList<IngredientModel>) {
        var repo = CourseRepository()
        repo.addIngredientCourse(ingredients)
    }
    private fun deleteIngredientCourse(ingredients: ArrayList<IngredientModel>){
        var repo = CourseRepository()
        repo.deleteIngredientCourse(ingredients)
    }

}
