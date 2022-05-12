package fr.juju.myapplication

import android.app.Dialog
import android.os.Bundle
import android.view.Window
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.juju.myapplication.CategorieRepository.Singleton.categorieList
import fr.juju.myapplication.adapter.PopupIngredientAdapter


class IngredientPopup(private val context: MainActivity, private val ingredientsInput: ArrayList<IngredientModel>)
    : Dialog(context) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.ingredient_popup)
        val listIngredientView = findViewById<RecyclerView>(R.id.recycler)
        val ingredients = arrayListOf<IngredientModel>()
        for (item in ingredientsInput){
            if (item.id_categorie == "None"){
                ingredients.add(item)
            }
        }
        for (item in ingredientsInput){
            if (item.id_categorie != "None"){
                ingredients.add(item)
            }
        }

        listIngredientView.adapter = PopupIngredientAdapter(context,ingredients, R.layout.item_edit_ingredient_categorie)
        listIngredientView.layoutManager = LinearLayoutManager(context)
        setupCloseButton()
        setupValidButton()
    }

    private fun setupValidButton() {
        findViewById<ImageView>(R.id.valid).setOnClickListener{
            Toast.makeText(context, "Catégories des ingrédients enregistrées !", Toast.LENGTH_SHORT).show()
            dismiss()
        }
    }

    private fun setupCloseButton(){
        findViewById<ImageView>(R.id.close_button).setOnClickListener{
            dismiss()
        }
    }


}
