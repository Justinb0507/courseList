package fr.juju.myapplication

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import fr.juju.myapplication.CourseRepository.Singleton.courseList
import fr.juju.myapplication.CourseRepository.Singleton.databaseRef
import java.util.*

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

    fun deleteCourseItem(courseModel: CourseModel){
      databaseRef.child(courseModel.id).removeValue()
    }

    fun updateCourseItem(courseModel: CourseModel) {
        databaseRef.child(courseModel.id).setValue(courseModel)
    }

    fun insertCourseItem(courseModel: CourseModel) {
        databaseRef.child(courseModel.id).setValue(courseModel)
    }

}