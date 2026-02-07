package com.bignerdranch.android.geoquiz

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bignerdranch.android.geoquiz.databinding.MainActivityBinding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Menu
import androidx.compose.runtime.Composable

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {
    private lateinit var binding: MainActivityBinding
    private val quizViewModel: QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()) {
            result ->
            if(result.resultCode == Activity.RESULT_OK) {
                quizViewModel.isCheater =
                    result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        binding.trueButton?.setOnClickListener { view: View ->
            checkAnswer(true)
        }

        binding.falseButton?.setOnClickListener { view: View ->
            checkAnswer(false)
        }

        binding.cheatButton?.setOnClickListener { view: View ->
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatLauncher.launch(intent)
        }

        binding.prevButton?.setOnClickListener { view: View ->
            quizViewModel.moveToNext()
            updateQuestion()
        }

        binding.nextButton?.setOnClickListener { view: View ->
            quizViewModel.moveToNext()
            updateQuestion()
        }

        binding.questionTextView?.setOnClickListener { view: View ->
            quizViewModel.moveToNext()
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
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView?.setText(questionTextResId)

        val answered = quizViewModel.currentQuestionAnswerCheck
        binding.trueButton?.isEnabled = !answered
        binding.falseButton?.isEnabled = !answered
    }

    private fun checkAnswer(userAnswer: Boolean) {
        val correctAnswer = quizViewModel.currentQuestionAnswer

        if(!quizViewModel.currentQuestionAnswerCheck) {
            if(userAnswer == correctAnswer) {
                quizViewModel.setTotalScore(1)
            }
            quizViewModel.setAnswerCheck(true)
        }

        val messageResId = when {
            quizViewModel.isCheater -> R.string.judgement_toast
            userAnswer == correctAnswer -> R.string.correct_toast
            else -> R.string.incorrect_toast
        }

        Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()

        val allAnswered = quizViewModel.getQuestionBank.all { it.answerCheck }
        if (allAnswered) {
            val scoreMessage = "Score: ${quizViewModel.getTotalScore} / ${quizViewModel.getQuestionBank.size}"
            Toast.makeText(this, scoreMessage, Toast.LENGTH_LONG).show()
        }
        binding.trueButton?.isEnabled = false
        binding.falseButton?.isEnabled = false
    }
}