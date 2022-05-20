package fr.juju.myapplication.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import fr.juju.myapplication.model.IngredientModel
import fr.juju.myapplication.activity.MainActivity
import fr.juju.myapplication.R

class EditIngredientAdapter (val context: MainActivity, private val ingredientList: ArrayList<IngredientModel>, private val layoutId:Int) : RecyclerView.Adapter<EditIngredientAdapter.ViewHolder>(){

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name: EditText? = view.findViewById(R.id.text_item)
        val img: ImageView? = view.findViewById(R.id.delete_ingredient)
        val quantite: EditText? = view.findViewById(R.id.quantity_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentIngredient = ingredientList[position]
        holder.name?.setText(currentIngredient.name)
        holder.name?.addTextChangedListener(
            object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    ingredientList[holder.adapterPosition].name = s.toString()
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // Fires right before text is changing
                }

                override fun afterTextChanged(s: Editable) {
                }
            }

        )

        holder.quantite?.addTextChangedListener(
            object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    ingredientList[holder.adapterPosition].quantite = s.toString()
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // Fires right before text is changing
                }

                override fun afterTextChanged(s: Editable) {
                }
            }

        )
        holder.quantite?.setText(currentIngredient.quantite)
        holder.img?.setOnClickListener {
            ingredientList.remove(currentIngredient)
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return ingredientList.size
    }
}