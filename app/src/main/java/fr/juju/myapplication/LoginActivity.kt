package fr.juju.myapplication

import android.content.ContentValues.TAG
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import org.json.JSONObject
import org.json.JSONTokener


class LoginActivity : AppCompatActivity() {
    lateinit var etEmail: EditText
    lateinit var  etPassword: EditText
    val MIN_PASSWORD_LENGTH = 6
    var auth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var preferences : SharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE)
        var checkbox : String? = preferences.getString("remember", "")
        this.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                finish()
            }
        })
        if (checkbox.equals("true")){
            val intent = Intent(this, SplashActivity::class.java)
            startActivity(intent)
            finish()
        } else if (checkbox.equals("false")){
        }

        setContentView(R.layout.login_fragment)
        findViewById<Button>(R.id.bt_signup).setOnClickListener{
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }
        viewInitializations()

    }

    fun viewInitializations() {
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)

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
            etPassword.error = "Password Length must be more than " + MIN_PASSWORD_LENGTH + "characters"
            return false
        }
        return true
    }

    fun isEmailValid(email: String?): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }

    // Hook Click Event
    fun performSignUp(v: View) {
        /*if (validateInput()) {
            // Input is valid, here send data to your server
            val email = etEmail!!.text.toString()
            val password = etPassword!!.text.toString()

            //AJOUTER L AUTHENTIFICATION ICI
            }

        }*/
        auth.signInWithEmailAndPassword("test@gmail.com", "cookeat").addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                if(findViewById<CheckBox>(R.id.checkBox).isChecked){
                    var preferences : SharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE)
                    var editor : SharedPreferences.Editor = preferences.edit()
                    editor.putString("remember", "true")
                    editor.apply()
                }
                else {
                    var preferences : SharedPreferences = getSharedPreferences("checkbox", MODE_PRIVATE)
                    var editor : SharedPreferences.Editor = preferences.edit()
                    editor.putString("remember", "false")
                    editor.apply()
                }
                Toast.makeText(this,"Vous êtes connecté ! <3", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, SplashActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // If sign in fails, display a message to the user.
                Log.w(TAG, "signInWithEmail:failure", task.exception)
                Toast.makeText(baseContext, "Authentication failed.",
                    Toast.LENGTH_SHORT).show()
            }
    }}
}