package com.wolf.notescout.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.wolf.notescout.R
import com.wolf.notescout.databinding.FragmentHomeBinding
import com.wolf.notescout.ui.dashboard.NoteViewModel
import com.wolf.notescout.ui.dialog.ExistingGroupDialog
import com.wolf.notescout.ui.dialog.GroupNotFoundDialog
import com.wolf.notescout.util.SharedPreferencesUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.add_new_group_note_dialog.*
import kotlinx.android.synthetic.main.add_new_name_dialog.*
import kotlinx.android.synthetic.main.add_new_name_dialog.et_new_username
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.join_existing_group_dialog.*


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: NoteViewModel
    private lateinit var navController: NavController
    private lateinit var existingGroupDialog: ExistingGroupDialog
    private lateinit var groupNotFoundDialog: GroupNotFoundDialog
    private var getGroupID: String = ""
    private var currentUser: String? = ""
    private var subscription = CompositeDisposable()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (requireActivity() as AppCompatActivity).supportActionBar?.hide()

        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        navController = Navigation.findNavController(view)

        currentUser = SharedPreferencesUtil.username
        Log.i("CURRENTUSER", currentUser.toString())
        if(!currentUser.isNullOrEmpty()){
            hideNewUserCL()
        }else{
            showNewUserCL()
        }
        setupView()

    }

    private fun setupView() {

        binding.cvJoinExistingNote.setOnClickListener {
            hideAddNewGroupCV()
            showJoinGroupCV()
        }

        binding.cvAddNewGroupNote.setOnClickListener {
            hideJoinGroupCV()
            showAddNewGroupCV()
        }

        //===============LISTENER EXISTING GROUP=====================

        et_existing_group_id.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        btn_submit_existing_group.setOnClickListener {
            getGroupID = et_existing_group_id.text.toString()
            SharedPreferencesUtil.groupId = getGroupID.toInt()
            handleCheckNotesByGroupId(SharedPreferencesUtil.groupId)
        }

        btn_back_existing_group.setOnClickListener {
            hideJoinGroupCV()
        }

        //===============LISTENER EXISTING GROUP=====================

        //===============LISTENER NEW GROUP=====================

        et_new_group_id.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        btn_submit_new_group_note.setOnClickListener {
            getGroupID = et_new_group_id.text.toString()
            SharedPreferencesUtil.groupId = getGroupID.toInt()
            handleCheckNotesByGroupId(SharedPreferencesUtil.groupId)
        }

        btn_back_new_group_note.setOnClickListener {
            hideAddNewGroupCV()
        }

        //===============LISTENER NEW GROUP=====================

        //===============LISTENER NEW USERNAME =====================

        et_new_username.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {}
        })

        btn_submit_username.setOnClickListener {
            SharedPreferencesUtil.username = et_new_username.text.toString()
            SharedPreferencesUtil.isFirstTime = false
            hideNewUserCL()
        }

        //===============LISTENER NEW USERNAME =====================

    }

    private fun hideNewUserCL() {
        cv_add_new_name_dialog.visibility = View.GONE
    }

    private fun showNewUserCL() {
        cv_add_new_name_dialog.visibility = View.VISIBLE
    }

    private fun hideJoinGroupCV() {
        cv_join_existing_note_dialog.visibility = View.GONE
    }

    private fun showJoinGroupCV() {
        cv_join_existing_note_dialog.visibility = View.VISIBLE
    }

    private fun showAddNewGroupCV() {
        cv_add_new_group_note_dialog.visibility = View.VISIBLE
    }

    private fun hideAddNewGroupCV() {
        cv_add_new_group_note_dialog.visibility = View.GONE
    }


    private fun handleCheckNotesByGroupId(groupID: Int){
        val subscribe = viewModel.handleCheckNotesExistence(groupID)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ it ->
                    if (it.isEmpty()) {
                        showGroupNotFoundDialog()
                    } else {
                        navController.navigate(R.id.action_homeFragment_to_dashboardFragment)
                    }
                }, { err ->
                    var msg = err.localizedMessage
                    Log.i("DATA", msg.toString())
                })
        subscription.add(subscribe)
    }

    private fun showExistingGroupDialog() {
        existingGroupDialog = ExistingGroupDialog(requireContext())
        existingGroupDialog.show()
    }

    private fun showGroupNotFoundDialog() {
        groupNotFoundDialog = GroupNotFoundDialog(requireContext())
        groupNotFoundDialog.show()
    }
}