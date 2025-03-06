package fr.isen.elakrimi.isensmartcompanion

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Event
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import fr.isen.elakrimi.isensmartcompanion.screen.AssistantScreen
import fr.isen.elakrimi.isensmartcompanion.event.EventsScreen
import fr.isen.elakrimi.isensmartcompanion.history.HistoryScreen
import fr.isen.elakrimi.isensmartcompanion.calendar.CalendarScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            Scaffold(
                bottomBar = { BottomNavigationBar(navController) }
            ) { innerPadding ->
                NavigationGraph(navController, Modifier.padding(innerPadding))
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        NavigationItem("home", "Home", Icons.Filled.Home),
        NavigationItem("events", "Events", Icons.Filled.Event),
        NavigationItem("history", "History", Icons.Filled.History),
        NavigationItem("calendar", "Calendar", Icons.Filled.CalendarMonth) // Correction ici
    )

    NavigationBar {
        items.forEach { item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) },
                selected = false,
                onClick = { navController.navigate(item.route) }
            )
        }
    }
}

data class NavigationItem(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector)

@Composable
fun NavigationGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController, startDestination = "home", modifier = modifier) {
        composable("home") { AssistantScreen() }
        composable("events") { EventsScreen(navController) }
        composable("history") { HistoryScreen() }
        composable("calendar") { CalendarScreen() }
    }
}
