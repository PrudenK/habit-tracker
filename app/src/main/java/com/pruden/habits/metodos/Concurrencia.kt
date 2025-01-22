package com.pruden.habits.metodos

fun lanzarHiloConJoin(hilo : Thread){
    hilo.start()
    hilo.join()
}