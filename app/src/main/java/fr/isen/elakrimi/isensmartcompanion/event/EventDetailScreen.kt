package fr.isen.elakrimi.isensmartcompanion.event

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun EventDetailScreen(navController: NavController, event: Event) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text(text = event.title, fontSize = 24.sp, fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = event.description)
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = " ${event.date}", fontSize = 16.sp, color = androidx.compose.ui.graphics.Color.Gray)

        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navController.popBackStack() }) {
            Text("Retour")
        }

    }
}