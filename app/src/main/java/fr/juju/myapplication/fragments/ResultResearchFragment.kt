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


class ResultResearchFragment(
    private val context:MainActivity,
    private val parameter: String,
    private val categorie: String,
) : Fragment()  {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater?.inflate(R.layout.fragment_resultresearch, container, false)
        val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.repas_list)
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

        if(categorie == "categorie"){
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
        }

        else {
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
        }

        collectionRecyclerView.adapter = RepasAdapter(context, resultResearch, R.layout.item_repas_vertical)
        collectionRecyclerView.layoutManager = LinearLayoutManager(context)
        collectionRecyclerView.addItemDecoration(RepasItemDecoration())

        return view
    }
}