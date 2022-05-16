package fr.juju.myapplication

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.view.View
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import org.json.JSONObject
import org.json.JSONTokener

class RegisterActivity : AppCompatActivity() {
    lateinit var etEmail: EditText
    lateinit var etPassword: EditText
    lateinit var signin: Button
    lateinit var go: TextView
    lateinit var go2: ImageView
    lateinit var etConfirmPassword: EditText
    lateinit var cadenas: ImageView
    val MIN_PASSWORD_LENGTH = 6

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.register_fragment)
        this.onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                signin.performClick()
            }
        })
        viewInitializations()
        etConfirmPassword.setOnClickListener{
            cadenas.clearColorFilter()
        }

        etConfirmPassword.addTextChangedListener(
            object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    cadenas.clearColorFilter()
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun afterTextChanged(s: Editable) {
                }
            }
        )

        var auth: FirebaseAuth
        auth = Firebase.auth
        go.setOnClickListener {
            if (validateInput()) {
                auth.createUserWithEmailAndPassword(
                    etEmail.text.toString(),
                    etPassword.text.toString()
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            val user = auth.currentUser
                            val databaseRef = FirebaseDatabase.getInstance()
                            if (user != null) {
                                user!!.sendEmailVerification()
                                    .addOnCompleteListener { task ->
                                        if (task.isSuccessful) {
                                            databaseRef.reference.child(user.uid).setValue(newDBModel())
                                            val intent = Intent(baseContext, LoginActivity::class.java)
                                            val b = Bundle()
                                            b.putString("emailInput", etEmail.text.toString())
                                            intent.putExtras(b)
                                            startActivity(intent)
                                            overridePendingTransition(0, 0)
                                            finish()
                                        } else Toast.makeText(this, "Erreur lors de l'envoi de l'email", Toast.LENGTH_SHORT).show()
                                    }
                            }

                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(
                                baseContext, "Authentication failed",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
            }
        }
        go2.setOnClickListener{
            go.performClick()
        }
        signin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
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
        if (etConfirmPassword.text.toString() == "") {
            etConfirmPassword.error = "Please Confirm Password"
            return false
        }

        if (etPassword.text.toString() != etConfirmPassword.text.toString()) {
            cadenas.setColorFilter(ContextCompat.getColor(this, android.R.color.holo_red_dark), android.graphics.PorterDuff.Mode.MULTIPLY)
            etConfirmPassword.error = "Mot de passe incorrects"
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

    fun viewInitializations() {
        etEmail = findViewById(R.id.et_email)
        etPassword = findViewById(R.id.et_password)
        signin = findViewById(R.id.bt_signup)
        go = findViewById(R.id.textView12)
        go2 = findViewById(R.id.envoyer)
        etConfirmPassword = findViewById(R.id.et_confirmPassword)
        cadenas = findViewById(R.id.imageView14)
        // To show back button in actionbar
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun newDBModel(): Any {
        val categorie: MutableMap<String, CategorieModel> = mutableMapOf<String, CategorieModel>()
        val repas: MutableMap<String, RepasModel> = mutableMapOf<String, RepasModel>()
        val ingredients: MutableMap<String, IngredientModel> =
            mutableMapOf<String, IngredientModel>()

        val semainier: MutableMap<String, SemainierModel> = mutableMapOf<String, SemainierModel>(
            "lundi" to SemainierModel("None", "None", "None", arrayListOf<String>(), "lundi"),
            "mardi" to SemainierModel("None", "None", "None", arrayListOf<String>(), "mardi"),
            "mercredi" to SemainierModel("None", "None", "None", arrayListOf<String>(), "mercredi"),
            "jeudi" to SemainierModel("None", "None", "None", arrayListOf<String>(), "jeudi"),
            "vendredi" to SemainierModel("None", "None", "None", arrayListOf<String>(), "vendredi"),
            "samedi" to SemainierModel("None", "None", "None", arrayListOf<String>(), "samedi"),
            "dimanche" to SemainierModel("None", "None", "None", arrayListOf<String>(), "dimanche")
        )
        val semainierSuivant: MutableMap<String, SemainierModel> =
            mutableMapOf<String, SemainierModel>(
                "lundi" to SemainierModel("None", "None", "None", arrayListOf<String>(), "lundi"),
                "mardi" to SemainierModel("None", "None", "None", arrayListOf<String>(), "mardi"),
                "mercredi" to SemainierModel(
                    "None",
                    "None",
                    "None",
                    arrayListOf<String>(),
                    "mercredi"
                ),
                "jeudi" to SemainierModel("None", "None", "None", arrayListOf<String>(), "jeudi"),
                "vendredi" to SemainierModel(
                    "None",
                    "None",
                    "None",
                    arrayListOf<String>(),
                    "vendredi"
                ),
                "samedi" to SemainierModel("None", "None", "None", arrayListOf<String>(), "samedi"),
                "dimanche" to SemainierModel(
                    "None",
                    "None",
                    "None",
                    arrayListOf<String>(),
                    "dimanche"
                )
            )
        val jsonObject = JSONTokener(
           """{ "categorie": {
      "categorie1": {
        "id": "categorie1",
        "name": "Légumes"
      },
      "categorie10": {
        "id": "categorie10",
        "name": "Surgelés"
      },
      "categorie11": {
        "id": "categorie11",
        "name": "Pâtisseries"
      },
      "categorie12": {
        "id": "categorie12",
        "name": "Epices et condiments"
      },
      "categorie13": {
        "id": "categorie13",
        "name": "Alcool"
      },
      "categorie14": {
        "id": "categorie14",
        "name": "Boisson"
      },
      "categorie15": {
        "id": "categorie15",
        "name": "Ménage"
      },
      "categorie16": {
        "id": "categorie16",
        "name": "Hygiène"
      },
      "categorie17": {
        "id": "categorie17",
        "name": "Autres"
      },
      "categorie18": {
        "id": "categorie18",
        "name": "Apéro"
      },
      "categorie19": {
        "id": "categorie19",
        "name": "Fromages"
      },
      "categorie2": {
        "id": "categorie2",
        "name": "Fruits"
      },
      "categorie20": {
        "id": "categorie20",
        "name": "Yaourts"
      },
      "categorie21": {
        "id": "categorie21",
        "name": "Pains"
      },
      "categorie22": {
        "id": "categorie22",
        "name": "Plats Préparés"
      },
      "categorie3": {
        "id": "categorie3",
        "name": "Produits laitier"
      },
      "categorie4": {
        "id": "categorie4",
        "name": "Viandes"
      },
      "categorie5": {
        "id": "categorie5",
        "name": "Poisson"
      },
      "categorie6": {
        "id": "categorie6",
        "name": "Protéines végétales"
      },
      "categorie7": {
        "id": "categorie7",
        "name": "Féculents"
      },
      "categorie8": {
        "id": "categorie8",
        "name": "Conserves"
      },
      "categorie9": {
        "id": "categorie9",
        "name": "Graines"
      }
    },
    "course": {
      "06b53fe5-5099-4ef6-b39f-ef8fedd735bf": {
        "ajoutExterieur": "true",
        "categorie": "Autres",
        "id": "06b53fe5-5099-4ef6-b39f-ef8fedd735bf",
        "name": "Deo juju",
        "ok": "false",
        "quantite": "1"
      },
      "0f2458d6-7d15-42c1-9550-b02728b66e55": {
        "ajoutExterieur": "true",
        "categorie": "Autres",
        "id": "0f2458d6-7d15-42c1-9550-b02728b66e55",
        "name": "Sycre",
        "ok": "false",
        "quantite": "1"
      },
      "260491fd-8cce-4274-ad7f-6983a2443a01": {
        "ajoutExterieur": "true",
        "categorie": "Autres",
        "id": "260491fd-8cce-4274-ad7f-6983a2443a01",
        "name": "Tistane",
        "ok": "false",
        "quantite": "1"
      },
      "4032b585-9da9-499f-ad85-b412d15c03d4": {
        "ajoutExterieur": "true",
        "categorie": "P'tit dej",
        "id": "4032b585-9da9-499f-ad85-b412d15c03d4",
        "name": "Bel vita",
        "ok": "false",
        "quantite": "2"
      },
      "4d111916-3295-413d-95b7-d985fd54f2dc": {
        "ajoutExterieur": "true",
        "categorie": "Yaourts",
        "id": "4d111916-3295-413d-95b7-d985fd54f2dc",
        "name": "Yaourt ",
        "ok": "false",
        "quantite": "1"
      },
      "6287e25b-9dac-4c61-81b5-1eb70df26039": {
        "ajoutExterieur": "true",
        "categorie": "Boisson",
        "id": "6287e25b-9dac-4c61-81b5-1eb70df26039",
        "name": "Jus d'orange",
        "ok": "false",
        "quantite": "1"
      },
      "a5ca5dd4-b3e2-4400-9ae6-276c8d8c1e2a": {
        "ajoutExterieur": "true",
        "categorie": "Hygiène",
        "id": "a5ca5dd4-b3e2-4400-9ae6-276c8d8c1e2a",
        "name": "Crème hydratante ",
        "ok": "false",
        "quantite": "1"
      }
    },
    "ingredients": {
      "007362b9-9a53-4cc8-b546-4ebf08d636e8": {
        "id": "007362b9-9a53-4cc8-b546-4ebf08d636e8",
        "id_categorie": "categorie3",
        "id_repas": "13a6e228-b415-4365-9972-314f036d109c",
        "name": "Fromage à gratin",
        "quantite": "20g",
        "rank": 0
      },
      "025aeb45-a207-49d4-931c-4af1ef640da5": {
        "id": "025aeb45-a207-49d4-931c-4af1ef640da5",
        "id_categorie": "categorie3",
        "id_repas": "bc3c46d3-fce1-4dd9-a070-4dd14da12c3c",
        "name": "Crème de coco",
        "quantite": "25cl",
        "rank": 0
      },
      "02b4ba0a-ff60-4bf0-8533-ee8b683f5965": {
        "id": "02b4ba0a-ff60-4bf0-8533-ee8b683f5965",
        "id_categorie": "categorie1",
        "id_repas": "2fecc7f5-c4f5-44e7-9c4c-f34296ed5180",
        "name": "Carottes",
        "quantite": "4",
        "rank": 2
      },
      "0304b259-863f-4d9b-babb-5de7aeef90f1": {
        "id": "0304b259-863f-4d9b-babb-5de7aeef90f1",
        "id_categorie": "categorie1",
        "id_repas": "3f5b18c5-cc67-4414-a572-8540a5a9082b",
        "name": "Menthe",
        "quantite": "10g",
        "rank": 8
      },
      "03ac7df4-1a9a-4801-8923-588d92771665": {
        "id": "03ac7df4-1a9a-4801-8923-588d92771665",
        "id_categorie": "categorie1",
        "id_repas": "9df0c46b-6251-498b-a1b7-2d1c1b344830",
        "name": "Tomates",
        "quantite": "1",
        "rank": 2
      },
      "03bd6c31-4035-49fe-9b25-4048a41e452e": {
        "id": "03bd6c31-4035-49fe-9b25-4048a41e452e",
        "id_categorie": "categorie12",
        "id_repas": "13a6e228-b415-4365-9972-314f036d109c",
        "name": "Sauce tomate panzani",
        "quantite": "1",
        "rank": 6
      },
      "043100ba-272b-44ef-a810-ef66dd2b765e": {
        "id": "043100ba-272b-44ef-a810-ef66dd2b765e",
        "id_categorie": "categorie11",
        "id_repas": "234de9de-e4d3-422a-8e3e-0d46d16e2e71",
        "name": "Poudre d'amandes",
        "quantite": "50g",
        "rank": 6
      },
      "04d5def9-2e86-4b9c-a946-5fe33e5447ab": {
        "id": "04d5def9-2e86-4b9c-a946-5fe33e5447ab",
        "id_categorie": "categorie1",
        "id_repas": "8fc288f8-54bd-42f7-9e26-6c7017e25faa",
        "name": "Tomates",
        "quantite": "2",
        "rank": 0
      },
      "0527802d-1f69-44f4-9843-6ff02bc6e0c1": {
        "id": "0527802d-1f69-44f4-9843-6ff02bc6e0c1",
        "id_categorie": "categorie1",
        "id_repas": "77be6bdb-ffb1-4615-a27d-760620b9077b",
        "name": "Concombre",
        "quantite": "1",
        "rank": 3
      },
      "0825176f-8402-4331-b8ed-c623d8f5d593": {
        "id": "0825176f-8402-4331-b8ed-c623d8f5d593",
        "id_categorie": "categorie17",
        "id_repas": "5588414e-a159-4c0f-bfb8-cc50a697ff6a",
        "name": "Base sauce blanche (recette à part)",
        "quantite": "60g",
        "rank": 13
      },
      "08599586-c8ff-42ed-ad7e-4e575b039d6c": {
        "id": "08599586-c8ff-42ed-ad7e-4e575b039d6c",
        "id_categorie": "categorie13",
        "id_repas": "53597371-9ff4-4b63-b1cf-71163246e032",
        "name": "Amareto",
        "quantite": "1 dose",
        "rank": 1
      },
      "087607a4-be9d-4e62-a2fa-caffadb9f1fb": {
        "id": "087607a4-be9d-4e62-a2fa-caffadb9f1fb",
        "id_categorie": "categorie14",
        "id_repas": "e8569d80-552d-4428-b3a9-d749e7df9baf",
        "name": "Glaçons (optionnel)",
        "quantite": "10",
        "rank": 7
      },
      "0be41e0a-25d9-4fdc-9c37-6f66a0bf1a8a": {
        "id": "0be41e0a-25d9-4fdc-9c37-6f66a0bf1a8a",
        "id_categorie": "categorie12",
        "id_repas": "2c1360f5-68ac-413a-978e-bee1df461fd9",
        "name": "Menthe fraîche",
        "quantite": "20g",
        "rank": 5
      },
      "0c175d13-3cf7-4680-978b-1bd717a780a6": {
        "id": "0c175d13-3cf7-4680-978b-1bd717a780a6",
        "id_categorie": "categorie11",
        "id_repas": "5689e797-6007-49e5-b3ee-0f6b79b16263",
        "name": "Raisins secs (optionnel)",
        "quantite": "au jugé",
        "rank": 3
      },
      "0da2d4a3-a29b-4eb6-bd06-4931d551ceae": {
        "id": "0da2d4a3-a29b-4eb6-bd06-4931d551ceae",
        "id_categorie": "categorie4",
        "id_repas": "c558c46b-c6ba-4b51-b43c-6caf92914f10",
        "name": "Jambon",
        "quantite": "2 tranches",
        "rank": 3
      },
      "0dd9a8bd-ebd5-494f-b9f3-640fc7276ee0": {
        "id": "0dd9a8bd-ebd5-494f-b9f3-640fc7276ee0",
        "id_categorie": "categorie12",
        "id_repas": "acccc58b-016e-41e1-9666-5b2edb4e416c",
        "name": "Huile d'olive",
        "quantite": "10g",
        "rank": 4
      },
      "0e5ce6bb-effc-40b1-9ac2-4a69b31ca795": {
        "id": "0e5ce6bb-effc-40b1-9ac2-4a69b31ca795",
        "id_categorie": "categorie19",
        "id_repas": "8beb0db0-e785-4a7e-b6ea-d19353295cba",
        "name": "Fromage râpé",
        "quantite": "30g",
        "rank": 4
      },
      "0e6c6255-db43-4bbe-9936-1e35474b58f4": {
        "id": "0e6c6255-db43-4bbe-9936-1e35474b58f4",
        "id_categorie": "categorie1",
        "id_repas": "25b4e9e2-2a92-43b1-8097-c02aca3e9425",
        "name": "Courge butternut",
        "quantite": "1",
        "rank": 0
      },
      "0ed4b562-2e5e-4cfe-8e24-eb47fad428ed": {
        "id": "0ed4b562-2e5e-4cfe-8e24-eb47fad428ed",
        "id_categorie": "categorie3",
        "id_repas": "25b4e9e2-2a92-43b1-8097-c02aca3e9425",
        "name": "Yaourt nature",
        "quantite": "1",
        "rank": 7
      },
      "0ffcf117-49c2-4ae9-90d4-239accdf363b": {
        "id": "0ffcf117-49c2-4ae9-90d4-239accdf363b",
        "id_categorie": "categorie1",
        "id_repas": "c363b713-2079-4b55-97f8-39f2da8fa340",
        "name": "Tomates",
        "quantite": "6",
        "rank": 1
      },
      "110849ac-ecec-4c09-b4b7-b526416b5667": {
        "id": "110849ac-ecec-4c09-b4b7-b526416b5667",
        "id_categorie": "categorie11",
        "id_repas": "234de9de-e4d3-422a-8e3e-0d46d16e2e71",
        "name": "Chocolat",
        "quantite": "75g",
        "rank": 5
      },
      "129b600c-c6b7-4ec3-8130-998350a1c782": {
        "id": "129b600c-c6b7-4ec3-8130-998350a1c782",
        "id_categorie": "categorie12",
        "id_repas": "bc3c46d3-fce1-4dd9-a070-4dd14da12c3c",
        "name": "Huile de colza",
        "quantite": "1 cas",
        "rank": 3
      },
      "131ae188-ddd2-483e-85cc-c908b11d2b56": {
        "id": "131ae188-ddd2-483e-85cc-c908b11d2b56",
        "id_categorie": "categorie1",
        "id_repas": "c558c46b-c6ba-4b51-b43c-6caf92914f10",
        "name": "Tomates cerises",
        "quantite": "8",
        "rank": 6
      },
      "134fd54d-40d0-483e-8c1a-ebe1b1b700b6": {
        "id": "134fd54d-40d0-483e-8c1a-ebe1b1b700b6",
        "id_categorie": "categorie1",
        "id_repas": "b06f4b84-32a6-43a9-86dc-c5da09dae484",
        "name": "Échalote",
        "quantite": "1",
        "rank": 2
      },
      "1450681a-52bf-4f68-93fb-990a6557b6c8": {
        "id": "1450681a-52bf-4f68-93fb-990a6557b6c8",
        "id_categorie": "categorie11",
        "id_repas": "fed07360-0c7c-441d-aa61-6cf6e742e298",
        "name": "Farine de blé (pâte à cookie)",
        "quantite": "30g",
        "rank": 4
      },
      "145c2672-f549-4df3-be50-8c47536f576a": {
        "id": "145c2672-f549-4df3-be50-8c47536f576a",
        "id_categorie": "categorie1",
        "id_repas": "90a01e21-4491-453b-b003-370e6722dc4f",
        "name": "Poivron",
        "quantite": "1",
        "rank": 0
      },
      "14ab3316-09c1-4347-ae23-59fa4a59eacf": {
        "id": "14ab3316-09c1-4347-ae23-59fa4a59eacf",
        "id_categorie": "categorie13",
        "id_repas": "f82e4ed6-6404-4bc1-95b7-831a71e100cc",
        "name": "Rhum negrita",
        "quantite": "au jugé",
        "rank": 0
      },
      "14d0865d-fc47-4b63-b050-e205fe83e017": {
        "id": "14d0865d-fc47-4b63-b050-e205fe83e017",
        "id_categorie": "categorie3",
        "id_repas": "fed07360-0c7c-441d-aa61-6cf6e742e298",
        "name": "Lait d'amande (pâte à brownie)",
        "quantite": "50ml",
        "rank": 16
      },
      "1588294a-e0ef-4540-b8cc-631417cc5bd2": {
        "id": "1588294a-e0ef-4540-b8cc-631417cc5bd2",
        "id_categorie": "categorie1",
        "id_repas": "f82e4ed6-6404-4bc1-95b7-831a71e100cc",
        "name": "Champignon (optionnel)",
        "quantite": "3",
        "rank": 9
      },
      "15bf0cdc-d1c0-44fd-a548-7049565e6d52": {
        "id": "15bf0cdc-d1c0-44fd-a548-7049565e6d52",
        "id_categorie": "categorie1",
        "id_repas": "8beb0db0-e785-4a7e-b6ea-d19353295cba",
        "name": "Champignons",
        "quantite": "40g",
        "rank": 3
      },
      "18770b72-aa4b-4192-8789-fcb410ed9696": {
        "id": "18770b72-aa4b-4192-8789-fcb410ed9696",
        "id_categorie": "categorie5",
        "id_repas": "3cd6f535-6bdd-48cf-9b1f-6d343347473a",
        "name": "Crevettes",
        "quantite": "1 boites",
        "rank": 5
      },
      "189e43c5-c9fd-468e-9b1a-ac0fa65a7cad": {
        "id": "189e43c5-c9fd-468e-9b1a-ac0fa65a7cad",
        "id_categorie": "categorie4",
        "id_repas": "f82e4ed6-6404-4bc1-95b7-831a71e100cc",
        "name": "Jambon (optionnel)",
        "quantite": "3 tranches",
        "rank": 6
      },
      "18ce4126-cad8-49d2-b161-9713ab16ac66": {
        "id": "18ce4126-cad8-49d2-b161-9713ab16ac66",
        "id_categorie": "categorie11",
        "id_repas": "7863c28f-5186-460d-95a6-f46fbfd1b6ba",
        "name": "Sucre de canne",
        "quantite": "10g",
        "rank": 0
      },
      "197e50b8-648d-4fe3-878f-f8f045275cef": {
        "id": "197e50b8-648d-4fe3-878f-f8f045275cef",
        "id_categorie": "categorie7",
        "id_repas": "25b4e9e2-2a92-43b1-8097-c02aca3e9425",
        "name": "Feuilles de brick",
        "quantite": "10",
        "rank": 4
      },
      "19fe99b7-b892-479e-b28c-f466fe028df9": {
        "id": "19fe99b7-b892-479e-b28c-f466fe028df9",
        "id_categorie": "categorie1",
        "id_repas": "b5d9c9e0-334e-4ce8-8889-c8bf4207d91c",
        "name": "Chou fleur",
        "quantite": "1",
        "rank": 0
      },
      "1a256082-d71c-42bc-b806-d545dd7ddf97": {
        "id": "1a256082-d71c-42bc-b806-d545dd7ddf97",
        "id_categorie": "categorie1",
        "id_repas": "f8ab0c87-8712-42b7-b7ff-875f70a6681f",
        "name": "Oignons ciselé",
        "quantite": "1",
        "rank": 1
      },
      "1a2717e5-9176-42b2-aebd-f34184a0cc70": {
        "id": "1a2717e5-9176-42b2-aebd-f34184a0cc70",
        "id_categorie": "categorie11",
        "id_repas": "1fa6c7eb-eb37-47f0-b9c9-29492fd7a838",
        "name": "Cacao maigre",
        "quantite": "1 cac",
        "rank": 5
      },
      "1b46408c-a3fa-4a2b-a757-fae47e8d6156": {
        "id": "1b46408c-a3fa-4a2b-a757-fae47e8d6156",
        "id_categorie": "categorie1",
        "id_repas": "7ddc8ac5-6c8a-44e4-9e3e-cad5bdd65594",
        "name": "Patates douces",
        "quantite": "1",
        "rank": 1
      },
      "1bc6f1e1-5aab-47c3-aa07-41a655c56d66": {
        "id": "1bc6f1e1-5aab-47c3-aa07-41a655c56d66",
        "id_categorie": "categorie6",
        "id_repas": "5588414e-a159-4c0f-bfb8-cc50a697ff6a",
        "name": "Tofu au curcuma",
        "quantite": "100g",
        "rank": 0
      },
      "1bcc1b24-0b29-45cc-a7b2-11ce24624af3": {
        "id": "1bcc1b24-0b29-45cc-a7b2-11ce24624af3",
        "id_categorie": "categorie19",
        "id_repas": "ef1bd565-7bc7-4693-a1f1-54e24b2a99fd",
        "name": "Gruyère",
        "quantite": "50g",
        "rank": 3
      },
      "1bceb460-fb11-4ddb-b5c1-c5686cea88bd": {
        "id": "1bceb460-fb11-4ddb-b5c1-c5686cea88bd",
        "id_categorie": "categorie1",
        "id_repas": "acccc58b-016e-41e1-9666-5b2edb4e416c",
        "name": "Oignon",
        "quantite": "1",
        "rank": 5
      },
      "1c82e880-9c71-40df-83c2-b287a77324f1": {
        "id": "1c82e880-9c71-40df-83c2-b287a77324f1",
        "id_categorie": "categorie12",
        "id_repas": "234de9de-e4d3-422a-8e3e-0d46d16e2e71",
        "name": "Cannelle (facultatif)",
        "quantite": "1 cac",
        "rank": 10
      },
      "1caadd3c-c846-4076-9902-bbcc43643b31": {
        "id": "1caadd3c-c846-4076-9902-bbcc43643b31",
        "id_categorie": "categorie12",
        "id_repas": "218fd948-a38d-4bf5-a395-e94228c8e51d",
        "name": "Sauce soja",
        "quantite": "1",
        "rank": 3
      },
      "1ce67a41-22ff-4826-81b9-0f5a29e4570a": {
        "id": "1ce67a41-22ff-4826-81b9-0f5a29e4570a",
        "id_categorie": "categorie12",
        "id_repas": "13a6e228-b415-4365-9972-314f036d109c",
        "name": "Herbe de provence",
        "quantite": "1",
        "rank": 8
      },
      "1ecc2cb7-4e0d-463e-975c-7f97d05a6afe": {
        "id": "1ecc2cb7-4e0d-463e-975c-7f97d05a6afe",
        "id_categorie": "categorie1",
        "id_repas": "da89093a-2b0e-4b27-b3d2-51acb43f92c8",
        "name": "Salade",
        "quantite": "1",
        "rank": 1
      },
      "201bc472-bca1-4428-bfcc-935ba61c8e17": {
        "id": "201bc472-bca1-4428-bfcc-935ba61c8e17",
        "id_categorie": "categorie12",
        "id_repas": "56b025f6-91af-4d15-92dc-b791c6d63933",
        "name": "Sel poivre",
        "quantite": "au jugé ",
        "rank": 4
      },
      "206d6d15-4903-4246-85e8-b6f9f1c4a19e": {
        "id": "206d6d15-4903-4246-85e8-b6f9f1c4a19e",
        "id_categorie": "categorie1",
        "id_repas": "3f5b18c5-cc67-4414-a572-8540a5a9082b",
        "name": "Concombre",
        "quantite": "125g",
        "rank": 3
      },
      "2186967f-7b20-4773-a1a1-e7a700035218": {
        "id": "2186967f-7b20-4773-a1a1-e7a700035218",
        "id_categorie": "categorie11",
        "id_repas": "0dd7d87b-8539-400b-b4b2-c2a58ca68fec",
        "name": "Beurre de cacahuète",
        "quantite": "50g",
        "rank": 6
      },
      "2387fb9f-4d20-4778-94b1-ef4c17386729": {
        "id": "2387fb9f-4d20-4778-94b1-ef4c17386729",
        "id_categorie": "categorie1",
        "id_repas": "acb9bef5-a446-4315-aa2f-ba10dcffc603",
        "name": "Salade",
        "quantite": "1",
        "rank": 0
      },
      "24c46f6d-18ff-4dc1-baf6-e09079ea6f48": {
        "id": "24c46f6d-18ff-4dc1-baf6-e09079ea6f48",
        "id_categorie": "categorie1",
        "id_repas": "3f5b18c5-cc67-4414-a572-8540a5a9082b",
        "name": "Radis",
        "quantite": "150g",
        "rank": 2
      },
      "25248e7e-e6dd-47af-acb7-c09e17fc01f2": {
        "id": "25248e7e-e6dd-47af-acb7-c09e17fc01f2",
        "id_categorie": "categorie12",
        "id_repas": "fed07360-0c7c-441d-aa61-6cf6e742e298",
        "name": "Sel",
        "quantite": "1 cac",
        "rank": 8
      },
      "25374a11-d08b-4dd8-8988-d38d462ecd7f": {
        "id": "25374a11-d08b-4dd8-8988-d38d462ecd7f",
        "id_categorie": "categorie19",
        "id_repas": "218fd948-a38d-4bf5-a395-e94228c8e51d",
        "name": "Saint moret (optionnel)",
        "quantite": "1",
        "rank": 6
      },
      "25408335-e53e-4a32-a6cb-f27e4e1be142": {
        "id": "25408335-e53e-4a32-a6cb-f27e4e1be142",
        "id_categorie": "categorie14",
        "id_repas": "e8569d80-552d-4428-b3a9-d749e7df9baf",
        "name": "Eau",
        "quantite": "2L",
        "rank": 0
      },
      "259b2079-4e14-4e7c-8ade-b366cc268886": {
        "id": "259b2079-4e14-4e7c-8ade-b366cc268886",
        "id_categorie": "categorie11",
        "id_repas": "b51f01ef-8e7e-4731-8dce-02d527c1479f",
        "name": "Chapelure",
        "quantite": "1",
        "rank": 0
      },
      "2653328f-b1b5-4f4d-a4d2-05d761116c83": {
        "id": "2653328f-b1b5-4f4d-a4d2-05d761116c83",
        "id_categorie": "categorie12",
        "id_repas": "fed07360-0c7c-441d-aa61-6cf6e742e298",
        "name": "Purée de cacahuète sans sucre ajouté",
        "quantite": "50g",
        "rank": 2
      },
      "26f24fc0-fad5-401c-a149-8fc57ca85ac6": {
        "id": "26f24fc0-fad5-401c-a149-8fc57ca85ac6",
        "id_categorie": "categorie11",
        "id_repas": "2b3b7821-a351-4083-84e7-8891d95ffbc9",
        "name": "Farine",
        "quantite": "20g",
        "rank": 1
      },
      "27111a5a-5bef-4677-aad5-1ce52710373f": {
        "id": "27111a5a-5bef-4677-aad5-1ce52710373f",
        "id_categorie": "categorie12",
        "id_repas": "c363b713-2079-4b55-97f8-39f2da8fa340",
        "name": "Thym",
        "quantite": "2 bouquets",
        "rank": 6
      },
      "2725d4f0-fc48-43a8-aecd-5ee3ee1a6109": {
        "id": "2725d4f0-fc48-43a8-aecd-5ee3ee1a6109",
        "id_categorie": "categorie12",
        "id_repas": "2c1360f5-68ac-413a-978e-bee1df461fd9",
        "name": "Branches de romarin (optionnel)",
        "quantite": "au jugé ",
        "rank": 9
      },
      "2836cf06-e9bb-4524-af83-5daf8ba3794c": {
        "id": "2836cf06-e9bb-4524-af83-5daf8ba3794c",
        "id_categorie": "categorie1",
        "id_repas": "c363b713-2079-4b55-97f8-39f2da8fa340",
        "name": "Gros oignon",
        "quantite": "1",
        "rank": 3
      },
      "2839feb1-13b4-4409-9209-c34a88ad32cf": {
        "id": "2839feb1-13b4-4409-9209-c34a88ad32cf",
        "id_categorie": "categorie12",
        "id_repas": "8beb0db0-e785-4a7e-b6ea-d19353295cba",
        "name": "Sauce tomate",
        "quantite": "100g",
        "rank": 1
      },
      "2889427b-aa1a-4f84-b34b-f036ea694dae": {
        "id": "2889427b-aa1a-4f84-b34b-f036ea694dae",
        "id_categorie": "categorie3",
        "id_repas": "7824bf18-5e96-4297-b754-041d3307105b",
        "name": "Lait",
        "quantite": "250ml",
        "rank": 5
      },
      "296f4414-6e26-43dc-879a-47c5ec334888": {
        "id": "296f4414-6e26-43dc-879a-47c5ec334888",
        "id_categorie": "categorie7",
        "id_repas": "791978e3-839a-4bb6-b134-dcddf7200d99",
        "name": "Semoule",
        "quantite": "125g",
        "rank": 0
      },
      "298c2402-0000-4c1e-b205-5f901e8cb753": {
        "id": "298c2402-0000-4c1e-b205-5f901e8cb753",
        "id_categorie": "categorie11",
        "id_repas": "fed07360-0c7c-441d-aa61-6cf6e742e298",
        "name": "Chocolat noir (pâte à brownie)",
        "quantite": "120g",
        "rank": 9
      },
      "29c4c7bd-453c-4dff-94ea-c7b8ded92883": {
        "id": "29c4c7bd-453c-4dff-94ea-c7b8ded92883",
        "id_categorie": "categorie19",
        "id_repas": "731b2a71-aa1c-42fb-a0bd-9fbc9a07f922",
        "name": "Parmesan",
        "quantite": "20g",
        "rank": 4
      },
      "2a38d26f-8b57-47fe-a794-f24e7263f3db": {
        "id": "2a38d26f-8b57-47fe-a794-f24e7263f3db",
        "id_categorie": "categorie7",
        "id_repas": "3cd6f535-6bdd-48cf-9b1f-6d343347473a",
        "name": "Riz",
        "quantite": "au jugé",
        "rank": 0
      },
      "2bbd4c41-1e9a-4b85-b144-018a1fad0382": {
        "id": "2bbd4c41-1e9a-4b85-b144-018a1fad0382",
        "id_categorie": "categorie1",
        "id_repas": "6871e45f-e3ca-4b07-9704-05023ca1ef47",
        "name": "Champignons frais",
        "quantite": "140g",
        "rank": 5
      },
      "2d873668-8f40-4ff7-9338-d1100a9d54e7": {
        "id": "2d873668-8f40-4ff7-9338-d1100a9d54e7",
        "id_categorie": "categorie1",
        "id_repas": "82b8d7dd-6978-43e1-a0bc-bae4436b1e48",
        "name": "Mélange de jeune pousse (roquette, épinard ...)",
        "quantite": "50g",
        "rank": 5
      },
      "2e16f4ae-93eb-4f7a-b870-81fae353d6f9": {
        "id": "2e16f4ae-93eb-4f7a-b870-81fae353d6f9",
        "id_categorie": "categorie12",
        "id_repas": "5588414e-a159-4c0f-bfb8-cc50a697ff6a",
        "name": "Purée de sésame (tahini)",
        "quantite": "5g",
        "rank": 15
      },
      "2e79b098-b8c5-4baa-8de4-786f39df8047": {
        "id": "2e79b098-b8c5-4baa-8de4-786f39df8047",
        "id_categorie": "categorie1",
        "id_repas": "b51f01ef-8e7e-4731-8dce-02d527c1479f",
        "name": "Avocat",
        "quantite": "1",
        "rank": 1
      },
      "2e7b27e2-8da0-4571-8fbb-6e631861ea01": {
        "id": "2e7b27e2-8da0-4571-8fbb-6e631861ea01",
        "id_categorie": "categorie1",
        "id_repas": "82b8d7dd-6978-43e1-a0bc-bae4436b1e48",
        "name": "Betterave cuite",
        "quantite": "250g",
        "rank": 3
      },
      "2fde453f-523c-44f8-9673-c7c61ccbb57b": {
        "id": "2fde453f-523c-44f8-9673-c7c61ccbb57b",
        "id_categorie": "categorie3",
        "id_repas": "fed07360-0c7c-441d-aa61-6cf6e742e298",
        "name": "Lait d'amande",
        "quantite": "50ml",
        "rank": 1
      },
      "3076d26a-89a2-463a-9f68-4f0f0623817f": {
        "id": "3076d26a-89a2-463a-9f68-4f0f0623817f",
        "id_categorie": "categorie12",
        "id_repas": "82b8d7dd-6978-43e1-a0bc-bae4436b1e48",
        "name": "Huile d'olive",
        "quantite": "2 cas",
        "rank": 8
      },
      "31b16ba5-c029-406b-b3e3-e7fea65a50ce": {
        "id": "31b16ba5-c029-406b-b3e3-e7fea65a50ce",
        "id_categorie": "categorie9",
        "id_repas": "b5d9c9e0-334e-4ce8-8889-c8bf4207d91c",
        "name": "Lentilles de corail",
        "quantite": "100g",
        "rank": 3
      },
      "329917a6-eddf-4228-b013-f1f128b178d9": {
        "id": "329917a6-eddf-4228-b013-f1f128b178d9",
        "id_categorie": "categorie13",
        "id_repas": "f8ab0c87-8712-42b7-b7ff-875f70a6681f",
        "name": "Vin rouge (optionnel)",
        "quantite": "20cl",
        "rank": 6
      },
      "32b27964-f8db-4b1b-b162-d18ef3081f3a": {
        "id": "32b27964-f8db-4b1b-b162-d18ef3081f3a",
        "id_categorie": "categorie12",
        "id_repas": "2b3b7821-a351-4083-84e7-8891d95ffbc9",
        "name": "Persil",
        "quantite": "1",
        "rank": 5
      },
      "32ca1402-93a4-4bbc-9073-7f718dad6ec7": {
        "id": "32ca1402-93a4-4bbc-9073-7f718dad6ec7",
        "id_categorie": "categorie12",
        "id_repas": "82b8d7dd-6978-43e1-a0bc-bae4436b1e48",
        "name": "Sel poivre",
        "quantite": "au jugé ",
        "rank": 12
      },
      "34bb1ec9-5a75-49b3-9958-643129a1287e": {
        "id": "34bb1ec9-5a75-49b3-9958-643129a1287e",
        "id_categorie": "categorie12",
        "id_repas": "5588414e-a159-4c0f-bfb8-cc50a697ff6a",
        "name": "Coriandre fraîche",
        "quantite": "10g",
        "rank": 14
      },
      "34cee77e-e569-472b-8618-00e58203b521": {
        "id": "34cee77e-e569-472b-8618-00e58203b521",
        "id_categorie": "categorie1",
        "id_repas": "731b2a71-aa1c-42fb-a0bd-9fbc9a07f922",
        "name": "Oignon rouge",
        "quantite": "1/2",
        "rank": 6
      },
      "363102ac-4553-4104-ae07-2db2ab884330": {
        "id": "363102ac-4553-4104-ae07-2db2ab884330",
        "id_categorie": "categorie4",
        "id_repas": "c5730e8c-88b3-4ad7-b003-bc4a6313fc4b",
        "name": "Lardons",
        "quantite": "1 paquet ",
        "rank": 1
      },
      "37847370-6c84-4977-a34e-54bbe8d8c3e4": {
        "id": "37847370-6c84-4977-a34e-54bbe8d8c3e4",
        "id_categorie": "categorie1",
        "id_repas": "3cd6f535-6bdd-48cf-9b1f-6d343347473a",
        "name": "Champignon",
        "quantite": "4",
        "rank": 2
      },
      "37965186-4af8-442d-a6b0-ac97e945be70": {
        "id": "37965186-4af8-442d-a6b0-ac97e945be70",
        "id_categorie": "categorie1",
        "id_repas": "c363b713-2079-4b55-97f8-39f2da8fa340",
        "name": "Oignons verts",
        "quantite": "3",
        "rank": 2
      },
      "37d14c11-f4eb-4fc6-903f-11d79b0f9ddc": {
        "id": "37d14c11-f4eb-4fc6-903f-11d79b0f9ddc",
        "id_categorie": "categorie11",
        "id_repas": "7824bf18-5e96-4297-b754-041d3307105b",
        "name": "Farine",
        "quantite": "225g",
        "rank": 0
      },
      "38a2458f-354f-42ff-a5c1-f905073dfe2a": {
        "id": "38a2458f-354f-42ff-a5c1-f905073dfe2a",
        "id_categorie": "categorie3",
        "id_repas": "ef1bd565-7bc7-4693-a1f1-54e24b2a99fd",
        "name": "Crème fraîche (optionnel)",
        "quantite": "10g",
        "rank": 2
      },
      "38ce7aa7-eeac-4035-b34e-b7946569321d": {
        "id": "38ce7aa7-eeac-4035-b34e-b7946569321d",
        "id_categorie": "categorie12",
        "id_repas": "6871e45f-e3ca-4b07-9704-05023ca1ef47",
        "name": "Bouillon de légumes",
        "quantite": "1",
        "rank": 1
      },
      "38e5f219-e42a-4107-852d-b5544fd6accc": {
        "id": "38e5f219-e42a-4107-852d-b5544fd6accc",
        "id_categorie": "categorie6",
        "id_repas": "b06f4b84-32a6-43a9-86dc-c5da09dae484",
        "name": "Tofu fumé",
        "quantite": "200g",
        "rank": 1
      },
      "38fb8877-9d02-48a3-8a99-e3c4f389da2e": {
        "id": "38fb8877-9d02-48a3-8a99-e3c4f389da2e",
        "id_categorie": "categorie14",
        "id_repas": "7863c28f-5186-460d-95a6-f46fbfd1b6ba",
        "name": "Limonade",
        "quantite": "1",
        "rank": 1
      },
      "39e1ca82-60be-4e45-97ec-d3add06d6b17": {
        "id": "39e1ca82-60be-4e45-97ec-d3add06d6b17",
        "id_categorie": "categorie12",
        "id_repas": "56b025f6-91af-4d15-92dc-b791c6d63933",
        "name": "Huile d'olive",
        "quantite": "0,5 cas",
        "rank": 2
      },
      "3afac380-be93-40d6-9b76-39b179f1ed6a": {
        "id": "3afac380-be93-40d6-9b76-39b179f1ed6a",
        "id_categorie": "categorie4",
        "id_repas": "90a01e21-4491-453b-b003-370e6722dc4f",
        "name": "Poulet (optionnel)",
        "quantite": "120g",
        "rank": 1
      },
      "3befdcdf-6888-4e9b-af71-8cd502fc783c": {
        "id": "3befdcdf-6888-4e9b-af71-8cd502fc783c",
        "id_categorie": "categorie1",
        "id_repas": "731b2a71-aa1c-42fb-a0bd-9fbc9a07f922",
        "name": "Courgettes",
        "quantite": "1",
        "rank": 2
      },
      "3c14e243-ce7b-4eec-a549-fbc1bfcec323": {
        "id": "3c14e243-ce7b-4eec-a549-fbc1bfcec323",
        "id_categorie": "categorie1",
        "id_repas": "13a6e228-b415-4365-9972-314f036d109c",
        "name": "Courgettes",
        "quantite": "3",
        "rank": 1
      },
      "3c791771-2558-4673-a06e-970cdb580c69": {
        "id": "3c791771-2558-4673-a06e-970cdb580c69",
        "id_categorie": "categorie1",
        "id_repas": "7d806748-f666-4db6-bcad-2f81fdec5374",
        "name": "Tomates",
        "quantite": "2",
        "rank": 1
      },
      "3d8b7a15-7aef-4d54-8e1d-f2c8a6cba3fd": {
        "id": "3d8b7a15-7aef-4d54-8e1d-f2c8a6cba3fd",
        "id_categorie": "categorie11",
        "id_repas": "e9b03b98-4be7-454a-be98-4a3234baf3ec",
        "name": "Sucre blanc",
        "quantite": "entre 200 et 300 g",
        "rank": 2
      },
      "3e2b7d00-bbec-43f7-9fe8-c30a17a330d8": {
        "id": "3e2b7d00-bbec-43f7-9fe8-c30a17a330d8",
        "id_categorie": "categorie11",
        "id_repas": "0dd7d87b-8539-400b-b4b2-c2a58ca68fec",
        "name": "Levure",
        "quantite": "1 sachet",
        "rank": 8
      },
      "3eba5b2c-fd15-4535-add7-2585b5293d77": {
        "id": "3eba5b2c-fd15-4535-add7-2585b5293d77",
        "id_categorie": "categorie11",
        "id_repas": "74513f16-2c62-4017-9320-a854f703b59f",
        "name": "Farine",
        "quantite": "2 cas",
        "rank": 4
      },
      "3fc0c491-5ae3-4b58-a627-86d17471a339": {
        "id": "3fc0c491-5ae3-4b58-a627-86d17471a339",
        "id_categorie": "categorie1",
        "id_repas": "77be6bdb-ffb1-4615-a27d-760620b9077b",
        "name": "Avocat",
        "quantite": "1",
        "rank": 4
      },
      "422d1629-df09-48eb-85a1-47a3873903c0": {
        "id": "422d1629-df09-48eb-85a1-47a3873903c0",
        "id_categorie": "categorie1",
        "id_repas": "e8569d80-552d-4428-b3a9-d749e7df9baf",
        "name": "Menthe",
        "quantite": "1 botte",
        "rank": 6
      },
      "422f9857-97b2-437c-baf6-cf28a03f449f": {
        "id": "422f9857-97b2-437c-baf6-cf28a03f449f",
        "id_categorie": "categorie7",
        "id_repas": "c558c46b-c6ba-4b51-b43c-6caf92914f10",
        "name": "Polenta (optionnel)",
        "quantite": "120g",
        "rank": 4
      },
      "43aada96-b63c-4b4c-8fc6-7d4cfba946f9": {
        "id": "43aada96-b63c-4b4c-8fc6-7d4cfba946f9",
        "id_categorie": "categorie14",
        "id_repas": "2c1360f5-68ac-413a-978e-bee1df461fd9",
        "name": "Eau pétillante fraîche",
        "quantite": "400ml",
        "rank": 1
      },
      "43ad7789-1a52-4cbd-ab61-a6d158c5e766": {
        "id": "43ad7789-1a52-4cbd-ab61-a6d158c5e766",
        "id_categorie": "categorie12",
        "id_repas": "3f5b18c5-cc67-4414-a572-8540a5a9082b",
        "name": "Sirop d'agave",
        "quantite": "1 cas",
        "rank": 12
      },
      "43b104ae-83b0-4540-8389-5687bf7788f1": {
        "id": "43b104ae-83b0-4540-8389-5687bf7788f1",
        "id_categorie": "categorie3",
        "id_repas": "c558c46b-c6ba-4b51-b43c-6caf92914f10",
        "name": "Kiri",
        "quantite": "1",
        "rank": 1
      },
      "43eb038b-5ae8-44b1-916a-9daf12f8518b": {
        "id": "43eb038b-5ae8-44b1-916a-9daf12f8518b",
        "id_categorie": "categorie11",
        "id_repas": "234de9de-e4d3-422a-8e3e-0d46d16e2e71",
        "name": "Flocon d'avoine",
        "quantite": "140g",
        "rank": 0
      },
      "459ab8f4-53e4-433c-a456-cb59129ed62e": {
        "id": "459ab8f4-53e4-433c-a456-cb59129ed62e",
        "id_categorie": "categorie12",
        "id_repas": "acb9bef5-a446-4315-aa2f-ba10dcffc603",
        "name": "Sauce burger",
        "quantite": "1",
        "rank": 1
      },
      "45d09246-0e54-4899-81d2-6de71ca2283f": {
        "id": "45d09246-0e54-4899-81d2-6de71ca2283f",
        "id_categorie": "categorie1",
        "id_repas": "c363b713-2079-4b55-97f8-39f2da8fa340",
        "name": "Petits piments verts",
        "quantite": "2",
        "rank": 4
      },
      "4630ae30-d3ae-42f9-9984-9e6944b0cc47": {
        "id": "4630ae30-d3ae-42f9-9984-9e6944b0cc47",
        "id_categorie": "categorie1",
        "id_repas": "acccc58b-016e-41e1-9666-5b2edb4e416c",
        "name": "Brocoli (optionnel)",
        "quantite": "1",
        "rank": 8
      },
      "46f269ce-9f66-4c36-b8e0-0cbf5414f371": {
        "id": "46f269ce-9f66-4c36-b8e0-0cbf5414f371",
        "id_categorie": "categorie7",
        "id_repas": "c558c46b-c6ba-4b51-b43c-6caf92914f10",
        "name": "Purée mousseline (optionnel)",
        "quantite": "120g",
        "rank": 5
      },
      "46f33d6b-7c42-4432-aa15-45bd25744bd0": {
        "id": "46f33d6b-7c42-4432-aa15-45bd25744bd0",
        "id_categorie": "categorie4",
        "id_repas": "acb9bef5-a446-4315-aa2f-ba10dcffc603",
        "name": "Viande hachée",
        "quantite": "250 g",
        "rank": 3
      },
      "47751552-5350-429b-9c0b-031a456c7487": {
        "id": "47751552-5350-429b-9c0b-031a456c7487",
        "id_categorie": "categorie12",
        "id_repas": "acccc58b-016e-41e1-9666-5b2edb4e416c",
        "name": "Bouillon de légumes",
        "quantite": "1",
        "rank": 2
      },
      "47a81196-0baa-4053-8d68-c93678d061ed": {
        "id": "47a81196-0baa-4053-8d68-c93678d061ed",
        "id_categorie": "categorie2",
        "id_repas": "e8569d80-552d-4428-b3a9-d749e7df9baf",
        "name": "Pêches mûres",
        "quantite": "7",
        "rank": 2
      },
      "482e89f2-09b5-4f89-b586-f7e87c420a3f": {
        "id": "482e89f2-09b5-4f89-b586-f7e87c420a3f",
        "id_categorie": "categorie12",
        "id_repas": "b5d9c9e0-334e-4ce8-8889-c8bf4207d91c",
        "name": "Soja",
        "quantite": "1 cas",
        "rank": 10
      },
      "4877b608-0cc1-403b-ba9c-f3a6e39bd898": {
        "id": "4877b608-0cc1-403b-ba9c-f3a6e39bd898",
        "id_categorie": "categorie14",
        "id_repas": "5588414e-a159-4c0f-bfb8-cc50a697ff6a",
        "name": "Eau",
        "quantite": "200ml",
        "rank": 3
      },
      "48cd6236-14f3-4fbc-bdda-139b763a4897": {
        "id": "48cd6236-14f3-4fbc-bdda-139b763a4897",
        "id_categorie": "categorie14",
        "id_repas": "fed07360-0c7c-441d-aa61-6cf6e742e298",
        "name": "Café instantané",
        "quantite": "1 cac",
        "rank": 17
      },
      "49137e13-d761-4522-bebd-3831f9b8ad8a": {
        "id": "49137e13-d761-4522-bebd-3831f9b8ad8a",
        "id_categorie": "categorie7",
        "id_repas": "acccc58b-016e-41e1-9666-5b2edb4e416c",
        "name": "Riz",
        "quantite": "120g",
        "rank": 6
      },
      "49a22edf-91c0-4b12-b189-9ba965c56284": {
        "id": "49a22edf-91c0-4b12-b189-9ba965c56284",
        "id_categorie": "categorie4",
        "id_repas": "2d5b555f-043b-4c73-8955-aed99046b841",
        "name": "Cervela herta",
        "quantite": "1",
        "rank": 2
      },
      "49c109fd-13b2-406e-b738-bb22245dbdb4": {
        "id": "49c109fd-13b2-406e-b738-bb22245dbdb4",
        "id_categorie": "categorie12",
        "id_repas": "8beb0db0-e785-4a7e-b6ea-d19353295cba",
        "name": "Huile de truffe (optionnel)",
        "quantite": "1",
        "rank": 7
      },
      "49de4e60-5d93-4a3d-b7a4-65af80b698b3": {
        "id": "49de4e60-5d93-4a3d-b7a4-65af80b698b3",
        "id_categorie": "categorie11",
        "id_repas": "a420e744-955d-483c-8d27-11b31b3cdc70",
        "name": "Maïzena (ou fécule de manioc)",
        "quantite": "1 cas",
        "rank": 1
      },
      "49e64ce5-a8ca-4a46-b6db-6915668e2b3d": {
        "id": "49e64ce5-a8ca-4a46-b6db-6915668e2b3d",
        "id_categorie": "categorie12",
        "id_repas": "25b4e9e2-2a92-43b1-8097-c02aca3e9425",
        "name": "Épices curry",
        "quantite": "1",
        "rank": 5
      },
      "4a613954-152f-469d-9a1b-47d257be28fe": {
        "id": "4a613954-152f-469d-9a1b-47d257be28fe",
        "id_categorie": "categorie11",
        "id_repas": "acccc58b-016e-41e1-9666-5b2edb4e416c",
        "name": "Sirop d'agave",
        "quantite": "70g",
        "rank": 3
      },
      "4b656d27-c725-4e6a-b35c-de6905396a56": {
        "id": "4b656d27-c725-4e6a-b35c-de6905396a56",
        "id_categorie": "categorie2",
        "id_repas": "0dd7d87b-8539-400b-b4b2-c2a58ca68fec",
        "name": "Bananes",
        "quantite": "3",
        "rank": 1
      },
      "4bc43c5e-0803-4e6d-b5e0-abe5a29c836c": {
        "id": "4bc43c5e-0803-4e6d-b5e0-abe5a29c836c",
        "id_categorie": "categorie1",
        "id_repas": "f8ab0c87-8712-42b7-b7ff-875f70a6681f",
        "name": "Carottes",
        "quantite": "1",
        "rank": 3
      },
      "4c72624e-865c-4819-9dc2-0aa7a3c196e8": {
        "id": "4c72624e-865c-4819-9dc2-0aa7a3c196e8",
        "id_categorie": "categorie12",
        "id_repas": "f8ab0c87-8712-42b7-b7ff-875f70a6681f",
        "name": "Huile d'olive (optionnel)",
        "quantite": "au jugé ",
        "rank": 9
      },
      "4d8919bc-4769-4862-82e3-4f4b24639eb2": {
        "id": "4d8919bc-4769-4862-82e3-4f4b24639eb2",
        "id_categorie": "categorie7",
        "id_repas": "ee7e1e07-b9c5-4e7a-b60e-a9252a3eb6e6",
        "name": "Lentilles corail",
        "quantite": "100g",
        "rank": 0
      },
      "4dbf96b6-897b-4dc6-9023-a16414503ecf": {
        "id": "4dbf96b6-897b-4dc6-9023-a16414503ecf",
        "id_categorie": "categorie1",
        "id_repas": "b06f4b84-32a6-43a9-86dc-c5da09dae484",
        "name": "Oignon rouge",
        "quantite": "1",
        "rank": 3
      },
      "4eac9588-dea9-4d36-9bac-c2c5fbf8cd1a": {
        "id": "4eac9588-dea9-4d36-9bac-c2c5fbf8cd1a",
        "id_categorie": "categorie11",
        "id_repas": "0dd7d87b-8539-400b-b4b2-c2a58ca68fec",
        "name": "Farine",
        "quantite": "250g",
        "rank": 0
      },
      "4f050ec8-d987-47b4-9b10-718dce310f9f": {
        "id": "4f050ec8-d987-47b4-9b10-718dce310f9f",
        "id_categorie": "categorie2",
        "id_repas": "5588414e-a159-4c0f-bfb8-cc50a697ff6a",
        "name": "Jus de citron vert",
        "quantite": "0,5 cas",
        "rank": 8
      },
      "5150577e-1966-4bae-b692-21edd7004e08": {
        "id": "5150577e-1966-4bae-b692-21edd7004e08",
        "id_categorie": "categorie19",
        "id_repas": "8fc288f8-54bd-42f7-9e26-6c7017e25faa",
        "name": "Mozzarella",
        "quantite": "1",
        "rank": 1
      },
      "53efdc06-716f-4f5a-b5c9-44927baecdae": {
        "id": "53efdc06-716f-4f5a-b5c9-44927baecdae",
        "id_categorie": "categorie12",
        "id_repas": "f8ab0c87-8712-42b7-b7ff-875f70a6681f",
        "name": "Basilic (optionnel)",
        "quantite": "au jugé ",
        "rank": 10
      },
      "54d9de21-fe5f-4e32-b634-319e8e95013e": {
        "id": "54d9de21-fe5f-4e32-b634-319e8e95013e",
        "id_categorie": "categorie19",
        "id_repas": "9df0c46b-6251-498b-a1b7-2d1c1b344830",
        "name": "Saint moret",
        "quantite": "1 boîte",
        "rank": 1
      },
      "54e554fb-c42d-40f3-a750-d9661b03f74a": {
        "id": "54e554fb-c42d-40f3-a750-d9661b03f74a",
        "id_categorie": "categorie6",
        "id_repas": "b5d9c9e0-334e-4ce8-8889-c8bf4207d91c",
        "name": "Protéines de soja texturées",
        "quantite": "100g",
        "rank": 4
      },
      "54ee1a7e-189e-4267-97af-ea44ca99f28d": {
        "id": "54ee1a7e-189e-4267-97af-ea44ca99f28d",
        "id_categorie": "categorie7",
        "id_repas": "77be6bdb-ffb1-4615-a27d-760620b9077b",
        "name": "Nouilles de riz",
        "quantite": "1 paquet",
        "rank": 1
      },
      "56540076-ec6f-44c0-a668-f71e3c157bd2": {
        "id": "56540076-ec6f-44c0-a668-f71e3c157bd2",
        "id_categorie": "categorie19",
        "id_repas": "df0dcbc9-433a-4c6c-9b09-03d47b7b810e",
        "name": "Roquefort (optionnel)",
        "quantite": "1",
        "rank": 6
      },
      "568126dd-7c8f-4a71-bc55-662d6b0a2660": {
        "id": "568126dd-7c8f-4a71-bc55-662d6b0a2660",
        "id_categorie": "categorie12",
        "id_repas": "0dd7d87b-8539-400b-b4b2-c2a58ca68fec",
        "name": "Canelle",
        "quantite": "1 cac",
        "rank": 9
      },
      "56cbef3c-941a-438f-a5a5-356774fa6ff5": {
        "id": "56cbef3c-941a-438f-a5a5-356774fa6ff5",
        "id_categorie": "categorie12",
        "id_repas": "56b025f6-91af-4d15-92dc-b791c6d63933",
        "name": "Jus de citron",
        "quantite": "1 cas",
        "rank": 1
      },
      "56d51f8a-5625-47b0-b272-5f87bbe361a3": {
        "id": "56d51f8a-5625-47b0-b272-5f87bbe361a3",
        "id_categorie": "categorie1",
        "id_repas": "da89093a-2b0e-4b27-b3d2-51acb43f92c8",
        "name": "Tomates",
        "quantite": "1",
        "rank": 2
      },
      "56eee529-b98a-42ba-9b63-b26776c116b9": {
        "id": "56eee529-b98a-42ba-9b63-b26776c116b9",
        "id_categorie": "categorie7",
        "id_repas": "6871e45f-e3ca-4b07-9704-05023ca1ef47",
        "name": "Quinoa",
        "quantite": "160g",
        "rank": 0
      },
      "56f7accf-d15b-4294-92f1-928856547b46": {
        "id": "56f7accf-d15b-4294-92f1-928856547b46",
        "id_categorie": "categorie1",
        "id_repas": "b5d9c9e0-334e-4ce8-8889-c8bf4207d91c",
        "name": "Gros oignon",
        "quantite": "1",
        "rank": 2
      },
      "57472d2d-c50d-4e13-bca1-802908972c43": {
        "id": "57472d2d-c50d-4e13-bca1-802908972c43",
        "id_categorie": "categorie11",
        "id_repas": "f82e4ed6-6404-4bc1-95b7-831a71e100cc",
        "name": "Farine",
        "quantite": "200g",
        "rank": 1
      },
      "585ba92f-12e6-44e9-805a-88b076251223": {
        "id": "585ba92f-12e6-44e9-805a-88b076251223",
        "id_categorie": "categorie14",
        "id_repas": "e8569d80-552d-4428-b3a9-d749e7df9baf",
        "name": "Thé noir",
        "quantite": "20g",
        "rank": 4
      },
      "590c01ad-ada7-4b4e-ab10-9a42ad2f1c79": {
        "id": "590c01ad-ada7-4b4e-ab10-9a42ad2f1c79",
        "id_categorie": "categorie11",
        "id_repas": "a420e744-955d-483c-8d27-11b31b3cdc70",
        "name": "Farine",
        "quantite": "300g",
        "rank": 0
      },
      "597d134f-1e03-4533-b103-fbd24d0ed87d": {
        "id": "597d134f-1e03-4533-b103-fbd24d0ed87d",
        "id_categorie": "categorie1",
        "id_repas": "13a6e228-b415-4365-9972-314f036d109c",
        "name": "Poivrons",
        "quantite": "2",
        "rank": 2
      },
      "5a316b4a-c794-4105-a8e7-514d60cdc957": {
        "id": "5a316b4a-c794-4105-a8e7-514d60cdc957",
        "id_categorie": "categorie12",
        "id_repas": "2d5b555f-043b-4c73-8955-aed99046b841",
        "name": "Huile",
        "quantite": "au jugé",
        "rank": 4
      },
      "5a890a21-67c6-4911-8e1b-c3a33e7bf3ca": {
        "id": "5a890a21-67c6-4911-8e1b-c3a33e7bf3ca",
        "id_categorie": "categorie1",
        "id_repas": "a420e744-955d-483c-8d27-11b31b3cdc70",
        "name": "Oignon finement ciselé",
        "quantite": "1",
        "rank": 5
      },
      "5bd03959-5e73-454c-b8af-2c44aa10d4f7": {
        "id": "5bd03959-5e73-454c-b8af-2c44aa10d4f7",
        "id_categorie": "categorie1",
        "id_repas": "90a01e21-4491-453b-b003-370e6722dc4f",
        "name": "Brocoli",
        "quantite": "1",
        "rank": 2
      },
      "5bfb0f95-f3c2-41bb-aee4-18cbdec2f1e5": {
        "id": "5bfb0f95-f3c2-41bb-aee4-18cbdec2f1e5",
        "id_categorie": "categorie1",
        "id_repas": "861728ce-f15d-42da-ba6e-a92d461c81fd",
        "name": "Courgettes",
        "quantite": "1",
        "rank": 2
      },
      "5c49daf7-3fac-464f-beec-da1c50d7397b": {
        "id": "5c49daf7-3fac-464f-beec-da1c50d7397b",
        "id_categorie": "categorie12",
        "id_repas": "3f5b18c5-cc67-4414-a572-8540a5a9082b",
        "name": "Sel poivre",
        "quantite": "au jugé ",
        "rank": 9
      },
      "5c950ba1-e733-4a75-9d62-137136c6f440": {
        "id": "5c950ba1-e733-4a75-9d62-137136c6f440",
        "id_categorie": "categorie11",
        "id_repas": "74513f16-2c62-4017-9320-a854f703b59f",
        "name": "Chocolat",
        "quantite": "200g",
        "rank": 0
      },
      "5ce8dfc4-1a45-4e39-9106-2bc2bc93dcdb": {
        "id": "5ce8dfc4-1a45-4e39-9106-2bc2bc93dcdb",
        "id_categorie": "categorie11",
        "id_repas": "7824bf18-5e96-4297-b754-041d3307105b",
        "name": "Levure",
        "quantite": "1 sachet",
        "rank": 3
      },
      "5d1cff98-d325-47a5-85a8-57d0e0cc8bbb": {
        "id": "5d1cff98-d325-47a5-85a8-57d0e0cc8bbb",
        "id_categorie": "categorie11",
        "id_repas": "fed07360-0c7c-441d-aa61-6cf6e742e298",
        "name": "Levure (pâte à brownie)",
        "quantite": "1/2 sachet",
        "rank": 19
      },
      "5dd76807-dfd9-44cc-bb1e-f4273422180f": {
        "id": "5dd76807-dfd9-44cc-bb1e-f4273422180f",
        "id_categorie": "categorie7",
        "id_repas": "b5d9c9e0-334e-4ce8-8889-c8bf4207d91c",
        "name": "Riz (optionnel)",
        "quantite": "100g",
        "rank": 18
      },
      "5ea73f3d-c6c3-4727-8174-b69c750cfa0c": {
        "id": "5ea73f3d-c6c3-4727-8174-b69c750cfa0c",
        "id_categorie": "categorie11",
        "id_repas": "14d4c8fe-7a8e-4bb8-8cbd-64ab29d178aa",
        "name": "Levure",
        "quantite": "1/2 sachet",
        "rank": 5
      },
      "5ed1ed21-37e0-4dea-97a0-3263d887b89f": {
        "id": "5ed1ed21-37e0-4dea-97a0-3263d887b89f",
        "id_categorie": "categorie14",
        "id_repas": "e9b03b98-4be7-454a-be98-4a3234baf3ec",
        "name": "Eau des alpes en bouteille",
        "quantite": "1,20 l",
        "rank": 3
      },
      "5f1b88ad-c104-4e49-b805-1a0f7dc2fb79": {
        "id": "5f1b88ad-c104-4e49-b805-1a0f7dc2fb79",
        "id_categorie": "categorie1",
        "id_repas": "2d5b555f-043b-4c73-8955-aed99046b841",
        "name": "Tomates",
        "quantite": "au jugé",
        "rank": 1
      },
      "5ffbfe52-1f85-4cc0-99a7-2bea0dfe4f13": {
        "id": "5ffbfe52-1f85-4cc0-99a7-2bea0dfe4f13",
        "id_categorie": "categorie11",
        "id_repas": "fed07360-0c7c-441d-aa61-6cf6e742e298",
        "name": "Sucre (pâte à cookie)",
        "quantite": "30g",
        "rank": 5
      },
      "60245a07-4a01-45c0-8096-dc36682cb957": {
        "id": "60245a07-4a01-45c0-8096-dc36682cb957",
        "id_categorie": "categorie12",
        "id_repas": "90a01e21-4491-453b-b003-370e6722dc4f",
        "name": "Sauce soja",
        "quantite": "1",
        "rank": 3
      },
      "6098d3d1-fa80-433d-a5b5-9934de19daf8": {
        "id": "6098d3d1-fa80-433d-a5b5-9934de19daf8",
        "id_categorie": "categorie12",
        "id_repas": "b5d9c9e0-334e-4ce8-8889-c8bf4207d91c",
        "name": "Garam massala",
        "quantite": "3 cac",
        "rank": 11
      },
      "625e509d-78f3-460f-b506-f1259d2cfbb6": {
        "id": "625e509d-78f3-460f-b506-f1259d2cfbb6",
        "id_categorie": "categorie12",
        "id_repas": "1fa6c7eb-eb37-47f0-b9c9-29492fd7a838",
        "name": "Piment d'espelette (facultatif)",
        "quantite": "4 pincées",
        "rank": 7
      },
      "62bba37d-4c1f-4193-8136-696322d17f6c": {
        "id": "62bba37d-4c1f-4193-8136-696322d17f6c",
        "id_categorie": "categorie1",
        "id_repas": "2b3b7821-a351-4083-84e7-8891d95ffbc9",
        "name": "Brocoli",
        "quantite": "1",
        "rank": 0
      },
      "631ecda6-ef94-49af-a6e4-df3595d30d9a": {
        "id": "631ecda6-ef94-49af-a6e4-df3595d30d9a",
        "id_categorie": "categorie7",
        "id_repas": "2d5b555f-043b-4c73-8955-aed99046b841",
        "name": "Coquillettes",
        "quantite": "au jugé",
        "rank": 0
      },
      "6335d25e-7567-4db7-9173-b2c94e1ab6ab": {
        "id": "6335d25e-7567-4db7-9173-b2c94e1ab6ab",
        "id_categorie": "categorie12",
        "id_repas": "da89093a-2b0e-4b27-b3d2-51acb43f92c8",
        "name": "Miel",
        "quantite": "1",
        "rank": 4
      },
      "63d04838-2ae4-4ece-88e2-06780539d8ba": {
        "id": "63d04838-2ae4-4ece-88e2-06780539d8ba",
        "id_categorie": "categorie11",
        "id_repas": "fed07360-0c7c-441d-aa61-6cf6e742e298",
        "name": "Levure (pâte à cookie)",
        "quantite": "1 sachet",
        "rank": 6
      },
      "64ceb6d0-8580-4bfd-9825-64480dc97fe1": {
        "id": "64ceb6d0-8580-4bfd-9825-64480dc97fe1",
        "id_categorie": "categorie14",
        "id_repas": "1fa6c7eb-eb37-47f0-b9c9-29492fd7a838",
        "name": "Expresso",
        "quantite": "30ml",
        "rank": 6
      },
      "65e148a0-44f7-4b94-af98-5513aadbe851": {
        "id": "65e148a0-44f7-4b94-af98-5513aadbe851",
        "id_categorie": "categorie1",
        "id_repas": "b51f01ef-8e7e-4731-8dce-02d527c1479f",
        "name": "Tomates",
        "quantite": "1",
        "rank": 2
      },
      "66248709-25fc-4064-9a65-14b633bd7d70": {
        "id": "66248709-25fc-4064-9a65-14b633bd7d70",
        "id_categorie": "categorie2",
        "id_repas": "2c1360f5-68ac-413a-978e-bee1df461fd9",
        "name": "Pamplemousse",
        "quantite": "100g",
        "rank": 3
      },
      "6753e317-1e2b-44cb-8d6b-4908d2749ae5": {
        "id": "6753e317-1e2b-44cb-8d6b-4908d2749ae5",
        "id_categorie": "categorie1",
        "id_repas": "25b4e9e2-2a92-43b1-8097-c02aca3e9425",
        "name": "Citron",
        "quantite": "1",
        "rank": 9
      },
      "68001d7c-ea2f-48f1-b439-379b8dda325e": {
        "id": "68001d7c-ea2f-48f1-b439-379b8dda325e",
        "id_categorie": "categorie21",
        "id_repas": "acb9bef5-a446-4315-aa2f-ba10dcffc603",
        "name": "Pains buns",
        "quantite": "4",
        "rank": 5
      },
      "689c5f35-3da5-4a09-86a8-001901f5d298": {
        "id": "689c5f35-3da5-4a09-86a8-001901f5d298",
        "id_categorie": "categorie12",
        "id_repas": "c363b713-2079-4b55-97f8-39f2da8fa340",
        "name": "Sel",
        "quantite": "1 cac",
        "rank": 7
      },
      "690f0835-5a80-424b-ae8e-3f8f8f3c3abd": {
        "id": "690f0835-5a80-424b-ae8e-3f8f8f3c3abd",
        "id_categorie": "categorie11",
        "id_repas": "234de9de-e4d3-422a-8e3e-0d46d16e2e71",
        "name": "Levure",
        "quantite": "1 sachet",
        "rank": 8
      },
      "6933f1f4-2b66-4cec-af93-dc9a681dd8e2": {
        "id": "6933f1f4-2b66-4cec-af93-dc9a681dd8e2",
        "id_categorie": "categorie7",
        "id_repas": "861728ce-f15d-42da-ba6e-a92d461c81fd",
        "name": "Pâte brisée",
        "quantite": "1",
        "rank": 0
      },
      "6a9ac360-df8d-43d9-acde-4590eac8defc": {
        "id": "6a9ac360-df8d-43d9-acde-4590eac8defc",
        "id_categorie": "categorie12",
        "id_repas": "731b2a71-aa1c-42fb-a0bd-9fbc9a07f922",
        "name": "Sel poivre",
        "quantite": "au jugé ",
        "rank": 9
      },
      "6b3348fd-7367-49db-94d9-78ad172883ce": {
        "id": "6b3348fd-7367-49db-94d9-78ad172883ce",
        "id_categorie": "categorie12",
        "id_repas": "6871e45f-e3ca-4b07-9704-05023ca1ef47",
        "name": "Persil",
        "quantite": "1",
        "rank": 8
      },
      "6b4a7e96-079f-434a-90cc-19ccd44b2751": {
        "id": "6b4a7e96-079f-434a-90cc-19ccd44b2751",
        "id_categorie": "categorie1",
        "id_repas": "7863c28f-5186-460d-95a6-f46fbfd1b6ba",
        "name": "Citron vert",
        "quantite": "2",
        "rank": 2
      },
      "6bd012ad-34c6-47fc-bfb4-0c2385b58de6": {
        "id": "6bd012ad-34c6-47fc-bfb4-0c2385b58de6",
        "id_categorie": "categorie12",
        "id_repas": "77be6bdb-ffb1-4615-a27d-760620b9077b",
        "name": "Sirop d'érable",
        "quantite": "au jugé",
        "rank": 7
      },
      "6e127f31-13f2-4af5-8c48-c1e446a7d4e0": {
        "id": "6e127f31-13f2-4af5-8c48-c1e446a7d4e0",
        "id_categorie": "categorie4",
        "id_repas": "74513f16-2c62-4017-9320-a854f703b59f",
        "name": "Oeufs",
        "quantite": "3",
        "rank": 3
      },
      "6e24fb4f-cfd4-494f-88c7-e825564d50a1": {
        "id": "6e24fb4f-cfd4-494f-88c7-e825564d50a1",
        "id_categorie": "categorie19",
        "id_repas": "df0dcbc9-433a-4c6c-9b09-03d47b7b810e",
        "name": "Gruyère",
        "quantite": "10g",
        "rank": 4
      },
      "705093e3-7860-4921-8eb8-328c5759875f": {
        "id": "705093e3-7860-4921-8eb8-328c5759875f",
        "id_categorie": "categorie12",
        "id_repas": "234de9de-e4d3-422a-8e3e-0d46d16e2e71",
        "name": "Sel (facultatif)",
        "quantite": "1 cac",
        "rank": 11
      },
      "709296fb-6acf-4f5c-9677-0267d45d0804": {
        "id": "709296fb-6acf-4f5c-9677-0267d45d0804",
        "id_categorie": "categorie12",
        "id_repas": "bc3c46d3-fce1-4dd9-a070-4dd14da12c3c",
        "name": "Coriandre fraiche",
        "quantite": "5,5g",
        "rank": 1
      },
      "71658a91-b690-4eec-bfe1-3f9e1263dec4": {
        "id": "71658a91-b690-4eec-bfe1-3f9e1263dec4",
        "id_categorie": "categorie14",
        "id_repas": "7863c28f-5186-460d-95a6-f46fbfd1b6ba",
        "name": "Sirop de canne (optionnel)",
        "quantite": "1",
        "rank": 3
      },
      "71df0d80-be4d-4d27-a6c1-4359884d8f5b": {
        "id": "71df0d80-be4d-4d27-a6c1-4359884d8f5b",
        "id_categorie": "categorie11",
        "id_repas": "fed07360-0c7c-441d-aa61-6cf6e742e298",
        "name": "Extrait de vanille",
        "quantite": "1 cac",
        "rank": 7
      },
      "720cd85f-74e5-4bed-9f9d-47ff246634b4": {
        "id": "720cd85f-74e5-4bed-9f9d-47ff246634b4",
        "id_categorie": "categorie7",
        "id_repas": "13a6e228-b415-4365-9972-314f036d109c",
        "name": "Riz",
        "quantite": "100g",
        "rank": 5
      },
      "72a51e71-0cf3-4239-8c69-4cd0abda0721": {
        "id": "72a51e71-0cf3-4239-8c69-4cd0abda0721",
        "id_categorie": "categorie11",
        "id_repas": "1fa6c7eb-eb37-47f0-b9c9-29492fd7a838",
        "name": "Farine",
        "quantite": "50g",
        "rank": 4
      },
      "735da47c-2052-4a8c-a4ad-0ef417d28778": {
        "id": "735da47c-2052-4a8c-a4ad-0ef417d28778",
        "id_categorie": "categorie3",
        "id_repas": "f82e4ed6-6404-4bc1-95b7-831a71e100cc",
        "name": "Lait",
        "quantite": "40cl",
        "rank": 2
      },
      "73d3802a-f1d2-4468-af32-9f3895eb3e08": {
        "id": "73d3802a-f1d2-4468-af32-9f3895eb3e08",
        "id_categorie": "categorie11",
        "id_repas": "2c1360f5-68ac-413a-978e-bee1df461fd9",
        "name": "Sucre (optionnel)",
        "quantite": "20g",
        "rank": 6
      },
      "7431a3ea-83a4-421f-b3dc-25b1e07bc581": {
        "id": "7431a3ea-83a4-421f-b3dc-25b1e07bc581",
        "id_categorie": "categorie6",
        "id_repas": "6871e45f-e3ca-4b07-9704-05023ca1ef47",
        "name": "Seitan",
        "quantite": "250g",
        "rank": 2
      },
      "7495e278-4ec1-4455-b78f-5a7fdab33e71": {
        "id": "7495e278-4ec1-4455-b78f-5a7fdab33e71",
        "id_categorie": "categorie6",
        "id_repas": "90a01e21-4491-453b-b003-370e6722dc4f",
        "name": "Seitan (optionnel)",
        "quantite": "1",
        "rank": 4
      },
      "74c1aa66-a2f3-41c7-87dd-557bd92a8323": {
        "id": "74c1aa66-a2f3-41c7-87dd-557bd92a8323",
        "id_categorie": "categorie13",
        "id_repas": "e9b03b98-4be7-454a-be98-4a3234baf3ec",
        "name": "Alcool à 94°",
        "quantite": "1 l",
        "rank": 1
      },
      "77d4b02f-ccf5-49d3-a045-440df8d2e095": {
        "id": "77d4b02f-ccf5-49d3-a045-440df8d2e095",
        "id_categorie": "categorie21",
        "id_repas": "da89093a-2b0e-4b27-b3d2-51acb43f92c8",
        "name": "Pain",
        "quantite": "4",
        "rank": 0
      },
      "7822d946-0ee8-4223-a1c4-2793eb347a9c": {
        "id": "7822d946-0ee8-4223-a1c4-2793eb347a9c",
        "id_categorie": "categorie12",
        "id_repas": "234de9de-e4d3-422a-8e3e-0d46d16e2e71",
        "name": "Sirop d'érable",
        "quantite": "70g",
        "rank": 4
      },
      "784cc258-99cc-4722-ac1b-cb3a7c9fffd1": {
        "id": "784cc258-99cc-4722-ac1b-cb3a7c9fffd1",
        "id_categorie": "categorie1",
        "id_repas": "32980464-61d5-4912-afc3-eb4b120dfd7a",
        "name": "Patates douces",
        "quantite": "1",
        "rank": 0
      },
      "786b4e4f-e2b1-414f-a48d-99a74b1f4b66": {
        "id": "786b4e4f-e2b1-414f-a48d-99a74b1f4b66",
        "id_categorie": "categorie1",
        "id_repas": "e8569d80-552d-4428-b3a9-d749e7df9baf",
        "name": "Citrons jaunes",
        "quantite": "3",
        "rank": 3
      },
      "786dfcf3-e688-4b88-acc6-e3d7820071ed": {
        "id": "786dfcf3-e688-4b88-acc6-e3d7820071ed",
        "id_categorie": "categorie12",
        "id_repas": "acccc58b-016e-41e1-9666-5b2edb4e416c",
        "name": "Sauce soja",
        "quantite": "45g",
        "rank": 1
      },
      "78f24a0f-5e9b-4395-9ef0-5fbda326be28": {
        "id": "78f24a0f-5e9b-4395-9ef0-5fbda326be28",
        "id_categorie": "categorie6",
        "id_repas": "acccc58b-016e-41e1-9666-5b2edb4e416c",
        "name": "Protéines de soja texturées grosse",
        "quantite": "100g",
        "rank": 0
      },
      "78fee07c-82e2-47e9-b5f1-b138bddfd739": {
        "id": "78fee07c-82e2-47e9-b5f1-b138bddfd739",
        "id_categorie": "categorie12",
        "id_repas": "b06f4b84-32a6-43a9-86dc-c5da09dae484",
        "name": "Huile d'olive",
        "quantite": "1 cas",
        "rank": 6
      },
      "790aded6-28a0-4ba6-a755-a5e2f8fb1f1a": {
        "id": "790aded6-28a0-4ba6-a755-a5e2f8fb1f1a",
        "id_categorie": "categorie2",
        "id_repas": "3f5b18c5-cc67-4414-a572-8540a5a9082b",
        "name": "Fraises",
        "quantite": "50g",
        "rank": 5
      },
      "7969b1a2-0a5b-4da8-8106-4f0b565ec155": {
        "id": "7969b1a2-0a5b-4da8-8106-4f0b565ec155",
        "id_categorie": "categorie12",
        "id_repas": "b5d9c9e0-334e-4ce8-8889-c8bf4207d91c",
        "name": "Bouillon de légumes",
        "quantite": "250ml",
        "rank": 6
      },
      "79e289fd-9b40-4927-87b9-c2a231242d04": {
        "id": "79e289fd-9b40-4927-87b9-c2a231242d04",
        "id_categorie": "categorie1",
        "id_repas": "90a01e21-4491-453b-b003-370e6722dc4f",
        "name": "Carottes",
        "quantite": "2",
        "rank": 5
      },
      "7a014ae4-5c7e-4943-a170-16a491ba603f": {
        "id": "7a014ae4-5c7e-4943-a170-16a491ba603f",
        "id_categorie": "categorie3",
        "id_repas": "0dd7d87b-8539-400b-b4b2-c2a58ca68fec",
        "name": "Lait végétal",
        "quantite": "120ml",
        "rank": 2
      },
      "7a043d8a-8cb2-475b-a9f8-2a974fd4c435": {
        "id": "7a043d8a-8cb2-475b-a9f8-2a974fd4c435",
        "id_categorie": "categorie21",
        "id_repas": "ef1bd565-7bc7-4693-a1f1-54e24b2a99fd",
        "name": "Pain de mie",
        "quantite": "4 tranches",
        "rank": 0
      },
      "7a7e220e-09a8-41ba-b555-46bddd57086d": {
        "id": "7a7e220e-09a8-41ba-b555-46bddd57086d",
        "id_categorie": "categorie19",
        "id_repas": "f82e4ed6-6404-4bc1-95b7-831a71e100cc",
        "name": "Roquefort (optionnel)",
        "quantite": "au jugé",
        "rank": 10
      },
      "7b37ef62-2395-4945-a7ae-efae4ef58e0e": {
        "id": "7b37ef62-2395-4945-a7ae-efae4ef58e0e",
        "id_categorie": "categorie1",
        "id_repas": "7ddc8ac5-6c8a-44e4-9e3e-cad5bdd65594",
        "name": "Butternut (optionnel)",
        "quantite": "1",
        "rank": 2
      },
      "7bd50eb8-b9e8-43f9-9a80-9cf537eef9f4": {
        "id": "7bd50eb8-b9e8-43f9-9a80-9cf537eef9f4",
        "id_categorie": "categorie12",
        "id_repas": "25b4e9e2-2a92-43b1-8097-c02aca3e9425",
        "name": "Persil",
        "quantite": "1 botte",
        "rank": 8
      },
      "7bd6d408-d09b-4c50-b849-83c1dde48adb": {
        "id": "7bd6d408-d09b-4c50-b849-83c1dde48adb",
        "id_categorie": "categorie9",
        "id_repas": "82b8d7dd-6978-43e1-a0bc-bae4436b1e48",
        "name": "Cerneaux de noix concassées",
        "quantite": "10g",
        "rank": 6
      },
      "7c3fcf3b-7591-41fa-b89b-e6d4fa078131": {
        "id": "7c3fcf3b-7591-41fa-b89b-e6d4fa078131",
        "id_categorie": "categorie1",
        "id_repas": "5689e797-6007-49e5-b3ee-0f6b79b16263",
        "name": "Carottes",
        "quantite": "4",
        "rank": 1
      },
      "7c79d876-8edb-4ac6-872e-f7d046312365": {
        "id": "7c79d876-8edb-4ac6-872e-f7d046312365",
        "id_categorie": "categorie21",
        "id_repas": "3f5b18c5-cc67-4414-a572-8540a5a9082b",
        "name": "Pain complet",
        "quantite": "2 tranches",
        "rank": 1
      },
      "7c9cacb0-b53b-4b5c-94ff-c69f17b90adf": {
        "id": "7c9cacb0-b53b-4b5c-94ff-c69f17b90adf",
        "id_categorie": "categorie1",
        "id_repas": "acb9bef5-a446-4315-aa2f-ba10dcffc603",
        "name": "Tomates",
        "quantite": "2",
        "rank": 4
      },
      "7eab8f16-fb2c-4e58-891a-812b4e8a155e": {
        "id": "7eab8f16-fb2c-4e58-891a-812b4e8a155e",
        "id_categorie": "categorie3",
        "id_repas": "fed07360-0c7c-441d-aa61-6cf6e742e298",
        "name": "Beurre à température ambiante",
        "quantite": "60g",
        "rank": 11
      },
      "8092bce2-04fe-4287-88a3-ced8cb0a1828": {
        "id": "8092bce2-04fe-4287-88a3-ced8cb0a1828",
        "id_categorie": "categorie12",
        "id_repas": "25b4e9e2-2a92-43b1-8097-c02aca3e9425",
        "name": "Paprika",
        "quantite": "1",
        "rank": 6
      },
      "809e812e-6364-46da-8a76-7e732b058865": {
        "id": "809e812e-6364-46da-8a76-7e732b058865",
        "id_categorie": "categorie1",
        "id_repas": "3cd6f535-6bdd-48cf-9b1f-6d343347473a",
        "name": "Petites tomates",
        "quantite": "1",
        "rank": 3
      },
      "813c4dcb-e24d-4fcf-9097-0184c9d89f68": {
        "id": "813c4dcb-e24d-4fcf-9097-0184c9d89f68",
        "id_categorie": "categorie19",
        "id_repas": "f8ab0c87-8712-42b7-b7ff-875f70a6681f",
        "name": "Fromage",
        "quantite": "150g",
        "rank": 8
      },
      "81432730-5c35-4558-a0d6-ac7b89ff9b07": {
        "id": "81432730-5c35-4558-a0d6-ac7b89ff9b07",
        "id_categorie": "categorie11",
        "id_repas": "b51f01ef-8e7e-4731-8dce-02d527c1479f",
        "name": "Farine",
        "quantite": "50g",
        "rank": 3
      },
      "81c4acd0-5a8c-465c-9ed0-854842d24976": {
        "id": "81c4acd0-5a8c-465c-9ed0-854842d24976",
        "id_categorie": "categorie1",
        "id_repas": "90a01e21-4491-453b-b003-370e6722dc4f",
        "name": "Pousse de soja (optionnel)",
        "quantite": "1",
        "rank": 6
      },
      "82525cf6-5299-4b82-a83a-4a8f470f4f7b": {
        "id": "82525cf6-5299-4b82-a83a-4a8f470f4f7b",
        "id_categorie": "categorie19",
        "id_repas": "f82e4ed6-6404-4bc1-95b7-831a71e100cc",
        "name": "Gruyère (optionnel)",
        "quantite": "au jugé",
        "rank": 7
      },
      "82a1b163-b031-4bd6-ad46-5e23ebd3dd35": {
        "id": "82a1b163-b031-4bd6-ad46-5e23ebd3dd35",
        "id_categorie": "categorie4",
        "id_repas": "a420e744-955d-483c-8d27-11b31b3cdc70",
        "name": "Viande hachée",
        "quantite": "400g",
        "rank": 4
      },
      "83aa5eac-a678-4709-bdd1-2ffa36f2d987": {
        "id": "83aa5eac-a678-4709-bdd1-2ffa36f2d987",
        "id_categorie": "categorie11",
        "id_repas": "14d4c8fe-7a8e-4bb8-8cbd-64ab29d178aa",
        "name": "Beurre",
        "quantite": "150g",
        "rank": 2
      },
      "83f93933-1ffb-42f5-8776-cdc6eb3eb597": {
        "id": "83f93933-1ffb-42f5-8776-cdc6eb3eb597",
        "id_categorie": "categorie12",
        "id_repas": "f8ab0c87-8712-42b7-b7ff-875f70a6681f",
        "name": "Purée de tomates",
        "quantite": "700g",
        "rank": 5
      },
      "8437bc84-38b5-4959-9c4c-ad3e4d81e3e9": {
        "id": "8437bc84-38b5-4959-9c4c-ad3e4d81e3e9",
        "id_categorie": "categorie11",
        "id_repas": "fed07360-0c7c-441d-aa61-6cf6e742e298",
        "name": "Farine de blé (pâte à brownie)",
        "quantite": "80g",
        "rank": 10
      },
      "8569d176-d466-4b38-bed8-e2b6e12b7d75": {
        "id": "8569d176-d466-4b38-bed8-e2b6e12b7d75",
        "id_categorie": "categorie12",
        "id_repas": "2d5b555f-043b-4c73-8955-aed99046b841",
        "name": "Vinaigre",
        "quantite": "au jugé",
        "rank": 5
      },
      "85c4aa7d-c0dd-4a89-a698-4d7c33f44693": {
        "id": "85c4aa7d-c0dd-4a89-a698-4d7c33f44693",
        "id_categorie": "categorie11",
        "id_repas": "f8ab0c87-8712-42b7-b7ff-875f70a6681f",
        "name": "Oignons ciselé",
        "quantite": "1",
        "rank": 12
      },
      "863626e9-5352-435c-91bd-ad0924257328": {
        "id": "863626e9-5352-435c-91bd-ad0924257328",
        "id_categorie": "categorie21",
        "id_repas": "df0dcbc9-433a-4c6c-9b09-03d47b7b810e",
        "name": "Pain de mie complet",
        "quantite": "4 tranches ",
        "rank": 0
      },
      "866d9934-df4c-4c4c-95c3-643a7d0d0347": {
        "id": "866d9934-df4c-4c4c-95c3-643a7d0d0347",
        "id_categorie": "categorie11",
        "id_repas": "74513f16-2c62-4017-9320-a854f703b59f",
        "name": "Beurre",
        "quantite": "125g",
        "rank": 1
      },
      "869c4496-9653-4548-a4ea-45ba34317929": {
        "id": "869c4496-9653-4548-a4ea-45ba34317929",
        "id_categorie": "categorie7",
        "id_repas": "90a01e21-4491-453b-b003-370e6722dc4f",
        "name": "Nouilles chinoises",
        "quantite": "1",
        "rank": 7
      },
      "870f4e5d-7f8a-46e7-bd49-99040bddbedb": {
        "id": "870f4e5d-7f8a-46e7-bd49-99040bddbedb",
        "id_categorie": "categorie12",
        "id_repas": "5588414e-a159-4c0f-bfb8-cc50a697ff6a",
        "name": "Sel poivre",
        "quantite": "au jugé",
        "rank": 11
      },
      "887743e2-61fd-4752-92b2-f5fdd71f3ba3": {
        "id": "887743e2-61fd-4752-92b2-f5fdd71f3ba3",
        "id_categorie": "categorie3",
        "id_repas": "3cd6f535-6bdd-48cf-9b1f-6d343347473a",
        "name": "Crème fraîche",
        "quantite": "1 petit pot",
        "rank": 1
      },
      "8b1e9a97-7064-42bf-bb6e-b9f74df90779": {
        "id": "8b1e9a97-7064-42bf-bb6e-b9f74df90779",
        "id_categorie": "categorie12",
        "id_repas": "56b025f6-91af-4d15-92dc-b791c6d63933",
        "name": "Ail en poudre",
        "quantite": "0,5 cac",
        "rank": 3
      },
      "8bb87614-dd11-48d7-a8df-51fc74d9fb94": {
        "id": "8bb87614-dd11-48d7-a8df-51fc74d9fb94",
        "id_categorie": "categorie11",
        "id_repas": "7824bf18-5e96-4297-b754-041d3307105b",
        "name": "Sucre",
        "quantite": "90g",
        "rank": 1
      },
      "8be726b8-cf30-4c3b-aba2-c42a3d21c413": {
        "id": "8be726b8-cf30-4c3b-aba2-c42a3d21c413",
        "id_categorie": "categorie11",
        "id_repas": "fed07360-0c7c-441d-aa61-6cf6e742e298",
        "name": "Chocolat (pâte à cookie)",
        "quantite": "50g",
        "rank": 3
      },
      "8caae013-f0a9-4ebe-9e96-62472941c5f7": {
        "id": "8caae013-f0a9-4ebe-9e96-62472941c5f7",
        "id_categorie": "categorie9",
        "id_repas": "0dd7d87b-8539-400b-b4b2-c2a58ca68fec",
        "name": "Graine de chia",
        "quantite": "2 cas",
        "rank": 7
      },
      "8fb29c1d-3942-4339-a3e6-e1df46d4568f": {
        "id": "8fb29c1d-3942-4339-a3e6-e1df46d4568f",
        "id_categorie": "categorie1",
        "id_repas": "13a6e228-b415-4365-9972-314f036d109c",
        "name": "Champignons",
        "quantite": "4",
        "rank": 4
      },
      "8fb7640b-fc4e-46e4-a0a1-66af873c0e31": {
        "id": "8fb7640b-fc4e-46e4-a0a1-66af873c0e31",
        "id_categorie": "categorie11",
        "id_repas": "fed07360-0c7c-441d-aa61-6cf6e742e298",
        "name": "Cacao maigre",
        "quantite": "1 cac ",
        "rank": 18
      },
      "9020d05e-1a46-4800-8ca0-68fbba202130": {
        "id": "9020d05e-1a46-4800-8ca0-68fbba202130",
        "id_categorie": "categorie22",
        "id_repas": "e909b217-b4c7-4bd8-a3ce-3ed7cf0d539f",
        "name": "Raviole",
        "quantite": "1 sachet",
        "rank": 0
      },
      "90f42c72-e86b-47f7-8f42-d773bad5bfbf": {
        "id": "90f42c72-e86b-47f7-8f42-d773bad5bfbf",
        "id_categorie": "categorie12",
        "id_repas": "b06f4b84-32a6-43a9-86dc-c5da09dae484",
        "name": "Épices",
        "quantite": "1",
        "rank": 4
      },
      "912313d6-a918-4a7b-9214-a1f8d93701fb": {
        "id": "912313d6-a918-4a7b-9214-a1f8d93701fb",
        "id_categorie": "categorie1",
        "id_repas": "b51f01ef-8e7e-4731-8dce-02d527c1479f",
        "name": "Salade",
        "quantite": "1",
        "rank": 4
      },
      "916ac6e9-eba5-4cbf-bfcb-6f47e378f2c7": {
        "id": "916ac6e9-eba5-4cbf-bfcb-6f47e378f2c7",
        "id_categorie": "categorie3",
        "id_repas": "861728ce-f15d-42da-ba6e-a92d461c81fd",
        "name": "Fromage de chèvre (optionnel)",
        "quantite": "1",
        "rank": 4
      },
      "92216839-8bb1-4211-9ab6-a2df1baf5140": {
        "id": "92216839-8bb1-4211-9ab6-a2df1baf5140",
        "id_categorie": "categorie3",
        "id_repas": "a420e744-955d-483c-8d27-11b31b3cdc70",
        "name": "Blanc d'oeuf",
        "quantite": "1",
        "rank": 2
      },
      "925c6524-c05d-4d66-8e5a-dd62b91e9b4e": {
        "id": "925c6524-c05d-4d66-8e5a-dd62b91e9b4e",
        "id_categorie": "categorie14",
        "id_repas": "2c1360f5-68ac-413a-978e-bee1df461fd9",
        "name": "Glaçons (optionnel)",
        "quantite": "au jugé ",
        "rank": 7
      },
      "928031f5-8a69-46e6-8f09-c9d118025301": {
        "id": "928031f5-8a69-46e6-8f09-c9d118025301",
        "id_categorie": "categorie19",
        "id_repas": "2b3b7821-a351-4083-84e7-8891d95ffbc9",
        "name": "Parmesan",
        "quantite": "10g",
        "rank": 2
      },
      "93626aa0-a3b0-47cf-94df-2e52bdd1447d": {
        "id": "93626aa0-a3b0-47cf-94df-2e52bdd1447d",
        "id_categorie": "categorie4",
        "id_repas": "f82e4ed6-6404-4bc1-95b7-831a71e100cc",
        "name": "Oeufs",
        "quantite": "2",
        "rank": 3
      },
      "94457d47-3795-452a-85ff-8628f0a89ac6": {
        "id": "94457d47-3795-452a-85ff-8628f0a89ac6",
        "id_categorie": "categorie8",
        "id_repas": "b5d9c9e0-334e-4ce8-8889-c8bf4207d91c",
        "name": "Tomates pelées",
        "quantite": "400g",
        "rank": 5
      },
      "948314b8-8750-4f36-a026-d71cee4196f8": {
        "id": "948314b8-8750-4f36-a026-d71cee4196f8",
        "id_categorie": "categorie19",
        "id_repas": "da89093a-2b0e-4b27-b3d2-51acb43f92c8",
        "name": "Bûche de chèvre",
        "quantite": "1",
        "rank": 3
      },
      "94ef06f0-5a52-41a7-82aa-e4cf89d10436": {
        "id": "94ef06f0-5a52-41a7-82aa-e4cf89d10436",
        "id_categorie": "categorie3",
        "id_repas": "c558c46b-c6ba-4b51-b43c-6caf92914f10",
        "name": "Lait",
        "quantite": "200ml",
        "rank": 0
      },
      "95acbf03-3376-4ed4-92b2-859cd1c76742": {
        "id": "95acbf03-3376-4ed4-92b2-859cd1c76742",
        "id_categorie": "categorie7",
        "id_repas": "5689e797-6007-49e5-b3ee-0f6b79b16263",
        "name": "Semoule",
        "quantite": "150 g",
        "rank": 5
      },
      "96f24d1d-7aa5-448e-a276-e896e271fdec": {
        "id": "96f24d1d-7aa5-448e-a276-e896e271fdec",
        "id_categorie": "categorie7",
        "id_repas": "7d806748-f666-4db6-bcad-2f81fdec5374",
        "name": "Semoule épicé",
        "quantite": "150 g",
        "rank": 0
      },
      "973996d5-8ff3-4449-a154-a69c5c76c3b3": {
        "id": "973996d5-8ff3-4449-a154-a69c5c76c3b3",
        "id_categorie": "categorie1",
        "id_repas": "5588414e-a159-4c0f-bfb8-cc50a697ff6a",
        "name": "Oignon rouge",
        "quantite": "0,5",
        "rank": 6
      },
      "979590b6-e15f-41f6-ab74-803e7e1b6a20": {
        "id": "979590b6-e15f-41f6-ab74-803e7e1b6a20",
        "id_categorie": "categorie12",
        "id_repas": "a420e744-955d-483c-8d27-11b31b3cdc70",
        "name": "Épices selon nos goûts (optionnel)",
        "quantite": "1",
        "rank": 9
      },
      "97d52639-5631-4158-a34f-c0b6da1224f8": {
        "id": "97d52639-5631-4158-a34f-c0b6da1224f8",
        "id_categorie": "categorie13",
        "id_repas": "2c1360f5-68ac-413a-978e-bee1df461fd9",
        "name": "Liqueur orange ou pêche (optionnel)",
        "quantite": "15ml",
        "rank": 8
      },
      "984723bb-a27b-48c9-9cfa-741e4a0ad4fc": {
        "id": "984723bb-a27b-48c9-9cfa-741e4a0ad4fc",
        "id_categorie": "categorie11",
        "id_repas": "fed07360-0c7c-441d-aa61-6cf6e742e298",
        "name": "Levure (pâte à brownie)",
        "quantite": "1/2 sachet",
        "rank": 0
      },
      "9a035d8e-5e53-4857-ae8f-6bde86c12432": {
        "id": "9a035d8e-5e53-4857-ae8f-6bde86c12432",
        "id_categorie": "categorie12",
        "id_repas": "b06f4b84-32a6-43a9-86dc-c5da09dae484",
        "name": "Vinaigre de cidre",
        "quantite": "2 cas",
        "rank": 5
      },
      "9b453267-1901-4fea-891e-9475ce6c71af": {
        "id": "9b453267-1901-4fea-891e-9475ce6c71af",
        "id_categorie": "categorie12",
        "id_repas": "5588414e-a159-4c0f-bfb8-cc50a697ff6a",
        "name": "Cumin moulu",
        "quantite": "0,5 cac",
        "rank": 10
      },
      "9b489700-6bbd-4601-a8d5-9c5a07d9d53e": {
        "id": "9b489700-6bbd-4601-a8d5-9c5a07d9d53e",
        "id_categorie": "categorie12",
        "id_repas": "6871e45f-e3ca-4b07-9704-05023ca1ef47",
        "name": "Sauce soja",
        "quantite": "1 cas",
        "rank": 3
      },
      "9e7a90c8-5734-4aa5-8cc7-ba527abb70d5": {
        "id": "9e7a90c8-5734-4aa5-8cc7-ba527abb70d5",
        "id_categorie": "categorie1",
        "id_repas": "2c1360f5-68ac-413a-978e-bee1df461fd9",
        "name": "Citrons jaunes",
        "quantite": "2",
        "rank": 2
      },
      "9e96e815-0c9b-4fdc-a656-77848a9ad927": {
        "id": "9e96e815-0c9b-4fdc-a656-77848a9ad927",
        "id_categorie": "categorie12",
        "id_repas": "e8569d80-552d-4428-b3a9-d749e7df9baf",
        "name": "Sirop d'agave",
        "quantite": "100g",
        "rank": 5
      },
      "9f470a02-ef5b-4ecc-bc48-b4bc6d118ee9": {
        "id": "9f470a02-ef5b-4ecc-bc48-b4bc6d118ee9",
        "id_categorie": "categorie11",
        "id_repas": "5588414e-a159-4c0f-bfb8-cc50a697ff6a",
        "name": "Raisins secs",
        "quantite": "15g",
        "rank": 4
      },
      "a099d632-0023-4b1f-9341-57840fd905cc": {
        "id": "a099d632-0023-4b1f-9341-57840fd905cc",
        "id_categorie": "categorie12",
        "id_repas": "90a01e21-4491-453b-b003-370e6722dc4f",
        "name": "Sirop d'érable (optionnel)",
        "quantite": "1",
        "rank": 8
      },
      "a1057b2b-897d-4e90-91c0-cc81fa9e276c": {
        "id": "a1057b2b-897d-4e90-91c0-cc81fa9e276c",
        "id_categorie": "categorie12",
        "id_repas": "bc3c46d3-fce1-4dd9-a070-4dd14da12c3c",
        "name": "Jus de citron",
        "quantite": "au pif",
        "rank": 2
      },
      "a1ff1611-1f72-46a7-ac7b-2c4891cbb9f2": {
        "id": "a1ff1611-1f72-46a7-ac7b-2c4891cbb9f2",
        "id_categorie": "categorie12",
        "id_repas": "f8ab0c87-8712-42b7-b7ff-875f70a6681f",
        "name": "Pâte lasagne",
        "quantite": "1 boîte ",
        "rank": 11
      },
      "a309053a-8f23-4c98-9c19-ef6052c41012": {
        "id": "a309053a-8f23-4c98-9c19-ef6052c41012",
        "id_categorie": "categorie12",
        "id_repas": "b5d9c9e0-334e-4ce8-8889-c8bf4207d91c",
        "name": "Cumin",
        "quantite": "1/2 cac",
        "rank": 13
      },
      "a3dc9789-86e3-4cef-84cd-eb32688a3bc6": {
        "id": "a3dc9789-86e3-4cef-84cd-eb32688a3bc6",
        "id_categorie": "categorie12",
        "id_repas": "b5d9c9e0-334e-4ce8-8889-c8bf4207d91c",
        "name": "Paprika fumé",
        "quantite": "1/2 cac",
        "rank": 16
      },
      "a4b740ec-ff25-4c66-919f-7fcec84af903": {
        "id": "a4b740ec-ff25-4c66-919f-7fcec84af903",
        "id_categorie": "categorie12",
        "id_repas": "861728ce-f15d-42da-ba6e-a92d461c81fd",
        "name": "Herbe de provence",
        "quantite": "1",
        "rank": 5
      },
      "a4bfbb9c-bee3-4868-995b-07304df0d9fd": {
        "id": "a4bfbb9c-bee3-4868-995b-07304df0d9fd",
        "id_categorie": "categorie8",
        "id_repas": "82b8d7dd-6978-43e1-a0bc-bae4436b1e48",
        "name": "Lentilles cuite",
        "quantite": "150g",
        "rank": 1
      },
      "a5535bc6-030a-4d32-8b29-6115cc3a4011": {
        "id": "a5535bc6-030a-4d32-8b29-6115cc3a4011",
        "id_categorie": "categorie1",
        "id_repas": "f82e4ed6-6404-4bc1-95b7-831a71e100cc",
        "name": "Tomates (optionnel)",
        "quantite": "1",
        "rank": 8
      },
      "a57c98d4-fbb5-4aa1-94a9-9eae0666c2b8": {
        "id": "a57c98d4-fbb5-4aa1-94a9-9eae0666c2b8",
        "id_categorie": "categorie12",
        "id_repas": "0dd7d87b-8539-400b-b4b2-c2a58ca68fec",
        "name": "Jus de citron",
        "quantite": "2 cas",
        "rank": 3
      },
      "a857c569-5071-4610-bb71-a41bf3c77284": {
        "id": "a857c569-5071-4610-bb71-a41bf3c77284",
        "id_categorie": "categorie7",
        "id_repas": "5588414e-a159-4c0f-bfb8-cc50a697ff6a",
        "name": "Boulgour",
        "quantite": "90g",
        "rank": 2
      },
      "a8a5d28d-199e-425b-95e7-7a780998f219": {
        "id": "a8a5d28d-199e-425b-95e7-7a780998f219",
        "id_categorie": "categorie12",
        "id_repas": "2d5b555f-043b-4c73-8955-aed99046b841",
        "name": "Mayonnaise",
        "quantite": "au jugé",
        "rank": 6
      },
      "a92ca291-60e8-4399-b1c3-d29506bec122": {
        "id": "a92ca291-60e8-4399-b1c3-d29506bec122",
        "id_categorie": "categorie1",
        "id_repas": "b51f01ef-8e7e-4731-8dce-02d527c1479f",
        "name": "Maïs",
        "quantite": "1",
        "rank": 5
      },
      "a95232c1-b350-442e-a8c7-3e575f113619": {
        "id": "a95232c1-b350-442e-a8c7-3e575f113619",
        "id_categorie": "categorie8",
        "id_repas": "c558c46b-c6ba-4b51-b43c-6caf92914f10",
        "name": "Maïs",
        "quantite": "1",
        "rank": 2
      },
      "aa8a4dff-c6b1-4766-b74c-098b1cf97ed6": {
        "id": "aa8a4dff-c6b1-4766-b74c-098b1cf97ed6",
        "id_categorie": "categorie12",
        "id_repas": "2b3b7821-a351-4083-84e7-8891d95ffbc9",
        "name": "Ail (optionnel)",
        "quantite": "1",
        "rank": 4
      },
      "ab0272df-ac5e-4f51-b4b6-f34a9105beb1": {
        "id": "ab0272df-ac5e-4f51-b4b6-f34a9105beb1",
        "id_categorie": "categorie4",
        "id_repas": "3f5b18c5-cc67-4414-a572-8540a5a9082b",
        "name": "Jambon ou œuf",
        "quantite": "150g ou 2",
        "rank": 0
      },
      "ab2b6ef4-219b-48d6-b3ee-8e2dd2a45777": {
        "id": "ab2b6ef4-219b-48d6-b3ee-8e2dd2a45777",
        "id_categorie": "categorie12",
        "id_repas": "731b2a71-aa1c-42fb-a0bd-9fbc9a07f922",
        "name": "Herbe",
        "quantite": "2 cac",
        "rank": 8
      },
      "acc35bdb-ecbf-4a83-8c10-861465865352": {
        "id": "acc35bdb-ecbf-4a83-8c10-861465865352",
        "id_categorie": "categorie19",
        "id_repas": "c5730e8c-88b3-4ad7-b003-bc4a6313fc4b",
        "name": "Gruyère",
        "quantite": "au jugé ",
        "rank": 3
      },
      "ad576de7-3ef1-4c12-be6a-23238cfc2e4c": {
        "id": "ad576de7-3ef1-4c12-be6a-23238cfc2e4c",
        "id_categorie": "categorie11",
        "id_repas": "1fa6c7eb-eb37-47f0-b9c9-29492fd7a838",
        "name": "Beurre",
        "quantite": "60g",
        "rank": 3
      },
      "ae014617-635a-4def-b422-6baded3ef1bd": {
        "id": "ae014617-635a-4def-b422-6baded3ef1bd",
        "id_categorie": "categorie12",
        "id_repas": "a420e744-955d-483c-8d27-11b31b3cdc70",
        "name": "Sel (optionnel)",
        "quantite": "1",
        "rank": 7
      },
      "ae417fc0-4fbf-406b-8d34-9ae1cd153eee": {
        "id": "ae417fc0-4fbf-406b-8d34-9ae1cd153eee",
        "id_categorie": "categorie1",
        "id_repas": "8beb0db0-e785-4a7e-b6ea-d19353295cba",
        "name": "Olives noires",
        "quantite": "10",
        "rank": 5
      },
      "ae8fa9d9-6bee-46ba-97f1-769fb3818807": {
        "id": "ae8fa9d9-6bee-46ba-97f1-769fb3818807",
        "id_categorie": "categorie6",
        "id_repas": "861728ce-f15d-42da-ba6e-a92d461c81fd",
        "name": "Seitan",
        "quantite": "50g",
        "rank": 7
      },
      "af319895-fea2-4fd4-b531-205f3c52acca": {
        "id": "af319895-fea2-4fd4-b531-205f3c52acca",
        "id_categorie": "categorie9",
        "id_repas": "3f5b18c5-cc67-4414-a572-8540a5a9082b",
        "name": "Cerneaux de noix",
        "quantite": "20g",
        "rank": 7
      },
      "af5e3cc2-81f7-4d4c-8036-224e73c802a5": {
        "id": "af5e3cc2-81f7-4d4c-8036-224e73c802a5",
        "id_categorie": "categorie1",
        "id_repas": "9df0c46b-6251-498b-a1b7-2d1c1b344830",
        "name": "Avocat",
        "quantite": "1",
        "rank": 5
      },
      "af6af58b-063d-4045-804d-021b5dbd3e92": {
        "id": "af6af58b-063d-4045-804d-021b5dbd3e92",
        "id_categorie": "categorie12",
        "id_repas": "82b8d7dd-6978-43e1-a0bc-bae4436b1e48",
        "name": "Vinaigre de cidre",
        "quantite": "2 cas",
        "rank": 9
      },
      "b08e702d-5442-4d61-adee-1e52cd504ffe": {
        "id": "b08e702d-5442-4d61-adee-1e52cd504ffe",
        "id_categorie": "categorie4",
        "id_repas": "c363b713-2079-4b55-97f8-39f2da8fa340",
        "name": "Saucisses fumées",
        "quantite": "12",
        "rank": 0
      },
      "b0bfaf2d-e8c5-49b3-9f65-c95a797931c2": {
        "id": "b0bfaf2d-e8c5-49b3-9f65-c95a797931c2",
        "id_categorie": "categorie11",
        "id_repas": "f82e4ed6-6404-4bc1-95b7-831a71e100cc",
        "name": "Beurre",
        "quantite": "40g",
        "rank": 4
      },
      "b2a806aa-9e2c-4b20-b0b3-20043eb303bb": {
        "id": "b2a806aa-9e2c-4b20-b0b3-20043eb303bb",
        "id_categorie": "categorie1",
        "id_repas": "14d4c8fe-7a8e-4bb8-8cbd-64ab29d178aa",
        "name": "Zeste de citron",
        "quantite": "au jugé",
        "rank": 6
      },
      "b2d05565-7603-47e3-9350-4ef21b8ecfe0": {
        "id": "b2d05565-7603-47e3-9350-4ef21b8ecfe0",
        "id_categorie": "categorie11",
        "id_repas": "234de9de-e4d3-422a-8e3e-0d46d16e2e71",
        "name": "Farine",
        "quantite": "120g",
        "rank": 1
      },
      "b33b4e66-0d80-41e8-8b48-17ed25240431": {
        "id": "b33b4e66-0d80-41e8-8b48-17ed25240431",
        "id_categorie": "categorie11",
        "id_repas": "14d4c8fe-7a8e-4bb8-8cbd-64ab29d178aa",
        "name": "Sucre",
        "quantite": "110g",
        "rank": 3
      },
      "b47694bc-7217-45c4-9ca6-0c28da04c209": {
        "id": "b47694bc-7217-45c4-9ca6-0c28da04c209",
        "id_categorie": "categorie1",
        "id_repas": "7d806748-f666-4db6-bcad-2f81fdec5374",
        "name": "Concombre",
        "quantite": "1",
        "rank": 2
      },
      "b55d6ce1-a87d-4582-9c61-c7bc3d29085d": {
        "id": "b55d6ce1-a87d-4582-9c61-c7bc3d29085d",
        "id_categorie": "categorie12",
        "id_repas": "df0dcbc9-433a-4c6c-9b09-03d47b7b810e",
        "name": "Pesto (vert ou rouge)",
        "quantite": "1",
        "rank": 3
      },
      "b6536410-7bb3-4c7a-a832-ba28aa059fce": {
        "id": "b6536410-7bb3-4c7a-a832-ba28aa059fce",
        "id_categorie": "categorie17",
        "id_repas": "e9b03b98-4be7-454a-be98-4a3234baf3ec",
        "name": "Filtre a café",
        "quantite": "3",
        "rank": 4
      },
      "b6d7f50e-997e-44a3-ae74-8766e8664c57": {
        "id": "b6d7f50e-997e-44a3-ae74-8766e8664c57",
        "id_categorie": "categorie12",
        "id_repas": "f82e4ed6-6404-4bc1-95b7-831a71e100cc",
        "name": "Pesto rouge (optionnel)",
        "quantite": "au jugé",
        "rank": 11
      },
      "b6fd2a9f-1d03-4c2d-9b03-aee9c4b17e61": {
        "id": "b6fd2a9f-1d03-4c2d-9b03-aee9c4b17e61",
        "id_categorie": "categorie7",
        "id_repas": "731b2a71-aa1c-42fb-a0bd-9fbc9a07f922",
        "name": "Pâte semi complète",
        "quantite": "100g",
        "rank": 1
      },
      "b779da6e-3bc8-469b-98a8-7640d0c6c619": {
        "id": "b779da6e-3bc8-469b-98a8-7640d0c6c619",
        "id_categorie": "categorie12",
        "id_repas": "b5d9c9e0-334e-4ce8-8889-c8bf4207d91c",
        "name": "Huile",
        "quantite": "2 cas",
        "rank": 9
      },
      "b821e02d-4049-4c04-bfd8-c83c34b11f7d": {
        "id": "b821e02d-4049-4c04-bfd8-c83c34b11f7d",
        "id_categorie": "categorie8",
        "id_repas": "791978e3-839a-4bb6-b134-dcddf7200d99",
        "name": "Lentilles",
        "quantite": "1",
        "rank": 1
      },
      "b82ea540-4cd8-4afb-992f-3d8a662b123d": {
        "id": "b82ea540-4cd8-4afb-992f-3d8a662b123d",
        "id_categorie": "categorie12",
        "id_repas": "b5d9c9e0-334e-4ce8-8889-c8bf4207d91c",
        "name": "Canelle",
        "quantite": "1/2 cac",
        "rank": 14
      },
      "b88763bc-866a-4ae4-a0b0-f9befbeaa457": {
        "id": "b88763bc-866a-4ae4-a0b0-f9befbeaa457",
        "id_categorie": "categorie13",
        "id_repas": "2c1360f5-68ac-413a-978e-bee1df461fd9",
        "name": "Vin blanc bien frais",
        "quantite": "750ml",
        "rank": 0
      },
      "b891638c-94df-4f6a-a2af-0efa59a1c074": {
        "id": "b891638c-94df-4f6a-a2af-0efa59a1c074",
        "id_categorie": "categorie12",
        "id_repas": "6871e45f-e3ca-4b07-9704-05023ca1ef47",
        "name": "Moutarde (optionnel)",
        "quantite": "1 cac",
        "rank": 11
      },
      "b8b40ade-1136-4c14-94d5-5d1689d94377": {
        "id": "b8b40ade-1136-4c14-94d5-5d1689d94377",
        "id_categorie": "categorie4",
        "id_repas": "b51f01ef-8e7e-4731-8dce-02d527c1479f",
        "name": "Oeufs",
        "quantite": "2",
        "rank": 6
      },
      "b912af1b-8d00-4ab4-9cf5-c606a9b6e738": {
        "id": "b912af1b-8d00-4ab4-9cf5-c606a9b6e738",
        "id_categorie": "categorie1",
        "id_repas": "861728ce-f15d-42da-ba6e-a92d461c81fd",
        "name": "Aubergine (optionnel)",
        "quantite": "1",
        "rank": 6
      },
      "ba2f57f9-2c29-4afa-a173-a9e019744f41": {
        "id": "ba2f57f9-2c29-4afa-a173-a9e019744f41",
        "id_categorie": "categorie1",
        "id_repas": "82b8d7dd-6978-43e1-a0bc-bae4436b1e48",
        "name": "Carottes",
        "quantite": "2",
        "rank": 4
      },
      "bb2440ae-a178-419e-b2a7-089ba90e2d32": {
        "id": "bb2440ae-a178-419e-b2a7-089ba90e2d32",
        "id_categorie": "categorie12",
        "id_repas": "234de9de-e4d3-422a-8e3e-0d46d16e2e71",
        "name": "Huile de coco froide",
        "quantite": "80g",
        "rank": 2
      },
      "bbd22b44-ca3a-457a-9e8f-277384f54d2d": {
        "id": "bbd22b44-ca3a-457a-9e8f-277384f54d2d",
        "id_categorie": "categorie4",
        "id_repas": "ef1bd565-7bc7-4693-a1f1-54e24b2a99fd",
        "name": "Jambon",
        "quantite": "2 tranches",
        "rank": 1
      },
      "bc5e3ce2-817a-4f61-a6b1-7caecf97217d": {
        "id": "bc5e3ce2-817a-4f61-a6b1-7caecf97217d",
        "id_categorie": "categorie13",
        "id_repas": "7863c28f-5186-460d-95a6-f46fbfd1b6ba",
        "name": "Rhum blanc",
        "quantite": "1",
        "rank": 4
      },
      "bc95fa45-870c-4a9b-a8ed-d977129b13c1": {
        "id": "bc95fa45-870c-4a9b-a8ed-d977129b13c1",
        "id_categorie": "categorie7",
        "id_repas": "2fecc7f5-c4f5-44e7-9c4c-f34296ed5180",
        "name": "Feuilles de brick",
        "quantite": "10",
        "rank": 0
      },
      "bce8a03a-cab0-4156-9b17-2448e5dd3eda": {
        "id": "bce8a03a-cab0-4156-9b17-2448e5dd3eda",
        "id_categorie": "categorie12",
        "id_repas": "1fa6c7eb-eb37-47f0-b9c9-29492fd7a838",
        "name": "Sel",
        "quantite": "1 pincée ",
        "rank": 8
      },
      "bd518270-d0a1-448c-a8f0-bb167416346f": {
        "id": "bd518270-d0a1-448c-a8f0-bb167416346f",
        "id_categorie": "categorie9",
        "id_repas": "fed07360-0c7c-441d-aa61-6cf6e742e298",
        "name": "Noix concassées",
        "quantite": "50g",
        "rank": 12
      },
      "bd60f100-38ac-42c9-8b7a-11ebdd701f89": {
        "id": "bd60f100-38ac-42c9-8b7a-11ebdd701f89",
        "id_categorie": "categorie14",
        "id_repas": "a420e744-955d-483c-8d27-11b31b3cdc70",
        "name": "Eau (optionnel)",
        "quantite": "un peu",
        "rank": 3
      },
      "bd8eb228-4db3-4d82-a738-0d8bc047b6fc": {
        "id": "bd8eb228-4db3-4d82-a738-0d8bc047b6fc",
        "id_categorie": "categorie1",
        "id_repas": "5689e797-6007-49e5-b3ee-0f6b79b16263",
        "name": "Courgettes",
        "quantite": "2",
        "rank": 2
      },
      "bdc5e424-4975-4d6a-8277-3937968ed133": {
        "id": "bdc5e424-4975-4d6a-8277-3937968ed133",
        "id_categorie": "categorie6",
        "id_repas": "82b8d7dd-6978-43e1-a0bc-bae4436b1e48",
        "name": "Tofu aux herbes",
        "quantite": "200g",
        "rank": 0
      },
      "bde9e03e-3220-4227-9df2-4f5313e8cdaf": {
        "id": "bde9e03e-3220-4227-9df2-4f5313e8cdaf",
        "id_categorie": "categorie1",
        "id_repas": "7ddc8ac5-6c8a-44e4-9e3e-cad5bdd65594",
        "name": "Carottes",
        "quantite": "3",
        "rank": 0
      },
      "bea60a19-fd7b-4f6b-ba40-d8e010943f12": {
        "id": "bea60a19-fd7b-4f6b-ba40-d8e010943f12",
        "id_categorie": "categorie12",
        "id_repas": "5588414e-a159-4c0f-bfb8-cc50a697ff6a",
        "name": "Huile d'olive",
        "quantite": "0,5 cas",
        "rank": 12
      },
      "c054ba9d-a47b-41ed-9e2b-5e8817ceede2": {
        "id": "c054ba9d-a47b-41ed-9e2b-5e8817ceede2",
        "id_categorie": "categorie12",
        "id_repas": "731b2a71-aa1c-42fb-a0bd-9fbc9a07f922",
        "name": "Huile d'olive",
        "quantite": "1/2 cas",
        "rank": 10
      },
      "c12d154c-05c7-4f24-83c5-f996d0129ff1": {
        "id": "c12d154c-05c7-4f24-83c5-f996d0129ff1",
        "id_categorie": "categorie3",
        "id_repas": "25b4e9e2-2a92-43b1-8097-c02aca3e9425",
        "name": "Roquefort",
        "quantite": "1/2",
        "rank": 1
      },
      "c260a392-c518-4675-b479-76e46ddafd42": {
        "id": "c260a392-c518-4675-b479-76e46ddafd42",
        "id_categorie": "categorie5",
        "id_repas": "9df0c46b-6251-498b-a1b7-2d1c1b344830",
        "name": "Saumon",
        "quantite": "2",
        "rank": 0
      },
      "c2665f4e-653b-4f0b-9b71-ebabe68103aa": {
        "id": "c2665f4e-653b-4f0b-9b71-ebabe68103aa",
        "id_categorie": "categorie12",
        "id_repas": "f8ab0c87-8712-42b7-b7ff-875f70a6681f",
        "name": "Muscade râpée",
        "quantite": "au jugé ",
        "rank": 7
      },
      "c2a33146-972f-4a5c-b80e-f17b92309f0c": {
        "id": "c2a33146-972f-4a5c-b80e-f17b92309f0c",
        "id_categorie": "categorie8",
        "id_repas": "5689e797-6007-49e5-b3ee-0f6b79b16263",
        "name": "Pois chiches (optionnel)",
        "quantite": "1 boite",
        "rank": 4
      },
      "c3b3cd08-c84d-45f0-a1c8-d7c666526559": {
        "id": "c3b3cd08-c84d-45f0-a1c8-d7c666526559",
        "id_categorie": "categorie11",
        "id_repas": "f8ab0c87-8712-42b7-b7ff-875f70a6681f",
        "name": "Ail",
        "quantite": "1 gousse",
        "rank": 13
      },
      "c4bfd05e-eca0-4aef-85c3-20b9050e0016": {
        "id": "c4bfd05e-eca0-4aef-85c3-20b9050e0016",
        "id_categorie": "categorie11",
        "id_repas": "fed07360-0c7c-441d-aa61-6cf6e742e298",
        "name": "Sucre blanc",
        "quantite": "30g",
        "rank": 14
      },
      "c5985ff0-347a-47ff-a182-fe5a171851fa": {
        "id": "c5985ff0-347a-47ff-a182-fe5a171851fa",
        "id_categorie": "categorie11",
        "id_repas": "0dd7d87b-8539-400b-b4b2-c2a58ca68fec",
        "name": "Beurre",
        "quantite": "60g",
        "rank": 5
      },
      "c6d8c37f-4f22-4300-b126-87e2cb3d446b": {
        "id": "c6d8c37f-4f22-4300-b126-87e2cb3d446b",
        "id_categorie": "categorie3",
        "id_repas": "82b8d7dd-6978-43e1-a0bc-bae4436b1e48",
        "name": "Crème de soja",
        "quantite": "1 cas",
        "rank": 11
      },
      "c6e11527-d0cb-436a-b3fe-0d029244baa3": {
        "id": "c6e11527-d0cb-436a-b3fe-0d029244baa3",
        "id_categorie": "categorie1",
        "id_repas": "c363b713-2079-4b55-97f8-39f2da8fa340",
        "name": "Gousses d'ail",
        "quantite": "2",
        "rank": 5
      },
      "c7d848cf-3c1e-4e66-a299-cb21c884dcc4": {
        "id": "c7d848cf-3c1e-4e66-a299-cb21c884dcc4",
        "id_categorie": "categorie1",
        "id_repas": "6871e45f-e3ca-4b07-9704-05023ca1ef47",
        "name": "Oignon",
        "quantite": "1",
        "rank": 6
      },
      "c7d949d0-a5cc-434e-8a16-5ec413e5e8de": {
        "id": "c7d949d0-a5cc-434e-8a16-5ec413e5e8de",
        "id_categorie": "categorie1",
        "id_repas": "acccc58b-016e-41e1-9666-5b2edb4e416c",
        "name": "Oignon nouveau (optionnel)",
        "quantite": "1",
        "rank": 7
      },
      "c94400f8-64cd-4f08-b64c-d52f2de80e70": {
        "id": "c94400f8-64cd-4f08-b64c-d52f2de80e70",
        "id_categorie": "categorie4",
        "id_repas": "2b3b7821-a351-4083-84e7-8891d95ffbc9",
        "name": "Oeufs",
        "quantite": "1",
        "rank": 3
      },
      "c9860f51-30a7-4edd-9ccc-8d4bd0af57b8": {
        "id": "c9860f51-30a7-4edd-9ccc-8d4bd0af57b8",
        "id_categorie": "categorie21",
        "id_repas": "9df0c46b-6251-498b-a1b7-2d1c1b344830",
        "name": "Pain bagel",
        "quantite": "2",
        "rank": 4
      },
      "c9f7cb10-35cc-4f75-b598-645d4577b3cc": {
        "id": "c9f7cb10-35cc-4f75-b598-645d4577b3cc",
        "id_categorie": "categorie1",
        "id_repas": "731b2a71-aa1c-42fb-a0bd-9fbc9a07f922",
        "name": "Gousse d'ail",
        "quantite": "1",
        "rank": 7
      },
      "cac509d2-d131-4d9d-8c3c-c804f74726af": {
        "id": "cac509d2-d131-4d9d-8c3c-c804f74726af",
        "id_categorie": "categorie1",
        "id_repas": "5588414e-a159-4c0f-bfb8-cc50a697ff6a",
        "name": "Concombre",
        "quantite": "0,5",
        "rank": 5
      },
      "cae8c59b-5d83-48ba-a7ce-27668603e981": {
        "id": "cae8c59b-5d83-48ba-a7ce-27668603e981",
        "id_categorie": "categorie1",
        "id_repas": "7863c28f-5186-460d-95a6-f46fbfd1b6ba",
        "name": "Menthe",
        "quantite": "1",
        "rank": 5
      },
      "ccc7289b-a36a-4506-9283-dc7f6eca5a0b": {
        "id": "ccc7289b-a36a-4506-9283-dc7f6eca5a0b",
        "id_categorie": "categorie7",
        "id_repas": "f8ab0c87-8712-42b7-b7ff-875f70a6681f",
        "name": "Pâte lasagne",
        "quantite": "1 boîte ",
        "rank": 0
      },
      "ccff977e-0f5d-43d0-a45d-13eebbf6b59d": {
        "id": "ccff977e-0f5d-43d0-a45d-13eebbf6b59d",
        "id_categorie": "categorie12",
        "id_repas": "b5d9c9e0-334e-4ce8-8889-c8bf4207d91c",
        "name": "Curcuma",
        "quantite": "1/2 cac",
        "rank": 15
      },
      "cd5e1459-aae4-4c3f-bc55-01f2660f143b": {
        "id": "cd5e1459-aae4-4c3f-bc55-01f2660f143b",
        "id_categorie": "categorie11",
        "id_repas": "14d4c8fe-7a8e-4bb8-8cbd-64ab29d178aa",
        "name": "Farine",
        "quantite": "180g",
        "rank": 4
      },
      "cd6e9535-8e63-4617-9b6e-45a6456a0205": {
        "id": "cd6e9535-8e63-4617-9b6e-45a6456a0205",
        "id_categorie": "categorie19",
        "id_repas": "df0dcbc9-433a-4c6c-9b09-03d47b7b810e",
        "name": "Avocat (optionnel)",
        "quantite": "1",
        "rank": 7
      },
      "cdd1f87d-115c-4d3d-ac8a-d1d4013ccecb": {
        "id": "cdd1f87d-115c-4d3d-ac8a-d1d4013ccecb",
        "id_categorie": "categorie12",
        "id_repas": "861728ce-f15d-42da-ba6e-a92d461c81fd",
        "name": "Sauce tomate (optionnel)",
        "quantite": "1",
        "rank": 3
      },
      "ce204e94-5ed9-4ac7-bd9c-2bc93058e63e": {
        "id": "ce204e94-5ed9-4ac7-bd9c-2bc93058e63e",
        "id_categorie": "categorie4",
        "id_repas": "2fecc7f5-c4f5-44e7-9c4c-f34296ed5180",
        "name": "Poulet",
        "quantite": "200g",
        "rank": 1
      },
      "ce51feb4-9db2-46f4-991c-4e53cc3ebab7": {
        "id": "ce51feb4-9db2-46f4-991c-4e53cc3ebab7",
        "id_categorie": "categorie11",
        "id_repas": "234de9de-e4d3-422a-8e3e-0d46d16e2e71",
        "name": "Extrait de vanile",
        "quantite": "1 cac",
        "rank": 9
      },
      "cffc0c02-494f-48be-8f6b-729971ba9a07": {
        "id": "cffc0c02-494f-48be-8f6b-729971ba9a07",
        "id_categorie": "categorie3",
        "id_repas": "f8ab0c87-8712-42b7-b7ff-875f70a6681f",
        "name": "Carottes",
        "quantite": "1",
        "rank": 14
      },
      "d03d44b9-052e-4096-bf0a-f62793083aa9": {
        "id": "d03d44b9-052e-4096-bf0a-f62793083aa9",
        "id_categorie": "categorie7",
        "id_repas": "77be6bdb-ffb1-4615-a27d-760620b9077b",
        "name": "Galette de riz",
        "quantite": "1 paquet",
        "rank": 0
      },
      "d165e03e-a8c7-49c3-a1db-0592f26d674d": {
        "id": "d165e03e-a8c7-49c3-a1db-0592f26d674d",
        "id_categorie": "categorie7",
        "id_repas": "218fd948-a38d-4bf5-a395-e94228c8e51d",
        "name": "Riz",
        "quantite": "150g",
        "rank": 0
      },
      "d1780a4e-65ea-482d-84d5-24bc30ec159b": {
        "id": "d1780a4e-65ea-482d-84d5-24bc30ec159b",
        "id_categorie": "categorie11",
        "id_repas": "f82e4ed6-6404-4bc1-95b7-831a71e100cc",
        "name": "Sucre",
        "quantite": "40g",
        "rank": 5
      },
      "d305c23f-1e8b-4830-8f54-e2a47d6df485": {
        "id": "d305c23f-1e8b-4830-8f54-e2a47d6df485",
        "id_categorie": "categorie3",
        "id_repas": "c5730e8c-88b3-4ad7-b003-bc4a6313fc4b",
        "name": "Crème fraîche",
        "quantite": "au jugé ",
        "rank": 0
      },
      "d37eecd8-1d40-48e1-9812-c8a46feaff7c": {
        "id": "d37eecd8-1d40-48e1-9812-c8a46feaff7c",
        "id_categorie": "categorie1",
        "id_repas": "5689e797-6007-49e5-b3ee-0f6b79b16263",
        "name": "Tomates",
        "quantite": "2",
        "rank": 0
      },
      "d3da7c85-c96e-45c5-b6d2-1f1d6d9bade4": {
        "id": "d3da7c85-c96e-45c5-b6d2-1f1d6d9bade4",
        "id_categorie": "categorie21",
        "id_repas": "8beb0db0-e785-4a7e-b6ea-d19353295cba",
        "name": "Pains pita",
        "quantite": "3",
        "rank": 0
      },
      "d41d94ee-4c55-4725-ac81-c9f7582644e7": {
        "id": "d41d94ee-4c55-4725-ac81-c9f7582644e7",
        "id_categorie": "categorie12",
        "id_repas": "3f5b18c5-cc67-4414-a572-8540a5a9082b",
        "name": "Vinaigre balsamique",
        "quantite": "2 cas ",
        "rank": 11
      },
      "d4e31247-6562-455b-939e-f6490de3d355": {
        "id": "d4e31247-6562-455b-939e-f6490de3d355",
        "id_categorie": "categorie3",
        "id_repas": "acb9bef5-a446-4315-aa2f-ba10dcffc603",
        "name": "Cheddar",
        "quantite": "1 sachet",
        "rank": 2
      },
      "d4fb81b9-38aa-4fd4-8786-89ed97a1a9c4": {
        "id": "d4fb81b9-38aa-4fd4-8786-89ed97a1a9c4",
        "id_categorie": "categorie4",
        "id_repas": "7824bf18-5e96-4297-b754-041d3307105b",
        "name": "Oeufs",
        "quantite": "3",
        "rank": 2
      },
      "d537647d-d855-4a35-987c-e1866e8cdb4d": {
        "id": "d537647d-d855-4a35-987c-e1866e8cdb4d",
        "id_categorie": "categorie8",
        "id_repas": "b06f4b84-32a6-43a9-86dc-c5da09dae484",
        "name": "Lentilles",
        "quantite": "400g",
        "rank": 0
      },
      "d6d5f3ca-e70e-4b20-abbb-b45859b687cb": {
        "id": "d6d5f3ca-e70e-4b20-abbb-b45859b687cb",
        "id_categorie": "categorie11",
        "id_repas": "1fa6c7eb-eb37-47f0-b9c9-29492fd7a838",
        "name": "Sucre",
        "quantite": "80g",
        "rank": 2
      },
      "d7c358c9-8db3-4ed8-82f7-49847f3bc436": {
        "id": "d7c358c9-8db3-4ed8-82f7-49847f3bc436",
        "id_categorie": "categorie11",
        "id_repas": "74513f16-2c62-4017-9320-a854f703b59f",
        "name": "Sucre",
        "quantite": "80g",
        "rank": 2
      },
      "d82bea41-deb7-45d1-9148-83dfbdb01fc0": {
        "id": "d82bea41-deb7-45d1-9148-83dfbdb01fc0",
        "id_categorie": "categorie11",
        "id_repas": "791978e3-839a-4bb6-b134-dcddf7200d99",
        "name": "Raisins secs (optionnel)",
        "quantite": "25g",
        "rank": 2
      },
      "d8faefb4-69ce-499d-9615-1678f25f72aa": {
        "id": "d8faefb4-69ce-499d-9615-1678f25f72aa",
        "id_categorie": "categorie1",
        "id_repas": "25b4e9e2-2a92-43b1-8097-c02aca3e9425",
        "name": "Oignon",
        "quantite": "1",
        "rank": 2
      },
      "d90367ff-1482-47bb-b863-4a104337d7ce": {
        "id": "d90367ff-1482-47bb-b863-4a104337d7ce",
        "id_categorie": "categorie12",
        "id_repas": "5588414e-a159-4c0f-bfb8-cc50a697ff6a",
        "name": "Coriandre",
        "quantite": "7g",
        "rank": 7
      },
      "d92250bf-e6ce-4ac0-8f19-716a0be820a6": {
        "id": "d92250bf-e6ce-4ac0-8f19-716a0be820a6",
        "id_categorie": "categorie21",
        "id_repas": "b51f01ef-8e7e-4731-8dce-02d527c1479f",
        "name": "Wraps",
        "quantite": "4",
        "rank": 7
      },
      "d94149c5-9ec0-4b65-860f-5d73f6bb534c": {
        "id": "d94149c5-9ec0-4b65-860f-5d73f6bb534c",
        "id_categorie": "categorie12",
        "id_repas": "0dd7d87b-8539-400b-b4b2-c2a58ca68fec",
        "name": "Sel",
        "quantite": "1 cac",
        "rank": 11
      },
      "da7f659c-695e-4890-af62-53f566423996": {
        "id": "da7f659c-695e-4890-af62-53f566423996",
        "id_categorie": "categorie17",
        "id_repas": "e9b03b98-4be7-454a-be98-4a3234baf3ec",
        "name": "Fleur de génépi",
        "quantite": "2 paquets",
        "rank": 0
      },
      "dad8bb53-379c-4314-a908-62310cf8ca9f": {
        "id": "dad8bb53-379c-4314-a908-62310cf8ca9f",
        "id_categorie": "categorie1",
        "id_repas": "13a6e228-b415-4365-9972-314f036d109c",
        "name": "Aubergine (optionnel)",
        "quantite": "1",
        "rank": 7
      },
      "db2a2e8c-a91d-4f3a-8f99-f6315c227add": {
        "id": "db2a2e8c-a91d-4f3a-8f99-f6315c227add",
        "id_categorie": "categorie14",
        "id_repas": "ee7e1e07-b9c5-4e7a-b60e-a9252a3eb6e6",
        "name": "Eau",
        "quantite": "200g",
        "rank": 1
      },
      "dccc7f99-ee45-4165-877c-f348e6828423": {
        "id": "dccc7f99-ee45-4165-877c-f348e6828423",
        "id_categorie": "categorie12",
        "id_repas": "218fd948-a38d-4bf5-a395-e94228c8e51d",
        "name": "Algues (optionnel)",
        "quantite": "1",
        "rank": 5
      },
      "dd485798-1d5f-4441-93e2-7b3f9605ad09": {
        "id": "dd485798-1d5f-4441-93e2-7b3f9605ad09",
        "id_categorie": "categorie11",
        "id_repas": "234de9de-e4d3-422a-8e3e-0d46d16e2e71",
        "name": "Sucre",
        "quantite": "60g",
        "rank": 3
      },
      "ddcf5100-8573-4415-a1eb-cbef13f4ad8e": {
        "id": "ddcf5100-8573-4415-a1eb-cbef13f4ad8e",
        "id_categorie": "categorie1",
        "id_repas": "3f5b18c5-cc67-4414-a572-8540a5a9082b",
        "name": "Jeune pousse",
        "quantite": "100g",
        "rank": 4
      },
      "de1df997-0367-4f83-a913-9eec29badacf": {
        "id": "de1df997-0367-4f83-a913-9eec29badacf",
        "id_categorie": "categorie1",
        "id_repas": "df0dcbc9-433a-4c6c-9b09-03d47b7b810e",
        "name": "Tomates",
        "quantite": "1",
        "rank": 1
      },
      "dec8ffd5-06ba-4289-9254-d7f2d9fcfb45": {
        "id": "dec8ffd5-06ba-4289-9254-d7f2d9fcfb45",
        "id_categorie": "categorie3",
        "id_repas": "6871e45f-e3ca-4b07-9704-05023ca1ef47",
        "name": "Crème liquide",
        "quantite": "2 cas",
        "rank": 10
      },
      "df2d694b-fd15-4d67-988c-2a36ca4df52b": {
        "id": "df2d694b-fd15-4d67-988c-2a36ca4df52b",
        "id_categorie": "categorie4",
        "id_repas": "731b2a71-aa1c-42fb-a0bd-9fbc9a07f922",
        "name": "Jambon",
        "quantite": "150g",
        "rank": 0
      },
      "dfcb1386-3aab-4026-8738-179e3b49d035": {
        "id": "dfcb1386-3aab-4026-8738-179e3b49d035",
        "id_categorie": "categorie13",
        "id_repas": "6871e45f-e3ca-4b07-9704-05023ca1ef47",
        "name": "Vin blanc (optionnel)",
        "quantite": "2 cas",
        "rank": 9
      },
      "dfde7299-aec5-4e74-bf5a-971703ac15d6": {
        "id": "dfde7299-aec5-4e74-bf5a-971703ac15d6",
        "id_categorie": "categorie12",
        "id_repas": "a420e744-955d-483c-8d27-11b31b3cdc70",
        "name": "Sauce soja",
        "quantite": "3",
        "rank": 6
      },
      "e00d11cf-6c9c-488c-8d2e-a4bbe5c68655": {
        "id": "e00d11cf-6c9c-488c-8d2e-a4bbe5c68655",
        "id_categorie": "categorie12",
        "id_repas": "3cd6f535-6bdd-48cf-9b1f-6d343347473a",
        "name": "Cube kubor",
        "quantite": "1",
        "rank": 4
      },
      "e0839f76-2493-4462-bce3-bfd389f41f34": {
        "id": "e0839f76-2493-4462-bce3-bfd389f41f34",
        "id_categorie": "categorie7",
        "id_repas": "82b8d7dd-6978-43e1-a0bc-bae4436b1e48",
        "name": "Quinoa",
        "quantite": "100g",
        "rank": 2
      },
      "e0aa23f8-c55d-4ece-b206-dc16f243cedb": {
        "id": "e0aa23f8-c55d-4ece-b206-dc16f243cedb",
        "id_categorie": "categorie4",
        "id_repas": "14d4c8fe-7a8e-4bb8-8cbd-64ab29d178aa",
        "name": "Oeufs",
        "quantite": "3",
        "rank": 1
      },
      "e0e82b02-ca51-4222-b1e3-9573c01e97c4": {
        "id": "e0e82b02-ca51-4222-b1e3-9573c01e97c4",
        "id_categorie": "categorie3",
        "id_repas": "1fa6c7eb-eb37-47f0-b9c9-29492fd7a838",
        "name": "Lait d'amande",
        "quantite": "100ml",
        "rank": 0
      },
      "e1a2c15b-e8f3-44d7-bc6f-7f1362003b43": {
        "id": "e1a2c15b-e8f3-44d7-bc6f-7f1362003b43",
        "id_categorie": "categorie3",
        "id_repas": "731b2a71-aa1c-42fb-a0bd-9fbc9a07f922",
        "name": "Crème",
        "quantite": "50ml",
        "rank": 3
      },
      "e28fe102-f1f7-4cac-aaa6-5c060c6de8cd": {
        "id": "e28fe102-f1f7-4cac-aaa6-5c060c6de8cd",
        "id_categorie": "categorie11",
        "id_repas": "5588414e-a159-4c0f-bfb8-cc50a697ff6a",
        "name": "Noix de coco râpée",
        "quantite": "0,5 cas",
        "rank": 9
      },
      "e2f7b1a4-1ed8-427a-8e8c-a1e52073c4fd": {
        "id": "e2f7b1a4-1ed8-427a-8e8c-a1e52073c4fd",
        "id_categorie": "categorie6",
        "id_repas": "77be6bdb-ffb1-4615-a27d-760620b9077b",
        "name": "Seitan",
        "quantite": "1 paquet",
        "rank": 6
      },
      "e35358fa-6e68-4bc1-939c-adbe58dc46ee": {
        "id": "e35358fa-6e68-4bc1-939c-adbe58dc46ee",
        "id_categorie": "categorie6",
        "id_repas": "f8ab0c87-8712-42b7-b7ff-875f70a6681f",
        "name": "Haché végétal",
        "quantite": "240g",
        "rank": 4
      },
      "e35e09ce-4898-4a76-9c7d-340be42bfb82": {
        "id": "e35e09ce-4898-4a76-9c7d-340be42bfb82",
        "id_categorie": "categorie3",
        "id_repas": "56b025f6-91af-4d15-92dc-b791c6d63933",
        "name": "Yaourt nature",
        "quantite": "1",
        "rank": 0
      },
      "e3669c07-1944-49c4-b4e7-96b1c9c250df": {
        "id": "e3669c07-1944-49c4-b4e7-96b1c9c250df",
        "id_categorie": "categorie3",
        "id_repas": "b06f4b84-32a6-43a9-86dc-c5da09dae484",
        "name": "Crème végétale",
        "quantite": "1 cas",
        "rank": 7
      },
      "e3974cca-3aea-41d1-9c76-9d50b0a643dd": {
        "id": "e3974cca-3aea-41d1-9c76-9d50b0a643dd",
        "id_categorie": "categorie12",
        "id_repas": "6871e45f-e3ca-4b07-9704-05023ca1ef47",
        "name": "Sirop d'érable",
        "quantite": "1 cas",
        "rank": 4
      },
      "e39ffdc2-a1be-49dc-97a6-50a634555e6d": {
        "id": "e39ffdc2-a1be-49dc-97a6-50a634555e6d",
        "id_categorie": "categorie11",
        "id_repas": "7824bf18-5e96-4297-b754-041d3307105b",
        "name": "Beurre",
        "quantite": "40g",
        "rank": 4
      },
      "e444ffad-8e86-4d80-814e-0a7416a9a19a": {
        "id": "e444ffad-8e86-4d80-814e-0a7416a9a19a",
        "id_categorie": "categorie4",
        "id_repas": "f82e4ed6-6404-4bc1-95b7-831a71e100cc",
        "name": "Oeufs (optionnel)",
        "quantite": "2 ",
        "rank": 12
      },
      "e50bfb10-972a-48af-8c4e-e5f386e88430": {
        "id": "e50bfb10-972a-48af-8c4e-e5f386e88430",
        "id_categorie": "categorie1",
        "id_repas": "b5d9c9e0-334e-4ce8-8889-c8bf4207d91c",
        "name": "Ail",
        "quantite": "4 gousses",
        "rank": 1
      },
      "e5b6317e-71e8-4ff1-8d47-2c224697d447": {
        "id": "e5b6317e-71e8-4ff1-8d47-2c224697d447",
        "id_categorie": "categorie3",
        "id_repas": "b5d9c9e0-334e-4ce8-8889-c8bf4207d91c",
        "name": "Crème végétale",
        "quantite": "25ml",
        "rank": 7
      },
      "e695a56a-7071-49b3-887f-cc3cf079f1d3": {
        "id": "e695a56a-7071-49b3-887f-cc3cf079f1d3",
        "id_categorie": "categorie4",
        "id_repas": "8beb0db0-e785-4a7e-b6ea-d19353295cba",
        "name": "Jambon",
        "quantite": "3 tranches ",
        "rank": 2
      },
      "e6cc13ee-26a9-4681-ae48-9a35644cc072": {
        "id": "e6cc13ee-26a9-4681-ae48-9a35644cc072",
        "id_categorie": "categorie12",
        "id_repas": "b5d9c9e0-334e-4ce8-8889-c8bf4207d91c",
        "name": "Coriandre",
        "quantite": "1 botte",
        "rank": 8
      },
      "e9837318-19ce-4702-bf1b-d5e39ce33b90": {
        "id": "e9837318-19ce-4702-bf1b-d5e39ce33b90",
        "id_categorie": "categorie1",
        "id_repas": "90a01e21-4491-453b-b003-370e6722dc4f",
        "name": "Champignon",
        "quantite": "4",
        "rank": 9
      },
      "e9e6bfca-e181-4b37-acba-5e803062f4ec": {
        "id": "e9e6bfca-e181-4b37-acba-5e803062f4ec",
        "id_categorie": "categorie14",
        "id_repas": "53597371-9ff4-4b63-b1cf-71163246e032",
        "name": "Jus d'ananas",
        "quantite": "2 dose",
        "rank": 0
      },
      "ea22eb4e-2ab2-4ddf-b53b-256882841496": {
        "id": "ea22eb4e-2ab2-4ddf-b53b-256882841496",
        "id_categorie": "categorie1",
        "id_repas": "8beb0db0-e785-4a7e-b6ea-d19353295cba",
        "name": "Roquette",
        "quantite": "1",
        "rank": 6
      },
      "ea27fce4-3ba4-4c89-8302-055786eecec5": {
        "id": "ea27fce4-3ba4-4c89-8302-055786eecec5",
        "id_categorie": "categorie8",
        "id_repas": "5588414e-a159-4c0f-bfb8-cc50a697ff6a",
        "name": "Pois chiches égouttés",
        "quantite": "50g",
        "rank": 1
      },
      "ea356d09-18be-4a8d-b0dd-2ea39e3cafd0": {
        "id": "ea356d09-18be-4a8d-b0dd-2ea39e3cafd0",
        "id_categorie": "categorie1",
        "id_repas": "df0dcbc9-433a-4c6c-9b09-03d47b7b810e",
        "name": "Salade",
        "quantite": "1",
        "rank": 2
      },
      "eb124748-3ddf-4607-baeb-4d12d2dea058": {
        "id": "eb124748-3ddf-4607-baeb-4d12d2dea058",
        "id_categorie": "categorie12",
        "id_repas": "b5d9c9e0-334e-4ce8-8889-c8bf4207d91c",
        "name": "Sel poivre (optionnel)",
        "quantite": "au jugé ",
        "rank": 17
      },
      "eb2a6414-cc80-442c-9dae-32fcb85e4187": {
        "id": "eb2a6414-cc80-442c-9dae-32fcb85e4187",
        "id_categorie": "categorie9",
        "id_repas": "fed07360-0c7c-441d-aa61-6cf6e742e298",
        "name": "Graine de lins mixées",
        "quantite": "20g",
        "rank": 15
      },
      "eba968b0-f533-4dfd-8454-3ea3506a08f3": {
        "id": "eba968b0-f533-4dfd-8454-3ea3506a08f3",
        "id_categorie": "categorie2",
        "id_repas": "2c1360f5-68ac-413a-978e-bee1df461fd9",
        "name": "Fruits rouges",
        "quantite": "150g",
        "rank": 4
      },
      "ec45ea4d-7075-4a54-b03f-66692fdcd90b": {
        "id": "ec45ea4d-7075-4a54-b03f-66692fdcd90b",
        "id_categorie": "categorie11",
        "id_repas": "1fa6c7eb-eb37-47f0-b9c9-29492fd7a838",
        "name": "Chocolat noir",
        "quantite": "120g",
        "rank": 1
      },
      "ec566f04-704f-4e00-9a2d-649931fbe3ee": {
        "id": "ec566f04-704f-4e00-9a2d-649931fbe3ee",
        "id_categorie": "categorie11",
        "id_repas": "0dd7d87b-8539-400b-b4b2-c2a58ca68fec",
        "name": "Vanille",
        "quantite": "1 cac",
        "rank": 10
      },
      "ec767b06-4633-4f26-b5be-407547531ed2": {
        "id": "ec767b06-4633-4f26-b5be-407547531ed2",
        "id_categorie": "categorie1",
        "id_repas": "2d5b555f-043b-4c73-8955-aed99046b841",
        "name": "Concombre",
        "quantite": "1",
        "rank": 3
      },
      "ecdddbed-2472-433c-a306-d850580f12eb": {
        "id": "ecdddbed-2472-433c-a306-d850580f12eb",
        "id_categorie": "categorie13",
        "id_repas": "e8569d80-552d-4428-b3a9-d749e7df9baf",
        "name": "Vodka",
        "quantite": "500ml",
        "rank": 1
      },
      "ee46b780-80cb-4db6-a1d1-a3b60c05f880": {
        "id": "ee46b780-80cb-4db6-a1d1-a3b60c05f880",
        "id_categorie": "categorie19",
        "id_repas": "3f5b18c5-cc67-4414-a572-8540a5a9082b",
        "name": "Feta ou chevre",
        "quantite": "40g",
        "rank": 6
      },
      "ee5a0a94-1ee7-44a9-a0e3-bd13d7f90054": {
        "id": "ee5a0a94-1ee7-44a9-a0e3-bd13d7f90054",
        "id_categorie": "categorie1",
        "id_repas": "9df0c46b-6251-498b-a1b7-2d1c1b344830",
        "name": "Salade",
        "quantite": "1",
        "rank": 3
      },
      "eefee0a5-209b-4a88-9815-930d90422fcf": {
        "id": "eefee0a5-209b-4a88-9815-930d90422fcf",
        "id_categorie": "categorie12",
        "id_repas": "2c1360f5-68ac-413a-978e-bee1df461fd9",
        "name": "Bâtons de cannelle (optionnel)",
        "quantite": "au jugé ",
        "rank": 10
      },
      "f0f77d96-9678-4e2d-9d8a-207b3cefdbf1": {
        "id": "f0f77d96-9678-4e2d-9d8a-207b3cefdbf1",
        "id_categorie": "categorie1",
        "id_repas": "218fd948-a38d-4bf5-a395-e94228c8e51d",
        "name": "Avocat",
        "quantite": "1",
        "rank": 2
      },
      "f2cfd016-b4d1-46b4-b171-753f3f225e7e": {
        "id": "f2cfd016-b4d1-46b4-b171-753f3f225e7e",
        "id_categorie": "categorie1",
        "id_repas": "7ddc8ac5-6c8a-44e4-9e3e-cad5bdd65594",
        "name": "Poti marron (optionnel)",
        "quantite": "1",
        "rank": 3
      },
      "f32849a5-a5d3-4e6b-8839-89da9d0b9be1": {
        "id": "f32849a5-a5d3-4e6b-8839-89da9d0b9be1",
        "id_categorie": "categorie1",
        "id_repas": "f8ab0c87-8712-42b7-b7ff-875f70a6681f",
        "name": "Ail",
        "quantite": "1 gousse",
        "rank": 2
      },
      "f334bff0-6c84-47cf-af8d-c4fb9ab278fe": {
        "id": "f334bff0-6c84-47cf-af8d-c4fb9ab278fe",
        "id_categorie": "categorie14",
        "id_repas": "3f5b18c5-cc67-4414-a572-8540a5a9082b",
        "name": "Eau",
        "quantite": "1 cas",
        "rank": 13
      },
      "f38aa552-2ba1-4d5d-b3a4-0f3e6f7fd1ca": {
        "id": "f38aa552-2ba1-4d5d-b3a4-0f3e6f7fd1ca",
        "id_categorie": "categorie12",
        "id_repas": "3f5b18c5-cc67-4414-a572-8540a5a9082b",
        "name": "Huile de noix ou olive",
        "quantite": "2 cas",
        "rank": 10
      },
      "f4d9e7c0-8cab-464f-b4fa-dc9cd56db70b": {
        "id": "f4d9e7c0-8cab-464f-b4fa-dc9cd56db70b",
        "id_categorie": "categorie7",
        "id_repas": "c5730e8c-88b3-4ad7-b003-bc4a6313fc4b",
        "name": "Pâtes",
        "quantite": "au jugé ",
        "rank": 2
      },
      "f4e8037f-13ee-4082-98a1-0318749a80d9": {
        "id": "f4e8037f-13ee-4082-98a1-0318749a80d9",
        "id_categorie": "categorie9",
        "id_repas": "82b8d7dd-6978-43e1-a0bc-bae4436b1e48",
        "name": "Graines de courges/ tournesol",
        "quantite": "20g",
        "rank": 7
      },
      "f51607ea-0b09-4cb3-84a0-62749a2c141a": {
        "id": "f51607ea-0b09-4cb3-84a0-62749a2c141a",
        "id_categorie": "categorie5",
        "id_repas": "218fd948-a38d-4bf5-a395-e94228c8e51d",
        "name": "Saumon",
        "quantite": "2 pavés",
        "rank": 1
      },
      "f578beb3-2b6b-4c87-9452-0760a1e35526": {
        "id": "f578beb3-2b6b-4c87-9452-0760a1e35526",
        "id_categorie": "categorie1",
        "id_repas": "13a6e228-b415-4365-9972-314f036d109c",
        "name": "Tomates",
        "quantite": "4",
        "rank": 3
      },
      "f5b62315-e4b4-4db8-8cbb-31f892d97268": {
        "id": "f5b62315-e4b4-4db8-8cbb-31f892d97268",
        "id_categorie": "categorie12",
        "id_repas": "77be6bdb-ffb1-4615-a27d-760620b9077b",
        "name": "Sauce soja",
        "quantite": "au jugé",
        "rank": 5
      },
      "f5b86a0a-0b7e-46aa-818e-9ec2a780a903": {
        "id": "f5b86a0a-0b7e-46aa-818e-9ec2a780a903",
        "id_categorie": "categorie1",
        "id_repas": "861728ce-f15d-42da-ba6e-a92d461c81fd",
        "name": "Tomates",
        "quantite": "1",
        "rank": 1
      },
      "f5d363de-209d-4e0e-b313-bda61db927ea": {
        "id": "f5d363de-209d-4e0e-b313-bda61db927ea",
        "id_categorie": "categorie1",
        "id_repas": "6871e45f-e3ca-4b07-9704-05023ca1ef47",
        "name": "Gousse d'ail",
        "quantite": "1",
        "rank": 7
      },
      "f5ddcf88-58ea-42f0-94d1-16ab31f49701": {
        "id": "f5ddcf88-58ea-42f0-94d1-16ab31f49701",
        "id_categorie": "categorie12",
        "id_repas": "b06f4b84-32a6-43a9-86dc-c5da09dae484",
        "name": "Moutarde",
        "quantite": "1 cas",
        "rank": 8
      },
      "f6206651-e34e-490b-a9ac-66990a9c106d": {
        "id": "f6206651-e34e-490b-a9ac-66990a9c106d",
        "id_categorie": "categorie11",
        "id_repas": "0dd7d87b-8539-400b-b4b2-c2a58ca68fec",
        "name": "Sucre",
        "quantite": "120g",
        "rank": 4
      },
      "f7b485e7-830b-4826-9652-562c87972068": {
        "id": "f7b485e7-830b-4826-9652-562c87972068",
        "id_categorie": "categorie19",
        "id_repas": "df0dcbc9-433a-4c6c-9b09-03d47b7b810e",
        "name": "Chèvre (optionnel)",
        "quantite": "1",
        "rank": 5
      },
      "f98d4ab9-50f4-4b5f-823c-cad10fe72a99": {
        "id": "f98d4ab9-50f4-4b5f-823c-cad10fe72a99",
        "id_categorie": "categorie3",
        "id_repas": "c5730e8c-88b3-4ad7-b003-bc4a6313fc4b",
        "name": "Oeuf (optionnel)",
        "quantite": "1",
        "rank": 4
      },
      "f9bb3e89-4dba-4997-9a87-0426f5b519e7": {
        "id": "f9bb3e89-4dba-4997-9a87-0426f5b519e7",
        "id_categorie": "categorie9",
        "id_repas": "234de9de-e4d3-422a-8e3e-0d46d16e2e71",
        "name": "Graine de chia",
        "quantite": "1 cas",
        "rank": 7
      },
      "fab9e0ae-ab60-4047-8815-1cbfd760320b": {
        "id": "fab9e0ae-ab60-4047-8815-1cbfd760320b",
        "id_categorie": "categorie11",
        "id_repas": "fed07360-0c7c-441d-aa61-6cf6e742e298",
        "name": "Sucre de canne (pâte à brownie)",
        "quantite": "40g",
        "rank": 13
      },
      "fab9e99e-7ffb-4e91-a674-ec0e67b608ed": {
        "id": "fab9e99e-7ffb-4e91-a674-ec0e67b608ed",
        "id_categorie": "categorie12",
        "id_repas": "a420e744-955d-483c-8d27-11b31b3cdc70",
        "name": "Poivre (optionnel)",
        "quantite": "1",
        "rank": 8
      },
      "fb91c345-faa7-4eb0-a534-9fe22d266370": {
        "id": "fb91c345-faa7-4eb0-a534-9fe22d266370",
        "id_categorie": "categorie13",
        "id_repas": "731b2a71-aa1c-42fb-a0bd-9fbc9a07f922",
        "name": "Vin blanc",
        "quantite": "10ml",
        "rank": 5
      },
      "fbdb83c8-e4a6-4fe8-a7bb-3e5a1eb89e62": {
        "id": "fbdb83c8-e4a6-4fe8-a7bb-3e5a1eb89e62",
        "id_categorie": "categorie3",
        "id_repas": "25b4e9e2-2a92-43b1-8097-c02aca3e9425",
        "name": "Crème fraîche",
        "quantite": "20cl",
        "rank": 3
      },
      "fbecbf6c-b3b7-4599-b4ea-e3c6e6654d23": {
        "id": "fbecbf6c-b3b7-4599-b4ea-e3c6e6654d23",
        "id_categorie": "categorie1",
        "id_repas": "77be6bdb-ffb1-4615-a27d-760620b9077b",
        "name": "Carottes",
        "quantite": "1",
        "rank": 2
      },
      "fc2d4f47-a85d-4620-aabe-786785024f99": {
        "id": "fc2d4f47-a85d-4620-aabe-786785024f99",
        "id_categorie": "categorie9",
        "id_repas": "218fd948-a38d-4bf5-a395-e94228c8e51d",
        "name": "Graine de sésame (optionnel)",
        "quantite": "10g",
        "rank": 4
      },
      "fd967b0e-0e3d-4fdb-a834-13aefba1816a": {
        "id": "fd967b0e-0e3d-4fdb-a834-13aefba1816a",
        "id_categorie": "categorie14",
        "id_repas": "82b8d7dd-6978-43e1-a0bc-bae4436b1e48",
        "name": "Eau",
        "quantite": "1 cas",
        "rank": 10
      },
      "fe515cb8-54c9-4a0e-815a-64c10cf76130": {
        "id": "fe515cb8-54c9-4a0e-815a-64c10cf76130",
        "id_categorie": "categorie4",
        "id_repas": "b51f01ef-8e7e-4731-8dce-02d527c1479f",
        "name": "Poulet",
        "quantite": "120g",
        "rank": 8
      },
      "feb2864d-d108-42ed-b3ff-9b82a3890600": {
        "id": "feb2864d-d108-42ed-b3ff-9b82a3890600",
        "id_categorie": "categorie11",
        "id_repas": "14d4c8fe-7a8e-4bb8-8cbd-64ab29d178aa",
        "name": "Éclat de chocolat",
        "quantite": "200 g",
        "rank": 0
      },
      "ff802f7b-6d95-4b00-a600-b2e799a14dad": {
        "id": "ff802f7b-6d95-4b00-a600-b2e799a14dad",
        "id_categorie": "categorie12",
        "id_repas": "b5d9c9e0-334e-4ce8-8889-c8bf4207d91c",
        "name": "Gingembre",
        "quantite": "1 cac",
        "rank": 12
      }
    },
    "repas": {
      "0dd7d87b-8539-400b-b4b2-c2a58ca68fec": {
        "description": "Recette de sissy",
        "duree": "35min",
        "id": "0dd7d87b-8539-400b-b4b2-c2a58ca68fec",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image0dd7d87b-8539-400b-b4b2-c2a58ca68fec?alt=media&token=0d72632c-ab65-490d-b850-f3b490c0b20b",
        "name": "Banana bread",
        "quantite": "12 muffins",
        "recette": "Préchauffer le four à 180°. Écraser à la fourchette (ou mixer au robot) les bananes épluchées, y ajouter les graines de chia, mélanger et réserver pour que les graines gonflent. Dans un autre récipient, mélanger le lait et le jus de citron, réserver.\n\nDans un saladier, verser tous les ingrédients secs : la farine, le sucre, la levure et le bicarbonate, la cannelle, la vanille et le sel. Mélanger rapidement et ajouter la banane écrasée, le lait, le beurre de cacahuète, la margarine fondue. Bien mélanger le tout au fouet ou à la cuillère. La texture doit rester légèrement compacte et non liquide.\n\nHuiler 12 moules à muffins, verser environ 2 bonnes cuillères à soupe de pâte par moule. Enfourner pour 25 min environ. C'est prêt ! À déguster tel quel ou tartiné de beurre de cacahuète, sirop d'érable, confiture...\n\nNB: Si vous utilisez un moule à cake traditionnel, comptez 45 à 50min pour la cuisson (surveillez des 40min avec la pointe d'un couteau).",
        "tags": [
          "Dessert",
          "Healthy"
        ]
      },
      "13a6e228-b415-4365-9972-314f036d109c": {
        "description": "Aux légumes",
        "duree": "1h30",
        "id": "13a6e228-b415-4365-9972-314f036d109c",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image13a6e228-b415-4365-9972-314f036d109c?alt=media&token=fa3fcf62-82f2-4df6-b923-ce0bef6c4b1f",
        "name": "Légumes farcis",
        "quantite": "2 personnes",
        "recette": "Couper tous les légumes, les faire cuire en mode ratatouille.\n\nlaisser certains légumes entier(mais creuser) pour être farcis \n\najouter les épices et sauce tomate si besoin\n\nen parallèle faire cuire le riz (1 portion de riz = 2 d'eau)\n\nune fois cuit, mettre le riz dans la poêle, mélanger. rajouter de la sauce tomate au besoin.\n\nmettre le mélange dans les légumes a farcir. saupoudrez de fromage.\n\nmettre eau four a 200° pendant 45min.\n\nmiam miam !\n",
        "tags": [
          "Végétarien",
          "Plat"
        ]
      },
      "14d4c8fe-7a8e-4bb8-8cbd-64ab29d178aa": {
        "description": "Recette de chez atelierduchocolat",
        "duree": "45 min",
        "id": "14d4c8fe-7a8e-4bb8-8cbd-64ab29d178aa",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image14d4c8fe-7a8e-4bb8-8cbd-64ab29d178aa?alt=media&token=1d5a6144-2e6f-4683-bc04-1e8a0c28f166",
        "name": "Madeleine au coeur de chocolat au lait citron",
        "quantite": "2 personnes",
        "recette": "1 : préchauffer le four à 240°\n\n2 : mélanger 120g de beurre pommade avec le sucre, incorporer les oeufs un par un, puis du zeste de citron\n\n3 : ajouter la farine et la levure chimique, et bien mélanger pour obtenir une pâte lisse\n\n4 : beurrer et fariner les moules à madeleines et les remplir au 2/3 de pâte\n\n5 : placer un éclat de chocolat au lait citron au centre de chaque madeleine\n\n6 : enfourner pendant 10min en baissant le four à 200°\n\n7 : démouler les madeleines et les laisser refroidir\n\n8 : faire fondre le chocolat restant au bain marie, avec le pinceau : étaler le chocolat sur la moitié de chaque madeleine et laisser figer.\n\ndégustez ❤️",
        "tags": [
          "Dessert"
        ]
      },
      "1fa6c7eb-eb37-47f0-b9c9-29492fd7a838": {
        "description": "Recette de sissy",
        "duree": "22 min",
        "id": "1fa6c7eb-eb37-47f0-b9c9-29492fd7a838",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image1fa6c7eb-eb37-47f0-b9c9-29492fd7a838?alt=media&token=c8cb8e1d-7878-4a1c-b042-73cebd89c7f6",
        "name": "Fondant au chocolat ",
        "quantite": "2 personnes",
        "recette": "• préchauffer le four à 200°.\n\n. dans un bol, verser les ingrédients secs : la farine, le sucre, le cacao, le piment et le sel. faire fondre le chocolat au micro-ondes, par tranche de 30 secondes. ajouter la margarine avant que tout le chocolat soit totalement fondu. mélanger et remettre rapidement au micro-ondes si besoin. déposer le chocolat dans un saladier, ajouter le lait. préparer l'expresso ou les 30 ml d'eau chaude avec la càc de café instantané. l'ajouter au chocolat fondu et mélanger à la spatule.\n\najouter les ingrédients secs en deux fois, et mélanger au fouet.\n\n• pour éviter que le fondant accroche au fond au moment du démoulage, il vaut mieux découper un cercle de papier cuisson et le déposer au fond du moule, puis le huiler légèrement, ainsi que les bords du moule. verser ensuite la préparation dans chaque moule, et garder un peu de pâte pour recouvrir les carrés de chocolat. déposer un carré de chocolat au centre de chaque moule, l'enfoncer légèrement puis recouvrir avec le restant de pâte. (le papier cuisson n'est pas nécessaire pour une dégustation directement dans le moule individuel).\n\n• pour la cuisson : soit, mettre les fondants crus au réfrigérateur puis les faire cuire à 200° environ 11-13 min au moment du repas. soit cuire les fondants directement, environ 9-10 min pour un mi-cuit, ou 10-12 min pour un fondant plus cuit. la durée dépend de la puissance du four, surveiller dès 9 min. si le dessus tremble encore quand on fait bouger le moule, alors le fondant manque d'un peu de cuisson, le laisser jusqu'à ce qu'il ne tremblote plus (cela va très vite !).\n\nune fois cuits, laisser les fondants refroidir légèrement avant de les démouler (5 à 10 min). c'est prêt ! démouler délicatement sur une assiette, ajouter les toppings de son choix, saupoudrer d'une pincée de piment si désiré, puis déguster.",
        "tags": [
          "Dessert"
        ]
      },
      "218fd948-a38d-4bf5-a395-e94228c8e51d": {
        "description": "Un sushi déstructurer",
        "duree": "30min",
        "id": "218fd948-a38d-4bf5-a395-e94228c8e51d",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image218fd948-a38d-4bf5-a395-e94228c8e51d?alt=media&token=55b47d3e-f2b1-4152-97bb-b1ba6f94082c",
        "name": "Sushi bowl",
        "quantite": "2 personnes",
        "recette": "Faire cuire le riz (1riz = 2 eau)\ntout mélanger dans un bowl!\n",
        "tags": [
          "Plat"
        ]
      },
      "234de9de-e4d3-422a-8e3e-0d46d16e2e71": {
        "description": "Cookie avoine choco de sissy",
        "duree": "45min",
        "id": "234de9de-e4d3-422a-8e3e-0d46d16e2e71",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image234de9de-e4d3-422a-8e3e-0d46d16e2e71?alt=media&token=835d90b6-bc7f-4396-a366-bbd2e3688334",
        "name": "Cookie \"healthy\"",
        "quantite": "2 personnes",
        "recette": "Pour 12 cookies:\npréchauffer le four à 180°. faire tremper les graines de lin dans l'eau et réserver. mixer les flocons d'avoine.\n\ndans un saladier, verser tous les ingrédients sauf le chocolat et l'huile de coco. mélanger à la cuillère. ajouter ensuite l'huile de coco froide et bien malaxer la pâte, à la cuillère ou à la main. ajouter ensuite les pépites de chocolat, et mélanger grossièrement.\n\nvérifier la texture de la pâte en faisant une boule, si elle s'effrite, ajouter 1 càs d'eau à la pâte et remélanger. façonner ensuite avec le reste des boules, les déposer sur une plaque anti adhésive, les aplatir légèrement avec la main ou le dos de la cuillère et enfourner pour 15 min. c'est prêt !\n\nn.b : il est conseillé de moudre les graines de lin pour profiter de tous leurs bienfaits, avec un mortier ou un moulin à café, par exemple. les cookies se gardent 3 jours dans une boîte en métal.",
        "tags": [
          "Dessert",
          "Vegan",
          "Goûter"
        ]
      },
      "25b4e9e2-2a92-43b1-8097-c02aca3e9425": {
        "description": "Vite fait bien fait",
        "duree": "45min",
        "id": "25b4e9e2-2a92-43b1-8097-c02aca3e9425",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image25b4e9e2-2a92-43b1-8097-c02aca3e9425?alt=media&token=7337f7cd-7240-424e-a7e7-0f1707081324",
        "name": "Samoussas roquefort butternut",
        "quantite": "2 personnes",
        "recette": "Faire revenir le butternut et l'oignon dans de l'huile et les épices. retirez toute l'eau\n\n\nle réduire en purée avec la crème et le yaourt\n\ncouper le roquefort en dés et remuer le tout.\n\nfaçonnez les samoussas.\n\nfaire cuire au four ou a la poêle avec de l'huile.\n\n au four c'est meilleur : 200° 10min-15min",
        "tags": [
          "Plat",
          "Apéro"
        ]
      },
      "2b3b7821-a351-4083-84e7-8891d95ffbc9": {
        "description": "Des petites nuggets veggie",
        "duree": "55 min",
        "id": "2b3b7821-a351-4083-84e7-8891d95ffbc9",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image2b3b7821-a351-4083-84e7-8891d95ffbc9?alt=media&token=bb908ae8-66e5-49d1-8f83-98ad8776bfaf",
        "name": "Galette de brocolis",
        "quantite": "2 personnes",
        "recette": "Coupez le brocoli en morceaux et faire cuire 5min dans de l'eau bouillante salée. \n\négoutter et hacher.\n\nhachez l'ail\n\nbattez les oeufs dans un saladier\n\ntout mélanger, et réalisez les galettes.\n\nenfournez pour 25min a 200°",
        "tags": [
          "Végétarien",
          "Apéro",
          "Plat"
        ]
      },
      "2c1360f5-68ac-413a-978e-bee1df461fd9": {
        "description": "Recette de sissy",
        "duree": "15min",
        "id": "2c1360f5-68ac-413a-978e-bee1df461fd9",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image2c1360f5-68ac-413a-978e-bee1df461fd9?alt=media&token=1373c5e7-3c22-4bd4-9a74-fff9dd51570f",
        "name": "Sangria blanche",
        "quantite": "7 verres de 220ml",
        "recette": "Préparer les fruits :\n\nLaver les fruits et la menthe. Couper les fruits en rondelles ou quartiers, et si besoin, les fruits rouges en deux (pour des fraises notamment).\n\nPréparer la sangria :\n\nDans le récipient choisi, déposer les fruits. Frotter la menthe entre ses mains, la mettre dans le récipient, puis écraser le tout au pilon. Verser le vin blanc et le soft. Ajouter les ingrédients facultatifs, mélanger. Réserver au frais.\n\nC'est prêt !\n\nGoûter et ajouter du sucre si désiré. Ajouter les glaçons avant de servir.",
        "tags": [
          "Cocktail"
        ]
      },
      "2d5b555f-043b-4c73-8955-aed99046b841": {
        "description": "Recette de mamy ❤️",
        "duree": "15min",
        "id": "2d5b555f-043b-4c73-8955-aed99046b841",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image2d5b555f-043b-4c73-8955-aed99046b841?alt=media&token=8e1f25a6-ea97-45be-9703-2e4d613844c8",
        "name": "Salade de pâtes",
        "quantite": "2 personnes",
        "recette": "1 : faire cuire les pâtes\n\n2 : tout coupé en dés ou en petit eheh\n\n3 : mélanger le tout avec huile vinaigre et une cuillère à soupe de mayonnaise",
        "tags": [
          "Plat"
        ]
      },
      "2fecc7f5-c4f5-44e7-9c4c-f34296ed5180": {
        "description": "Recette de dalleuse pour l'apéro",
        "duree": "45 min",
        "id": "2fecc7f5-c4f5-44e7-9c4c-f34296ed5180",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image2fecc7f5-c4f5-44e7-9c4c-f34296ed5180?alt=media&token=8740ee13-1792-4748-8ea8-ba03e714e34a",
        "name": "Samoussas poulet carottes",
        "quantite": "2-3 personnes",
        "recette": "Couper le poulet et les carottes en petite morceaux.\n\nfaire cuire le poulet et les carottes.\nsalez la préparation\nplier les samoussas\n\ncuire au four ou à la poêle.\n\nau four c'est meilleur : 200° 10min",
        "tags": [
          "Plat",
          "Apéro"
        ]
      },
      "32980464-61d5-4912-afc3-eb4b120dfd7a": {
        "description": "Kro kro bon",
        "duree": "45 min",
        "id": "32980464-61d5-4912-afc3-eb4b120dfd7a",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image32980464-61d5-4912-afc3-eb4b120dfd7a?alt=media&token=9bc1bd06-d118-42e7-bf13-3ee1b814c1e3",
        "name": "Frites patates douces",
        "quantite": "2 personnes",
        "recette": "Couper les frites\nMettre à chauffer pendant 30 min à 200 degrés !\nMettre de l assaisonnement/huile (si volonté)",
        "tags": [
          "Plat",
          "Végétarien"
        ]
      },
      "3cd6f535-6bdd-48cf-9b1f-6d343347473a": {
        "description": "Le plat de la flemme",
        "duree": "25min",
        "id": "3cd6f535-6bdd-48cf-9b1f-6d343347473a",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image3cd6f535-6bdd-48cf-9b1f-6d343347473a?alt=media&token=4b1b375c-997e-4371-917f-dc05c6009ebf",
        "name": "Risotto crevettes",
        "quantite": "2 personnes",
        "recette": "Tout mettre dans l'autocuiseur et ne rien faire :)",
        "tags": [
          "Plat",
          "Facile"
        ]
      },
      "3f5b18c5-cc67-4414-a572-8540a5a9082b": {
        "description": "Recette de sissy",
        "duree": "20min",
        "id": "3f5b18c5-cc67-4414-a572-8540a5a9082b",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image3f5b18c5-cc67-4414-a572-8540a5a9082b?alt=media&token=eb68a037-e253-481a-bc2b-e67f28870f50",
        "name": "Salade proteinée feta fraise",
        "quantite": "2 personnes",
        "recette": "1. Préparer les protéines (soit faire cuire les œufs 6 minutes pour des œufs mollets ou 9 minutes pour des œeufs durs - soit couper le jambon en lamelles).\n\n2. Préparer la vinaigrette dans un bocal hermétique, verser tous les ingrédients, fermer, secouer. Goûter et rectifier l'assaisonnement si nécessaire (sirop d'agave, vinaigre notamment). Réserver.\n\n3. Faire griller les tranches de pain.\n\n4. Préparer les crudités: laver tous les fruits et légumes. Équeuter et trancher les fraises. Couper les radis en quatre, et couper le concombre en tranches.\n\n5. Préparer la salade: dans un saladier, déposer les jeunes pousses, les crudités, les protéines, les fraises. Émietter la feta par-dessus. Ajouter les noix concassées et la menthe. Saler, poivrer (attention la feta étant déjà salée, ne pas trop en mettre !). Servir avec la vinaigrette et le pain à disposition.",
        "tags": [
          "Végétarien",
          "Plat",
          "Entrée",
          "Salade"
        ]
      },
      "53597371-9ff4-4b63-b1cf-71163246e032": {
        "description": "Recette de thibault",
        "duree": "5min",
        "id": "53597371-9ff4-4b63-b1cf-71163246e032",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image53597371-9ff4-4b63-b1cf-71163246e032?alt=media&token=cf878853-4bca-41fb-80c5-1552651c9ad6",
        "name": "Shot de madeleine",
        "quantite": "1 personne ",
        "recette": "Tout mettre dans un shaker et mélanger.\n\nPour le cocktail allonger avec du jus d'ananas peu sucré.",
        "tags": [
          "Cocktail"
        ]
      },
      "5588414e-a159-4c0f-bfb8-cc50a697ff6a": {
        "description": "Recette de sissy",
        "duree": "25min",
        "id": "5588414e-a159-4c0f-bfb8-cc50a697ff6a",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image5588414e-a159-4c0f-bfb8-cc50a697ff6a?alt=media&token=bd31aa33-3493-4a8f-9a82-c59e0f12276e",
        "name": "Boulgour protéiné à l'indienne",
        "quantite": "2 personnes",
        "recette": "1 Emincer l'oignon rouge le piment, la coriandre  émietter le tofu ou couper en morceaux les filets  de poulet Couper le concombre, soit en tranches soit en lamelles\n\n2 Dans un wok ou une grande poêle, faire chauffer la cas d'huile sur feu fort. Faire revenir le tofu ou le poulet (ajouter le curcuma au poulet, le cumin moulu, la sauce pimentée pendant 2 min tout en remuant. Ajouter ensuite l'oignon, les pois chiches et cuire pendant 7 min à feu moyen\n\nPendant ce temps  assembler rapidement la sauce dans un bol, verser la base sauce blanche, la coriandre finement taillée et la purés de sésame, mélanger et  réserver au frais\n\nDons wok, ajouter  ensuite le boulgour et les raisins secs, la noix de coco râpée et le jus de citron. Mélanger rapidement puis verser l'eau par dessus. Saler, poivrer, et laisser cuire à feu doux pendant 7 min environ, jusqu'à ce que toute l'eau soit absorbée. Ajouter la coriandre émincée, mélanger.\nServir avec les lamelles de concombre et la sauce blanche bien fraiche.",
        "tags": [
          "Végétarien",
          "Plat"
        ]
      },
      "5689e797-6007-49e5-b3ee-0f6b79b16263": {
        "description": "Flash",
        "duree": "25 min",
        "id": "5689e797-6007-49e5-b3ee-0f6b79b16263",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image5689e797-6007-49e5-b3ee-0f6b79b16263?alt=media&token=b7e44eee-db8f-45c4-856d-025fb550e4ed",
        "name": "Couscous rapide",
        "quantite": "2 personnes",
        "recette": "Couper et faire cuire les légumes et voilà",
        "tags": [
          "Plat"
        ]
      },
      "56b025f6-91af-4d15-92dc-b791c6d63933": {
        "description": "Recette de sissy",
        "duree": "3min",
        "id": "56b025f6-91af-4d15-92dc-b791c6d63933",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image56b025f6-91af-4d15-92dc-b791c6d63933?alt=media&token=eded0e78-fdc7-488a-a1cf-8915b1acbf1d",
        "name": "Sauce blanche",
        "quantite": "3 portions ",
        "recette": "Au moment du repas, prendre la quantité de base sauce blanche indiquée et bien vérifier s'il y a des ingrédients à ajouter dedans selon les recettes. Exemple recette de la patate douce à l'orientale : Ajout de purée de sésame dans le base de sauce blanche.\n\n(Se conserve 5 jours)",
        "tags": [
          "Sauce",
          "Plat",
          "Apéro"
        ]
      },
      "6871e45f-e3ca-4b07-9704-05023ca1ef47": {
        "description": "Recette de sissy",
        "duree": "35 min",
        "id": "6871e45f-e3ca-4b07-9704-05023ca1ef47",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image6871e45f-e3ca-4b07-9704-05023ca1ef47?alt=media&token=a1ba1ea8-551e-4ca3-90db-0326a3c1c265",
        "name": "Quinoa protéine à la forestière",
        "quantite": "2 personnes ",
        "recette": "• Commencer par le quinoa: compter un volume de quinoa sec pour deux volumes d'eau. Bien rincer le quinoa plusieurs fois à l'eau, jusqu'à ce que l'eau soit claire. Le faire cuire avec un cube de bouillon, pendant 10 à 15 min à feu doux. Goûter pour vérifier la cuisson.\n\n• Préparer les légumes et la protéine: pendant la cuisson du quinoa, émincer la protéine (pour le seitan en fines tranches d'environ 5 mm d'épaisseur. Pour du poulet, en aiguillettes) puis préparer un petit bol avec la sauce soja et le sirop pour la cuisson. Émincer les champignons, l'oignon et l'ail.\n\n. Cuire les champignons dans une grande poêle, faire chauffer une càs d'huile sur feu moyen/fort. Faire revenir l'oignon pendant 2 min. Ajouter ensuite les champignons, cuire à feu vif 1 min. Ajouter le vin blanc, remuer pour décoller les sucs puis baisser le feu. Laisser cuire environ 5 min à feu moyen, en remuant assez souvent.\n\n• Ensuite, ajouter le persil et l'ail. Continuer la cuisson, les champignons sont cuits quand toute leur eau s'est évaporée. Poivrer, ajouter la càc de moutarde, la crème et mélanger. Goûter et rectifier l'assaisonnement si nécessaire. Réserver au chaud. Si la sauce est devenue épaisse au moment de servir, ne pas hésiter à ajouter un peu d'eau et à la refaire chauffer rapidement.\n\n• Cuire la protéine : faire chauffer 1 càs d'huile dans une poêle, à feu moyen. Ajouter la protéine et la faire revenir environ 4 min par face (ou plus pour le poulet). Quand elle est bien colorée, verser la sauce, enrober la protéine avec, couper le feu. C'est prêt !\n\n. Dans une assiette, déposer d'abord le quinoa, puis les champignons d'un côté, et la protéine caramélisée de l'autre. Si désiré, parsemer de persil ciselé.",
        "tags": [
          "Végétarien",
          "Plat",
          "Healthy"
        ]
      },
      "731b2a71-aa1c-42fb-a0bd-9fbc9a07f922": {
        "description": "Aux courgettes",
        "duree": "21 min",
        "id": "731b2a71-aa1c-42fb-a0bd-9fbc9a07f922",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image731b2a71-aa1c-42fb-a0bd-9fbc9a07f922?alt=media&token=a26ffd56-6cd7-4ff7-bd3f-0186642e6115",
        "name": "Pâte carbo légère",
        "quantite": "2 portions",
        "recette": "1. Commencer par lancer la cuisson des pâtes al dente en suivant les instructions du paquet, puis les réserver.\n\n2. Préparer les autres ingrédients : émincer l'ail et l'oignon. Couper les courgettes en cubes. Couper le jambon en lamelles. Préparer le verre de vin blanc.\n\n3. Faire chauffer la càs d'huile dans une casserole, à feu moyen/ fort. Y faire revenir l'ail, l'oignon, les lamelles de jambon et laisser cuire 5 min. Déglacer avec le vin blanc.\n\n4. Baisser un peu le feu, ajouter la courgette, couvrir et laisser cuire jusqu'à ce que les courgettes soient cuites, compter environ 8 min. En fin de cuisson, retirer le couvercle, ajouter la crème, les herbes, le parmesan et du poivre. Remuer, goûter et rectifier l'assaisonnement si nécessaire (ajouter du sel notamment). Ajouter les pâtes cuites et mélanger. Si la crème a épaissi, ajouter un peu d'eau. À déguster chaud.",
        "tags": [
          "Plat",
          "Végétarien"
        ]
      },
      "74513f16-2c62-4017-9320-a854f703b59f": {
        "description": "Recette de corée",
        "duree": "15min",
        "id": "74513f16-2c62-4017-9320-a854f703b59f",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image74513f16-2c62-4017-9320-a854f703b59f?alt=media&token=1aea8e7f-f3e5-4570-8ed0-e207a6ec012f",
        "name": "Gâteau fondant au chocolat express",
        "quantite": "6 personnes",
        "recette": "1. Cassez le chocolat en morceaux, coupez le beurre en morceaux et mettez le tout à fondre 2 minutes au micro-ondes.\n\n2. Ajoutez les oeufs, le sucre la farine et battez le tout 5minutes au fouet électrique.\n\n3. Cuisez le mélange dans un moule type « Flexipan » ou dans un moule recouvert de papier d'aluminium. 8-9' min a 200°C",
        "tags": [
          "Dessert",
          "Rapide"
        ]
      },
      "77be6bdb-ffb1-4615-a27d-760620b9077b": {
        "description": "Spring rolls",
        "duree": "20 min",
        "id": "77be6bdb-ffb1-4615-a27d-760620b9077b",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image77be6bdb-ffb1-4615-a27d-760620b9077b?alt=media&token=cce76054-1309-4aad-ab67-8b5de21d9450",
        "name": "Rouleau de printemps",
        "quantite": "2 personnes",
        "recette": "Faire trempé le seitan dans la sauce\nCuire les nouilles\nTout couper et se faire plaiz\nHumidifié les feuilles de riz et façonner son rouleau\n",
        "tags": [
          "Plat",
          "Végétarien"
        ]
      },
      "7824bf18-5e96-4297-b754-041d3307105b": {
        "description": "Recette de mamy jo",
        "duree": "10min",
        "id": "7824bf18-5e96-4297-b754-041d3307105b",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image7824bf18-5e96-4297-b754-041d3307105b?alt=media&token=726d8177-6674-4422-8146-21c252f1eea7",
        "name": "Pancakes",
        "quantite": "25 pancakes",
        "recette": "Tout mélanger.\nLaisser reposer 30min.\nEnjoy!",
        "tags": [
          "Dessert"
        ]
      },
      "7863c28f-5186-460d-95a6-f46fbfd1b6ba": {
        "description": "La base",
        "duree": "5 min",
        "id": "7863c28f-5186-460d-95a6-f46fbfd1b6ba",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image7863c28f-5186-460d-95a6-f46fbfd1b6ba?alt=media&token=314b817b-02e5-42b4-b3ef-87a2a84c7b0b",
        "name": "Mojito",
        "quantite": "2 personnes",
        "recette": "Fais toi plaiz tu sais faire :)",
        "tags": [
          "Cocktail"
        ]
      },
      "791978e3-839a-4bb6-b134-dcddf7200d99": {
        "description": "Le repas de la hess",
        "duree": "5 min",
        "id": "791978e3-839a-4bb6-b134-dcddf7200d99",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image791978e3-839a-4bb6-b134-dcddf7200d99?alt=media&token=07a25abf-d40a-4946-a228-343566e71416",
        "name": "Semoule lentilles",
        "quantite": "2 personnes",
        "recette": "Faire cuire la semoule et les lentilles, mélanger miam",
        "tags": [
          "Rapide",
          "Plat"
        ]
      },
      "7d806748-f666-4db6-bcad-2f81fdec5374": {
        "description": "C'est l'été",
        "duree": "10 min",
        "id": "7d806748-f666-4db6-bcad-2f81fdec5374",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image7d806748-f666-4db6-bcad-2f81fdec5374?alt=media&token=c1e62fd3-b687-4f92-bc62-2b644e78dc89",
        "name": "Taboulé épicé",
        "quantite": "2 personnes",
        "recette": "Tout mélanger 😁",
        "tags": [
          "Plat"
        ]
      },
      "7ddc8ac5-6c8a-44e4-9e3e-cad5bdd65594": {
        "description": "Giga bon",
        "duree": "45min",
        "id": "7ddc8ac5-6c8a-44e4-9e3e-cad5bdd65594",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image7ddc8ac5-6c8a-44e4-9e3e-cad5bdd65594?alt=media&token=201f2118-dcf1-4481-98b6-6e1d93f115cd",
        "name": "Soupe courge",
        "quantite": "4 personnes",
        "recette": "Couper les légumes\nMettre dans un fait tout\nAvec un bcp d'eau, elle doit dépasser les légumes\nEt faire cuire en remuant\nEt voilà",
        "tags": [
          "Soupe"
        ]
      },
      "82b8d7dd-6978-43e1-a0bc-bae4436b1e48": {
        "description": "Recette de sissy",
        "duree": "1h",
        "id": "82b8d7dd-6978-43e1-a0bc-bae4436b1e48",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image82b8d7dd-6978-43e1-a0bc-bae4436b1e48?alt=media&token=79c1f779-0f4e-4bae-b7d2-af2745020676",
        "name": "Bowl betterave lentilles quinoa",
        "quantite": "4 portions",
        "recette": "1. Commencer par cuire le quinoa (avec sel) en suivant les indications du paquet (environ 10/15 min). Pendant ce temps, préparer la vinaigrette: mettre tous les ingrédients de la sauce dans un petit bocal, fermer et secouer.\n\n2. Faire revenir les cubes de tofu aux herbes à la poêle (si vous n'aimez pas le tofu cru) ou cuire les œufs mollets : faire chauffer de l'eau dans une casserole. Lorsque l'eau bout, baisser le feu. Y déposer délicatement les œeufs entiers, et laisser cuire 6 min, en gardant l'eau frémissante et non bouillante. Préparer un saladier d'eau très froide pour y déposer les oeufs par la suite, et ainsi stopper la cuisson du jaune.\n\n3. Râper les carottes, couper la betterave en cubes de 1x1 cm, laver et égoutter les jeunes pousses.\n\n4. Dresser dans des bols, commencer par déposer, à part plus moins égale, le quinoa les lentilles, la beterave, les protéines. Ajouter les carottes râpées, la verdure, les noix et graines. Servir avec la vinaigrette à côté.",
        "tags": [
          "Végétarien",
          "Plat"
        ]
      },
      "861728ce-f15d-42da-ba6e-a92d461c81fd": {
        "description": "Où comment finir les restes ",
        "duree": "1h25",
        "id": "861728ce-f15d-42da-ba6e-a92d461c81fd",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image861728ce-f15d-42da-ba6e-a92d461c81fd?alt=media&token=7bbf0e08-4b60-4ea3-be9a-8ec569c95f87",
        "name": "Tartelettes salées ",
        "quantite": "2 personnes",
        "recette": "Tout couper, on peut précuire le légumes.\n\nmettre la pâte sur les emportes pièces, badigeonnez de sauce tomate, ou de pesto ou de crème fraîche ou n'importe quoi.\nmettre les légumes et le fromage.\n\ncuire 200° 40min\n\nmiam\n\n",
        "tags": [
          "Végétarien",
          "Plat",
          "Restes"
        ]
      },
      "8beb0db0-e785-4a7e-b6ea-d19353295cba": {
        "description": "Express, recette de sissy",
        "duree": "20min",
        "id": "8beb0db0-e785-4a7e-b6ea-d19353295cba",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image8beb0db0-e785-4a7e-b6ea-d19353295cba?alt=media&token=df542772-8e25-4385-bc4f-685910825a05",
        "name": "Pizza reine pita",
        "quantite": "2 personnes",
        "recette": "• Préchauffer le four à 200°. Laver et émincer les champignons. Couper le jambon en lamelles.\n\n• Sur chaque pain pita, déposer de la sauce tomate, les lamelles de jambon, les champignons. Ajouter le fromage, les olives noires et enfourner pour 10 à 12 min, en plaçant les pains assez haut dans le four. Surveiller en fin de cuisson.\n\n• Après cuisson, verser un filet d'huile pizza si désiré, et accompagner de crudités ou de verdure. C'est prêt ! Servir une pizza et demie par personne (couper la 3ème pizza en deux).",
        "tags": [
          "Plat",
          "Apéro",
          "Rapide"
        ]
      },
      "8fc288f8-54bd-42f7-9e26-6c7017e25faa": {
        "description": "C'est l'été ou quoi là",
        "duree": "10 min",
        "id": "8fc288f8-54bd-42f7-9e26-6c7017e25faa",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image8fc288f8-54bd-42f7-9e26-6c7017e25faa?alt=media&token=61fc1f1a-6b29-4967-891b-be640c7b3a6c",
        "name": "Tomates mozzarella",
        "quantite": "2 personnes",
        "recette": "Couper tout, et rajouter des épices à ta convenance eheh",
        "tags": [
          "Plat",
          "Été",
          "Entrée"
        ]
      },
      "90a01e21-4491-453b-b003-370e6722dc4f": {
        "description": "Nouilles sautées aux légumes",
        "duree": "25 min",
        "id": "90a01e21-4491-453b-b003-370e6722dc4f",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image90a01e21-4491-453b-b003-370e6722dc4f?alt=media&token=6b343a37-ea06-4231-8597-a731607a4a53",
        "name": "Wok",
        "quantite": "2 personnes",
        "recette": "(si nouilles au seitan : faire la marinade (2 c.s de soja + 1c.s de sirop d'érable), couper le seitan en petit cube, faire tremper.)\n\ncouper les légumes : \n- les poivrons en lamelles\n- les brocolis en petites branches\n- les champignons en lamelles\n- râper les carottes en lamelles\n(- les pousses de soja sont ok à mettre comme ça)\n\nfaites revenir dans une poêle, recouvrir pour cuire à la vapeur.\n\n(dans une autre poêle, faites cuire le poulet ou le seitan).\n\nune fois les légumes cuits, ajouter de la sauce soja (et la viande). laissez cuire 3-4min.\n\najoutez les nouilles, rajoutez de la sauce, couvrez pour cuire à la vapeur. laissez cuire 5min.\n\nmiam\n\n",
        "tags": [
          "Plat",
          "Asiatique"
        ]
      },
      "9df0c46b-6251-498b-a1b7-2d1c1b344830": {
        "description": "P'tit plaisir",
        "duree": "20min",
        "id": "9df0c46b-6251-498b-a1b7-2d1c1b344830",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image9df0c46b-6251-498b-a1b7-2d1c1b344830?alt=media&token=81df89e8-695a-44c3-92a4-3b02e6021276",
        "name": "Bagels",
        "quantite": "2 personnes",
        "recette": "Faire cuire le pain au four pour le faire griller\nMettre le saumon en parallèle\nCouper tout et se concocter un bon p'tit bagel",
        "tags": [
          "Plat"
        ]
      },
      "a420e744-955d-483c-8d27-11b31b3cdc70": {
        "description": "Recette de dylan",
        "duree": "1h20",
        "id": "a420e744-955d-483c-8d27-11b31b3cdc70",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imagea420e744-955d-483c-8d27-11b31b3cdc70?alt=media&token=8fcd753d-98cb-4f25-b1f4-1d1e52271382",
        "name": "Bouchon réunionnais",
        "quantite": "4 personnes ",
        "recette": "1 La pâte:\n\n2 Dans une jatte, mettez la farine et la fécule, mélangez bien puis creusez un puits et ajoutez le blanc d'œuf. Mélangez\n\n3 Préparez un verre d'eau et commencez par verser 10cl, puis rajoutez petit à petit, de sorte à former une boule homogène et non collante. Réservez au frais.\n\n4 La farce :\n\n5 C'est pas compliqué, mettez l'ensemble des ingrédients dans un saladier et mélangez.\n\n6 Formez ensuite des boulettes d'environ 2cm de diamètre. Avec ces quantités, vous en réaliserez au moins 30.\n\n7 Montage des bouchons :\n\n8 Farinez votre plan de travail.\n\n9 Déposez votre pâte au centre et aplatissez-la au maximum avec vos mains, puis à l'aide d'un rouleau à pâtisserie, abaissez jusqu'à ce qu'elle ne fasse pas plus de 2mm d'épaisseur, l'idéal étant 1mm.\n\n10 C'est assez élastique et facile à manipuler.\n\n11 Ensuite découpez des carrés d'environ 6cm de largeur.\n\n12 Prenez en main un carré, mettez au centre une boulette, repliez les bords de façon à recouvrir la boulette, soudez les (vu la consistance de la pâte, pas besoin de rajouter de l'eau pour souder) mais gardez une ouverture, une sorte de petite cheminée sur le haut du bouchon.\n\n13 Continuez ainsi jusqu'à épuisement de la farce.\n\n14 Si il vous reste de la pâte, sachez qu'elle se congèle très bien.\n\n15 La cuisson :\n\n16 Elle se fait en deux étapes, une où on poche les bouchons comme de simples ravioles fraîches et une phase cuisson à la vapeur.\n\n17 Pour les pocher, mettez de l'eau à bouillir dans une marmite, plongez une dizaine de bouchons (pas plus au risque qu'ils ne se collent entre eux). Au bout d'environ 3 minutes, ils vont remonter à la surface: il n'y a plus qu'à les récupérer au moyen d'une écumoire, réserver et poursuivre avec le reste des bouchons.\n\n18 A cette étape, vous pouvez les laisser refroidir et éventuellement les congeler. Vous les laisserez décongeler une nuit au frigidaire puis les cuirez.\n\n19 Les bouchons se cuisent à la vapeur, de préférence dans un panier en bambou, sinon graissez un peu votre panier avec un peu de papier absorbant imbibé d'huile, placez le au dessus de l'eau bouillante.\n\n20 Disposez vos bouchons et selon leur nombre, c'est parti pour 30 à 45 minutes de cuisson.\n\n21 Servir aussitôt. (avec une sauce pimentée, c'est divin!)",
        "tags": [
          "Plat",
          "Apéro"
        ]
      },
      "acb9bef5-a446-4315-aa2f-ba10dcffc603": {
        "description": "Le meilleur plat",
        "duree": "30 min",
        "id": "acb9bef5-a446-4315-aa2f-ba10dcffc603",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imageacb9bef5-a446-4315-aa2f-ba10dcffc603?alt=media&token=eed53d29-5703-43c5-be9a-df7865fd214d",
        "name": "Hamburger maison",
        "quantite": "2 personnes",
        "recette": "1 : faire griller le pain au four avec le cheddar dessus\n\n2 : faire des steaks à la main\n\n3 : quand tout est cuit, faire le burger\n\nhopla c'est prêt eheh",
        "tags": [
          "Plat",
          "Gros",
          "Burger"
        ]
      },
      "acccc58b-016e-41e1-9666-5b2edb4e416c": {
        "description": "Ou en version veggie",
        "duree": "45min",
        "id": "acccc58b-016e-41e1-9666-5b2edb4e416c",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imageacccc58b-016e-41e1-9666-5b2edb4e416c?alt=media&token=24fc972a-6edd-4915-bc95-01d4255d24e8",
        "name": "Porc au caramel",
        "quantite": "2 personnes",
        "recette": "Mettre au préalable les protéines de soja dans un saladier, y ajouter les 4 cas de sauce soja et les recouvrir du bouillon de légumes. Laisser mariner 30 min minimum.\n\nEn parallèle, préparer la sauce en mettant dans un bol les 4 cas de sauce soja restante ainsi que les 6 cas de sirop d'agave, bien mélanger.\n\nFaire cuire en attendant le riz thai.\n\nDans une poêle, faire fondre l'oignon dans un fond\n\nd'huile jusqu'à ce qu'ils soient bien fondants, réserver.\n\nAjouter ensuite dans la poêle les protéines égouttées\n\net bien faire dorer à feux plus fort. Lorsque les\n\nprotéines sont bien grillées, y ajouter le sirop préparé\n\net faire réduire afin d'obtenir de beaux morceaux\n\ncaramélisés. Réserver.\n\nEgoutter le riz et y mettre si besoin une cac d'huile pour qu'il ne colle pas.\n\nServir les protéines caramélisées avec le riz thaï.",
        "tags": [
          "Végétarien",
          "Plat"
        ]
      },
      "b06f4b84-32a6-43a9-86dc-c5da09dae484": {
        "description": "Protéiné et healthy",
        "duree": "25 min",
        "id": "b06f4b84-32a6-43a9-86dc-c5da09dae484",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imageb06f4b84-32a6-43a9-86dc-c5da09dae484?alt=media&token=d45a3f07-1ffc-4771-a197-7cf3b7bf9ada",
        "name": "Salade de lentilles",
        "quantite": "2 personnes",
        "recette": "Commencer par faire cuire les lentilles vertes comme indiqué sur le paquet, puis les laisser refroidir (à préparer idéalement plusieurs heures avant, voir la veille).\n\nAjouter un oignon coupé en deux et piqué de clous de girofle, une feuille de laurier, ou bien un bouquet garni pour parfumer l'eau de cuisson et donc les lentilles.\n\nÉmincer l'oignon rouge, l'échalote, le persil et couper le tofu en cubes.\n\nDans une poêle, faire chauffer la càs d'huile, sur feu moyen. Faire revenir l'oignon et le tofu pendant 5 min environ, jusqu'à ce que le tofu soit grillé. Réserver.\n\nDans un petit pot hermétique, préparer la vinaigrette : y verser tous les ingrédients, fermer, secouer, goûter et rectifier l'assaisonnement si besoin. Réserver.\n\nDans un saladier, déposer les lentilles cuites, le tofu, l'oignon, l'échalotte, le persil. Mélanger à l'aide d'une spatule. C'est prêt !\n\nLaisser la vinaigrette à disposition au moment de déguster.\n\nConseil : pour accélérer le temps de cuisson des lentilles vertes et les rendre plus digestes, ajouter une lamelle d'algue kombu dans l'eau de cuisson (non salée).",
        "tags": [
          "Plat",
          "Végétarien",
          "Healthy",
          "Sissy"
        ]
      },
      "b51f01ef-8e7e-4731-8dce-02d527c1479f": {
        "description": "Le plat qui met tout le monde d'accord",
        "duree": "45min",
        "id": "b51f01ef-8e7e-4731-8dce-02d527c1479f",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imageb51f01ef-8e7e-4731-8dce-02d527c1479f?alt=media&token=b3aab37d-139f-4e66-9e32-973c595e0012",
        "name": "Wraps poulet pané",
        "quantite": "2 personnes",
        "recette": "Pané le poulet : farine oeuf chapelure.\ntout couper\nchauffer les wraps\net puis miam hein !",
        "tags": [
          "Plat",
          "Soirée",
          "Groupe"
        ]
      },
      "b5d9c9e0-334e-4ce8-8889-c8bf4207d91c": {
        "description": "Recette de sissy",
        "duree": "45min",
        "id": "b5d9c9e0-334e-4ce8-8889-c8bf4207d91c",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imageb5d9c9e0-334e-4ce8-8889-c8bf4207d91c?alt=media&token=c6623117-fd90-443e-9cc2-3bcc8c693e49",
        "name": "Chou-fleur tikka massala",
        "quantite": "4 personnes",
        "recette": "Déposer les protéines de soja dans un bol, ajouter 1 càs de sauce soja et couvrir d'eau bouillante. Rincer les lentilles corail. Couper le chou-fleur en petits bouquets (trop gros, ils seront plus longs à cuire). Émincer l'ail et l'oignon. Râper le gingembre s'il est frais.\n\nDans une grande casserole, faire chauffer à feu moyen/fort 2 càs d'huile de cuisson. Faire revenir l'oignon et le chou-fleur pour les faire légèrement griller pendant environ 3 min. Ajouter ensuite les lentilles, les protéines de soja égouttées, les épices, les tomates, le bouillon, saler, poivrer, remuer, et laisser cuire à couvert à feu doux/moyen pendant 15min, jusqu'à ce que les lentilles et le chou-fleur soit cuit. Remettre du bouillon s'il s'évapore de trop pendant la cuisson.\n\nÉmincer grossièrement la botte de coriandre, ajouter les 2/3 en fin de cuisson avec la crème végétale, mélanger, c'est prêt ! Ajuster l'assaisonnement avec les épices si besoin.\n\nServir avec du riz et parsemer du reste de la coriandre.",
        "tags": [
          "Végétarien",
          "Plat"
        ]
      },
      "bc3c46d3-fce1-4dd9-a070-4dd14da12c3c": {
        "description": "Sauce",
        "duree": "3 min",
        "id": "bc3c46d3-fce1-4dd9-a070-4dd14da12c3c",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imagebc3c46d3-fce1-4dd9-a070-4dd14da12c3c?alt=media&token=f119691c-a1df-4828-8e48-858c3d849eca",
        "name": "Sauce pour bowl",
        "quantite": "2 bowl",
        "recette": "Mettre tout les ingrédients dans le mixeur\nMixer\nMettre dans un beau bol\nC'est prêt !",
        "tags": [
          "Bowl",
          "Plat",
          "Sauce"
        ]
      },
      "c363b713-2079-4b55-97f8-39f2da8fa340": {
        "description": "Recette de mami réunionnaise",
        "duree": "1h20",
        "id": "c363b713-2079-4b55-97f8-39f2da8fa340",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imagec363b713-2079-4b55-97f8-39f2da8fa340?alt=media&token=ccc84a40-fbab-4c4c-a1f7-db6ace5cea65",
        "name": "Rougail saucisses",
        "quantite": "4 personnes ",
        "recette": "Piquer les saucisses et les faire bouillir 15 min dans une marmite (pour les dégraisser et les dessaler), puis jeter l'eau.\n\nCouper en morceaux les petits oignons verts, le gros piment, les oignons, les tomates.\n\nEcraser dans un pilon les 2 petits piments verts et du sel, puis écraser avec l'ail coupé en\npetits morceaux.\n\nCouper les saucisses en tronçons de 1,5 à 2 cm d'épaisseur; les mettre à frire avec un peu d'huile dans la marmite (ou une casserole) profonde, remuer régulièrement jusqu'à ce qu'elles prennent une belle couleur.\n\nAjouter les oignons sans enlever les saucisses. Bien remuer pour détacher ce qui serait accroché au fond de la casserole.\n\nAjouter la pâte d'ail, de sel et de 2 petits piments, remuer.\n\nAjouter les tomates, remuer.\n\nBaisser le feu, couvrir et laisser mijoter. Ajouter un peu d'eau si besoin.\n\nAprès 10 mn, ajouter un petit bouquet de thym, les oignons verts et les piments verts. Enlever le couvercle et laisser réduire sans couvercle.\n\nLa peau de la saucisse ne doit pas être trop dure, laisser cuire plus sinon.\n\nLaisser mijoter à feu doux encore au moins 15/20 mn.\n\nA consommer avec du riz blanc et des grains (haricots rouges ou blancs, lentilles, pois du cap, etc.). Accompagner d'un petit rougail tomate pimenté selon les goûts. Le rougail saucisse est prêt à être consommé immédiatement, mais il est encore meilleur le lendemain ou le surlendemain, quand il a bien pris tout son goût. Il se congèle très bien.",
        "tags": [
          "Plat"
        ]
      },
      "c558c46b-c6ba-4b51-b43c-6caf92914f10": {
        "description": "Ou polenta jambon maïs",
        "duree": "10min",
        "id": "c558c46b-c6ba-4b51-b43c-6caf92914f10",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imagec558c46b-c6ba-4b51-b43c-6caf92914f10?alt=media&token=a969b12a-1eec-48a4-986d-794d74355012",
        "name": "Purée jambon maïs",
        "quantite": "2 personnes",
        "recette": "Faire la purée et mélanger",
        "tags": [
          "Rapide",
          "Plat"
        ]
      },
      "c5730e8c-88b3-4ad7-b003-bc4a6313fc4b": {
        "description": "Repas de la flemme de ju",
        "duree": "20min",
        "id": "c5730e8c-88b3-4ad7-b003-bc4a6313fc4b",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imagec5730e8c-88b3-4ad7-b003-bc4a6313fc4b?alt=media&token=40e29520-f630-4a82-8549-d30e243de289",
        "name": "Pâte carbonara",
        "quantite": "2 personnes",
        "recette": "Faire cuire et dévorer !",
        "tags": [
          "Plat",
          "Rapide"
        ]
      },
      "da89093a-2b0e-4b27-b3d2-51acb43f92c8": {
        "description": "Trop bon selon hélo",
        "duree": "20 min",
        "id": "da89093a-2b0e-4b27-b3d2-51acb43f92c8",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imageda89093a-2b0e-4b27-b3d2-51acb43f92c8?alt=media&token=c5906df5-4bfc-46e4-9281-090dfe8f392b",
        "name": "Salade chèvre chaud",
        "quantite": "2 personnes",
        "recette": "Préchauffer le four à 180\n\n1 : tout mettre dedans le saladier\n\n2 : couper des tranches de pains, badigeonner d'huile d'olive et d'épices, mettre le fromage dessus, l'enfourner au four\n\nbon ap'\n",
        "tags": [
          "Plat"
        ]
      },
      "df0dcbc9-433a-4c6c-9b09-03d47b7b810e": {
        "description": "Pour hélo",
        "duree": "10min",
        "id": "df0dcbc9-433a-4c6c-9b09-03d47b7b810e",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imagedf0dcbc9-433a-4c6c-9b09-03d47b7b810e?alt=media&token=c05b4d7f-ebda-40f4-99a8-705dd45bbc99",
        "name": "Croque monsieur végé",
        "quantite": "2 personnes",
        "recette": "200° pendant 10min",
        "tags": [
          "Végétarien",
          "Apéro",
          "Plat"
        ]
      },
      "e8569d80-552d-4428-b3a9-d749e7df9baf": {
        "description": "A base de vodka",
        "duree": "20min",
        "id": "e8569d80-552d-4428-b3a9-d749e7df9baf",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imagee8569d80-552d-4428-b3a9-d749e7df9baf?alt=media&token=b38cbccd-b051-4fd8-846f-07a88a213461",
        "name": "Cocktail de sissy au thé",
        "quantite": "10 verres ",
        "recette": "N.B : pour une version sans alcool, préparer le thé (étape 2) puis y déposer les morceaux de pêches et citrons (expliqué à l'étape 1), directement dans le thé. Après plusieurs heures au frais, ajouter environ 40 g de sucre. Mélanger et gouter pour ajouter plus de sucre si désiré. Ensuite, au choix, filtrer avant de servir, ou servir avec les morceaux de pêches, et ajouter quelques feuilles de menthe fraiche.\n\n1. La veille, commencer par faire macérer les pêches dans l'alcool: éplucher ou non les pêches, les dénoyauter et les couper en gros morceaux. Couper un demi citron en rondelles. Dans un bocal, verser le jus de l'autre moitié du citron, avec les morceaux de pêches (sans les noyaux), les tranches de citron et enfin la vodka. Fermer le bocal de manière hermétique et laisser macérer 24 heures.\n\n2. Ensuite, préparer le thé : dans un grand bocal (ou\n\ndeux plus petits), déposer les feuilles de thé, le sucre\n\nou sirop d'agave, puis ajouter 2 litres d'eau filtrée, à\n\ntempérature ambiante. Laisser macérer entre 1h30 et\n\n2h. Ensuite, filtrer l'eau pour enlever les feuilles de thé\n\net mettre au frais pour 5 heures minimum.\n\n3. Le lendemain, quand les pêches ont macéré 24 heures dans la vodka, retirer les rondelles de citron. Verser la vodka aux pêches dans un blender, avec les pêches, et mixer environ 30 secondes. Réserver au frais si désiré.\n\n4. Trancher les citrons jaunes et les pêches en quartiers. Goûter le thé glacé, ajouter du sirop d'agave si désiré.\n\nEnsuite, pour un service au verre : filtrer et verser environ 50 ml de vodka aux pêches dans un verre haut, utiliser une passoire à maille fine pour filtrer la purée de pêche. Ajouter quelques tranches de pêche et de citron, quelques feuilles de menthe. À l'aide d'un pilon, écraser plusieurs fois le contenu du verre. Ajouter plein de glaçons, et remplir le verre de thé bien froid, jusqu'en haut. C'est prêt !\n\nPour un service au pichet : dans un grand pichet ou une bombonne, déposer les quartiers de pêche et de citron, ainsi qu'une bonne poignée de feuilles de menthe. Écraser le tout à l'aide d'un pilon pour dégager les arômes. Filtrer toute la vodka, et la verser dans le pichet. Verser le thé glacé et mélanger. Goûter et rectifier en sucre ou en jus de citron si désiré. Ensuite, pour se servir, ajouter d'abord les glaçons dans le verre puis verser le cocktail avec une louche.\n\nLa vodka peut se garder quelques semaines dans un endroit frais et sec. Le thé glacé quelques jours au frais.",
        "tags": [
          "Healthy",
          "Cocktail"
        ]
      },
      "e909b217-b4c7-4bd8-a3ce-3ed7cf0d539f": {
        "description": "Le repas de la flemme tu coco",
        "duree": "3 min",
        "id": "e909b217-b4c7-4bd8-a3ce-3ed7cf0d539f",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imagee909b217-b4c7-4bd8-a3ce-3ed7cf0d539f?alt=media&token=fc1a2d5f-7aac-481a-b900-3e0e2306263d",
        "name": "Raviole",
        "quantite": "2 personnes",
        "recette": "Ben du coup... ouvrir le paquet...oui..et euh..cuire dans l'eau qui bout...voilaaaaa",
        "tags": [
          "Plat",
          "Rapide",
          "Flemme"
        ]
      },
      "e9b03b98-4be7-454a-be98-4a3234baf3ec": {
        "description": "Recette de papa",
        "duree": "40 jours",
        "id": "e9b03b98-4be7-454a-be98-4a3234baf3ec",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imagee9b03b98-4be7-454a-be98-4a3234baf3ec?alt=media&token=6a5413e8-bb57-4430-8162-9fc2ca88a006",
        "name": "Génépi",
        "quantite": "2 litres",
        "recette": "Attention aux propositions : 50cl ou 1l donc 1 paquet ou 2.\nGarder un ou deux ou trois brins pour la déco dans la bouteille.\nFaire macérer le génépi pendant 40 jours dans l alcool.\nAu bout de 40 jours : Filtrer avec un filtre à café.\nFaire le sirop : faire 1 l de sirop : eau + sucre.\nMélanger l'alcool et le sirop.\nPuis repartir le génépi.\nBien joué !!\n\n(220g + eau du monts blanc)\n\n",
        "tags": [
          "Cocktail"
        ]
      },
      "ee7e1e07-b9c5-4e7a-b60e-a9252a3eb6e6": {
        "description": "Végétarien",
        "duree": "10min",
        "id": "ee7e1e07-b9c5-4e7a-b60e-a9252a3eb6e6",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imageee7e1e07-b9c5-4e7a-b60e-a9252a3eb6e6?alt=media&token=83972d60-c7b7-405c-9e8d-8d722565b063",
        "name": "Wraps de lentilles corail",
        "quantite": "2 personnes ",
        "recette": "Cuire les lentilles corail\n\nMixez\n\nCuire comme des crêpes",
        "tags": [
          "Plat"
        ]
      },
      "ef1bd565-7bc7-4693-a1f1-54e24b2a99fd": {
        "description": "Recette - l'original ",
        "duree": "10min",
        "id": "ef1bd565-7bc7-4693-a1f1-54e24b2a99fd",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imageef1bd565-7bc7-4693-a1f1-54e24b2a99fd?alt=media&token=6c5b5a4e-10d0-4350-a059-ce51622a87b6",
        "name": "Croque monsieur ",
        "quantite": "2 personnes",
        "recette": "200° au four 10min\n",
        "tags": [
          "Plat",
          "Apéro",
          "Rapide"
        ]
      },
      "f82e4ed6-6404-4bc1-95b7-831a71e100cc": {
        "description": "Les crêpes de maman !",
        "duree": "15min",
        "id": "f82e4ed6-6404-4bc1-95b7-831a71e100cc",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imagef82e4ed6-6404-4bc1-95b7-831a71e100cc?alt=media&token=484b6ad0-47c3-40fe-a7d9-a93be26b6738",
        "name": "Crêpes",
        "quantite": "2 personnes",
        "recette": "Pour 10 crêpes\n\ndans un saladier :\n- versez les oeufs (battus jusqu'à devenir blanc)\n- faites fondre le beurre et versez le\n- ajoutez le sucre\najoutez alternativement lait et farine dans le saladier\n- versez autant de rhum que nécessaire",
        "tags": [
          "Brunch",
          "Dessert",
          "Goûter",
          "Plat"
        ]
      },
      "f8ab0c87-8712-42b7-b7ff-875f70a6681f": {
        "description": "Recette de sissy",
        "duree": "1h15",
        "id": "f8ab0c87-8712-42b7-b7ff-875f70a6681f",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imagef8ab0c87-8712-42b7-b7ff-875f70a6681f?alt=media&token=3e398efd-bd79-4698-8706-e287188757c2",
        "name": "Lasagne bolognaise",
        "quantite": "6-8 personnes",
        "recette": "Faire revenir l'ail et l'oignon émincé dans un peu d'huile d'olive. Ajouter la carotte coupée en morceaux puis l'haché et faire revenir le tout.\n\nAu bout de quelques minutes, ajouter le vin rouge. Laisser cuire jusqu'à évaporation. Ajouter la purée de tomates, l'eau et le basilic. Saler, poivrer, puis laisser mijoter à feu doux 45 minutes.\n\nPendant ce temps-là, préparer la béchamel : faire fondre la margarine végétale. Hors du feu, ajouter la farine d'un coup. Remettre sur le feu et remuer avec un fouet jusqu'à l'obtention d'un mélange bien lisse. Ajouter le lait peu à peu. Remuer sans cesse, jusqu'à ce que le mélange s'épaississe. Ensuite, parfumer avec la muscade, saler, poivrer. Laisser cuire environ 5 minutes, à feu très doux, en remuant. Réserver.\n\nPréchauffer le four à 200°C (thermostat 6-7). Huiler le plat à lasagnes. Poser une fine couche de béchamel puis des feuilles de lasagnes, de la bolognaise, de la béchamel et du fauxmage. Répéter l'opération environ 3 fois de suite.\n\nSur la dernière couche de lasagnes, ne mettre que de la béchamel et recouvrir de fromage râpé.\n\nEnfourner pour environ 25 minutes de cuisson.",
        "tags": [
          "Végétarien",
          "Plat"
        ]
      },
      "fed07360-0c7c-441d-aa61-6cf6e742e298": {
        "description": "Sissy",
        "duree": "1h35",
        "id": "fed07360-0c7c-441d-aa61-6cf6e742e298",
        "imageUri": "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/imagefed07360-0c7c-441d-aa61-6cf6e742e298?alt=media&token=cf9c9983-0b29-425e-9693-ec5ecc7b1b3d",
        "name": "Brookie healthy",
        "quantite": "16 portions",
        "recette": "• Commencer par la pâte à cookie: préchauffer le four à 220°. Bien égoutter et rincer les pois chiches (garder le jus de pois chiche pour une autre recette si désiré). Étaler les pois chiches sur une plaque antiadhésive et enfourner pour 15 min, afin d'assécher les pois chiches. Laisser refroidir quelques instants avant de les utiliser.\n\nEnsuite, déposer les pois chiches refroidis dans le mixeur, avec le reste des ingrédients SAUF les pépites de chocolat. Bien mixer jusqu'à avoir la texture la plus lisse possible, racler les bords si besoin. La pâte doit être compacte. Puis, ajouter les pépites de chocolat, mélanger à la cuillère ou spatule puis mettre la pâte obtenue au frais.\n\n• Préparer la pâte à brownie: dans un bol, mélanger le lait avec les graines de lin mixées. Laisser gonfler. Faire fondre le chocolat au micro-ondes, à puissance moyenne pendant max 1 min, puis remuer à la cuillère pour aider le chocolat à fondre, remettre à chauffer max 30 secondes si nécessaire.\n\n• Dans un grand saladier ou dans le bol d'un robot pâtissier, mettre la margarine ramollie avec les sucres et fouetter ou mélanger le tout. Ajouter le café, le cacao, la fleur de sel, les graines de lin mixées avec le lait, et mélanger à nouveau. Ajouter le chocolat fondu, mélanger à la spatule. Et enfin, terminer par la farine tamisée, le bicarbonate de soude puis les noix concassées, si désiré. Mélanger. La pâte obtenue doit être compacte.\n\n. Cuire le brookie : préchauffer le four à 180°. Recouvrir de papier sulfurisé le moule choisi (ou huiler le moule). Y verser la pâte à brownie et bien l'étaler jusqu'aux coins du moule. Utiliser une spatule plate ou ses mains humidifiées. Ensuite, faire de même avec la pâte à cookie, en parsemer uniformément sur la pâte à brownie (comme un crumble), puis essayer de lisser le dessus, sans trop appuyer pour ne pas écraser le brownie.\n\n• Enfourner pour 20/25min pour un brookie moins cuit et plus humide, ou 30 min pour un brookie bien cuit, selon ses préférences. C'est prêt !\n\nPeut se garder quelques jours dans une boîte hermétique, dans un endroit frais et sec. Ou au réfrigérateur pendant une semaine. Le cookie perd néanmoins son croquant assez rapidement.",
        "tags": [
          "Dessert",
          "Sissy"
        ]
      }
    },
    "semainier": {
      "dimanche": {
        "apero": "None",
        "id_semainier": "dimanche",
        "midi": "None",
        "soir": "None"
      },
      "jeudi": {
        "apero": "None",
        "id_semainier": "jeudi",
        "midi": "None",
        "soir": "731b2a71-aa1c-42fb-a0bd-9fbc9a07f922"
      },
      "lundi": {
        "apero": "None",
        "id_semainier": "lundi",
        "midi": "None",
        "soir": "None"
      },
      "mardi": {
        "apero": "None",
        "id_semainier": "mardi",
        "midi": "None",
        "soir": "8fc288f8-54bd-42f7-9e26-6c7017e25faa"
      },
      "mercredi": {
        "apero": "None",
        "autres": [
          "56b025f6-91af-4d15-92dc-b791c6d63933"
        ],
        "id_semainier": "mercredi",
        "midi": "None",
        "soir": "5588414e-a159-4c0f-bfb8-cc50a697ff6a"
      },
      "samedi": {
        "apero": "None",
        "id_semainier": "samedi",
        "midi": "None",
        "soir": "77be6bdb-ffb1-4615-a27d-760620b9077b"
      },
      "vendredi": {
        "apero": "None",
        "id_semainier": "vendredi",
        "midi": "None",
        "soir": "82b8d7dd-6978-43e1-a0bc-bae4436b1e48"
      }
    },
    "semainierSuivant": {
      "dimanche": {
        "apero": "None",
        "id_semainier": "dimanche",
        "midi": "None",
        "soir": "None"
      },
      "jeudi": {
        "apero": "None",
        "id_semainier": "jeudi",
        "midi": "None",
        "soir": "None"
      },
      "lundi": {
        "apero": "None",
        "id_semainier": "lundi",
        "midi": "None",
        "soir": "None"
      },
      "mardi": {
        "apero": "None",
        "id_semainier": "mardi",
        "midi": "None",
        "soir": "None"
      },
      "mercredi": {
        "apero": "None",
        "id_semainier": "mercredi",
        "midi": "None",
        "soir": "None"
      },
      "samedi": {
        "apero": "None",
        "id_semainier": "samedi",
        "midi": "None",
        "soir": "None"
      },
      "vendredi": {
        "apero": "None",
        "id_semainier": "vendredi",
        "midi": "None",
        "soir": "None"
      }
    }
}
"""
        ).nextValue() as JSONObject

        var jsonArray = jsonObject.getJSONObject("categorie")
        for (currentKey in jsonArray.keys()) {
            // ID
            val categorieId = jsonArray.getJSONObject(currentKey.toString()).getString("id")
            // name
            val categorieName = jsonArray.getJSONObject(currentKey.toString()).getString("name")

            categorie.put(currentKey, CategorieModel(categorieId, categorieName))
        }

        jsonArray = jsonObject.getJSONObject("ingredients")

        for (currentKey in jsonArray.keys()) {

            // ID
            val ingredientId = jsonArray.getJSONObject(currentKey.toString()).getString("id")
            // name
            val ingredientName = jsonArray.getJSONObject(currentKey.toString()).getString("name")
            // name
            val ingredientRepas =
                jsonArray.getJSONObject(currentKey.toString()).getString("id_repas")
            // name
            val ingredientCategorie =
                jsonArray.getJSONObject(currentKey.toString()).getString("id_categorie")
            // name
            val ingredientQuantite =
                jsonArray.getJSONObject(currentKey.toString()).getString("quantite")
            // name
            val ingredientRank = jsonArray.getJSONObject(currentKey.toString()).getString("rank")

            ingredients.put(
                currentKey,
                IngredientModel(
                    ingredientId,
                    ingredientName,
                    ingredientRepas,
                    ingredientCategorie,
                    ingredientQuantite,
                    ingredientRank.toInt()
                )
            )
        }

        jsonArray = jsonObject.getJSONObject("repas")

        for (currentKey in jsonArray.keys()) {

            // ID
            val repasDescription =
                jsonArray.getJSONObject(currentKey.toString()).getString("description")
            // name
            val repasDuree = jsonArray.getJSONObject(currentKey.toString()).getString("duree")
            // name
            val repasId = jsonArray.getJSONObject(currentKey.toString()).getString("id")
            // name
            val repasImage = jsonArray.getJSONObject(currentKey.toString()).getString("imageUri")
            // name
            val repasQuantite = jsonArray.getJSONObject(currentKey.toString()).getString("quantite")
            // name
            val repasName = jsonArray.getJSONObject(currentKey.toString()).getString("name")
            val repasRecette = jsonArray.getJSONObject(currentKey.toString()).getString("recette")

            val tagsList = jsonArray.getJSONObject(currentKey.toString()).getJSONArray("tags")
            val tagListArray = arrayListOf<String>()
            for (tag in 0 until tagsList.length()) {
                tagListArray.add(tagsList[tag].toString())
            }


            repas.put(
                currentKey,
                RepasModel(
                    repasId,
                    repasName,
                    repasDescription,
                    repasImage,
                    repasRecette,
                    repasQuantite,
                    tagListArray,
                    repasDuree
                )
            )
        }

        return mutableMapOf<String, Any>(
            "categorie" to categorie,
            "repas" to repas,
            "ingredients" to ingredients,
            "semainierSuivant" to semainierSuivant,
            "semainier" to semainier
        )
    }

}