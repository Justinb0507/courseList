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
import android.view.View.OnLongClickListener




class EditTagsAdapter(val context: MainActivity, private val tags: ArrayList<String>, private val layoutId:Int): RecyclerView.Adapter<EditTagsAdapter.ViewHolder>() {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name: TextView? = view.findViewById(R.id.text_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EditTagsAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: EditTagsAdapter.ViewHolder, position: Int) {
        val currentTag = tags[position]
        holder.name?.text = currentTag
        holder.name?.setOnLongClickListener(OnLongClickListener {
            tags.remove(currentTag)
            notifyDataSetChanged()
                true
        })
    }

    override fun getItemCount(): Int {
        return tags.size
    }
}