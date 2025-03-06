package fr.isen.elakrimi.isensmartcompanion.event

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.navigation.NavController
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.SharedPreferences
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import androidx.core.app.ActivityCompat
import android.content.pm.PackageManager
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.NotificationsNone
import fr.isen.elakrimi.isensmartcompanion.EventDetailActivity
import kotlinx.coroutines.delay
import android.os.Handler
import android.os.Looper


@Parcelize
data class Event(
    val id: String,
    val title: String,
    val description: String,
    val date: String,
    val location: String,
    val category: String
) : Parcelable

@Composable
fun EventsScreen(navController: NavController) {
    var eventList by remember { mutableStateOf<List<Event>?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Récupération des événements via l'API
    LaunchedEffect(Unit) {
        RetrofitInstance.api.getEvents().enqueue(object : Callback<List<Event>> {
            override fun onResponse(call: Call<List<Event>>, response: Response<List<Event>>) {
                if (response.isSuccessful) {
                    eventList = response.body() ?: emptyList()
                    isLoading = false
                } else {
                    errorMessage = "Échec du chargement des événements"
                    isLoading = false
                }
            }

            override fun onFailure(call: Call<List<Event>>, t: Throwable) {
                errorMessage = "Erreur : ${t.message}"
                isLoading = false
                Log.e("EventsScreen", "Échec de l'appel API : ${t.message}")
            }
        })
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(
            text = " ISEN Events",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFFB71C1C)
        )
        Spacer(modifier = Modifier.height(16.dp))

        when {
            isLoading -> CircularProgressIndicator()
            errorMessage != null -> Text(text = errorMessage!!, color = Color.Red)
            eventList.isNullOrEmpty() -> Text(text = "Aucun événement trouvé.", fontSize = 18.sp)
            else -> LazyColumn {
                items(eventList!!) { event ->
                    EventItem(event, navController)
                }
            }
        }
    }
}

@Composable
fun EventItem(event: Event, navController: NavController) {
    val context = LocalContext.current
    val sharedPreferences: SharedPreferences = context.getSharedPreferences("EventPrefs", Context.MODE_PRIVATE)
    var isNotified by remember { mutableStateOf(sharedPreferences.getBoolean(event.id, false)) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                val intent = Intent(context, EventDetailActivity::class.java).apply {
                    putExtra("event_id", event.id)
                    putExtra("event_title", event.title)
                    putExtra("event_description", event.description)
                    putExtra("event_date", event.date)
                    putExtra("event_location", event.location)
                    putExtra("event_category", event.category)
                }
                context.startActivity(intent)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier
            .background(Color(0xFFB71C1C))
            .padding(16.dp)
        ) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Column(modifier = Modifier.weight(1f)) {

                    Text(
                        text = event.title,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = event.date,
                        fontSize = 14.sp,
                        color = Color.White
                    )
                }

                IconButton(
                    onClick = {
                        isNotified = !isNotified
                        sharedPreferences.edit().putBoolean(event.id, isNotified).apply()

                        if (isNotified) {
                            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU) {
                                    val activity = context as? android.app.Activity
                                    activity?.let {
                                        ActivityCompat.requestPermissions(it, arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 1)
                                    }
                                }
                            } else {
                                sendNotification(context, event)
                            }
                        }
                    },
                    modifier = Modifier
                        .background(Color(0xFFB71C1C))
                        .size(48.dp)
                ) {

                    Icon(
                        imageVector = if (isNotified) Icons.Filled.Notifications else Icons.Filled.NotificationsNone,
                        contentDescription = "Notification Bell",
                        tint = Color.White
                    )
                }
            }
        }
    }
}

fun createNotificationChannel(context: Context) {
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
        val channel = NotificationChannel(
            "default_channel",
            "Event Notifications",
            NotificationManager.IMPORTANCE_DEFAULT
        ).apply {
            description = "Canal pour les notifications d'événements ISEN"
        }

        val notificationManager: NotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}

fun sendNotification(context: Context, event: Event) {
    createNotificationChannel(context)
    try {
        val notification = NotificationCompat.Builder(context, "default_channel")
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("Rappel: ${event.title}")
            .setContentText("N'oubliez pas cet événement le ${event.date} !")
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setTimeoutAfter(10000)
            .build()

        val notificationManager: NotificationManagerCompat = NotificationManagerCompat.from(context)
        notificationManager.notify(event.id.hashCode(), notification)
    } catch (e: SecurityException) {
        e.printStackTrace()
    }
}
