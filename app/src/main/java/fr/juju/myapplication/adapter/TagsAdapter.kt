package fr.juju.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.juju.myapplication.MainActivity
import fr.juju.myapplication.R

class TagsAdapter (val context: MainActivity, private val tags: List<String>, private val layoutId:Int) : RecyclerView.Adapter<TagsAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name: TextView? = view.findViewById(R.id.text_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TagsAdapter.ViewHolder, position: Int) {
        val currentTag = tags[position]
        holder.name?.text = currentTag
    }

    override fun getItemCount(): Int {
        return tags.size
    }
}