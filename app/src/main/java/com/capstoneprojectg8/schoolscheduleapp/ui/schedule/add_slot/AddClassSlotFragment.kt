package com.capstoneprojectg8.schoolscheduleapp.ui.schedule.add_slot

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.capstoneprojectg8.schoolscheduleapp.databinding.FragmentAddClassSlotBinding
import com.capstoneprojectg8.schoolscheduleapp.models.SClass
import com.capstoneprojectg8.schoolscheduleapp.models.ClassSlot
import com.capstoneprojectg8.schoolscheduleapp.utils.DateHelper
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale


class AddClassSlotFragment : Fragment() {
    private lateinit var calendar: Calendar

    private var _binding: FragmentAddClassSlotBinding? = null
    private val binding get() = _binding!!

    private lateinit var SClassList: List<SClass>


    private val viewModel: AddClassSlotViewModel by activityViewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddClassSlotBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getAllClasses().observe(viewLifecycleOwner) { classList ->

            this.SClassList = classList
            val classOptions = classList.map { it.name }

            Log.d("Add", classOptions.toString())
            val adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_dropdown_item_1line,
                classOptions
            )

            (binding.autoCompleteTextViewClass as? AutoCompleteTextView)?.setAdapter(adapter)
        }


        calendar = Calendar.getInstance()
        val today = LocalDate.now()
        val localDate = DateHelper.startOfTheWeek(today)
        if (localDate > today) {
            calendar.set(localDate.year, localDate.monthValue - 1, localDate.dayOfMonth, 9, 0)
        } else {
            calendar.set(today.year, today.monthValue - 1, today.dayOfMonth, 9, 0)
        }

        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        binding.editTextDate.setText(dateFormat.format(calendar.time))
        binding.editTextTime.setText("09:00")

        binding.editTextDate.setOnClickListener { showDatePicker() }
        binding.editTextTime.setOnClickListener { showTimePicker() }

        binding.buttonSubmit.setOnClickListener {
            val selectedClassName = binding.autoCompleteTextViewClass.text.toString()
            val roomNumber = binding.editTextRoom.text.toString()
            val duration = binding.editTextDuration.text.toString().toIntOrNull()

            val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val timeFormat = SimpleDateFormat("HH", Locale.getDefault())
            val formattedDate = dateFormat.format(calendar.time)
            val formattedTime = timeFormat.format(calendar.time)


            if ((duration == null || duration <= 0) || roomNumber.isBlank() || selectedClassName.isBlank()) {
                if (selectedClassName.isBlank()) {
                    binding.textInputClass.error = "Please select a class"
                }

                if (roomNumber.isBlank()) {
                    binding.textInputRoom.error = "Please enter a valid room number"
                }

                if (duration == null || duration <= 0) {
                    binding.textInputDuration.error = "Please enter a valid duration"
                }


                return@setOnClickListener
            }
            val selectedClass = SClassList.find { it.name == selectedClassName }

            val classSlot = ClassSlot(
                id = 0,
                startingHour = formattedTime.toInt(),
                dayOfTheWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1,
                noOfHours = duration,
                classId = selectedClass!!.id,
                className = selectedClass.name,
                classRoom = roomNumber,
                color = selectedClass.colour,
                date = formattedDate
            )
            lifecycleScope.launch {
                viewModel.addClassSlot(classSlot)
                requireActivity().onBackPressedDispatcher.onBackPressed()
            }

        }
        binding.buttonCancel.setOnClickListener {
            requireActivity().onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun showDatePicker() {
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            requireContext(),
            { _, _year, _month, dayOfMonth ->
                calendar.set(_year, _month, dayOfMonth)
                updateDateEditText()
            },
            year,
            month,
            day
        )
        datePickerDialog.show()
    }

    private fun showTimePicker() {
        val hour = 9
        val minute = 0

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            { _, hourOfDay, _ ->
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
                calendar.set(Calendar.MINUTE, 0)
                updateTimeEditText()
            },
            hour,
            minute,
            true
        )
        timePickerDialog.show()
    }

    private fun updateDateEditText() {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        binding.editTextDate.setText(dateFormat.format(calendar.time))
    }

    private fun updateTimeEditText() {
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        binding.editTextTime.setText(timeFormat.format(calendar.time))
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
    }
}