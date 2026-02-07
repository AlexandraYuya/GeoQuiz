package com.bignerdranch.android.geoquiz

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.geoquiz.databinding.MainActivityBinding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.Composable

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding

    private val questionBank = listOf(
        Question(R.string.question_australia, true, false),
        Question(R.string.question_oceans, true, false),
        Question(R.string.question_mideast, false, false),
        Question(R.string.question_africa, false, false),
        Question(R.string.question_americas, true, false),
        Question(R.string.question_asia, true, false)
    )

    private var currentIndex = 0
    private var totalScore = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.trueButton?.setOnClickListener { view: View ->
            checkAnswer(true)
        }

        binding.falseButton?.setOnClickListener { view: View ->
            checkAnswer(false)
        }

        binding.prevButton?.setOnClickListener { view: View ->
            if (currentIndex != 0) {
                currentIndex = (currentIndex - 1) % questionBank.size
            } else {
                currentIndex = 0
            }
            updateQuestion()
        }

        binding.nextButton?.setOnClickListener { view: View ->
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        binding.questionTextView?.setOnClickListener { view: View ->
            currentIndex = (currentIndex + 1) % questionBank.size
            updateQuestion()
        }

        updateQuestion()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = questionBank[currentIndex].textResId
        binding.questionTextView?.setText(questionTextResId)

        val answered = questionBank[currentIndex].answerCheck
        binding.trueButton?.isEnabled = !answered
        binding.falseButton?.isEnabled = !answered
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = questionBank[currentIndex].answer

        if(!questionBank[currentIndex].answerCheck) {
            if(userAnswer === correctAnswer) {
                totalScore += 1
            }
            questionBank[currentIndex].answerCheck = true
        }

        val messageResId = if (userAnswer === correctAnswer) {
            R.string.correct_toast
        } else {
            R.string.incorrect_toast
        }
        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()

        val allAnswered = questionBank.all { it.answerCheck }
        if (allAnswered) {
            val scoreMessage = "Score: $totalScore / ${questionBank.size}"
            Toast.makeText(this, scoreMessage, Toast.LENGTH_LONG).show()
        }
        binding.trueButton?.isEnabled = false
        binding.falseButton?.isEnabled = false
    }
}