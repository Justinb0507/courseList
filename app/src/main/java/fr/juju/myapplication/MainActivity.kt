package fr.juju.myapplication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import fr.juju.myapplication.fragments.*


class MainActivity : AppCompatActivity() {

    lateinit var navigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigationView = findViewById<BottomNavigationView>(R.id.navigation_view)

        findViewById<ImageView>(R.id.imageView).setOnClickListener{
            var preferences : SharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE)
            var editor : SharedPreferences.Editor = preferences.edit()
            editor.putString("remember", "false")
            editor.apply()
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        loadFragmentHome(HomeFragment(this))
        unprintSoir()
        unprintApero()
        unprintMidi()
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
                    loadFragment(FiltreRepasFragment(this, "None", "None", "None"))
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.calendar->{
                    unprintSoir()
                    unprintApero()
                    unprintMidi()
                    loadFragment(SemainierFragment(this, "None", "None"))
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

    fun selectedCourse(){
        navigationView.getMenu().getItem(3).setChecked(true)
    }
    fun selectedSemainier(){
        navigationView.getMenu().getItem(2).setChecked(true)
    }
    fun selectedRecettes(){
        navigationView.getMenu().getItem(1).setChecked(true)
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
        findViewById<ConstraintLayout>(R.id.toggle_soir).alpha = 1F
    }
    fun unprintSoir(){
        findViewById<ConstraintLayout>(R.id.toggle_soir).visibility = View.GONE
    }
    fun printApero(){
        findViewById<ConstraintLayout>(R.id.toggle_apero).visibility = View.VISIBLE
        findViewById<ConstraintLayout>(R.id.toggle_apero).alpha = 1F
    }
    fun unprintApero(){
        findViewById<ConstraintLayout>(R.id.toggle_apero).visibility = View.GONE
    }
    fun printMidi(){
        findViewById<ConstraintLayout>(R.id.toggle_midi).visibility = View.VISIBLE
        findViewById<ConstraintLayout>(R.id.toggle_midi).alpha = 1F
    }
    fun unprintMidi(){
        findViewById<ConstraintLayout>(R.id.toggle_midi).visibility = View.GONE
    }
    fun animationSoir(){
        val animation = AnimationUtils.loadAnimation(this, R.anim.transformation_button_animation)
        val scaleX = ObjectAnimator.ofFloat(findViewById<ConstraintLayout>(R.id.toggle_soir), View.SCALE_X, 1.0f, 4F).setDuration(200)
        val alpha = ObjectAnimator.ofFloat(findViewById<ConstraintLayout>(R.id.toggle_soir), View.ALPHA, 0F).setDuration(150)
        val translateX = ObjectAnimator.ofFloat(findViewById<ConstraintLayout>(R.id.toggle_soir), View.TRANSLATION_X, 1.0f, -150f).setDuration(200)
        val scaleY = ObjectAnimator.ofFloat(findViewById<ConstraintLayout>(R.id.toggle_soir), View.SCALE_Y, 1.0f, 4f).setDuration(200)

        val set = AnimatorSet()
        set.playTogether(scaleX,scaleY, translateX, alpha)

        set.start()

    }

    fun nonAnimationSoir(){
        val animation = AnimationUtils.loadAnimation(this, R.anim.transformation_button_animation)
        val scaleX = ObjectAnimator.ofFloat(findViewById<ConstraintLayout>(R.id.toggle_soir), View.SCALE_X, 4.0f, 1F).setDuration(200)
        val translateX = ObjectAnimator.ofFloat(findViewById<ConstraintLayout>(R.id.toggle_soir), View.TRANSLATION_X, -150f, 1f).setDuration(200)
        val scaleY = ObjectAnimator.ofFloat(findViewById<ConstraintLayout>(R.id.toggle_soir), View.SCALE_Y,  4f,1.0F).setDuration(200)

        val set = AnimatorSet()
        set.playTogether(scaleX,scaleY, translateX)

        set.start()

    }

    fun animationMidi(){
        val animation = AnimationUtils.loadAnimation(this, R.anim.transformation_button_animation)
        val scaleX = ObjectAnimator.ofFloat(findViewById<ConstraintLayout>(R.id.toggle_soir), View.SCALE_X, 1.0f, 4F).setDuration(200)
        val alpha = ObjectAnimator.ofFloat(findViewById<ConstraintLayout>(R.id.toggle_soir), View.ALPHA, 0F).setDuration(150)
        val translateX = ObjectAnimator.ofFloat(findViewById<ConstraintLayout>(R.id.toggle_soir), View.TRANSLATION_X, 1.0f, -150f).setDuration(200)
        val scaleY = ObjectAnimator.ofFloat(findViewById<ConstraintLayout>(R.id.toggle_soir), View.SCALE_Y, 1.0f, 4f).setDuration(200)

        val set = AnimatorSet()
        set.playTogether(scaleX,scaleY, translateX, alpha)

        set.start()

    }

    fun nonAnimationMidi(){
        val animation = AnimationUtils.loadAnimation(this, R.anim.transformation_button_animation)
        val scaleX = ObjectAnimator.ofFloat(findViewById<ConstraintLayout>(R.id.toggle_soir), View.SCALE_X, 4.0f, 1F).setDuration(200)
        val translateX = ObjectAnimator.ofFloat(findViewById<ConstraintLayout>(R.id.toggle_soir), View.TRANSLATION_X, -150f, 1f).setDuration(200)
        val scaleY = ObjectAnimator.ofFloat(findViewById<ConstraintLayout>(R.id.toggle_soir), View.SCALE_Y,  4f,1.0F).setDuration(200)

        val set = AnimatorSet()
        set.playTogether(scaleX,scaleY, translateX)

        set.start()

    }

    fun animationApero(){
        val animation = AnimationUtils.loadAnimation(this, R.anim.transformation_button_animation)
        val scaleX = ObjectAnimator.ofFloat(findViewById<ConstraintLayout>(R.id.toggle_soir), View.SCALE_X, 1.0f, 4F).setDuration(200)
        val alpha = ObjectAnimator.ofFloat(findViewById<ConstraintLayout>(R.id.toggle_soir), View.ALPHA, 0F).setDuration(150)
        val translateX = ObjectAnimator.ofFloat(findViewById<ConstraintLayout>(R.id.toggle_soir), View.TRANSLATION_X, 1.0f, -150f).setDuration(200)
        val scaleY = ObjectAnimator.ofFloat(findViewById<ConstraintLayout>(R.id.toggle_soir), View.SCALE_Y, 1.0f, 4f).setDuration(200)

        val set = AnimatorSet()
        set.playTogether(scaleX,scaleY, translateX, alpha)

        set.start()

    }

    fun nonAnimationApero(){
        val animation = AnimationUtils.loadAnimation(this, R.anim.transformation_button_animation)
        val scaleX = ObjectAnimator.ofFloat(findViewById<ConstraintLayout>(R.id.toggle_soir), View.SCALE_X, 4.0f, 1F).setDuration(200)
        val translateX = ObjectAnimator.ofFloat(findViewById<ConstraintLayout>(R.id.toggle_soir), View.TRANSLATION_X, -150f, 1f).setDuration(200)
        val scaleY = ObjectAnimator.ofFloat(findViewById<ConstraintLayout>(R.id.toggle_soir), View.SCALE_Y,  4f,1.0F).setDuration(200)

        val set = AnimatorSet()
        set.playTogether(scaleX,scaleY, translateX)

        set.start()

    }

}