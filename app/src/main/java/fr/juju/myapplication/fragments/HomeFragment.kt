package fr.juju.myapplication.fragments

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage

import fr.juju.myapplication.MainActivity
import fr.juju.myapplication.R
import fr.juju.myapplication.RepasModel
import fr.juju.myapplication.RepasRepository.Singleton.repasList
import fr.juju.myapplication.SemainierRepository.Singleton.semainierList
import fr.juju.myapplication.adapter.RepasAdapter
import fr.juju.myapplication.adapter.RepasItemDecoration
import fr.juju.myapplication.adapter.TagsAdapter
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class HomeFragment
    (val context: MainActivity
) : Fragment()  {

    val currentDay = SimpleDateFormat("EEEE", Locale.FRANCE).format(Date())

    @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater?.inflate(R.layout.fragment_home, container, false)
        val currentDays = semainierList.filter{s->s.id_semainier == currentDay}[0]


        if (currentDays.midi != "None"){
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.visibility = View.VISIBLE
            var currentRepasMidi = repasList.filter { s->s.id == currentDays.midi }[0]
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.setOnClickListener{
                context.loadFragment(RecetteFragment(context, currentRepasMidi))
            }
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.nomMidi)?.text  = currentRepasMidi.name
            view?.findViewById<TextView>(R.id.descriptionMidi)?.text  = currentRepasMidi.duree
            val imageref = Firebase.storage.reference.child(currentRepasMidi.imageUri)
            imageref.downloadUrl.addOnSuccessListener {Uri->
                val imageURL = Uri.toString()
                val imagetest = view.findViewById<ImageView>(R.id.image_item2)
                Glide.with(context)
                    .load(imageURL)
                    .into(imagetest)
            }
            val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.tagListMidi)
            collectionRecyclerView.adapter = TagsAdapter(context, currentRepasMidi.tags, R.layout.item_tags_horizontal)
        }else {
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.visibility = View.GONE
            }

        if (currentDays.soir != "None"){

            view?.findViewById<ConstraintLayout>(R.id.Soir)?.visibility = View.VISIBLE
            var currentRepasSoir = repasList.filter { s->s.id == currentDays.soir }[0]
            view?.findViewById<ConstraintLayout>(R.id.Soir)?.setOnClickListener{
                context.loadFragment(RecetteFragment(context, currentRepasSoir))
            }
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.nomSoir)?.text  = currentRepasSoir.name
            view?.findViewById<TextView>(R.id.descriptionSoir)?.text  = currentRepasSoir.duree

            val imageref = Firebase.storage.reference.child(currentRepasSoir.imageUri)
            imageref.downloadUrl.addOnSuccessListener {Uri->
                val imageURL = Uri.toString()
                val imagetest = view.findViewById<ImageView>(R.id.image_item3)
                Glide.with(context)
                    .load(imageURL)
                    .into(imagetest)
            }



            val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.tagListSoir)
            collectionRecyclerView.adapter = TagsAdapter(context, currentRepasSoir.tags, R.layout.item_tags_horizontal)

        }else {
            view?.findViewById<ConstraintLayout>(R.id.Soir)?.visibility = View.GONE
        }

        if (currentDays.apero != "None"){
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.visibility = View.VISIBLE
            var currentRepasApero = repasList.filter { s->s.id == currentDays.apero }[0]
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.setOnClickListener{
                context.loadFragment(RecetteFragment(context, currentRepasApero))
            }
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.nomApero)?.text  = currentRepasApero.name
            view?.findViewById<TextView>(R.id.descriptionApero)?.text  = currentRepasApero.duree

            val imageref = Firebase.storage.reference.child(currentRepasApero.imageUri)
            imageref.downloadUrl.addOnSuccessListener {Uri->
                val imageURL = Uri.toString()
                val imagetest = view.findViewById<ImageView>(R.id.image_item4)
                Glide.with(context)
                    .load(imageURL)
                    .into(imagetest)
            }

            val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.tagListApero)
            collectionRecyclerView.adapter = TagsAdapter(context, currentRepasApero.tags, R.layout.item_tags_horizontal)

        }else {
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.visibility = View.GONE
        }

        if(currentDays.midi == "None" && currentDays.soir == "None" && currentDays.apero == "None"){
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.VISIBLE
        }


        return view
    }


}