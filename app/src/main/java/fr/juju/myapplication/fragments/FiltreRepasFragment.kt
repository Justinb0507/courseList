package fr.juju.myapplication.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.opengl.Visibility
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
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
    val selectedDay: String,
    val currentSemaine: String
) : Fragment(){

@SuppressLint("ResourceAsColor")
override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {

    val view = inflater?.inflate(R.layout.fragment_filtrerepas, container, false)
    view.findViewById<ImageView>(R.id.add_recette).setOnClickListener{
        context.unprintSoir()
        context.unprintMidi()
        context.unprintApero()
        context.loadFragment(AddRepasFragment(context))
    }
    view.findViewById<ImageView>(R.id.dessert_rouge).alpha = 0F
    view.findViewById<ImageView>(R.id.plat_rouge).alpha = 0F
    view.findViewById<ImageView>(R.id.soupe_rouge).alpha = 0F
    view.findViewById<ImageView>(R.id.apero_rouge).alpha = 0F

    view.findViewById<ImageView>(R.id.plat).setOnClickListener{
        context.hideKeyboard()
        if (categorieFiltre("Plat Plats").isNotEmpty()){
            context.loadFragment(ResultResearchFragment(context, categorieFiltre("Plat Plats"), time, selectedDay, currentSemaine))
        }
        else {
            view.findViewById<ImageView>(R.id.plat_rouge).alpha = 0.5F
            view.findViewById<ImageView>(R.id.plat_rouge).animate().apply {
                duration = 30
                translationY(10F)
                rotation(4F)
            }.withEndAction {
                view.findViewById<ImageView>(R.id.plat_rouge).animate().apply {
                    duration = 30
                    translationY(0F)
                    rotation(0F)
                }
            }.start()

            view.findViewById<ImageView>(R.id.plat).animate().apply {
                duration = 30
                translationY(10F)
                rotation(4F)
            }.withEndAction {
                view.findViewById<ImageView>(R.id.plat).animate().apply {
                    duration = 30
                    translationY(0F)
                    rotation(0F)
                }
            }.start()

        }

    }

    view.findViewById<ImageView>(R.id.dessert).setOnClickListener{
        context.hideKeyboard()
        if (categorieFiltre("Dessert Desserts").isNotEmpty()){
            context.loadFragment(ResultResearchFragment(context, categorieFiltre("Dessert Desserts"), time, selectedDay, currentSemaine))
        }
        else {
            view.findViewById<ImageView>(R.id.dessert_rouge).alpha = 0.5F
            view.findViewById<ImageView>(R.id.dessert_rouge).animate().apply {
                duration = 30
                translationY(10F)
                rotation(4F)
            }.withEndAction {
                view.findViewById<ImageView>(R.id.dessert_rouge).animate().apply {
                    duration = 30
                    translationY(0F)
                    rotation(0F)
                }
            }.start()

            view.findViewById<ImageView>(R.id.dessert).animate().apply {
                duration = 30
                translationY(10F)
                rotation(4F)
            }.withEndAction {
                view.findViewById<ImageView>(R.id.dessert).animate().apply {
                    duration = 30
                    translationY(0F)
                    rotation(0F)
                }
            }.start()

        }

    }
    view.findViewById<ImageView>(R.id.soupe).setOnClickListener{
        context.hideKeyboard()
        if (categorieFiltre("Soupe Soupes").isNotEmpty()){
            context.loadFragment(ResultResearchFragment(context, categorieFiltre("Soupe Soupes"), time, selectedDay, currentSemaine))
        }
        else {
            view.findViewById<ImageView>(R.id.soupe_rouge).alpha = 0.5F
            view.findViewById<ImageView>(R.id.soupe_rouge).animate().apply {
                duration = 30
                translationY(10F)
                rotation(4F)
            }.withEndAction {
                view.findViewById<ImageView>(R.id.soupe_rouge).animate().apply {
                    duration = 30
                    translationY(0F)
                    rotation(0F)
                }
            }.start()

            view.findViewById<ImageView>(R.id.soupe).animate().apply {
                duration = 30
                translationY(10F)
                rotation(4F)
            }.withEndAction {
                view.findViewById<ImageView>(R.id.soupe).animate().apply {
                    duration = 30
                    translationY(0F)
                    rotation(0F)
                }
            }.start()

        }
    }
    view.findViewById<ImageView>(R.id.apero).setOnClickListener{
        context.hideKeyboard()

        if (categorieFiltre("Apéro Apero").isNotEmpty()){
            context.loadFragment(ResultResearchFragment(context, categorieFiltre("Apéro Apero"), time, selectedDay,currentSemaine))
        }
        else {
            view.findViewById<ImageView>(R.id.apero_rouge).alpha = 0.5F
            view.findViewById<ImageView>(R.id.apero_rouge).animate().apply {
                duration = 30
                translationY(10F)
                rotation(4F)
            }.withEndAction {
                view.findViewById<ImageView>(R.id.apero_rouge).animate().apply {
                    duration = 30
                    translationY(0F)
                    rotation(0F)
                }
            }.start()

            view.findViewById<ImageView>(R.id.apero).animate().apply {
                duration = 30
                translationY(10F)
                rotation(4F)
            }.withEndAction {
                view.findViewById<ImageView>(R.id.apero).animate().apply {
                    duration = 30
                    translationY(0F)
                    rotation(0F)
                }
            }.start()

        }
    }

    view.findViewById<Button>(R.id.research).setOnClickListener{
        if(research(view.findViewById<EditText>(R.id.research_input).text.toString()).isNotEmpty()){
            context.loadFragment(ResultResearchFragment(context, research(view.findViewById<EditText>(R.id.research_input).text.toString()), time, selectedDay, currentSemaine))
        }
        else {
            context.hideKeyboard()
            view.findViewById<ImageView>(R.id.searchbar).animate().alpha(0F).setDuration(10)
            view.findViewById<ImageView>(R.id.searchbar_red).animate().alpha(1F).setDuration(1)

            val scaleX = ObjectAnimator.ofFloat(view.findViewById<ImageView>(R.id.searchbar_red), View.SCALE_X, 1.0f, 1.1f).setDuration(200)
            val scaleY = ObjectAnimator.ofFloat(view.findViewById<ImageView>(R.id.searchbar_red), View.SCALE_Y, 1.0f, 1.1f).setDuration(200)
            val downscaleX = ObjectAnimator.ofFloat(view.findViewById<ImageView>(R.id.searchbar_red), View.SCALE_X, 1.1f, 1.0f).setDuration(200)
            val downscaleY = ObjectAnimator.ofFloat(view.findViewById<ImageView>(R.id.searchbar_red), View.SCALE_Y, 1.1f, 1.0f).setDuration(200)

            val set = AnimatorSet()
            set.playTogether(scaleX, scaleY)
            set.play(downscaleX).after(scaleX)
            set.playTogether(downscaleX, downscaleY)
            set.playTogether(scaleX, scaleY)
            set.play(downscaleX).after(scaleX)
            set.playTogether(downscaleX, downscaleY)
            set.start()
        }
    }
    view.findViewById<EditText>(R.id.research_input).setOnKeyListener(
        View.OnKeyListener { v, keyCode, event ->
        if (keyCode == KeyEvent.KEYCODE_ENTER) {
            context.hideKeyboard()
            if(research(view.findViewById<EditText>(R.id.research_input).text.toString()).isNotEmpty()){
                context.loadFragment(ResultResearchFragment(context, research(view.findViewById<EditText>(R.id.research_input).text.toString()), time, selectedDay, currentSemaine))
            }
            else {
                view.findViewById<ImageView>(R.id.searchbar).animate().alpha(0F).setDuration(10)
                view.findViewById<ImageView>(R.id.searchbar_red).animate().alpha(1F).setDuration(1)

                val scaleX = ObjectAnimator.ofFloat(view.findViewById<ImageView>(R.id.searchbar_red), View.SCALE_X, 1.0f, 1.1f).setDuration(200)
                val scaleY = ObjectAnimator.ofFloat(view.findViewById<ImageView>(R.id.searchbar_red), View.SCALE_Y, 1.0f, 1.1f).setDuration(200)
                val downscaleX = ObjectAnimator.ofFloat(view.findViewById<ImageView>(R.id.searchbar_red), View.SCALE_X, 1.1f, 1.0f).setDuration(200)
                val downscaleY = ObjectAnimator.ofFloat(view.findViewById<ImageView>(R.id.searchbar_red), View.SCALE_Y, 1.1f, 1.0f).setDuration(200)

                val set = AnimatorSet()
                set.playTogether(scaleX, scaleY)
                set.play(downscaleX).after(scaleX)
                set.playTogether(downscaleX, downscaleY)
                set.playTogether(scaleX, scaleY)
                set.play(downscaleX).after(scaleX)
                set.playTogether(downscaleX, downscaleY)
                set.start()
            }
            return@OnKeyListener true
        }
            else {
            view.findViewById<ImageView>(R.id.searchbar_red).animate().alpha(0F).setDuration(10)
            view.findViewById<ImageView>(R.id.searchbar).animate().alpha(1F).setDuration(1)
        }
            return@OnKeyListener false
        })

    view.findViewById<EditText>(R.id.research_input).setOnClickListener{
        view.findViewById<ImageView>(R.id.searchbar_red).animate().alpha(0F).setDuration(10)
        view.findViewById<ImageView>(R.id.searchbar).animate().alpha(1F).setDuration(1)
    }

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

    private fun categorieFiltre(parameter: String): ArrayList<RepasModel> {

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

            for (tag in listResearch) {
                if (!repasList.filter { se -> se.tags.contains(tag) }.isEmpty()) {
                    for (repas in repasList.filter { se -> se.tags.contains(tag) }
                        .sortedBy { s -> s.name }) {
                        if (!resultResearch.contains(repas)) {
                            resultResearch.add(repas)
                        }
                    }
                }
            }
        return resultResearch
    }

}