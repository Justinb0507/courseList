package fr.juju.myapplication

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
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

}