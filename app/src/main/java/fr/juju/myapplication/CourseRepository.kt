package fr.juju.myapplication

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import fr.juju.myapplication.CourseRepository.Singleton.courseList
import fr.juju.myapplication.CourseRepository.Singleton.databaseRef

class CourseRepository {
    object Singleton{

        private val BUCKET_URL: String = "gs://naturecollection-c9efc.appspot.com"
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)
        val databaseRef = FirebaseDatabase.getInstance().getReference("course")
        val courseList = arrayListOf<CourseModel>()
    }


    fun updateData(callback:()-> Unit){
        databaseRef.addValueEventListener(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                courseList.clear()
                for(ds in snapshot.children){
                    val courseItem = ds.getValue(CourseModel::class.java)
                    if (courseItem != null){
                        courseList.add(courseItem)
                    }
                }
                callback()
            }
            override fun onCancelled(error: DatabaseError) {
            }

        })
    }
}