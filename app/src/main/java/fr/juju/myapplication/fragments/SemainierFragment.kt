package fr.juju.myapplication.fragments

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import fr.juju.myapplication.*
import fr.juju.myapplication.adapter.TagsAdapter
import org.w3c.dom.Text
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SemainierFragment (
    private val context: MainActivity,
) : Fragment() {

    val currentDay = SimpleDateFormat("EEEE", Locale.FRANCE).format(Date())
    val pastDay: ArrayList<String> = arrayListOf<String>()

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {


        val view = inflater?.inflate(R.layout.fragment_semainier, container, false)
        val currentDays = SemainierRepository.Singleton.semainierList.filter{ s->s.id_semainier == currentDay}[0]
        reinitialisation()

        if (currentDay == "lundi"){
            view?.findViewById<ImageView>(R.id.Mardi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Mercredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Jeudi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Vendredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Samedi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Dimanche_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view.findViewById<TextView>(R.id.Lundi)?.setTypeface(null,Typeface.BOLD)
            view.findViewById<ImageView>(R.id.Lundi_img).setImageDrawable(this.getContext()
                ?.getDrawable(R.drawable.radio_uncheck))
        }
        else if(currentDay == "mardi"){
            view?.findViewById<ImageView>(R.id.Lundi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mercredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Jeudi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Vendredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Samedi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Dimanche_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view.findViewById<TextView>(R.id.Mardi)?.setTypeface(null,Typeface.BOLD)
            view.findViewById<ImageView>(R.id.Mardi_img).setImageDrawable(this.getContext()
                ?.getDrawable(R.drawable.radio_uncheck))
            pastDay.add("lundi")
        }
        else if(currentDay == "mercredi"){
            view?.findViewById<ImageView>(R.id.Lundi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mardi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mercredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Jeudi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Vendredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Samedi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Dimanche_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))

            view.findViewById<TextView>(R.id.Mercredi)?.setTypeface(null,Typeface.BOLD)
            view.findViewById<ImageView>(R.id.Mercredi_img).setImageDrawable(this.getContext()
                ?.getDrawable(R.drawable.radio_uncheck))
            pastDay.add("lundi")
            pastDay.add("mardi")


        }
        else if(currentDay == "jeudi"){
            view?.findViewById<ImageView>(R.id.Lundi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mardi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mercredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Jeudi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Vendredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Samedi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Dimanche_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))

            view.findViewById<TextView>(R.id.Jeudi)?.setTypeface(null,Typeface.BOLD)
            view.findViewById<ImageView>(R.id.Jeudi_img).setImageDrawable(this.getContext()
                ?.getDrawable(R.drawable.radio_uncheck))
            pastDay.add("lundi")
            pastDay.add("mardi")
            pastDay.add("mercredi")

        }
        else if(currentDay == "vendredi"){
            view?.findViewById<ImageView>(R.id.Lundi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mardi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mercredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Jeudi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Vendredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Samedi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Dimanche_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))

            view.findViewById<TextView>(R.id.Vendredi)?.setTypeface(null,Typeface.BOLD)
            view.findViewById<ImageView>(R.id.Vendredi_img).setImageDrawable(this.getContext()
                ?.getDrawable(R.drawable.radio_uncheck))
            pastDay.add("lundi")
            pastDay.add("mardi")
            pastDay.add("mercredi")
            pastDay.add("jeudi")


        }
        else if(currentDay == "samedi"){
            view?.findViewById<ImageView>(R.id.Lundi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mardi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mercredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Jeudi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Vendredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Samedi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Dimanche_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))

            view.findViewById<TextView>(R.id.Samedi)?.setTypeface(null,Typeface.BOLD)
            view.findViewById<ImageView>(R.id.Samedi_img).setImageDrawable(this.getContext()
                ?.getDrawable(R.drawable.radio_uncheck))
            pastDay.add("lundi")
            pastDay.add("mardi")
            pastDay.add("mercredi")
            pastDay.add("jeudi")
            pastDay.add("vendredi")

        }
        else if(currentDay == "dimanche"){
            view?.findViewById<ImageView>(R.id.Lundi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mardi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mercredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Jeudi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Vendredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Samedi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Dimanche_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))

            view.findViewById<TextView>(R.id.Dimanche)?.setTypeface(null,Typeface.BOLD)
            view.findViewById<ImageView>(R.id.Dimanche_img).setImageDrawable(this.getContext()
                ?.getDrawable(R.drawable.radio_uncheck))
            pastDay.add("lundi")
            pastDay.add("mardi")
            pastDay.add("mercredi")
            pastDay.add("jeudi")
            pastDay.add("vendredi")
            pastDay.add("samedi")

        }
        if (currentDays.midi != "None"){
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.visibility = View.VISIBLE
            var currentRepasMidi = RepasRepository.Singleton.repasList.filter { s->s.id == currentDays.midi }[0]
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.setOnClickListener{
                context.loadFragment(RecetteFragment(context, currentRepasMidi))
            }
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.nomMidi)?.text  = currentRepasMidi.name
            view?.findViewById<TextView>(R.id.descriptionMidi)?.text  = currentRepasMidi.duree
            Glide.with(context)
                 .load(currentRepasMidi.imageUri)
                 .into(view.findViewById<ImageView>(R.id.image_item2))


            val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.tagListMidi)
            collectionRecyclerView.adapter = TagsAdapter(context, currentRepasMidi.tags, R.layout.item_tags_horizontal)
        }else {
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.visibility = View.GONE
        }

        if (currentDays.soir != "None"){

            view?.findViewById<ConstraintLayout>(R.id.Soir)?.visibility = View.VISIBLE
            var currentRepasSoir = RepasRepository.Singleton.repasList.filter { s->s.id == currentDays.soir }[0]
            view?.findViewById<ConstraintLayout>(R.id.Soir)?.setOnClickListener{
                context.loadFragment(RecetteFragment(context, currentRepasSoir))
            }
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.nomSoir)?.text  = currentRepasSoir.name
            view?.findViewById<TextView>(R.id.descriptionSoir)?.text  = currentRepasSoir.duree
            Glide.with(context)
                 .load(currentRepasSoir.imageUri)
                 .into(view.findViewById<ImageView>(R.id.image_item3))

            val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.tagListSoir)
            collectionRecyclerView.adapter = TagsAdapter(context, currentRepasSoir.tags, R.layout.item_tags_horizontal)

        }else {
            view?.findViewById<ConstraintLayout>(R.id.Soir)?.visibility = View.GONE
        }

        if (currentDays.apero != "None"){
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.visibility = View.VISIBLE
            var currentRepasApero = RepasRepository.Singleton.repasList.filter { s->s.id == currentDays.apero }[0]
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.setOnClickListener{
                context.loadFragment(RecetteFragment(context, currentRepasApero))
            }
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.nomApero)?.text  = currentRepasApero.name
            view?.findViewById<TextView>(R.id.descriptionApero)?.text  = currentRepasApero.duree
            Glide.with(context)
                 .load(currentRepasApero.imageUri)
                 .into(view.findViewById<ImageView>(R.id.image_item4))


            val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.tagListApero)
            collectionRecyclerView.adapter = TagsAdapter(context, currentRepasApero.tags, R.layout.item_tags_horizontal)

        }else {
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.visibility = View.GONE
        }

        if(currentDays.midi == "None" && currentDays.soir == "None" && currentDays.apero == "None"){
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.VISIBLE
        }
        view.findViewById<TextView>(R.id.Lundi).setOnClickListener{
            reinitialisation()
            switch("lundi", view.findViewById<TextView>(R.id.Lundi), view.findViewById<ImageView>(R.id.Lundi_img))
        }
        view.findViewById<ImageView>(R.id.Lundi_img).setOnClickListener{
            reinitialisation()
            switch("lundi", view.findViewById<TextView>(R.id.Lundi), view.findViewById<ImageView>(R.id.Lundi_img))
        }
        view.findViewById<TextView>(R.id.Mardi).setOnClickListener{
            reinitialisation()
            switch("mardi", view.findViewById<TextView>(R.id.Mardi), view.findViewById<ImageView>(R.id.Mardi_img))
        }
        view.findViewById<ImageView>(R.id.Mardi_img).setOnClickListener{
            reinitialisation()
            switch("mardi", view.findViewById<TextView>(R.id.Mardi), view.findViewById<ImageView>(R.id.Mardi_img))
        }
        view.findViewById<TextView>(R.id.Mercredi).setOnClickListener{
            reinitialisation()
            switch("mercredi", view.findViewById<TextView>(R.id.Mercredi), view.findViewById<ImageView>(R.id.Mercredi_img))
        }
        view.findViewById<ImageView>(R.id.Mercredi_img).setOnClickListener{
            reinitialisation()
            switch("mercredi", view.findViewById<TextView>(R.id.Mercredi), view.findViewById<ImageView>(R.id.Mercredi_img))
        }
        view.findViewById<TextView>(R.id.Jeudi).setOnClickListener{
            reinitialisation()
            switch("jeudi", view.findViewById<TextView>(R.id.Jeudi), view.findViewById<ImageView>(R.id.Jeudi_img))
        }
        view.findViewById<ImageView>(R.id.Jeudi_img).setOnClickListener{
            reinitialisation()
            switch("jeudi", view.findViewById<TextView>(R.id.Jeudi), view.findViewById<ImageView>(R.id.Jeudi_img))
        }
        view.findViewById<TextView>(R.id.Vendredi).setOnClickListener{
            reinitialisation()
            switch("vendredi", view.findViewById<TextView>(R.id.Vendredi), view.findViewById<ImageView>(R.id.Vendredi_img))
        }
        view.findViewById<ImageView>(R.id.Vendredi_img).setOnClickListener{
            reinitialisation()
            switch("vendredi", view.findViewById<TextView>(R.id.Vendredi), view.findViewById<ImageView>(R.id.Vendredi_img))
        }
        view.findViewById<TextView>(R.id.Samedi).setOnClickListener{
            reinitialisation()
            switch("samedi", view.findViewById<TextView>(R.id.Samedi), view.findViewById<ImageView>(R.id.Samedi_img))
        }
        view.findViewById<ImageView>(R.id.Samedi_img).setOnClickListener{
            reinitialisation()
            switch("samedi", view.findViewById<TextView>(R.id.Samedi), view.findViewById<ImageView>(R.id.Samedi_img))
        }
        view.findViewById<TextView>(R.id.Dimanche).setOnClickListener{
            reinitialisation()
            switch("dimanche", view.findViewById<TextView>(R.id.Dimanche), view.findViewById<ImageView>(R.id.Dimanche_img))
        }
        view.findViewById<ImageView>(R.id.Dimanche_img).setOnClickListener{
            reinitialisation()
            switch("dimanche", view.findViewById<TextView>(R.id.Dimanche), view.findViewById<ImageView>(R.id.Dimanche_img))
        }

        view.findViewById<ImageView>(R.id.affect_repas).setOnClickListener{
            enableEdit(view)
        }
        val translate = AnimationUtils.loadAnimation(context, R.anim.translate_anim)
        view.findViewById<ConstraintLayout>(R.id.Midi).startAnimation(translate)
        view.findViewById<ConstraintLayout>(R.id.Soir).startAnimation(translate)
        view.findViewById<ConstraintLayout>(R.id.Apero).startAnimation(translate)
        view.findViewById<ConstraintLayout>(R.id.NoRepas).startAnimation(translate)
        view.findViewById<TextView>(R.id.substyle).startAnimation(translate)
        view.findViewById<ImageView>(R.id.imageView5).startAnimation(translate)



        return view
    }

    private fun enableEdit(view: View?) {

    }


    private fun reinitialisation(){
        view?.findViewById<TextView>(R.id.Lundi)?.setTypeface(null,Typeface.NORMAL)
        view?.findViewById<TextView>(R.id.Mardi)?.setTypeface(null,Typeface.NORMAL)
        view?.findViewById<TextView>(R.id.Mercredi)?.setTypeface(null,Typeface.NORMAL)
        view?.findViewById<TextView>(R.id.Jeudi)?.setTypeface(null,Typeface.NORMAL)
        view?.findViewById<TextView>(R.id.Vendredi)?.setTypeface(null,Typeface.NORMAL)
        view?.findViewById<TextView>(R.id.Samedi)?.setTypeface(null,Typeface.NORMAL)
        view?.findViewById<TextView>(R.id.Dimanche)?.setTypeface(null,Typeface.NORMAL)

        if(currentDay == "lundi"){
            view?.findViewById<ImageView>(R.id.Lundi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Mardi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Mercredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Jeudi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Vendredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Samedi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Dimanche_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
        }
        if(currentDay == "mardi"){
            view?.findViewById<ImageView>(R.id.Lundi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mardi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Mercredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Jeudi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Vendredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Samedi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Dimanche_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
        }
        if(currentDay == "mercredi"){
            view?.findViewById<ImageView>(R.id.Lundi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mardi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mercredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Jeudi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Vendredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Samedi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Dimanche_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
        }
        if(currentDay == "jeudi"){
            view?.findViewById<ImageView>(R.id.Lundi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mardi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mercredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Jeudi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Vendredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Samedi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Dimanche_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
        }
        if(currentDay == "vendredi"){
            view?.findViewById<ImageView>(R.id.Lundi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mardi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mercredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Jeudi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Vendredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Samedi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Dimanche_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
        }
        if(currentDay == "samedi"){
            view?.findViewById<ImageView>(R.id.Lundi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mardi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mercredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Jeudi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Vendredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Samedi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Dimanche_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
        }
        if(currentDay == "dimanche"){
            view?.findViewById<ImageView>(R.id.Lundi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mardi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mercredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Jeudi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Vendredi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Samedi_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Dimanche_img)?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
        }

    }

    private fun switch(day: String, button: TextView?, img: ImageView?){
        val currentDays = SemainierRepository.Singleton.semainierList.filter{ s->s.id_semainier == day}[0]
        button?.setTypeface(null,Typeface.BOLD)
        if(day in pastDay){
            img?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check_check))
        }else{
            img?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_uncheck))
        }

        if (currentDays.midi != "None"){
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.visibility = View.VISIBLE
            var currentRepasMidi = RepasRepository.Singleton.repasList.filter { s->s.id == currentDays.midi }[0]
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.setOnClickListener{
                context.loadFragment(RecetteFragment(context, currentRepasMidi))
            }
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.nomMidi)?.text  = currentRepasMidi.name
            view?.findViewById<TextView>(R.id.descriptionMidi)?.text  = currentRepasMidi.duree
            Glide.with(context).load(Uri.parse(currentRepasMidi.imageUri)).into(view?.findViewById<ImageView>(R.id.image_item2)!!)

            val collectionRecyclerView1 = view?.findViewById<RecyclerView>(R.id.tagListMidi)
            collectionRecyclerView1?.adapter = TagsAdapter(context, currentRepasMidi.tags, R.layout.item_tags_horizontal)
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.setOnClickListener{
                context.loadFragment(RecetteFragment(context, currentRepasMidi))
            }
        }else {
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.visibility = View.GONE
        }

        if (currentDays.soir != "None"){
            var currentRepasSoir = RepasRepository.Singleton.repasList.filter { s->s.id == currentDays.soir }[0]
            view?.findViewById<ConstraintLayout>(R.id.Soir)?.visibility = View.VISIBLE
            view?.findViewById<ConstraintLayout>(R.id.Soir)?.setOnClickListener{
                context.loadFragment(RecetteFragment(context, currentRepasSoir))
            }
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.nomSoir)?.text  = currentRepasSoir.name
            view?.findViewById<TextView>(R.id.descriptionSoir)?.text  = currentRepasSoir.duree
            Glide.with(context).load(Uri.parse(currentRepasSoir.imageUri)).into(view?.findViewById<ImageView>(R.id.image_item3)!!)

            val collectionRecyclerView2 = view?.findViewById<RecyclerView>(R.id.tagListSoir)
            collectionRecyclerView2?.adapter = TagsAdapter(context,currentRepasSoir.tags, R.layout.item_tags_horizontal)
            view?.findViewById<ConstraintLayout>(R.id.Soir)?.setOnClickListener{
                context.loadFragment(RecetteFragment(context, currentRepasSoir))
            }
        }else {
            view?.findViewById<ConstraintLayout>(R.id.Soir)?.visibility = View.GONE
        }

        if (currentDays.apero != "None"){
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.visibility = View.VISIBLE
            var currentRepasApero = RepasRepository.Singleton.repasList.filter { s->s.id == currentDays.apero }[0]
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.setOnClickListener{
                context.loadFragment(RecetteFragment(context, currentRepasApero))
            }
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.nomApero)?.text  = currentRepasApero.name
            view?.findViewById<TextView>(R.id.descriptionApero)?.text  = currentRepasApero.duree
            Glide.with(context).load(Uri.parse(currentRepasApero.imageUri)).into(view?.findViewById<ImageView>(R.id.image_item4)!!)
            val collectionRecyclerView3 = view?.findViewById<RecyclerView>(R.id.tagListApero)
            collectionRecyclerView3?.adapter = TagsAdapter(context, currentRepasApero.tags, R.layout.item_tags_horizontal)
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.setOnClickListener{
                context.loadFragment(RecetteFragment(context, currentRepasApero))
            }
        }else {
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.visibility = View.GONE
        }

        if(currentDays.midi == "None" && currentDays.soir == "None" && currentDays.apero == "None")
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.VISIBLE
    }
}