package fr.juju.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.juju.myapplication.IngredientModel
import fr.juju.myapplication.MainActivity
import fr.juju.myapplication.R

class EditIngredientAdapter (val context: MainActivity, private val ingredientList: ArrayList<IngredientModel>, private val layoutId:Int) : RecyclerView.Adapter<EditIngredientAdapter.ViewHolder>(){

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name: TextView? = view.findViewById(R.id.text_item)
        val img: ImageView? = view.findViewById(R.id.delete_ingredient)
        val quantite: TextView? = view.findViewById(R.id.quantity_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentIngredient = ingredientList[position]
        holder.name?.text = currentIngredient.name
        holder.quantite?.text = currentIngredient.quantite
        holder.img?.setOnClickListener{
            ingredientList.remove(currentIngredient)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }
}