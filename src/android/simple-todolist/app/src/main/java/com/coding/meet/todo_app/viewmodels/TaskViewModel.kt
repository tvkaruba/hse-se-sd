package com.coding.meet.todo_app.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.room.Query
import com.coding.meet.todo_app.models.Task
import com.coding.meet.todo_app.repository.TaskRepository
import com.coding.meet.todo_app.utils.Resource

class TaskViewModel(application: Application) : AndroidViewModel(application) {

    // Task Repository
    private val taskRepository = TaskRepository(application)
    val taskStateFlow get() =  taskRepository.taskStateFlow
    val statusLiveData get() =  taskRepository.statusLiveData
    val sortByLiveData get() =  taskRepository.sortByLiveData

    // Set Sort By
    fun setSortBy(sort:Pair<String,Boolean>){
        taskRepository.setSortBy(sort)
    }

    // Get Task List
    fun getTaskList(isAsc : Boolean, sortByName:String) {
        taskRepository.getTaskList(isAsc, sortByName)
    }

    // Insert Task
    fun insertTask(task: Task){
        taskRepository.insertTask(task)
    }

    // Delete Task
    fun deleteTask(task: Task) {
        taskRepository.deleteTask(task)
    }

    // Delete Task Using Id
    fun deleteTaskUsingId(taskId: String){
        taskRepository.deleteTaskUsingId(taskId)
    }

    // Update Task
    fun updateTask(task: Task) {
        taskRepository.updateTask(task)
    }

    // Update Task Paticular Field
    fun updateTaskPaticularField(taskId: String,title:String,description:String) {
        taskRepository.updateTaskPaticularField(taskId, title, description)
    }

    // Search Task List
    fun searchTaskList(query: String){
        taskRepository.searchTaskList(query)
    }
}