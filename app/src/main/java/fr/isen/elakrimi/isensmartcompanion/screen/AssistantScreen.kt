package fr.isen.elakrimi.isensmartcompanion.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.withContext
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.lifecycle.viewmodel.compose.viewModel
import fr.isen.elakrimi.isensmartcompanion.Data.InteractionViewModel

@Composable
fun AssistantScreen(viewModel: InteractionViewModel = viewModel()) {
    var question by remember { mutableStateOf("") }
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    val interactionHistory by viewModel.allInteractions.collectAsState(initial = emptyList())

    val generativeModel = GenerativeModel("gemini-1.5-flash", "AIzaSyBTyoEZSxYr1kvqfhoazLiz3LB9YvppZrQ")

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    Text("ISEN", fontSize = 48.sp, fontWeight = FontWeight.Bold, color = Color(0xFFB71C1C))
                }
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Start) {
                    Text("Smart Companion", fontSize = 18.sp, color = Color.Black)
                }
            }

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(interactionHistory) { interaction ->
                    Column(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFF0F0F0), CircleShape)
                                .padding(8.dp)
                        ) {
                            Text("❓ ${interaction.question}", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                        Box(
                            modifier = Modifier
                                .background(Color.White, CircleShape)
                                .padding(8.dp)
                                .padding(top = 8.dp)
                        ) {
                            Text("💬 ${interaction.answer}", fontSize = 16.sp)
                        }
                    }
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5), shape = MaterialTheme.shapes.medium)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = question,
                onValueChange = { question = it },
                placeholder = { Text("Posez votre question...") },
                textStyle = TextStyle(fontSize = 16.sp),
                singleLine = true,
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            )

            Button(
                onClick = {
                    if (question.isNotEmpty()) {
                        coroutineScope.launch(Dispatchers.IO) {
                            val response = getAIResponse(generativeModel, question)
                            withContext(Dispatchers.Main) {
                                viewModel.insertInteraction(question, response)
                                question = ""
                            }
                        }
                    } else {
                        Toast.makeText(context, "Veuillez entrer une question", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.size(48.dp).clip(CircleShape),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C))
            ) {
                Icon(Icons.Filled.ArrowForward, contentDescription = "Envoyer", tint = Color.White)
            }
        }
    }
}

private suspend fun getAIResponse(generativeModel: GenerativeModel, input: String): String {
    return try {
        val response = generativeModel.generateContent(input)
        response.text ?: "Aucune réponse obtenue"
    } catch (e: Exception) {
        "Erreur: ${e.message}"
    }
}
