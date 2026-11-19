package com.guevara.campusgps.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.guevara.campusgps.data.LugaresRepository
import com.guevara.campusgps.data.UbicacionRepository

class CampusViewModelFactory(
    private val ubicacionRepo: UbicacionRepository,
    private val lugaresRepo: LugaresRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(MapaViewModel::class.java)) {
            "ViewModel desconocido: ${modelClass.name}"
        }
        return MapaViewModel(ubicacionRepo, lugaresRepo) as T
    }
}
