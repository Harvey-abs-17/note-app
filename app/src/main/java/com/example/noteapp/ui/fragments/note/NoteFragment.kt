package com.example.noteapp.ui.fragments.note

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.data.model.NoteEntity
import com.example.noteapp.databinding.AlertReminderBinding
import com.example.noteapp.databinding.FragmentNoteBinding
import com.example.noteapp.ui.fragments.note.adapter.ColorAdapter
import com.example.noteapp.ui.fragments.note.adapter.NotePresenter
import com.example.noteapp.utils.Utils
import com.example.noteapp.utils.initRec
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import javax.inject.Inject

@AndroidEntryPoint
class NoteFragment : Fragment(), NoteContract.View {

    //binding
    private lateinit var binding: FragmentNoteBinding

    //others
    private val cal = Calendar.getInstance()
    private var isPinned = false
    private val colorsList = listOf<Utils.Color>(
        Utils.Color.Blue,
        Utils.Color.Green,
        Utils.Color.Orange,
        Utils.Color.Red,
        Utils.Color.White,
        Utils.Color.Yellow
    )

    @Inject
    lateinit var noteEntity: NoteEntity

    @Inject
    lateinit var colorAdapter: ColorAdapter

    @Inject
    lateinit var presenter: NotePresenter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentNoteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            // back btn navigation
            detailToolBar.setNavigationOnClickListener {
                findNavController().popBackStack()
            }
            // tool bar menu item listener
            detailToolBar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    // show date alert dialog
                    R.id.remind_date -> {
                        showReminderAlertDialog()
                    }

                    R.id.pin_item -> {
                        //manage isPinned
                        noteEntity.isPinned = manageIsPinned(item)
                    }
                }
                return@setOnMenuItemClickListener true
            }
            //save new notes
            saveBtn.setOnClickListener {
                //fill note entity title and description
                if (titleEditText.text.isNotEmpty() && descriptionEditText.text.isNotEmpty()) {
                    noteEntity.title = titleEditText.text.toString()
                    noteEntity.description = descriptionEditText.text.toString()
                    //save new note
                    presenter.insertNewNote(noteEntity)
                } else {
                    Snackbar.make(
                        binding.root,
                        "Please fill fields correctly.",
                        Snackbar.LENGTH_INDEFINITE
                    )
                        .setAction(
                            "Ok"
                        ) { }.show()
                }
            }
        }
        // initialize color adapter
        initColorAdapter()
        //note background color
        setNoteFragmentBackgroundColor()
    }

    private fun setNoteFragmentBackgroundColor() {
        colorAdapter.itemClickListener {
            binding.root.setBackgroundColor(Utils().setBackgroundColor(it, requireContext()))
            binding.titleEditText.setBackgroundColor(
                Utils().setBackgroundColor(
                    it,
                    requireContext()
                )
            )
            binding.descriptionEditText.setBackgroundColor(
                Utils().setBackgroundColor(
                    it,
                    requireContext()
                )
            )
            //set note entity color
            noteEntity.color = it
        }
    }

    private fun initColorAdapter() {
        colorAdapter.differ.submitList(colorsList)
        binding.colorRec.initRec(
            colorAdapter,
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        )
    }

    private fun manageIsPinned(item: MenuItem): Boolean {
        isPinned = if (!isPinned) {
            item.icon?.setTint(ContextCompat.getColor(requireContext(), R.color.SafetyOrange))
            true
        } else {
            item.icon?.setTint(ContextCompat.getColor(requireContext(), R.color.white))
            false
        }
        return isPinned
    }

    private fun showReminderAlertDialog() {
        val alertBuilder = AlertDialog.Builder(requireContext())
        val alertBinding = AlertReminderBinding.inflate(
            LayoutInflater.from(requireContext()),
            binding.root,
            false
        )
        alertBuilder.setView(alertBinding.root)
        alertBuilder.setCancelable(false)
        val alertDialog = alertBuilder.create()
        alertBinding.apply {
            cancelBtn.setOnClickListener {
                alertDialog.dismiss()
            }
            saveBtn.setOnClickListener {
                if (datePickerTxt.text.isNotEmpty() && timePickerTxt.text.isNotEmpty()) {
                    noteEntity.create_time = timePickerTxt.text.toString()
                    noteEntity.create_date = datePickerTxt.text.toString()
                    alertDialog.dismiss()
                } else {
                    Toast.makeText(
                        requireContext(),
                        " please choose date and time reminder ",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
            selectDateBtn.setOnClickListener {
                showDatePickerDialog(
                    datePickerTxt
                )
            }
            selectTimeBtn.setOnClickListener {
                showTimePickerDialog(timePickerTxt)
            }
        }
        alertDialog.show()
    }

    private fun showDatePickerDialog(
        datePickerTxt: TextView,
    ) {
        //create constraint for date picker which start from today
        val constraintsBuilder =
            CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
                .setStart(cal.get(Calendar.MONTH + 1).toLong())
                .build()
        //create date picker
        val datePicker =
            MaterialDatePicker.Builder.datePicker()
                .setTitleText("Select Reminder date")
                .setSelection(MaterialDatePicker.todayInUtcMilliseconds())
                .setCalendarConstraints(constraintsBuilder)
                .build()
        datePicker.show(parentFragmentManager, "salam")
        // manage different situation listener
        // set selected date when click on ok
        datePicker.addOnPositiveButtonClickListener {
            datePickerTxt.text = SimpleDateFormat("MM/dd/yyyy").format(it)
        }
        // set today date when click on cancel
        datePicker.addOnNegativeButtonClickListener {
            datePickerTxt.text =
                SimpleDateFormat("MM/dd/yyyy").format(MaterialDatePicker.todayInUtcMilliseconds())
        }
    }

    private fun showTimePickerDialog(timePickerTxt: TextView) {
        //create date picker
        val timer = MaterialTimePicker.Builder()
            .setTitleText("Select reminder time")
            .setTimeFormat(TimeFormat.CLOCK_24H)
            .setHour(cal.get(Calendar.HOUR))
            .setMinute(cal.get(Calendar.MINUTE))
            .build()
        // manage different situation listener
        // set selected time when click on ok
        timer.addOnPositiveButtonClickListener {
            timePickerTxt.text = "${timer.hour}:${timer.minute}"
        }
        // set today time when click on cancel
        timer.addOnNegativeButtonClickListener {
            timePickerTxt.text = "${cal.get(Calendar.HOUR)}:${cal.get(Calendar.MINUTE)}"
        }
        timer.show(parentFragmentManager, "hadi")
    }

    override fun closeFragment() {
        findNavController().popBackStack()
    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

}