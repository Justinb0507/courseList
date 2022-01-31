package fr.juju.myapplication.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import fr.juju.myapplication.*

import fr.juju.myapplication.RepasRepository.Singleton.repasList
import fr.juju.myapplication.SemainierRepository.Singleton.semainierList
import fr.juju.myapplication.adapter.TagsAdapter
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment
    (val context: MainActivity
) : Fragment()  {

    val currentDay = SimpleDateFormat("EEEE", Locale.FRANCE).format(Date())

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater?.inflate(R.layout.fragment_home, container, false)
        //var currentDays= SemainierModel()
        var currentDays = semainierList.filter{s->s.id_semainier == currentDay}[0]

        if (currentDays.midi != "None"){
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.visibility = View.VISIBLE
            if(repasList.filter { s->s.id == currentDays.midi }.isNotEmpty()){
                var currentRepasMidi = repasList.filter { s->s.id == currentDays.midi }[0]
                view?.findViewById<ConstraintLayout>(R.id.Midi)?.setOnClickListener{
                    context.loadFragment(RecetteFragment(context, currentRepasMidi, "None", "None", "None"))
                }
                view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
                view?.findViewById<TextView>(R.id.nomMidi)?.text  = currentRepasMidi.name
                view?.findViewById<TextView>(R.id.descriptionMidi)?.text  = currentRepasMidi.duree
                Glide.with(context)
                    .load(Uri.parse(currentRepasMidi.imageUri))
                    .into(view.findViewById<ImageView>(R.id.image_item2))

                val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.tagListMidi)
                collectionRecyclerView.adapter = TagsAdapter(context, currentRepasMidi.tags, R.layout.item_tags_vertical)
                collectionRecyclerView.layoutManager = LinearLayoutManager(context)

            } else {
                view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
                view?.findViewById<TextView>(R.id.nomMidi)?.text  = currentDays.midi
                view?.findViewById<TextView>(R.id.descriptionMidi)?.visibility = View.GONE
                view.findViewById<RecyclerView>(R.id.tagListMidi)?.visibility = View.GONE
                view.findViewById<CardView>(R.id.cardView2)?.visibility = View.GONE
            }
        }else {
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.visibility = View.GONE
            }

        if (currentDays.soir != "None"){
            view?.findViewById<ConstraintLayout>(R.id.Soir)?.visibility = View.VISIBLE
            if (repasList.filter { s->s.id == currentDays.soir }.isNotEmpty()){
                var currentRepasSoir = repasList.filter { s->s.id == currentDays.soir }[0]
                view?.findViewById<ConstraintLayout>(R.id.Soir)?.setOnClickListener{
                    context.loadFragment(RecetteFragment(context, currentRepasSoir, "None", "None", "None"))
                }
                view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
                view?.findViewById<TextView>(R.id.nomSoir)?.text  = currentRepasSoir.name
                view?.findViewById<TextView>(R.id.descriptionSoir)?.text  = currentRepasSoir.duree
                Glide.with(context)
                    .load(currentRepasSoir.imageUri)
                    .into(view.findViewById<ImageView>(R.id.image_item3))

                val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.tagListSoir)
                collectionRecyclerView.adapter = TagsAdapter(context, currentRepasSoir.tags, R.layout.item_tags_vertical)
                collectionRecyclerView.layoutManager = LinearLayoutManager(context)
            } else {
                view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
                view?.findViewById<TextView>(R.id.nomSoir)?.text  = currentDays.soir
                view?.findViewById<TextView>(R.id.descriptionSoir)?.visibility = View.GONE
                view.findViewById<RecyclerView>(R.id.tagListSoir)?.visibility = View.GONE
                view.findViewById<CardView>(R.id.cardView3)?.visibility = View.GONE
            }

        }else {
            view?.findViewById<ConstraintLayout>(R.id.Soir)?.visibility = View.GONE
        }

        if (currentDays.apero != "None"){
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.visibility = View.VISIBLE
            if(repasList.filter { s->s.id == currentDays.apero }.isNotEmpty()){
                var currentRepasApero = repasList.filter { s->s.id == currentDays.apero }[0]
                view?.findViewById<ConstraintLayout>(R.id.Apero)?.setOnClickListener{
                    context.loadFragment(RecetteFragment(context, currentRepasApero, "None", "None", "None"))
                }
                view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
                view?.findViewById<TextView>(R.id.nomApero)?.text  = currentRepasApero.name
                view?.findViewById<TextView>(R.id.descriptionApero)?.text  = currentRepasApero.duree

                Glide.with(context)
                    .load(currentRepasApero.imageUri)
                    .into(view.findViewById<ImageView>(R.id.image_item4))

                val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.tagListApero)
                collectionRecyclerView.adapter = TagsAdapter(context, currentRepasApero.tags, R.layout.item_tags_vertical)
                collectionRecyclerView.layoutManager = LinearLayoutManager(context)
            } else {
                view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
                view?.findViewById<TextView>(R.id.nomApero)?.text  = currentDays.apero
                view?.findViewById<TextView>(R.id.descriptionApero)?.visibility = View.GONE
                view.findViewById<RecyclerView>(R.id.tagListApero)?.visibility = View.GONE
                view.findViewById<CardView>(R.id.cardView4)?.visibility = View.GONE
            }

        }else {
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.visibility = View.GONE
        }

        if(currentDays.midi == "None" && currentDays.soir == "None" && currentDays.apero == "None"){
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.VISIBLE
            view?.findViewById<TextView>(R.id.currentDays)?.text = "Rien de prévu aujourd’hui ! :)"
            view?.findViewById<ConstraintLayout>(R.id.vite_recette)?.setOnClickListener{
                Toast.makeText(context, "Ben va falloir s'y mettre là hein...", Toast.LENGTH_SHORT).show()
            }

        }

        view.findViewById<Button>(R.id.research).setOnClickListener{
            if(research(view.findViewById<EditText>(R.id.research_input).text.toString()).isNotEmpty()){
                context.loadFragment(ResultResearchFragment(context, research(view.findViewById<EditText>(R.id.research_input).text.toString()), "None", "None", "None"))
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
                        context.loadFragment(ResultResearchFragment(context, research(view.findViewById<EditText>(R.id.research_input).text.toString()), "None", "None", "None"))
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

            if (!IngredientRepository.Singleton.ingredientList.filter { se -> se.name == tag }.isEmpty()) {
                for (ingredient in IngredientRepository.Singleton.ingredientList.filter { se -> se.name == tag }) {
                    for (repas in repasList.filter { se -> se.id == ingredient.id_repas }) {
                        if (!resultResearch.contains(repas)) {
                            resultResearch.add(repas)
                        }
                    }
                }
            }

            if (!CategorieRepository.Singleton.categorieList.filter { se -> se.name == tag }.isEmpty()) {
                for (categorie in CategorieRepository.Singleton.categorieList.filter { se -> se.name == tag }) {
                    for (ingredient in IngredientRepository.Singleton.ingredientList.filter { se -> se.id_categorie == categorie.id }) {
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
            if (!IngredientRepository.Singleton.ingredientList.filter { se -> se.name.contains(tag) }.isEmpty()) {
                for (ingredient in IngredientRepository.Singleton.ingredientList.filter { se -> se.name.contains(tag) }) {
                    for (repas in repasList.filter { se -> se.id == ingredient.id_repas }) {
                        if (!resultResearch.contains(repas)) {
                            resultResearch.add(repas)
                        }
                    }
                }
            }

            if (!CategorieRepository.Singleton.categorieList.filter { se -> se.name.contains(tag) }.isEmpty()) {
                for (categorie in CategorieRepository.Singleton.categorieList.filter { se -> se.name.contains(tag) }) {
                    for (ingredient in IngredientRepository.Singleton.ingredientList.filter { se -> se.id_categorie == categorie.id }) {
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