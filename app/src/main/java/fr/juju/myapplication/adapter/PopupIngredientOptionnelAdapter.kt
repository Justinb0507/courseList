package fr.juju.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.juju.myapplication.*
import fr.juju.myapplication.CategorieRepository.Singleton.categorieList
import fr.juju.myapplication.CourseRepository.Singleton.courseList
import java.util.*
import kotlin.collections.ArrayList

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
        holder.name?.setOnClickListener{
            holder.checkBox?.performClick()
        }
        holder.quantite?.setOnClickListener{
            holder.checkBox?.performClick()
        }
        holder.checkBox?.setOnClickListener{
            if(holder.checkBox?.isChecked){
                addIngredientCourse(arrayListOf(currentIngredient))
            } else deleteIngredientCourse(arrayListOf(currentIngredient))
        }
    }
    private fun addIngredientCourse(ingredients: ArrayList<IngredientModel>) {
        var repo = CourseRepository()
        repo.addIngredientCourse(ingredients)
    }

    private fun deleteIngredientCourse(ingredients: ArrayList<IngredientModel>) {
        var repo = CourseRepository()
        repo.deleteIngredientCourse(ingredients)
    }

    override fun getItemCount(): Int {
        return ingredients.size
    }
}

