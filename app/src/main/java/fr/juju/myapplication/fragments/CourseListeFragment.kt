package fr.juju.myapplication.fragments

import android.os.Bundle
import android.text.TextUtils.substring
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import fr.juju.myapplication.CategorieRepository.Singleton.categorieList
import fr.juju.myapplication.CourseModel
import fr.juju.myapplication.CourseRepository
import fr.juju.myapplication.CourseRepository.Singleton.courseList
import fr.juju.myapplication.IngredientRepository.Singleton.ingredientList
import fr.juju.myapplication.MainActivity
import fr.juju.myapplication.R
import fr.juju.myapplication.RepasRepository.Singleton.repasList
import fr.juju.myapplication.SemainierRepository.Singleton.semainierList
import java.util.*

class CourseListeFragment (val context: MainActivity
) : Fragment()  {


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater?.inflate(R.layout.course_liste_fragment, container, false)
        view.findViewById<Switch>(R.id.toggleButton).setOnClickListener{
            if( view.findViewById<Switch>(R.id.toggleButton).isChecked){
                view.findViewById<ConstraintLayout>(R.id.Midi).visibility = View.GONE
            }
            else view.findViewById<ConstraintLayout>(R.id.Midi).visibility = View.VISIBLE
        }

        view.findViewById<ImageView>(R.id.generateCourse).setOnClickListener(){
            generateCourse()
        }
        view.findViewById<ImageView>(R.id.clearCourse).setOnClickListener(){
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
                        var oldItem = courseList.filter { s->s.name == ingredient.name }[0]

                        if (oldItem.quantite.contains("cl"))
                        {
                            var value = oldItem.quantite.substring( 0, oldItem.quantite.indexOf("cl")).replace(" ", "").toInt()
                            var newValue = ingredient.quantite.substring( 0, ingredient.quantite.indexOf("cl")).replace(" ", "").toInt()
                            oldItem.quantite = ((value + newValue).toString()) + " cl"
                            repo.updateCourseItem(oldItem)
                        }



                    }

                    else {
                        courseList.add(courseItem)
                        repo.insertCourseItem(courseItem)
                    }
                }
            }
        }
    }


    private fun clearCourse(){
        var repo = CourseRepository()
        for(courseItem in courseList){
            repo.deleteCourseItem(courseItem)
        }
    }

}