package fr.juju.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.juju.myapplication.*
import fr.juju.myapplication.activity.MainActivity
import fr.juju.myapplication.model.IngredientModel

class IngredientAdapter (val context: MainActivity, private val ingredientList: List<IngredientModel>, private val layoutId:Int) : RecyclerView.Adapter<IngredientAdapter.ViewHolder>(){

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name: TextView? = view.findViewById(R.id.text_item)
        val quantity: TextView? = view.findViewById(R.id.quantity_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentIngredient = ingredientList[position]
        holder.name?.text = currentIngredient.name
        holder.quantity?.text = currentIngredient.quantite

    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }
}