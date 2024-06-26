package com.capstoneprojectg8.schoolscheduleapp.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.capstoneprojectg8.schoolscheduleapp.database.entities.AssignmentEntity
import com.capstoneprojectg8.schoolscheduleapp.database.entities.ClassEntity
import com.capstoneprojectg8.schoolscheduleapp.database.entities.ClassSlotEntity
import com.capstoneprojectg8.schoolscheduleapp.database.relations.ClassWithAssignmentsRelation

@Dao
interface ClassDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addClass(classes: ClassEntity)

    @Update
    suspend fun editClass(classes: ClassEntity)

    @Delete
    suspend fun deleteClass(classes: ClassEntity)

    @Query("SELECT * FROM CLASSES ORDER BY id ASC")
    fun getAllClasses(): LiveData<List<ClassEntity>>

    @Transaction
    @Query("SELECT * FROM CLASSES ORDER BY id ASC")
    fun getAllAssignmentsWithClasses(): LiveData<List<ClassWithAssignmentsRelation>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAssignment(assignmentEntity: AssignmentEntity)

    @Update
    suspend fun editAssignment(assignmentEntity: AssignmentEntity)

    @Delete
    suspend fun deleteAssignment(assignmentEntity: AssignmentEntity)

    @Query("SELECT * FROM ASSIGNMENT ORDER BY id ASC")
    fun getAllAssignments(): LiveData<List<AssignmentEntity>>

    @Transaction
    @Query("SELECT * FROM ASSIGNMENT WHERE classSlotId = :id")
    fun getAssignmentListByClassSlotId(id: Int): LiveData<List<AssignmentEntity>>

    @Transaction
    @Query("SELECT * FROM CLASSES WHERE id = :id")
    fun getClassById(id: Int): LiveData<ClassEntity>
}