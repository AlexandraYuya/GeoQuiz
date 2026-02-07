package com.bignerdranch.android.geoquiz

import androidx.annotation.StringRes

// Use data keyword to clearly indicate this class holds model data
data class Question(@StringRes val textResId: Int, val answer: Boolean, var answerCheck: Boolean)

