package fr.juju.myapplication

import android.net.Uri
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import java.net.URI

class RepasModel(
    val id:String = "Repas0",
    var name:String = "NomPlat",
    var description:String = "Vla la description",
    var imageUri: String = "https://cdn.pixabay.com/photo/2015/11/19/10/38/food-1050813_960_720.jpg",
    var lien:String = "https://www.marmiton.org/repas-de-fetes/album1316891/mon-apero-dinatoire-du-nouvel-an-0.html#p1",
    var recette:String = "Premierement manger pas le plat avant de l'avoir cuisiné sinon vous allez pas kiffé jvous l'dis moi eheh",
    var tags: ArrayList<String> = arrayListOf("test", "test1","test2"),
    var duree: String = "45min"
)

