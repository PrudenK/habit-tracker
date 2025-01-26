package com.pruden.habits.common.metodos

fun lanzarHiloConJoin(hilo : Thread){
    hilo.start()
    hilo.join()
}