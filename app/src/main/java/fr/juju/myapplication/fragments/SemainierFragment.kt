package fr.juju.myapplication.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.juju.myapplication.*
import fr.juju.myapplication.IngredientRepository.Singleton.ingredientList
import fr.juju.myapplication.RepasRepository.Singleton.repasList
import fr.juju.myapplication.SemainierRepository.Singleton.semainierList
import fr.juju.myapplication.SemainierSuivantRepository.Singleton.semainierSuivantList
import fr.juju.myapplication.adapter.AutresRapasSemainierAdapter
import fr.juju.myapplication.adapter.TagsAdapter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class SemainierFragment(
    private val context: MainActivity,
    private val selectedDayInput: String,
    private val currentSemaineInput: String

) : Fragment() {

    val currentDay = SimpleDateFormat("EEEE", Locale.FRANCE).format(Date())
    val pastDay: ArrayList<String> = arrayListOf<String>()
    var enable: Boolean = false
    lateinit var selectedDay: String
    var currentSemaine = arrayListOf<SemainierModel>()
    var suivant = false

    @SuppressLint("UseCompatLoadingForDrawables")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater?.inflate(R.layout.fragment_semainier, container, false)
        view.clearAnimation()
        if (selectedDayInput == "None") {
            selectedDay = currentDay
        } else {
            selectedDay = selectedDayInput
        }
        var currentDays = SemainierModel()

        if (currentDay == "mardi") {
            pastDay.add("lundi")
        }
        if (currentDay == "mercredi") {
            pastDay.add("lundi")
            pastDay.add("mardi")
        }
        if (currentDay == "jeudi") {
            pastDay.add("lundi")
            pastDay.add("mardi")
            pastDay.add("mercredi")
        }
        if (currentDay == "vendredi") {
            pastDay.add("lundi")
            pastDay.add("mardi")
            pastDay.add("mercredi")
            pastDay.add("jeudi")
        }
        if (currentDay == "samedi") {
            pastDay.add("lundi")
            pastDay.add("mardi")
            pastDay.add("mercredi")
            pastDay.add("jeudi")
            pastDay.add("vendredi")
        }
        if (currentDay == "dimanche") {
            pastDay.add("lundi")
            pastDay.add("mardi")
            pastDay.add("mercredi")
            pastDay.add("jeudi")
            pastDay.add("vendredi")
            pastDay.add("samedi")
        }
        val recyclerAutres = view.findViewById<RecyclerView>(R.id.autresRepasRecylcer)
        var repoSemainier = SemainierRepository()
        var repasAutresList = arrayListOf<RepasModel>()
        var repoSemainierSuivant = SemainierSuivantRepository()
        if (currentSemaineInput == "suivant") {
            currentSemaine = semainierSuivantList
            suivant = true
            currentDays = currentSemaine.filter { s -> s.id_semainier == selectedDay }[0]
            view.findViewById<ImageView>(R.id.echange).visibility = View.VISIBLE
            view.findViewById<ImageView>(R.id.eye).visibility = View.GONE
            view.findViewById<ImageView>(R.id.calendar).visibility = View.VISIBLE
            view.findViewById<Switch>(R.id.toggleButton).isChecked = true
            reinitialisationSuivant()

            if (currentSemaine[2].midi != "None" && currentSemaine[2].soir == "None") {
                view?.findViewById<ImageView>(R.id.Lundi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_top))
            } else if (currentSemaine[2].midi == "None" && currentSemaine[2].soir != "None") {
                view?.findViewById<ImageView>(R.id.Lundi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_bottom))

            } else if (currentSemaine[2].midi != "None" && currentSemaine[2].soir != "None") {
                view?.findViewById<ImageView>(R.id.Lundi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_combined))
            } else view?.findViewById<ImageView>(R.id.Lundi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))

            if (currentSemaine[3].midi != "None" && currentSemaine[3].soir == "None") {
                view?.findViewById<ImageView>(R.id.Mardi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_top))
            } else if (currentSemaine[3].midi == "None" && currentSemaine[3].soir != "None") {
                view?.findViewById<ImageView>(R.id.Mardi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_bottom))

            } else if (currentSemaine[3].midi != "None" && currentSemaine[3].soir != "None") {
                view?.findViewById<ImageView>(R.id.Mardi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_combined))
            } else view?.findViewById<ImageView>(R.id.Mardi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))

            if (currentSemaine[4].midi != "None" && currentSemaine[4].soir == "None") {
                view?.findViewById<ImageView>(R.id.Mercredi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_top))
            } else if (currentSemaine[4].midi == "None" && currentSemaine[4].soir != "None") {
                view?.findViewById<ImageView>(R.id.Mercredi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_bottom))

            } else if (currentSemaine[4].midi != "None" && currentSemaine[4].soir != "None") {
                view?.findViewById<ImageView>(R.id.Mercredi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_combined))
            } else view?.findViewById<ImageView>(R.id.Mercredi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))


            if (currentSemaine[1].midi != "None" && currentSemaine[1].soir == "None") {
                view.findViewById<ImageView>(R.id.Jeudi_img)
                    .setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_top))
            } else if (currentSemaine[1].midi == "None" && currentSemaine[1].soir != "None") {
                view.findViewById<ImageView>(R.id.Jeudi_img)
                    .setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_bottom))

            } else if (currentSemaine[1].midi != "None" && currentSemaine[1].soir != "None") {
                view?.findViewById<ImageView>(R.id.Jeudi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_combined))
            } else view?.findViewById<ImageView>(R.id.Jeudi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))

            if (currentSemaine[6].midi != "None" && currentSemaine[6].soir == "None") {
                view?.findViewById<ImageView>(R.id.Vendredi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_top))
            } else if (currentSemaine[6].midi == "None" && currentSemaine[6].soir != "None") {
                view?.findViewById<ImageView>(R.id.Vendredi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_bottom))

            } else if (currentSemaine[6].midi != "None" && currentSemaine[6].soir != "None") {
                view?.findViewById<ImageView>(R.id.Vendredi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_combined))
            } else view?.findViewById<ImageView>(R.id.Vendredi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))


            if (currentSemaine[5].midi != "None" && currentSemaine[5].soir == "None") {
                view?.findViewById<ImageView>(R.id.Samedi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_top))
            } else if (currentSemaine[5].midi == "None" && currentSemaine[5].soir != "None") {
                view?.findViewById<ImageView>(R.id.Samedi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_bottom))

            } else if (currentSemaine[5].midi != "None" && currentSemaine[5].soir != "None") {
                view?.findViewById<ImageView>(R.id.Samedi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_combined))
            } else view?.findViewById<ImageView>(R.id.Samedi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))

            if (currentSemaine[0].midi != "None" && currentSemaine[0].soir == "None") {
                view?.findViewById<ImageView>(R.id.Dimanche_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_top))
            } else if (currentSemaine[0].midi == "None" && currentSemaine[0].soir != "None") {
                view?.findViewById<ImageView>(R.id.Dimanche_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_bottom))

            } else if (currentSemaine[0].midi != "None" && currentSemaine[0].soir != "None") {
                view?.findViewById<ImageView>(R.id.Dimanche_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_combined))
            } else view?.findViewById<ImageView>(R.id.Dimanche_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))


            if (selectedDay == "lundi") {
                view.findViewById<TextView>(R.id.Lundi)?.setTypeface(null, Typeface.BOLD)
                if (currentDays.midi != "None" && currentDays.soir == "None") {
                    view?.findViewById<ImageView>(R.id.Lundi_img)
                        ?.setImageDrawable(
                            this.getContext()?.getDrawable(R.drawable.ellipse_top_select)
                        )
                } else if (currentDays.midi == "None" && currentDays.soir != "None") {
                    view?.findViewById<ImageView>(R.id.Lundi_img)
                        ?.setImageDrawable(
                            this.getContext()?.getDrawable(R.drawable.ellipse_bottom_select)
                        )

                } else if (currentDays.midi != "None" && currentDays.soir != "None") {
                    view?.findViewById<ImageView>(R.id.Lundi_img)
                        ?.setImageDrawable(
                            this.getContext()?.getDrawable(R.drawable.ellipse_combine_select)
                        )
                } else view?.findViewById<ImageView>(R.id.Lundi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_uncheck))

            } else if (selectedDay == "mardi") {
                view.findViewById<TextView>(R.id.Mardi)?.setTypeface(null, Typeface.BOLD)
                if (currentDays.midi != "None" && currentDays.soir == "None") {
                    view?.findViewById<ImageView>(R.id.Mardi_img)
                        ?.setImageDrawable(
                            this.getContext()?.getDrawable(R.drawable.ellipse_top_select)
                        )
                } else if (currentDays.midi == "None" && currentDays.soir != "None") {
                    view?.findViewById<ImageView>(R.id.Mardi_img)
                        ?.setImageDrawable(
                            this.getContext()?.getDrawable(R.drawable.ellipse_bottom_select)
                        )

                } else if (currentDays.midi != "None" && currentDays.soir != "None") {
                    view?.findViewById<ImageView>(R.id.Mardi_img)
                        ?.setImageDrawable(
                            this.getContext()?.getDrawable(R.drawable.ellipse_combine_select)
                        )
                } else view?.findViewById<ImageView>(R.id.Mardi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_uncheck))
            } else if (selectedDay == "mercredi") {
                view.findViewById<TextView>(R.id.Mercredi)?.setTypeface(null, Typeface.BOLD)
                if (currentDays.midi != "None" && currentDays.soir == "None") {
                    view?.findViewById<ImageView>(R.id.Mercredi_img)
                        ?.setImageDrawable(
                            this.getContext()?.getDrawable(R.drawable.ellipse_top_select)
                        )
                } else if (currentDays.midi == "None" && currentDays.soir != "None") {
                    view?.findViewById<ImageView>(R.id.Mercredi_img)
                        ?.setImageDrawable(
                            this.getContext()?.getDrawable(R.drawable.ellipse_bottom_select)
                        )

                } else if (currentDays.midi != "None" && currentDays.soir != "None") {
                    view?.findViewById<ImageView>(R.id.Mercredi_img)
                        ?.setImageDrawable(
                            this.getContext()?.getDrawable(R.drawable.ellipse_combine_select)
                        )
                } else view?.findViewById<ImageView>(R.id.Mercredi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_uncheck))
            } else if (selectedDay == "jeudi") {
                view.findViewById<TextView>(R.id.Jeudi)?.setTypeface(null, Typeface.BOLD)
                if (currentDays.midi != "None" && currentDays.soir == "None") {
                    view?.findViewById<ImageView>(R.id.Jeudi_img)
                        ?.setImageDrawable(
                            this.getContext()?.getDrawable(R.drawable.ellipse_top_select)
                        )
                } else if (currentDays.midi == "None" && currentDays.soir != "None") {
                    view?.findViewById<ImageView>(R.id.Jeudi_img)
                        ?.setImageDrawable(
                            this.getContext()?.getDrawable(R.drawable.ellipse_bottom_select)
                        )

                } else if (currentDays.midi != "None" && currentDays.soir != "None") {
                    view?.findViewById<ImageView>(R.id.Jeudi_img)
                        ?.setImageDrawable(
                            this.getContext()?.getDrawable(R.drawable.ellipse_combine_select)
                        )
                } else view?.findViewById<ImageView>(R.id.Jeudi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_uncheck))
            } else if (selectedDay == "vendredi") {
                view.findViewById<TextView>(R.id.Vendredi)?.setTypeface(null, Typeface.BOLD)
                if (currentDays.midi != "None" && currentDays.soir == "None") {
                    view?.findViewById<ImageView>(R.id.Vendredi_img)
                        ?.setImageDrawable(
                            this.getContext()?.getDrawable(R.drawable.ellipse_top_select)
                        )
                } else if (currentDays.midi == "None" && currentDays.soir != "None") {
                    view?.findViewById<ImageView>(R.id.Vendredi_img)
                        ?.setImageDrawable(
                            this.getContext()?.getDrawable(R.drawable.ellipse_bottom_select)
                        )

                } else if (currentDays.midi != "None" && currentDays.soir != "None") {
                    view?.findViewById<ImageView>(R.id.Vendredi_img)
                        ?.setImageDrawable(
                            this.getContext()?.getDrawable(R.drawable.ellipse_combine_select)
                        )
                } else view?.findViewById<ImageView>(R.id.Vendredi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_uncheck))
            } else if (selectedDay == "samedi") {
                view.findViewById<TextView>(R.id.Samedi)?.setTypeface(null, Typeface.BOLD)
                if (currentDays.midi != "None" && currentDays.soir == "None") {
                    view?.findViewById<ImageView>(R.id.Samedi_img)
                        ?.setImageDrawable(
                            this.getContext()?.getDrawable(R.drawable.ellipse_top_select)
                        )
                } else if (currentDays.midi == "None" && currentDays.soir != "None") {
                    view?.findViewById<ImageView>(R.id.Samedi_img)
                        ?.setImageDrawable(
                            this.getContext()?.getDrawable(R.drawable.ellipse_bottom_select)
                        )

                } else if (currentDays.midi != "None" && currentDays.soir != "None") {
                    view?.findViewById<ImageView>(R.id.Samedi_img)
                        ?.setImageDrawable(
                            this.getContext()?.getDrawable(R.drawable.ellipse_combine_select)
                        )
                } else view?.findViewById<ImageView>(R.id.Samedi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_uncheck))
            } else if (selectedDay == "dimanche") {
                view.findViewById<TextView>(R.id.Dimanche)?.setTypeface(null, Typeface.BOLD)
                if (currentDays.midi != "None" && currentDays.soir == "None") {
                    view?.findViewById<ImageView>(R.id.Dimanche_img)
                        ?.setImageDrawable(
                            this.getContext()?.getDrawable(R.drawable.ellipse_top_select)
                        )
                } else if (currentDays.midi == "None" && currentDays.soir != "None") {
                    view?.findViewById<ImageView>(R.id.Dimanche_img)
                        ?.setImageDrawable(
                            this.getContext()?.getDrawable(R.drawable.ellipse_bottom_select)
                        )

                } else if (currentDays.midi != "None" && currentDays.soir != "None") {
                    view?.findViewById<ImageView>(R.id.Dimanche_img)
                        ?.setImageDrawable(
                            this.getContext()?.getDrawable(R.drawable.ellipse_combine_select)
                        )
                } else view?.findViewById<ImageView>(R.id.Dimanche_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_uncheck))
            }
        }
        else {
            currentSemaine = semainierList
            suivant = false
            currentDays = currentSemaine.filter { s -> s.id_semainier == selectedDay }[0]
            view.findViewById<Switch>(R.id.toggleButton).isChecked = false
            view.findViewById<ImageView>(R.id.eye).visibility = View.VISIBLE
            view.findViewById<ImageView>(R.id.calendar).visibility = View.GONE
            view.findViewById<ImageView>(R.id.echange).visibility = View.GONE

            reinitialisation()

            if (currentDay == "lundi") {
                view?.findViewById<ImageView>(R.id.Lundi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
                view?.findViewById<ImageView>(R.id.Mardi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
                view?.findViewById<ImageView>(R.id.Mercredi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
                view?.findViewById<ImageView>(R.id.Jeudi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
                view?.findViewById<ImageView>(R.id.Vendredi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
                view?.findViewById<ImageView>(R.id.Samedi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
                view?.findViewById<ImageView>(R.id.Dimanche_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            }
            if (currentDay == "mardi") {
                view?.findViewById<ImageView>(R.id.Lundi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
                view?.findViewById<ImageView>(R.id.Mardi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
                view?.findViewById<ImageView>(R.id.Mercredi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
                view?.findViewById<ImageView>(R.id.Jeudi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
                view?.findViewById<ImageView>(R.id.Vendredi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
                view?.findViewById<ImageView>(R.id.Samedi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
                view?.findViewById<ImageView>(R.id.Dimanche_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            }
            if (currentDay == "mercredi") {
                view?.findViewById<ImageView>(R.id.Lundi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
                view?.findViewById<ImageView>(R.id.Mardi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
                view?.findViewById<ImageView>(R.id.Mercredi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
                view?.findViewById<ImageView>(R.id.Jeudi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
                view?.findViewById<ImageView>(R.id.Vendredi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
                view?.findViewById<ImageView>(R.id.Samedi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
                view?.findViewById<ImageView>(R.id.Dimanche_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            }
            if (currentDay == "jeudi") {
                view?.findViewById<ImageView>(R.id.Lundi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
                view?.findViewById<ImageView>(R.id.Mardi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
                view?.findViewById<ImageView>(R.id.Mercredi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
                view?.findViewById<ImageView>(R.id.Jeudi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
                view?.findViewById<ImageView>(R.id.Vendredi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
                view?.findViewById<ImageView>(R.id.Samedi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
                view?.findViewById<ImageView>(R.id.Dimanche_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            }
            if (currentDay == "vendredi") {
                view?.findViewById<ImageView>(R.id.Lundi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
                view?.findViewById<ImageView>(R.id.Mardi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
                view?.findViewById<ImageView>(R.id.Mercredi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
                view?.findViewById<ImageView>(R.id.Jeudi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
                view?.findViewById<ImageView>(R.id.Vendredi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
                view?.findViewById<ImageView>(R.id.Samedi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
                view?.findViewById<ImageView>(R.id.Dimanche_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            }
            if (currentDay == "samedi") {
                view?.findViewById<ImageView>(R.id.Lundi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
                view?.findViewById<ImageView>(R.id.Mardi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
                view?.findViewById<ImageView>(R.id.Mercredi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
                view?.findViewById<ImageView>(R.id.Jeudi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
                view?.findViewById<ImageView>(R.id.Vendredi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
                view?.findViewById<ImageView>(R.id.Samedi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
                view?.findViewById<ImageView>(R.id.Dimanche_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            }
            if (currentDay == "dimanche") {
                view?.findViewById<ImageView>(R.id.Lundi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
                view?.findViewById<ImageView>(R.id.Mardi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
                view?.findViewById<ImageView>(R.id.Mercredi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
                view?.findViewById<ImageView>(R.id.Jeudi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
                view?.findViewById<ImageView>(R.id.Vendredi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
                view?.findViewById<ImageView>(R.id.Samedi_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
                view?.findViewById<ImageView>(R.id.Dimanche_img)
                    ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            }

            if (selectedDay == "lundi") {
                view.findViewById<TextView>(R.id.Lundi)?.setTypeface(null, Typeface.BOLD)
                if (selectedDay in pastDay) {
                    view.findViewById<ImageView>(R.id.Lundi_img)
                        ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check_check))
                } else {
                    view.findViewById<ImageView>(R.id.Lundi_img)
                        ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_uncheck))
                }
            } else if (selectedDay == "mardi") {

                view.findViewById<TextView>(R.id.Mardi)?.setTypeface(null, Typeface.BOLD)
                if (selectedDay in pastDay) {
                    view.findViewById<ImageView>(R.id.Mardi_img)
                        ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check_check))
                } else {
                    view.findViewById<ImageView>(R.id.Mardi_img)
                        ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_uncheck))
                }
            } else if (selectedDay == "mercredi") {
                view.findViewById<TextView>(R.id.Mercredi)?.setTypeface(null, Typeface.BOLD)
                if (selectedDay in pastDay) {
                    view.findViewById<ImageView>(R.id.Mercredi_img)
                        ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check_check))
                } else {
                    view.findViewById<ImageView>(R.id.Mercredi_img)
                        ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_uncheck))
                }
            } else if (selectedDay == "jeudi") {
                view.findViewById<TextView>(R.id.Jeudi)?.setTypeface(null, Typeface.BOLD)
                if (selectedDay in pastDay) {
                    view.findViewById<ImageView>(R.id.Jeudi_img)
                        ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check_check))
                } else {
                    view.findViewById<ImageView>(R.id.Jeudi_img)
                        ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_uncheck))
                }
            } else if (selectedDay == "vendredi") {
                view.findViewById<TextView>(R.id.Vendredi)?.setTypeface(null, Typeface.BOLD)
                if (selectedDay in pastDay) {
                    view.findViewById<ImageView>(R.id.Vendredi_img)
                        ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check_check))
                } else {
                    view.findViewById<ImageView>(R.id.Vendredi_img)
                        ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_uncheck))
                }
            } else if (selectedDay == "samedi") {
                view.findViewById<TextView>(R.id.Samedi)?.setTypeface(null, Typeface.BOLD)
                if (selectedDay in pastDay) {
                    view.findViewById<ImageView>(R.id.Samedi_img)
                        ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check_check))
                } else {
                    view.findViewById<ImageView>(R.id.Samedi_img)
                        ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_uncheck))
                }
            } else if (selectedDay == "dimanche") {
                view.findViewById<TextView>(R.id.Dimanche)?.setTypeface(null, Typeface.BOLD)
                if (selectedDay in pastDay) {
                    view.findViewById<ImageView>(R.id.Dimanche_img)
                        ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check_check))
                } else {
                    view.findViewById<ImageView>(R.id.Dimanche_img)
                        ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_uncheck))
                }
            }
        }


        view.findViewById<Switch>(R.id.toggleButton).setOnClickListener {
            if (view.findViewById<Switch>(R.id.toggleButton).isChecked) {
                view.findViewById<ImageView>(R.id.echange).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.eye).visibility = View.GONE
                view.findViewById<ImageView>(R.id.calendar).visibility = View.VISIBLE
                currentSemaine = semainierSuivantList
                suivant = true
                reinitialisationSuivant()
                if (selectedDay == "lundi") {
                    switchSuivant(
                        selectedDay,
                        view.findViewById<TextView>(R.id.Lundi),
                        view.findViewById<ImageView>(R.id.Lundi_img)
                    )
                } else if (selectedDay == "mardi") {
                    switchSuivant(
                        selectedDay,
                        view.findViewById<TextView>(R.id.Mardi),
                        view.findViewById<ImageView>(R.id.Mardi_img)
                    )

                } else if (selectedDay == "mercredi") {
                    switchSuivant(
                        selectedDay,
                        view.findViewById<TextView>(R.id.Mercredi),
                        view.findViewById<ImageView>(R.id.Mercredi_img)
                    )

                } else if (selectedDay == "jeudi") {
                    switchSuivant(
                        selectedDay,
                        view.findViewById<TextView>(R.id.Jeudi),
                        view.findViewById<ImageView>(R.id.Jeudi_img)
                    )

                } else if (selectedDay == "vendredi") {
                    switchSuivant(
                        selectedDay,
                        view.findViewById<TextView>(R.id.Vendredi),
                        view.findViewById<ImageView>(R.id.Vendredi_img)
                    )

                } else if (selectedDay == "samedi") {
                    switchSuivant(
                        selectedDay,
                        view.findViewById<TextView>(R.id.Samedi),
                        view.findViewById<ImageView>(R.id.Samedi_img)
                    )

                } else if (selectedDay == "dimanche") {
                    switchSuivant(
                        selectedDay,
                        view.findViewById<TextView>(R.id.Dimanche),
                        view.findViewById<ImageView>(R.id.Dimanche_img)
                    )
                }
                repoSemainierSuivant.updateData {
                    currentDays = currentSemaine.filter { s -> s.id_semainier == selectedDay }[0]

                    if (currentDays.autres.isNotEmpty()) {
                        view?.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(fr.juju.myapplication.R.id.NoRepas)?.visibility = android.view.View.GONE
                        view?.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(fr.juju.myapplication.R.id.Autres)?.visibility = android.view.View.VISIBLE
                    }else {
                        view?.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(fr.juju.myapplication.R.id.Autres)?.visibility = android.view.View.GONE
                    }
                    if (currentDays.midi == "None" && currentDays.soir == "None" && currentDays.apero == "None" && currentDays.autres.isEmpty()) {
                        view?.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(fr.juju.myapplication.R.id.NoRepas)?.visibility = android.view.View.VISIBLE
                    }
                }
            }
            else {
                view.findViewById<ImageView>(R.id.echange).visibility = View.GONE
                view.findViewById<ImageView>(R.id.eye).visibility = View.VISIBLE
                view.findViewById<ImageView>(R.id.calendar).visibility = View.GONE
                currentSemaine = semainierList
                suivant = false
                reinitialisation()
                if (selectedDay == "lundi") {
                    switch(
                        selectedDay,
                        view.findViewById<TextView>(R.id.Lundi),
                        view.findViewById<ImageView>(R.id.Lundi_img)
                    )
                } else if (selectedDay == "mardi") {
                    switch(
                        selectedDay,
                        view.findViewById<TextView>(R.id.Mardi),
                        view.findViewById<ImageView>(R.id.Mardi_img)
                    )

                } else if (selectedDay == "mercredi") {
                    switch(
                        selectedDay,
                        view.findViewById<TextView>(R.id.Mercredi),
                        view.findViewById<ImageView>(R.id.Mercredi_img)
                    )

                } else if (selectedDay == "jeudi") {
                    switch(
                        selectedDay,
                        view.findViewById<TextView>(R.id.Jeudi),
                        view.findViewById<ImageView>(R.id.Jeudi_img)
                    )

                } else if (selectedDay == "vendredi") {
                    switch(
                        selectedDay,
                        view.findViewById<TextView>(R.id.Vendredi),
                        view.findViewById<ImageView>(R.id.Vendredi_img)
                    )

                } else if (selectedDay == "samedi") {
                    switch(
                        selectedDay,
                        view.findViewById<TextView>(R.id.Samedi),
                        view.findViewById<ImageView>(R.id.Samedi_img)
                    )

                } else if (selectedDay == "dimanche") {
                    switch(
                        selectedDay,
                        view.findViewById<TextView>(R.id.Dimanche),
                        view.findViewById<ImageView>(R.id.Dimanche_img)
                    )
                }
            }

            repoSemainier.updateData {
                currentDays = currentSemaine.filter { s -> s.id_semainier == selectedDay }[0]

                if (currentDays.autres.isNotEmpty()) {
                    view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
                    view?.findViewById<ConstraintLayout>(R.id.Autres)?.visibility = View.VISIBLE
                }else {
                    view?.findViewById<ConstraintLayout>(R.id.Autres)?.visibility = View.GONE
                }
                if (currentDays.midi == "None" && currentDays.soir == "None" && currentDays.apero == "None" && currentDays.autres.isEmpty()) {
                    view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.VISIBLE
                }
            }
        }

        if (currentDays.midi != "None") {
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.visibility = View.VISIBLE
            var currentRepasMidi = repasList.filter { s -> s.id == currentDays.midi }[0]
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.setOnClickListener {
                context.loadFragment(
                    RecetteFragment(
                        context,
                        currentRepasMidi,
                        "None",
                        "None",
                        "None"
                    )
                )
            }
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.nomMidi)?.text = currentRepasMidi.name
            view?.findViewById<TextView>(R.id.descriptionMidi)?.text = currentRepasMidi.duree
            Glide.with(context)
                .load(currentRepasMidi.imageUri)
                .into(view.findViewById<ImageView>(R.id.image_item2))

            val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.tagListMidi)
            collectionRecyclerView.adapter =
                TagsAdapter(context, currentRepasMidi.tags, R.layout.item_tags_horizontal)
        } else {
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.visibility = View.GONE
        }

        if (currentDays.soir != "None") {
            view?.findViewById<ConstraintLayout>(R.id.Soir)?.visibility = View.VISIBLE
            var currentRepasSoir = repasList.filter { s -> s.id == currentDays.soir }[0]
            view?.findViewById<ConstraintLayout>(R.id.Soir)?.setOnClickListener {
                context.loadFragment(
                    RecetteFragment(
                        context,
                        currentRepasSoir,
                        "None",
                        "None",
                        "None"
                    )
                )
            }
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.nomSoir)?.text = currentRepasSoir.name
            view?.findViewById<TextView>(R.id.descriptionSoir)?.text = currentRepasSoir.duree
            Glide.with(context)
                .load(currentRepasSoir.imageUri)
                .into(view.findViewById<ImageView>(R.id.image_item3))

            val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.tagListSoir)
            collectionRecyclerView.adapter =
                TagsAdapter(context, currentRepasSoir.tags, R.layout.item_tags_horizontal)

        } else {
            view?.findViewById<ConstraintLayout>(R.id.Soir)?.visibility = View.GONE
        }

        if (currentDays.apero != "None") {
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.visibility = View.VISIBLE
            var currentRepasApero = repasList.filter { s -> s.id == currentDays.apero }[0]
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.setOnClickListener {
                context.loadFragment(
                    RecetteFragment(
                        context,
                        currentRepasApero,
                        "None",
                        "None",
                        "None"
                    )
                )
            }
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.nomApero)?.text = currentRepasApero.name
            view?.findViewById<TextView>(R.id.descriptionApero)?.text = currentRepasApero.duree
            Glide.with(context)
                .load(currentRepasApero.imageUri)
                .into(view.findViewById<ImageView>(R.id.image_item4))


            val collectionRecyclerView = view.findViewById<RecyclerView>(R.id.tagListApero)
            collectionRecyclerView.adapter =
                TagsAdapter(context, currentRepasApero.tags, R.layout.item_tags_horizontal)

        } else {
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.visibility = View.GONE
        }

        if (currentDays.autres.isNotEmpty()) {
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
            view?.findViewById<ConstraintLayout>(R.id.Autres)?.visibility = View.VISIBLE
            for (repas in currentDays.autres) {
                repasAutresList.add(repasList.filter { s -> s.id == repas }[0])
            }
            if (suivant){
                recyclerAutres.adapter = AutresRapasSemainierAdapter(
                    context,
                    repasAutresList,
                    currentDays.id_semainier,
                    "suivant",
                    R.layout.item_semainier_autres_vertical
                )
                recyclerAutres.layoutManager = LinearLayoutManager(context)
            }else {
                recyclerAutres.adapter = AutresRapasSemainierAdapter(
                    context,
                    repasAutresList,
                    currentDays.id_semainier,
                    "courant",
                    R.layout.item_semainier_autres_vertical
                )
                recyclerAutres.layoutManager = LinearLayoutManager(context)
            }

        } else {
            view?.findViewById<ConstraintLayout>(R.id.Autres)?.visibility = View.GONE
        }

        if (currentDays.midi == "None" && currentDays.soir == "None" && currentDays.apero == "None" && currentDays.autres.isEmpty()) {
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.VISIBLE
        }

    if(suivant) {
            repoSemainierSuivant.updateData {
                currentDays = currentSemaine.filter { s -> s.id_semainier == selectedDay }[0]

                if (currentDays.autres.isNotEmpty()) {
                    view?.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(fr.juju.myapplication.R.id.NoRepas)?.visibility = android.view.View.GONE
                    view?.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(fr.juju.myapplication.R.id.Autres)?.visibility = android.view.View.VISIBLE
                }else {
                    view?.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(fr.juju.myapplication.R.id.Autres)?.visibility = android.view.View.GONE
                }
                if (currentDays.midi == "None" && currentDays.soir == "None" && currentDays.apero == "None" && currentDays.autres.isEmpty()) {
                    view?.findViewById<androidx.constraintlayout.widget.ConstraintLayout>(fr.juju.myapplication.R.id.NoRepas)?.visibility = android.view.View.VISIBLE
                }
            }
    }else {
        repoSemainier.updateData {
            currentDays = currentSemaine.filter { s -> s.id_semainier == selectedDay }[0]

            if (currentDays.autres.isNotEmpty()) {
                view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
                view?.findViewById<ConstraintLayout>(R.id.Autres)?.visibility = View.VISIBLE
            }else {
                view?.findViewById<ConstraintLayout>(R.id.Autres)?.visibility = View.GONE
            }
            if (currentDays.midi == "None" && currentDays.soir == "None" && currentDays.apero == "None" && currentDays.autres.isEmpty()) {
                view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.VISIBLE
            }
        }
    }

        view.findViewById<TextView>(R.id.Lundi).setOnClickListener {
            if (suivant == false) {
                selectedDay = "lundi"
                reinitialisation()
                switch(
                    "lundi",
                    view.findViewById<TextView>(R.id.Lundi),
                    view.findViewById<ImageView>(R.id.Lundi_img)
                )
            } else {
                selectedDay = "lundi"
                reinitialisationSuivant()
                switchSuivant(
                    "lundi",
                    view.findViewById<TextView>(R.id.Lundi),
                    view.findViewById<ImageView>(R.id.Lundi_img)
                )
            }
        }
        view.findViewById<ImageView>(R.id.Lundi_img).setOnClickListener {
            if (suivant == false) {
                selectedDay = "lundi"
                reinitialisation()
                switch(
                    "lundi",
                    view.findViewById<TextView>(R.id.Lundi),
                    view.findViewById<ImageView>(R.id.Lundi_img)
                )
            } else {
                selectedDay = "lundi"
                reinitialisationSuivant()
                switchSuivant(
                    "lundi",
                    view.findViewById<TextView>(R.id.Lundi),
                    view.findViewById<ImageView>(R.id.Lundi_img)
                )
            }
        }
        view.findViewById<TextView>(R.id.Mardi).setOnClickListener {
            if (suivant == false) {
                selectedDay = "mardi"
                reinitialisation()
                switch(
                    "mardi",
                    view.findViewById<TextView>(R.id.Mardi),
                    view.findViewById<ImageView>(R.id.Mardi_img)
                )
            } else {
                selectedDay = "mardi"
                reinitialisationSuivant()
                switchSuivant(
                    "mardi",
                    view.findViewById<TextView>(R.id.Mardi),
                    view.findViewById<ImageView>(R.id.Mardi_img)
                )
            }
        }
        view.findViewById<ImageView>(R.id.Mardi_img).setOnClickListener {
            if (suivant == false) {
                selectedDay = "mardi"
                reinitialisation()
                switch(
                    "mardi",
                    view.findViewById<TextView>(R.id.Mardi),
                    view.findViewById<ImageView>(R.id.Mardi_img)
                )
            } else {
                selectedDay = "mardi"
                reinitialisationSuivant()
                switchSuivant(
                    "mardi",
                    view.findViewById<TextView>(R.id.Mardi),
                    view.findViewById<ImageView>(R.id.Mardi_img)
                )
            }
        }
        view.findViewById<TextView>(R.id.Mercredi).setOnClickListener {
            if (suivant == false) {
                selectedDay = "mercredi"
                reinitialisation()
                switch(
                    "mercredi",
                    view.findViewById<TextView>(R.id.Mercredi),
                    view.findViewById<ImageView>(R.id.Mercredi_img)
                )
            } else {
                selectedDay = "mercredi"
                reinitialisationSuivant()
                switchSuivant(
                    "mercredi",
                    view.findViewById<TextView>(R.id.Mercredi),
                    view.findViewById<ImageView>(R.id.Mercredi_img)
                )
            }
        }
        view.findViewById<ImageView>(R.id.Mercredi_img).setOnClickListener {
            if (suivant == false) {
                selectedDay = "mercredi"
                reinitialisation()
                switch(
                    "mercredi",
                    view.findViewById<TextView>(R.id.Mercredi),
                    view.findViewById<ImageView>(R.id.Mercredi_img)
                )
            } else {
                selectedDay = "mercredi"
                reinitialisationSuivant()
                switchSuivant(
                    "mercredi",
                    view.findViewById<TextView>(R.id.Mercredi),
                    view.findViewById<ImageView>(R.id.Mercredi_img)
                )
            }
        }
        view.findViewById<TextView>(R.id.Jeudi).setOnClickListener {
            if (suivant == false) {
                selectedDay = "jeudi"
                reinitialisation()
                switch(
                    "jeudi",
                    view.findViewById<TextView>(R.id.Jeudi),
                    view.findViewById<ImageView>(R.id.Jeudi_img)
                )
            } else {
                selectedDay = "jeudi"
                reinitialisationSuivant()
                switchSuivant(
                    "jeudi",
                    view.findViewById<TextView>(R.id.Jeudi),
                    view.findViewById<ImageView>(R.id.Jeudi_img)
                )
            }
        }
        view.findViewById<ImageView>(R.id.Jeudi_img).setOnClickListener {
            if (suivant == false) {
                selectedDay = "jeudi"
                reinitialisation()
                switch(
                    "jeudi",
                    view.findViewById<TextView>(R.id.Jeudi),
                    view.findViewById<ImageView>(R.id.Jeudi_img)
                )
            } else {
                selectedDay = "jeudi"
                reinitialisationSuivant()
                switchSuivant(
                    "jeudi",
                    view.findViewById<TextView>(R.id.Jeudi),
                    view.findViewById<ImageView>(R.id.Jeudi_img)
                )
            }
        }
        view.findViewById<TextView>(R.id.Vendredi).setOnClickListener {
            if (suivant == false) {
                selectedDay = "vendredi"
                reinitialisation()
                switch(
                    "vendredi",
                    view.findViewById<TextView>(R.id.Vendredi),
                    view.findViewById<ImageView>(R.id.Vendredi_img)
                )
            } else {
                selectedDay = "vendredi"
                reinitialisationSuivant()
                switchSuivant(
                    "vendredi",
                    view.findViewById<TextView>(R.id.Vendredi),
                    view.findViewById<ImageView>(R.id.Vendredi_img)
                )
            }
        }
        view.findViewById<ImageView>(R.id.Vendredi_img).setOnClickListener {
            if (suivant == false) {
                selectedDay = "vendredi"
                reinitialisation()
                switch(
                    "vendredi",
                    view.findViewById<TextView>(R.id.Vendredi),
                    view.findViewById<ImageView>(R.id.Vendredi_img)
                )
            } else {
                selectedDay = "vendredi"
                reinitialisationSuivant()
                switchSuivant(
                    "vendredi",
                    view.findViewById<TextView>(R.id.Vendredi),
                    view.findViewById<ImageView>(R.id.Vendredi_img)
                )
            }
        }
        view.findViewById<TextView>(R.id.Samedi).setOnClickListener {
            if (suivant == false) {
                selectedDay = "samedi"
                reinitialisation()
                switch(
                    "samedi",
                    view.findViewById<TextView>(R.id.Samedi),
                    view.findViewById<ImageView>(R.id.Samedi_img)
                )
            } else {
                selectedDay = "samedi"
                reinitialisationSuivant()
                switchSuivant(
                    "samedi",
                    view.findViewById<TextView>(R.id.Samedi),
                    view.findViewById<ImageView>(R.id.Samedi_img)
                )
            }
        }
        view.findViewById<ImageView>(R.id.Samedi_img).setOnClickListener {
            if (suivant == false) {
                selectedDay = "samedi"
                reinitialisation()
                switch(
                    "samedi",
                    view.findViewById<TextView>(R.id.Samedi),
                    view.findViewById<ImageView>(R.id.Samedi_img)
                )
            } else {
                selectedDay = "samedi"
                reinitialisationSuivant()
                switchSuivant(
                    "samedi",
                    view.findViewById<TextView>(R.id.Samedi),
                    view.findViewById<ImageView>(R.id.Samedi_img)
                )
            }
        }
        view.findViewById<TextView>(R.id.Dimanche).setOnClickListener {
            if (suivant == false) {
                selectedDay = "dimanche"
                reinitialisation()
                switch(
                    "dimanche",
                    view.findViewById<TextView>(R.id.Dimanche),
                    view.findViewById<ImageView>(R.id.Dimanche_img)
                )
            } else {
                selectedDay = "dimanche"
                reinitialisationSuivant()
                switchSuivant(
                    "dimanche",
                    view.findViewById<TextView>(R.id.Dimanche),
                    view.findViewById<ImageView>(R.id.Dimanche_img)
                )

            }
        }
        view.findViewById<ImageView>(R.id.Dimanche_img).setOnClickListener {
            if (suivant == false) {
                selectedDay = "dimanche"
                reinitialisation()
                switch(
                    "dimanche",
                    view.findViewById<TextView>(R.id.Dimanche),
                    view.findViewById<ImageView>(R.id.Dimanche_img)
                )
            } else {
                selectedDay = "dimanche"
                reinitialisationSuivant()
                switchSuivant(
                    "dimanche",
                    view.findViewById<TextView>(R.id.Dimanche),
                    view.findViewById<ImageView>(R.id.Dimanche_img)
                )

            }
        }

        view.findViewById<ConstraintLayout>(R.id.affect_repas_midi).visibility = View.GONE
        view.findViewById<ConstraintLayout>(R.id.affect_repas_apero).visibility = View.GONE
        view.findViewById<ConstraintLayout>(R.id.affect_repas_soir).visibility = View.GONE
        view.findViewById<ConstraintLayout>(R.id.affect_repas_autres).visibility = View.GONE

        view.findViewById<ImageView>(R.id.affect_repas).setOnClickListener {
            enableEdit(view)
        }

        view.findViewById<ConstraintLayout>(R.id.affect_repas_soir).setOnClickListener {
            context.printSoir()
            if (suivant) {
                context.loadFragment(FiltreRepasFragment(context, "soir", selectedDay, "suivant"))
            } else context.loadFragment(
                FiltreRepasFragment(
                    context,
                    "soir",
                    selectedDay,
                    "courant"
                )
            )
        }
        view.findViewById<ConstraintLayout>(R.id.affect_repas_apero).setOnClickListener {
            context.printApero()
            if (suivant) {
                context.loadFragment(FiltreRepasFragment(context, "apero", selectedDay, "suivant"))
            } else context.loadFragment(
                FiltreRepasFragment(
                    context,
                    "apero",
                    selectedDay,
                    "courant"
                )
            )

        }
        view.findViewById<ConstraintLayout>(R.id.affect_repas_midi).setOnClickListener {
            context.printMidi()
            if (suivant) {
                context.loadFragment(FiltreRepasFragment(context, "midi", selectedDay, "suivant"))
            } else context.loadFragment(
                FiltreRepasFragment(
                    context,
                    "midi",
                    selectedDay,
                    "courant"
                )
            )
        }
        view.findViewById<ConstraintLayout>(R.id.affect_repas_autres).setOnClickListener {
            context.printAutres()
            if (suivant) {
                context.loadFragment(FiltreRepasFragment(context, "autres", selectedDay, "suivant"))
            } else context.loadFragment(
                FiltreRepasFragment(
                    context,
                    "autres",
                    selectedDay,
                    "courant"
                )
            )
        }

        val translate = AnimationUtils.loadAnimation(context, R.anim.translate_anim)
        view.findViewById<ConstraintLayout>(R.id.Midi).startAnimation(translate)
        view.findViewById<ConstraintLayout>(R.id.Soir).startAnimation(translate)
        view.findViewById<ConstraintLayout>(R.id.Apero).startAnimation(translate)
        view.findViewById<ConstraintLayout>(R.id.Autres).startAnimation(translate)
        view.findViewById<ConstraintLayout>(R.id.NoRepas).startAnimation(translate)
        view.findViewById<TextView>(R.id.substyle).startAnimation(translate)
        view.findViewById<ImageView>(R.id.imageView5).startAnimation(translate)

        val repo = SemainierRepository()
        val repoSuivant = SemainierSuivantRepository()
        view.findViewById<ImageView>(R.id.delete_midi).setOnClickListener {
            if (suivant) {
                deleteIngredientCourse(ingredientList.filter { s -> s.id_repas == semainierSuivantList.filter { s -> s.id_semainier == selectedDay }[0].midi } as ArrayList<IngredientModel>)
                repoSuivant.resetMidi(selectedDay)
            } else repo.resetMidi(selectedDay)

            Toast.makeText(context, "Repas du $selectedDay midi supprimé", Toast.LENGTH_SHORT)
                .show()
            if (selectedDay == "lundi") {
                if (suivant == false) {
                    selectedDay = "lundi"
                    reinitialisation()
                    switch(
                        "lundi",
                        view.findViewById<TextView>(R.id.Lundi),
                        view.findViewById<ImageView>(R.id.Lundi_img)
                    )
                } else {
                    selectedDay = "lundi"
                    reinitialisationSuivant()
                    switchSuivant(
                        "lundi",
                        view.findViewById<TextView>(R.id.Lundi),
                        view.findViewById<ImageView>(R.id.Lundi_img)
                    )
                }
            } else if (selectedDay == "mardi") {
                if (suivant == false) {
                    selectedDay = "mardi"
                    reinitialisation()
                    switch(
                        "mardi",
                        view.findViewById<TextView>(R.id.Mardi),
                        view.findViewById<ImageView>(R.id.Mardi_img)
                    )
                } else {
                    selectedDay = "mardi"
                    reinitialisationSuivant()
                    switchSuivant(
                        "mardi",
                        view.findViewById<TextView>(R.id.Mardi),
                        view.findViewById<ImageView>(R.id.Mardi_img)
                    )
                }


            } else if (selectedDay == "mercredi") {
                if (suivant == false) {
                    selectedDay = "mercredi"
                    reinitialisation()
                    switch(
                        "mercredi",
                        view.findViewById<TextView>(R.id.Mercredi),
                        view.findViewById<ImageView>(R.id.Mercredi_img)
                    )
                } else {
                    selectedDay = "mercredi"
                    reinitialisationSuivant()
                    switchSuivant(
                        "mercredi",
                        view.findViewById<TextView>(R.id.Mercredi),
                        view.findViewById<ImageView>(R.id.Mercredi_img)
                    )
                }


            } else if (selectedDay == "jeudi") {
                if (suivant == false) {
                    selectedDay = "jeudi"
                    reinitialisation()
                    switch(
                        "jeudi",
                        view.findViewById<TextView>(R.id.Jeudi),
                        view.findViewById<ImageView>(R.id.Jeudi_img)
                    )
                } else {
                    selectedDay = "jeudi"
                    reinitialisationSuivant()
                    switchSuivant(
                        "jeudi",
                        view.findViewById<TextView>(R.id.Jeudi),
                        view.findViewById<ImageView>(R.id.Jeudi_img)
                    )
                }


            } else if (selectedDay == "vendredi") {
                if (suivant == false) {
                    selectedDay = "vendredi"
                    reinitialisation()
                    switch(
                        "vendredi",
                        view.findViewById<TextView>(R.id.Vendredi),
                        view.findViewById<ImageView>(R.id.Vendredi_img)
                    )
                } else {
                    selectedDay = "vendredi"
                    reinitialisationSuivant()
                    switchSuivant(
                        "vendredi",
                        view.findViewById<TextView>(R.id.Vendredi),
                        view.findViewById<ImageView>(R.id.Vendredi_img)
                    )
                }


            } else if (selectedDay == "samedi") {
                if (suivant == false) {
                    selectedDay = "samedi"
                    reinitialisation()
                    switch(
                        "samedi",
                        view.findViewById<TextView>(R.id.Samedi),
                        view.findViewById<ImageView>(R.id.Samedi_img)
                    )
                } else {
                    selectedDay = "samedi"
                    reinitialisationSuivant()
                    switchSuivant(
                        "samedi",
                        view.findViewById<TextView>(R.id.Samedi),
                        view.findViewById<ImageView>(R.id.Samedi_img)
                    )
                }


            } else if (selectedDay == "dimanche") {
                if (suivant == false) {
                    selectedDay = "dimanche"
                    reinitialisation()
                    switch(
                        "dimanche",
                        view.findViewById<TextView>(R.id.Dimanche),
                        view.findViewById<ImageView>(R.id.Dimanche_img)
                    )
                } else {
                    selectedDay = "dimanche"
                    reinitialisationSuivant()
                    switchSuivant(
                        "dimanche",
                        view.findViewById<TextView>(R.id.Dimanche),
                        view.findViewById<ImageView>(R.id.Dimanche_img)
                    )

                }

            }
        }
        view.findViewById<ImageView>(R.id.delete_apero).setOnClickListener {
            if (suivant) {
                deleteIngredientCourse(ingredientList.filter { s -> s.id_repas == semainierSuivantList.filter { s -> s.id_semainier == selectedDay }[0].apero } as ArrayList<IngredientModel>)
                repoSuivant.resetApero(selectedDay)
            } else repo.resetApero(selectedDay)

            Toast.makeText(context, "Apéro du $selectedDay supprimé (sniff)", Toast.LENGTH_SHORT)
                .show()
            if (selectedDay == "lundi") {
                if (suivant == false) {
                    selectedDay = "lundi"
                    reinitialisation()
                    switch(
                        "lundi",
                        view.findViewById<TextView>(R.id.Lundi),
                        view.findViewById<ImageView>(R.id.Lundi_img)
                    )
                } else {
                    selectedDay = "lundi"
                    reinitialisationSuivant()
                    switchSuivant(
                        "lundi",
                        view.findViewById<TextView>(R.id.Lundi),
                        view.findViewById<ImageView>(R.id.Lundi_img)
                    )
                }
            } else if (selectedDay == "mardi") {
                if (suivant == false) {
                    selectedDay = "mardi"
                    reinitialisation()
                    switch(
                        "mardi",
                        view.findViewById<TextView>(R.id.Mardi),
                        view.findViewById<ImageView>(R.id.Mardi_img)
                    )
                } else {
                    selectedDay = "mardi"
                    reinitialisationSuivant()
                    switchSuivant(
                        "mardi",
                        view.findViewById<TextView>(R.id.Mardi),
                        view.findViewById<ImageView>(R.id.Mardi_img)
                    )
                }


            } else if (selectedDay == "mercredi") {
                if (suivant == false) {
                    selectedDay = "mercredi"
                    reinitialisation()
                    switch(
                        "mercredi",
                        view.findViewById<TextView>(R.id.Mercredi),
                        view.findViewById<ImageView>(R.id.Mercredi_img)
                    )
                } else {
                    selectedDay = "mercredi"
                    reinitialisationSuivant()
                    switchSuivant(
                        "mercredi",
                        view.findViewById<TextView>(R.id.Mercredi),
                        view.findViewById<ImageView>(R.id.Mercredi_img)
                    )
                }


            } else if (selectedDay == "jeudi") {
                if (suivant == false) {
                    selectedDay = "jeudi"
                    reinitialisation()
                    switch(
                        "jeudi",
                        view.findViewById<TextView>(R.id.Jeudi),
                        view.findViewById<ImageView>(R.id.Jeudi_img)
                    )
                } else {
                    selectedDay = "jeudi"
                    reinitialisationSuivant()
                    switchSuivant(
                        "jeudi",
                        view.findViewById<TextView>(R.id.Jeudi),
                        view.findViewById<ImageView>(R.id.Jeudi_img)
                    )
                }


            } else if (selectedDay == "vendredi") {
                if (suivant == false) {
                    selectedDay = "vendredi"
                    reinitialisation()
                    switch(
                        "vendredi",
                        view.findViewById<TextView>(R.id.Vendredi),
                        view.findViewById<ImageView>(R.id.Vendredi_img)
                    )
                } else {
                    selectedDay = "vendredi"
                    reinitialisationSuivant()
                    switchSuivant(
                        "vendredi",
                        view.findViewById<TextView>(R.id.Vendredi),
                        view.findViewById<ImageView>(R.id.Vendredi_img)
                    )
                }


            } else if (selectedDay == "samedi") {
                if (suivant == false) {
                    selectedDay = "samedi"
                    reinitialisation()
                    switch(
                        "samedi",
                        view.findViewById<TextView>(R.id.Samedi),
                        view.findViewById<ImageView>(R.id.Samedi_img)
                    )
                } else {
                    selectedDay = "samedi"
                    reinitialisationSuivant()
                    switchSuivant(
                        "samedi",
                        view.findViewById<TextView>(R.id.Samedi),
                        view.findViewById<ImageView>(R.id.Samedi_img)
                    )
                }


            } else if (selectedDay == "dimanche") {
                if (suivant == false) {
                    selectedDay = "dimanche"
                    reinitialisation()
                    switch(
                        "dimanche",
                        view.findViewById<TextView>(R.id.Dimanche),
                        view.findViewById<ImageView>(R.id.Dimanche_img)
                    )
                } else {
                    selectedDay = "dimanche"
                    reinitialisationSuivant()
                    switchSuivant(
                        "dimanche",
                        view.findViewById<TextView>(R.id.Dimanche),
                        view.findViewById<ImageView>(R.id.Dimanche_img)
                    )

                }

            }

        }
        view.findViewById<ImageView>(R.id.delete_soir).setOnClickListener {
            if (suivant) {
                deleteIngredientCourse(ingredientList.filter { s -> s.id_repas == semainierSuivantList.filter { s -> s.id_semainier == selectedDay }[0].soir } as ArrayList<IngredientModel>)
                repoSuivant.resetSoir(selectedDay)
            } else repo.resetSoir(selectedDay)
            Toast.makeText(context, "Repas du $selectedDay soir supprimé", Toast.LENGTH_SHORT)
                .show()
            if (selectedDay == "lundi") {
                if (suivant == false) {
                    selectedDay = "lundi"
                    reinitialisation()
                    switch(
                        "lundi",
                        view.findViewById<TextView>(R.id.Lundi),
                        view.findViewById<ImageView>(R.id.Lundi_img)
                    )
                } else {
                    selectedDay = "lundi"
                    reinitialisationSuivant()
                    switchSuivant(
                        "lundi",
                        view.findViewById<TextView>(R.id.Lundi),
                        view.findViewById<ImageView>(R.id.Lundi_img)
                    )
                }
            } else if (selectedDay == "mardi") {
                if (suivant == false) {
                    selectedDay = "mardi"
                    reinitialisation()
                    switch(
                        "mardi",
                        view.findViewById<TextView>(R.id.Mardi),
                        view.findViewById<ImageView>(R.id.Mardi_img)
                    )
                } else {
                    selectedDay = "mardi"
                    reinitialisationSuivant()
                    switchSuivant(
                        "mardi",
                        view.findViewById<TextView>(R.id.Mardi),
                        view.findViewById<ImageView>(R.id.Mardi_img)
                    )
                }


            } else if (selectedDay == "mercredi") {
                if (suivant == false) {
                    selectedDay = "mercredi"
                    reinitialisation()
                    switch(
                        "mercredi",
                        view.findViewById<TextView>(R.id.Mercredi),
                        view.findViewById<ImageView>(R.id.Mercredi_img)
                    )
                } else {
                    selectedDay = "mercredi"
                    reinitialisationSuivant()
                    switchSuivant(
                        "mercredi",
                        view.findViewById<TextView>(R.id.Mercredi),
                        view.findViewById<ImageView>(R.id.Mercredi_img)
                    )
                }


            } else if (selectedDay == "jeudi") {
                if (suivant == false) {
                    selectedDay = "jeudi"
                    reinitialisation()
                    switch(
                        "jeudi",
                        view.findViewById<TextView>(R.id.Jeudi),
                        view.findViewById<ImageView>(R.id.Jeudi_img)
                    )
                } else {
                    selectedDay = "jeudi"
                    reinitialisationSuivant()
                    switchSuivant(
                        "jeudi",
                        view.findViewById<TextView>(R.id.Jeudi),
                        view.findViewById<ImageView>(R.id.Jeudi_img)
                    )
                }


            } else if (selectedDay == "vendredi") {
                if (suivant == false) {
                    selectedDay = "vendredi"
                    reinitialisation()
                    switch(
                        "vendredi",
                        view.findViewById<TextView>(R.id.Vendredi),
                        view.findViewById<ImageView>(R.id.Vendredi_img)
                    )
                } else {
                    selectedDay = "vendredi"
                    reinitialisationSuivant()
                    switchSuivant(
                        "vendredi",
                        view.findViewById<TextView>(R.id.Vendredi),
                        view.findViewById<ImageView>(R.id.Vendredi_img)
                    )
                }


            } else if (selectedDay == "samedi") {
                if (suivant == false) {
                    selectedDay = "samedi"
                    reinitialisation()
                    switch(
                        "samedi",
                        view.findViewById<TextView>(R.id.Samedi),
                        view.findViewById<ImageView>(R.id.Samedi_img)
                    )
                } else {
                    selectedDay = "samedi"
                    reinitialisationSuivant()
                    switchSuivant(
                        "samedi",
                        view.findViewById<TextView>(R.id.Samedi),
                        view.findViewById<ImageView>(R.id.Samedi_img)
                    )
                }


            } else if (selectedDay == "dimanche") {
                if (suivant == false) {
                    selectedDay = "dimanche"
                    reinitialisation()
                    switch(
                        "dimanche",
                        view.findViewById<TextView>(R.id.Dimanche),
                        view.findViewById<ImageView>(R.id.Dimanche_img)
                    )
                } else {
                    selectedDay = "dimanche"
                    reinitialisationSuivant()
                    switchSuivant(
                        "dimanche",
                        view.findViewById<TextView>(R.id.Dimanche),
                        view.findViewById<ImageView>(R.id.Dimanche_img)
                    )

                }

            }

        }
        view.findViewById<ImageView>(R.id.echange).setOnClickListener {
            var builder = AlertDialog.Builder(context)
            builder.setTitle("Oulaaaaaa !")
            builder.setMessage("Tu veux vraiment transférer les repas d'une semaine sur l'autre et supprimer la liste de course ?")
            builder.setPositiveButton("Oui", DialogInterface.OnClickListener { dialog, id ->
                echange()
                dialog.cancel()
            })
            builder.setNegativeButton("Non", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })
            var alert: AlertDialog = builder.create()
            alert.show()
        }


        return view
    }

    private fun echange() {
        var repo = SemainierRepository()
        repo.setMidi(
            "lundi",
            semainierSuivantList.filter { s -> s.id_semainier == "lundi" }[0].midi
        )
        repo.setMidi(
            "mardi",
            semainierSuivantList.filter { s -> s.id_semainier == "mardi" }[0].midi
        )
        repo.setMidi(
            "mercredi",
            semainierSuivantList.filter { s -> s.id_semainier == "mercredi" }[0].midi
        )
        repo.setMidi(
            "jeudi",
            semainierSuivantList.filter { s -> s.id_semainier == "jeudi" }[0].midi
        )
        repo.setMidi(
            "vendredi",
            semainierSuivantList.filter { s -> s.id_semainier == "vendredi" }[0].midi
        )
        repo.setMidi(
            "samedi",
            semainierSuivantList.filter { s -> s.id_semainier == "samedi" }[0].midi
        )
        repo.setMidi(
            "dimanche",
            semainierSuivantList.filter { s -> s.id_semainier == "dimanche" }[0].midi
        )

        repo.setApero(
            "lundi",
            semainierSuivantList.filter { s -> s.id_semainier == "lundi" }[0].apero
        )
        repo.setApero(
            "mardi",
            semainierSuivantList.filter { s -> s.id_semainier == "mardi" }[0].apero
        )
        repo.setApero(
            "mercredi",
            semainierSuivantList.filter { s -> s.id_semainier == "mercredi" }[0].apero
        )
        repo.setApero(
            "jeudi",
            semainierSuivantList.filter { s -> s.id_semainier == "jeudi" }[0].apero
        )
        repo.setApero(
            "vendredi",
            semainierSuivantList.filter { s -> s.id_semainier == "vendredi" }[0].apero
        )
        repo.setApero(
            "samedi",
            semainierSuivantList.filter { s -> s.id_semainier == "samedi" }[0].apero
        )
        repo.setApero(
            "dimanche",
            semainierSuivantList.filter { s -> s.id_semainier == "dimanche" }[0].apero
        )

        repo.setSoir(
            "lundi",
            semainierSuivantList.filter { s -> s.id_semainier == "lundi" }[0].soir
        )
        repo.setSoir(
            "mardi",
            semainierSuivantList.filter { s -> s.id_semainier == "mardi" }[0].soir
        )
        repo.setSoir(
            "mercredi",
            semainierSuivantList.filter { s -> s.id_semainier == "mercredi" }[0].soir
        )
        repo.setSoir(
            "jeudi",
            semainierSuivantList.filter { s -> s.id_semainier == "jeudi" }[0].soir
        )
        repo.setSoir(
            "vendredi",
            semainierSuivantList.filter { s -> s.id_semainier == "vendredi" }[0].soir
        )
        repo.setSoir(
            "samedi",
            semainierSuivantList.filter { s -> s.id_semainier == "samedi" }[0].soir
        )
        repo.setSoir(
            "dimanche",
            semainierSuivantList.filter { s -> s.id_semainier == "dimanche" }[0].soir
        )

        repo.setAutres(
            "lundi",
            semainierSuivantList.filter { s -> s.id_semainier == "lundi" }[0].autres
        )
        repo.setAutres(
            "mardi",
            semainierSuivantList.filter { s -> s.id_semainier == "mardi" }[0].autres
        )
        repo.setAutres(
            "mercredi",
            semainierSuivantList.filter { s -> s.id_semainier == "mercredi" }[0].autres
        )
        repo.setAutres(
            "jeudi",
            semainierSuivantList.filter { s -> s.id_semainier == "jeudi" }[0].autres
        )
        repo.setAutres(
            "vendredi",
            semainierSuivantList.filter { s -> s.id_semainier == "vendredi" }[0].autres
        )
        repo.setAutres(
            "samedi",
            semainierSuivantList.filter { s -> s.id_semainier == "samedi" }[0].autres
        )
        repo.setAutres(
            "dimanche",
            semainierSuivantList.filter { s -> s.id_semainier == "dimanche" }[0].autres
        )

        var repo2 = SemainierSuivantRepository()
        repo2.resetMidi("lundi")
        repo2.resetSoir("lundi")
        repo2.resetApero("lundi")
        repo2.resetAutres("lundi")

        repo2.resetMidi("mardi")
        repo2.resetSoir("mardi")
        repo2.resetApero("mardi")
        repo2.resetAutres("mardi")

        repo2.resetMidi("mercredi")
        repo2.resetSoir("mercredi")
        repo2.resetApero("mercredi")
        repo2.resetAutres("mercredi")

        repo2.resetMidi("jeudi")
        repo2.resetSoir("jeudi")
        repo2.resetApero("jeudi")
        repo2.resetAutres("jeudi")

        repo2.resetMidi("vendredi")
        repo2.resetSoir("vendredi")
        repo2.resetApero("vendredi")
        repo2.resetAutres("vendredi")

        repo2.resetMidi("samedi")
        repo2.resetSoir("samedi")
        repo2.resetApero("samedi")
        repo2.resetAutres("samedi")

        repo2.resetMidi("dimanche")
        repo2.resetSoir("dimanche")
        repo2.resetApero("dimanche")
        repo2.resetAutres("dimanche")

        if (suivant) {
            reinitialisationSuivant()
        } else reinitialisation()
        clearCourse()
        view?.findViewById<Switch>(R.id.toggleButton)?.performClick()
    }

    private fun clearCourse() {
        var repo = CourseRepository()
        for (courseItem in CourseRepository.Singleton.courseList) {
            if (courseItem.ok == "true") {
                repo.deleteCourseItem(courseItem)
            }
        }
    }

    @SuppressLint("CutPasteId")
    private fun enableEdit(view: View?) {
        if (!enable) {
            enable = true
            view?.findViewById<ConstraintLayout>(R.id.affect_repas_midi)?.visibility = View.VISIBLE
            view?.findViewById<ConstraintLayout>(R.id.affect_repas_apero)?.visibility = View.VISIBLE
            view?.findViewById<ConstraintLayout>(R.id.affect_repas_soir)?.visibility = View.VISIBLE
            view?.findViewById<ConstraintLayout>(R.id.affect_repas_autres)?.visibility = View.VISIBLE
            view?.findViewById<ScrollView>(R.id.scrollView)?.alpha = 0.25F
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.isEnabled = false
            view?.findViewById<ConstraintLayout>(R.id.Soir)?.isEnabled = false
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.isEnabled = false
            view?.findViewById<ConstraintLayout>(R.id.Autres)?.isEnabled = false
            view?.findViewById<ImageView>(R.id.Lundi_img)?.isEnabled = false
            view?.findViewById<ImageView>(R.id.Mardi_img)?.isEnabled = false
            view?.findViewById<ImageView>(R.id.Mercredi_img)?.isEnabled = false
            view?.findViewById<ImageView>(R.id.Jeudi_img)?.isEnabled = false
            view?.findViewById<ImageView>(R.id.Vendredi_img)?.isEnabled = false
            view?.findViewById<ImageView>(R.id.Samedi_img)?.isEnabled = false
            view?.findViewById<ImageView>(R.id.Dimanche_img)?.isEnabled = false
            view?.findViewById<TextView>(R.id.Lundi)?.isEnabled = false
            view?.findViewById<TextView>(R.id.Mardi)?.isEnabled = false
            view?.findViewById<TextView>(R.id.Mercredi)?.isEnabled = false
            view?.findViewById<TextView>(R.id.Jeudi)?.isEnabled = false
            view?.findViewById<TextView>(R.id.Vendredi)?.isEnabled = false
            view?.findViewById<TextView>(R.id.Samedi)?.isEnabled = false
            view?.findViewById<TextView>(R.id.Dimanche)?.isEnabled = false
            val translateAnim = AnimationUtils.loadAnimation(context, R.anim.translate_anim_affect)
            view?.findViewById<ConstraintLayout>(R.id.affect_repas_midi)
                ?.startAnimation(translateAnim)
            view?.findViewById<ConstraintLayout>(R.id.affect_repas_apero)
                ?.startAnimation(translateAnim)
            view?.findViewById<ConstraintLayout>(R.id.affect_repas_soir)
                ?.startAnimation(translateAnim)
            view?.findViewById<ConstraintLayout>(R.id.affect_repas_autres)
                ?.startAnimation(translateAnim)
            view?.findViewById<ImageView>(R.id.affect_repas)?.animate()?.rotation(45F)?.duration =
                250
        } else {
            enable = false
            val translateAntiAnim =
                AnimationUtils.loadAnimation(context, R.anim.translate_anti_anim_affect)
            view?.findViewById<ScrollView>(R.id.scrollView)?.alpha = 1F
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.isEnabled = true
            view?.findViewById<ConstraintLayout>(R.id.Soir)?.isEnabled = true
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.isEnabled = true
            view?.findViewById<ConstraintLayout>(R.id.Autres)?.isEnabled = true
            view?.findViewById<ImageView>(R.id.Lundi_img)?.isEnabled = true
            view?.findViewById<ImageView>(R.id.Mardi_img)?.isEnabled = true
            view?.findViewById<ImageView>(R.id.Mercredi_img)?.isEnabled = true
            view?.findViewById<ImageView>(R.id.Jeudi_img)?.isEnabled = true
            view?.findViewById<ImageView>(R.id.Vendredi_img)?.isEnabled = true
            view?.findViewById<ImageView>(R.id.Samedi_img)?.isEnabled = true
            view?.findViewById<ImageView>(R.id.Dimanche_img)?.isEnabled = true
            view?.findViewById<TextView>(R.id.Lundi)?.isEnabled = true
            view?.findViewById<TextView>(R.id.Mardi)?.isEnabled = true
            view?.findViewById<TextView>(R.id.Mercredi)?.isEnabled = true
            view?.findViewById<TextView>(R.id.Jeudi)?.isEnabled = true
            view?.findViewById<TextView>(R.id.Vendredi)?.isEnabled = true
            view?.findViewById<TextView>(R.id.Samedi)?.isEnabled = true
            view?.findViewById<TextView>(R.id.Dimanche)?.isEnabled = true
            view?.findViewById<ConstraintLayout>(R.id.affect_repas_midi)?.visibility = View.GONE
            view?.findViewById<ConstraintLayout>(R.id.affect_repas_apero)?.visibility = View.GONE
            view?.findViewById<ConstraintLayout>(R.id.affect_repas_soir)?.visibility = View.GONE
            view?.findViewById<ConstraintLayout>(R.id.affect_repas_autres)?.visibility = View.GONE
            view?.findViewById<ImageView>(R.id.affect_repas)?.animate()?.rotation(0F)?.duration =
                250
            view?.findViewById<ConstraintLayout>(R.id.affect_repas_midi)
                ?.startAnimation(translateAntiAnim)
            view?.findViewById<ConstraintLayout>(R.id.affect_repas_apero)
                ?.startAnimation(translateAntiAnim)
            view?.findViewById<ConstraintLayout>(R.id.affect_repas_soir)
                ?.startAnimation(translateAntiAnim)
            view?.findViewById<ConstraintLayout>(R.id.affect_repas_autres)
                ?.startAnimation(translateAntiAnim)

        }


    }


    private fun reinitialisation() {
        view?.findViewById<TextView>(R.id.Lundi)?.setTypeface(null, Typeface.NORMAL)
        view?.findViewById<TextView>(R.id.Mardi)?.setTypeface(null, Typeface.NORMAL)
        view?.findViewById<TextView>(R.id.Mercredi)?.setTypeface(null, Typeface.NORMAL)
        view?.findViewById<TextView>(R.id.Jeudi)?.setTypeface(null, Typeface.NORMAL)
        view?.findViewById<TextView>(R.id.Vendredi)?.setTypeface(null, Typeface.NORMAL)
        view?.findViewById<TextView>(R.id.Samedi)?.setTypeface(null, Typeface.NORMAL)
        view?.findViewById<TextView>(R.id.Dimanche)?.setTypeface(null, Typeface.NORMAL)

        if (currentDay == "lundi") {
            view?.findViewById<ImageView>(R.id.Lundi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Mardi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Mercredi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Jeudi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Vendredi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Samedi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Dimanche_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
        }
        if (currentDay == "mardi") {
            view?.findViewById<ImageView>(R.id.Lundi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mardi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Mercredi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Jeudi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Vendredi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Samedi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Dimanche_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
        }
        if (currentDay == "mercredi") {
            view?.findViewById<ImageView>(R.id.Lundi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mardi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mercredi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Jeudi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Vendredi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Samedi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Dimanche_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
        }
        if (currentDay == "jeudi") {
            view?.findViewById<ImageView>(R.id.Lundi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mardi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mercredi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Jeudi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Vendredi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Samedi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Dimanche_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
        }
        if (currentDay == "vendredi") {
            view?.findViewById<ImageView>(R.id.Lundi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mardi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mercredi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Jeudi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Vendredi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Samedi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Dimanche_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
        }
        if (currentDay == "samedi") {
            view?.findViewById<ImageView>(R.id.Lundi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mardi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mercredi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Jeudi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Vendredi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Samedi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
            view?.findViewById<ImageView>(R.id.Dimanche_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
        }
        if (currentDay == "dimanche") {
            view?.findViewById<ImageView>(R.id.Lundi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mardi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Mercredi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Jeudi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Vendredi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Samedi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check))
            view?.findViewById<ImageView>(R.id.Dimanche_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))
        }

    }

    private fun reinitialisationSuivant() {

        view?.findViewById<TextView>(R.id.Lundi)?.setTypeface(null, Typeface.NORMAL)
        view?.findViewById<TextView>(R.id.Mardi)?.setTypeface(null, Typeface.NORMAL)
        view?.findViewById<TextView>(R.id.Mercredi)?.setTypeface(null, Typeface.NORMAL)
        view?.findViewById<TextView>(R.id.Jeudi)?.setTypeface(null, Typeface.NORMAL)
        view?.findViewById<TextView>(R.id.Vendredi)?.setTypeface(null, Typeface.NORMAL)
        view?.findViewById<TextView>(R.id.Samedi)?.setTypeface(null, Typeface.NORMAL)
        view?.findViewById<TextView>(R.id.Dimanche)?.setTypeface(null, Typeface.NORMAL)

        if (currentSemaine[2].midi != "None" && currentSemaine[2].soir == "None") {
            view?.findViewById<ImageView>(R.id.Lundi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_top))
        } else if (currentSemaine[2].midi == "None" && currentSemaine[2].soir != "None") {
            view?.findViewById<ImageView>(R.id.Lundi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_bottom))

        } else if (currentSemaine[2].midi != "None" && currentSemaine[2].soir != "None") {
            view?.findViewById<ImageView>(R.id.Lundi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_combined))
        } else view?.findViewById<ImageView>(R.id.Lundi_img)
            ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))

        if (currentSemaine[3].midi != "None" && currentSemaine[3].soir == "None") {
            view?.findViewById<ImageView>(R.id.Mardi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_top))
        } else if (currentSemaine[3].midi == "None" && currentSemaine[3].soir != "None") {
            view?.findViewById<ImageView>(R.id.Mardi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_bottom))

        } else if (currentSemaine[3].midi != "None" && currentSemaine[3].soir != "None") {
            view?.findViewById<ImageView>(R.id.Mardi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_combined))
        } else view?.findViewById<ImageView>(R.id.Mardi_img)
            ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))

        if (currentSemaine[4].midi != "None" && currentSemaine[4].soir == "None") {
            view?.findViewById<ImageView>(R.id.Mercredi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_top))
        } else if (currentSemaine[4].midi == "None" && currentSemaine[4].soir != "None") {
            view?.findViewById<ImageView>(R.id.Mercredi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_bottom))

        } else if (currentSemaine[4].midi != "None" && currentSemaine[4].soir != "None") {
            view?.findViewById<ImageView>(R.id.Mercredi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_combined))
        } else view?.findViewById<ImageView>(R.id.Mercredi_img)
            ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))


        if (currentSemaine[1].midi != "None" && currentSemaine[1].soir == "None") {
            view?.findViewById<ImageView>(R.id.Jeudi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_top))
        } else if (currentSemaine[1].midi == "None" && currentSemaine[1].soir != "None") {
            view?.findViewById<ImageView>(R.id.Jeudi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_bottom))

        } else if (currentSemaine[1].midi != "None" && currentSemaine[1].soir != "None") {
            view?.findViewById<ImageView>(R.id.Jeudi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_combined))
        } else view?.findViewById<ImageView>(R.id.Jeudi_img)
            ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))

        if (currentSemaine[6].midi != "None" && currentSemaine[6].soir == "None") {
            view?.findViewById<ImageView>(R.id.Vendredi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_top))
        } else if (currentSemaine[6].midi == "None" && currentSemaine[6].soir != "None") {
            view?.findViewById<ImageView>(R.id.Vendredi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_bottom))

        } else if (currentSemaine[6].midi != "None" && currentSemaine[6].soir != "None") {
            view?.findViewById<ImageView>(R.id.Vendredi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_combined))
        } else view?.findViewById<ImageView>(R.id.Vendredi_img)
            ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))


        if (currentSemaine[5].midi != "None" && currentSemaine[5].soir == "None") {
            view?.findViewById<ImageView>(R.id.Samedi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_top))
        } else if (currentSemaine[5].midi == "None" && currentSemaine[5].soir != "None") {
            view?.findViewById<ImageView>(R.id.Samedi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_bottom))

        } else if (currentSemaine[5].midi != "None" && currentSemaine[5].soir != "None") {
            view?.findViewById<ImageView>(R.id.Samedi_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_combined))
        } else view?.findViewById<ImageView>(R.id.Samedi_img)
            ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))

        if (currentSemaine[0].midi != "None" && currentSemaine[0].soir == "None") {
            view?.findViewById<ImageView>(R.id.Dimanche_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_top))
        } else if (currentSemaine[0].midi == "None" && currentSemaine[0].soir != "None") {
            view?.findViewById<ImageView>(R.id.Dimanche_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_bottom))

        } else if (currentSemaine[0].midi != "None" && currentSemaine[0].soir != "None") {
            view?.findViewById<ImageView>(R.id.Dimanche_img)
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_combined))
        } else view?.findViewById<ImageView>(R.id.Dimanche_img)
            ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_to_come))

    }

    private fun switch(day: String, button: TextView?, img: ImageView?) {

        val currentDays = currentSemaine.filter { s -> s.id_semainier == day }[0]
        button?.setTypeface(null, Typeface.BOLD)
        if (day in pastDay) {
            img?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check_check))
        } else {
            img?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_uncheck))
        }

        if (currentDays.midi != "None") {
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.visibility = View.VISIBLE
            var currentRepasMidi = repasList.filter { s -> s.id == currentDays.midi }[0]
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.setOnClickListener {
                context.loadFragment(
                    RecetteFragment(
                        context,
                        currentRepasMidi,
                        "None",
                        "None",
                        "None"
                    )
                )
            }
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.nomMidi)?.text = currentRepasMidi.name
            view?.findViewById<TextView>(R.id.descriptionMidi)?.text = currentRepasMidi.duree
            Glide.with(context).load(Uri.parse(currentRepasMidi.imageUri))
                .into(view?.findViewById<ImageView>(R.id.image_item2)!!)

            val collectionRecyclerView1 = view?.findViewById<RecyclerView>(R.id.tagListMidi)
            collectionRecyclerView1?.adapter =
                TagsAdapter(context, currentRepasMidi.tags, R.layout.item_tags_horizontal)
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.setOnClickListener {
                context.loadFragment(
                    RecetteFragment(
                        context,
                        currentRepasMidi,
                        "None",
                        "None",
                        "None"
                    )
                )
            }
        } else {
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.visibility = View.GONE
        }

        if (currentDays.soir != "None") {
            var currentRepasSoir = repasList.filter { s -> s.id == currentDays.soir }[0]
            view?.findViewById<ConstraintLayout>(R.id.Soir)?.visibility = View.VISIBLE
            view?.findViewById<ConstraintLayout>(R.id.Soir)?.setOnClickListener {
                context.loadFragment(
                    RecetteFragment(
                        context,
                        currentRepasSoir,
                        "None",
                        "None",
                        "None"
                    )
                )
            }
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.nomSoir)?.text = currentRepasSoir.name
            view?.findViewById<TextView>(R.id.descriptionSoir)?.text = currentRepasSoir.duree
            Glide.with(context).load(Uri.parse(currentRepasSoir.imageUri))
                .into(view?.findViewById<ImageView>(R.id.image_item3)!!)

            val collectionRecyclerView2 = view?.findViewById<RecyclerView>(R.id.tagListSoir)
            collectionRecyclerView2?.adapter =
                TagsAdapter(context, currentRepasSoir.tags, R.layout.item_tags_horizontal)
            view?.findViewById<ConstraintLayout>(R.id.Soir)?.setOnClickListener {
                context.loadFragment(
                    RecetteFragment(
                        context,
                        currentRepasSoir,
                        "None",
                        "None",
                        "None"
                    )
                )
            }
        } else {
            view?.findViewById<ConstraintLayout>(R.id.Soir)?.visibility = View.GONE
        }

        if (currentDays.apero != "None") {
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.visibility = View.VISIBLE
            var currentRepasApero = repasList.filter { s -> s.id == currentDays.apero }[0]
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.setOnClickListener {
                context.loadFragment(
                    RecetteFragment(
                        context,
                        currentRepasApero,
                        "None",
                        "None",
                        "None"
                    )
                )
            }
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.nomApero)?.text = currentRepasApero.name
            view?.findViewById<TextView>(R.id.descriptionApero)?.text = currentRepasApero.duree
            Glide.with(context).load(Uri.parse(currentRepasApero.imageUri))
                .into(view?.findViewById<ImageView>(R.id.image_item4)!!)
            val collectionRecyclerView3 = view?.findViewById<RecyclerView>(R.id.tagListApero)
            collectionRecyclerView3?.adapter =
                TagsAdapter(context, currentRepasApero.tags, R.layout.item_tags_horizontal)
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.setOnClickListener {
                context.loadFragment(
                    RecetteFragment(
                        context,
                        currentRepasApero,
                        "None",
                        "None",
                        "None"
                    )
                )
            }
        } else {
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.visibility = View.GONE
        }
        var repasAutresList = arrayListOf<RepasModel>()
        if (currentDays.autres.isNotEmpty()) {
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
            view?.findViewById<ConstraintLayout>(R.id.Autres)?.visibility = View.VISIBLE
            for (repas in currentDays.autres) {
                repasAutresList.add(repasList.filter { s -> s.id == repas }[0])
            }
            view?.findViewById<RecyclerView>(R.id.autresRepasRecylcer)?.adapter = AutresRapasSemainierAdapter(
                context,
                repasAutresList,
                currentDays.id_semainier,
                "courant",
                R.layout.item_semainier_autres_vertical
            )
            view?.findViewById<RecyclerView>(R.id.autresRepasRecylcer)?.layoutManager = LinearLayoutManager(context)
        } else {
            view?.findViewById<ConstraintLayout>(R.id.Autres)?.visibility = View.GONE
        }
        if (currentDays.midi == "None" && currentDays.soir == "None" && currentDays.apero == "None" && currentDays.autres.isEmpty())
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.VISIBLE
    }

    private fun switchSuivant(day: String, button: TextView?, img: ImageView?) {

        val currentDays = currentSemaine.filter { s -> s.id_semainier == day }[0]
        button?.setTypeface(null, Typeface.BOLD)
        if (currentDays.midi != "None" && currentDays.soir == "None") {
            img
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_top_select))
        } else if (currentDays.midi == "None" && currentDays.soir != "None") {
            img
                ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.ellipse_bottom_select))

        } else if (currentDays.midi != "None" && currentDays.soir != "None") {
            img
                ?.setImageDrawable(
                    this.getContext()?.getDrawable(R.drawable.ellipse_combine_select)
                )
        } else img
            ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_uncheck))


        if (currentDays.midi != "None") {
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.visibility = View.VISIBLE
            var currentRepasMidi = repasList.filter { s -> s.id == currentDays.midi }[0]
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.setOnClickListener {
                context.loadFragment(
                    RecetteFragment(
                        context,
                        currentRepasMidi,
                        "None",
                        "None",
                        "None"
                    )
                )
            }
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.nomMidi)?.text = currentRepasMidi.name
            view?.findViewById<TextView>(R.id.descriptionMidi)?.text = currentRepasMidi.duree
            Glide.with(context).load(Uri.parse(currentRepasMidi.imageUri))
                .into(view?.findViewById<ImageView>(R.id.image_item2)!!)

            val collectionRecyclerView1 = view?.findViewById<RecyclerView>(R.id.tagListMidi)
            collectionRecyclerView1?.adapter =
                TagsAdapter(context, currentRepasMidi.tags, R.layout.item_tags_horizontal)
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.setOnClickListener {
                context.loadFragment(
                    RecetteFragment(
                        context,
                        currentRepasMidi,
                        "None",
                        "None",
                        "None"
                    )
                )
            }
        } else {
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.visibility = View.GONE
        }

        if (currentDays.soir != "None") {
            var currentRepasSoir = repasList.filter { s -> s.id == currentDays.soir }[0]
            view?.findViewById<ConstraintLayout>(R.id.Soir)?.visibility = View.VISIBLE
            view?.findViewById<ConstraintLayout>(R.id.Soir)?.setOnClickListener {
                context.loadFragment(
                    RecetteFragment(
                        context,
                        currentRepasSoir,
                        "None",
                        "None",
                        "None"
                    )
                )
            }
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.nomSoir)?.text = currentRepasSoir.name
            view?.findViewById<TextView>(R.id.descriptionSoir)?.text = currentRepasSoir.duree
            Glide.with(context).load(Uri.parse(currentRepasSoir.imageUri))
                .into(view?.findViewById<ImageView>(R.id.image_item3)!!)

            val collectionRecyclerView2 = view?.findViewById<RecyclerView>(R.id.tagListSoir)
            collectionRecyclerView2?.adapter =
                TagsAdapter(context, currentRepasSoir.tags, R.layout.item_tags_horizontal)
            view?.findViewById<ConstraintLayout>(R.id.Soir)?.setOnClickListener {
                context.loadFragment(
                    RecetteFragment(
                        context,
                        currentRepasSoir,
                        "None",
                        "None",
                        "None"
                    )
                )
            }
        } else {
            view?.findViewById<ConstraintLayout>(R.id.Soir)?.visibility = View.GONE
        }

        if (currentDays.apero != "None") {
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.visibility = View.VISIBLE
            var currentRepasApero = repasList.filter { s -> s.id == currentDays.apero }[0]
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.setOnClickListener {
                context.loadFragment(
                    RecetteFragment(
                        context,
                        currentRepasApero,
                        "None",
                        "None",
                        "None"
                    )
                )
            }
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
            view?.findViewById<TextView>(R.id.nomApero)?.text = currentRepasApero.name
            view?.findViewById<TextView>(R.id.descriptionApero)?.text = currentRepasApero.duree
            Glide.with(context).load(Uri.parse(currentRepasApero.imageUri))
                .into(view?.findViewById<ImageView>(R.id.image_item4)!!)
            val collectionRecyclerView3 = view?.findViewById<RecyclerView>(R.id.tagListApero)
            collectionRecyclerView3?.adapter =
                TagsAdapter(context, currentRepasApero.tags, R.layout.item_tags_horizontal)
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.setOnClickListener {
                context.loadFragment(
                    RecetteFragment(
                        context,
                        currentRepasApero,
                        "None",
                        "None",
                        "None"
                    )
                )
            }
        } else {
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.visibility = View.GONE
        }
        var repasAutresList = arrayListOf<RepasModel>()
        if (currentDays.autres.isNotEmpty()) {
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.GONE
            view?.findViewById<ConstraintLayout>(R.id.Autres)?.visibility = View.VISIBLE
            for (repas in currentDays.autres) {
                repasAutresList.add(repasList.filter { s -> s.id == repas }[0])
            }
            view?.findViewById<RecyclerView>(R.id.autresRepasRecylcer)?.adapter = AutresRapasSemainierAdapter(
                context,
                repasAutresList,
                currentDays.id_semainier,
                "suivant",
                R.layout.item_semainier_autres_vertical
            )
            view?.findViewById<RecyclerView>(R.id.autresRepasRecylcer)?.layoutManager = LinearLayoutManager(context)
        } else {
            view?.findViewById<ConstraintLayout>(R.id.Autres)?.visibility = View.GONE
        }
        if (currentDays.midi == "None" && currentDays.soir == "None" && currentDays.apero == "None" && currentDays.autres.isEmpty()) {
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.VISIBLE
        }
    }


    private fun deleteIngredientCourse(ingredients: ArrayList<IngredientModel>) {
        var repo = CourseRepository()
        repo.deleteIngredientCourse(ingredients)
    }

}