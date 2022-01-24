package fr.juju.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import androidx.constraintlayout.widget.ConstraintLayout
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
import fr.juju.myapplication.adapter.CourseItemAdapter
import java.util.*

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
        recyclerCourseList.adapter = CourseCategoryAdapter(context, courseList, categoryList, false, R.layout.item_course_vertical)
        recyclerCourseList.layoutManager = LinearLayoutManager(context)

            categoryList.clear()
            for(item in courseList){
                if (!categoryList.contains(item.categorie)){
                    categoryList.add(item.categorie)
                }
            }

            if(courseList.isEmpty()){
                view.findViewById<ConstraintLayout>(R.id.NoRepas).visibility = View.VISIBLE
            }
            else view.findViewById<ConstraintLayout>(R.id.NoRepas).visibility = View.GONE


        view.findViewById<Switch>(R.id.toggleButton).setOnClickListener{
            if(view.findViewById<Switch>(R.id.toggleButton).isChecked){
                recyclerCourseList.adapter = CourseCategoryAdapter(context, courseList, categoryList, true, R.layout.item_course_vertical)
                recyclerCourseList.layoutManager = LinearLayoutManager(context)
            }
            else {
                recyclerCourseList.adapter = CourseCategoryAdapter(context, courseList, categoryList, false, R.layout.item_course_vertical)
                recyclerCourseList.layoutManager = LinearLayoutManager(context)
            }
        }

        view.findViewById<ImageView>(R.id.generateCourse).setOnClickListener{
            generateCourse()
        }

        view.findViewById<ImageView>(R.id.clearCourse).setOnClickListener{
            clearCourse()
        }

        return view
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
                        var value = 0
                        var oldItem = courseList.filter { s->s.name == ingredient.name }[0]

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
                            if(oldItem.quantite.contains(" boites")){
                                value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" boites")).replace(" ", "").toInt()+1
                                oldItem.quantite = value.toString() + " boites"
                            }else
                                oldItem.quantite = "2 boites"
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
                        var value = 0
                        var oldItem = courseList.filter { s->s.name == ingredient.name }[0]

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
                            if(oldItem.quantite.contains(" boites")){
                                value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" boites")).replace(" ", "").toInt()+1
                                oldItem.quantite = value.toString() + " boites"
                            }else
                                oldItem.quantite = "2 boites"
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
                        var value = 0
                        var oldItem = courseList.filter { s->s.name == ingredient.name }[0]

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
                            if(oldItem.quantite.contains(" boites")){
                                value = oldItem.quantite.substring( 0,  oldItem.quantite.indexOf(" boites")).replace(" ", "").toInt()+1
                                oldItem.quantite = value.toString() + " boites"
                            }else
                                oldItem.quantite = "2 boites"
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