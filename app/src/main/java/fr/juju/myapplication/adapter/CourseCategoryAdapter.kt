package fr.juju.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.juju.myapplication.*

class CourseCategoryAdapter(
    val context: MainActivity,
    private val courseList: ArrayList<CourseModel>,
    private val category: java.util.ArrayList<String>,
    private val printToggle: Boolean,
    private val layoutId: Int
)
    : RecyclerView.Adapter<CourseCategoryAdapter.ViewHolder>()  {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name: TextView? = view.findViewById(R.id.text_categorie)
        val recycler: RecyclerView? = view.findViewById(R.id.itemByCategories)
        val fleche_down: ImageView? = view.findViewById(R.id.fleche_down)
        val fleche_up: ImageView? = view.findViewById(R.id.fleche_up)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCat = category[position]

        holder.name?.text = currentCat
        holder.recycler?.adapter = CourseItemAdapter(context, courseList.filter { s->s.categorie ==  currentCat } as ArrayList<CourseModel>, printToggle, R.layout.item_course_in_category_vertical)
        holder.recycler?.layoutManager = LinearLayoutManager(context)

        holder.fleche_down?.setOnClickListener{
            holder.recycler?.visibility = View.VISIBLE
            holder.fleche_down?.visibility = View.GONE
            holder.fleche_up?.visibility = View.VISIBLE
        }
        holder.fleche_up?.setOnClickListener{
            holder.recycler?.visibility = View.GONE
            holder.fleche_up?.visibility = View.GONE
            holder.fleche_down?.visibility = View.VISIBLE
        }



    }

    override fun getItemCount(): Int {
        return category.size
    }
}