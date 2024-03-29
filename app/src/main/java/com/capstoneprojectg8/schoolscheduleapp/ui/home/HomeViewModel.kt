package com.capstoneprojectg8.schoolscheduleapp.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstoneprojectg8.schoolscheduleapp.database.repository.ClassRepository
import com.capstoneprojectg8.schoolscheduleapp.models.SClass
import com.capstoneprojectg8.schoolscheduleapp.models.Assignment
import com.capstoneprojectg8.schoolscheduleapp.utils.toEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val classRepository: ClassRepository
) :
    ViewModel() {


    private var _classes = MutableLiveData<List<SClass>>()
    val classes: LiveData<List<SClass>> = _classes

    fun getAllAssignmentsWithClasses() = classRepository.getAllAssignmentsWithClasses()

    fun getAssignmentListByClassId(classSlotId: Int) =
        classRepository.getAssignmentListByClassSlot(classSlotId)

    fun deleteAssignment(assignment: Assignment) {
        viewModelScope.launch(Dispatchers.IO) {
            classRepository.deleteAssignment(assignment.toEntity())
        }
    }

    fun editAssignment(assignment: Assignment) {
        viewModelScope.launch(Dispatchers.IO) {
            classRepository.editAssignment(assignment.toEntity())
        }
    }

    fun getAllClassSlots() = classRepository.getAllClassSlots()
}