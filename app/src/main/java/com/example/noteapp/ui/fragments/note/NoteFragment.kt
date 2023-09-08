package com.example.noteapp.ui.fragments.note

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.noteapp.R
import com.example.noteapp.data.model.NoteEntity
import com.example.noteapp.databinding.AlertReminderBinding
import com.example.noteapp.databinding.FragmentNoteBinding
import com.example.noteapp.ui.fragments.note.adapter.ColorAdapter
import com.example.noteapp.utils.NoteQueryType
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
    private val noteArgs: NoteFragmentArgs by navArgs()
    private var noteType: NoteQueryType? = null
    private var updateNoteCreateTime: String? = null
    private var updateNoteCreateDate: String? = null

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
                    //if user does not select time and date
                    if (noteEntity.create_date.isEmpty() && noteEntity.create_time.isEmpty()) {
                        noteEntity.create_time = "${cal.get(Calendar.HOUR)}:${cal.get(Calendar.MINUTE)}"
                        noteEntity.create_date = SimpleDateFormat("MM/dd/yyyy").format(MaterialDatePicker.todayInUtcMilliseconds())
                    }
                    //save note
                    if (noteType == NoteQueryType.UPDATE) {
                        noteEntity.id = noteArgs.noteId
                        presenter.updateNotePresenter(noteEntity)
                    } else {
                        presenter.insertNewNotePresenter(noteEntity)
                    }
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
        //check query type
        checkNoteQueryType()

    }

    //check query type -> update or insert
    private fun checkNoteQueryType() {
        noteType = if (noteArgs.noteId > 0) {
            presenter.getNoteByIdPresenter(noteArgs.noteId)
            NoteQueryType.UPDATE
        } else {
            NoteQueryType.INSERT
        }
    }

    //set note back ground from color adapter
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

    //init color rec
    private fun initColorAdapter() {
        colorAdapter.differ.submitList(colorsList)
        binding.colorRec.initRec(
            colorAdapter,
            LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        )
    }

    // set pine menu item tint and value
    @SuppressLint("ResourceType")
    private fun manageIsPinned(item: MenuItem): Boolean {
        isPinned = if (!isPinned) {
            item.icon?.setTint(ContextCompat.getColor(requireContext(), R.color.SafetyOrange))
            true
        } else {
            val typeValue = TypedValue()
            context?.theme?.resolveAttribute(
                android.R.attr.textColorSecondary, typeValue, false
            )
            item.icon?.setTint(
                ContextCompat.getColor(
                    requireContext(), typeValue.data
                )
            )
            false
        }
        return isPinned
    }

    //show reminder alert dialog to choose time and date
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
            if (noteType == NoteQueryType.UPDATE) {
                timePickerTxt.text = updateNoteCreateTime
                datePickerTxt.text = updateNoteCreateDate
                noteEntity.create_time = timePickerTxt.text.toString()
                noteEntity.create_date = datePickerTxt.text.toString()
            }
            cancelBtn.setOnClickListener {
                alertDialog.dismiss()
            }
            saveBtn.setOnClickListener {
                //check that if create date and time are not null
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

    //show material date picker dialog
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

    //show material time picker dialog
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

    //close note fragment when click on save btn
    override fun closeFragment() {
        findNavController().popBackStack()
    }

    //load note data for update
    @SuppressLint("ResourceType")
    override fun loadNoteData(note: NoteEntity) {
        binding.apply {
            //set txt fields data
            titleEditText.setText(note.title)
            descriptionEditText.setText(note.description)
            //set background color
            root.setBackgroundColor(Utils().setBackgroundColor(note.color, requireContext()))
            descriptionEditText.setBackgroundColor(
                Utils().setBackgroundColor(
                    note.color,
                    requireContext()
                )
            )
            titleEditText.setBackgroundColor(
                Utils().setBackgroundColor(
                    note.color,
                    requireContext()
                )
            )
            //manage pin item color
            isPinned = note.isPinned
            if (isPinned) {
                detailToolBar.menu.findItem(R.id.pin_item).icon?.setTint(
                    ContextCompat.getColor(
                        requireContext(),
                        R.color.SafetyOrange
                    )
                )
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N_MR1) {
                    detailToolBar.menu.findItem(R.id.pin_item).icon?.setTint(
                        ContextCompat.getColor(
                            requireContext(),
                            android.R.attr.textColorSecondary
                        )
                    )
                }
            }
        }
        //set time and date data
        updateNoteCreateTime = note.create_time
        updateNoteCreateDate = note.create_date

    }

    override fun onStop() {
        super.onStop()
        presenter.onStop()
    }

}