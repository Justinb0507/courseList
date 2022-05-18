package fr.juju.myapplication

import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import fr.juju.myapplication.IngredientRepository.Singleton.ingredientList
import fr.juju.myapplication.RepasCommunRepository.Singleton.databaseRef
import fr.juju.myapplication.RepasCommunRepository.Singleton.repasCommunList

class RepasCommunRepository {
    object Singleton{
        private val BUCKET_URL: String = "gs://naturecollection-c9efc.appspot.com"
        var databaseRef = FirebaseDatabase.getInstance().getReference("/repasCommun")
        val repasCommunList = arrayListOf<RepasCommunModel>()
    }

    fun removeLink(){
        databaseRef = FirebaseDatabase.getInstance().getReference("/repasCommun")
        repasCommunList.clear()
    }

    fun updateData(callback:()-> Unit){
        databaseRef.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                repasCommunList.clear()
                for(ds in snapshot.children){
                    val repasCommun = ds.getValue(RepasCommunModel::class.java)
                    if (repasCommun != null){
                        repasCommunList.add(repasCommun)
                    }
                }
                callback()
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

    fun insertRepasCommun(repasCommun: RepasCommunModel) {
        databaseRef.child(repasCommun.id).setValue(repasCommun)
    }
    fun deleteRepasCommun(repasCommun: String) {
        databaseRef.child(repasCommun).removeValue()
    }
    fun addRepasBddCo(repas: RepasModel, ingredientsList : ArrayList<IngredientModel>) {
        val repo = RepasCommunRepository()
        repo.insertRepasCommun(
            RepasCommunModel(
                repas.id,
                repas.name,
                Firebase.auth.currentUser?.email.toString(),
                repas.description,
                repas.imageUri,
                repas.recette,
                repas.quantite,
                repas.tags,
                repas.duree,
                ingredientsList.filter{ s -> s.id_repas == repas.id} as ArrayList<IngredientModel>
            )
        )
    }

    fun editRepasBddCo(repas: RepasModel, ingredientsList : ArrayList<IngredientModel>){
        val repo = RepasCommunRepository()
        repo.deleteRepasCommun(repas.id)
        repo.insertRepasCommun(
            RepasCommunModel(
                repas.id,
                repas.name,
                repas.createur,
                repas.description,
                repas.imageUri,
                repas.recette,
                repas.quantite,
                repas.tags,
                repas.duree,
                ingredientsList.filter{ s -> s.id_repas == repas.id} as ArrayList<IngredientModel>
            )
        )
    }

    fun retrieveData(repasCommun: RepasCommunModel){
        val repo = RepasRepository()
        val repo2 = IngredientRepository()
        repo.insertRepas(
            RepasModel(
                repasCommun.id,
                repasCommun.name,
                repasCommun.description,
                repasCommun.imageUri,
                repasCommun.recette,
                repasCommun.quantite,
                repasCommun.tags,
                repasCommun.duree,
                repasCommun.createur,
                true
            ))
        for(ingredient in  repasCommun.ingredientsList){
            repo2.insertIngredient(ingredient)
        }
    }

}