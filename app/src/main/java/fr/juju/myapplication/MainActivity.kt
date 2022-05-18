package fr.juju.myapplication

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.AlertDialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import fr.juju.myapplication.CategorieRepository.Singleton.categorieList
import fr.juju.myapplication.IngredientRepository.Singleton.ingredientList
import fr.juju.myapplication.RepasCommunRepository.Singleton.repasCommunList
import fr.juju.myapplication.RepasRepository.Singleton.repasList
import fr.juju.myapplication.fragments.*
import java.util.*
import kotlin.collections.ArrayList


class MainActivity : AppCompatActivity() {

    lateinit var navigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        navigationView = findViewById<BottomNavigationView>(R.id.navigation_view)
        findViewById<ImageView>(R.id.imageView2).setOnClickListener {
            loadFragment(UserFragment(this))
        }
        loadFragment(HomeFragment(this))
        unprintSoir()
        unprintApero()
        unprintMidi()
        unprintAutres()
        navigationView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home_page -> {
                    unprintSoir()
                    unprintApero()
                    unprintMidi()
                    unprintAutres()
                    loadFragment(HomeFragment(this))
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.cookies -> {
                    unprintSoir()
                    unprintApero()
                    unprintMidi()
                    unprintAutres()
                    loadFragment(FiltreRepasFragment(this, "None", "None", "None"))
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.calendar -> {
                    unprintSoir()
                    unprintApero()
                    unprintMidi()
                    unprintAutres()
                    loadFragment(SemainierFragment(this, "None", "None"))
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.course -> {
                    unprintSoir()
                    unprintApero()
                    unprintMidi()
                    unprintAutres()
                    loadFragment(CourseListeFragment(this))
                    return@setOnNavigationItemSelectedListener true
                }
                else -> false
            }
        }
    }

    fun selectedCourse() {
        navigationView.getMenu().getItem(3).setChecked(true)
    }

    fun selectedSemainier() {
        navigationView.getMenu().getItem(2).setChecked(true)
    }

    fun selectedRecettes() {
        navigationView.getMenu().getItem(1).setChecked(true)
    }

    fun selectedHome() {
        navigationView.getMenu().getItem(0).setChecked(true)
    }

    fun hideKeyboard() {
        val view = this.currentFocus
        if (view != null) {
            val hideMe = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            hideMe.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun loadFragment(fragment: Fragment) {
        //injecter le fragment dans notre boite fragment container
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment).setReorderingAllowed(true)
        if (fragment is AddRepasFragment) {
            transaction.addToBackStack("AddRepas")
        } else if (fragment is AddRepasCommunFragment) {
            transaction.addToBackStack("AddRepasCommun")
        } else if (fragment is EditRepasFragment) {
                transaction.addToBackStack("EditRepas")
        } else if (fragment is CourseListeFragment) {
            transaction.addToBackStack("CourseListe")
        } else if (fragment is FiltreRepasFragment) {
            transaction.addToBackStack("FiltreRepas")
        } else if (fragment is HomeFragment) {
            transaction.addToBackStack("Home")
        } else if (fragment is RecetteFragment) {
            transaction.addToBackStack("Recette")
        } else if (fragment is ResultResearchFragment) {
            transaction.addToBackStack("ResultResearch")
        } else if (fragment is SemainierFragment) {
            transaction.addToBackStack("Semainier")
        } else if (fragment is UserFragment) {
            transaction.addToBackStack("User")
        } else transaction.addToBackStack(null)

        transaction.commit()

    }

    fun handleBack() {
        val fm: FragmentManager = supportFragmentManager
        var fragment = fm.getBackStackEntryAt(fm.backStackEntryCount - 2)
        if (fragment.name == "EditRepas" || fragment.name == "AddRepas" || fragment.name == "AddRepasCommun") {
            var fragment2 = fm.getBackStackEntryAt(fm.backStackEntryCount - 4)
            if (fragment2.name == "EditRepas" || fragment2.name == "AddRepas" || fragment.name == "AddRepasCommun") {
                var fragment3 = fm.getBackStackEntryAt(fm.backStackEntryCount - 5)
                if (fragment3.name == "CourseListe") {
                    selectedCourse()
                } else if (fragment3.name == "FiltreRepas") {
                    selectedRecettes()
                    fm.popBackStack(fragment3.name, 0)
                } else if (fragment3.name == "Home") {
                    selectedHome()
                    fm.popBackStack(fragment3.name, 0)
                } else if (fragment3.name == "Recette") {
                    selectedRecettes()
                    fm.popBackStack(fragment3.name, 0)
                } else if (fragment3.name == "ResultResearch") {
                    selectedRecettes()
                    fm.popBackStack(fragment3.name, 0)
                } else if (fragment3.name == "Semainier") {
                    findViewById<BottomNavigationItemView>(R.id.calendar).performClick()
                }
            }
            else {
                if (fragment2.name == "CourseListe") {
                    selectedCourse()
                    fm.popBackStack(fragment2.name, 0)
                } else if (fragment2.name == "FiltreRepas") {
                    selectedRecettes()
                    fm.popBackStack(fragment2.name, 0)
                } else if (fragment2.name == "Home") {
                    selectedHome()
                    fm.popBackStack(fragment2.name, 0)
                } else if (fragment2.name == "Recette") {
                    selectedRecettes()
                    fm.popBackStack(fragment2.name, 0)
                } else if (fragment2.name == "ResultResearch") {
                    selectedRecettes()
                    fm.popBackStack(fragment2.name, 0)
                } else if (fragment2.name == "Semainier") {
                    findViewById<BottomNavigationItemView>(R.id.calendar).performClick()
                }
            }
        }
        else {
            if (fragment.name == "CourseListe") {
                selectedCourse()
                fm.popBackStack()
            } else if (fragment.name == "FiltreRepas") {
                selectedRecettes()
                fm.popBackStack()
            } else if (fragment.name == "Home") {
                selectedHome()
                fm.popBackStack()
            } else if (fragment.name == "Recette") {
                selectedRecettes()
                fm.popBackStack()
            } else if (fragment.name == "ResultResearch") {
                selectedRecettes()
                fm.popBackStack()
            } else if (fragment.name == "Semainier") {
                findViewById<BottomNavigationItemView>(R.id.calendar).performClick()
            }
            else fm.popBackStack()
        }
    }

    fun printSoir() {
        findViewById<ConstraintLayout>(R.id.toggle_soir).visibility = View.VISIBLE
        findViewById<ConstraintLayout>(R.id.toggle_soir).alpha = 1F
    }

    fun printAutres() {
        findViewById<ConstraintLayout>(R.id.toggle_autres).visibility = View.VISIBLE
        findViewById<ConstraintLayout>(R.id.toggle_autres).alpha = 1F
    }

    fun unprintAutres() {
        findViewById<ConstraintLayout>(R.id.toggle_autres).visibility = View.GONE
    }

    fun unprintSoir() {
        findViewById<ConstraintLayout>(R.id.toggle_soir).visibility = View.GONE
    }

    fun printApero() {
        findViewById<ConstraintLayout>(R.id.toggle_apero).visibility = View.VISIBLE
        findViewById<ConstraintLayout>(R.id.toggle_apero).alpha = 1F
    }

    fun unprintApero() {
        findViewById<ConstraintLayout>(R.id.toggle_apero).visibility = View.GONE
    }

    fun printMidi() {
        findViewById<ConstraintLayout>(R.id.toggle_midi).visibility = View.VISIBLE
        findViewById<ConstraintLayout>(R.id.toggle_midi).alpha = 1F
    }

    fun unprintMidi() {
        findViewById<ConstraintLayout>(R.id.toggle_midi).visibility = View.GONE
    }

    fun animationSoir() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.transformation_button_animation)
        val scaleX = ObjectAnimator.ofFloat(
            findViewById<ConstraintLayout>(R.id.toggle_soir),
            View.SCALE_X,
            1.0f,
            4F
        ).setDuration(200)
        val alpha =
            ObjectAnimator.ofFloat(findViewById<ConstraintLayout>(R.id.toggle_soir), View.ALPHA, 0F)
                .setDuration(150)
        val translateX = ObjectAnimator.ofFloat(
            findViewById<ConstraintLayout>(R.id.toggle_soir),
            View.TRANSLATION_X,
            1.0f,
            -150f
        ).setDuration(200)
        val scaleY = ObjectAnimator.ofFloat(
            findViewById<ConstraintLayout>(R.id.toggle_soir),
            View.SCALE_Y,
            1.0f,
            4f
        ).setDuration(200)

        val set = AnimatorSet()
        set.playTogether(scaleX, scaleY, translateX, alpha)

        set.start()

    }

    fun nonAnimationSoir() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.transformation_button_animation)
        val scaleX = ObjectAnimator.ofFloat(
            findViewById<ConstraintLayout>(R.id.toggle_soir),
            View.SCALE_X,
            4.0f,
            1F
        ).setDuration(200)
        val translateX = ObjectAnimator.ofFloat(
            findViewById<ConstraintLayout>(R.id.toggle_soir),
            View.TRANSLATION_X,
            -150f,
            1f
        ).setDuration(200)
        val scaleY = ObjectAnimator.ofFloat(
            findViewById<ConstraintLayout>(R.id.toggle_soir),
            View.SCALE_Y,
            4f,
            1.0F
        ).setDuration(200)

        val set = AnimatorSet()
        set.playTogether(scaleX, scaleY, translateX)

        set.start()

    }

    fun animationMidi() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.transformation_button_animation)
        val scaleX = ObjectAnimator.ofFloat(
            findViewById<ConstraintLayout>(R.id.toggle_soir),
            View.SCALE_X,
            1.0f,
            4F
        ).setDuration(200)
        val alpha =
            ObjectAnimator.ofFloat(findViewById<ConstraintLayout>(R.id.toggle_soir), View.ALPHA, 0F)
                .setDuration(150)
        val translateX = ObjectAnimator.ofFloat(
            findViewById<ConstraintLayout>(R.id.toggle_soir),
            View.TRANSLATION_X,
            1.0f,
            -150f
        ).setDuration(200)
        val scaleY = ObjectAnimator.ofFloat(
            findViewById<ConstraintLayout>(R.id.toggle_soir),
            View.SCALE_Y,
            1.0f,
            4f
        ).setDuration(200)

        val set = AnimatorSet()
        set.playTogether(scaleX, scaleY, translateX, alpha)

        set.start()

    }

    fun nonAnimationMidi() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.transformation_button_animation)
        val scaleX = ObjectAnimator.ofFloat(
            findViewById<ConstraintLayout>(R.id.toggle_soir),
            View.SCALE_X,
            4.0f,
            1F
        ).setDuration(200)
        val translateX = ObjectAnimator.ofFloat(
            findViewById<ConstraintLayout>(R.id.toggle_soir),
            View.TRANSLATION_X,
            -150f,
            1f
        ).setDuration(200)
        val scaleY = ObjectAnimator.ofFloat(
            findViewById<ConstraintLayout>(R.id.toggle_soir),
            View.SCALE_Y,
            4f,
            1.0F
        ).setDuration(200)

        val set = AnimatorSet()
        set.playTogether(scaleX, scaleY, translateX)

        set.start()

    }

    fun animationApero() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.transformation_button_animation)
        val scaleX = ObjectAnimator.ofFloat(
            findViewById<ConstraintLayout>(R.id.toggle_soir),
            View.SCALE_X,
            1.0f,
            4F
        ).setDuration(200)
        val alpha =
            ObjectAnimator.ofFloat(findViewById<ConstraintLayout>(R.id.toggle_soir), View.ALPHA, 0F)
                .setDuration(150)
        val translateX = ObjectAnimator.ofFloat(
            findViewById<ConstraintLayout>(R.id.toggle_soir),
            View.TRANSLATION_X,
            1.0f,
            -150f
        ).setDuration(200)
        val scaleY = ObjectAnimator.ofFloat(
            findViewById<ConstraintLayout>(R.id.toggle_soir),
            View.SCALE_Y,
            1.0f,
            4f
        ).setDuration(200)

        val set = AnimatorSet()
        set.playTogether(scaleX, scaleY, translateX, alpha)

        set.start()

    }

    fun nonAnimationApero() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.transformation_button_animation)
        val scaleX = ObjectAnimator.ofFloat(
            findViewById<ConstraintLayout>(R.id.toggle_soir),
            View.SCALE_X,
            4.0f,
            1F
        ).setDuration(200)
        val translateX = ObjectAnimator.ofFloat(
            findViewById<ConstraintLayout>(R.id.toggle_soir),
            View.TRANSLATION_X,
            -150f,
            1f
        ).setDuration(200)
        val scaleY = ObjectAnimator.ofFloat(
            findViewById<ConstraintLayout>(R.id.toggle_soir),
            View.SCALE_Y,
            4f,
            1.0F
        ).setDuration(200)

        val set = AnimatorSet()
        set.playTogether(scaleX, scaleY, translateX)

        set.start()

    }

    fun animationAutres() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.transformation_button_animation)
        val scaleX = ObjectAnimator.ofFloat(
            findViewById<ConstraintLayout>(R.id.toggle_autres),
            View.SCALE_X,
            1.0f,
            4F
        ).setDuration(200)
        val alpha =
            ObjectAnimator.ofFloat(
                findViewById<ConstraintLayout>(R.id.toggle_autres),
                View.ALPHA,
                0F
            )
                .setDuration(150)
        val translateX = ObjectAnimator.ofFloat(
            findViewById<ConstraintLayout>(R.id.toggle_autres),
            View.TRANSLATION_X,
            1.0f,
            -150f
        ).setDuration(200)
        val scaleY = ObjectAnimator.ofFloat(
            findViewById<ConstraintLayout>(R.id.toggle_autres),
            View.SCALE_Y,
            1.0f,
            4f
        ).setDuration(200)

        val set = AnimatorSet()
        set.playTogether(scaleX, scaleY, translateX, alpha)

        set.start()

    }

    fun nonAnimationAutres() {
        val animation = AnimationUtils.loadAnimation(this, R.anim.transformation_button_animation)
        val scaleX = ObjectAnimator.ofFloat(
            findViewById<ConstraintLayout>(R.id.toggle_autres),
            View.SCALE_X,
            4.0f,
            1F
        ).setDuration(200)
        val translateX = ObjectAnimator.ofFloat(
            findViewById<ConstraintLayout>(R.id.toggle_autres),
            View.TRANSLATION_X,
            -150f,
            1f
        ).setDuration(200)
        val scaleY = ObjectAnimator.ofFloat(
            findViewById<ConstraintLayout>(R.id.toggle_autres),
            View.SCALE_Y,
            4f,
            1.0F
        ).setDuration(200)

        val set = AnimatorSet()
        set.playTogether(scaleX, scaleY, translateX)

        set.start()

    }

    fun research(parameter: String): ArrayList<RepasModel> {

        val resultResearch = arrayListOf<RepasModel>()
        var listResearch = arrayListOf<String>()

        if (parameter.isNotEmpty()){
            for (item in parameter.split(" ")){
                var temp = item.lowercase(Locale.getDefault())
                listResearch.add(temp)
                temp = temp.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(
                        Locale.getDefault()
                    ) else it.toString()
                }
                listResearch.add(temp)
            }
        }

        if (parameter.isNotEmpty()){
            if (!repasList.filter { se -> se.name.contains(parameter) }.isEmpty()) {
                for (repas in repasList.filter { se -> se.name.contains(parameter) }
                    .sortedBy { s -> s.name }) {
                    if (!resultResearch.contains(repas)) {
                        resultResearch.add(repas)
                    }
                }
            }
        }
        
        if (listResearch.isNotEmpty()) for (tag in listResearch) {

            if (!repasList.filter { se -> se.name == tag }.isEmpty()) {
                for (repas in repasList.filter { se -> se.name == tag }
                    .sortedBy { s -> s.name }) {
                    if (!resultResearch.contains(repas)) {
                        resultResearch.add(repas)
                    }
                }
            }

            if (!repasList.filter { se -> se.tags.contains(tag) }.isEmpty()) {
                for (repas in repasList.filter { se -> se.tags.contains(tag) }
                    .sortedBy { s -> s.name }) {
                    if (!resultResearch.contains(repas)) {
                        resultResearch.add(repas)
                    }
                }
            }

            if (!ingredientList.filter { se -> se.name == tag }.isEmpty()) {
                for (ingredient in ingredientList.filter { se -> se.name == tag }) {
                    for (repas in repasList.filter { se -> se.id == ingredient.id_repas }) {
                        if (!resultResearch.contains(repas)) {
                            resultResearch.add(repas)
                        }
                    }
                }
            }

            if (!categorieList.filter { se -> se.name == tag }.isEmpty()) {
                for (categorie in categorieList.filter { se -> se.name == tag }) {
                    for (ingredient in ingredientList.filter { se -> se.id_categorie == categorie.id }) {
                        for (repas in repasList.filter { se -> se.id == ingredient.id_repas }) {
                            if (!resultResearch.contains(repas)) {
                                resultResearch.add(repas)
                            }
                        }
                    }
                }
            }

            if (!repasList.filter { se -> se.name.contains(tag) }.isEmpty()) {
                for (repas in repasList.filter { se -> se.name.contains(tag) }
                    .sortedBy { s -> s.name }) {
                    if (!resultResearch.contains(repas)) {
                        resultResearch.add(repas)
                    }
                }
            }
            if (!ingredientList.filter { se -> se.name.contains(tag) }.isEmpty()) {
                for (ingredient in ingredientList.filter { se -> se.name.contains(tag) }) {
                    for (repas in repasList.filter { se -> se.id == ingredient.id_repas }) {
                        if (!resultResearch.contains(repas)) {
                            resultResearch.add(repas)
                        }
                    }
                }
            }

            if (!categorieList.filter { se -> se.name.contains(tag) }.isEmpty()) {
                for (categorie in categorieList.filter { se -> se.name.contains(tag) }) {
                    for (ingredient in ingredientList.filter { se -> se.id_categorie == categorie.id }) {
                        for (repas in repasList.filter { se -> se.id == ingredient.id_repas }) {
                            if (!resultResearch.contains(repas)) {
                                resultResearch.add(repas)
                            }
                        }
                    }
                }
            }
        }


        return resultResearch

    }

}