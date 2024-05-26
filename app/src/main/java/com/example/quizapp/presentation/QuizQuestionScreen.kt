@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.example.quizapp.presentation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.quizapp.R
import kotlinx.coroutines.delay

data class QuizQuestion(
    val question: String,
    val imageResId: Int,
    val options: List<String>,
    val correctAnswer: String
)

val quizQuestions = listOf(
    QuizQuestion(
        question = "What is the capital of France?",
        imageResId = R.drawable.ic_paris,
        options = listOf("Berlin", "Madrid", "Paris", "Lisbon"),
        correctAnswer = "Paris"
    ),
    QuizQuestion(
        question = "Which planet is known as the Red Planet?",
        imageResId = R.drawable.ic_mars,
        options = listOf("Earth", "Mars", "Jupiter", "Saturn"),
        correctAnswer = "Mars"
    ),
    QuizQuestion(
        question = "Machu Picchu is located in ________.",
        imageResId = R.drawable.ic_machupichhu,
        options = listOf("Peru", "Rome", "Barcelona", "Dakar"),
        correctAnswer = "Peru"
    ),
    QuizQuestion(
        question = "Which one is the winged horse in Greek mythology?",
        imageResId = R.drawable.ic_moon,
        options = listOf("Chrysaor", "Perseus", "Minotaur", "Pegasus"),
        correctAnswer = "Pegasus"
    ),
    QuizQuestion(
        question = "_______________ served as the 10th president of the United States.",
        imageResId = R.drawable.ic_com,
        options = listOf(
            "John Tyler",
            "William Henry Harrison",
            "James a Garfield",
            "Calvin  Collidge"
        ),
        correctAnswer = "John Tyler"
    )
)

@Composable
fun QuizScreen(totalQuizTime: Long) {
    var currentQuestionIndex by remember { mutableStateOf(0) }
    var selectedAnswer by remember { mutableStateOf<String?>(null) }
    var isAnswerCorrect by remember { mutableStateOf<Boolean?>(null) }
    var score by remember { mutableStateOf(0) }
    var questionTime by remember { mutableStateOf(15L) }  // 15 seconds per question
    var totalTimeRemaining by remember { mutableStateOf(totalQuizTime) }

    val currentQuestion = quizQuestions[currentQuestionIndex]

    LaunchedEffect(currentQuestionIndex) {
        questionTime = 15L
        while (questionTime > 0 && totalTimeRemaining > 0) {
            delay(1000L)
            questionTime--
            totalTimeRemaining--
        }
        if (questionTime == 0L) {
            if (currentQuestionIndex < quizQuestions.size - 1) {
                currentQuestionIndex++
                selectedAnswer = null
                isAnswerCorrect = null
            } else {
                // Handle quiz completion
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 13.dp, end = 20.dp, top = 15.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(
                                text = "Question ${currentQuestionIndex + 1} of ${quizQuestions.size}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Red
                            )
                            Text(text = "Score: $score",
                                style = MaterialTheme.typography.bodyMedium)
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(10.dp)
        ) {
            Text(
                text = currentQuestion.question,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(start = 15.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 15.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Time Remaining: $questionTime seconds",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Red,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))

            Image(
                painter = painterResource(id = currentQuestion.imageResId),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            currentQuestion.options.forEach { option ->
                val buttonColor = when {
                    selectedAnswer == option -> {
                        if (isAnswerCorrect == true) Color.Green else Color.Red
                    }
                    else -> Color.Blue
                }
                Button(
                    onClick = {
                        if (questionTime > 0) {
                            selectedAnswer = option
                            isAnswerCorrect = option == currentQuestion.correctAnswer
                            if (isAnswerCorrect == true) {
                                score++
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(buttonColor),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(text = option)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (selectedAnswer != null && questionTime > 0) {
                        if (currentQuestionIndex < quizQuestions.size - 1) {
                            currentQuestionIndex++
                            selectedAnswer = null
                            isAnswerCorrect = null
                        } else {
                            // Handle quiz completion
                        }
                    }
                },
                ) {
                Text(text = "Next")
            }

            Spacer(modifier = Modifier.height(16.dp))

            LinearProgressIndicator(
                progress = (currentQuestionIndex + 1) / quizQuestions.size.toFloat(),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
