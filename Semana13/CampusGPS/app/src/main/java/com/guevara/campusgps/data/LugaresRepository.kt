package com.guevara.campusgps.data

import com.guevara.campusgps.model.Lugar

interface LugaresRepository {
    fun obtenerLugares(): List<Lugar>
}
