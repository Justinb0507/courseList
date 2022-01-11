package fr.juju.myapplication.fragments
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import fr.juju.myapplication.*
import fr.juju.myapplication.IngredientRepository.Singleton.ingredientList
import fr.juju.myapplication.adapter.EditIngredientAdapter
import fr.juju.myapplication.adapter.IngredientAdapter
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class EditRepasFragment(
    private val context: MainActivity,
    private val currentRepas: RepasModel,
    private val ingredientList: List<IngredientModel>
) : Fragment() {

    var ingredients = arrayListOf<IngredientModel>()
    private var uploadedImage: ImageView? = null
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val repo = RepasRepository()
        val repo2 = IngredientRepository()

        ingredients = ingredientList.filter { s->s.id_repas == currentRepas.id } as ArrayList<IngredientModel>

        val view = inflater?.inflate(R.layout.fragment_edit_repas, container, false)
        val listIngredientView = view.findViewById<RecyclerView>(R.id.list_ingredient)

        listIngredientView.adapter = EditIngredientAdapter(context,ingredients, R.layout.item_edit_ingredient_vertical)
        listIngredientView.layoutManager = LinearLayoutManager(context)

        view.findViewById<EditText>(R.id.name_input).hint = currentRepas.name
        view.findViewById<EditText>(R.id.description_input).hint = currentRepas.description
        view.findViewById<EditText>(R.id.lien_input).hint = currentRepas.lien
        view.findViewById<EditText>(R.id.recette_input).hint = currentRepas.recette
        view.findViewById<EditText>(R.id.duree).hint = currentRepas.duree
        view.findViewById<TextView>(R.id.tags).hint = currentRepas.tags.toString()

        uploadedImage = view.findViewById<ImageView>(R.id.image_item)
        Glide.with(context).load(Uri.parse(currentRepas.imageUri)).into(view.findViewById<ImageView>(R.id.image_item))


        view.findViewById<ImageView>(R.id.valid).setOnClickListener{
            updateRepas(view)
        }
        view.findViewById<ImageView>(R.id.trash).setOnClickListener{
            repo.deleteRepas(currentRepas)
            for (ingredient in ingredientList.filter { s->s.id_repas == currentRepas.id } as ArrayList<IngredientModel>){
                repo2.deleteIngredient(ingredient)
            }
            context.loadFragment(filtreRepasFragment(context))
        }
        val addIngredientButton = view.findViewById<Button>(R.id.add_ingredient)
        addIngredientButton.setOnClickListener{
            addIngredient(view)
            listIngredientView.adapter = EditIngredientAdapter(context,ingredients, R.layout.item_edit_ingredient_vertical)
            view.findViewById<EditText>(R.id.ingredient).setText("")
        }

        view.findViewById<CardView>(R.id.cardView).setOnClickListener{
            launchGallery()
        }

        return view
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

            val fileName = "image${currentRepas.id}"
            val storageReference = FirebaseStorage.getInstance().getReference(fileName)
            storageReference.putFile(filePath!!).addOnSuccessListener {
                Toast.makeText(context, "Saved to DB", Toast.LENGTH_SHORT).show()
                if(progressDialog.isShowing) progressDialog.dismiss()
                Firebase.storage.reference.child(fileName).downloadUrl.addOnSuccessListener {
                    currentRepas.imageUri = it.toString()
                    repo.updateRepas(currentRepas)
                    context.loadFragment(RecetteFragment(context,currentRepas))
                }.addOnFailureListener {
                    Toast.makeText(context, "Failed to get URI", Toast.LENGTH_SHORT).show()
                }

            }.addOnFailureListener{
                if(progressDialog.isShowing) progressDialog.dismiss()
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            }



    }

    private fun addIngredient(view : View){
        var repasIngredient =  view.findViewById<EditText>(R.id.ingredient).text.toString()
        repasIngredient = repasIngredient.lowercase(Locale.getDefault())
        repasIngredient = repasIngredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()) else it.toString() }

        var repasIngredientquantite =  view.findViewById<EditText>(R.id.quantite).text.toString()


        var ingredient = IngredientModel(
            UUID.randomUUID().toString(),
            repasIngredient,
            currentRepas.id,
            "None",
            repasIngredientquantite
        )
        if(ingredients.filter{s-> s.name == ingredient.name}.isEmpty() && repasIngredient != "" && repasIngredient != " "&& repasIngredient != "  " && ingredient.quantite.isNotEmpty()){
            ingredients.add(ingredient)
        }

    }

    private fun updateRepas(view : View) {
        val repo2 = IngredientRepository()
        val name = view.findViewById<EditText>(R.id.name_input).text.toString()
        val description = view.findViewById<EditText>(R.id.description_input).text.toString()
        val lien = view.findViewById<EditText>(R.id.lien_input).text.toString()
        val recette = view.findViewById<EditText>(R.id.recette_input).text.toString()
        val duree = view.findViewById<EditText>(R.id.duree).text.toString()
        val tags = view.findViewById<EditText>(R.id.tags).text.toString()

        if (!name.isBlank()){
            currentRepas.name = name
        }
        if (!description.isBlank()){
            currentRepas.description = description
        }
        if (!lien.isBlank()){
            currentRepas.lien = lien
        }
        if (!recette.isBlank()){
            currentRepas.recette = recette
        }
        if (!duree.isBlank()){
            currentRepas.duree = duree
        }
        if (!tags.isBlank()){
            for (tag in tags.split(" ")){
                currentRepas.tags.add(tag)
            }
        }

        if (filePath != null){
            uploadImage()
        }

        for(ingredientRepo in ingredientList.filter { s->s.id_repas == currentRepas.id }){
            if(!ingredients.contains(ingredientRepo)){
                repo2.deleteIngredient(ingredientRepo)
            }
        }

        for (ingredient in ingredients) {
            if(!ingredientList.contains(ingredient)){
                repo2.insertIngredient(ingredient)
            }
        }

        Toast.makeText(context, "Repas modifié !", Toast.LENGTH_SHORT).show()
        if (!ingredients.filter { s->s.id_categorie == "None" }.isEmpty()){
            IngredientPopup(context,
                ingredients.filter { s->s.id_categorie == "None" } as ArrayList<IngredientModel>).show()
        }


    }
}
