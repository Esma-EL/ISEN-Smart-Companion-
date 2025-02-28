package fr.isen.elakrimi.isensmartcompanion.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.elakrimi.isensmartcompanion.Data.*
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight

@Composable
fun HistoryScreen(viewModel: InteractionViewModel = viewModel()) {
    val coroutineScope = rememberCoroutineScope()
    val interactionHistory by viewModel.allInteractions.collectAsState(initial = emptyList())

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(text = "History", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))

        // Liste des interactions
        LazyColumn {
            items(interactionHistory) { interaction ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(" ${formatDate(interaction.date)}", fontSize = 14.sp, color = Color.Gray)
                        Text(" ${interaction.question}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        Text(" ${interaction.answer}", fontSize = 16.sp)



                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bouton pour supprimer tout l'historique
        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.deleteAllInteractions() // Supprime tout l'historique
                }
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Effacer l'historique", color = Color.White)
        }
    }
}

// Fonction pour convertir le timestamp en date lisible
fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
    return sdf.format(Date(timestamp))
}
