package fr.juju.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import fr.juju.myapplication.RepasRepository.Singleton.repasList
import fr.juju.myapplication.SemainierRepository.Singleton.semainierList

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val repo = SemainierRepository()
        val repo2 = RepasRepository()
        val repo3 = IngredientRepository()
        val repo4 = CategorieRepository()
        repo.updateData  {  }
        repo2.updateData { }
        repo3.updateData {  }
        repo4.updateData {  }

        findViewById<ImageView>(R.id.logo).alpha = 0f
        findViewById<TextView>(R.id.designedBy).alpha = 0f
        findViewById<TextView>(R.id.designedBy).animate().setDuration(3000).alpha(1f)
        findViewById<ImageView>(R.id.logo).animate().setDuration(3000).alpha(1f).withEndAction{

                val i = Intent(this, MainActivity::class.java)
                startActivity(i)
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
                finish()
        }

    }
}