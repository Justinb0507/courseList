package fr.juju.myapplication

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import org.json.JSONObject
import org.json.JSONTokener

class RegisterActivity : AppCompatActivity() {
    lateinit var etEmail: EditText
    lateinit var  etPassword: EditText
    lateinit var  signin: Button
    lateinit var  go: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_fragment)
        this.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                signin.performClick()
            }
        })
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
                            databaseRef.reference.child(user.uid).setValue(newDBModel())
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

    fun newDBModel(): Any {
        val categorie : MutableMap<String, CategorieModel> = mutableMapOf<String, CategorieModel>()
        val repas : MutableMap<String, RepasModel> = mutableMapOf<String, RepasModel>()
        val ingredients : MutableMap<String, IngredientModel> = mutableMapOf<String, IngredientModel>()

        val semainier : MutableMap<String, SemainierModel> = mutableMapOf<String, SemainierModel>(
            "lundi" to  SemainierModel("None", "None", "None", arrayListOf<String>(), "lundi"),
            "mardi" to  SemainierModel("None", "None", "None",arrayListOf<String>(), "mardi"),
            "mercredi" to  SemainierModel("None", "None", "None",arrayListOf<String>(), "mercredi"),
            "jeudi" to  SemainierModel("None", "None", "None",arrayListOf<String>(), "jeudi"),
            "vendredi" to  SemainierModel("None", "None", "None",arrayListOf<String>(), "vendredi"),
            "samedi" to  SemainierModel("None", "None", "None", arrayListOf<String>(),"samedi"),
            "dimanche" to  SemainierModel("None", "None", "None",arrayListOf<String>(), "dimanche")
        )
        val semainierSuivant : MutableMap<String, SemainierModel> = mutableMapOf<String, SemainierModel>(
            "lundi" to  SemainierModel("None", "None", "None",arrayListOf<String>(), "lundi"),
            "mardi" to  SemainierModel("None", "None", "None",arrayListOf<String>(), "mardi"),
            "mercredi" to  SemainierModel("None", "None", "None",arrayListOf<String>(), "mercredi"),
            "jeudi" to  SemainierModel("None", "None", "None",arrayListOf<String>(), "jeudi"),
            "vendredi" to  SemainierModel("None", "None", "None",arrayListOf<String>(), "vendredi"),
            "samedi" to  SemainierModel("None", "None", "None",arrayListOf<String>(), "samedi"),
            "dimanche" to  SemainierModel("None", "None", "None",arrayListOf<String>(), "dimanche")
        )
        val jsonObject = JSONTokener(
            """  {
    "categorie" : {
      "categorie1" : {
        "id" : "categorie1",
        "name" : "Légumes"
      },
      "categorie10" : {
        "id" : "categorie10",
        "name" : "Surgelés"
      },
      "categorie11" : {
        "id" : "categorie11",
        "name" : "Pâtisseries"
      },
      "categorie12" : {
        "id" : "categorie12",
        "name" : "Epices et condiments"
      },
      "categorie13" : {
        "id" : "categorie13",
        "name" : "Alcool"
      },
      "categorie14" : {
        "id" : "categorie14",
        "name" : "Boisson"
      },
      "categorie15" : {
        "id" : "categorie15",
        "name" : "Ménage"
      },
      "categorie16" : {
        "id" : "categorie16",
        "name" : "Hygiène"
      },
      "categorie17" : {
        "id" : "categorie17",
        "name" : "Autres"
      },
      "categorie18" : {
        "id" : "categorie18",
        "name" : "Apéro"
      },
      "categorie19" : {
        "id" : "categorie19",
        "name" : "Fromages"
      },
      "categorie2" : {
        "id" : "categorie2",
        "name" : "Fruits"
      },
      "categorie20" : {
        "id" : "categorie20",
        "name" : "Yaourts"
      },
      "categorie21" : {
        "id" : "categorie21",
        "name" : "Pains"
      },
      "categorie22" : {
        "id" : "categorie22",
        "name" : "Plats Préparés"
      },
      "categorie3" : {
        "id" : "categorie3",
        "name" : "Produits laitier"
      },
      "categorie4" : {
        "id" : "categorie4",
        "name" : "Viandes"
      },
      "categorie5" : {
        "id" : "categorie5",
        "name" : "Poisson"
      },
      "categorie6" : {
        "id" : "categorie6",
        "name" : "Protéines végétales"
      },
      "categorie7" : {
        "id" : "categorie7",
        "name" : "Féculents"
      },
      "categorie8" : {
        "id" : "categorie8",
        "name" : "Conserves"
      },
      "categorie9" : {
        "id" : "categorie9",
        "name" : "Graines"
      }
    },
    "ingredients" : {
      "007362b9-9a53-4cc8-b546-4ebf08d636e8" : {
        "id" : "007362b9-9a53-4cc8-b546-4ebf08d636e8",
        "id_categorie" : "categorie3",
        "id_repas" : "13a6e228-b415-4365-9972-314f036d109c",
        "name" : "Fromage à gratin",
        "quantite" : "20g",
        "rank" : 0
      },
      "02b4ba0a-ff60-4bf0-8533-ee8b683f5965" : {
        "id" : "02b4ba0a-ff60-4bf0-8533-ee8b683f5965",
        "id_categorie" : "categorie1",
        "id_repas" : "2fecc7f5-c4f5-44e7-9c4c-f34296ed5180",
        "name" : "Carottes",
        "quantite" : "6",
        "rank" : 2
      },
      "03bd6c31-4035-49fe-9b25-4048a41e452e" : {
        "id" : "03bd6c31-4035-49fe-9b25-4048a41e452e",
        "id_categorie" : "categorie12",
        "id_repas" : "13a6e228-b415-4365-9972-314f036d109c",
        "name" : "Sauce tomate panzani",
        "quantite" : "1",
        "rank" : 6
      },
      "04d5def9-2e86-4b9c-a946-5fe33e5447ab" : {
        "id" : "04d5def9-2e86-4b9c-a946-5fe33e5447ab",
        "id_categorie" : "categorie1",
        "id_repas" : "8fc288f8-54bd-42f7-9e26-6c7017e25faa",
        "name" : "Tomates",
        "quantite" : "2",
        "rank" : 0
      },
      "0da2d4a3-a29b-4eb6-bd06-4931d551ceae" : {
        "id" : "0da2d4a3-a29b-4eb6-bd06-4931d551ceae",
        "id_categorie" : "categorie4",
        "id_repas" : "c558c46b-c6ba-4b51-b43c-6caf92914f10",
        "name" : "Jambon ",
        "quantite" : "2 tranches ",
        "rank" : 3
      },
      "0e6c6255-db43-4bbe-9936-1e35474b58f4" : {
        "id" : "0e6c6255-db43-4bbe-9936-1e35474b58f4",
        "id_categorie" : "categorie1",
        "id_repas" : "25b4e9e2-2a92-43b1-8097-c02aca3e9425",
        "name" : "Courge butternut",
        "quantite" : "1",
        "rank" : 0
      },
      "0ed4b562-2e5e-4cfe-8e24-eb47fad428ed" : {
        "id" : "0ed4b562-2e5e-4cfe-8e24-eb47fad428ed",
        "id_categorie" : "categorie3",
        "id_repas" : "25b4e9e2-2a92-43b1-8097-c02aca3e9425",
        "name" : "Yaourt nature ",
        "quantite" : "1",
        "rank" : 7
      },
      "145c2672-f549-4df3-be50-8c47536f576a" : {
        "id" : "145c2672-f549-4df3-be50-8c47536f576a",
        "id_categorie" : "categorie1",
        "id_repas" : "90a01e21-4491-453b-b003-370e6722dc4f",
        "name" : "Poivron ",
        "quantite" : "1",
        "rank" : 0
      },
      "14ab3316-09c1-4347-ae23-59fa4a59eacf" : {
        "id" : "14ab3316-09c1-4347-ae23-59fa4a59eacf",
        "id_categorie" : "None",
        "id_repas" : "f82e4ed6-6404-4bc1-95b7-831a71e100cc",
        "name" : "Rhum negrita",
        "quantite" : "au jugé",
        "rank" : 0
      },
      "18770b72-aa4b-4192-8789-fcb410ed9696" : {
        "id" : "18770b72-aa4b-4192-8789-fcb410ed9696",
        "id_categorie" : "categorie5",
        "id_repas" : "3cd6f535-6bdd-48cf-9b1f-6d343347473a",
        "name" : "Crevettes",
        "quantite" : "1 boites",
        "rank" : 5
      },
      "18ce4126-cad8-49d2-b161-9713ab16ac66" : {
        "id" : "18ce4126-cad8-49d2-b161-9713ab16ac66",
        "id_categorie" : "None",
        "id_repas" : "7863c28f-5186-460d-95a6-f46fbfd1b6ba",
        "name" : "Sucre de canne",
        "quantite" : "10g",
        "rank" : 0
      },
      "197e50b8-648d-4fe3-878f-f8f045275cef" : {
        "id" : "197e50b8-648d-4fe3-878f-f8f045275cef",
        "id_categorie" : "categorie7",
        "id_repas" : "25b4e9e2-2a92-43b1-8097-c02aca3e9425",
        "name" : "Feuilles de brick",
        "quantite" : "10",
        "rank" : 4
      },
      "1bcc1b24-0b29-45cc-a7b2-11ce24624af3" : {
        "id" : "1bcc1b24-0b29-45cc-a7b2-11ce24624af3",
        "id_categorie" : "categorie19",
        "id_repas" : "ef1bd565-7bc7-4693-a1f1-54e24b2a99fd",
        "name" : "Gruyère ",
        "quantite" : "50g",
        "rank" : 3
      },
      "1caadd3c-c846-4076-9902-bbcc43643b31" : {
        "id" : "1caadd3c-c846-4076-9902-bbcc43643b31",
        "id_categorie" : "categorie12",
        "id_repas" : "218fd948-a38d-4bf5-a395-e94228c8e51d",
        "name" : "Sauce soja",
        "quantite" : "1",
        "rank" : 3
      },
      "1ce67a41-22ff-4826-81b9-0f5a29e4570a" : {
        "id" : "1ce67a41-22ff-4826-81b9-0f5a29e4570a",
        "id_categorie" : "categorie12",
        "id_repas" : "13a6e228-b415-4365-9972-314f036d109c",
        "name" : "Herbe de provence ",
        "quantite" : "1",
        "rank" : 8
      },
      "1ecc2cb7-4e0d-463e-975c-7f97d05a6afe" : {
        "id" : "1ecc2cb7-4e0d-463e-975c-7f97d05a6afe",
        "id_categorie" : "categorie1",
        "id_repas" : "da89093a-2b0e-4b27-b3d2-51acb43f92c8",
        "name" : "Salade",
        "quantite" : "1",
        "rank" : 1
      },
      "2387fb9f-4d20-4778-94b1-ef4c17386729" : {
        "id" : "2387fb9f-4d20-4778-94b1-ef4c17386729",
        "id_categorie" : "categorie1",
        "id_repas" : "acb9bef5-a446-4315-aa2f-ba10dcffc603",
        "name" : "Salade",
        "quantite" : "1",
        "rank" : 0
      },
      "25374a11-d08b-4dd8-8988-d38d462ecd7f" : {
        "id" : "25374a11-d08b-4dd8-8988-d38d462ecd7f",
        "id_categorie" : "categorie19",
        "id_repas" : "218fd948-a38d-4bf5-a395-e94228c8e51d",
        "name" : "Saint moret (optionnel)",
        "quantite" : "1",
        "rank" : 6
      },
      "259b2079-4e14-4e7c-8ade-b366cc268886" : {
        "id" : "259b2079-4e14-4e7c-8ade-b366cc268886",
        "id_categorie" : "categorie11",
        "id_repas" : "b51f01ef-8e7e-4731-8dce-02d527c1479f",
        "name" : "Chapelure",
        "quantite" : "1",
        "rank" : 0
      },
      "26f24fc0-fad5-401c-a149-8fc57ca85ac6" : {
        "id" : "26f24fc0-fad5-401c-a149-8fc57ca85ac6",
        "id_categorie" : "categorie11",
        "id_repas" : "2b3b7821-a351-4083-84e7-8891d95ffbc9",
        "name" : "Farine ",
        "quantite" : "20g",
        "rank" : 1
      },
      "296f4414-6e26-43dc-879a-47c5ec334888" : {
        "id" : "296f4414-6e26-43dc-879a-47c5ec334888",
        "id_categorie" : "categorie7",
        "id_repas" : "791978e3-839a-4bb6-b134-dcddf7200d99",
        "name" : "Semoule",
        "quantite" : "125",
        "rank" : 0
      },
      "2a38d26f-8b57-47fe-a794-f24e7263f3db" : {
        "id" : "2a38d26f-8b57-47fe-a794-f24e7263f3db",
        "id_categorie" : "categorie7",
        "id_repas" : "3cd6f535-6bdd-48cf-9b1f-6d343347473a",
        "name" : "Riz",
        "quantite" : "au jugé",
        "rank" : 0
      },
      "2e79b098-b8c5-4baa-8de4-786f39df8047" : {
        "id" : "2e79b098-b8c5-4baa-8de4-786f39df8047",
        "id_categorie" : "categorie1",
        "id_repas" : "b51f01ef-8e7e-4731-8dce-02d527c1479f",
        "name" : "Avocat",
        "quantite" : "1",
        "rank" : 1
      },
      "32b27964-f8db-4b1b-b162-d18ef3081f3a" : {
        "id" : "32b27964-f8db-4b1b-b162-d18ef3081f3a",
        "id_categorie" : "categorie12",
        "id_repas" : "2b3b7821-a351-4083-84e7-8891d95ffbc9",
        "name" : "Persil",
        "quantite" : "1",
        "rank" : 5
      },
      "37847370-6c84-4977-a34e-54bbe8d8c3e4" : {
        "id" : "37847370-6c84-4977-a34e-54bbe8d8c3e4",
        "id_categorie" : "categorie1",
        "id_repas" : "3cd6f535-6bdd-48cf-9b1f-6d343347473a",
        "name" : "Champignon",
        "quantite" : "4",
        "rank" : 2
      },
      "38a2458f-354f-42ff-a5c1-f905073dfe2a" : {
        "id" : "38a2458f-354f-42ff-a5c1-f905073dfe2a",
        "id_categorie" : "categorie3",
        "id_repas" : "ef1bd565-7bc7-4693-a1f1-54e24b2a99fd",
        "name" : "Crème fraîche (optionnel)",
        "quantite" : "10g",
        "rank" : 2
      },
      "38fb8877-9d02-48a3-8a99-e3c4f389da2e" : {
        "id" : "38fb8877-9d02-48a3-8a99-e3c4f389da2e",
        "id_categorie" : "None",
        "id_repas" : "7863c28f-5186-460d-95a6-f46fbfd1b6ba",
        "name" : "Limonade",
        "quantite" : "1",
        "rank" : 1
      },
      "3afac380-be93-40d6-9b76-39b179f1ed6a" : {
        "id" : "3afac380-be93-40d6-9b76-39b179f1ed6a",
        "id_categorie" : "categorie4",
        "id_repas" : "90a01e21-4491-453b-b003-370e6722dc4f",
        "name" : "Poulet (optionnel)",
        "quantite" : "120g",
        "rank" : 1
      },
      "3c14e243-ce7b-4eec-a549-fbc1bfcec323" : {
        "id" : "3c14e243-ce7b-4eec-a549-fbc1bfcec323",
        "id_categorie" : "categorie1",
        "id_repas" : "13a6e228-b415-4365-9972-314f036d109c",
        "name" : "Courgettes",
        "quantite" : "3",
        "rank" : 1
      },
      "3c791771-2558-4673-a06e-970cdb580c69" : {
        "id" : "3c791771-2558-4673-a06e-970cdb580c69",
        "id_categorie" : "categorie1",
        "id_repas" : "7d806748-f666-4db6-bcad-2f81fdec5374",
        "name" : "Tomates",
        "quantite" : "2",
        "rank" : 1
      },
      "422f9857-97b2-437c-baf6-cf28a03f449f" : {
        "id" : "422f9857-97b2-437c-baf6-cf28a03f449f",
        "id_categorie" : "categorie7",
        "id_repas" : "c558c46b-c6ba-4b51-b43c-6caf92914f10",
        "name" : "Polenta (optionnel)",
        "quantite" : "120g",
        "rank" : 4
      },
      "43b104ae-83b0-4540-8389-5687bf7788f1" : {
        "id" : "43b104ae-83b0-4540-8389-5687bf7788f1",
        "id_categorie" : "categorie3",
        "id_repas" : "c558c46b-c6ba-4b51-b43c-6caf92914f10",
        "name" : "Kiri",
        "quantite" : "1",
        "rank" : 1
      },
      "459ab8f4-53e4-433c-a456-cb59129ed62e" : {
        "id" : "459ab8f4-53e4-433c-a456-cb59129ed62e",
        "id_categorie" : "categorie12",
        "id_repas" : "acb9bef5-a446-4315-aa2f-ba10dcffc603",
        "name" : "Sauce burger",
        "quantite" : "1",
        "rank" : 1
      },
      "46f269ce-9f66-4c36-b8e0-0cbf5414f371" : {
        "id" : "46f269ce-9f66-4c36-b8e0-0cbf5414f371",
        "id_categorie" : "categorie7",
        "id_repas" : "c558c46b-c6ba-4b51-b43c-6caf92914f10",
        "name" : "Purée mousseline (optionnel)",
        "quantite" : "120g",
        "rank" : 5
      },
      "46f33d6b-7c42-4432-aa15-45bd25744bd0" : {
        "id" : "46f33d6b-7c42-4432-aa15-45bd25744bd0",
        "id_categorie" : "categorie4",
        "id_repas" : "acb9bef5-a446-4315-aa2f-ba10dcffc603",
        "name" : "Viande hachée",
        "quantite" : "250 g",
        "rank" : 3
      },
      "49a22edf-91c0-4b12-b189-9ba965c56284" : {
        "id" : "49a22edf-91c0-4b12-b189-9ba965c56284",
        "id_categorie" : "categorie4",
        "id_repas" : "2d5b555f-043b-4c73-8955-aed99046b841",
        "name" : "Cervela herta",
        "quantite" : "1",
        "rank" : 2
      },
      "49e64ce5-a8ca-4a46-b6db-6915668e2b3d" : {
        "id" : "49e64ce5-a8ca-4a46-b6db-6915668e2b3d",
        "id_categorie" : "categorie12",
        "id_repas" : "25b4e9e2-2a92-43b1-8097-c02aca3e9425",
        "name" : "Épices curry",
        "quantite" : "1",
        "rank" : 5
      },
      "5150577e-1966-4bae-b692-21edd7004e08" : {
        "id" : "5150577e-1966-4bae-b692-21edd7004e08",
        "id_categorie" : "categorie19",
        "id_repas" : "8fc288f8-54bd-42f7-9e26-6c7017e25faa",
        "name" : "Mozzarella",
        "quantite" : "1",
        "rank" : 1
      },
      "56540076-ec6f-44c0-a668-f71e3c157bd2" : {
        "id" : "56540076-ec6f-44c0-a668-f71e3c157bd2",
        "id_categorie" : "categorie19",
        "id_repas" : "df0dcbc9-433a-4c6c-9b09-03d47b7b810e",
        "name" : "Roquefort (optionnel)",
        "quantite" : "1",
        "rank" : 6
      },
      "56d51f8a-5625-47b0-b272-5f87bbe361a3" : {
        "id" : "56d51f8a-5625-47b0-b272-5f87bbe361a3",
        "id_categorie" : "categorie1",
        "id_repas" : "da89093a-2b0e-4b27-b3d2-51acb43f92c8",
        "name" : "Tomates",
        "quantite" : "1",
        "rank" : 2
      },
      "57472d2d-c50d-4e13-bca1-802908972c43" : {
        "id" : "57472d2d-c50d-4e13-bca1-802908972c43",
        "id_categorie" : "None",
        "id_repas" : "f82e4ed6-6404-4bc1-95b7-831a71e100cc",
        "name" : "Farine",
        "quantite" : "200g",
        "rank" : 0
      },
      "597d134f-1e03-4533-b103-fbd24d0ed87d" : {
        "id" : "597d134f-1e03-4533-b103-fbd24d0ed87d",
        "id_categorie" : "categorie1",
        "id_repas" : "13a6e228-b415-4365-9972-314f036d109c",
        "name" : "Poivrons ",
        "quantite" : "2",
        "rank" : 2
      },
      "5a316b4a-c794-4105-a8e7-514d60cdc957" : {
        "id" : "5a316b4a-c794-4105-a8e7-514d60cdc957",
        "id_categorie" : "categorie12",
        "id_repas" : "2d5b555f-043b-4c73-8955-aed99046b841",
        "name" : "Huile",
        "quantite" : "au jugé",
        "rank" : 4
      },
      "5bd03959-5e73-454c-b8af-2c44aa10d4f7" : {
        "id" : "5bd03959-5e73-454c-b8af-2c44aa10d4f7",
        "id_categorie" : "categorie1",
        "id_repas" : "90a01e21-4491-453b-b003-370e6722dc4f",
        "name" : "Brocoli",
        "quantite" : "1",
        "rank" : 2
      },
      "5bfb0f95-f3c2-41bb-aee4-18cbdec2f1e5" : {
        "id" : "5bfb0f95-f3c2-41bb-aee4-18cbdec2f1e5",
        "id_categorie" : "categorie1",
        "id_repas" : "861728ce-f15d-42da-ba6e-a92d461c81fd",
        "name" : "Courgettes ",
        "quantite" : "1",
        "rank" : 2
      },
      "5ea73f3d-c6c3-4727-8174-b69c750cfa0c" : {
        "id" : "5ea73f3d-c6c3-4727-8174-b69c750cfa0c",
        "id_categorie" : "None",
        "id_repas" : "14d4c8fe-7a8e-4bb8-8cbd-64ab29d178aa",
        "name" : "Levure",
        "quantite" : "1/2 sachet",
        "rank" : 5
      },
      "5f1b88ad-c104-4e49-b805-1a0f7dc2fb79" : {
        "id" : "5f1b88ad-c104-4e49-b805-1a0f7dc2fb79",
        "id_categorie" : "categorie1",
        "id_repas" : "2d5b555f-043b-4c73-8955-aed99046b841",
        "name" : "Tomates",
        "quantite" : "au jugé",
        "rank" : 1
      },
      "60245a07-4a01-45c0-8096-dc36682cb957" : {
        "id" : "60245a07-4a01-45c0-8096-dc36682cb957",
        "id_categorie" : "categorie12",
        "id_repas" : "90a01e21-4491-453b-b003-370e6722dc4f",
        "name" : "Sauce soja",
        "quantite" : "1",
        "rank" : 3
      },
      "62bba37d-4c1f-4193-8136-696322d17f6c" : {
        "id" : "62bba37d-4c1f-4193-8136-696322d17f6c",
        "id_categorie" : "categorie1",
        "id_repas" : "2b3b7821-a351-4083-84e7-8891d95ffbc9",
        "name" : "Brocoli ",
        "quantite" : "1",
        "rank" : 0
      },
      "631ecda6-ef94-49af-a6e4-df3595d30d9a" : {
        "id" : "631ecda6-ef94-49af-a6e4-df3595d30d9a",
        "id_categorie" : "categorie7",
        "id_repas" : "2d5b555f-043b-4c73-8955-aed99046b841",
        "name" : "Coquillettes ",
        "quantite" : "au jugé",
        "rank" : 0
      },
      "6335d25e-7567-4db7-9173-b2c94e1ab6ab" : {
        "id" : "6335d25e-7567-4db7-9173-b2c94e1ab6ab",
        "id_categorie" : "categorie12",
        "id_repas" : "da89093a-2b0e-4b27-b3d2-51acb43f92c8",
        "name" : "Miel",
        "quantite" : "1",
        "rank" : 4
      },
      "65e148a0-44f7-4b94-af98-5513aadbe851" : {
        "id" : "65e148a0-44f7-4b94-af98-5513aadbe851",
        "id_categorie" : "categorie1",
        "id_repas" : "b51f01ef-8e7e-4731-8dce-02d527c1479f",
        "name" : "Tomates",
        "quantite" : "1",
        "rank" : 2
      },
      "6753e317-1e2b-44cb-8d6b-4908d2749ae5" : {
        "id" : "6753e317-1e2b-44cb-8d6b-4908d2749ae5",
        "id_categorie" : "categorie1",
        "id_repas" : "25b4e9e2-2a92-43b1-8097-c02aca3e9425",
        "name" : "Citron",
        "quantite" : "1",
        "rank" : 9
      },
      "68001d7c-ea2f-48f1-b439-379b8dda325e" : {
        "id" : "68001d7c-ea2f-48f1-b439-379b8dda325e",
        "id_categorie" : "categorie21",
        "id_repas" : "acb9bef5-a446-4315-aa2f-ba10dcffc603",
        "name" : "Pains buns",
        "quantite" : "4",
        "rank" : 5
      },
      "6933f1f4-2b66-4cec-af93-dc9a681dd8e2" : {
        "id" : "6933f1f4-2b66-4cec-af93-dc9a681dd8e2",
        "id_categorie" : "categorie7",
        "id_repas" : "861728ce-f15d-42da-ba6e-a92d461c81fd",
        "name" : "Pâte brisée ",
        "quantite" : "1",
        "rank" : 0
      },
      "6b4a7e96-079f-434a-90cc-19ccd44b2751" : {
        "id" : "6b4a7e96-079f-434a-90cc-19ccd44b2751",
        "id_categorie" : "None",
        "id_repas" : "7863c28f-5186-460d-95a6-f46fbfd1b6ba",
        "name" : "Citron vert",
        "quantite" : "2",
        "rank" : 2
      },
      "6e24fb4f-cfd4-494f-88c7-e825564d50a1" : {
        "id" : "6e24fb4f-cfd4-494f-88c7-e825564d50a1",
        "id_categorie" : "categorie19",
        "id_repas" : "df0dcbc9-433a-4c6c-9b09-03d47b7b810e",
        "name" : "Gruyère ",
        "quantite" : "10g",
        "rank" : 4
      },
      "71658a91-b690-4eec-bfe1-3f9e1263dec4" : {
        "id" : "71658a91-b690-4eec-bfe1-3f9e1263dec4",
        "id_categorie" : "None",
        "id_repas" : "7863c28f-5186-460d-95a6-f46fbfd1b6ba",
        "name" : "Sirop de canne (optionnel)",
        "quantite" : "1",
        "rank" : 3
      },
      "720cd85f-74e5-4bed-9f9d-47ff246634b4" : {
        "id" : "720cd85f-74e5-4bed-9f9d-47ff246634b4",
        "id_categorie" : "categorie7",
        "id_repas" : "13a6e228-b415-4365-9972-314f036d109c",
        "name" : "Riz ",
        "quantite" : "100g",
        "rank" : 5
      },
      "735da47c-2052-4a8c-a4ad-0ef417d28778" : {
        "id" : "735da47c-2052-4a8c-a4ad-0ef417d28778",
        "id_categorie" : "None",
        "id_repas" : "f82e4ed6-6404-4bc1-95b7-831a71e100cc",
        "name" : "Lait",
        "quantite" : "40cl",
        "rank" : 0
      },
      "7495e278-4ec1-4455-b78f-5a7fdab33e71" : {
        "id" : "7495e278-4ec1-4455-b78f-5a7fdab33e71",
        "id_categorie" : "categorie6",
        "id_repas" : "90a01e21-4491-453b-b003-370e6722dc4f",
        "name" : "Seitan (optionnel)",
        "quantite" : "1",
        "rank" : 4
      },
      "77d4b02f-ccf5-49d3-a045-440df8d2e095" : {
        "id" : "77d4b02f-ccf5-49d3-a045-440df8d2e095",
        "id_categorie" : "categorie21",
        "id_repas" : "da89093a-2b0e-4b27-b3d2-51acb43f92c8",
        "name" : "Pain",
        "quantite" : "4",
        "rank" : 0
      },
      "79e289fd-9b40-4927-87b9-c2a231242d04" : {
        "id" : "79e289fd-9b40-4927-87b9-c2a231242d04",
        "id_categorie" : "categorie1",
        "id_repas" : "90a01e21-4491-453b-b003-370e6722dc4f",
        "name" : "Carottes",
        "quantite" : "2",
        "rank" : 5
      },
      "7a043d8a-8cb2-475b-a9f8-2a974fd4c435" : {
        "id" : "7a043d8a-8cb2-475b-a9f8-2a974fd4c435",
        "id_categorie" : "categorie21",
        "id_repas" : "ef1bd565-7bc7-4693-a1f1-54e24b2a99fd",
        "name" : "Pain de mie ",
        "quantite" : "4 tranches",
        "rank" : 0
      },
      "7bd50eb8-b9e8-43f9-9a80-9cf537eef9f4" : {
        "id" : "7bd50eb8-b9e8-43f9-9a80-9cf537eef9f4",
        "id_categorie" : "categorie12",
        "id_repas" : "25b4e9e2-2a92-43b1-8097-c02aca3e9425",
        "name" : "Persil",
        "quantite" : "1 botte",
        "rank" : 8
      },
      "7c9cacb0-b53b-4b5c-94ff-c69f17b90adf" : {
        "id" : "7c9cacb0-b53b-4b5c-94ff-c69f17b90adf",
        "id_categorie" : "categorie1",
        "id_repas" : "acb9bef5-a446-4315-aa2f-ba10dcffc603",
        "name" : "Tomates",
        "quantite" : "2",
        "rank" : 4
      },
      "8092bce2-04fe-4287-88a3-ced8cb0a1828" : {
        "id" : "8092bce2-04fe-4287-88a3-ced8cb0a1828",
        "id_categorie" : "categorie12",
        "id_repas" : "25b4e9e2-2a92-43b1-8097-c02aca3e9425",
        "name" : "Paprika ",
        "quantite" : "1",
        "rank" : 6
      },
      "809e812e-6364-46da-8a76-7e732b058865" : {
        "id" : "809e812e-6364-46da-8a76-7e732b058865",
        "id_categorie" : "categorie1",
        "id_repas" : "3cd6f535-6bdd-48cf-9b1f-6d343347473a",
        "name" : "Petites tomates",
        "quantite" : "1",
        "rank" : 3
      },
      "81432730-5c35-4558-a0d6-ac7b89ff9b07" : {
        "id" : "81432730-5c35-4558-a0d6-ac7b89ff9b07",
        "id_categorie" : "categorie11",
        "id_repas" : "b51f01ef-8e7e-4731-8dce-02d527c1479f",
        "name" : "Farine",
        "quantite" : "50g",
        "rank" : 3
      },
      "81c4acd0-5a8c-465c-9ed0-854842d24976" : {
        "id" : "81c4acd0-5a8c-465c-9ed0-854842d24976",
        "id_categorie" : "categorie1",
        "id_repas" : "90a01e21-4491-453b-b003-370e6722dc4f",
        "name" : "Pousse de soja (optionnel)",
        "quantite" : "1",
        "rank" : 6
      },
      "83aa5eac-a678-4709-bdd1-2ffa36f2d987" : {
        "id" : "83aa5eac-a678-4709-bdd1-2ffa36f2d987",
        "id_categorie" : "None",
        "id_repas" : "14d4c8fe-7a8e-4bb8-8cbd-64ab29d178aa",
        "name" : "Beurre",
        "quantite" : "150g",
        "rank" : 2
      },
      "8569d176-d466-4b38-bed8-e2b6e12b7d75" : {
        "id" : "8569d176-d466-4b38-bed8-e2b6e12b7d75",
        "id_categorie" : "categorie12",
        "id_repas" : "2d5b555f-043b-4c73-8955-aed99046b841",
        "name" : "Vinaigre",
        "quantite" : "au jugé",
        "rank" : 5
      },
      "863626e9-5352-435c-91bd-ad0924257328" : {
        "id" : "863626e9-5352-435c-91bd-ad0924257328",
        "id_categorie" : "categorie21",
        "id_repas" : "df0dcbc9-433a-4c6c-9b09-03d47b7b810e",
        "name" : "Pain de mie complet",
        "quantite" : "4 tranches ",
        "rank" : 0
      },
      "869c4496-9653-4548-a4ea-45ba34317929" : {
        "id" : "869c4496-9653-4548-a4ea-45ba34317929",
        "id_categorie" : "categorie7",
        "id_repas" : "90a01e21-4491-453b-b003-370e6722dc4f",
        "name" : "Nouilles chinoises",
        "quantite" : "1",
        "rank" : 7
      },
      "887743e2-61fd-4752-92b2-f5fdd71f3ba3" : {
        "id" : "887743e2-61fd-4752-92b2-f5fdd71f3ba3",
        "id_categorie" : "categorie3",
        "id_repas" : "3cd6f535-6bdd-48cf-9b1f-6d343347473a",
        "name" : "Crème fraîche",
        "quantite" : "1 petit pot",
        "rank" : 1
      },
      "8fb29c1d-3942-4339-a3e6-e1df46d4568f" : {
        "id" : "8fb29c1d-3942-4339-a3e6-e1df46d4568f",
        "id_categorie" : "categorie1",
        "id_repas" : "13a6e228-b415-4365-9972-314f036d109c",
        "name" : "Champignons",
        "quantite" : "4",
        "rank" : 4
      },
      "9020d05e-1a46-4800-8ca0-68fbba202130" : {
        "id" : "9020d05e-1a46-4800-8ca0-68fbba202130",
        "id_categorie" : "categorie22",
        "id_repas" : "e909b217-b4c7-4bd8-a3ce-3ed7cf0d539f",
        "name" : "Raviole",
        "quantite" : "1 sachet",
        "rank" : 0
      },
      "912313d6-a918-4a7b-9214-a1f8d93701fb" : {
        "id" : "912313d6-a918-4a7b-9214-a1f8d93701fb",
        "id_categorie" : "categorie1",
        "id_repas" : "b51f01ef-8e7e-4731-8dce-02d527c1479f",
        "name" : "Salade",
        "quantite" : "1",
        "rank" : 4
      },
      "916ac6e9-eba5-4cbf-bfcb-6f47e378f2c7" : {
        "id" : "916ac6e9-eba5-4cbf-bfcb-6f47e378f2c7",
        "id_categorie" : "categorie3",
        "id_repas" : "861728ce-f15d-42da-ba6e-a92d461c81fd",
        "name" : "Fromage de chèvre (optionnel)",
        "quantite" : "1",
        "rank" : 4
      },
      "928031f5-8a69-46e6-8f09-c9d118025301" : {
        "id" : "928031f5-8a69-46e6-8f09-c9d118025301",
        "id_categorie" : "categorie19",
        "id_repas" : "2b3b7821-a351-4083-84e7-8891d95ffbc9",
        "name" : "Parmesan",
        "quantite" : "10g",
        "rank" : 2
      },
      "93626aa0-a3b0-47cf-94df-2e52bdd1447d" : {
        "id" : "93626aa0-a3b0-47cf-94df-2e52bdd1447d",
        "id_categorie" : "None",
        "id_repas" : "f82e4ed6-6404-4bc1-95b7-831a71e100cc",
        "name" : "Œufs",
        "quantite" : "2",
        "rank" : 0
      },
      "948314b8-8750-4f36-a026-d71cee4196f8" : {
        "id" : "948314b8-8750-4f36-a026-d71cee4196f8",
        "id_categorie" : "categorie19",
        "id_repas" : "da89093a-2b0e-4b27-b3d2-51acb43f92c8",
        "name" : "Bûche de chèvre",
        "quantite" : "1",
        "rank" : 3
      },
      "94ef06f0-5a52-41a7-82aa-e4cf89d10436" : {
        "id" : "94ef06f0-5a52-41a7-82aa-e4cf89d10436",
        "id_categorie" : "categorie3",
        "id_repas" : "c558c46b-c6ba-4b51-b43c-6caf92914f10",
        "name" : "Lait ",
        "quantite" : "200ml",
        "rank" : 0
      },
      "96f24d1d-7aa5-448e-a276-e896e271fdec" : {
        "id" : "96f24d1d-7aa5-448e-a276-e896e271fdec",
        "id_categorie" : "categorie7",
        "id_repas" : "7d806748-f666-4db6-bcad-2f81fdec5374",
        "name" : "Semoule épicé",
        "quantite" : "150 g",
        "rank" : 0
      },
      "a099d632-0023-4b1f-9341-57840fd905cc" : {
        "id" : "a099d632-0023-4b1f-9341-57840fd905cc",
        "id_categorie" : "categorie12",
        "id_repas" : "90a01e21-4491-453b-b003-370e6722dc4f",
        "name" : "Sirop d'érable (optionnel)",
        "quantite" : "1",
        "rank" : 8
      },
      "a4b740ec-ff25-4c66-919f-7fcec84af903" : {
        "id" : "a4b740ec-ff25-4c66-919f-7fcec84af903",
        "id_categorie" : "categorie12",
        "id_repas" : "861728ce-f15d-42da-ba6e-a92d461c81fd",
        "name" : "Herbe de provence ",
        "quantite" : "1",
        "rank" : 5
      },
      "a8a5d28d-199e-425b-95e7-7a780998f219" : {
        "id" : "a8a5d28d-199e-425b-95e7-7a780998f219",
        "id_categorie" : "categorie12",
        "id_repas" : "2d5b555f-043b-4c73-8955-aed99046b841",
        "name" : "Mayonnaise",
        "quantite" : "au jugé",
        "rank" : 6
      },
      "a92ca291-60e8-4399-b1c3-d29506bec122" : {
        "id" : "a92ca291-60e8-4399-b1c3-d29506bec122",
        "id_categorie" : "categorie1",
        "id_repas" : "b51f01ef-8e7e-4731-8dce-02d527c1479f",
        "name" : "Maïs",
        "quantite" : "1",
        "rank" : 5
      },
      "a95232c1-b350-442e-a8c7-3e575f113619" : {
        "id" : "a95232c1-b350-442e-a8c7-3e575f113619",
        "id_categorie" : "categorie8",
        "id_repas" : "c558c46b-c6ba-4b51-b43c-6caf92914f10",
        "name" : "Maïs ",
        "quantite" : "1",
        "rank" : 2
      },
      "aa8a4dff-c6b1-4766-b74c-098b1cf97ed6" : {
        "id" : "aa8a4dff-c6b1-4766-b74c-098b1cf97ed6",
        "id_categorie" : "categorie12",
        "id_repas" : "2b3b7821-a351-4083-84e7-8891d95ffbc9",
        "name" : "Ail (optionnel)",
        "quantite" : "1",
        "rank" : 4
      },
      "ae8fa9d9-6bee-46ba-97f1-769fb3818807" : {
        "id" : "ae8fa9d9-6bee-46ba-97f1-769fb3818807",
        "id_categorie" : "categorie6",
        "id_repas" : "861728ce-f15d-42da-ba6e-a92d461c81fd",
        "name" : "Seitan",
        "quantite" : "50g",
        "rank" : 7
      },
      "b0bfaf2d-e8c5-49b3-9f65-c95a797931c2" : {
        "id" : "b0bfaf2d-e8c5-49b3-9f65-c95a797931c2",
        "id_categorie" : "None",
        "id_repas" : "f82e4ed6-6404-4bc1-95b7-831a71e100cc",
        "name" : "Beurre",
        "quantite" : "40g",
        "rank" : 0
      },
      "b2a806aa-9e2c-4b20-b0b3-20043eb303bb" : {
        "id" : "b2a806aa-9e2c-4b20-b0b3-20043eb303bb",
        "id_categorie" : "None",
        "id_repas" : "14d4c8fe-7a8e-4bb8-8cbd-64ab29d178aa",
        "name" : "Zeste de citron",
        "quantite" : "au jugé",
        "rank" : 6
      },
      "b33b4e66-0d80-41e8-8b48-17ed25240431" : {
        "id" : "b33b4e66-0d80-41e8-8b48-17ed25240431",
        "id_categorie" : "None",
        "id_repas" : "14d4c8fe-7a8e-4bb8-8cbd-64ab29d178aa",
        "name" : "Sucre",
        "quantite" : "110g",
        "rank" : 3
      },
      "b47694bc-7217-45c4-9ca6-0c28da04c209" : {
        "id" : "b47694bc-7217-45c4-9ca6-0c28da04c209",
        "id_categorie" : "categorie1",
        "id_repas" : "7d806748-f666-4db6-bcad-2f81fdec5374",
        "name" : "Concombre",
        "quantite" : "1",
        "rank" : 2
      },
      "b55d6ce1-a87d-4582-9c61-c7bc3d29085d" : {
        "id" : "b55d6ce1-a87d-4582-9c61-c7bc3d29085d",
        "id_categorie" : "categorie12",
        "id_repas" : "df0dcbc9-433a-4c6c-9b09-03d47b7b810e",
        "name" : "Pesto (vert ou rouge)",
        "quantite" : "1",
        "rank" : 3
      },
      "b821e02d-4049-4c04-bfd8-c83c34b11f7d" : {
        "id" : "b821e02d-4049-4c04-bfd8-c83c34b11f7d",
        "id_categorie" : "categorie8",
        "id_repas" : "791978e3-839a-4bb6-b134-dcddf7200d99",
        "name" : "Lentilles",
        "quantite" : "1",
        "rank" : 1
      },
      "b8b40ade-1136-4c14-94d5-5d1689d94377" : {
        "id" : "b8b40ade-1136-4c14-94d5-5d1689d94377",
        "id_categorie" : "categorie4",
        "id_repas" : "b51f01ef-8e7e-4731-8dce-02d527c1479f",
        "name" : "Oeuf",
        "quantite" : "2",
        "rank" : 6
      },
      "b912af1b-8d00-4ab4-9cf5-c606a9b6e738" : {
        "id" : "b912af1b-8d00-4ab4-9cf5-c606a9b6e738",
        "id_categorie" : "categorie1",
        "id_repas" : "861728ce-f15d-42da-ba6e-a92d461c81fd",
        "name" : "Aubergine (optionnel)",
        "quantite" : "1",
        "rank" : 6
      },
      "bbd22b44-ca3a-457a-9e8f-277384f54d2d" : {
        "id" : "bbd22b44-ca3a-457a-9e8f-277384f54d2d",
        "id_categorie" : "categorie4",
        "id_repas" : "ef1bd565-7bc7-4693-a1f1-54e24b2a99fd",
        "name" : "Jambon",
        "quantite" : "2 tranches",
        "rank" : 1
      },
      "bc5e3ce2-817a-4f61-a6b1-7caecf97217d" : {
        "id" : "bc5e3ce2-817a-4f61-a6b1-7caecf97217d",
        "id_categorie" : "categorie13",
        "id_repas" : "7863c28f-5186-460d-95a6-f46fbfd1b6ba",
        "name" : "Rhum blanc",
        "quantite" : "1",
        "rank" : 4
      },
      "bc95fa45-870c-4a9b-a8ed-d977129b13c1" : {
        "id" : "bc95fa45-870c-4a9b-a8ed-d977129b13c1",
        "id_categorie" : "categorie7",
        "id_repas" : "2fecc7f5-c4f5-44e7-9c4c-f34296ed5180",
        "name" : "Feuilles de brick",
        "quantite" : "10",
        "rank" : 0
      },
      "c12d154c-05c7-4f24-83c5-f996d0129ff1" : {
        "id" : "c12d154c-05c7-4f24-83c5-f996d0129ff1",
        "id_categorie" : "categorie3",
        "id_repas" : "25b4e9e2-2a92-43b1-8097-c02aca3e9425",
        "name" : "Roquefort ",
        "quantite" : "1/2",
        "rank" : 1
      },
      "c94400f8-64cd-4f08-b64c-d52f2de80e70" : {
        "id" : "c94400f8-64cd-4f08-b64c-d52f2de80e70",
        "id_categorie" : "categorie4",
        "id_repas" : "2b3b7821-a351-4083-84e7-8891d95ffbc9",
        "name" : "Oeuf ",
        "quantite" : "1",
        "rank" : 3
      },
      "cae8c59b-5d83-48ba-a7ce-27668603e981" : {
        "id" : "cae8c59b-5d83-48ba-a7ce-27668603e981",
        "id_categorie" : "None",
        "id_repas" : "7863c28f-5186-460d-95a6-f46fbfd1b6ba",
        "name" : "Menthe",
        "quantite" : "1",
        "rank" : 5
      },
      "cd5e1459-aae4-4c3f-bc55-01f2660f143b" : {
        "id" : "cd5e1459-aae4-4c3f-bc55-01f2660f143b",
        "id_categorie" : "None",
        "id_repas" : "14d4c8fe-7a8e-4bb8-8cbd-64ab29d178aa",
        "name" : "Farine",
        "quantite" : "180g",
        "rank" : 4
      },
      "cd6e9535-8e63-4617-9b6e-45a6456a0205" : {
        "id" : "cd6e9535-8e63-4617-9b6e-45a6456a0205",
        "id_categorie" : "categorie19",
        "id_repas" : "df0dcbc9-433a-4c6c-9b09-03d47b7b810e",
        "name" : "Avocat (optionnel)",
        "quantite" : "1",
        "rank" : 7
      },
      "cdd1f87d-115c-4d3d-ac8a-d1d4013ccecb" : {
        "id" : "cdd1f87d-115c-4d3d-ac8a-d1d4013ccecb",
        "id_categorie" : "categorie12",
        "id_repas" : "861728ce-f15d-42da-ba6e-a92d461c81fd",
        "name" : "Sauce tomate (optionnel)",
        "quantite" : "1",
        "rank" : 3
      },
      "ce204e94-5ed9-4ac7-bd9c-2bc93058e63e" : {
        "id" : "ce204e94-5ed9-4ac7-bd9c-2bc93058e63e",
        "id_categorie" : "categorie4",
        "id_repas" : "2fecc7f5-c4f5-44e7-9c4c-f34296ed5180",
        "name" : "Poulet",
        "quantite" : "200g",
        "rank" : 1
      },
      "d165e03e-a8c7-49c3-a1db-0592f26d674d" : {
        "id" : "d165e03e-a8c7-49c3-a1db-0592f26d674d",
        "id_categorie" : "categorie7",
        "id_repas" : "218fd948-a38d-4bf5-a395-e94228c8e51d",
        "name" : "Riz",
        "quantite" : "150g",
        "rank" : 0
      },
      "d1780a4e-65ea-482d-84d5-24bc30ec159b" : {
        "id" : "d1780a4e-65ea-482d-84d5-24bc30ec159b",
        "id_categorie" : "None",
        "id_repas" : "f82e4ed6-6404-4bc1-95b7-831a71e100cc",
        "name" : "Sucre",
        "quantite" : "40g",
        "rank" : 0
      },
      "d4e31247-6562-455b-939e-f6490de3d355" : {
        "id" : "d4e31247-6562-455b-939e-f6490de3d355",
        "id_categorie" : "categorie3",
        "id_repas" : "acb9bef5-a446-4315-aa2f-ba10dcffc603",
        "name" : "Cheddar",
        "quantite" : "1 sachet",
        "rank" : 2
      },
      "d82bea41-deb7-45d1-9148-83dfbdb01fc0" : {
        "id" : "d82bea41-deb7-45d1-9148-83dfbdb01fc0",
        "id_categorie" : "categorie11",
        "id_repas" : "791978e3-839a-4bb6-b134-dcddf7200d99",
        "name" : "Raisins secs (optionnel)",
        "quantite" : "25g",
        "rank" : 2
      },
      "d8faefb4-69ce-499d-9615-1678f25f72aa" : {
        "id" : "d8faefb4-69ce-499d-9615-1678f25f72aa",
        "id_categorie" : "categorie1",
        "id_repas" : "25b4e9e2-2a92-43b1-8097-c02aca3e9425",
        "name" : "Oignon ",
        "quantite" : "1",
        "rank" : 2
      },
      "d92250bf-e6ce-4ac0-8f19-716a0be820a6" : {
        "id" : "d92250bf-e6ce-4ac0-8f19-716a0be820a6",
        "id_categorie" : "categorie21",
        "id_repas" : "b51f01ef-8e7e-4731-8dce-02d527c1479f",
        "name" : "Wraps",
        "quantite" : "4",
        "rank" : 7
      },
      "dad8bb53-379c-4314-a908-62310cf8ca9f" : {
        "id" : "dad8bb53-379c-4314-a908-62310cf8ca9f",
        "id_categorie" : "categorie1",
        "id_repas" : "13a6e228-b415-4365-9972-314f036d109c",
        "name" : "Aubergine (optionnel)",
        "quantite" : "1",
        "rank" : 7
      },
      "dccc7f99-ee45-4165-877c-f348e6828423" : {
        "id" : "dccc7f99-ee45-4165-877c-f348e6828423",
        "id_categorie" : "categorie12",
        "id_repas" : "218fd948-a38d-4bf5-a395-e94228c8e51d",
        "name" : "Algues (optionnel)",
        "quantite" : "1",
        "rank" : 5
      },
      "de1df997-0367-4f83-a913-9eec29badacf" : {
        "id" : "de1df997-0367-4f83-a913-9eec29badacf",
        "id_categorie" : "categorie1",
        "id_repas" : "df0dcbc9-433a-4c6c-9b09-03d47b7b810e",
        "name" : "Tomates",
        "quantite" : "1",
        "rank" : 1
      },
      "e00d11cf-6c9c-488c-8d2e-a4bbe5c68655" : {
        "id" : "e00d11cf-6c9c-488c-8d2e-a4bbe5c68655",
        "id_categorie" : "categorie12",
        "id_repas" : "3cd6f535-6bdd-48cf-9b1f-6d343347473a",
        "name" : "Cube kubor",
        "quantite" : "1",
        "rank" : 4
      },
      "e0aa23f8-c55d-4ece-b206-dc16f243cedb" : {
        "id" : "e0aa23f8-c55d-4ece-b206-dc16f243cedb",
        "id_categorie" : "None",
        "id_repas" : "14d4c8fe-7a8e-4bb8-8cbd-64ab29d178aa",
        "name" : "Œufs",
        "quantite" : "3",
        "rank" : 1
      },
      "e9837318-19ce-4702-bf1b-d5e39ce33b90" : {
        "id" : "e9837318-19ce-4702-bf1b-d5e39ce33b90",
        "id_categorie" : "categorie1",
        "id_repas" : "90a01e21-4491-453b-b003-370e6722dc4f",
        "name" : "Champignon",
        "quantite" : "4",
        "rank" : 9
      },
      "ea356d09-18be-4a8d-b0dd-2ea39e3cafd0" : {
        "id" : "ea356d09-18be-4a8d-b0dd-2ea39e3cafd0",
        "id_categorie" : "categorie1",
        "id_repas" : "df0dcbc9-433a-4c6c-9b09-03d47b7b810e",
        "name" : "Salade ",
        "quantite" : "1",
        "rank" : 2
      },
      "ec767b06-4633-4f26-b5be-407547531ed2" : {
        "id" : "ec767b06-4633-4f26-b5be-407547531ed2",
        "id_categorie" : "categorie1",
        "id_repas" : "2d5b555f-043b-4c73-8955-aed99046b841",
        "name" : "Concombre",
        "quantite" : "1",
        "rank" : 3
      },
      "f0f77d96-9678-4e2d-9d8a-207b3cefdbf1" : {
        "id" : "f0f77d96-9678-4e2d-9d8a-207b3cefdbf1",
        "id_categorie" : "categorie1",
        "id_repas" : "218fd948-a38d-4bf5-a395-e94228c8e51d",
        "name" : "Avocat",
        "quantite" : "1",
        "rank" : 2
      },
      "f51607ea-0b09-4cb3-84a0-62749a2c141a" : {
        "id" : "f51607ea-0b09-4cb3-84a0-62749a2c141a",
        "id_categorie" : "categorie5",
        "id_repas" : "218fd948-a38d-4bf5-a395-e94228c8e51d",
        "name" : "Saumon",
        "quantite" : "2 pavés",
        "rank" : 1
      },
      "f578beb3-2b6b-4c87-9452-0760a1e35526" : {
        "id" : "f578beb3-2b6b-4c87-9452-0760a1e35526",
        "id_categorie" : "categorie1",
        "id_repas" : "13a6e228-b415-4365-9972-314f036d109c",
        "name" : "Tomates ",
        "quantite" : "4",
        "rank" : 3
      },
      "f5b86a0a-0b7e-46aa-818e-9ec2a780a903" : {
        "id" : "f5b86a0a-0b7e-46aa-818e-9ec2a780a903",
        "id_categorie" : "categorie1",
        "id_repas" : "861728ce-f15d-42da-ba6e-a92d461c81fd",
        "name" : "Tomates ",
        "quantite" : "1",
        "rank" : 1
      },
      "f7b485e7-830b-4826-9652-562c87972068" : {
        "id" : "f7b485e7-830b-4826-9652-562c87972068",
        "id_categorie" : "categorie19",
        "id_repas" : "df0dcbc9-433a-4c6c-9b09-03d47b7b810e",
        "name" : "Chèvre (optionnel)",
        "quantite" : "1",
        "rank" : 5
      },
      "fbdb83c8-e4a6-4fe8-a7bb-3e5a1eb89e62" : {
        "id" : "fbdb83c8-e4a6-4fe8-a7bb-3e5a1eb89e62",
        "id_categorie" : "categorie3",
        "id_repas" : "25b4e9e2-2a92-43b1-8097-c02aca3e9425",
        "name" : "Crème fraîche ",
        "quantite" : "20cl",
        "rank" : 3
      },
      "fc2d4f47-a85d-4620-aabe-786785024f99" : {
        "id" : "fc2d4f47-a85d-4620-aabe-786785024f99",
        "id_categorie" : "categorie9",
        "id_repas" : "218fd948-a38d-4bf5-a395-e94228c8e51d",
        "name" : "Graine de sésame (optionnel)",
        "quantite" : "10g",
        "rank" : 4
      },
      "fe515cb8-54c9-4a0e-815a-64c10cf76130" : {
        "id" : "fe515cb8-54c9-4a0e-815a-64c10cf76130",
        "id_categorie" : "categorie4",
        "id_repas" : "b51f01ef-8e7e-4731-8dce-02d527c1479f",
        "name" : "Poulet",
        "quantite" : "120g",
        "rank" : 8
      },
      "feb2864d-d108-42ed-b3ff-9b82a3890600" : {
        "id" : "feb2864d-d108-42ed-b3ff-9b82a3890600",
        "id_categorie" : "None",
        "id_repas" : "14d4c8fe-7a8e-4bb8-8cbd-64ab29d178aa",
        "name" : "Éclat de chocolat",
        "quantite" : "200 g",
        "rank" : 0
      }
    },
    "repas" : {
      "13a6e228-b415-4365-9972-314f036d109c" : {
        "description" : "Aux légumes ",
        "duree" : "1h30",
        "id" : "13a6e228-b415-4365-9972-314f036d109c",
        "imageUri" : "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image13a6e228-b415-4365-9972-314f036d109c?alt=media&token=fa3fcf62-82f2-4df6-b923-ce0bef6c4b1f",
        "lien" : "",
        "name" : "Légumes farcis ",
        "recette" : "Couper tous les légumes, les faire cuire en mode ratatouille.\n\nlaisser certains légumes entier(mais creuser) pour être farcis \n\najouter les épices et sauce tomate si besoin\n\nen parallèle faire cuire le riz (1 portion de riz = 2 d'eau)\n\nune fois cuit, mettre le riz dans la poêle, mélanger. rajouter de la sauce tomate au besoin.\n\nmettre le mélange dans les légumes a farcir. saupoudrez de fromage.\n\nmettre eau four a 200° pendant 45min.\n\nmiam miam !\n",
        "tags" : [ "Végétarien ", "Plat" ]
      },
      "14d4c8fe-7a8e-4bb8-8cbd-64ab29d178aa" : {
        "description" : "Recette de chez atelierduchocolat",
        "duree" : "45 min",
        "id" : "14d4c8fe-7a8e-4bb8-8cbd-64ab29d178aa",
        "imageUri" : "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image14d4c8fe-7a8e-4bb8-8cbd-64ab29d178aa?alt=media&token=1d5a6144-2e6f-4683-bc04-1e8a0c28f166",
        "lien" : "",
        "name" : "Madeleine au coeur de chocolat au lait citron",
        "recette" : "1 : préchauffer le four à 240°\n\n2 : mélanger 120g de beurre pommade avec le sucre, incorporer les oeufs un par un, puis du zeste de citron\n\n3 : ajouter la farine et la levure chimique, et bien mélanger pour obtenir une pâte lisse\n\n4 : beurrer et fariner les moules à madeleines et les remplir au 2/3 de pâte\n\n5 : placer un éclat de chocolat au lait citron au centre de chaque madeleine\n\n6 : enfourner pendant 10min en baissant le four à 200°\n\n7 : démouler les madeleines et les laisser refroidir\n\n8 : faire fondre le chocolat restant au bain marie, avec le pinceau : étaler le chocolat sur la moitié de chaque madeleine et laisser figer.\n\ndégustez ❤️",
        "tags" : [ "Dessert" ]
      },
      "218fd948-a38d-4bf5-a395-e94228c8e51d" : {
        "description" : "Un sushi déstructurer",
        "duree" : "30min",
        "id" : "218fd948-a38d-4bf5-a395-e94228c8e51d",
        "imageUri" : "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image218fd948-a38d-4bf5-a395-e94228c8e51d?alt=media&token=55b47d3e-f2b1-4152-97bb-b1ba6f94082c",
        "lien" : "",
        "name" : "Sushi bowl",
        "recette" : "Faire cuire le riz (1riz = 2 eau)\ntout mélanger dans un bowl!\n",
        "tags" : [ "Plat" ]
      },
      "25b4e9e2-2a92-43b1-8097-c02aca3e9425" : {
        "description" : "Vite fait bien fait ",
        "duree" : "45min",
        "id" : "25b4e9e2-2a92-43b1-8097-c02aca3e9425",
        "imageUri" : "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image25b4e9e2-2a92-43b1-8097-c02aca3e9425?alt=media&token=7337f7cd-7240-424e-a7e7-0f1707081324",
        "lien" : "",
        "name" : "Samoussas roquefort butternut ",
        "recette" : "Faire revenir le butternut et l'oignon dans de l'huile et les épices. retirez toute l'eau\n\n\nle réduire en purée avec la crème et le yaourt\n\ncouper le roquefort en dés et remuer le tout.\n\nfaçonnez les samoussas.\n\nfaire cuire au four ou a la poêle avec de l'huile.\n\n au four c'est meilleur : 200° 10min-15min",
        "tags" : [ "Plat", "Apéro" ]
      },
      "2b3b7821-a351-4083-84e7-8891d95ffbc9" : {
        "description" : "Des petites nuggets veggie",
        "duree" : "55 min",
        "id" : "2b3b7821-a351-4083-84e7-8891d95ffbc9",
        "imageUri" : "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image2b3b7821-a351-4083-84e7-8891d95ffbc9?alt=media&token=bb908ae8-66e5-49d1-8f83-98ad8776bfaf",
        "lien" : "",
        "name" : "Galette de brocolis ",
        "recette" : "Coupez le brocoli en morceaux et faire cuire 5min dans de l'eau bouillante salée. \n\négoutter et hacher.\n\nhachez l'ail\n\nbattez les oeufs dans un saladier\n\ntout mélanger, et réalisez les galettes.\n\nenfournez pour 15min",
        "tags" : [ "Végétarien ", "Apéro ", "Plat " ]
      },
      "2d5b555f-043b-4c73-8955-aed99046b841" : {
        "description" : "Recette de mamy ❤️",
        "duree" : "15min",
        "id" : "2d5b555f-043b-4c73-8955-aed99046b841",
        "imageUri" : "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image2d5b555f-043b-4c73-8955-aed99046b841?alt=media&token=8e1f25a6-ea97-45be-9703-2e4d613844c8",
        "lien" : "",
        "name" : "Salade de pâtes",
        "recette" : "1 : faire cuire les pâtes\n\n2 : tout coupé en dés ou en petit eheh\n\n3 : mélanger le tout avec huile vinaigre et une cuillère à soupe de mayonnaise",
        "tags" : [ "Plat" ]
      },
      "2fecc7f5-c4f5-44e7-9c4c-f34296ed5180" : {
        "description" : "",
        "duree" : "45 min",
        "id" : "2fecc7f5-c4f5-44e7-9c4c-f34296ed5180",
        "imageUri" : "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image2fecc7f5-c4f5-44e7-9c4c-f34296ed5180?alt=media&token=8740ee13-1792-4748-8ea8-ba03e714e34a",
        "lien" : "",
        "name" : "Samoussas poulet carottes",
        "recette" : "Couper le poulet et les carottes en petite morceaux.\n\nfaire cuire le poulet et les carottes.\nsalez la préparation\nplier les samoussas\n\ncuire au four ou à la poêle.\n\nau four c'est meilleur : 200° 10min",
        "tags" : [ "Plat", "Apéro" ]
      },
      "3cd6f535-6bdd-48cf-9b1f-6d343347473a" : {
        "description" : "Le plat de la flemme",
        "duree" : "25min",
        "id" : "3cd6f535-6bdd-48cf-9b1f-6d343347473a",
        "imageUri" : "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image3cd6f535-6bdd-48cf-9b1f-6d343347473a?alt=media&token=4b1b375c-997e-4371-917f-dc05c6009ebf",
        "lien" : "",
        "name" : "Risotto crevettes",
        "recette" : "Tout mettre dans l'autocuiseur et ne rien faire :)",
        "tags" : [ "Plat", "Facile" ]
      },
      "7863c28f-5186-460d-95a6-f46fbfd1b6ba" : {
        "description" : "La base",
        "duree" : "5 min",
        "id" : "7863c28f-5186-460d-95a6-f46fbfd1b6ba",
        "imageUri" : "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image7863c28f-5186-460d-95a6-f46fbfd1b6ba?alt=media&token=314b817b-02e5-42b4-b3ef-87a2a84c7b0b",
        "lien" : "",
        "name" : "Mojito",
        "recette" : "Fais toi plaiz tu sais faire :)",
        "tags" : [ "Cocktail", "Apero" ]
      },
      "791978e3-839a-4bb6-b134-dcddf7200d99" : {
        "description" : "Le repas de la hess",
        "duree" : "5 min",
        "id" : "791978e3-839a-4bb6-b134-dcddf7200d99",
        "imageUri" : "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image791978e3-839a-4bb6-b134-dcddf7200d99?alt=media&token=07a25abf-d40a-4946-a228-343566e71416",
        "lien" : "",
        "name" : "Semoule lentilles",
        "recette" : "Faire cuire la semoule et les lentilles, mélanger miam",
        "tags" : [ "Rapide", "Plat" ]
      },
      "7d806748-f666-4db6-bcad-2f81fdec5374" : {
        "description" : "C'est l'été",
        "duree" : "10 min",
        "id" : "7d806748-f666-4db6-bcad-2f81fdec5374",
        "imageUri" : "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image7d806748-f666-4db6-bcad-2f81fdec5374?alt=media&token=c1e62fd3-b687-4f92-bc62-2b644e78dc89",
        "lien" : "",
        "name" : "Taboulé épicé",
        "recette" : "Tout mélanger 😁",
        "tags" : [ "Plat" ]
      },
      "861728ce-f15d-42da-ba6e-a92d461c81fd" : {
        "description" : "Où comment finir les restes ",
        "duree" : "1h25",
        "id" : "861728ce-f15d-42da-ba6e-a92d461c81fd",
        "imageUri" : "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image861728ce-f15d-42da-ba6e-a92d461c81fd?alt=media&token=7bbf0e08-4b60-4ea3-be9a-8ec569c95f87",
        "lien" : "",
        "name" : "Tartelettes salées ",
        "recette" : "Tout couper, on peut précuire le légumes.\n\nmettre la pâte sur les emportes pièces, badigeonnez de sauce tomate, ou de pesto ou de crème fraîche ou n'importe quoi.\nmettre les légumes et le fromage.\n\ncuire 200° 40min\n\nmiam\n\n",
        "tags" : [ "Végétarien ", "Plat", "Restes " ]
      },
      "8fc288f8-54bd-42f7-9e26-6c7017e25faa" : {
        "description" : "C'est l'été ou quoi là",
        "duree" : "10 min",
        "id" : "8fc288f8-54bd-42f7-9e26-6c7017e25faa",
        "imageUri" : "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image8fc288f8-54bd-42f7-9e26-6c7017e25faa?alt=media&token=61fc1f1a-6b29-4967-891b-be640c7b3a6c",
        "lien" : "",
        "name" : "Tomates mozzarella",
        "recette" : "Couper tout, et rajouter des épices à ta convenance eheh",
        "tags" : [ "Plat", "Été" ]
      },
      "90a01e21-4491-453b-b003-370e6722dc4f" : {
        "description" : "Nouilles sautées aux légumes",
        "duree" : "25 min",
        "id" : "90a01e21-4491-453b-b003-370e6722dc4f",
        "imageUri" : "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image90a01e21-4491-453b-b003-370e6722dc4f?alt=media&token=6b343a37-ea06-4231-8597-a731607a4a53",
        "lien" : "",
        "name" : "Wok",
        "recette" : "(si nouilles au seitan : faire la marinade (2 c.s de soja + 1c.s de sirop d'érable), couper le seitan en petit cube, faire tremper.)\n\ncouper les légumes : \n- les poivrons en lamelles\n- les brocolis en petites branches\n- les champignons en lamelles\n- râper les carottes en lamelles\n(- les pousses de soja sont ok à mettre comme ça)\n\nfaites revenir dans une poêle, recouvrir pour cuire à la vapeur.\n\n(dans une autre poêle, faites cuire le poulet ou le seitan).\n\nune fois les légumes cuits, ajouter de la sauce soja (et la viande). laissez cuire 3-4min.\n\najoutez les nouilles, rajoutez de la sauce, couvrez pour cuire à la vapeur. laissez cuire 5min.\n\nmiam\n\n",
        "tags" : [ "Plat", "Asiatique" ]
      },
      "acb9bef5-a446-4315-aa2f-ba10dcffc603" : {
        "description" : "Le meilleur plat",
        "duree" : "30 min",
        "id" : "acb9bef5-a446-4315-aa2f-ba10dcffc603",
        "imageUri" : "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imageacb9bef5-a446-4315-aa2f-ba10dcffc603?alt=media&token=eed53d29-5703-43c5-be9a-df7865fd214d",
        "lien" : "",
        "name" : "Hamburger maison",
        "recette" : "1 : faire griller le pain au four avec le cheddar dessus\n\n2 : faire des steaks à la main\n\n3 : quand tout est cuit, faire le burger\n\nhopla c'est prêt eheh",
        "tags" : [ "Plat", "Gros", "Burger" ]
      },
      "b51f01ef-8e7e-4731-8dce-02d527c1479f" : {
        "description" : "Le plat qui met tout le monde d'accord",
        "duree" : "45min",
        "id" : "b51f01ef-8e7e-4731-8dce-02d527c1479f",
        "imageUri" : "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imageb51f01ef-8e7e-4731-8dce-02d527c1479f?alt=media&token=b3aab37d-139f-4e66-9e32-973c595e0012",
        "lien" : "",
        "name" : "Wraps poulet pané",
        "recette" : "Pané le poulet : farine oeuf chapelure.\ntout couper\nchauffer les wraps\net puis miam hein !",
        "tags" : [ "Plat", "Soirée", "Groupe" ]
      },
      "c558c46b-c6ba-4b51-b43c-6caf92914f10" : {
        "description" : "Ou polenta jambon maïs",
        "duree" : "10min",
        "id" : "c558c46b-c6ba-4b51-b43c-6caf92914f10",
        "imageUri" : "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imagec558c46b-c6ba-4b51-b43c-6caf92914f10?alt=media&token=ccea49ec-2e9a-4ade-9e51-85e6eb2cbcbf",
        "lien" : "",
        "name" : "Purée jambon maïs",
        "recette" : "Faire la purée et mélanger",
        "tags" : [ "Rapide", "Plat" ]
      },
      "da89093a-2b0e-4b27-b3d2-51acb43f92c8" : {
        "description" : "Trop bon selon hélo",
        "duree" : "20 min",
        "id" : "da89093a-2b0e-4b27-b3d2-51acb43f92c8",
        "imageUri" : "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imageda89093a-2b0e-4b27-b3d2-51acb43f92c8?alt=media&token=c5906df5-4bfc-46e4-9281-090dfe8f392b",
        "lien" : "",
        "name" : "Salade chèvre chaud",
        "recette" : "Préchauffer le four à 180\n\n1 : tout mettre dedans le saladier\n\n2 : couper des tranches de pains, badigeonner d'huile d'olive et d'épices, mettre le fromage dessus, l'enfourner au four\n\nbon ap'\n",
        "tags" : [ "Plat" ]
      },
      "df0dcbc9-433a-4c6c-9b09-03d47b7b810e" : {
        "description" : "Pour hélo",
        "duree" : "10min",
        "id" : "df0dcbc9-433a-4c6c-9b09-03d47b7b810e",
        "imageUri" : "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imagedf0dcbc9-433a-4c6c-9b09-03d47b7b810e?alt=media&token=c05b4d7f-ebda-40f4-99a8-705dd45bbc99",
        "lien" : "",
        "name" : "Croque monsieur végé",
        "recette" : "200° pendant 10min",
        "tags" : [ "Végétarien ", "Apéro ", "Plat" ]
      },
      "e909b217-b4c7-4bd8-a3ce-3ed7cf0d539f" : {
        "description" : "Le repas de la flemme tu coco",
        "duree" : "3 min",
        "id" : "e909b217-b4c7-4bd8-a3ce-3ed7cf0d539f",
        "imageUri" : "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imagee909b217-b4c7-4bd8-a3ce-3ed7cf0d539f?alt=media&token=fc1a2d5f-7aac-481a-b900-3e0e2306263d",
        "lien" : "",
        "name" : "Raviole",
        "recette" : "Ben du coup... ouvrir le paquet...oui..et euh..cuire dans l'eau qui bout...voilaaaaa",
        "tags" : [ "Plat", "Rapide", "Flemme" ]
      },
      "ef1bd565-7bc7-4693-a1f1-54e24b2a99fd" : {
        "description" : "Recette juju - l'original ",
        "duree" : "10min",
        "id" : "ef1bd565-7bc7-4693-a1f1-54e24b2a99fd",
        "imageUri" : "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imageef1bd565-7bc7-4693-a1f1-54e24b2a99fd?alt=media&token=6c5b5a4e-10d0-4350-a059-ce51622a87b6",
        "lien" : "",
        "name" : "Croque monsieur ",
        "recette" : "200° au four 10min\n",
        "tags" : [ "Plat", "Apéro ", "Rapide" ]
      },
      "f82e4ed6-6404-4bc1-95b7-831a71e100cc" : {
        "description" : "Les crêpes de maman !",
        "duree" : "15min",
        "id" : "f82e4ed6-6404-4bc1-95b7-831a71e100cc",
        "imageUri" : "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imagef82e4ed6-6404-4bc1-95b7-831a71e100cc?alt=media&token=484b6ad0-47c3-40fe-a7d9-a93be26b6738",
        "lien" : "",
        "name" : "Crêpes",
        "recette" : "Pour 10 crêpes\n\ndans un saladier :\n- versez les oeufs (battus jusqu'à devenir blanc)\n- faites fondre le beurre et versez le\n- ajoutez le sucre\najoutez alternativement lait et farine dans le saladier\n- versez autant de rhum que nécessaire",
        "tags" : [ "Brunch", "Dessert", "Goûter" ]
      }
    },
    "semainier" : {
      "dimanche" : {
        "apero" : "None",
        "id_semainier" : "dimanche",
        "midi" : "None",
        "soir" : "None"
      },
      "jeudi" : {
        "apero" : "None",
        "id_semainier" : "jeudi",
        "midi" : "None",
        "soir" : "b51f01ef-8e7e-4731-8dce-02d527c1479f"
      },
      "lundi" : {
        "apero" : "None",
        "id_semainier" : "lundi",
        "midi" : "None",
        "soir" : "2b3b7821-a351-4083-84e7-8891d95ffbc9"
      },
      "mardi" : {
        "apero" : "None",
        "id_semainier" : "mardi",
        "midi" : "e909b217-b4c7-4bd8-a3ce-3ed7cf0d539f",
        "soir" : "da89093a-2b0e-4b27-b3d2-51acb43f92c8"
      },
      "mercredi" : {
        "apero" : "None",
        "id_semainier" : "mercredi",
        "midi" : "None",
        "soir" : "218fd948-a38d-4bf5-a395-e94228c8e51d"
      },
      "samedi" : {
        "apero" : "None",
        "id_semainier" : "samedi",
        "midi" : "None",
        "soir" : "None"
      },
      "vendredi" : {
        "apero" : "None",
        "id_semainier" : "vendredi",
        "midi" : "None",
        "soir" : "None"
      }
    },
    "semainierSuivant" : {
      "dimanche" : {
        "apero" : "None",
        "id_semainier" : "dimanche",
        "midi" : "None",
        "soir" : "None"
      },
      "jeudi" : {
        "apero" : "None",
        "id_semainier" : "jeudi",
        "midi" : "None",
        "soir" : "None"
      },
      "lundi" : {
        "apero" : "None",
        "id_semainier" : "lundi",
        "midi" : "None",
        "soir" : "None"
      },
      "mardi" : {
        "apero" : "None",
        "id_semainier" : "mardi",
        "midi" : "None",
        "soir" : "None"
      },
      "mercredi" : {
        "apero" : "None",
        "id_semainier" : "mercredi",
        "midi" : "None",
        "soir" : "None"
      },
      "samedi" : {
        "apero" : "None",
        "id_semainier" : "samedi",
        "midi" : "None",
        "soir" : "None"
      },
      "vendredi" : {
        "apero" : "None",
        "id_semainier" : "vendredi",
        "midi" : "None",
        "soir" : "None"
      }
    }
  }"""
        ).nextValue() as JSONObject

        var jsonArray = jsonObject.getJSONObject("categorie")
        for (currentKey in jsonArray.keys()){
            // ID
            val categorieId = jsonArray.getJSONObject(currentKey.toString()).getString("id")
            // name
            val categorieName = jsonArray.getJSONObject(currentKey.toString()).getString("name")

            categorie.put(currentKey, CategorieModel(categorieId,categorieName))
        }

        jsonArray = jsonObject.getJSONObject("ingredients")

        for (currentKey in jsonArray.keys()){

            // ID
            val ingredientId = jsonArray.getJSONObject(currentKey.toString()).getString("id")
            // name
            val ingredientName = jsonArray.getJSONObject(currentKey.toString()).getString("name")
            // name
            val ingredientRepas = jsonArray.getJSONObject(currentKey.toString()).getString("id_repas")
            // name
            val ingredientCategorie = jsonArray.getJSONObject(currentKey.toString()).getString("id_categorie")
            // name
            val ingredientQuantite = jsonArray.getJSONObject(currentKey.toString()).getString("quantite")
            // name
            val ingredientRank = jsonArray.getJSONObject(currentKey.toString()).getString("rank")

            ingredients.put(currentKey, IngredientModel(ingredientId,ingredientName, ingredientRepas, ingredientCategorie,ingredientQuantite,ingredientRank.toInt()))
        }

        jsonArray = jsonObject.getJSONObject("repas")

        for (currentKey in jsonArray.keys()){

            // ID
            val repasDescription = jsonArray.getJSONObject(currentKey.toString()).getString("description")
            // name
            val repasDuree = jsonArray.getJSONObject(currentKey.toString()).getString("duree")
            // name
            val repasId = jsonArray.getJSONObject(currentKey.toString()).getString("id")
            // name
            val repasImage = jsonArray.getJSONObject(currentKey.toString()).getString("imageUri")
            // name
            val repasLien = jsonArray.getJSONObject(currentKey.toString()).getString("lien")
            // name
            val repasName = jsonArray.getJSONObject(currentKey.toString()).getString("name")
            val repasRecette= jsonArray.getJSONObject(currentKey.toString()).getString("recette")

            val tagsList = jsonArray.getJSONObject(currentKey.toString()).getJSONArray("tags")
            val tagListArray = arrayListOf<String>()
            for(tag in 0 until tagsList.length()){
                tagListArray.add(tagsList[tag].toString())
            }


            repas.put(currentKey, RepasModel(repasId, repasName,repasDescription,repasImage,repasLien,repasRecette, tagListArray, repasDuree))
        }

        return mutableMapOf<String, Any>(
            "categorie" to categorie,
            "repas" to repas,
            "ingredients" to ingredients,
            "semainierSuivant" to semainierSuivant,
            "semainier" to semainier)
    }

}