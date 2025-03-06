package fr.isen.elakrimi.isensmartcompanion.Data

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InteractionViewModel(application: Application) : AndroidViewModel(application) {
    private val db = AppDatabase.getDatabase(application)
    private val interactionDao = db.interactionDao()

    private val _allInteractions = MutableStateFlow<List<Interaction>>(emptyList())
    val allInteractions = _allInteractions.asStateFlow()


    private val _currentInteraction = MutableStateFlow<Interaction?>(null)
    val currentInteraction = _currentInteraction.asStateFlow()

    init {

        viewModelScope.launch(Dispatchers.IO) {
            _allInteractions.value = interactionDao.getAllInteractions()
        }
    }


    fun insertInteraction(question: String, answer: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val interaction = Interaction(
                question = question,
                answer = answer,
                date = System.currentTimeMillis()
            )
            interactionDao.insertInteraction(interaction)  //


            _allInteractions.value = interactionDao.getAllInteractions()


            _currentInteraction.value = interaction
        }
    }


    fun deleteAllInteractions() {
        viewModelScope.launch(Dispatchers.IO) {
            interactionDao.deleteAllInteractions()


            _allInteractions.value = interactionDao.getAllInteractions()
        }
    }
}


