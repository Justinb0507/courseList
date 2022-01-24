package fr.juju.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.juju.myapplication.CourseModel
import fr.juju.myapplication.IngredientModel
import fr.juju.myapplication.MainActivity
import fr.juju.myapplication.R

class CourseItemAdapter(
    val context: MainActivity,
    private val courseList: ArrayList<CourseModel>,
    private val layoutId:Int)
    : RecyclerView.Adapter<CourseItemAdapter.ViewHolder>()
{
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name: TextView? = view.findViewById(R.id.text_item)
        val quantite: TextView? = view.findViewById(R.id.quantity_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentIngredient = courseList[position]
        holder.name?.text = currentIngredient.name
        holder.quantite?.text = currentIngredient.quantite
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

}