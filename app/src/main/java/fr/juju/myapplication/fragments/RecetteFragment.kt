package fr.juju.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.juju.myapplication.*
import fr.juju.myapplication.CategorieRepository.Singleton.categorieList
import fr.juju.myapplication.CourseRepository.Singleton.courseList
import fr.juju.myapplication.IngredientRepository.Singleton.ingredientList
import fr.juju.myapplication.SemainierSuivantRepository.Singleton.semainierSuivantList
import fr.juju.myapplication.adapter.IngredientAdapter
import fr.juju.myapplication.adapter.TagsAdapter
import java.util.*
import kotlin.collections.ArrayList


class RecetteFragment (
    private val context: MainActivity,
    private val currentRepas: RepasModel,
    private val time: String,
    private val selectedDay: String,
    private val currentSemaine: String
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val repo2 = SemainierRepository()
        val repoSuivant = SemainierSuivantRepository()

        val view = inflater?.inflate(R.layout.fragment_recette, container, false)
        view.findViewById<TextView>(R.id.name).text = currentRepas.name
        view.findViewById<TextView>(R.id.description).text = currentRepas.description
        view.findViewById<TextView>(R.id.lien).text = currentRepas.lien
        view.findViewById<TextView>(R.id.recette_display).text = currentRepas.recette
        view.findViewById<TextView>(R.id.duree).text = currentRepas.duree
        val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.tags)
        collectionRecyclerView.adapter = TagsAdapter(context, currentRepas.tags, R.layout.item_tags_horizontal)

        view.findViewById<ImageView>(R.id.edit).setOnClickListener{
            context.loadFragment(EditRepasFragment(context,currentRepas, ingredientList.filter { s->s.id_repas == currentRepas.id }.sortedBy { s->s.rank }))
        }

        Glide.with(context)
            .load(currentRepas.imageUri)
            .into(view.findViewById<ImageView>(R.id.image_item))


        val repo = IngredientRepository()
        val listIngredientView = view.findViewById<RecyclerView>(R.id.list_ingredient)
        repo.updateData {
            listIngredientView.adapter = IngredientAdapter(context,ingredientList.filter { s->s.id_repas == currentRepas.id }.sortedBy { s->s.rank }, R.layout.item_ingredient_vertical)
            listIngredientView.layoutManager = LinearLayoutManager(context)
        }

        view?.findViewById<ConstraintLayout>(R.id.recetteCard)?.visibility = View.GONE
        view?.findViewById<View>(R.id.recette_soulignage)?.visibility = View.INVISIBLE
        view?.findViewById<ConstraintLayout>(R.id.planning)?.visibility = View.GONE
        view?.findViewById<ImageView>(R.id.icone_soir)?.visibility = View.GONE
        view?.findViewById<ImageView>(R.id.icone_midi)?.visibility = View.GONE
        view?.findViewById<ImageView>(R.id.icone_apero)?.visibility = View.GONE

        if (time == "midi" && selectedDay != "None"){
            view?.findViewById<ImageView>(R.id.icone_midi)?.visibility = View.VISIBLE
            view?.findViewById<ConstraintLayout>(R.id.planning)?.visibility = View.VISIBLE
            context.animationMidi()
            context.unprintMidi()
            context.nonAnimationMidi()
            view.findViewById<ConstraintLayout>(R.id.planning).animate().alpha(1F).setDuration(150)
            context.unprintApero()
            context.unprintSoir()
            view.findViewById<ConstraintLayout>(R.id.planning).setOnClickListener{
                if (currentSemaine == "suivant"){
                    if(ingredientList.filter { s->s.id_repas == currentRepas.id }.filter{ s->s.name.contains("optionnel") }.isNotEmpty()){
                        IngredientOptionnelPopup(context,ingredientList.filter { s->s.id_repas == currentRepas.id }.filter{ s->s.name.contains("optionnel") } as ArrayList<IngredientModel>, currentRepas, time, selectedDay).show()
                    }else{
                        addIngredientCourse(ingredientList.filter { s->s.id_repas == currentRepas.id } as ArrayList<IngredientModel>)
                        if(semainierSuivantList.filter{s->s.id_semainier == selectedDay}[0].midi != "None"){
                            deleteIngredientCourse(ingredientList.filter { s->s.id_repas == semainierSuivantList.filter{s->s.id_semainier == selectedDay}[0].midi } as ArrayList<IngredientModel>)
                        }
                        repoSuivant.setMidi(time, selectedDay, currentRepas.id)
                        Toast.makeText(context, "Repas ajouté pour le $selectedDay $time!", Toast.LENGTH_SHORT).show()
                        context.loadFragment(SemainierFragment(context, selectedDay,"suivant" ))
                        view?.findViewById<ImageView>(R.id.icone_midi)?.visibility = View.GONE
                    }
                }else {
                    repo2.setMidi(time, selectedDay, currentRepas.id)
                    Toast.makeText(context, "Repas ajouté pour le $selectedDay $time!", Toast.LENGTH_SHORT).show()
                    context.loadFragment(SemainierFragment(context, selectedDay,"courant"))
                    view?.findViewById<ImageView>(R.id.icone_midi)?.visibility = View.GONE
                }

            }
        }
        if (time == "soir" && selectedDay != "None"){
            view?.findViewById<ImageView>(R.id.icone_soir)?.visibility = View.VISIBLE
            view?.findViewById<ConstraintLayout>(R.id.planning)?.visibility = View.VISIBLE
            context.animationSoir()
            context.unprintSoir()
            context.nonAnimationSoir()
            view.findViewById<ConstraintLayout>(R.id.planning).animate().alpha(1F).setDuration(150)
            context.unprintApero()
            context.unprintMidi()
            view.findViewById<ConstraintLayout>(R.id.planning).setOnClickListener{
                if (currentSemaine == "suivant"){
                    if(ingredientList.filter { s->s.id_repas == currentRepas.id }.filter{ s->s.name.contains("optionnel") }.isNotEmpty()){
                        IngredientOptionnelPopup(context,ingredientList.filter { s->s.id_repas == currentRepas.id }.filter{ s->s.name.contains("optionnel") } as ArrayList<IngredientModel>, currentRepas, time, selectedDay).show()
                    }else{
                        addIngredientCourse(ingredientList.filter { s->s.id_repas == currentRepas.id } as ArrayList<IngredientModel>)
                        if(semainierSuivantList.filter{s->s.id_semainier == selectedDay}[0].soir != "None"){
                            deleteIngredientCourse(ingredientList.filter { s->s.id_repas == semainierSuivantList.filter{s->s.id_semainier == selectedDay}[0].soir } as ArrayList<IngredientModel>)
                        }
                        repoSuivant.setSoir(time, selectedDay, currentRepas.id)
                        Toast.makeText(context, "Repas ajouté pour le $selectedDay $time!", Toast.LENGTH_SHORT).show()
                        context.loadFragment(SemainierFragment(context, selectedDay,"suivant" ))
                        view?.findViewById<ImageView>(R.id.icone_soir)?.visibility = View.GONE
                    }
                }else {
                    repo2.setSoir(time, selectedDay, currentRepas.id)
                    Toast.makeText(context, "Repas ajouté pour le $selectedDay $time!", Toast.LENGTH_SHORT).show()
                    context.loadFragment(SemainierFragment(context, selectedDay, "courant"))
                    view?.findViewById<ImageView>(R.id.icone_soir)?.visibility = View.GONE
                }

            }
        }
        if (time == "apero" && selectedDay != "None"){
            view?.findViewById<ImageView>(R.id.icone_apero)?.visibility = View.VISIBLE
            view?.findViewById<ConstraintLayout>(R.id.planning)?.visibility = View.VISIBLE
            context.animationApero()
            context.unprintApero()
            context.nonAnimationApero()
            view.findViewById<ConstraintLayout>(R.id.planning).animate().alpha(1F).setDuration(150)
            context.unprintSoir()
            context.unprintMidi()
            view.findViewById<ConstraintLayout>(R.id.planning).setOnClickListener{
                if (currentSemaine == "suivant"){
                    if(ingredientList.filter { s->s.id_repas == currentRepas.id }.filter{ s->s.name.contains("optionnel") }.isNotEmpty()){
                        IngredientOptionnelPopup(context,ingredientList.filter { s->s.id_repas == currentRepas.id }.filter{ s->s.name.contains("optionnel") } as ArrayList<IngredientModel>, currentRepas, time, selectedDay).show()
                    }else{
                        addIngredientCourse(ingredientList.filter { s->s.id_repas == currentRepas.id } as ArrayList<IngredientModel>)
                        if(semainierSuivantList.filter{s->s.id_semainier == selectedDay}[0].apero != "None"){
                            deleteIngredientCourse(ingredientList.filter { s->s.id_repas == semainierSuivantList.filter{s->s.id_semainier == selectedDay}[0].apero } as ArrayList<IngredientModel>)
                        }
                        repoSuivant.setApero(time, selectedDay, currentRepas.id)
                        Toast.makeText(context, "Repas ajouté pour le $selectedDay $time!", Toast.LENGTH_SHORT).show()
                        context.loadFragment(SemainierFragment(context, selectedDay,"suivant" ))
                        view?.findViewById<ImageView>(R.id.icone_apero)?.visibility = View.GONE
                    }
                }else {
                    repo2.setApero(time, selectedDay, currentRepas.id)
                    Toast.makeText(context, "Repas ajouté pour l\'$time du $selectedDay !", Toast.LENGTH_SHORT).show()
                    context.loadFragment(SemainierFragment(context, selectedDay, "courant"))
                    view?.findViewById<ImageView>(R.id.icone_apero)?.visibility = View.GONE
                }
            }
        }

        view.findViewById<TextView>(R.id.ingredients).setOnClickListener{
            switcher("ingredient")
        }
        view.findViewById<TextView>(R.id.recette).setOnClickListener{
            switcher("recette")
        }

        if(currentRepas.lien.isEmpty()){
            view?.findViewById<ImageView>(R.id.imageView7)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.lien)?.visibility = View.GONE
            view?.findViewById<ImageView>(R.id.clock)?.updateLayoutParams<ConstraintLayout.LayoutParams> {
                startToStart = view?.findViewById<LinearLayout>(R.id.linearLayout2)?.id!!
                topToBottom = view?.findViewById<LinearLayout>(R.id.linearLayout2)?.id!!
            }

        }


        return view
    }

    private fun switcher(switch: String){
        if(switch == "ingredient"){
            view?.findViewById<ConstraintLayout>(R.id.recetteCard)?.visibility = View.GONE
            view?.findViewById<ConstraintLayout>(R.id.ingredientCard)?.visibility = View.VISIBLE
            view?.findViewById<View>(R.id.recette_soulignage)?.visibility = View.INVISIBLE
            view?.findViewById<View>(R.id.ingredient_soulignage)?.visibility = View.VISIBLE


        }
        else if(switch == "recette"){
            view?.findViewById<ConstraintLayout>(R.id.recetteCard)?.visibility = View.VISIBLE
            view?.findViewById<ConstraintLayout>(R.id.ingredientCard)?.visibility = View.GONE
            view?.findViewById<View>(R.id.recette_soulignage)?.visibility = View.VISIBLE
            view?.findViewById<View>(R.id.ingredient_soulignage)?.visibility = View.INVISIBLE
        }
    }

    private fun addIngredientCourse(ingredients: ArrayList<IngredientModel>){
        var repo = CourseRepository()

        for (ingredient in ingredients){
                    var courseItem = CourseModel(
                        UUID.randomUUID().toString(),
                        ingredient.name,
                        ingredient.quantite,
                        if (ingredient.id_categorie!="None") categorieList.filter { s->s.id == ingredient.id_categorie }[0].name else "Autres",
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
                            else if (oldItem.quantite.contains("sachet") && !oldItem.quantite.contains("sachets")){
                                value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("sachet")).replace(" ", "").toInt()
                            }
                            else if (oldItem.quantite.contains("sachets")){
                                value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("sachets")).replace(" ", "").toInt()
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
                            else if (ingredient.quantite.contains("sachet") && !ingredient.quantite.contains("sachets")){
                                var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("sachet")).replace(" ", "").toInt()
                                    oldItem.quantite = ((value + newValue).toString()) + " sachets"
                            }
                            else if (ingredient.quantite.contains("sachets")){
                                var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("sachets")).replace(" ", "").toInt()
                                oldItem.quantite = ((value + newValue).toString()) + " sachets"
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
                                    oldItem.quantite = "0 * au jugé"
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