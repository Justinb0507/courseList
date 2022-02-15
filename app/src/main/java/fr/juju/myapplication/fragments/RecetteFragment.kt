package fr.juju.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.updateLayoutParams
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
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


class RecetteFragment(
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
        context.onBackPressedDispatcher.addCallback(context, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                context.handleBack()
            }})
        val view = inflater?.inflate(R.layout.fragment_recette, container, false)
        view.findViewById<TextView>(R.id.name).text = currentRepas.name
        view.findViewById<TextView>(R.id.description).text = currentRepas.description
        view.findViewById<TextView>(R.id.lien).text = currentRepas.lien
        view.findViewById<TextView>(R.id.recette_display).text = currentRepas.recette
        view.findViewById<TextView>(R.id.duree).text = currentRepas.duree
        val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.tags)
        collectionRecyclerView.adapter =
            TagsAdapter(context, currentRepas.tags, R.layout.item_tags_horizontal)

        view.findViewById<ImageView>(R.id.edit).setOnClickListener {
            context.loadFragment(
                EditRepasFragment(
                    context,
                    currentRepas,
                    ingredientList.filter { s -> s.id_repas == currentRepas.id }
                        .sortedBy { s -> s.rank })
            )
        }

        Glide.with(context)
            .load(currentRepas.imageUri)
            .into(view.findViewById<ImageView>(R.id.image_item))


        val repo = IngredientRepository()
        val listIngredientView = view.findViewById<RecyclerView>(R.id.list_ingredient)
        repo.updateData {
            listIngredientView.adapter = IngredientAdapter(
                context,
                ingredientList.filter { s -> s.id_repas == currentRepas.id }
                    .sortedBy { s -> s.rank },
                R.layout.item_ingredient_vertical
            )
            listIngredientView.layoutManager = LinearLayoutManager(context)
        }

        view?.findViewById<ConstraintLayout>(R.id.recetteCard)?.visibility = View.GONE
        view?.findViewById<View>(R.id.recette_soulignage)?.visibility = View.INVISIBLE
        view?.findViewById<ConstraintLayout>(R.id.planning)?.visibility = View.GONE
        view?.findViewById<ImageView>(R.id.icone_soir)?.visibility = View.GONE
        view?.findViewById<ImageView>(R.id.icone_midi)?.visibility = View.GONE
        view?.findViewById<ImageView>(R.id.icone_apero)?.visibility = View.GONE
        view?.findViewById<ImageView>(R.id.icone_autres)?.visibility = View.GONE

        if (time == "midi" && selectedDay != "None") {
            view?.findViewById<ImageView>(R.id.icone_midi)?.visibility = View.VISIBLE
            view?.findViewById<ConstraintLayout>(R.id.planning)?.visibility = View.VISIBLE
            context.animationMidi()
            context.unprintMidi()
            context.nonAnimationMidi()
            view.findViewById<ConstraintLayout>(R.id.planning).animate().alpha(1F).setDuration(150)
            context.unprintApero()
            context.unprintSoir()
            context.unprintAutres()
            view.findViewById<ConstraintLayout>(R.id.planning).setOnClickListener {
                if (currentSemaine == "suivant") {
                    if (ingredientList.filter { s -> s.id_repas == currentRepas.id }
                            .filter { s -> s.name.contains("optionnel") }.isNotEmpty()) {
                        IngredientOptionnelPopup(
                            context,
                            ingredientList.filter { s -> s.id_repas == currentRepas.id }
                                .filter { s -> s.name.contains("optionnel") } as ArrayList<IngredientModel>,
                            currentRepas,
                            time,
                            selectedDay).show()
                    } else {
                        addIngredientCourse(ingredientList.filter { s -> s.id_repas == currentRepas.id } as ArrayList<IngredientModel>)
                        if (semainierSuivantList.filter { s -> s.id_semainier == selectedDay }[0].midi != "None") {
                            deleteIngredientCourse(ingredientList.filter { s -> s.id_repas == semainierSuivantList.filter { s -> s.id_semainier == selectedDay }[0].midi } as ArrayList<IngredientModel>)
                        }
                        repoSuivant.setMidi(selectedDay, currentRepas.id)
                        Toast.makeText(
                            context,
                            "Repas ajouté pour le $selectedDay $time!",
                            Toast.LENGTH_SHORT
                        ).show()
                        context.loadFragment(SemainierFragment(context, selectedDay, "suivant"))
                        view?.findViewById<ImageView>(R.id.icone_midi)?.visibility = View.GONE
                    }
                } else {
                    repo2.setMidi(selectedDay, currentRepas.id)
                    Toast.makeText(
                        context,
                        "Repas ajouté pour le $selectedDay $time!",
                        Toast.LENGTH_SHORT
                    ).show()
                    context.loadFragment(SemainierFragment(context, selectedDay, "courant"))
                    view?.findViewById<ImageView>(R.id.icone_midi)?.visibility = View.GONE
                }

            }
        }
        if (time == "soir" && selectedDay != "None") {
            view?.findViewById<ImageView>(R.id.icone_soir)?.visibility = View.VISIBLE
            view?.findViewById<ConstraintLayout>(R.id.planning)?.visibility = View.VISIBLE
            context.animationSoir()
            context.unprintSoir()
            context.nonAnimationSoir()
            view.findViewById<ConstraintLayout>(R.id.planning).animate().alpha(1F).setDuration(150)
            context.unprintApero()
            context.unprintMidi()
            context.unprintAutres()

            view.findViewById<ConstraintLayout>(R.id.planning).setOnClickListener {
                if (currentSemaine == "suivant") {
                    if (ingredientList.filter { s -> s.id_repas == currentRepas.id }
                            .filter { s -> s.name.contains("optionnel") }.isNotEmpty()) {
                        IngredientOptionnelPopup(
                            context,
                            ingredientList.filter { s -> s.id_repas == currentRepas.id }
                                .filter { s -> s.name.contains("optionnel") } as ArrayList<IngredientModel>,
                            currentRepas,
                            time,
                            selectedDay).show()
                    } else {
                        addIngredientCourse(ingredientList.filter { s -> s.id_repas == currentRepas.id } as ArrayList<IngredientModel>)
                        if (semainierSuivantList.filter { s -> s.id_semainier == selectedDay }[0].soir != "None") {
                            deleteIngredientCourse(ingredientList.filter { s -> s.id_repas == semainierSuivantList.filter { s -> s.id_semainier == selectedDay }[0].soir } as ArrayList<IngredientModel>)
                        }
                        repoSuivant.setSoir(selectedDay, currentRepas.id)
                        Toast.makeText(
                            context,
                            "Repas ajouté pour le $selectedDay $time!",
                            Toast.LENGTH_SHORT
                        ).show()
                        context.loadFragment(SemainierFragment(context, selectedDay, "suivant"))
                        view?.findViewById<ImageView>(R.id.icone_soir)?.visibility = View.GONE
                    }
                } else {
                    repo2.setSoir(selectedDay, currentRepas.id)
                    Toast.makeText(
                        context,
                        "Repas ajouté pour le $selectedDay $time!",
                        Toast.LENGTH_SHORT
                    ).show()
                    context.loadFragment(SemainierFragment(context, selectedDay, "courant"))
                    view?.findViewById<ImageView>(R.id.icone_soir)?.visibility = View.GONE
                }

            }
        }
        if (time == "apero" && selectedDay != "None") {
            view?.findViewById<ImageView>(R.id.icone_apero)?.visibility = View.VISIBLE
            view?.findViewById<ConstraintLayout>(R.id.planning)?.visibility = View.VISIBLE
            context.animationApero()
            context.unprintApero()
            context.nonAnimationApero()
            view.findViewById<ConstraintLayout>(R.id.planning).animate().alpha(1F).setDuration(150)
            context.unprintSoir()
            context.unprintMidi()
            context.unprintAutres()

            view.findViewById<ConstraintLayout>(R.id.planning).setOnClickListener {
                if (currentSemaine == "suivant") {
                    if (ingredientList.filter { s -> s.id_repas == currentRepas.id }
                            .filter { s -> s.name.contains("optionnel") }.isNotEmpty()) {
                        IngredientOptionnelPopup(
                            context,
                            ingredientList.filter { s -> s.id_repas == currentRepas.id }
                                .filter { s -> s.name.contains("optionnel") } as ArrayList<IngredientModel>,
                            currentRepas,
                            time,
                            selectedDay).show()
                    } else {
                        addIngredientCourse(ingredientList.filter { s -> s.id_repas == currentRepas.id } as ArrayList<IngredientModel>)
                        if (semainierSuivantList.filter { s -> s.id_semainier == selectedDay }[0].apero != "None") {
                            deleteIngredientCourse(ingredientList.filter { s -> s.id_repas == semainierSuivantList.filter { s -> s.id_semainier == selectedDay }[0].apero } as ArrayList<IngredientModel>)
                        }
                        repoSuivant.setApero(selectedDay, currentRepas.id)
                        Toast.makeText(
                            context,
                            "Repas ajouté pour le $selectedDay $time!",
                            Toast.LENGTH_SHORT
                        ).show()
                        context.loadFragment(SemainierFragment(context, selectedDay, "suivant"))
                        view?.findViewById<ImageView>(R.id.icone_apero)?.visibility = View.GONE
                    }
                } else {
                    repo2.setApero(selectedDay, currentRepas.id)
                    Toast.makeText(
                        context,
                        "Repas ajouté pour l\'$time du $selectedDay !",
                        Toast.LENGTH_SHORT
                    ).show()
                    context.loadFragment(SemainierFragment(context, selectedDay, "courant"))
                    view?.findViewById<ImageView>(R.id.icone_apero)?.visibility = View.GONE
                }
            }
        }
        if (time == "autres" && selectedDay != "None") {
            view?.findViewById<ImageView>(R.id.icone_autres)?.visibility = View.VISIBLE
            view?.findViewById<ConstraintLayout>(R.id.planning)?.visibility = View.VISIBLE
            context.animationAutres()
            context.unprintAutres()
            context.nonAnimationAutres()
            view.findViewById<ConstraintLayout>(R.id.planning).animate().alpha(1F).setDuration(150)
            context.unprintApero()
            context.unprintSoir()
            context.unprintMidi()
            view.findViewById<ConstraintLayout>(R.id.planning).setOnClickListener {
                if (currentSemaine == "suivant") {
                    if (ingredientList.filter { s -> s.id_repas == currentRepas.id }
                            .filter { s -> s.name.contains("optionnel") }.isNotEmpty()) {
                        IngredientOptionnelPopup(
                            context,
                            ingredientList.filter { s -> s.id_repas == currentRepas.id }
                                .filter { s -> s.name.contains("optionnel") } as ArrayList<IngredientModel>,
                            currentRepas,
                            time,
                            selectedDay).show()
                    } else {
                        addIngredientCourse(ingredientList.filter { s -> s.id_repas == currentRepas.id } as ArrayList<IngredientModel>)
                        repoSuivant.setAutres(selectedDay, currentRepas.id)
                        Toast.makeText(
                            context,
                            "Repas ajouté pour le $selectedDay $time!",
                            Toast.LENGTH_SHORT
                        ).show()
                        context.loadFragment(SemainierFragment(context, selectedDay, "suivant"))
                        view?.findViewById<ImageView>(R.id.icone_midi)?.visibility = View.GONE
                    }
                } else {
                    repo2.setAutres(selectedDay, currentRepas.id)
                    Toast.makeText(
                        context,
                        "Repas ajouté pour le $selectedDay $time!",
                        Toast.LENGTH_SHORT
                    ).show()
                    context.loadFragment(SemainierFragment(context, selectedDay, "courant"))
                    view?.findViewById<ImageView>(R.id.icone_autres)?.visibility = View.GONE
                }

            }
        }
        view.findViewById<TextView>(R.id.ingredients).setOnClickListener {
            switcher("ingredient")
        }
        view.findViewById<TextView>(R.id.recette).setOnClickListener {
            switcher("recette")
        }

        if (currentRepas.lien.isEmpty()) {
            view?.findViewById<ImageView>(R.id.imageView7)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.lien)?.visibility = View.GONE
            view?.findViewById<ImageView>(R.id.clock)
                ?.updateLayoutParams<ConstraintLayout.LayoutParams> {
                    startToStart = view?.findViewById<LinearLayout>(R.id.linearLayout2)?.id!!
                    topToBottom = view?.findViewById<LinearLayout>(R.id.linearLayout2)?.id!!
                }

        }


        return view
    }

    private fun switcher(switch: String) {
        if (switch == "ingredient") {
            view?.findViewById<ConstraintLayout>(R.id.recetteCard)?.visibility = View.GONE
            view?.findViewById<ConstraintLayout>(R.id.ingredientCard)?.visibility = View.VISIBLE
            view?.findViewById<View>(R.id.recette_soulignage)?.visibility = View.INVISIBLE
            view?.findViewById<View>(R.id.ingredient_soulignage)?.visibility = View.VISIBLE


        } else if (switch == "recette") {
            view?.findViewById<ConstraintLayout>(R.id.recetteCard)?.visibility = View.VISIBLE
            view?.findViewById<ConstraintLayout>(R.id.ingredientCard)?.visibility = View.GONE
            view?.findViewById<View>(R.id.recette_soulignage)?.visibility = View.VISIBLE
            view?.findViewById<View>(R.id.ingredient_soulignage)?.visibility = View.INVISIBLE
        }
    }

    private fun addIngredientCourse(ingredients: ArrayList<IngredientModel>) {
        var repo = CourseRepository()
        repo.addIngredientCourse(ingredients)
    }

    private fun deleteIngredientCourse(ingredients: ArrayList<IngredientModel>){
        var repo = CourseRepository()
        repo.deleteIngredientCourse(ingredients)
    }
}