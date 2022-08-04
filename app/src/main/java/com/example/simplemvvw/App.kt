package com.example.simplemvvw

import android.app.Application
import com.example.foundation.BaseApplication
import com.example.foundation.model.Repository
import com.example.foundation.model.tasks.SimpleTaskFactory
import com.example.simplemvvw.model.colors.InMemoryColorsRepository

/**
* Here we store instances of model layer classes
*/
class App:Application(), BaseApplication {

    private val taskFactory = SimpleTaskFactory()

    override val repositories: List<Repository> = listOf(
        taskFactory,
        InMemoryColorsRepository(taskFactory)
    )
}