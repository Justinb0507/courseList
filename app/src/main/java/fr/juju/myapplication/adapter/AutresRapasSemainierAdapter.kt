package fr.juju.myapplication.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.juju.myapplication.*
import fr.juju.myapplication.IngredientRepository.Singleton.ingredientList
import fr.juju.myapplication.SemainierSuivantRepository.Singleton.semainierSuivantList
import fr.juju.myapplication.fragments.RecetteFragment

class AutresRapasSemainierAdapter(
    val context: MainActivity,
    private val autresRepas: ArrayList<RepasModel>,
    private val selectedDayInput: String,
    private val currentSemaine: String,
    private val layoutId: Int
    ) : RecyclerView.Adapter<AutresRapasSemainierAdapter.ViewHolder>()  {

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val container: ConstraintLayout? = view.findViewById(R.id.container)
        val name: TextView? = view.findViewById(R.id.nom)
        val image: ImageView? = view.findViewById(R.id.image_item)
        val description : TextView? = view.findViewById(R.id.description)
        val tagRescycler : RecyclerView? = view.findViewById(R.id.tagList)
        val delete: ImageView? = view.findViewById(R.id.delete)
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
        collectionRecyclerView?.adapter = TagsAdapter(context, currentRepas.tags, R.layout.item_tags_horizontal)
        collectionRecyclerView?.layoutManager = LinearLayoutManager(context)

        holder.container?.setOnClickListener{
            context.loadFragment(RecetteFragment(context,currentRepas,"None","None","None"))
        }
        var repo = SemainierRepository()
        var repo2 = SemainierSuivantRepository()
        holder.delete?.setOnClickListener{
            if(currentSemaine == "suivant"){
                autresRepas.remove(currentRepas)
                repo2.deleteAutres(selectedDayInput, currentRepas.id)
                deleteIngredientCourse(ingredientList.filter { s -> s.id_repas == currentRepas.id } as ArrayList<IngredientModel>)
                Toast.makeText(context, "Repas du $selectedDayInput supprimé", Toast.LENGTH_SHORT)
                    .show()
                notifyDataSetChanged()
            } else{
                autresRepas.remove(currentRepas)
                deleteIngredientCourse(ingredientList.filter { s -> s.id_repas == currentRepas.id } as ArrayList<IngredientModel>)
                Toast.makeText(context, "Repas du $selectedDayInput supprimé", Toast.LENGTH_SHORT)
                    .show()
                repo.deleteAutres(selectedDayInput, currentRepas.id)
                notifyDataSetChanged()
            }

        }
    }

    override fun getItemCount(): Int {
        return autresRepas.size
    }

    private fun deleteIngredientCourse(ingredients: ArrayList<IngredientModel>) {
        var repo = CourseRepository()
        repo.deleteIngredientCourse(ingredients)
    }

}