package fr.juju.myapplication

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import fr.juju.myapplication.SemainierRepository.Singleton.databaseRef
import fr.juju.myapplication.SemainierRepository.Singleton.semainierList
import javax.security.auth.callback.Callback

class SemainierRepository {

    object Singleton{

        private val BUCKET_URL: String = "gs://naturecollection-c9efc.appspot.com"

        //se connecter à notre espace de stockage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)
        //se co à la ref plante
        val databaseRef = FirebaseDatabase.getInstance().getReference("semainier")
        //Créer une liste qui va contenir les plantes
        val semainierList = arrayListOf<SemainierModel>()

    }
    fun updateData(callback:()-> Unit){
        databaseRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                semainierList.clear()
                //recup la liste
                for(ds in snapshot.children){
                    //construire un object Plant
                    val repas = ds.getValue(SemainierModel::class.java)
                    if (repas != null){
                        semainierList.add(repas)
                    }
                }
                //actionner le callback
                callback()
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })    }
}