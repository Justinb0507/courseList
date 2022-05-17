package fr.juju.myapplication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import fr.juju.myapplication.CategorieRepository.Singleton.authUid
import fr.juju.myapplication.CategorieRepository.Singleton.categorieList
import fr.juju.myapplication.CategorieRepository.Singleton.databaseRef
import java.util.*

class CategorieRepository {
    object Singleton{
        private val BUCKET_URL: String = "gs://naturecollection-c9efc.appspot.com"
        //se connecter à notre espace de stockage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)
        var authUid =  FirebaseAuth.getInstance().uid
        var databaseRef = FirebaseDatabase.getInstance().getReference(authUid.toString() + "/categorie")
        //Créer une liste qui va contenir les plantes
        val categorieList = arrayListOf<CategorieModel>()
    }

    fun removeLink(){
        authUid =  FirebaseAuth.getInstance().uid
        databaseRef = FirebaseDatabase.getInstance().getReference(authUid.toString() + "/categorie")
        categorieList.clear()
    }

    fun updateData(callback:()-> Unit){
        databaseRef.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                categorieList.clear()
                for(ds in snapshot.children){
                    val categorie = ds.getValue(CategorieModel::class.java)
                    if (categorie != null){
                        categorieList.add(categorie)
                    }
                }
                callback()
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }

}