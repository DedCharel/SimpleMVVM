package com.example.foundation.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.foundation.model.PendingResult
import com.example.foundation.model.Result
import com.example.foundation.model.tasks.Task
import com.example.foundation.model.tasks.TaskListener
import com.example.foundation.model.tasks.dispatchers.Dispatcher
import com.example.foundation.utils.Event

typealias LiveEvent<T> = LiveData<Event<T>>
typealias MutableLiveEvent<T> = MutableLiveData<Event<T>>

typealias LiveResult<T> = LiveData<Result<T>>
typealias MutableLiveResult<T> = MutableLiveData<Result<T>>
typealias MediatorLiveResult<T> = MediatorLiveData<Result<T>>
/**
 * Base class for all view-models.
 */
open class BaseViewModel(
    private val dispatcher: Dispatcher
) : ViewModel() {

    private val tasks = mutableListOf<Task<*>>()

    override fun onCleared() {
        super.onCleared()
        clearTasks()
    }

    /**
     * Override this method in child classes if you want to listen for results
     * from other screens
     */
    open fun onResult(result: Any) {

    }

    fun onBackPressed(){
        clearTasks()
    }

    fun <T> Task<T>.safeEnqueue(listener:TaskListener<T>? = null){
        tasks.add(this)
        this.enqueue(dispatcher) {
            tasks.remove(this)
            listener?.invoke(it)
        }
    }

    fun <T> Task<T>.into(liveResult: MutableLiveResult<T>){
        liveResult.value = PendingResult()
        this.safeEnqueue{
            liveResult.value = it
        }
    }

    fun clearTasks(){
        tasks.forEach { it.cancel() }
        tasks.clear()
    }
}