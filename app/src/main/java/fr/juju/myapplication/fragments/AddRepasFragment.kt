package fr.juju.myapplication.fragments

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.DialogInterface
import android.view.inputmethod.InputMethodManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import fr.juju.myapplication.*
import fr.juju.myapplication.repository.IngredientRepository.Singleton.ingredientList
import fr.juju.myapplication.adapter.*
import java.io.IOException
import java.util.*
import androidx.recyclerview.widget.ItemTouchHelper
import com.google.firebase.auth.ktx.auth
import fr.juju.myapplication.activity.MainActivity
import fr.juju.myapplication.repository.RepasRepository.Singleton.repasList
import fr.juju.myapplication.model.IngredientModel
import fr.juju.myapplication.model.RepasModel
import fr.juju.myapplication.popup.IngredientPopup
import fr.juju.myapplication.repository.IngredientRepository
import fr.juju.myapplication.repository.RepasCommunRepository
import fr.juju.myapplication.repository.RepasRepository


class AddRepasFragment(
    private val context: MainActivity
) : Fragment() {

    private var uploadedImage: ImageView? = null
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private val randomId = UUID.randomUUID().toString()
    val listItem = arrayListOf<IngredientModel>()
    var repas = RepasModel()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater?.inflate(R.layout.fragment_add_repas, container, false)
        val listIngredientView = view.findViewById<RecyclerView>(R.id.list_ingredient)
        context.onBackPressedDispatcher.addCallback(context, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                var builder = AlertDialog.Builder(context)
                builder.setTitle("Ben ?")
                builder.setMessage("Tu veux vraiment quitter la page ?")
                builder.setPositiveButton("Oui", DialogInterface.OnClickListener { dialog, id ->
                    context.hideKeyboard()
                    context.supportFragmentManager.popBackStack()
                    dialog.cancel()
                })
                builder.setNegativeButton("Non", DialogInterface.OnClickListener { dialog, id ->
                    dialog.cancel()
                })
                var alert: AlertDialog = builder.create()
                alert.show()
            }
        })
        listIngredientView.adapter =
            EditIngredientAdapter(context, listItem, R.layout.item_edit_ingredient_vertical)
        listIngredientView.layoutManager = LinearLayoutManager(context)

        var simpleCallback = object :
            ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or (ItemTouchHelper.DOWN), 0) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                var startPosition = viewHolder.adapterPosition
                var endPosition = target.adapterPosition
                Collections.swap(listItem, startPosition, endPosition)
                listIngredientView.adapter?.notifyItemMoved(startPosition, endPosition)

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }

        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(listIngredientView)


        //recup uploadilmage pour lui associer son composant
        uploadedImage = view.findViewById(R.id.preview_image)
        //recup le bouton pour charger l'image
        val pickupImage = view.findViewById<ImageView>(R.id.preview_image)
        pickupImage.setOnClickListener {
            launchGallery()
        }


        view.findViewById<ImageView>(R.id.valid).setOnClickListener {
            sendform(view)
        }

        var temp = false
        val addIngredientButton = view.findViewById<ImageView>(R.id.add_ingredient)

        view.findViewById<EditText>(R.id.quantite).addTextChangedListener(
            object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.contains("\n")) {
                        view.findViewById<EditText>(R.id.quantite)
                            .setText(s.toString().replace("\n", ""))
                        addIngredientButton.performClick()
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // Fires right before text is changing
                }

                override fun afterTextChanged(s: Editable) {


                }
            }
        )

        view.findViewById<EditText>(R.id.ingredient).addTextChangedListener(
            object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.contains("\n")) {
                        view.findViewById<EditText>(R.id.ingredient)
                            .setText(s.toString().replace("\n", ""))
                        view.findViewById<EditText>(R.id.quantite).requestFocus()
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // Fires right before text is changing
                }

                override fun afterTextChanged(s: Editable) {


                }
            }
        )



        addIngredientButton.setOnClickListener {
            if (view.findViewById<EditText>(R.id.ingredient).text.isNotEmpty()) {
                addIngredient(view)
                view.findViewById<EditText>(R.id.ingredient).setText("")
                view.findViewById<EditText>(R.id.quantite).setText("")
                view.findViewById<EditText>(R.id.ingredient).requestFocus()
                listIngredientView.adapter =
                    EditIngredientAdapter(context, listItem, R.layout.item_edit_ingredient_vertical)
                listIngredientView.scrollToPosition(1500)
                temp = false
            }
            view.findViewById<EditText>(R.id.ingredient).visibility = View.VISIBLE
            view.findViewById<EditText>(R.id.quantite).visibility = View.VISIBLE
            addIngredientButton.animate().translationX(+790F).setDuration(150)
            view.findViewById<EditText>(R.id.ingredient).requestFocus()
            val showMe =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            showMe.showSoftInput(
                view.findViewById<EditText>(R.id.ingredient),
                InputMethodManager.SHOW_IMPLICIT
            )

        }
        val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.tags)
        collectionRecyclerView.adapter =
            EditTagsAdapter(context, repas.tags, R.layout.item_edit_tags_horizontal)
        val add_tagButton = view.findViewById<ImageView>(R.id.add_tag)
        add_tagButton.setOnClickListener {
            if (view.findViewById<EditText>(R.id.tag_input).text.isNotEmpty()) {
                add_tag(view)
                collectionRecyclerView.adapter =
                    EditTagsAdapter(context, repas.tags, R.layout.item_edit_tags_horizontal)
                view.findViewById<EditText>(R.id.tag_input).setText("")
                view.findViewById<EditText>(R.id.tag_input).requestFocus()
            }
        }
        view.findViewById<EditText>(R.id.tag_input).addTextChangedListener(
            object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.contains("\n")) {
                        view.findViewById<EditText>(R.id.tag_input)
                            .setText(s.toString().replace("\n", ""))
                        add_tagButton.performClick()
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // Fires right before text is changing
                }

                override fun afterTextChanged(s: Editable) {


                }
            }
        )

        view.findViewById<EditText>(R.id.name_input).addTextChangedListener(
            object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (repasList.filter { s -> s.name == view.findViewById<EditText>(R.id.name_input).text.toString() }
                            .isNotEmpty()) {
                        view.findViewById<EditText>(R.id.name_input).error =
                            "La plat existe d??j?? :)"
                    }
                }


                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // Fires right before text is changing
                }

                override fun afterTextChanged(s: Editable) {


                }
            }
        )

        //Set to linerarlayout to ingredients
        view?.findViewById<ConstraintLayout>(R.id.recetteCard)?.visibility = View.GONE
        view?.findViewById<View>(R.id.recette_soulignage)?.visibility = View.INVISIBLE

        view.findViewById<TextView>(R.id.ingredients).setOnClickListener {
            switcher("ingredient")
        }
        view.findViewById<TextView>(R.id.recette).setOnClickListener {
            switcher("recette")
            view.findViewById<EditText>(R.id.recette_input).requestFocus()
            val showMe =
                context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            showMe.showSoftInput(
                view.findViewById<EditText>(R.id.recette_input),
                InputMethodManager.SHOW_IMPLICIT
            )
        }

        var scrollView = view.findViewById<ScrollView>(R.id.scrollView)
        scrollView.getViewTreeObserver()
            .addOnScrollChangedListener(ViewTreeObserver.OnScrollChangedListener {
                if (scrollView.getChildAt(0).getBottom()
                    <= scrollView.getHeight() + scrollView.getScrollY()
                ) {
                    temp = true
                    view?.findViewById<ImageView>(R.id.valid)?.animate()?.alpha(0F)
                        ?.translationY(+1000F)?.setDuration(100)

                } else {
                    if (temp == true) {
                        view?.findViewById<ImageView>(R.id.valid)?.animate()?.alpha(1F)
                            ?.translationY(-5F)?.setDuration(100)
                    }
                }
            })

        return view
    }

    private fun add_tag(view: View?) {
        var tag_input = view?.findViewById<EditText>(R.id.tag_input)?.text.toString()
        tag_input = tag_input.lowercase(Locale.getDefault())
        tag_input = tag_input.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }

        if (repas.tags.filter { s -> s == tag_input }
                .isEmpty() && tag_input != "" && tag_input != " " && tag_input != "  ") {
            repas.tags.add(tag_input)
        }
    }

    fun getPositionIngredient(ingredient: IngredientModel): Int {
        for (i in ingredientList.indices) {
            if (ingredientList[i].name == ingredient.name || ingredientList[i].name == ingredient.name + "s") {
                return i
            }
        }
        return -1
    }

    private fun addIngredient(view: View) {
        var repasIngredient = view.findViewById<EditText>(R.id.ingredient).text.toString()
        var quantite = view.findViewById<EditText>(R.id.quantite).text.toString()
        repasIngredient = repasIngredient.lowercase(Locale.getDefault())
        repasIngredient =
            repasIngredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        var ingredient = IngredientModel(
            UUID.randomUUID().toString(),
            repasIngredient,
            randomId,
            "None",
            quantite
        )

        if (getPositionIngredient(ingredient) != -1) {
            ingredient.id_categorie = ingredientList[getPositionIngredient(ingredient)].id_categorie
            ingredient.name = ingredientList[getPositionIngredient(ingredient)].name
        }

        if (listItem.filter { s -> s.name == ingredient.name }
                .isEmpty() && repasIngredient != "" && repasIngredient != " " && repasIngredient != "  " && quantite.isNotEmpty()) {
            listItem.add(ingredient)
        }

    }

    private fun switcher(switch: String) {
        if (switch == "ingredient") {
            view?.findViewById<ConstraintLayout>(R.id.recetteCard)?.visibility = View.GONE
            view?.findViewById<ConstraintLayout>(R.id.ingredientCard)?.visibility = View.VISIBLE
            view?.findViewById<View>(R.id.recette_soulignage)?.visibility = View.INVISIBLE
            view?.findViewById<View>(R.id.ingredient_soulignage)?.visibility = View.VISIBLE
        } else if (switch == "recette") {
            view?.findViewById<ConstraintLayout>(R.id.recetteCard)?.visibility = View.VISIBLE
            view?.findViewById<ConstraintLayout>(R.id.ingredientCard)?.visibility = View.GONE
            view?.findViewById<View>(R.id.recette_soulignage)?.visibility = View.VISIBLE
            view?.findViewById<View>(R.id.ingredient_soulignage)?.visibility = View.INVISIBLE
        }
    }

    private fun validateInput(view: View): Boolean {
        if (view.findViewById<EditText>(R.id.name_input).text.isEmpty()) {
            view.findViewById<EditText>(R.id.name_input).error = "Renseignez un nom de plat :)"
            view.findViewById<EditText>(R.id.name_input).requestFocus()
            val showMe = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            showMe.showSoftInput(
                view.findViewById<EditText>(R.id.name_input),
                InputMethodManager.SHOW_IMPLICIT
            )
            return false
        }
        if (view.findViewById<EditText>(R.id.description_input).text.isEmpty()) {
            view.findViewById<EditText>(R.id.description_input).error =
                "Renseignez une description de plat :)"
            view.findViewById<EditText>(R.id.description_input).requestFocus()
            val showMe = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            showMe.showSoftInput(
                view.findViewById<EditText>(R.id.description_input),
                InputMethodManager.SHOW_IMPLICIT
            )
            return false
        }
        if (repas.tags.isEmpty()) {
            view.findViewById<EditText>(R.id.tag_input).error =
                "Renseignez au moins un tags de plat :)"
            view.findViewById<EditText>(R.id.tag_input).requestFocus()
            val showMe = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            showMe.showSoftInput(
                view.findViewById<EditText>(R.id.tag_input),
                InputMethodManager.SHOW_IMPLICIT
            )
            return false
        }
        if (view.findViewById<EditText>(R.id.duree).text.isEmpty()) {
            view.findViewById<EditText>(R.id.duree).error = "Renseignez une dur??e de plat :)"
            view.findViewById<EditText>(R.id.duree).requestFocus()
            val showMe = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            showMe.showSoftInput(
                view.findViewById<EditText>(R.id.duree),
                InputMethodManager.SHOW_IMPLICIT
            )

            return false
        }
        if (view.findViewById<EditText>(R.id.quantite_input).text.isEmpty()) {
            view.findViewById<EditText>(R.id.quantite_input).error =
                "Renseignez une quantit?? de plat :)"
            view.findViewById<EditText>(R.id.quantite_input).requestFocus()
            val showMe = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            showMe.showSoftInput(
                view.findViewById<EditText>(R.id.quantite_input),
                InputMethodManager.SHOW_IMPLICIT
            )
            return false
        }
        if (view.findViewById<EditText>(R.id.recette_input).text.isEmpty()) {
            view.findViewById<EditText>(R.id.recette_input).error =
                "Renseignez une recette de plat :)"
            switcher("recette")
            view.findViewById<EditText>(R.id.recette_input).requestFocus()
            val showMe = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            showMe.showSoftInput(
                view.findViewById<EditText>(R.id.recette_input),
                InputMethodManager.SHOW_IMPLICIT
            )
            return false
        }
        return true
    }

    private fun sendform(view: View) {
        val repo = RepasRepository()
        val repo2 = IngredientRepository()

        val name = view.findViewById<EditText>(R.id.name_input).text.toString()
        val description = view.findViewById<EditText>(R.id.description_input).text.toString()
        val recette = view.findViewById<EditText>(R.id.recette_input).text.toString()
        val duree = view.findViewById<EditText>(R.id.duree).text.toString()
        val quantite = view.findViewById<EditText>(R.id.quantite_input).text.toString()

        if (validateInput(view)) {
            repas.id = randomId
            repas.createur = Firebase.auth.currentUser?.email.toString()

            if (!name.isBlank()) {
                repas.name = name
            }
            if (!description.isBlank()) {
                repas.description = description
            }
            if (!recette.isBlank()) {
                repas.recette = recette
            }
            if (!duree.isBlank()) {
                repas.duree = duree
            }
            if (!quantite.isBlank()) {
                repas.quantite = quantite
            }
            if (filePath != null) {
                uploadImage()
            } else {
                repas.id = randomId
                repo.updateRepas(repas)

                for (ingredientRepo in ingredientList.filter { s -> s.id_repas == repas.id }) {
                    if (!listItem.contains(ingredientRepo)) {
                        repo2.deleteIngredient(ingredientRepo)
                    }
                }

                for (ingredient in listItem) {
                    if (!ingredientList.contains(ingredient)) {
                        repo2.insertIngredient(ingredient)
                    }
                }

                listItem.forEachIndexed { index, ingredient ->
                    ingredient.rank = index
                    repo2.updateIngredient(ingredient)
                }
                val repo = RepasCommunRepository()
                if(view.findViewById<CheckBox>(R.id.share).isChecked){
                    repo.addRepasBddCo(repas, listItem)
                }

                context.loadFragment(RecetteFragment(context, repas, "None", "None", "None"))
                IngredientPopup(context, listItem).show()
                Toast.makeText(context, "Recette ajout??e !", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun launchGallery() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null || data.data == null) {
                return
            }

            filePath = data.data
            try {
                uploadedImage?.setImageURI(filePath)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    private fun uploadImage() {
        val repo = RepasRepository()
        val repo2 = IngredientRepository()
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Uploading File...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val fileName = "image${randomId}"
        val storageReference = FirebaseStorage.getInstance().getReference(fileName)
        storageReference.putFile(filePath!!).addOnSuccessListener {
            Toast.makeText(context, "Image saved to DB", Toast.LENGTH_SHORT).show()
            if (progressDialog.isShowing) progressDialog.dismiss()
            Firebase.storage.reference.child(fileName).downloadUrl.addOnSuccessListener {
                repas.imageUri = it.toString()
                repo.insertRepas(repas)
            }.addOnCompleteListener {
                for (ingredientRepo in ingredientList.filter { s -> s.id_repas == repas.id }) {
                    if (!listItem.contains(ingredientRepo)) {
                        repo2.deleteIngredient(ingredientRepo)
                    }
                }

                for (ingredient in listItem) {
                    if (!ingredientList.contains(ingredient)) {
                        repo2.insertIngredient(ingredient)
                    }
                }

                listItem.forEachIndexed { index, ingredient ->
                    ingredient.rank = index
                    repo2.updateIngredient(ingredient)
                }
                context.loadFragment(RecetteFragment(context, repas, "None", "None", "None"))

                IngredientPopup(context, listItem).show()
                val repo = RepasCommunRepository()
                if(view?.findViewById<CheckBox>(R.id.share)?.isChecked == true){
                    repo.addRepasBddCo(repas, listItem)
                }
                Toast.makeText(context, "Recette ajout??e !", Toast.LENGTH_SHORT).show()
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to get URI", Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener {
            if (progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }
    }

}