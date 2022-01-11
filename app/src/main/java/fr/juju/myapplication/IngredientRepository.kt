package fr.juju.myapplication

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import fr.juju.myapplication.IngredientRepository.Singleton.databaseRef
import fr.juju.myapplication.IngredientRepository.Singleton.ingredientList
import java.lang.Exception
import java.util.*

class IngredientRepository {


    object Singleton{

        private val BUCKET_URL: String = "gs://naturecollection-c9efc.appspot.com"

        //se connecter à notre espace de stockage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)
        //se co à la ref plante
        val databaseRef = FirebaseDatabase.getInstance().getReference("ingredients")
        //Créer une liste qui va contenir les plantes
        val ingredientList = arrayListOf<IngredientModel>()
    }

    fun updateData(callback:()-> Unit){
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ingredientList.clear()
                for(ds in snapshot.children){
                    val ingredient = ds.getValue(IngredientModel::class.java)
                    if (ingredient != null){
                        ingredientList.add(ingredient)
                    }
                }
                callback()
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun insertIngredient(ingredient: IngredientModel) {

        if(ingredientList.filter{s-> s.name == ingredient.name}.isEmpty()){
            databaseRef.child(ingredient.id).setValue(ingredient)
        }
        if(getPosition(ingredient) != -1){
            ingredient.id_categorie = ingredientList[getPosition(ingredient)].id_categorie
            databaseRef.child(ingredient.id).setValue(ingredient)
        }

    }

    fun getPosition(ingredient: IngredientModel): Int {
        for (i in ingredientList.indices){
            if (ingredientList[i].name == ingredient.name){
                return i
            }
        }
        return -1
    }

    private fun formattage(string: String): String {
        var returnValue : String
        returnValue = string.lowercase(Locale.getDefault())
        returnValue = returnValue.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
        return returnValue
    }

    fun deleteIngredient(ingredient: IngredientModel){
        databaseRef.child(ingredient.id).removeValue()
    }

    fun updateIngredient(ingredient: IngredientModel) {
        ingredient.name = formattage(ingredient.name)
        databaseRef.child(ingredient.id).setValue(ingredient)
    }


}