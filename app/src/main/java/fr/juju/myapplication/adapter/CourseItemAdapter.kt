package fr.juju.myapplication.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.juju.myapplication.*
import java.math.RoundingMode
import java.text.DecimalFormat

class CourseItemAdapter(
    val context: MainActivity,
    private val courseList: ArrayList<CourseModel>,
    private val printToggle: Boolean,
    private val layoutId:Int)
    : RecyclerView.Adapter<CourseItemAdapter.ViewHolder>()
{
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name: TextView? = view.findViewById(R.id.text_item)
        val quantite: TextView? = view.findViewById(R.id.quantity_item)
        val button: CheckBox? = view.findViewById(R.id.radioButton)
        val trash: ImageView? = view.findViewById(R.id.trash)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentIngredient = courseList[position]
        var repo = CourseRepository()
        var quantite = repo.getQuantite(currentIngredient.quantite).toString()
        var unite = repo.getUnite(currentIngredient.quantite)
        holder.name?.text = currentIngredient.name
            if (quantite.split( '.')[1] == "0"){
                holder.quantite?.text = quantite.split('.')[0] + " " + unite
            } else {
                val df = DecimalFormat("#.#")
                df.roundingMode = RoundingMode.CEILING
                holder.quantite?.text = df.format(quantite.toFloat()) + " " +  unite
            }


        holder.name?.setOnClickListener{
            if(printToggle){
                if(currentIngredient.ok == "false") {
                    currentIngredient.ok = "true"
                    holder.name?.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    holder.quantite?.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    holder.button?.isChecked = true
                }
                else {
                    currentIngredient.ok = "false"
                    holder.button?.isChecked = false
                    holder.name?.paintFlags = 0
                    holder.quantite?.paintFlags = 0
                }
                repo.updateCourseItem(currentIngredient)
            }
        }
        holder.quantite?.setOnClickListener{
            if(printToggle){
                if(currentIngredient.ok == "false") {
                    currentIngredient.ok = "true"
                    holder.name?.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    holder.quantite?.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                    holder.button?.isChecked = true
                }
                else {
                    currentIngredient.ok = "false"
                    holder.button?.isChecked = false
                    holder.name?.paintFlags = 0
                    holder.quantite?.paintFlags = 0
                }
                repo.updateCourseItem(currentIngredient)
            }
        }

        holder.button?.isChecked = currentIngredient.ok == "true"
        if(currentIngredient.ok == "true"){
            holder.name?.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.quantite?.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
        holder.button?.setOnClickListener{
            if(currentIngredient.ok == "false") {
                currentIngredient.ok = "true"
                holder.name?.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                holder.quantite?.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                holder.button?.isChecked = true
            }
            else {
                currentIngredient.ok = "false"
                holder.button?.isChecked = false
                holder.name?.paintFlags = 0
                holder.quantite?.paintFlags = 0
            }
            repo.updateCourseItem(currentIngredient)
        }

        holder.trash?.setOnClickListener{
            repo.deleteCourseItem(currentIngredient)
        }

        if(printToggle){
            holder.button?.visibility = View.VISIBLE
            holder.trash?.visibility = View.GONE
        } else {
            holder.trash?.visibility = View.VISIBLE
            holder.button?.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

}