package fr.juju.myapplication

import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.net.URI

class RepasModel(
    var id:String = "fdsf",
    var name:String = "fdsfs",
    var description:String = "fdsfdsgfd",
    var imageUri: String = "fdsdg",
    var lien:String = "gfdgdssdfs",
    var recette:String = "fdsds",
    var tags: ArrayList<String> = arrayListOf("dsfs"),
    var duree: String = "fdsfdsf"
)

