package fr.juju.myapplication

import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.net.URI
import java.util.*
import kotlin.collections.ArrayList

class RepasModel(
    var id:String = UUID.randomUUID().toString(),
    var name:String = "",
    var description:String = "",
    var imageUri: String = "",
    var lien:String = "",
    var recette:String = "",
    var tags: ArrayList<String> = arrayListOf(),
    var duree: String = ""
)

