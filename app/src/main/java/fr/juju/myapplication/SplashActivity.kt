package fr.juju.myapplication

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.widget.ImageView
import android.widget.TextView
import org.json.JSONObject
import org.json.JSONTokener


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val repo = SemainierRepository()
        repo.removeLink()
        val repo2 = RepasRepository()
        repo2.removeLink()
        val repo3 = IngredientRepository()
        repo3.removeLink()
        val repo4 = CategorieRepository()
        repo4.removeLink()
        val repo5 = CourseRepository()
        repo5.removeLink()
        val repo6 = SemainierSuivantRepository()
        repo6.removeLink()

        repo.updateData  {  }
        repo2.updateData {  }
        repo3.updateData {  }
        repo4.updateData {  }
        repo5.updateData {  }
        repo6.updateData {  }
        findViewById<ImageView>(R.id.logo).alpha = 0f
        findViewById<TextView>(R.id.designedBy).alpha = 0f
        findViewById<TextView>(R.id.designedBy).animate().setDuration(2000).alpha(1f)
        findViewById<ImageView>(R.id.logo).animate().setDuration(2000).alpha(1f).withEndAction{
                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                finish()
        }

    }


}