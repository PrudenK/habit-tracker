package com.pruden.habits.common.elementos

import android.view.Gravity
import androidx.recyclerview.widget.RecyclerView
import com.github.rubensousa.gravitysnaphelper.GravitySnapHelper

class SincronizadorDeScrolls {
    private val recyclerViews = mutableListOf<RecyclerView>()
    private val scrollListeners = mutableMapOf<RecyclerView, RecyclerView.OnScrollListener>()
    private var isSyncing = false
    private val snapHelpers = mutableMapOf<RecyclerView, GravitySnapHelper>()

    fun addRecyclerView(recyclerView: RecyclerView) {
        if (recyclerViews.contains(recyclerView)) {
            removeListeners(recyclerView)
        }

        recyclerViews.add(recyclerView)

        // Usar GravitySnapHelper para alinear a la izquierda
        val snapHelper = GravitySnapHelper(Gravity.START)
        snapHelper.attachToRecyclerView(recyclerView)
        snapHelpers[recyclerView] = snapHelper

        // Listener para sincronizar scroll entre RecyclerViews
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
        snapHelpers.clear()
    }

    private fun removeListeners(recyclerView: RecyclerView) {
        scrollListeners[recyclerView]?.let {
            recyclerView.removeOnScrollListener(it)
        }
        scrollListeners.remove(recyclerView)

        snapHelpers[recyclerView]?.attachToRecyclerView(null)
        snapHelpers.remove(recyclerView)
    }
}
