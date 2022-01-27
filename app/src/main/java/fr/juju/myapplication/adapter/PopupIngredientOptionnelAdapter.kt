package fr.juju.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.juju.myapplication.*

class PopupIngredientOptionnelAdapter (val context: MainActivity, private val ingredients: ArrayList<IngredientModel>, private val layoutId:Int) : RecyclerView.Adapter<PopupIngredientOptionnelAdapter.ViewHolder>() {


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView? = view.findViewById(R.id.name)
        val quantite: TextView? = view.findViewById(R.id.quantite)
        val checkBox: CheckBox? = view.findViewById(R.id.checkBox)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentIngredient = ingredients[position]
        holder.name?.text = currentIngredient.name
        holder.quantite?.text = currentIngredient.quantite
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }
}

