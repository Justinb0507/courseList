package fr.juju.myapplication.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import fr.juju.myapplication.*
import fr.juju.myapplication.fragments.RecetteFragment


class RepasAdapter(
    val context: MainActivity,
    private val repasList: List<RepasModel>,
    private val layoutId:Int,
    private val time: String,
    private val selectedDay: String
    ) : RecyclerView.Adapter<RepasAdapter.ViewHolder>() {

    class ViewHolder(view:View): RecyclerView.ViewHolder(view){
        val imageUri: ImageView = view.findViewById<ImageView>(R.id.image_item)
        val name: TextView? = view.findViewById(R.id.name_item)
        val description: TextView?  =  view.findViewById(R.id.description_item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //recuper les informations du repas
        val currentRepas = repasList[position]
        //Recup l'image depuis l'INTERNET
        Glide.with(context)
            .load(currentRepas.imageUri)
            .into(holder.imageUri)

        holder.name?.text = currentRepas.name
        holder.description?.text = currentRepas.description

        holder.itemView.setOnClickListener {
            context.loadFragment(RecetteFragment(context, currentRepas, time, selectedDay))
        }

    }

    override fun getItemCount(): Int {
        return repasList.size
    }

}