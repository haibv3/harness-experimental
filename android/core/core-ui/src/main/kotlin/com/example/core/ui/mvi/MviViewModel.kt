package com.example.core.ui.mvi

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Base ViewModel for MVI pattern.
 * Subclasses should override [handleIntent] to process user actions.
 */
abstract class MviViewModel<I : MviIntent, S : MviViewState>(
    initialState: S
) : ViewModel() {

    private val _state = MutableStateFlow(initialState)
    val state: StateFlow<S> = _state.asStateFlow()

    /**
     * Process a user intent and update the state.
     */
    abstract fun handleIntent(intent: I)

    protected fun updateState(reducer: S.() -> S) {
        _state.value = _state.value.reducer()
    }
}
