package fr.juju.myapplication

import android.net.Uri
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import fr.juju.myapplication.RepasRepository.Singleton.databaseRef
import fr.juju.myapplication.RepasRepository.Singleton.repasList
import java.util.*

class RepasRepository {

    object Singleton{

        private val BUCKET_URL: String = "gs://naturecollection-c9efc.appspot.com"

        //se connecter à notre espace de stockage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)
        //se co à la ref plante
        val databaseRef = FirebaseDatabase.getInstance().getReference("repas")
        //Créer une liste qui va contenir les plantes
        val repasList = arrayListOf<RepasModel>()

    }


    fun updateData(callback:()-> Unit){
        //absorber les donées depuis la db ref -> les mettre dans la liste
        databaseRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                repasList.clear()
                //recup la liste
                for(ds in snapshot.children){
                    //construire un object Plant
                    val repas = ds.getValue(RepasModel::class.java)
                    if (repas != null){
                        repasList.add(repas)
                    }
                }
                //actionner le callback
                callback()
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
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

    fun insertRepas(repas: RepasModel) {
        repas.name = formattage(repas.name)
        repas.description = formattage(repas.description)
        repas.recette = formattage(repas.recette)
        var tags = arrayListOf<String>()
        for (tag in repas.tags){
            tags.add(formattage(tag))
        }
        repas.tags.clear()
        repas.tags = tags
        databaseRef.child(repas.id).setValue(repas)
    }
    fun deleteRepas(repas: RepasModel) {
        databaseRef.child(repas.id).removeValue()
    }
    fun updateRepas(repas: RepasModel) {
        repas.name = formattage(repas.name)
        repas.description = formattage(repas.description)
        repas.recette = formattage(repas.recette)
        var tags = arrayListOf<String>()
        for (tag in repas.tags){
            tags.add(formattage(tag))
        }
        repas.tags.clear()
        repas.tags = tags
        databaseRef.child(repas.id).setValue(repas)
    }

}