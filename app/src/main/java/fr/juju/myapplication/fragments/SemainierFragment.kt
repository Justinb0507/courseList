package fr.juju.myapplication.fragments

import android.annotation.SuppressLint
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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import fr.juju.myapplication.*
import fr.juju.myapplication.IngredientRepository.Singleton.ingredientList
import fr.juju.myapplication.RepasRepository.Singleton.repasList
import fr.juju.myapplication.SemainierRepository.Singleton.semainierList
import fr.juju.myapplication.SemainierSuivantRepository.Singleton.semainierSuivantList
import fr.juju.myapplication.CourseRepository.Singleton.courseList
import fr.juju.myapplication.adapter.TagsAdapter
import java.lang.Math.ceil
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


        if (currentSemaineInput == "suivant") {
            currentSemaine = semainierSuivantList
            suivant = true
            currentDays = currentSemaine.filter { s -> s.id_semainier == selectedDay }[0]
            view.findViewById<ImageView>(R.id.eye).visibility = View.GONE
            view.findViewById<ImageView>(R.id.calendar).visibility = View.VISIBLE
            view.findViewById<Switch>(R.id.toggleButton).isChecked = true
            reinitialisationSuivant()


            if (selectedDay == "lundi") {
                view.findViewById<TextView>(R.id.Lundi)?.setTypeface(null, Typeface.BOLD)
            }
            else if (selectedDay == "mardi") {
                view.findViewById<TextView>(R.id.Mardi)?.setTypeface(null, Typeface.BOLD)
            }
            else if (selectedDay == "mercredi") {
                view.findViewById<TextView>(R.id.Mercredi)?.setTypeface(null, Typeface.BOLD)
            }
            else if (selectedDay == "jeudi") {
                view.findViewById<TextView>(R.id.Jeudi)?.setTypeface(null, Typeface.BOLD)

            }
            else if (selectedDay == "vendredi") {
                view.findViewById<TextView>(R.id.Vendredi)?.setTypeface(null, Typeface.BOLD)
            }
            else if (selectedDay == "samedi") {
                view.findViewById<TextView>(R.id.Samedi)?.setTypeface(null, Typeface.BOLD)
            }
            else if (selectedDay == "dimanche") {
                view.findViewById<TextView>(R.id.Dimanche)?.setTypeface(null, Typeface.BOLD)
            }

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
        else {
            currentSemaine = semainierList
            suivant = false
            currentDays = currentSemaine.filter { s -> s.id_semainier == selectedDay }[0]
            view.findViewById<Switch>(R.id.toggleButton).isChecked = false
            view.findViewById<ImageView>(R.id.eye).visibility = View.VISIBLE
            view.findViewById<ImageView>(R.id.calendar).visibility = View.GONE

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
            }
            else if (selectedDay == "mardi") {

                view.findViewById<TextView>(R.id.Mardi)?.setTypeface(null, Typeface.BOLD)
                if (selectedDay in pastDay) {
                    view.findViewById<ImageView>(R.id.Mardi_img)
                        ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check_check))
                } else {
                    view.findViewById<ImageView>(R.id.Mardi_img)
                        ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_uncheck))
                }
            }
            else if (selectedDay == "mercredi") {
                view.findViewById<TextView>(R.id.Mercredi)?.setTypeface(null, Typeface.BOLD)
                if (selectedDay in pastDay) {
                    view.findViewById<ImageView>(R.id.Mercredi_img)
                        ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check_check))
                } else {
                    view.findViewById<ImageView>(R.id.Mercredi_img)
                        ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_uncheck))
                }
            }
            else if (selectedDay == "jeudi") {
                view.findViewById<TextView>(R.id.Jeudi)?.setTypeface(null, Typeface.BOLD)
                if (selectedDay in pastDay) {
                    view.findViewById<ImageView>(R.id.Jeudi_img)
                        ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check_check))
                } else {
                    view.findViewById<ImageView>(R.id.Jeudi_img)
                        ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_uncheck))
                }
            }
            else if (selectedDay == "vendredi") {
                view.findViewById<TextView>(R.id.Vendredi)?.setTypeface(null, Typeface.BOLD)
                if (selectedDay in pastDay) {
                    view.findViewById<ImageView>(R.id.Vendredi_img)
                        ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check_check))
                } else {
                    view.findViewById<ImageView>(R.id.Vendredi_img)
                        ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_uncheck))
                }
            }
            else if (selectedDay == "samedi") {
                view.findViewById<TextView>(R.id.Samedi)?.setTypeface(null, Typeface.BOLD)
                if (selectedDay in pastDay) {
                    view.findViewById<ImageView>(R.id.Samedi_img)
                        ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.check_check))
                } else {
                    view.findViewById<ImageView>(R.id.Samedi_img)
                        ?.setImageDrawable(this.getContext()?.getDrawable(R.drawable.radio_uncheck))
                }
            }
            else if (selectedDay == "dimanche") {
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
            }
            else {
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

        if (currentDays.midi == "None" && currentDays.soir == "None" && currentDays.apero == "None") {
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.VISIBLE
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

        val translate = AnimationUtils.loadAnimation(context, R.anim.translate_anim)
        view.findViewById<ConstraintLayout>(R.id.Midi).startAnimation(translate)
        view.findViewById<ConstraintLayout>(R.id.Soir).startAnimation(translate)
        view.findViewById<ConstraintLayout>(R.id.Apero).startAnimation(translate)
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

        return view
    }

    @SuppressLint("CutPasteId")
    private fun enableEdit(view: View?) {
        if (!enable) {
            enable = true
            view?.findViewById<ConstraintLayout>(R.id.affect_repas_midi)?.visibility = View.VISIBLE
            view?.findViewById<ConstraintLayout>(R.id.affect_repas_apero)?.visibility = View.VISIBLE
            view?.findViewById<ConstraintLayout>(R.id.affect_repas_soir)?.visibility = View.VISIBLE
            view?.findViewById<ScrollView>(R.id.scrollView)?.alpha = 0.25F
            view?.findViewById<ConstraintLayout>(R.id.Midi)?.isEnabled = false
            view?.findViewById<ConstraintLayout>(R.id.Soir)?.isEnabled = false
            view?.findViewById<ConstraintLayout>(R.id.Apero)?.isEnabled = false
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
            view?.findViewById<ImageView>(R.id.affect_repas)?.animate()?.rotation(0F)?.duration =
                250
            view?.findViewById<ConstraintLayout>(R.id.affect_repas_midi)
                ?.startAnimation(translateAntiAnim)
            view?.findViewById<ConstraintLayout>(R.id.affect_repas_apero)
                ?.startAnimation(translateAntiAnim)
            view?.findViewById<ConstraintLayout>(R.id.affect_repas_soir)
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

        if (currentDays.midi == "None" && currentDays.soir == "None" && currentDays.apero == "None")
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.VISIBLE
    }

    private fun switchSuivant(day: String, button: TextView?, img: ImageView?) {

        val currentDays = currentSemaine.filter { s -> s.id_semainier == day }[0]
        button?.setTypeface(null, Typeface.BOLD)

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




        if (currentDays.midi == "None" && currentDays.soir == "None" && currentDays.apero == "None")
            view?.findViewById<ConstraintLayout>(R.id.NoRepas)?.visibility = View.VISIBLE
    }


    private fun deleteIngredientCourse(ingredients: ArrayList<IngredientModel>) {
        var repo = CourseRepository()
        repo.deleteIngredientCourse(ingredients)
    }

}