package fr.juju.myapplication.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginBottom
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import fr.juju.myapplication.*
import fr.juju.myapplication.CategorieRepository.Singleton.categorieList
import fr.juju.myapplication.CourseRepository.Singleton.courseList
import fr.juju.myapplication.IngredientRepository.Singleton.ingredientList
import fr.juju.myapplication.RepasRepository.Singleton.repasList
import fr.juju.myapplication.SemainierRepository.Singleton.semainierList
import fr.juju.myapplication.adapter.CourseCategoryAdapter
import org.w3c.dom.Text
import java.util.*
import kotlin.collections.ArrayList

class CourseListeFragment (val context: MainActivity
) : Fragment()  {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.course_liste_fragment, container, false)
        var recyclerCourseList = view.findViewById<RecyclerView>(R.id.course_liste)
        var categoryList: ArrayList<String> = arrayListOf()
        view.findViewById<Switch>(R.id.toggleButton).isChecked = false

            categoryList.clear()
            for(item in courseList){
                if (!categoryList.contains(item.categorie)){
                    categoryList.add(item.categorie)
                }
            }
        categoryList = ArrayList(categoryList.sorted())
        recyclerCourseList.adapter = CourseCategoryAdapter(context, courseList, categoryList, false, R.layout.item_course_vertical)
        recyclerCourseList.layoutManager = LinearLayoutManager(context)
        var afficheAdd = false

        if(courseList.isEmpty()){
            view.findViewById<ImageView>(R.id.clearCourse).visibility = View.GONE
            view.findViewById<ConstraintLayout>(R.id.NoRepas).visibility = View.VISIBLE
            view.findViewById<Switch>(R.id.toggleButton).visibility = View.GONE
            view.findViewById<ImageView>(R.id.commencer).visibility = View.VISIBLE
            view.findViewById<ConstraintLayout>(R.id.add_item).visibility = View.GONE
            view.findViewById<ImageView>(R.id.open).visibility = View.GONE
            view.findViewById<TextView>(R.id.add_text).visibility = View.GONE
        }else {
            view.findViewById<ImageView>(R.id.clearCourse).visibility = View.VISIBLE
            view.findViewById<Switch>(R.id.toggleButton).visibility = View.VISIBLE
            view.findViewById<ConstraintLayout>(R.id.add_item).visibility = View.GONE
            view.findViewById<TextView>(R.id.add_text).visibility = View.VISIBLE
            view.findViewById<ImageView>(R.id.open).visibility = View.VISIBLE
            view.findViewById<ConstraintLayout>(R.id.NoRepas).visibility = View.GONE
        }

        view.findViewById<ImageView>(R.id.commencer).setOnClickListener{
            afficheAdd = true
            if(view.findViewById<Switch>(R.id.toggleButton).isChecked){
                view.findViewById<Switch>(R.id.toggleButton).isChecked = false
                view.findViewById<ImageView>(R.id.commencer).visibility = View.GONE
            }
            view.findViewById<ConstraintLayout>(R.id.add_item).visibility = View.VISIBLE
            view.findViewById<ImageView>(R.id.commencer).visibility = View.GONE
            view.findViewById<ConstraintLayout>(R.id.add_item).visibility = View.VISIBLE
            view.findViewById<EditText>(R.id.item).requestFocus()
            val showMe = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            showMe.showSoftInput(view.findViewById<EditText>(R.id.item), InputMethodManager.SHOW_IMPLICIT)
        }

        view.findViewById<ImageView>(R.id.add_item_button).setOnClickListener{
            if(view.findViewById<EditText>(R.id.item).text.isNotEmpty()) {
                addItem(view)
                view.findViewById<EditText>(R.id.item).setText("")
                view.findViewById<EditText>(R.id.quantite).setText("")
                view.findViewById<AutoCompleteTextView>(R.id.categorie).setText("")
                view.findViewById<EditText>(R.id.item).requestFocus()
            }
        }

        view.findViewById<Switch>(R.id.toggleButton).setOnClickListener{
            context.hideKeyboard()
            if(view.findViewById<Switch>(R.id.toggleButton).isChecked){
                view.findViewById<ImageView>(R.id.clearCourse).visibility = View.GONE
                view.findViewById<TextView>(R.id.add_text).visibility = View.GONE
                view.findViewById<ConstraintLayout>(R.id.add_item).visibility = View.GONE
                view.findViewById<ImageView>(R.id.open).visibility = View.GONE
                view.findViewById<ImageView>(R.id.commencer).visibility = View.VISIBLE
                view.findViewById<ConstraintLayout>(R.id.add_item).visibility = View.GONE
                recyclerCourseList.adapter = CourseCategoryAdapter(context, courseList, categoryList, true, R.layout.item_course_vertical)
                recyclerCourseList.layoutManager = LinearLayoutManager(context)
            }
            else {
                view.findViewById<ImageView>(R.id.clearCourse).visibility = View.VISIBLE
                if(afficheAdd){
                    view.findViewById<TextView>(R.id.add_text).visibility = View.GONE
                    view.findViewById<ImageView>(R.id.open).setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                    view.findViewById<ConstraintLayout>(R.id.add_item).visibility = View.VISIBLE
                    view.findViewById<ImageView>(R.id.open).visibility = View.VISIBLE
                }else{
                    view.findViewById<TextView>(R.id.add_text).visibility = View.VISIBLE
                    view.findViewById<ImageView>(R.id.open).setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
                    view.findViewById<ConstraintLayout>(R.id.add_item).visibility = View.GONE
                    view.findViewById<ImageView>(R.id.open).visibility = View.VISIBLE
                }

                recyclerCourseList.adapter = CourseCategoryAdapter(context, courseList, categoryList, false, R.layout.item_course_vertical)
                recyclerCourseList.layoutManager = LinearLayoutManager(context)
            }
        }


        var repo = CourseRepository()
        repo.updateData {

            categoryList.clear()
            for(item in courseList){
                if (!categoryList.contains(item.categorie)){
                    categoryList.add(item.categorie)
                }
            }
            categoryList = ArrayList(categoryList.sorted())
            if(courseList.isEmpty()){
                context.hideKeyboard()
                view.findViewById<ImageView>(R.id.clearCourse).visibility = View.GONE
                view.findViewById<ConstraintLayout>(R.id.NoRepas).visibility = View.VISIBLE
                view.findViewById<Switch>(R.id.toggleButton).visibility = View.GONE
                view.findViewById<ImageView>(R.id.commencer).visibility = View.VISIBLE
                view.findViewById<ConstraintLayout>(R.id.add_item).visibility = View.GONE
                view.findViewById<ImageView>(R.id.open).visibility = View.GONE
                view.findViewById<TextView>(R.id.add_text).visibility = View.GONE
            }else if(courseList.isNotEmpty() && view.findViewById<ConstraintLayout>(R.id.add_item).visibility == View.GONE)
            {
                view.findViewById<ImageView>(R.id.clearCourse).visibility = View.VISIBLE
                view.findViewById<Switch>(R.id.toggleButton).visibility = View.VISIBLE
                view.findViewById<ConstraintLayout>(R.id.add_item).visibility = View.GONE
                view.findViewById<TextView>(R.id.add_text).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.open).visibility = View.VISIBLE
                view.findViewById<ConstraintLayout>(R.id.NoRepas).visibility = View.GONE
            }else if(courseList.isNotEmpty() && view.findViewById<ConstraintLayout>(R.id.add_item).visibility == View.VISIBLE)
            {
                view.findViewById<ImageView>(R.id.clearCourse).visibility = View.VISIBLE
                view.findViewById<Switch>(R.id.toggleButton).visibility = View.VISIBLE
                view.findViewById<ConstraintLayout>(R.id.add_item).visibility = View.VISIBLE
                view.findViewById<TextView>(R.id.add_text).visibility = View.GONE
                view.findViewById<ImageView>(R.id.open).visibility = View.VISIBLE
                view.findViewById<ConstraintLayout>(R.id.NoRepas).visibility = View.GONE
            }

            if(view.findViewById<Switch>(R.id.toggleButton).isChecked && view.findViewById<Switch>(R.id.toggleButton).visibility == View.VISIBLE){
                view.findViewById<ConstraintLayout>(R.id.add_item).visibility = View.GONE
                view.findViewById<TextView>(R.id.add_text).visibility = View.GONE
                view.findViewById<ImageView>(R.id.open).visibility = View.GONE
                var saveState = (recyclerCourseList.layoutManager as LinearLayoutManager).onSaveInstanceState()
                recyclerCourseList.adapter = CourseCategoryAdapter(context, courseList, categoryList, true, R.layout.item_course_vertical)
                recyclerCourseList.layoutManager = LinearLayoutManager(context)
                (recyclerCourseList.layoutManager as LinearLayoutManager).onRestoreInstanceState(saveState)
            }
            else if (!view.findViewById<Switch>(R.id.toggleButton).isChecked && view.findViewById<Switch>(R.id.toggleButton).visibility == View.VISIBLE) {
                var saveState = (recyclerCourseList.layoutManager as LinearLayoutManager).onSaveInstanceState()
                recyclerCourseList.adapter = CourseCategoryAdapter(context, courseList, categoryList, false, R.layout.item_course_vertical)
                recyclerCourseList.layoutManager = LinearLayoutManager(context)
                (recyclerCourseList.layoutManager as LinearLayoutManager).onRestoreInstanceState(saveState)
            }
        }

        view.findViewById<ImageView>(R.id.open).setOnClickListener{
            afficheAdd = !afficheAdd
            view.findViewById<EditText>(R.id.item).setText("")
            view.findViewById<EditText>(R.id.quantite).setText("")
            view.findViewById<AutoCompleteTextView>(R.id.categorie).setText("")
            if (view.findViewById<TextView>(R.id.add_text).visibility == View.GONE){
                context.hideKeyboard()
                view.findViewById<ConstraintLayout>(R.id.add_item).visibility = View.GONE
                view.findViewById<TextView>(R.id.add_text).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.open).setImageResource(R.drawable.ic_baseline_keyboard_arrow_down_24)
            } else {
                view.findViewById<ImageView>(R.id.open).setImageResource(R.drawable.ic_baseline_keyboard_arrow_up_24)
                view.findViewById<TextView>(R.id.add_text).visibility = View.GONE
                view.findViewById<ConstraintLayout>(R.id.add_item).visibility = View.VISIBLE
                view.findViewById<EditText>(R.id.item).requestFocus()
                val showMe = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                showMe.showSoftInput(view.findViewById<EditText>(R.id.item), InputMethodManager.SHOW_IMPLICIT)
            }
        }

        view.findViewById<ImageView>(R.id.clearCourse).setOnClickListener {
            var builder = AlertDialog.Builder(context)
            builder.setTitle("Oulaaaaaa !")
            builder.setMessage("Tu veux vraiment supprimer la liste de cours là ?")
            builder.setPositiveButton("Oui", DialogInterface.OnClickListener { dialog, id ->
                clearCourse()
                context.hideKeyboard()
            dialog.cancel()
            })
            builder.setNegativeButton("Non", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })
            var alert: AlertDialog = builder.create()
            alert.show()
        }

        var categorieInput = view.findViewById<AutoCompleteTextView>(R.id.categorie)
        var categorieInputList = arrayListOf<String>()
        for(item in categorieList){
            categorieInputList.add(item.name)
        }
        var adapter : ArrayAdapter<String> = ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, categorieInputList)
        categorieInput.setAdapter(adapter)


        view.findViewById<AutoCompleteTextView>(R.id.categorie).addTextChangedListener(
            object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if(s.contains("\n")) {
                        view.findViewById<AutoCompleteTextView>(R.id.categorie).setText(s.toString().replace("\n",""))
                        view.findViewById<ImageView>(R.id.add_item_button).performClick()
                    }}

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
        view.findViewById<EditText>(R.id.quantite).addTextChangedListener(
            object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if(s.contains("\n")) {
                        view.findViewById<EditText>(R.id.quantite).setText(s.toString().replace("\n",""))
                        view.findViewById<AutoCompleteTextView>(R.id.categorie).requestFocus()
                    }}

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

        view.findViewById<EditText>(R.id.item).addTextChangedListener(
            object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if(s.contains("\n")) {
                        view.findViewById<EditText>(R.id.item).setText(s.toString().replace("\n",""))
                        view.findViewById<EditText>(R.id.quantite).requestFocus()
                    }}

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
        return view
    }

    private fun addItem(view : View){
        var repo = CourseRepository()
        var itemName =  view.findViewById<EditText>(R.id.item).text.toString()
        var quantite = view.findViewById<EditText>(R.id.quantite).text.toString()
        var categorie = view.findViewById<AutoCompleteTextView>(R.id.categorie).text.toString()
        itemName = itemName.lowercase(Locale.getDefault())
        itemName = itemName.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

        categorie = categorie.lowercase(Locale.getDefault())
        categorie = categorie.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

        var itemCourse = CourseModel(
            UUID.randomUUID().toString(),
            itemName,
            quantite,
            categorie,
            "false",
            "true"
        )
        if(courseList.filter{ s-> s.name == itemCourse.name}.isEmpty() && itemName != "" && itemName != " "&& itemName != "  " && quantite.isNotEmpty() && categorie.isNotEmpty()){
            repo.insertCourseItem(itemCourse)
        }

    }


    private fun clearCourse(){
        var repo = CourseRepository()
        for(courseItem in courseList){
            if(courseItem.ajoutExterieur == "false"){
                repo.deleteCourseItem(courseItem)
            }

        }
    }

}