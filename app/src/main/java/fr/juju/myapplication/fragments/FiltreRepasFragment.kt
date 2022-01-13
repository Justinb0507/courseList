package fr.juju.myapplication.fragments

import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import fr.juju.myapplication.*
import fr.juju.myapplication.CategorieRepository.Singleton.categorieList
import fr.juju.myapplication.IngredientRepository.Singleton.ingredientList
import fr.juju.myapplication.RepasRepository.Singleton.repasList
import java.util.*
import kotlin.collections.ArrayList

class FiltreRepasFragment(
    val context: MainActivity,
    val time: String,
    val selectedDay: String
) : Fragment(){

override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {

    val view = inflater?.inflate(R.layout.fragment_filtrerepas, container, false)
    view.findViewById<ImageView>(R.id.add_recette).setOnClickListener{
        context.unprintSoir()
        context.loadFragment(AddRepasFragment(context))
    }
    view.findViewById<ImageView>(R.id.plat).setOnClickListener{
        context.loadFragment(ResultResearchFragment(context, "Plat", "categorie", time, selectedDay))
    }
    view.findViewById<ImageView>(R.id.dessert).setOnClickListener{
        context.loadFragment(ResultResearchFragment(context, "Dessert", "categorie", time, selectedDay))
    }
    view.findViewById<ImageView>(R.id.soupe).setOnClickListener{
        context.loadFragment(ResultResearchFragment(context, "Soupe", "categorie", time, selectedDay))
    }
    view.findViewById<ImageView>(R.id.apero).setOnClickListener{
        context.loadFragment(ResultResearchFragment(context, "Apero", "categorie", time, selectedDay))
    }

    view.findViewById<Button>(R.id.research).setOnClickListener{
        context.loadFragment(ResultResearchFragment(context, view.findViewById<EditText>(R.id.research_input).text.toString(), "nope", time, selectedDay))
    }
    view.findViewById<EditText>(R.id.research_input).setOnKeyListener(
        View.OnKeyListener { v, keyCode, event ->
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            context.hideKeyboard()
            if(research(view.findViewById<EditText>(R.id.research_input).text.toString()).isNotEmpty() == true){
                context.loadFragment(ResultResearchFragment(context, view.findViewById<EditText>(R.id.research_input).text.toString(), "nope", time, selectedDay))
            }
            else view.findViewById<ImageView>(R.id.searchbar).animate().scaleY(10F).scaleY(0F)
            return@OnKeyListener true
        }
            return@OnKeyListener false
        })


    val translate = AnimationUtils.loadAnimation(context, R.anim.translate_anim)
    view.findViewById<ConstraintLayout>(R.id.constraint).startAnimation(translate)

    return view
}

    private fun research(parameter: String): ArrayList<RepasModel> {
        val resultResearch = arrayListOf<RepasModel>()
        var listResearch = arrayListOf<String>()

        if (parameter.isNotEmpty()){
            for (item in parameter.split(" ")){
                var temp = item.lowercase(Locale.getDefault())
                listResearch.add(temp)
                temp = temp.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
                listResearch.add(temp)
            }
        }

        if (listResearch.isNotEmpty()) for (tag in listResearch) {

                if (!repasList.filter { se -> se.name == tag }.isEmpty()) {
                    for (repas in repasList.filter { se -> se.name == tag }
                        .sortedBy { s -> s.name }) {
                        if (!resultResearch.contains(repas)) {
                            resultResearch.add(repas)
                        }
                    }
                }

                if (!repasList.filter { se -> se.tags.contains(tag) }.isEmpty()) {
                    for (repas in repasList.filter { se -> se.tags.contains(tag) }
                        .sortedBy { s -> s.name }) {
                        if (!resultResearch.contains(repas)) {
                            resultResearch.add(repas)
                        }
                    }
                }

                if (!ingredientList.filter { se -> se.name == tag }.isEmpty()) {
                    for (ingredient in ingredientList.filter { se -> se.name == tag }) {
                        for (repas in repasList.filter { se -> se.id == ingredient.id_repas }) {
                            if (!resultResearch.contains(repas)) {
                                resultResearch.add(repas)
                            }
                        }
                    }
                }

                if (!categorieList.filter { se -> se.name == tag }.isEmpty()) {
                    for (categorie in categorieList.filter { se -> se.name == tag }) {
                        for (ingredient in ingredientList.filter { se -> se.id_categorie == categorie.id }) {
                            for (repas in repasList.filter { se -> se.id == ingredient.id_repas }) {
                                if (!resultResearch.contains(repas)) {
                                    resultResearch.add(repas)
                                }
                            }
                        }
                    }
                }

                if (!repasList.filter { se -> se.name.contains(tag) }.isEmpty()) {
                    for (repas in repasList.filter { se -> se.name.contains(tag) }
                        .sortedBy { s -> s.name }) {
                        if (!resultResearch.contains(repas)) {
                            resultResearch.add(repas)
                        }
                    }
                }
                if (!ingredientList.filter { se -> se.name.contains(tag) }.isEmpty()) {
                    for (ingredient in ingredientList.filter { se -> se.name.contains(tag) }) {
                        for (repas in repasList.filter { se -> se.id == ingredient.id_repas }) {
                            if (!resultResearch.contains(repas)) {
                                resultResearch.add(repas)
                            }
                        }
                    }
                }

                if (!categorieList.filter { se -> se.name.contains(tag) }.isEmpty()) {
                    for (categorie in categorieList.filter { se -> se.name.contains(tag) }) {
                        for (ingredient in ingredientList.filter { se -> se.id_categorie == categorie.id }) {
                            for (repas in repasList.filter { se -> se.id == ingredient.id_repas }) {
                                if (!resultResearch.contains(repas)) {
                                    resultResearch.add(repas)
                                }
                            }
                        }
                    }
                }
            }


        return resultResearch

    }

}