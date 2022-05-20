package fr.juju.myapplication.fragments

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import fr.juju.myapplication.activity.MainActivity
import fr.juju.myapplication.R
import fr.juju.myapplication.model.RepasModel
import fr.juju.myapplication.adapter.RepasAdapter
import kotlin.collections.ArrayList


class ResultResearchFragment(
    private val context: MainActivity,
    val resultResearch: ArrayList<RepasModel> = arrayListOf<RepasModel>(),
    private val time: String,
    private val selectedDay: String,
    private val currentSemaine: String

) : Fragment()  {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater?.inflate(R.layout.fragment_resultresearch, container, false)
        val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.repas_list)
        context.onBackPressedDispatcher.addCallback(context, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                context.handleBack()
            }})
        resultResearch.sortBy { s->s.name }
        collectionRecyclerView.adapter = RepasAdapter(context, resultResearch, R.layout.item_repas_vertical, time,selectedDay, currentSemaine)
        collectionRecyclerView.layoutManager = LinearLayoutManager(context)
        view.findViewById<ImageView>(R.id.add_recette).setOnClickListener{
            context.unprintSoir()
            context.unprintMidi()
            context.unprintApero()
            context.unprintAutres()
            context.loadFragment(AddRepasFragment(context))
        }

        view.findViewById<Button>(R.id.research).setOnClickListener{
            if(context.research(view.findViewById<EditText>(R.id.research_input).text.toString()).isNotEmpty()){
                context.hideKeyboard()
                context.loadFragment(ResultResearchFragment(context, context.research(view.findViewById<EditText>(R.id.research_input).text.toString()), time, selectedDay, currentSemaine))
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

        return view
    }
}