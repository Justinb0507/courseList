package fr.juju.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.juju.myapplication.*

class CourseCategoryAdapter(
    val context: MainActivity,
    private val courseList: ArrayList<CourseModel>,
    private val category: ArrayList<String>,
    private val layoutId: Int
)
    : RecyclerView.Adapter<CourseCategoryAdapter.ViewHolder>()  {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name: TextView? = view.findViewById(R.id.text_categorie)
        val recycler: RecyclerView? = view.findViewById(R.id.itemByCategories)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentCat = category[position]
        holder.name?.text = currentCat
        var repo = CourseRepository()
        repo.updateData {
            holder.recycler?.adapter = CourseItemAdapter(context, courseList.filter { s->s.categorie ==  currentCat } as ArrayList<CourseModel>,R.layout.item_course_in_category_vertical)
            holder.recycler?.layoutManager = LinearLayoutManager(context)
        }
    }

    override fun getItemCount(): Int {
        return category.size
    }
}