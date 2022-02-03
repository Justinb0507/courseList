package fr.juju.myapplication

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import fr.juju.myapplication.SemainierSuivantRepository.Singleton.databaseRef
import fr.juju.myapplication.SemainierSuivantRepository.Singleton.semainierSuivantList
import javax.security.auth.callback.Callback

class SemainierSuivantRepository {

    object Singleton{

        private val BUCKET_URL: String = "gs://naturecollection-c9efc.appspot.com"

        //se connecter à notre espace de stockage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)
        val databaseRef = FirebaseDatabase.getInstance().getReference("semainierSuivant")
        //Créer une liste qui va contenir les plantes
        val semainierSuivantList = arrayListOf<SemainierModel>()

    }
    fun updateData(callback:()-> Unit){
        databaseRef.addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                semainierSuivantList.clear()
                //recup la liste
                for(ds in snapshot.children){
                    //construire un object Plant
                    val day = ds.getValue(SemainierModel::class.java)
                    if (day != null){
                        semainierSuivantList.add(day)
                    }
                }
                //actionner le callback
                callback()
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })    }

    fun resetMidi(day_name: String) {
        var day = semainierSuivantList.filter { s->s.id_semainier == day_name }[0]
        day.midi = "None"
        databaseRef.child(day.id_semainier).setValue(day)
    }
    fun resetSoir(day_name: String) {
        var day = semainierSuivantList.filter { s->s.id_semainier == day_name }[0]
        day.soir= "None"
        databaseRef.child(day.id_semainier).setValue(day)
    }
    fun resetApero(day_name: String) {
        var day = semainierSuivantList.filter { s->s.id_semainier == day_name }[0]
        day.apero = "None"
        databaseRef.child(day.id_semainier).setValue(day)
    }
    fun setMidi(selectedDay: String, id_repas: String){
        var day = semainierSuivantList.filter { s->s.id_semainier == selectedDay }[0]
        day.midi = id_repas
        databaseRef.child(selectedDay).setValue(day)
    }
    fun setSoir(selectedDay: String, id_repas: String){
        var day = semainierSuivantList.filter { s->s.id_semainier == selectedDay }[0]
        day.soir = id_repas
        databaseRef.child(selectedDay).setValue(day)
    }
    fun setApero(selectedDay: String, id_repas: String){
        var day = semainierSuivantList.filter { s->s.id_semainier == selectedDay }[0]
        day.apero = id_repas
        databaseRef.child(selectedDay).setValue(day)
    }

}