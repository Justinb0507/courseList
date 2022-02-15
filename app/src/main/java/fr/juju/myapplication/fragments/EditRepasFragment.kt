package fr.juju.myapplication.fragments
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import fr.juju.myapplication.*
import fr.juju.myapplication.SemainierRepository.Singleton.semainierList
import fr.juju.myapplication.adapter.EditIngredientAdapter
import fr.juju.myapplication.adapter.EditTagsAdapter
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList
import android.view.ViewTreeObserver.OnScrollChangedListener
import android.view.inputmethod.InputMethodManager
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.ItemTouchHelper


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

        ingredients = ingredientList.filter { s -> s.id_repas == currentRepas.id } as ArrayList<IngredientModel>

        val view = inflater?.inflate(R.layout.fragment_edit_repas, container, false)
        val listIngredientView = view.findViewById<RecyclerView>(R.id.list_ingredient)
        listIngredientView.adapter = EditIngredientAdapter(context, ingredients, R.layout.item_edit_ingredient_vertical)
        listIngredientView.layoutManager = LinearLayoutManager(context)
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

        var simpleCallback = object : ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP or(ItemTouchHelper.DOWN), 0){
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                var startPosition = viewHolder.adapterPosition
                var endPosition = target.adapterPosition
                Collections.swap(ingredients, startPosition, endPosition)
                listIngredientView.adapter?.notifyItemMoved(startPosition, endPosition)

                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
            }

        }

        val itemTouchHelper = ItemTouchHelper(simpleCallback)
        itemTouchHelper.attachToRecyclerView(listIngredientView)

        view.findViewById<EditText>(R.id.name_input).setText(currentRepas.name)
        view.findViewById<EditText>(R.id.description_input).setText(currentRepas.description)
        if(currentRepas.lien != ""){
            view.findViewById<EditText>(R.id.lien_input).setText(currentRepas.lien)
        }
        view.findViewById<EditText>(R.id.recette_input).setText(currentRepas.recette)
        view.findViewById<EditText>(R.id.duree).setText(currentRepas.duree)

        uploadedImage = view.findViewById<ImageView>(R.id.image)
        Glide.with(context).load(Uri.parse(currentRepas.imageUri))
            .into(view.findViewById<ImageView>(R.id.image))


        view.findViewById<ImageView>(R.id.valid).setOnClickListener {
            updateRepas(view)
        }

        val repoSemainier = SemainierRepository()
        view.findViewById<ImageView>(R.id.trash).setOnClickListener {
            var builder = AlertDialog.Builder(context)
            builder.setTitle("Oulaaaaaa !")
            builder.setMessage("Tu veux vraiment supprimer la recette ?")
            builder.setPositiveButton("Oui", DialogInterface.OnClickListener { dialog, id ->

                repo.deleteRepas(currentRepas)
                for (ingredient in ingredientList.filter { s -> s.id_repas == currentRepas.id } as ArrayList<IngredientModel>) {
                    repo2.deleteIngredient(ingredient)
                }
                for (day in semainierList.filter { s -> s.midi == currentRepas.id } as ArrayList<SemainierModel>) {
                    repoSemainier.resetMidi(day.id_semainier)
                }
                for (day in semainierList.filter { s -> s.soir == currentRepas.id } as ArrayList<SemainierModel>) {
                    repoSemainier.resetSoir(day.id_semainier)
                }
                for (day in semainierList.filter { s -> s.apero == currentRepas.id } as ArrayList<SemainierModel>) {
                    repoSemainier.resetApero(day.id_semainier)
                }
                val storageRef = Firebase.storage.reference.child("image${currentRepas.id}")
                storageRef.delete().addOnSuccessListener {
                    Toast.makeText(context, "Recette supprimée !", Toast.LENGTH_SHORT).show()
                }.addOnFailureListener {
                    Toast.makeText(context, "Recette non supprimée !", Toast.LENGTH_SHORT).show()
                }
                context.loadFragment(FiltreRepasFragment(context, "None", "None", "None"))
                dialog.cancel()
            })
            builder.setNegativeButton("Non", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })
            var alert: AlertDialog = builder.create()
            alert.show()
        }

        var temp = false

        val addIngredientButton = view.findViewById<ImageView>(R.id.add_ingredient)

        view.findViewById<EditText>(R.id.quantite).addTextChangedListener(
            object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if(s.contains("\n")) {
                        view.findViewById<EditText>(R.id.quantite).setText(s.toString().replace("\n",""))
                        addIngredientButton.performClick()
                    }}

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
                    if(s.contains("\n")) {
                        view.findViewById<EditText>(R.id.ingredient).setText(s.toString().replace("\n",""))
                        view.findViewById<EditText>(R.id.quantite).requestFocus()
                    }}

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


        addIngredientButton.setOnClickListener{
            if(view.findViewById<EditText>(R.id.ingredient).text.isNotEmpty() && view.findViewById<EditText>(R.id.quantite).text.isNotEmpty()) {
                addIngredient(view)
                view.findViewById<EditText>(R.id.ingredient).setText("")
                view.findViewById<EditText>(R.id.quantite).setText("")
                view.findViewById<EditText>(R.id.ingredient).requestFocus()
                listIngredientView.adapter = EditIngredientAdapter(context,ingredients, R.layout.item_edit_ingredient_vertical)
                listIngredientView.scrollToPosition(1500)
                temp = false
            }
            view.findViewById<EditText>(R.id.ingredient).visibility = View.VISIBLE
            view.findViewById<EditText>(R.id.quantite).visibility = View.VISIBLE
            addIngredientButton.animate().translationX(+790F).setDuration(150)
            view.findViewById<EditText>(R.id.ingredient).requestFocus()
            val showMe = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            showMe.showSoftInput(view.findViewById<EditText>(R.id.ingredient), InputMethodManager.SHOW_IMPLICIT)
        }
        val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.tags)
        collectionRecyclerView.adapter = EditTagsAdapter(context, currentRepas.tags, R.layout.item_edit_tags_horizontal)
        collectionRecyclerView.scrollToPosition(300)
        val add_tagButton = view.findViewById<ImageView>(R.id.add_tag)
        add_tagButton.setOnClickListener{
            if(view.findViewById<EditText>(R.id.tag_input).text.isNotEmpty()){
                add_tag(view)
                collectionRecyclerView.adapter = EditTagsAdapter(context, currentRepas.tags, R.layout.item_edit_tags_horizontal)
                view.findViewById<EditText>(R.id.tag_input).setText("")
                collectionRecyclerView.scrollY = 1500
                collectionRecyclerView.scrollX = 1500
            }
            add_tagButton.animate().translationX(+250F).setDuration(150)
            view.findViewById<EditText>(R.id.tag_input).visibility = View.VISIBLE
            view.findViewById<EditText>(R.id.tag_input).requestFocus()
            val showMe = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            showMe.showSoftInput(view.findViewById<EditText>(R.id.tag_input), InputMethodManager.SHOW_IMPLICIT)
        }
        view.findViewById<EditText>(R.id.tag_input).addTextChangedListener(
            object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if(s.contains("\n")) {
                        view.findViewById<EditText>(R.id.tag_input).setText(s.toString().replace("\n",""))
                        add_tagButton.performClick()
                    }}

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

        view.findViewById<ImageView>(R.id.image).setOnClickListener{
            launchGallery()
        }
        view.findViewById<ImageView>(R.id.image_refresh).setOnClickListener{
            launchGallery()
        }

        //Set to linerarlayout to ingredients
        view?.findViewById<ConstraintLayout>(R.id.recetteCard)?.visibility = View.GONE
        view?.findViewById<View>(R.id.recette_soulignage)?.visibility = View.INVISIBLE


        view.findViewById<TextView>(R.id.ingredients).setOnClickListener{
            switcher("ingredient")
        }
        view.findViewById<TextView>(R.id.recette).setOnClickListener{
            switcher("recette")
            view.findViewById<EditText>(R.id.recette_input).requestFocus()
            val showMe = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            showMe.showSoftInput(view.findViewById<EditText>(R.id.recette_input), InputMethodManager.SHOW_IMPLICIT)
            view.findViewById<EditText>(R.id.recette_input).setSelection(view.findViewById<EditText>(R.id.recette_input).getText().length);
        }

        var scrollView = view.findViewById<ScrollView>(R.id.scrollView)
        scrollView.getViewTreeObserver()
            .addOnScrollChangedListener(OnScrollChangedListener {
                if (scrollView.getChildAt(0).getBottom()
                    <= scrollView.getHeight() + scrollView.getScrollY()
                ) {
                    temp = true
                    view?.findViewById<ImageView>(R.id.valid)?.animate()?.alpha(0F)?.translationY(+1000F)?.setDuration(100)

                } else {
                    if(temp == true){
                        view?.findViewById<ImageView>(R.id.valid)?.animate()?.alpha(1F)?.translationY(-5F)?.setDuration(100)
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

        if(currentRepas.tags.filter{s-> s == tag_input}.isEmpty() && tag_input != "" && tag_input != " "&& tag_input != "  "){
            currentRepas.tags.add(tag_input)
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
                Toast.makeText(context, "Failed to change image", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(context, "Image saved to DB", Toast.LENGTH_SHORT).show()
            if(progressDialog.isShowing) progressDialog.dismiss()
            Firebase.storage.reference.child(fileName).downloadUrl.addOnSuccessListener {
                currentRepas.imageUri = it.toString()
                repo.updateRepas(currentRepas)
            }.addOnFailureListener {
                Toast.makeText(context, "Failed to get URI", Toast.LENGTH_SHORT).show()
            }

        }.addOnFailureListener{
            if(progressDialog.isShowing) progressDialog.dismiss()
            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show()
        }



    }

    fun getPositionIngredient(ingredient: IngredientModel): Int {
        for (i in ingredientList.indices){
            if (ingredientList[i].name == ingredient.name || ingredientList[i].name == ingredient.name+"s"){
                return i
            }
        }
        return -1
    }

    private fun addIngredient(view : View){
        var repasIngredient =  view.findViewById<EditText>(R.id.ingredient).text.toString()
        repasIngredient = repasIngredient.lowercase(Locale.getDefault())
        repasIngredient = repasIngredient.replaceFirstChar { if (it.isLowerCase()) it.titlecase(
            Locale.getDefault()) else it.toString() }

        var repasIngredientquantite =  view.findViewById<EditText>(R.id.quantite).text.toString()
        if(repasIngredientquantite.contains("\n")){
            repasIngredientquantite = repasIngredientquantite.split("\n")[0]
        }

        var ingredient = IngredientModel(
            UUID.randomUUID().toString(),
            repasIngredient,
            currentRepas.id,
            "None",
            repasIngredientquantite,
            0
        )

        if(getPositionIngredient(ingredient) != -1){
            ingredient.id_categorie = ingredientList[getPositionIngredient(ingredient)].id_categorie
            ingredient.name = ingredientList[getPositionIngredient(ingredient)].name
        }

        if(ingredients.filter{s-> s.name == ingredient.name}.isEmpty() && repasIngredient != "" && repasIngredient != " "&& repasIngredient != "  " && ingredient.quantite.isNotEmpty()){
            ingredients.add(ingredient)
        }

    }

    private fun updateRepas(view : View) {
        val repo = RepasRepository()
        val repo2 = IngredientRepository()
        val name = view.findViewById<EditText>(R.id.name_input).text.toString()
        val description = view.findViewById<EditText>(R.id.description_input).text.toString()
        val lien = view.findViewById<EditText>(R.id.lien_input).text.toString()
        val recette = view.findViewById<EditText>(R.id.recette_input).text.toString()
        val duree = view.findViewById<EditText>(R.id.duree).text.toString()

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

        if (filePath != null){
            uploadImage()
        }
        else {
            repo.updateRepas(currentRepas)
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
        ingredients.forEachIndexed { index, ingredient ->
            ingredient.rank = index
            repo2.updateIngredient(ingredient)
        }

        IngredientPopup(context,ingredients).show()
        context.loadFragment(RecetteFragment(context,currentRepas, "None", "None", "None"))
        Toast.makeText(context, "Repas modifié !", Toast.LENGTH_SHORT).show()
    }
}
