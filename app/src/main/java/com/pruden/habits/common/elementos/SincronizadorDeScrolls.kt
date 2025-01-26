package com.pruden.habits.common.elementos

import androidx.recyclerview.widget.RecyclerView

class SincronizadorDeScrolls {
    private val recyclerViews = mutableListOf<RecyclerView>()
    private val scrollListeners = mutableMapOf<RecyclerView, RecyclerView.OnScrollListener>()
    private val flingListeners = mutableMapOf<RecyclerView, RecyclerView.OnFlingListener>()
    private var isSyncing = false

    fun addRecyclerView(recyclerView: RecyclerView) {
        // Si el RecyclerView ya está registrado, elimina sus listeners previos
        if (recyclerViews.contains(recyclerView)) {
            removeListeners(recyclerView)
        }

        // Registrar el RecyclerView
        recyclerViews.add(recyclerView)

        // Agregar el listener de scroll
        val scrollListener = object : RecyclerView.OnScrollListener() {
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
        }
        recyclerView.addOnScrollListener(scrollListener)
        scrollListeners[recyclerView] = scrollListener

        // Agregar el listener de fling
        val flingListener = object : RecyclerView.OnFlingListener() {
            override fun onFling(velocityX: Int, velocityY: Int): Boolean {
                if (!isSyncing) {
                    isSyncing = true
                    recyclerViews.forEach { syncedRecycler ->
                        if (syncedRecycler !== recyclerView) {
                            syncedRecycler.fling(velocityX, velocityY)
                        }
                    }
                    isSyncing = false
                }
                return false // Permitir el fling normal
            }
        }
        recyclerView.onFlingListener = flingListener
        flingListeners[recyclerView] = flingListener
    }

    fun removeRecyclerView(recyclerView: RecyclerView) {
        if (recyclerViews.contains(recyclerView)) {
            removeListeners(recyclerView)
            recyclerViews.remove(recyclerView)
        }
    }

    fun limpiarRecycler() {
        recyclerViews.forEach { removeListeners(it) }
        recyclerViews.clear()
        scrollListeners.clear()
        flingListeners.clear()
    }

    private fun removeListeners(recyclerView: RecyclerView) {
        // Quitar el listener de scroll
        scrollListeners[recyclerView]?.let {
            recyclerView.removeOnScrollListener(it)
        }
        scrollListeners.remove(recyclerView)

        // Quitar el listener de fling
        flingListeners[recyclerView]?.let {
            recyclerView.onFlingListener = null
        }
        flingListeners.remove(recyclerView)
    }
}
