package com.bignerdranch.android.geoquiz

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"

const val IS_CHEATER_KEY = "IS_CHEATER_KEY"
class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {
    private val questionBank = listOf(
        Question(R.string.question_australia, true, false),
        Question(R.string.question_oceans, true, false),
        Question(R.string.question_mideast, false, false),
        Question(R.string.question_africa, false, false),
        Question(R.string.question_americas, true, false),
        Question(R.string.question_asia, true, false)
    )

    var isCheater: Boolean
        get() = savedStateHandle.get(IS_CHEATER_KEY) ?: false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)

    private var currentIndex: Int
        get() = savedStateHandle.get(CURRENT_INDEX_KEY) ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)
    private var totalScore = 0

    val getCurrentIndex: Int
        get() = currentIndex

    val getTotalScore: Int
        get() = totalScore

    val getQuestionBank: List<Question>
        get() = questionBank
    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    val currentQuestionAnswerCheck: Boolean
        get() = questionBank[currentIndex].answerCheck

    fun setTotalScore(point: Int) {
        totalScore += point
    }
    fun setAnswerCheck(value: Boolean) {
        questionBank[currentIndex].answerCheck = value
    }
//    fun setCurrentIndex(index: Int) {
//        currentIndex = index
//    }
    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }
}