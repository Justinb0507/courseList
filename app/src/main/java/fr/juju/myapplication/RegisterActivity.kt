package fr.juju.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {
    lateinit var etEmail: EditText
    lateinit var  etPassword: EditText
    lateinit var  signin: Button
    lateinit var  go: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_fragment)
        viewInitializations()

        var auth: FirebaseAuth
        auth = Firebase.auth
        go.setOnClickListener{
            auth.createUserWithEmailAndPassword(etEmail.text.toString(), etPassword.text.toString())
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        val user = auth.currentUser
                        val databaseRef = FirebaseDatabase.getInstance()
                        if (user != null) {
                            Toast.makeText(baseContext, "Bienvenue ! <3",
                                Toast.LENGTH_SHORT).show()
                            databaseRef.reference.child(user.uid).setValue(DBModel())
                        }
                        val intent = Intent(this, SplashActivity::class.java)
                        startActivity(intent)
                        finish()

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(baseContext, "Authentication failed",
                            Toast.LENGTH_SHORT).show()
                    }
                }
        }
        signin.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
    fun viewInitializations() {
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        signin = findViewById(R.id.bt_signup)
        go = findViewById(R.id.textView12)
        // To show back button in actionbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
}