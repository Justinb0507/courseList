package fr.juju.myapplication.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Context.INPUT_METHOD_SERVICE
import android.view.inputmethod.InputMethodManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.WindowManager.LayoutParams.SOFT_INPUT_ADJUST_UNSPECIFIED
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import fr.juju.myapplication.*
import fr.juju.myapplication.IngredientRepository.Singleton.ingredientList
import fr.juju.myapplication.adapter.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import androidx.core.content.ContextCompat.getSystemService




class AddRepasFragment(
    private val context: MainActivity
)
    : Fragment() {

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

        listIngredientView.adapter = EditIngredientAdapter(context,listItem, R.layout.item_edit_ingredient_vertical)
        listIngredientView.layoutManager = LinearLayoutManager(context)
        //recup uploadilmage pour lui associer son composant
        uploadedImage = view.findViewById(R.id.preview_image)
        //recup le bouton pour charger l'image
        val pickupImage = view.findViewById<ImageView>(R.id.preview_image)
        pickupImage.setOnClickListener{
            launchGallery()
        }


        view.findViewById<ImageView>(R.id.valid).setOnClickListener{
            sendform(view)
        }

        var temp = false

        val addIngredientButton = view.findViewById<ImageView>(R.id.add_ingredient)
        addIngredientButton.setOnClickListener{
            if(view.findViewById<EditText>(R.id.ingredient).text.isNotEmpty()) {
                addIngredient(view)
                view.findViewById<EditText>(R.id.ingredient).setText("")
                view.findViewById<EditText>(R.id.quantite).setText("")
                view.findViewById<EditText>(R.id.ingredient).requestFocus()
                listIngredientView.adapter = EditIngredientAdapter(context,listItem, R.layout.item_edit_ingredient_vertical)
                listIngredientView.scrollToPosition(1500)
                temp = false
            }
            view.findViewById<EditText>(R.id.ingredient).visibility = View.VISIBLE
            view.findViewById<EditText>(R.id.quantite).visibility = View.VISIBLE
            addIngredientButton.animate().translationX(+790F).setDuration(150)
            var temp = false


        }
        val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.tags)
        collectionRecyclerView.adapter = EditTagsAdapter(context, repas.tags, R.layout.item_edit_tags_horizontal)
        collectionRecyclerView.scrollToPosition(300)
        val add_tagButton = view.findViewById<ImageView>(R.id.add_tag)
        add_tagButton.setOnClickListener{
            if(view.findViewById<EditText>(R.id.tag_input).text.isNotEmpty()){
                add_tag(view)
                collectionRecyclerView.adapter = EditTagsAdapter(context, repas.tags, R.layout.item_edit_tags_horizontal)
                view.findViewById<EditText>(R.id.tag_input).setText("")
                collectionRecyclerView.scrollToPosition(1500)
            }
            add_tagButton.animate().translationX(+250F).setDuration(150)
            view.findViewById<EditText>(R.id.tag_input).visibility = View.VISIBLE
            view.findViewById<EditText>(R.id.tag_input).requestFocus()
            val showMe = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            showMe.showSoftInput(view.findViewById<EditText>(R.id.tag_input), InputMethodManager.SHOW_IMPLICIT)

        }


        //Set to linerarlayout to ingredients
        view?.findViewById<ConstraintLayout>(R.id.recetteCard)?.visibility = View.GONE
        view?.findViewById<View>(R.id.recette_soulignage)?.visibility = View.INVISIBLE


        view.findViewById<TextView>(R.id.ingredients).setOnClickListener{
            switcher("ingredient")
        }
        view.findViewById<TextView>(R.id.recette).setOnClickListener{
            switcher("recette")
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
        var tag_input =  view?.findViewById<EditText>(R.id.tag_input)?.text.toString()
        tag_input = tag_input.lowercase(Locale.getDefault())
        tag_input = tag_input.replaceFirstChar { if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()) else it.toString() }

        if(repas.tags.filter{s-> s == tag_input}.isEmpty() && tag_input != "" && tag_input != " "&& tag_input != "  "){
            repas.tags.add(tag_input)
        }
    }

    private fun addIngredient(view : View){
        var repasIngredient =  view.findViewById<EditText>(R.id.ingredient).text.toString()
        var quantite = view.findViewById<EditText>(R.id.quantite).text.toString()
        repasIngredient = repasIngredient.lowercase(Locale.getDefault())
        repasIngredient = repasIngredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        var ingredient = IngredientModel(
            UUID.randomUUID().toString(),
            repasIngredient,
            randomId,
            "None",
            quantite
        )
        if(listItem.filter{s-> s.name == ingredient.name}.isEmpty() && repasIngredient != "" && repasIngredient != " "&& repasIngredient != "  " && quantite.isNotEmpty()){
            listItem.add(ingredient)
        }

    }

    private fun switcher(switch: String){
        if(switch == "ingredient"){
            view?.findViewById<ConstraintLayout>(R.id.recetteCard)?.visibility = View.GONE
            view?.findViewById<ConstraintLayout>(R.id.ingredientCard)?.visibility = View.VISIBLE
            view?.findViewById<View>(R.id.recette_soulignage)?.visibility = View.INVISIBLE
            view?.findViewById<View>(R.id.ingredient_soulignage)?.visibility = View.VISIBLE


        }
        else if(switch == "recette"){
            view?.findViewById<ConstraintLayout>(R.id.recetteCard)?.visibility = View.VISIBLE
            view?.findViewById<ConstraintLayout>(R.id.ingredientCard)?.visibility = View.GONE
            view?.findViewById<View>(R.id.recette_soulignage)?.visibility = View.VISIBLE
            view?.findViewById<View>(R.id.ingredient_soulignage)?.visibility = View.INVISIBLE
        }
    }

    private fun sendform(view : View) {
        val repo = RepasRepository()
        val repo2 = IngredientRepository()
        val name = view.findViewById<EditText>(R.id.name_input).text.toString()
        val description = view.findViewById<EditText>(R.id.description_input).text.toString()
        val lien = view.findViewById<EditText>(R.id.lien_input).text.toString()
        val recette = view.findViewById<EditText>(R.id.recette_input).text.toString()
        val duree = view.findViewById<EditText>(R.id.duree).text.toString()

        repas.id = randomId

        if (!name.isBlank()){
            repas.name = name
        }
        if (!description.isBlank()){
            repas.description = description
        }
        if (!lien.isBlank()){
            repas.lien = lien
        }
        if (!recette.isBlank()){
            repas.recette = recette
        }
        if (!duree.isBlank()){
            repas.duree = duree
        }

        if (filePath != null){
            uploadImage()
        }else {
            repas.id = randomId
            repo.updateRepas(repas)
            if (!listItem.filter { s->s.id_categorie == "None" }.isEmpty()){
                IngredientPopup(context,
                    listItem.filter { s->s.id_categorie == "None" } as ArrayList<IngredientModel>).show()
            }
            context.loadFragment(RecetteFragment(context,repas, "None", "None"))
        }

        for(ingredientRepo in ingredientList.filter { s->s.id_repas == repas.id }){
            if(!listItem.contains(ingredientRepo)){
                repo2.deleteIngredient(ingredientRepo)
            }
        }

        for (ingredient in listItem) {
            if(!ingredientList.contains(ingredient)){
                repo2.insertIngredient(ingredient)
            }
        }

        Toast.makeText(context, "Repas ajouté !", Toast.LENGTH_SHORT).show()
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
            if(data == null || data.data == null){
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


    private fun uploadImage(){
        val repo = RepasRepository()
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Uploading File...")
        progressDialog.setCancelable(false)
        progressDialog.show()

        val fileName = "image${randomId}"
        val storageReference = FirebaseStorage.getInstance().getReference(fileName)
        storageReference.putFile(filePath!!).addOnSuccessListener {
            Toast.makeText(context, "Image saved to DB", Toast.LENGTH_SHORT).show()
            if(progressDialog.isShowing) progressDialog.dismiss()
            Firebase.storage.reference.child(fileName).downloadUrl.addOnSuccessListener {
                repas.imageUri = it.toString()
                repo.insertRepas(repas)
                if (!listItem.filter { s->s.id_categorie == "None" }.isEmpty()){
                    IngredientPopup(context,
                        listItem.filter { s->s.id_categorie == "None" } as ArrayList<IngredientModel>).show()
                }
                context.loadFragment(RecetteFragment(context,repas, "None", "None"))
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to get URI", Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener{
            if(progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }



    }



}