package fr.juju.myapplication.adapter

import android.annotation.SuppressLint
import android.graphics.Paint
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.juju.myapplication.*

class CourseItemEditAdapter(
    val context: MainActivity,
    private val courseList: ArrayList<CourseModel>,
    private val printToggle: Boolean,
    private val layoutId:Int)
    : RecyclerView.Adapter<CourseItemEditAdapter.ViewHolder>()
{
    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name: EditText? = view.findViewById(R.id.text_item)
        val quantite: EditText? = view.findViewById(R.id.quantity_item)
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
        holder.name?.setText(currentIngredient.name)
        holder.name?.addTextChangedListener(
            object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    currentIngredient.name = holder.name?.text.toString()
                    repo.updateCourseItem(currentIngredient)
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // Fires right before text is changing
                }

                override fun afterTextChanged(s: Editable) {


                }
            }

        )

        holder.quantite?.setText(currentIngredient.quantite)
        holder.quantite?.addTextChangedListener(
            object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    currentIngredient.quantite = holder.quantite?.text.toString()
                    repo.updateCourseItem(currentIngredient)

                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // Fires right before text is changing
                }

                override fun afterTextChanged(s: Editable) {


                }
            }

        )

        if(currentIngredient.ok == "true"){
            holder.name?.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
            holder.quantite?.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
        }
        holder.trash?.setOnClickListener{

            repo.deleteCourseItem(currentIngredient)

        }
    }

    override fun getItemCount(): Int {
        return courseList.size
    }

}