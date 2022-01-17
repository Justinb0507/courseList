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
    var name:String = "Coucou",
    var description:String = "Potite description",
    var imageUri: String = "https://firebasestorage.googleapis.com/v0/b/naturecollection-c9efc.appspot.com/o/image4750b61d-efca-4735-8453-aa691b78a988?alt=media&token=099556a0-a465-4c5b-9238-22dbfc4e8497",
    var lien:String = "www.google.fr",
    var recette:String = "cuisinez",
    var tags: ArrayList<String> = arrayListOf("test", "test1"),
    var duree: String = "10"
)

