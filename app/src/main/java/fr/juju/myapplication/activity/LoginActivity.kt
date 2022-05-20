package fr.juju.myapplication.activity

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import fr.juju.myapplication.R


class LoginActivity : AppCompatActivity() {
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var indication: TextView


    val MIN_PASSWORD_LENGTH = 6
    var auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var preferences: SharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE)
        var checkbox: String? = preferences.getString("remember", "")
        this.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finish()
            }
        })
        if (checkbox.equals("true")) {
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
            finish()
        } else if (checkbox.equals("false")) {
        }

        setContentView(R.layout.login_fragment)
        findViewById<Button>(R.id.bt_signup).setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        viewInitializations()

        val translate = AnimationUtils.loadAnimation(this, R.anim.translate_anim)
        findViewById<ConstraintLayout>(R.id.constraint).startAnimation(translate)

    }

    fun viewInitializations() {
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        indication = findViewById(R.id.textView14)

        val b = intent.extras
        if (b != null) {
            indication.visibility = View.VISIBLE
            etEmail.setText(b.getString("emailInput"))
        }
        else indication.visibility = View.GONE

        // To show back button in actionbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    // Checking if the input in form is valid
    fun validateInput(): Boolean {
        if (etEmail.text.toString() == "") {
            etEmail.error = "Please Enter Email"
            return false
        }
        if (etPassword.text.toString() == "") {
            etPassword.error = "Please Enter Password"
            return false
        }

        // checking the proper email format
        if (!isEmailValid(etEmail.text.toString())) {
            etEmail.error = "Please Enter Valid Email"
            return false
        }

        // checking minimum password Length
        if (etPassword.text.length < MIN_PASSWORD_LENGTH) {
            etPassword.error =
                "Password Length must be more than " + MIN_PASSWORD_LENGTH + "characters"
            return false
        }
        return true
    }

    fun isEmailValid(email: String?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Hook Click Event
    fun performSignUp(v: View) {
        val translateYImage = ObjectAnimator.ofFloat(
            findViewById<ImageView>(R.id.imageView12),
            View.TRANSLATION_Y,
            0F,
            1500F
        ).setDuration(500)

        val alphaImage = ObjectAnimator.ofFloat(
            findViewById<ImageView>(R.id.imageView12),
            View.ALPHA,
            1F,
            0F
        ).setDuration(400)


        val set = AnimatorSet()
        set.playTogether(translateYImage,alphaImage)

        set.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator) {
                findViewById<EditText>(R.id.et_email).visibility = View.GONE
                findViewById<ImageView>(R.id.image).visibility = View.GONE
                findViewById<EditText>(R.id.et_password).visibility = View.GONE
                findViewById<ImageView>(R.id.imageView13).visibility = View.GONE
                findViewById<TextView>(R.id.textView14).visibility = View.GONE
                findViewById<TextView>(R.id.go).visibility = View.GONE
                findViewById<TextView>(R.id.textView11).visibility = View.GONE
                findViewById<CheckBox>(R.id.checkBox).visibility = View.GONE
                findViewById<Button>(R.id.bt_signup).visibility = View.GONE
                findViewById<TextView>(R.id.tv_heading).visibility = View.GONE
                findViewById<ImageView>(R.id.imageView20).animate().alpha(0F).setDuration(200)
            }

            override fun onAnimationRepeat(animation: Animator) {
                // ...
            }

            override fun onAnimationEnd(animation: Animator) {
                val intent = Intent(baseContext, SplashActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
                finish()
            }

            override fun onAnimationCancel(animation: Animator) {
                // ...
            }
        })
    if (validateInput()) {
            // Input is valid, here send data to your server
            val email = etEmail!!.text.toString()
            val password = etPassword!!.text.toString()

            auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = Firebase.auth.currentUser
                    if(user?.isEmailVerified == true){
                        if (findViewById<CheckBox>(R.id.checkBox).isChecked) {
                            var preferences: SharedPreferences =
                                getSharedPreferences("checkbox", MODE_PRIVATE)
                            var editor: SharedPreferences.Editor = preferences.edit()
                            editor.putString("remember", "true")
                            editor.apply()
                        } else {
                            var preferences: SharedPreferences =
                                getSharedPreferences("checkbox", MODE_PRIVATE)
                            var editor: SharedPreferences.Editor = preferences.edit()
                            editor.putString("remember", "false")
                            editor.apply()
                        }
                        set.start()
                        Toast.makeText(this, "Vous êtes connecté ! <3", Toast.LENGTH_SHORT).show()
                    }else {
                        Toast.makeText(this, "Email non vérifié ! Veuillez valider votre inscription en cliquant sur le lien envoyé par mail :)",Toast.LENGTH_LONG).show()
                    }

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(
                        baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }

    }

}
