package com.pruden.habits.common.metodos.Fragments

import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import com.pruden.habits.R
import com.pruden.habits.modules.configuracionesModule.ConfiguracionesFragment

fun cargarFragmentConfiguraciones(activity: FragmentActivity, argumentos : Bundle? = null){
    val fragment = ConfiguracionesFragment()

    val fragmentManager = activity.supportFragmentManager
    val fragmentTransaction = fragmentManager.beginTransaction()


    fragmentTransaction.add(R.id.main, fragment)


    fragmentTransaction.addToBackStack(null)
    fragmentTransaction.commit()

}