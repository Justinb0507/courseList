package fr.juju.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import fr.juju.myapplication.MainActivity
import fr.juju.myapplication.R

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




        return view
    }
}