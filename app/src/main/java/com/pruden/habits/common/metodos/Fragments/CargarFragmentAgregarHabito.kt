package com.pruden.habits.common.metodos.Fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.pruden.habits.R

fun cargarFragment(activity: FragmentActivity, fragment: Fragment, nombreHabito: String? = null){
    val fragmentManager = activity.supportFragmentManager
    val fragmentTransaction = fragmentManager.beginTransaction()

    nombreHabito?.let {
        fragment.arguments = Bundle().apply { putString("nombre", it) }
    }

    fragmentTransaction.add(R.id.main, fragment)

    fragmentTransaction.addToBackStack(null)
    fragmentTransaction.commit()
}