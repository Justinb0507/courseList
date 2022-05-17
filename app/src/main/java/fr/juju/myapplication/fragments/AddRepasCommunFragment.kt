package fr.juju.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import fr.juju.myapplication.*
import fr.juju.myapplication.RepasCommunRepository.Singleton.repasCommunList

class AddRepasCommunFragment (val context: MainActivity
) : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.add_repas_commun_fragment, container, false)

        return view
    }

    fun retrieveData(){
        val repo = RepasRepository()
        val repo2 = IngredientRepository()
        repo.insertRepas(
            RepasModel(
                repasCommunList[0].id,
                repasCommunList[0].name,
                repasCommunList[0].description,
                repasCommunList[0].imageUri,
                repasCommunList[0].recette,
                repasCommunList[0].quantite,
                repasCommunList[0].tags,
                repasCommunList[0].duree
        ))
        for(ingredient in  repasCommunList[0].ingredientsList){
            repo2.insertIngredient(ingredient)
        }
        Toast.makeText(context,"Repas ajouté :" + repasCommunList[0].name, Toast.LENGTH_SHORT).show()
    }
}