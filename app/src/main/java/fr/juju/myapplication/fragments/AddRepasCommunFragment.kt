package fr.juju.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import fr.juju.myapplication.*
import fr.juju.myapplication.RepasCommunRepository.Singleton.repasCommunList
import fr.juju.myapplication.RepasRepository.Singleton.repasList
import fr.juju.myapplication.adapter.RepasCommunAdapter

class AddRepasCommunFragment (val context: MainActivity
) : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.add_repas_commun_fragment, container, false)
        val recyclerView = view.findViewById<RecyclerView>(R.id.RepasCommunRecyclerView)
        val auth = FirebaseAuth.getInstance().currentUser
        var liste = arrayListOf<RepasCommunModel>()
        for (repas in repasCommunList.filter { s->s.createur != auth?.email }){
            if(repasList.filter{ s->s.id == repas.id}.isEmpty()){
                liste.add(repas)
            }
        }
        recyclerView.adapter = RepasCommunAdapter(context, liste, R.layout.item_repas_vertical)
        recyclerView.layoutManager = LinearLayoutManager(context)
        return view
    }

}