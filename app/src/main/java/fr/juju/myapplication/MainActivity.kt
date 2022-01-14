package fr.juju.myapplication

import android.content.Context.INPUT_METHOD_SERVICE
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import fr.juju.myapplication.fragments.*
import io.grpc.Context
import java.util.logging.Logger.global

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        loadFragmentHome(HomeFragment(this))
        unprintSoir()
        unprintApero()
        unprintMidi()
        val navigationView = findViewById<BottomNavigationView>(R.id.navigation_view)
        navigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.home_page->{
                    unprintSoir()
                    unprintApero()
                    unprintMidi()
                    loadFragmentHome(HomeFragment(this))
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.cookies->{
                    unprintSoir()
                    unprintApero()
                    unprintMidi()
                    loadFragment(FiltreRepasFragment(this, "None", "None"))
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.calendar->{
                    unprintSoir()
                    unprintApero()
                    unprintMidi()
                    loadFragment(SemainierFragment(this, "None"))
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.course->{
                    unprintSoir()
                    unprintApero()
                    unprintMidi()
                    loadFragment(CourseListeFragment(this))
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
    }

    fun hideKeyboard(){
        val view = this.currentFocus
        if(view != null){
            val hideMe = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            hideMe.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun loadFragment(fragment: Fragment) {

        //injecter le fragment dans notre boite fragment container
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()

    }

    fun loadFragmentHome(fragment: Fragment) {
        //injecter le fragment dans notre boite fragment container
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun printSoir(){
        findViewById<ConstraintLayout>(R.id.toggle_soir).visibility = View.VISIBLE
    }
    fun unprintSoir(){
        findViewById<ConstraintLayout>(R.id.toggle_soir).visibility = View.GONE
    }
    fun printApero(){
        findViewById<ConstraintLayout>(R.id.toggle_apero).visibility = View.VISIBLE
    }
    fun unprintApero(){
        findViewById<ConstraintLayout>(R.id.toggle_apero).visibility = View.GONE
    }
    fun printMidi(){
        findViewById<ConstraintLayout>(R.id.toggle_midi).visibility = View.VISIBLE
    }
    fun unprintMidi(){
        findViewById<ConstraintLayout>(R.id.toggle_midi).visibility = View.GONE
    }
}