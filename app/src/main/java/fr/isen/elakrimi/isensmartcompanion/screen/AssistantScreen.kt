package fr.isen.elakrimi.isensmartcompanion.screen

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
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
    var question by remember { mutableStateOf("") } // Question entrée par l'utilisateur
    var aiResponse by remember { mutableStateOf("") } // Réponse de l'IA
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    // Récupération de l'historique des interactions
    val interactionHistory by viewModel.allInteractions.collectAsState(initial = emptyList())

    // Modèle Gemini AI
    val generativeModel = GenerativeModel("gemini-1.5-flash", "AIzaSyBTyoEZSxYr1kvqfhoazLiz3LB9YvppZrQ")

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Titre ISEN Smart Companion
            Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start // Aligne à gauche
                ) {
                    Text(
                        text = "ISEN",
                        fontSize = 48.sp,  // Augmenter la taille de "ISEN"
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFB71C1C) // 🔴 Rouge ISEN
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start // Aligne à gauche
                ) {
                    Text(
                        text = "Smart Companion",
                        fontSize = 18.sp,
                        color = Color.Black // Noir pour "Smart Companion"
                    )
                }
            }


            // Affichage de la réponse actuelle (question + réponse)
            if (aiResponse.isNotEmpty()) {
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color(0xFFF0F0F0), CircleShape) // Gris pâle
                            .padding(8.dp)
                            .weight(1f)
                    ) {
                        Text("❓ $question", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                }
                Row(
                    modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.White, CircleShape) // Fond blanc
                            .padding(8.dp)
                            .weight(1f)
                    ) {
                        Text(" $aiResponse", fontSize = 16.sp)
                    }

                }
            }
        }

        // Champ de texte + bouton envoyer en bas
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFF5F5F5), shape = MaterialTheme.shapes.medium)
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .align(Alignment.BottomCenter),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Champ de saisie
            TextField(
                value = question,
                onValueChange = { question = it },
                placeholder = { Text("Posez votre question...") },
                textStyle = TextStyle(fontSize = 16.sp),
                singleLine = true,
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            )

            // Bouton envoyer
            Button(
                onClick = {
                    if (question.isNotEmpty()) {
                        // Envoyer la question à Gemini AI
                        coroutineScope.launch(Dispatchers.IO) {
                            aiResponse = getAIResponse(generativeModel, question)

                            // Ajouter l'interaction dans la base de données
                            viewModel.insertInteraction(question, aiResponse)
                        }
                    } else {
                        Toast.makeText(context, "Veuillez entrer une question", Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFB71C1C))
            ) {
                Icon(
                    imageVector = Icons.Filled.Send,
                    contentDescription = "Envoyer",
                    tint = Color.White
                )
            }
        }
    }
}

// Fonction pour interroger Gemini AI
private suspend fun getAIResponse(generativeModel: GenerativeModel, input: String): String {
    return try {
        // Appel à Gemini AI pour obtenir la réponse
        val response = generativeModel.generateContent(input)
        response.text ?: "Aucune réponse obtenue"
    } catch (e: Exception) {
        "Erreur: ${e.message}"
    }
}