package fr.juju.myapplication.fragments

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
import fr.juju.myapplication.adapter.IngredientAdapter
import fr.juju.myapplication.adapter.TagsAdapter


class RecetteFragment (
    private val context: MainActivity,
    private val currentRepas: RepasModel
) : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

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

        val imageref = Firebase.storage.reference.child(currentRepas.imageUri)
        imageref.downloadUrl.addOnSuccessListener {Uri->
            val imageURL = Uri.toString()
            val imagetest = view.findViewById<ImageView>(R.id.image_item)
            Glide.with(context)
                .load(imageURL)
                .into(imagetest)
        }

        val repo = IngredientRepository()
        val listIngredientView = view.findViewById<RecyclerView>(R.id.list_ingredient)
        repo.updateData {
            listIngredientView.adapter = IngredientAdapter(context,ingredientList.filter { s->s.id_repas == currentRepas.id }, R.layout.item_ingredient_vertical)
            listIngredientView.layoutManager = LinearLayoutManager(context)
        }
        //Set to linerarlayout to ingredients
        view?.findViewById<ConstraintLayout>(R.id.recetteCard)?.visibility = View.GONE
        view?.findViewById<View>(R.id.recette_soulignage)?.visibility = View.INVISIBLE




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