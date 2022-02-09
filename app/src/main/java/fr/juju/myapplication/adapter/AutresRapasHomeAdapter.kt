package fr.juju.myapplication.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.juju.myapplication.MainActivity
import fr.juju.myapplication.R
import fr.juju.myapplication.RepasModel
import fr.juju.myapplication.fragments.RecetteFragment

class AutresRapasHomeAdapter(
    val context: MainActivity,
    private val autresRepas: ArrayList<RepasModel>,
    private val layoutId: Int
    ) : RecyclerView.Adapter<AutresRapasHomeAdapter.ViewHolder>()  {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val container: ConstraintLayout = view.findViewById(R.id.autres)
        val name: TextView? = view.findViewById(R.id.nom)
        val image: ImageView? = view.findViewById(R.id.image_item2)
        val description : TextView = view.findViewById(R.id.description)
        val tagRescycler : RecyclerView = view.findViewById(R.id.tagList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentRepas = autresRepas[position]
        holder.name?.text = currentRepas.name
        holder.description?.text = currentRepas.duree
        holder.image?.let {
            Glide.with(context)
                .load(Uri.parse(currentRepas.imageUri))
                .into(it)
        }
        val collectionRecyclerView = holder.tagRescycler
        collectionRecyclerView.adapter = TagsAdapter(context, currentRepas.tags, R.layout.item_tags_vertical)
        collectionRecyclerView.layoutManager = LinearLayoutManager(context)
        holder.container.setOnClickListener{
            context.loadFragment(RecetteFragment(context,currentRepas,"None","None","None"))
        }
    }

    override fun getItemCount(): Int {
        return autresRepas.size
    }
}