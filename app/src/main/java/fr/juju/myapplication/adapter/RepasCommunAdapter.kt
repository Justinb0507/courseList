package fr.juju.myapplication.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.juju.myapplication.*
import fr.juju.myapplication.activity.MainActivity
import fr.juju.myapplication.fragments.AddRepasCommunFragment
import fr.juju.myapplication.fragments.FiltreRepasFragment
import fr.juju.myapplication.model.RepasCommunModel
import fr.juju.myapplication.repository.RepasCommunRepository

class RepasCommunAdapter(
    val context: MainActivity,
    private val repasCommun: ArrayList<AddRepasCommunFragment.MultipleSelectRepas>,
    private val layoutId: Int
) : RecyclerView.Adapter<RepasCommunAdapter.ViewHolder>() {
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imageUri: ImageView = view.findViewById<ImageView>(R.id.image_item)
        val name: TextView? = view.findViewById(R.id.name)
        val time: TextView?  =  view.findViewById(R.id.time)
        val recycler: RecyclerView = view.findViewById(R.id.tagList)
        val conteneur: ConstraintLayout = view.findViewById(R.id.conteneur_repas)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //recuper les informations du repas
        val currentRepas = repasCommun[position]
        //Recup l'image depuis l'INTERNET
        Glide.with(context)
            .load(currentRepas.repas.imageUri)
            .into(holder.imageUri)
        holder.name?.text = currentRepas.repas.name
        holder.time?.text = currentRepas.repas.duree
        holder.recycler.adapter = TagsAdapter(context, currentRepas.repas.tags, R.layout.item_tags_horizontal)

        holder.conteneur.setOnClickListener{
            if(!currentRepas.selected){
                currentRepas.selected = true
                holder.conteneur.setBackgroundColor(Color.parseColor("#012E40"))
                holder.conteneur.alpha = 0.5F
            } else {
                currentRepas.selected = false
                holder.conteneur.setBackgroundColor(Color.parseColor("#020E1F"))
                holder.conteneur.alpha = 1F
            }
        }
    }

    override fun getItemCount(): Int {
        return repasCommun.size
    }
}