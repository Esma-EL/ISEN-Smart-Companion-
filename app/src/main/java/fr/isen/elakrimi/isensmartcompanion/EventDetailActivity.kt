package fr.isen.elakrimi.isensmartcompanion

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.NotificationsNone
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


class EventDetailActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val eventTitle = intent.getStringExtra("event_title") ?: "Événement"
        val eventDescription = intent.getStringExtra("event_description") ?: "Aucune description disponible"
        val eventDate = intent.getStringExtra("event_date") ?: "Date inconnue"
        val eventLocation = intent.getStringExtra("event_location") ?: "Lieu inconnu"
        val eventCategory = intent.getStringExtra("event_category") ?: "Catégorie inconnue"

        setContent {
            EventDetailScreen(eventTitle, eventDescription, eventDate, eventLocation, eventCategory)
        }
    }
}

@Composable
fun EventDetailScreen(
    title: String,
    description: String,
    date: String,
    location: String,
    category: String
) {
    val context = LocalContext.current


    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // 🔹 Titre de l'événement
            Text(
                text = title,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFB71C1C),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // 🔹 Carte de description
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = description,
                        fontSize = 18.sp,
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Black
                    )
                }
            }

            // 🔹 Informations de l'événement
            InfoRow(icon = Icons.Filled.Event, label = "Date", value = date)
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(icon = Icons.Filled.LocationOn, label = "Lieu", value = location)
            Spacer(modifier = Modifier.height(8.dp))
            InfoRow(icon = Icons.Filled.Tag, label = "Catégorie", value = category)


            Spacer(modifier = Modifier.height(16.dp))


            // 🔙 Bouton retour
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { (context as? Activity)?.finish() },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C)) // Rouge
            ) {
                Text("Retour", color = Color.White) // Texte blanc
            }

        }
    }
}

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            modifier = Modifier.size(24.dp),
            tint = Color(0xFFB71C1C)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Column {
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Gray
            )
            Text(
                text = value,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}



