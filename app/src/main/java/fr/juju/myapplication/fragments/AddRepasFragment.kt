package fr.juju.myapplication.fragments

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import fr.juju.myapplication.*
import fr.juju.myapplication.adapter.*
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class AddRepasFragment(
    private val context: MainActivity
)
    : Fragment() {

    private var uploadedImage: ImageView? = null
    private val PICK_IMAGE_REQUEST = 71
    private var filePath: Uri? = null
    private val randomId = UUID.randomUUID().toString()
    val listItem = arrayListOf<IngredientModel>()

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
        val pickupImageButton = view.findViewById<Button>(R.id.upload_button)
        pickupImageButton.setOnClickListener{
            launchGallery()
        }

        val confirmButton = view.findViewById<Button>(R.id.confirm_button)
        confirmButton.setOnClickListener{
            sendform(view)
        }

        val addIngredientButton = view.findViewById<Button>(R.id.add_ingredient)
        addIngredientButton.setOnClickListener{
            addIngredient(view)
            listIngredientView.adapter = EditIngredientAdapter(context,listItem, R.layout.item_edit_ingredient_vertical)
            view.findViewById<EditText>(R.id.ingredient).setText("")
            view.findViewById<EditText>(R.id.quantite).setText("")
        }

        return view
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

    private fun sendform(view : View) {
        //hebergement bucket
        val repo = RepasRepository()
        val repo2 = IngredientRepository()
        val repasName = view.findViewById<EditText>(R.id.name_input).text.toString()
        val repasDescription = view.findViewById<EditText>(R.id.description_input).text.toString()
        val repasLien =  view.findViewById<EditText>(R.id.lien).text.toString()
        val repasRecette =  view.findViewById<EditText>(R.id.recette).text.toString()
        val tags = view.findViewById<EditText>(R.id.tags).text.toString()
        val duree = view.findViewById<EditText>(R.id.duree).text.toString()

        val tagsList = arrayListOf<String>()
        for (tag in tags.split(" ")){
            tagsList.add(tag)
        }
        val repas = RepasModel(
            randomId,
            repasName,
            repasDescription,
            filePath.toString(),
            repasLien,
            repasRecette,
            tagsList,
            duree
        )

        uploadImage()

        for (ingredient in listItem) {
            repo2.insertIngredient(ingredient)
        }
        repo.insertRepas(repas)
        context.loadFragment(RecetteFragment(context, repas))
        Toast.makeText(context, "Repas ajouté !", Toast.LENGTH_SHORT).show();
        if (!listItem.filter { s->s.id_categorie == "None" }.isEmpty()){
            IngredientPopup(context,
                listItem.filter { s->s.id_categorie == "None" } as ArrayList<IngredientModel>).show()
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
        if(filePath != null){
            val progressDialog = ProgressDialog(context)
            progressDialog.setMessage("Uploading File...")
            progressDialog.setCancelable(false)
            progressDialog.show()

            val fileName = "image$randomId"
            val storageReference = FirebaseStorage.getInstance().getReference(fileName)
            storageReference.putFile(filePath!!).addOnSuccessListener {
                Toast.makeText(context, "Saved to DB", Toast.LENGTH_SHORT).show()
                if(progressDialog.isShowing) progressDialog.dismiss()
            }.addOnFailureListener{
                if(progressDialog.isShowing) progressDialog.dismiss()
                Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
            }

        }
    }
}