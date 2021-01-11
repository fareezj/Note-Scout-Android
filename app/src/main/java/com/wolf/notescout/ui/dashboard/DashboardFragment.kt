package com.wolf.notescout.ui.dashboard

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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.wolf.notescout.R
import com.wolf.notescout.data.model.NoteRestData
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.add_item_dialog.*
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : Fragment() {

    private lateinit var adapter: NoteListAdapter
    private lateinit var viewModel: NoteViewModel
    private var noteItemList: ArrayList<NoteRestData.NoteData> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        viewModel.handleGetAllNotesFromApi()

        viewModel.allNotesData.observe(viewLifecycleOwner, Observer {
            noteItemList = it as ArrayList<NoteRestData.NoteData>
            setupAdapter(noteItemList)
        })

        setupComponent()

        srl_note_list.setOnRefreshListener {
            viewModel.handleGetAllNotesFromApi()
            srl_note_list.isRefreshing = false
        }

        val dialog = setupAddNewNoteItem()

        fab_add_note.setOnClickListener {
            dialog.show()
        }
    }

    private fun setupComponent() {
        srl_note_list.setColorSchemeColors(Color.BLUE)
        srl_note_list.setProgressBackgroundColorSchemeColor(Color.GREEN)

    }

    private fun setupAddNewNoteItem(): AlertDialog {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.add_item_dialog, null)
        val adb = AlertDialog.Builder(requireContext())
            .setTitle("Add New Note Item")
            .setPositiveButton("OK"){dialog, _ ->
                val newItem = view.findViewById(R.id.et_add_item) as EditText
                viewModel.handleAddNote(newItem.text.toString(), true)
            }
            .setNegativeButton("Cancel"){dialog, _ -> dialog.cancel()}
        val dialog = adb.setView(view).create()
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)
        return dialog
    }

    private fun setupAdapter(noteList: ArrayList<NoteRestData.NoteData>) {
        rv_note_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rv_note_list.setHasFixedSize(true)
        adapter = NoteListAdapter(context, noteItemList)
        rv_note_list.adapter = adapter

        adapter.onItemClick= {
            Log.i("CLICK ", it.item.toString())
        }
    }

}