package fr.juju.myapplication.fragments

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class EnablePersistence : Application(){
    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}