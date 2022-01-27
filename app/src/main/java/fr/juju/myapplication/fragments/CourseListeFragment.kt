package fr.juju.myapplication.fragments

import android.content.Context
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
            view.findViewById<ConstraintLayout>(R.id.NoRepas).visibility = View.VISIBLE
            view.findViewById<Switch>(R.id.toggleButton).visibility = View.GONE
            view.findViewById<ImageView>(R.id.commencer).visibility = View.VISIBLE
            view.findViewById<ConstraintLayout>(R.id.add_item).visibility = View.GONE
            view.findViewById<ImageView>(R.id.open).visibility = View.GONE
            view.findViewById<TextView>(R.id.add_text).visibility = View.GONE
        }else {
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
                view.findViewById<TextView>(R.id.add_text).visibility = View.GONE
                view.findViewById<ConstraintLayout>(R.id.add_item).visibility = View.GONE
                view.findViewById<ImageView>(R.id.open).visibility = View.GONE
                view.findViewById<ImageView>(R.id.commencer).visibility = View.VISIBLE
                view.findViewById<ConstraintLayout>(R.id.add_item).visibility = View.GONE
                recyclerCourseList.adapter = CourseCategoryAdapter(context, courseList, categoryList, true, R.layout.item_course_vertical)
                recyclerCourseList.layoutManager = LinearLayoutManager(context)
            }
            else {
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
                view.findViewById<ConstraintLayout>(R.id.NoRepas).visibility = View.VISIBLE
                view.findViewById<Switch>(R.id.toggleButton).visibility = View.GONE
                view.findViewById<ImageView>(R.id.commencer).visibility = View.VISIBLE
                view.findViewById<ConstraintLayout>(R.id.add_item).visibility = View.GONE
                view.findViewById<ImageView>(R.id.open).visibility = View.GONE
                view.findViewById<TextView>(R.id.add_text).visibility = View.GONE
            }else if(courseList.isNotEmpty() && view.findViewById<ConstraintLayout>(R.id.add_item).visibility == View.GONE)
            {
                view.findViewById<Switch>(R.id.toggleButton).visibility = View.VISIBLE
                view.findViewById<ConstraintLayout>(R.id.add_item).visibility = View.GONE
                view.findViewById<TextView>(R.id.add_text).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.open).visibility = View.VISIBLE
                view.findViewById<ConstraintLayout>(R.id.NoRepas).visibility = View.GONE
            }else if(courseList.isNotEmpty() && view.findViewById<ConstraintLayout>(R.id.add_item).visibility == View.VISIBLE)
            {
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
                recyclerCourseList.adapter = CourseCategoryAdapter(context, courseList, categoryList, true, R.layout.item_course_vertical)
                recyclerCourseList.layoutManager = LinearLayoutManager(context)
            }
            else if (!view.findViewById<Switch>(R.id.toggleButton).isChecked && view.findViewById<Switch>(R.id.toggleButton).visibility == View.VISIBLE) {
                recyclerCourseList.adapter = CourseCategoryAdapter(context, courseList, categoryList, false, R.layout.item_course_vertical)
                recyclerCourseList.layoutManager = LinearLayoutManager(context)
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

        view.findViewById<ImageView>(R.id.generateCourse).setOnClickListener{
            generateCourse()
        }

        view.findViewById<ImageView>(R.id.clearCourse).setOnClickListener{
            clearCourse()
            context.hideKeyboard()
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

    private fun generateCourse(){
        var repo = CourseRepository()
        for (days in semainierList){
            if(days.midi != "None"){
                var ingredients = ingredientList.filter { s->s.id_repas == repasList.filter { s->s.id == days.midi }[0].id }
                for (ingredient in ingredients){
                    var courseItem = CourseModel(
                        UUID.randomUUID().toString(),
                        ingredient.name,
                        ingredient.quantite,
                        if (ingredient.id_categorie!="None") categorieList.filter { s->s.id == ingredient.id_categorie }[0].name else "Autres",
                        "false"
                    )
                    if(courseList.filter { s->s.name == ingredient.name }.isNotEmpty()){
                        var isDigit = true
                        var value = 0
                        var oldItem = courseList.filter { s->s.name == ingredient.name }[0]
                        for(lettre in oldItem.quantite){
                            if(!lettre.isDigit()){
                                isDigit = false
                            }
                        }

                        if (isDigit){
                            oldItem.quantite = (oldItem.quantite.toInt() + ingredient.quantite.toInt()).toString()
                        }else{
                            if (oldItem.quantite.contains("cl")){
                                value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("cl")).replace(" ", "").toInt()
                            }
                            else if (oldItem.quantite.contains("l") && !oldItem.quantite.contains("cl")){
                                value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("l")).replace(" ", "").toInt()*100
                            }
                            else if (oldItem.quantite.contains("g")  && !oldItem.quantite.contains("kg") && !oldItem.quantite.contains("au jugé")){
                                value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("g")).replace(" ", "").toInt()
                            }
                            else if (oldItem.quantite.contains("kg")){
                                value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("kg")).replace(" ", "").toInt()*1000
                            }
                            else if (oldItem.quantite.contains("petit pot")){
                                value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("petit pot")).replace(" ", "").toInt()
                            }
                            else if (oldItem.quantite.contains("petits pots")){
                                value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("petits pots")).replace(" ", "").toInt()
                            }

                            if (ingredient.quantite.contains("cl"))
                            {
                                var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("cl")).replace(" ", "").toInt()
                                oldItem.quantite = ((value + newValue).toString()) + " cl"
                            }
                            else if (ingredient.quantite.contains("l") && !ingredient.quantite.contains("cl"))
                            {
                                var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("l")).replace(" ", "").toInt()*100
                                oldItem.quantite = ((value + newValue).toString()) + " cl"
                            }
                            else if (ingredient.quantite.contains("kg"))
                            {
                                var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("kg")).replace(" ", "").toInt()*1000
                                oldItem.quantite = ((value + newValue).toString()) + " g"
                            }
                            else if (ingredient.quantite.contains("g") && !ingredient.quantite.contains("kg") && !ingredient.quantite.contains("au jugé"))
                            {
                                var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("g")).replace(" ", "").toInt()
                                oldItem.quantite = ((value + newValue).toString()) + " g"
                            }
                            else if (ingredient.quantite.contains("au jugé")){
                                if(oldItem.quantite.contains(" *")){
                                    value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" *")).replace(" ", "").toInt()+1
                                    oldItem.quantite = value.toString() + " *" + " au jugé"
                                }else
                                    oldItem.quantite = "2 * " + oldItem.quantite
                            }
                            else if (ingredient.quantite.contains("boites")){
                                var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("boites")).replace(" ", "").toInt()
                                if(oldItem.quantite.contains(" boites")){
                                    value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" boites")).replace(" ", "").toInt()
                                    oldItem.quantite = ((value + newValue).toString()) + " boites"
                                }else
                                    value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" boite")).replace(" ", "").toInt()
                                oldItem.quantite = ((value + newValue).toString()) + " boites"
                            }
                            else if (ingredient.quantite.contains("petit pot")){
                                var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("petit pot")).replace(" ", "").toInt()
                                if(oldItem.quantite.contains(" petits pots")){
                                    value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" petits pots")).replace(" ", "").toInt()
                                    oldItem.quantite = ((value + newValue).toString()) + " boites"
                                }else
                                    value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" petit pot")).replace(" ", "").toInt()
                                oldItem.quantite = ((value + newValue).toString()) + " petits pots"
                            }
                            else if (ingredient.quantite.contains("petits pots")){
                                var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("petit pots")).replace(" ", "").toInt()
                                if(oldItem.quantite.contains(" petits pots")){
                                    value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" petits pots")).replace(" ", "").toInt()
                                    oldItem.quantite = ((value + newValue).toString()) + " boites"
                                }else
                                    value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" petit pot")).replace(" ", "").toInt()
                                oldItem.quantite = ((value + newValue).toString()) + " petits pots"
                            }
                        }

                    }

                    else {
                        courseList.add(courseItem)
                    }
                }
            }
            if(days.apero != "None"){
                var ingredients = ingredientList.filter { s->s.id_repas == repasList.filter { s->s.id == days.apero }[0].id }
                for (ingredient in ingredients){
                    var courseItem = CourseModel(
                        UUID.randomUUID().toString(),
                        ingredient.name,
                        ingredient.quantite,
                        if (ingredient.id_categorie!="None") categorieList.filter { s->s.id == ingredient.id_categorie }[0].name else "Autres",
                        "false"
                    )
                    if(courseList.filter { s->s.name == ingredient.name }.isNotEmpty()){
                        var isDigit = true
                        var value = 0
                        var oldItem = courseList.filter { s->s.name == ingredient.name }[0]
                        for(lettre in oldItem.quantite){
                            if(!lettre.isDigit()){
                                isDigit = false
                            }
                        }

                        if (isDigit){
                            oldItem.quantite = (oldItem.quantite.toInt() + ingredient.quantite.toInt()).toString()
                        }else{
                            if (oldItem.quantite.contains("cl")){
                                value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("cl")).replace(" ", "").toInt()
                            }
                            else if (oldItem.quantite.contains("l") && !oldItem.quantite.contains("cl")){
                                value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("l")).replace(" ", "").toInt()*100
                            }
                            else if (oldItem.quantite.contains("g")  && !oldItem.quantite.contains("kg") && !oldItem.quantite.contains("au jugé")){
                                value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("g")).replace(" ", "").toInt()
                            }
                            else if (oldItem.quantite.contains("kg")){
                                value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("kg")).replace(" ", "").toInt()*1000
                            }
                            else if (oldItem.quantite.contains("petit pot")){
                                value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("petit pot")).replace(" ", "").toInt()
                            }
                            else if (oldItem.quantite.contains("petits pots")){
                                value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("petits pots")).replace(" ", "").toInt()
                            }

                            if (ingredient.quantite.contains("cl"))
                            {
                                var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("cl")).replace(" ", "").toInt()
                                oldItem.quantite = ((value + newValue).toString()) + " cl"
                            }
                            else if (ingredient.quantite.contains("l") && !ingredient.quantite.contains("cl"))
                            {
                                var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("l")).replace(" ", "").toInt()*100
                                oldItem.quantite = ((value + newValue).toString()) + " cl"
                            }
                            else if (ingredient.quantite.contains("kg"))
                            {
                                var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("kg")).replace(" ", "").toInt()*1000
                                oldItem.quantite = ((value + newValue).toString()) + " g"
                            }
                            else if (ingredient.quantite.contains("g") && !ingredient.quantite.contains("kg") && !ingredient.quantite.contains("au jugé"))
                            {
                                var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("g")).replace(" ", "").toInt()
                                oldItem.quantite = ((value + newValue).toString()) + " g"
                            }
                            else if (ingredient.quantite.contains("au jugé")){
                                if(oldItem.quantite.contains(" *")){
                                    value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" *")).replace(" ", "").toInt()+1
                                    oldItem.quantite = value.toString() + " *" + " au jugé"
                                }else
                                    oldItem.quantite = "2 * " + oldItem.quantite
                            }
                            else if (ingredient.quantite.contains("boites")){
                                var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("boites")).replace(" ", "").toInt()
                                if(oldItem.quantite.contains(" boites")){
                                    value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" boites")).replace(" ", "").toInt()
                                    oldItem.quantite = ((value + newValue).toString()) + " boites"
                                }else
                                    value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" boite")).replace(" ", "").toInt()
                                oldItem.quantite = ((value + newValue).toString()) + " boites"
                            }
                            else if (ingredient.quantite.contains("petit pot")){
                                var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("petit pot")).replace(" ", "").toInt()
                                if(oldItem.quantite.contains(" petits pots")){
                                    value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" petits pots")).replace(" ", "").toInt()
                                    oldItem.quantite = ((value + newValue).toString()) + " boites"
                                }else
                                    value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" petit pot")).replace(" ", "").toInt()
                                oldItem.quantite = ((value + newValue).toString()) + " petits pots"
                            }
                            else if (ingredient.quantite.contains("petits pots")){
                                var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("petit pots")).replace(" ", "").toInt()
                                if(oldItem.quantite.contains(" petits pots")){
                                    value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" petits pots")).replace(" ", "").toInt()
                                    oldItem.quantite = ((value + newValue).toString()) + " boites"
                                }else
                                    value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" petit pot")).replace(" ", "").toInt()
                                oldItem.quantite = ((value + newValue).toString()) + " petits pots"
                            }
                        }

                    }

                    else {
                        courseList.add(courseItem)
                    }
                }
            }
            if(days.soir != "None"){
                var ingredients = ingredientList.filter { s->s.id_repas == repasList.filter { s->s.id == days.soir }[0].id }
                for (ingredient in ingredients){
                    var courseItem = CourseModel(
                        UUID.randomUUID().toString(),
                        ingredient.name,
                        ingredient.quantite,
                        if (ingredient.id_categorie!="None") categorieList.filter { s->s.id == ingredient.id_categorie }[0].name else "Autres",
                        "false"
                    )
                    if(courseList.filter { s->s.name == ingredient.name }.isNotEmpty()){
                        var isDigit = true
                        var value = 0
                        var oldItem = courseList.filter { s->s.name == ingredient.name }[0]
                        for(lettre in oldItem.quantite){
                            if(!lettre.isDigit()){
                                isDigit = false
                            }
                        }

                        if (isDigit){
                            oldItem.quantite = (oldItem.quantite.toInt() + ingredient.quantite.toInt()).toString()
                        }else{
                            if (oldItem.quantite.contains("cl")){
                                value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("cl")).replace(" ", "").toInt()
                            }
                            else if (oldItem.quantite.contains("l") && !oldItem.quantite.contains("cl")){
                                value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("l")).replace(" ", "").toInt()*100
                            }
                            else if (oldItem.quantite.contains("g")  && !oldItem.quantite.contains("kg") && !oldItem.quantite.contains("au jugé")){
                                value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("g")).replace(" ", "").toInt()
                            }
                            else if (oldItem.quantite.contains("kg")){
                                value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("kg")).replace(" ", "").toInt()*1000
                            }
                            else if (oldItem.quantite.contains("petit pot")){
                                value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("petit pot")).replace(" ", "").toInt()
                            }
                            else if (oldItem.quantite.contains("petits pots")){
                                value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("petits pots")).replace(" ", "").toInt()
                            }

                            if (ingredient.quantite.contains("cl"))
                            {
                                var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("cl")).replace(" ", "").toInt()
                                oldItem.quantite = ((value + newValue).toString()) + " cl"
                            }
                            else if (ingredient.quantite.contains("l") && !ingredient.quantite.contains("cl"))
                            {
                                var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("l")).replace(" ", "").toInt()*100
                                oldItem.quantite = ((value + newValue).toString()) + " cl"
                            }
                            else if (ingredient.quantite.contains("kg"))
                            {
                                var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("kg")).replace(" ", "").toInt()*1000
                                oldItem.quantite = ((value + newValue).toString()) + " g"
                            }
                            else if (ingredient.quantite.contains("g") && !ingredient.quantite.contains("kg") && !ingredient.quantite.contains("au jugé"))
                            {
                                var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("g")).replace(" ", "").toInt()
                                oldItem.quantite = ((value + newValue).toString()) + " g"
                            }
                            else if (ingredient.quantite.contains("au jugé")){
                                if(oldItem.quantite.contains(" *")){
                                    value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" *")).replace(" ", "").toInt()+1
                                    oldItem.quantite = value.toString() + " *" + " au jugé"
                                }else
                                    oldItem.quantite = "2 * " + oldItem.quantite
                            }
                            else if (ingredient.quantite.contains("boites")){
                                var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("boites")).replace(" ", "").toInt()
                                if(oldItem.quantite.contains(" boites")){
                                    value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" boites")).replace(" ", "").toInt()
                                    oldItem.quantite = ((value + newValue).toString()) + " boites"
                                }else
                                    value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" boite")).replace(" ", "").toInt()
                                oldItem.quantite = ((value + newValue).toString()) + " boites"
                            }
                            else if (ingredient.quantite.contains("petit pot")){
                                var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("petit pot")).replace(" ", "").toInt()
                                if(oldItem.quantite.contains(" petits pots")){
                                    value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" petits pots")).replace(" ", "").toInt()
                                    oldItem.quantite = ((value + newValue).toString()) + " boites"
                                }else
                                    value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" petit pot")).replace(" ", "").toInt()
                                oldItem.quantite = ((value + newValue).toString()) + " petits pots"
                            }
                            else if (ingredient.quantite.contains("petits pots")){
                                var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("petit pots")).replace(" ", "").toInt()
                                if(oldItem.quantite.contains(" petits pots")){
                                    value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" petits pots")).replace(" ", "").toInt()
                                    oldItem.quantite = ((value + newValue).toString()) + " boites"
                                }else
                                    value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" petit pot")).replace(" ", "").toInt()
                                oldItem.quantite = ((value + newValue).toString()) + " petits pots"
                            }
                        }

                    }

                    else {
                        courseList.add(courseItem)
                    }
                }
            }
        }
        for (item in courseList){
            repo.insertCourseItem(item)
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