package com.wolf.notescout.ui.dashboard

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.EditText
import android.widget.SimpleAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wolf.notescout.R
import com.wolf.notescout.data.model.NoteRestData
import com.wolf.notescout.databinding.FragmentDashboardBinding
import com.wolf.notescout.util.SharedPreferencesUtil
import com.wolf.notescout.util.SwipeToDeleteCallback
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.add_item_dialog.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import kotlinx.android.synthetic.main.item_note.*

class DashboardFragment : Fragment() {

    private lateinit var adapter: NoteListAdapter
    private lateinit var viewModel: NoteViewModel
    private lateinit var binding: FragmentDashboardBinding
    private var isFirstTime: Boolean = false
    private var noteItemList: ArrayList<NoteRestData.NoteData> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        setupComponent()
        viewModel.handleGetAllNotesFromApi()
//        viewModel.completedTask.observe(viewLifecycleOwner, Observer {
//            completedTask = it
//            Log.i("COMPLETED TASK:", it.toString())
//        })

        viewModel.allNotesData.observe(viewLifecycleOwner, Observer {

            noteItemList = it as ArrayList<NoteRestData.NoteData>
            setupAdapter(noteItemList)
        })


        binding.srlNoteList.setOnRefreshListener {
            viewModel.handleGetAllNotesFromApi()
            binding.srlNoteList.isRefreshing = false
        }

        val dialog = setupAddNewNoteItem()

        binding.fabAddNote.setOnClickListener {
            dialog.show()
        }
    }

    private fun setupComponent() {
        binding.srlNoteList.setColorSchemeColors(Color.BLUE)
        binding.srlNoteList.setProgressBackgroundColorSchemeColor(Color.GREEN)

        val firstTime: Boolean = SharedPreferencesUtil.isFirstTime
        if(firstTime){
            Log.i("ACCESS", firstTime.toString())
        }else{
            Log.i("ACCESS", "Not first time")
        }
        SharedPreferencesUtil.isFirstTime = false

    }

    private fun setupAddNewNoteItem(): AlertDialog {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.add_item_dialog, null)
        val adb = AlertDialog.Builder(requireContext())
            .setTitle("Add New Note Item")
            .setPositiveButton("OK"){dialog, _ ->
                val newItem = view.findViewById(R.id.et_add_item) as EditText
                viewModel.handleAddNote(
                        newItem.text.toString(),
                        false,
                        "Fareez",
                        101)
            }
            .setNegativeButton("Cancel"){dialog, _ -> dialog.cancel()}
        val dialog = adb.setView(view).create()
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        return dialog
    }

    private fun setupAdapter(noteList: ArrayList<NoteRestData.NoteData>) {
        binding.rvNoteList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvNoteList.setHasFixedSize(true)
        adapter = NoteListAdapter(context, noteItemList)
        binding.rvNoteList.adapter = adapter

        adapter.onItemClick= {
            Log.i("CLICK ", it.item.toString())
        }

        adapter.onCheckBoxClick = {
            if(it.isChecked){
                viewModel.handleNoteItemIsChecked(true, it.id)
                viewModel.getCompletedNote()
                Log.i("CHECK", it.toString())
            }else{
                viewModel.handleNoteItemIsChecked(false, it.id)
                viewModel.getCompletedNote()
                Log.i("CHECK", it.toString())
            }
        }

        val swipeHandler = object : SwipeToDeleteCallback(requireContext()) {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                adapter.onItemClick = {
                    viewModel.handleDeleteNoteItem(it.id)
                }
                adapter.removeAt(viewHolder.adapterPosition)
            }
        }
        val itemTouchHelper = ItemTouchHelper(swipeHandler)
        itemTouchHelper.attachToRecyclerView(rv_note_list)
    }

}