package fr.juju.myapplication.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.activity.OnBackPressedCallback
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.juju.myapplication.*
import fr.juju.myapplication.activity.MainActivity

import fr.juju.myapplication.repository.RepasRepository.Singleton.repasList
import fr.juju.myapplication.repository.SemainierRepository.Singleton.semainierList
import fr.juju.myapplication.adapter.AutresRapasHomeAdapter
import fr.juju.myapplication.adapter.TagsAdapter
import fr.juju.myapplication.model.RepasModel
import java.text.SimpleDateFormat
import java.util.*


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
        //var currentDays= SemainierModel()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                context.finish()
            }

        })

        var currentDays = semainierList.filter{s->s.id_semainier == currentDay}[0]

        if (currentDays.midi != "None"){
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.visibility = View.VISIBLE
                var currentRepasMidi = repasList.filter { s->s.id == currentDays.midi }[0]
                view?.findViewById<ConstraintLayout>(R.id.Midi)?.setOnClickListener{
                    context.loadFragment(RecetteFragment(context, currentRepasMidi, "None", "None", "None"))
                }
                view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
                view?.findViewById<TextView>(R.id.nomMidi)?.text  = currentRepasMidi.name
                view?.findViewById<TextView>(R.id.descriptionMidi)?.text  = currentRepasMidi.duree
                Glide.with(context)
                    .load(Uri.parse(currentRepasMidi.imageUri))
                    .into(view.findViewById<ImageView>(R.id.image_item2))

                val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.tagListMidi)
                collectionRecyclerView.adapter = TagsAdapter(context, currentRepasMidi.tags, R.layout.item_tags_vertical)
                collectionRecyclerView.layoutManager = LinearLayoutManager(context)

            }
        else {
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.visibility = View.GONE
            }

        if (currentDays.soir != "None"){
            view?.findViewById<ConstraintLayout>(R.id.Soir)?.visibility = View.VISIBLE
                var currentRepasSoir = repasList.filter { s->s.id == currentDays.soir }[0]
                view?.findViewById<ConstraintLayout>(R.id.Soir)?.setOnClickListener{
                    context.loadFragment(RecetteFragment(context, currentRepasSoir, "None", "None", "None"))
                }
                view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
                view?.findViewById<TextView>(R.id.nomSoir)?.text  = currentRepasSoir.name
                view?.findViewById<TextView>(R.id.descriptionSoir)?.text  = currentRepasSoir.duree
                Glide.with(context)
                    .load(currentRepasSoir.imageUri)
                    .into(view.findViewById<ImageView>(R.id.image_item3))

                val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.tagListSoir)
                collectionRecyclerView.adapter = TagsAdapter(context, currentRepasSoir.tags, R.layout.item_tags_vertical)
                collectionRecyclerView.layoutManager = LinearLayoutManager(context)
        }else {
            view?.findViewById<ConstraintLayout>(R.id.Soir)?.visibility = View.GONE
        }

        if (currentDays.apero != "None"){
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.visibility = View.VISIBLE
                var currentRepasApero = repasList.filter { s->s.id == currentDays.apero }[0]
                view?.findViewById<ConstraintLayout>(R.id.Apero)?.setOnClickListener{
                    context.loadFragment(RecetteFragment(context, currentRepasApero, "None", "None", "None"))
                }
                view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
                view?.findViewById<TextView>(R.id.nomApero)?.text  = currentRepasApero.name
                view?.findViewById<TextView>(R.id.descriptionApero)?.text  = currentRepasApero.duree

                Glide.with(context)
                    .load(currentRepasApero.imageUri)
                    .into(view.findViewById<ImageView>(R.id.image_item4))

                val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.tagListApero)
                collectionRecyclerView.adapter = TagsAdapter(context, currentRepasApero.tags, R.layout.item_tags_vertical)
                collectionRecyclerView.layoutManager = LinearLayoutManager(context)

        }
        else {
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.visibility = View.GONE
        }

        val recyclerAutres = view.findViewById<RecyclerView>(R.id.autresRepasRecylcer)
        if (currentDays.autres.isNotEmpty()){
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
            view?.findViewById<ConstraintLayout>(R.id.autres)?.visibility = View.VISIBLE
            var repasAutresList = arrayListOf<RepasModel>()
            for(repas in currentDays.autres){
                repasAutresList.add(repasList.filter { s->s.id == repas }[0])
            }
            recyclerAutres.adapter = AutresRapasHomeAdapter(context,repasAutresList, R.layout.item_home_autres_vertical)
            recyclerAutres.layoutManager = LinearLayoutManager(context)

        }else {
            view?.findViewById<ConstraintLayout>(R.id.autres)?.visibility = View.GONE
        }

        if(currentDays.midi == "None" && currentDays.soir == "None" && currentDays.apero == "None" && currentDays.autres.isEmpty()){
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.VISIBLE
            view?.findViewById<TextView>(R.id.currentDays)?.text = "Rien de prévu aujourd’hui ! :)"
            view?.findViewById<ConstraintLayout>(R.id.vite_recette)?.setOnClickListener{
               context.loadFragment(RecetteFragment(context, repasList.filter{s->s.tags.contains("Plat")}.random(),"None","None","None"))
            }

        }


        view.findViewById<Button>(R.id.research).setOnClickListener{
            if(context.research(view.findViewById<EditText>(R.id.research_input).text.toString()).isNotEmpty()){
                context.hideKeyboard()
                context.loadFragment(ResultResearchFragment(context, context.research(view.findViewById<EditText>(R.id.research_input).text.toString()), "None",  "None",  "None"))
            }
            else {
                context.hideKeyboard()
                view.findViewById<ImageView>(R.id.searchbar).animate().alpha(0F).setDuration(10)
                view.findViewById<ImageView>(R.id.searchbar_red).animate().alpha(1F).setDuration(1)

                val scaleX = ObjectAnimator.ofFloat(view.findViewById<ImageView>(R.id.searchbar_red), View.SCALE_X, 1.0f, 1.1f).setDuration(200)
                val scaleY = ObjectAnimator.ofFloat(view.findViewById<ImageView>(R.id.searchbar_red), View.SCALE_Y, 1.0f, 1.1f).setDuration(200)
                val downscaleX = ObjectAnimator.ofFloat(view.findViewById<ImageView>(R.id.searchbar_red), View.SCALE_X, 1.1f, 1.0f).setDuration(200)
                val downscaleY = ObjectAnimator.ofFloat(view.findViewById<ImageView>(R.id.searchbar_red), View.SCALE_Y, 1.1f, 1.0f).setDuration(200)

                val set = AnimatorSet()
                set.playTogether(scaleX, scaleY)
                set.play(downscaleX).after(scaleX)
                set.playTogether(downscaleX, downscaleY)
                set.playTogether(scaleX, scaleY)
                set.play(downscaleX).after(scaleX)
                set.playTogether(downscaleX, downscaleY)
                set.start()
            }
        }

        view.findViewById<EditText>(R.id.research_input).addTextChangedListener(
            object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if(s.contains("\n")) {
                        view.findViewById<EditText>(R.id.research_input).setText(s.toString().replace("\n",""))
                        view.findViewById<EditText>(R.id.research_input).setSelection(s.length-1)
                        view.findViewById<Button>(R.id.research).performClick()
                    }
                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                    // Fires right before text is changing
                }

                override fun afterTextChanged(s: Editable) {


                }
            }
        )

        view.findViewById<EditText>(R.id.research_input).setOnClickListener{
            view.findViewById<ImageView>(R.id.searchbar_red).animate().alpha(0F).setDuration(10)
            view.findViewById<ImageView>(R.id.searchbar).animate().alpha(1F).setDuration(1)
        }

        //val translate = AnimationUtils.loadAnimation(context, R.anim.translate_anim)
     //   view.findViewById<ConstraintLayout>(R.id.constraint).startAnimation(translate)


        return view
    }


}