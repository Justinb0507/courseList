package fr.juju.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.common.api.internal.LifecycleCallback
import fr.juju.myapplication.activity.MainActivity
import fr.juju.myapplication.R
import fr.juju.myapplication.fragments.CourseListeFragment

class CategorieSelectAdapter(
    val context: MainActivity,
    private val tags: List<String>,
    private val layoutId1: EditText,
    private val layoutId: Int
) : RecyclerView.Adapter<CategorieSelectAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name: TextView? = view.findViewById(R.id.text_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentTag = tags[position]
        holder.name?.text = currentTag
        holder.name?.setOnClickListener{
            layoutId1.setText( holder.name?.text)
        }

    }

    override fun getItemCount(): Int {
        return tags.size
    }
}