package com.pruden.habits.common.metodos.Fragments

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.pruden.habits.R

fun cargarFragment(activity: FragmentActivity, fragment: Fragment){
    val fragmentManager = activity.supportFragmentManager
    val fragmentTransaction = fragmentManager.beginTransaction()

    fragmentTransaction.add(R.id.main, fragment)

    fragmentTransaction.addToBackStack(null)
    fragmentTransaction.commit()
}