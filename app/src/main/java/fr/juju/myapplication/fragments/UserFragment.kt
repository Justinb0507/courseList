package fr.juju.myapplication.fragments

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.highlight.Highlight
import com.google.firebase.auth.FirebaseAuth
import fr.juju.myapplication.activity.LoginActivity
import fr.juju.myapplication.activity.MainActivity
import fr.juju.myapplication.R
import java.util.ArrayList
import com.github.mikephil.charting.listener.OnChartValueSelectedListener
import fr.juju.myapplication.repository.CategorieRepository.Singleton.categorieList
import fr.juju.myapplication.repository.RepasRepository.Singleton.repasList


class UserFragment (val context: MainActivity)  : Fragment(), OnChartValueSelectedListener  {

    private lateinit var pieChart: PieChart
    private lateinit var hashMapTag: HashMap<String,Int>
    private lateinit var tabColor: HashMap<String,String>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_user, container, false)
        pieChart = view.findViewById(R.id.chart)
        initPieChart()

        view.findViewById<TextView>(R.id.deconnexion).setOnClickListener{
            var builder = AlertDialog.Builder(context)
            builder.setTitle("Oulaaaaaa !")
            builder.setMessage("Tu veux vraiment te déconnecter ? :(")
            builder.setPositiveButton("Oui", DialogInterface.OnClickListener { dialog, id ->
                var preferences: SharedPreferences = context.getSharedPreferences("checkbox",
                    AppCompatActivity.MODE_PRIVATE
                )
                var editor: SharedPreferences.Editor = preferences.edit()
                editor.putString("remember", "false")
                editor.apply()
                FirebaseAuth.getInstance().signOut()
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent)
                context.finish()
                dialog.cancel()
            })
            builder.setNegativeButton("Non", DialogInterface.OnClickListener { dialog, id ->
                dialog.cancel()
            })
            var alert: AlertDialog = builder.create()
            alert.show()

        }
        var nbrRecette = view.findViewById<TextView>(R.id.textView18)
        nbrRecette.setText(repasList.size.toString())
        var nbrTags = view.findViewById<TextView>(R.id.textView21)

        hashMapTag = HashMap<String,Int>()
        for (repas in repasList){
            for (tag in repas.tags){
                if(!hashMapTag.contains(tag)){
                    hashMapTag.put(tag, 1)
                } else {
                    hashMapTag[tag] = hashMapTag[tag]!! +1
                }
            }
        }
        nbrTags.setText(hashMapTag.size.toString())
        var nbrCategorieIngredient = view.findViewById<TextView>(R.id.textView20)
        nbrCategorieIngredient.setText(categorieList.size.toString())
        setDataToPieChart()
        return view
    }


    private fun initPieChart() {
        pieChart.setUsePercentValues(true)
        pieChart.setTouchEnabled(true)
        pieChart.holeRadius = 70f
        pieChart.isDrawHoleEnabled = true
        pieChart.setHoleColor(R.color.background)
        pieChart.getLegend().setEnabled(false)
        pieChart.getDescription().setEnabled(false)
        pieChart.setDrawSliceText(false)
    }

    private fun setDataToPieChart() {
        pieChart.setUsePercentValues(true)
        var dataEntries = ArrayList<PieEntry>()
        var colors: ArrayList<Int> = ArrayList()
        tabColor = HashMap<String, String>()
        if(hashMapTag.containsKey("Plat")){
            dataEntries.add(PieEntry(hashMapTag["Plat"]!!.toFloat(), "Plat"))
            colors.add(Color.parseColor("#F26619"))
            tabColor.put("Plat", "#F26619")
        }
        if(hashMapTag.containsKey("Apéro")){
            dataEntries.add(PieEntry(hashMapTag["Apéro"]!!.toFloat(), "Apéro"))
            colors.add(Color.parseColor("#0594D0"))
            tabColor.put("Apéro","#0594D0")
        }
        if(hashMapTag.containsKey("Dessert")){
            dataEntries.add(PieEntry(hashMapTag["Dessert"]!!.toFloat(), "Dessert"))
            colors.add(Color.parseColor("#007198"))
            tabColor.put("Dessert","#007198")
        }
        if(hashMapTag.containsKey("Cocktail")){
            dataEntries.add(PieEntry(hashMapTag["Cocktail"]!!.toFloat(), "Cocktail"))
            colors.add(Color.parseColor("#003C57"))
            tabColor.put("Cocktail", "#003C57")
        }
        if(hashMapTag.containsKey("Soupe")){
            dataEntries.add(PieEntry(hashMapTag["Soupe"]!!.toFloat(), "Soupe"))
            colors.add(Color.parseColor("#F6B12D"))
            tabColor.put("Soupe", "#F6B12D")
        }
        if(hashMapTag.containsKey("Entrées")){
            dataEntries.add(PieEntry(hashMapTag["Entrées"]!!.toFloat(), "Entrées"))
            colors.add(Color.parseColor("#04BBFF"))
            tabColor.put("Entrées", "#04BBFF")
        }


        val dataSet = PieDataSet(dataEntries, "")
        val data = PieData(dataSet)

        // In Percentage
        data.setValueFormatter(PercentFormatter())
        dataSet.sliceSpace = 3f
        dataSet.colors = colors
        data.setDrawValues(false)
        pieChart.data = data
        /*data.setValueTextColor(R.color.background)
        data.setValueTextSize(15f)
        pieChart.setEntryLabelColor(R.color.bleu_canard)
        pieChart.setEntryLabelTextSize(12f)
         */

        pieChart.setOnChartValueSelectedListener(this)
        pieChart.animateY(1000, Easing.EaseInOutQuad)
        pieChart.invalidate()

    }

    override fun onValueSelected(e: Entry?, h: Highlight?) {
        view?.findViewById<TextView>(R.id.legendSelected)?.setText("Vous avez " + hashMapTag[(e as PieEntry).label] + " recette(s) avec le tag " + (e as PieEntry).label)
        view?.findViewById<View>(R.id.underline)?.setBackgroundColor(Color.parseColor(tabColor[(e as PieEntry).label]))
    }

    @SuppressLint("ResourceAsColor")
    override fun onNothingSelected() {
        view?.findViewById<TextView>(R.id.legendSelected)?.setText("Cliquez sur le graphique pour afficher la légende, et vous pouvez même le faire tourner :)")
        view?.findViewById<View>(R.id.underline)?.setBackgroundColor(R.color.background)
    }
}