package fr.isen.elakrimi.isensmartcompanion.calendar

import android.content.Context
import android.os.Bundle
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.clickable
import java.util.*

@Composable
fun CalendarScreen() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("CalendarEvents", Context.MODE_PRIVATE)
    val currentDate = remember { Calendar.getInstance() }
    val selectedDay = remember { mutableStateOf<Int?>(null) }
    val eventText = remember { mutableStateOf("") }
    var currentMonth by remember { mutableStateOf(currentDate.get(Calendar.MONTH)) }
    var currentYear by remember { mutableStateOf(currentDate.get(Calendar.YEAR)) }

    val daysInMonth = remember(currentMonth, currentYear) {
        getDaysInMonth(currentDate.apply {
            set(Calendar.MONTH, currentMonth)
            set(Calendar.YEAR, currentYear)
        })
    }

    Column(modifier = Modifier.padding(16.dp)) {
        MonthSelector(currentMonth, currentYear, onMonthChange = { month, year ->
            currentMonth = month
            currentYear = year
        })
        Spacer(modifier = Modifier.height(16.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(7),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(7) { index ->
                Text(
                    text = getDayOfWeek(index),
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.padding(8.dp),
                    textAlign = TextAlign.Center
                )
            }

            daysInMonth.forEach { day ->
                item {
                    DayCard(day, selectedDay, eventText, sharedPreferences)
                }
            }
        }

        selectedDay.value?.let { day ->
            Spacer(modifier = Modifier.height(16.dp))
            Text("Event for Day $day", style = MaterialTheme.typography.bodyLarge)
            TextField(
                value = eventText.value,
                onValueChange = {
                    eventText.value = it
                    sharedPreferences.edit().putString("event_$day", it).apply()
                },
                label = { Text("Event") },
                placeholder = { Text("Add an event") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
fun MonthSelector(currentMonth: Int, currentYear: Int, onMonthChange: (Int, Int) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        IconButton(onClick = {
            val newMonth = if (currentMonth == 0) 11 else currentMonth - 1
            val newYear = if (currentMonth == 0) currentYear - 1 else currentYear
            onMonthChange(newMonth, newYear)
        }) {
            Text("<")
        }

        Text(
            text = "${Calendar.getInstance().apply { set(Calendar.MONTH, currentMonth); set(Calendar.YEAR, currentYear) }.getDisplayName(Calendar.MONTH, Calendar.LONG, Locale.getDefault())} $currentYear",
            style = MaterialTheme.typography.titleLarge
        )

        IconButton(onClick = {
            val newMonth = if (currentMonth == 11) 0 else currentMonth + 1
            val newYear = if (currentMonth == 11) currentYear + 1 else currentYear
            onMonthChange(newMonth, newYear)
        }) {
            Text(">")
        }
    }
}

@Composable
fun DayCard(day: Int, selectedDay: MutableState<Int?>, eventText: MutableState<String>, sharedPreferences: android.content.SharedPreferences) {
    val isSelected = selectedDay.value == day

    Card(
        modifier = Modifier
            .padding(8.dp)
            .clickable {
                selectedDay.value = day
                eventText.value = sharedPreferences.getString("event_$day", "") ?: ""
            },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
        )
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = day.toString(),
                style = MaterialTheme.typography.bodyLarge,
                color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

fun getDaysInMonth(calendar: Calendar): List<Int> {
    val daysInMonth = mutableListOf<Int>()
    val maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

    for (day in 1..maxDays) {
        daysInMonth.add(day)
    }
    return daysInMonth
}

fun getDayOfWeek(index: Int): String {
    return when (index) {
        0 -> "Sun"
        1 -> "Mon"
        2 -> "Tue"
        3 -> "Wed"
        4 -> "Thu"
        5 -> "Fri"
        6 -> "Sat"
        else -> ""
    }
}

@Preview(showBackground = true)
@Composable
fun CalendarScreenPreview() {
    CalendarScreen()
}
