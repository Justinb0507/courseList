package fr.juju.myapplication.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import fr.juju.myapplication.*
import fr.juju.myapplication.IngredientRepository.Singleton.ingredientList
import fr.juju.myapplication.SemainierRepository.Singleton.semainierList
import fr.juju.myapplication.adapter.IngredientAdapter
import fr.juju.myapplication.adapter.TagsAdapter


class RecetteFragment (
    private val context: MainActivity,
    private val currentRepas: RepasModel,
    private val time: String,
    private val selectedDay: String
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val repo2 = SemainierRepository()

        val view = inflater?.inflate(R.layout.fragment_recette, container, false)
        view.findViewById<TextView>(R.id.name).text = currentRepas.name
        view.findViewById<TextView>(R.id.description).text = currentRepas.description
        view.findViewById<TextView>(R.id.lien).text = currentRepas.lien
        view.findViewById<TextView>(R.id.recette_display).text = currentRepas.recette
        view.findViewById<TextView>(R.id.duree).text = currentRepas.duree
        val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.tags)
        collectionRecyclerView.adapter = TagsAdapter(context, currentRepas.tags, R.layout.item_tags_horizontal)

        view.findViewById<ImageView>(R.id.edit).setOnClickListener{
            context.loadFragment(EditRepasFragment(context,currentRepas, ingredientList.filter { s->s.id_repas == currentRepas.id }))
        }

        Glide.with(context)
            .load(currentRepas.imageUri)
            .into(view.findViewById<ImageView>(R.id.image_item))


        val repo = IngredientRepository()
        val listIngredientView = view.findViewById<RecyclerView>(R.id.list_ingredient)
        repo.updateData {
            listIngredientView.adapter = IngredientAdapter(context,ingredientList.filter { s->s.id_repas == currentRepas.id }, R.layout.item_ingredient_vertical)
            listIngredientView.layoutManager = LinearLayoutManager(context)
        }
        //Set to linerarlayout to ingredients
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
                repo2.setMidi(time, selectedDay, currentRepas.id)
                Toast.makeText(context, "Repas ajouté pour le $selectedDay $time!", Toast.LENGTH_SHORT).show()
                context.loadFragment(SemainierFragment(context, selectedDay))
                view?.findViewById<ImageView>(R.id.icone_midi)?.visibility = View.GONE
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
                repo2.setSoir(time, selectedDay, currentRepas.id)
                Toast.makeText(context, "Repas ajouté pour le $selectedDay $time!", Toast.LENGTH_SHORT).show()
                context.loadFragment(SemainierFragment(context, selectedDay))
                view?.findViewById<ImageView>(R.id.icone_soir)?.visibility = View.GONE
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
                repo2.setApero(time, selectedDay, currentRepas.id)
                Toast.makeText(context, "Repas ajouté pour l\'$time du $selectedDay !", Toast.LENGTH_SHORT).show()
                context.loadFragment(SemainierFragment(context, selectedDay))
                view?.findViewById<ImageView>(R.id.icone_apero)?.visibility = View.GONE
            }
        }

        view.findViewById<TextView>(R.id.ingredients).setOnClickListener{
            switcher("ingredient")
        }
        view.findViewById<TextView>(R.id.recette).setOnClickListener{
            switcher("recette")
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

}