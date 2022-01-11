package fr.juju.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.Fragment
import fr.juju.myapplication.MainActivity
import fr.juju.myapplication.R

class filtreRepasFragment (val context: MainActivity
) : Fragment(){

override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {

    val view = inflater?.inflate(R.layout.fragment_filtrerepas, container, false)
    view.findViewById<ImageView>(R.id.add_recette).setOnClickListener{
        context.loadFragment(AddRepasFragment(context))
    }
    view.findViewById<ImageView>(R.id.plat).setOnClickListener{
        context.loadFragment(ResultResearchFragment(context, "Plat", "categorie"))
    }
    view.findViewById<ImageView>(R.id.dessert).setOnClickListener{
        context.loadFragment(ResultResearchFragment(context, "Dessert", "categorie"))
    }
    view.findViewById<ImageView>(R.id.soupe).setOnClickListener{
        context.loadFragment(ResultResearchFragment(context, "Soupe", "categorie"))
    }
    view.findViewById<ImageView>(R.id.apero).setOnClickListener{
        context.loadFragment(ResultResearchFragment(context, "Apero", "categorie"))
    }
    view.findViewById<ImageView>(R.id.param_research).setOnClickListener{
        context.loadFragment(ResultResearchFragment(context, "Apero", "nope"))
    }
    view.findViewById<Button>(R.id.research).setOnClickListener{
        context.loadFragment(ResultResearchFragment(context, view.findViewById<EditText>(R.id.research_input).text.toString(), "nope"))
    }

    return view
}
}