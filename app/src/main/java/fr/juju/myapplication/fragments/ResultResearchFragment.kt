package fr.juju.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.juju.myapplication.CategorieRepository.Singleton.categorieList
import fr.juju.myapplication.IngredientRepository.Singleton.ingredientList
import fr.juju.myapplication.MainActivity
import fr.juju.myapplication.R
import fr.juju.myapplication.RepasModel
import fr.juju.myapplication.RepasRepository.Singleton.repasList
import fr.juju.myapplication.adapter.RepasAdapter
import fr.juju.myapplication.adapter.RepasItemDecoration
import java.util.*
import kotlin.collections.ArrayList


class ResultResearchFragment(
    private val context:MainActivity,
    val resultResearch: ArrayList<RepasModel> = arrayListOf<RepasModel>(),
    private val time: String,
    private val selectedDay: String
) : Fragment()  {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater?.inflate(R.layout.fragment_resultresearch, container, false)
        val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.repas_list)
        collectionRecyclerView.adapter = RepasAdapter(context, resultResearch, R.layout.item_repas_vertical, time,selectedDay)
        collectionRecyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }
}