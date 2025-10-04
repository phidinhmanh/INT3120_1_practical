package com.example.unscramble.data

import com.example.unscramble.ui.GameViewModel
import org.junit.Test


class ViewModelTest {
    private val viewModel = GameViewModel()

    @Test
    fun gameViewModel_CorrectWordGuessed_ScoreUpdatedAndErrorFlagUnset() {
        var currentGameUiState = viewModel.uiState.value
        val correctGuess = getUnscrambledWord(currentGameUiState.currentGameWord)

        viewModel.updateUserGuess(correctGuess)
        viewModel.checkUserGuess()

        currentGameUiState = viewModel.uiState.value
        assert(currentGameUiState.isGuessedWrong.not())
        assert(currentGameUiState.score == SCORE_AFTER_FIRST_CORRECT_ANSWER)

    }

    @Test
    fun gameViewModel_IncorrectWordGuessed_ErrorFlagSet() {
        val incorrectGuess = "image"

        viewModel.updateUserGuess(incorrectGuess)
        viewModel.checkUserGuess()
        var currentGameUiState = viewModel.uiState.value

        currentGameUiState = viewModel.uiState.value
        assert(currentGameUiState.isGuessedWrong)
        assert(currentGameUiState.score == 0)
    }


    // Boundary case
    @Test
    fun gameViewModel_Initialization_FirstWordLoaded() {
        val currentGameUiState = viewModel.uiState.value
        val unscrambledWord = getUnscrambledWord(currentGameUiState.currentGameWord)
        assert(currentGameUiState.currentWordCount == 1)
        assert(currentGameUiState.currentGameWord != unscrambledWord)
        assert(currentGameUiState.score == 0)
        assert(currentGameUiState.isGuessedWrong.not())
        assert(currentGameUiState.isGameOver .not())

    }

    @Test
    fun gameViewModel_AllWordsGuessed_UiStateUpdatedCorrectly() {
        var exceptedScore = 0
        var currentGameUiState = viewModel.uiState.value
        var correctPlayerWord = getUnscrambledWord(currentGameUiState.currentGameWord)
        repeat(MAX_NO_OF_WORDS) {
            viewModel.updateUserGuess(correctPlayerWord)
            viewModel.checkUserGuess()
            exceptedScore += SCORE_INCREASE
            currentGameUiState = viewModel.uiState.value
            correctPlayerWord = getUnscrambledWord(currentGameUiState.currentGameWord)
            assert(currentGameUiState.score == exceptedScore)
        }
        assert(currentGameUiState.isGameOver)
        assert(currentGameUiState.currentWordCount == MAX_NO_OF_WORDS)
    }



    companion object {
        private const val 
                SCORE_AFTER_FIRST_CORRECT_ANSWER = SCORE_INCREASE
    }
}