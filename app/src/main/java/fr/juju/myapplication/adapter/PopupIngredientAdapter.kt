package fr.juju.myapplication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.juju.myapplication.*
import fr.juju.myapplication.CategorieRepository.Singleton.categorieList

class PopupIngredientAdapter(val context: MainActivity, private val ingredients: ArrayList<IngredientModel>, private val layoutId:Int) : RecyclerView.Adapter<PopupIngredientAdapter.ViewHolder>() {


    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name: TextView? = view.findViewById(R.id.name)
        val spinner: Spinner? = view.findViewById(R.id.spinner_description)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val view = LayoutInflater.from(context).inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val repo = IngredientRepository()
        val currentIngredient = ingredients[position]
        holder.name?.text = currentIngredient.name
        var spinnerList = arrayListOf<String>("None")
        for(cat in categorieList.sortedBy { s-> s.name }){
            spinnerList.add(cat.name)
        }
        holder.spinner?.adapter = ArrayAdapter(context,android.R.layout.simple_list_item_1, spinnerList)
        holder.spinner?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (spinnerList[p2] != "None"){
                    currentIngredient.id_categorie = categorieList.filter { s->s.name == spinnerList[p2] }[0].id
                    repo.updateIngredient(currentIngredient)
                }

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

    }

    override fun getItemCount(): Int {
        return ingredients.size
    }

}