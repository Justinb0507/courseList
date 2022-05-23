package fr.juju.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import fr.juju.myapplication.*
import fr.juju.myapplication.activity.MainActivity
import fr.juju.myapplication.repository.RepasCommunRepository.Singleton.repasCommunList
import fr.juju.myapplication.repository.RepasRepository.Singleton.repasList
import fr.juju.myapplication.adapter.RepasCommunAdapter
import fr.juju.myapplication.model.RepasCommunModel
import fr.juju.myapplication.repository.RepasCommunRepository

class AddRepasCommunFragment (val context: MainActivity
) : Fragment() {
    class MultipleSelectRepas(
        var repas: RepasCommunModel,
        var selected: Boolean
    )



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.add_repas_commun_fragment, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.RepasCommunRecyclerView)
        val auth = FirebaseAuth.getInstance().currentUser
        var liste = arrayListOf<MultipleSelectRepas>()
        for (repas in repasCommunList.filter { s->s.createur != auth?.email }){
            if(repasList.filter{ s->s.id == repas.id}.isEmpty()){
                liste.add(MultipleSelectRepas(repas, false))
            }
        }
        recyclerView.adapter = RepasCommunAdapter(context, liste, R.layout.item_repas_vertical)
        recyclerView.layoutManager = LinearLayoutManager(context)

        var compteur = 0
        view.findViewById<ConstraintLayout>(R.id.ok).setOnClickListener{
            for(repas in liste){
                if(repas.selected){
                    RepasCommunRepository().retrieveData(repas.repas)
                    compteur+=1
                }
            }
            Toast.makeText(context, "Vous avez ajouté " + compteur.toString() + " à votre livre de recette !", Toast.LENGTH_SHORT).show()
            context.loadFragment(FiltreRepasFragment(context,"None","None","None"))
        }
        return view
    }

}