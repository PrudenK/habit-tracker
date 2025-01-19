package com.pruden.habits.elementos

import androidx.recyclerview.widget.RecyclerView

class SincronizadorDeScrolls {
    private val recyclerViews = mutableListOf<RecyclerView>()
    private var isSyncing = false

    fun addRecyclerView(recyclerView: RecyclerView) {
        recyclerViews.add(recyclerView)

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(rv: RecyclerView, dx: Int, dy: Int) {
                if (!isSyncing) {
                    isSyncing = true
                    recyclerViews.forEach { syncedRecycler ->
                        if (syncedRecycler !== rv) {
                            syncedRecycler.scrollBy(dx, dy)
                        }
                    }
                    isSyncing = false
                }
            }
        })

        recyclerView.setOnFlingListener(object : RecyclerView.OnFlingListener() {
            override fun onFling(velocityX: Int, velocityY: Int): Boolean {
                if (!isSyncing) { // Solo ejecuta si no está sincronizando
                    isSyncing = true
                    recyclerViews.forEach { syncedRecycler ->
                        if (syncedRecycler !== recyclerView) {
                            syncedRecycler.fling(velocityX, velocityY)
                        }
                    }
                    isSyncing = false
                }
                return false // Retorna false para permitir el fling normal
            }
        })
    }
}