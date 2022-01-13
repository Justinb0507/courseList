package fr.juju.myapplication.fragments

import android.opengl.Visibility
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import fr.juju.myapplication.MainActivity
import fr.juju.myapplication.R

class FiltreRepasFragment(
    val context: MainActivity,
    val time: String,
    val selectedDay: String
) : Fragment(){

override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {

    val view = inflater?.inflate(R.layout.fragment_filtrerepas, container, false)
    view.findViewById<ImageView>(R.id.add_recette).setOnClickListener{
        context.unprintSoir()
        context.loadFragment(AddRepasFragment(context))
    }
    view.findViewById<ImageView>(R.id.plat).setOnClickListener{
        context.loadFragment(ResultResearchFragment(context, "Plat", "categorie", time, selectedDay))
    }
    view.findViewById<ImageView>(R.id.dessert).setOnClickListener{
        context.loadFragment(ResultResearchFragment(context, "Dessert", "categorie", time, selectedDay))
    }
    view.findViewById<ImageView>(R.id.soupe).setOnClickListener{
        context.loadFragment(ResultResearchFragment(context, "Soupe", "categorie", time, selectedDay))
    }
    view.findViewById<ImageView>(R.id.apero).setOnClickListener{
        context.loadFragment(ResultResearchFragment(context, "Apero", "categorie", time, selectedDay))
    }

    view.findViewById<Button>(R.id.research).setOnClickListener{
        context.loadFragment(ResultResearchFragment(context, view.findViewById<EditText>(R.id.research_input).text.toString(), "nope", time, selectedDay))
    }

    val translate = AnimationUtils.loadAnimation(context, R.anim.translate_anim)
    view.findViewById<ConstraintLayout>(R.id.constraint).startAnimation(translate)

    return view
}
}