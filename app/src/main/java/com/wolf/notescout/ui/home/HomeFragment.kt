package com.wolf.notescout.ui.home

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.wolf.notescout.R
import com.wolf.notescout.databinding.FragmentDashboardBinding
import com.wolf.notescout.databinding.FragmentHomeBinding
import com.wolf.notescout.ui.dashboard.NoteViewModel
import com.wolf.notescout.util.SharedPreferencesUtil
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: NoteViewModel
    private lateinit var navController: NavController
    private var getGroupID: String = ""
    private var currentUser: String? = ""
    private var isValid: Boolean = false
    private var subscription = CompositeDisposable()


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(NoteViewModel::class.java)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel
        navController = Navigation.findNavController(view)

        currentUser = SharedPreferencesUtil.username
        Log.i("CURRENTUSER", currentUser.toString())

        viewModel.noteNotFound.observe(viewLifecycleOwner, Observer {
            if(it){
                isValid = false
                btn_home_submit.isEnabled = false
                Timber.i("INVALID !!!!")
            }else{
                isValid = true
                btn_home_submit.isEnabled = true
            }
        })

        if(!currentUser.isNullOrEmpty()){
            showGroupIdCL()
            hideNewUserCL()
        }else{
            hideGroupIdCL()
            showNewUserCL()
        }

        et_home_groupId.addTextChangedListener(object : TextWatcher {

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(s: Editable?) {}

        })

        btn_home_submit.setOnClickListener {
            getGroupID = et_home_groupId.text.toString()
            SharedPreferencesUtil.groupId = getGroupID.toInt()
            handleGetNotesByGroupId(SharedPreferencesUtil.groupId)
        }

        et_new_username.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                val user = s.toString()
                currentUser = user
            }

        })

        btn_submit_username.setOnClickListener {
            SharedPreferencesUtil.username = currentUser
            SharedPreferencesUtil.isFirstTime = false
            hideNewUserCL()
            showGroupIdCL()
        }

    }

    private fun hideNewUserCL() {
        cl_enter_username.visibility = View.GONE
        et_new_username.isEnabled = false
        btn_submit_username.isEnabled = false
    }

    private fun showNewUserCL() {
        cl_enter_username.visibility = View.VISIBLE
        et_new_username.isEnabled = true
        btn_submit_username.isEnabled = true
    }

    private fun hideGroupIdCL() {
        cl_enter_group_id.visibility = View.GONE
        et_home_groupId.isEnabled = false
        btn_home_submit.isEnabled = false
    }

    private fun showGroupIdCL() {
        cl_enter_group_id.visibility = View.VISIBLE
        et_home_groupId.isEnabled = true
        btn_home_submit.isEnabled = true
    }

    fun handleGetNotesByGroupId(groupID: Int){
        val subscribe = viewModel.handleCheckNotesExistance(groupID)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ it ->
                if(it.isEmpty()) {
                    Log.i("DATA", "EMPTYYYYYY :PPPPPP")
                }else{
                    navController.navigate(R.id.action_homeFragment_to_dashboardFragment)
                }
            }, {
                    err -> var msg = err.localizedMessage
                Log.i("DATA", msg.toString())
            })
        subscription.add(subscribe)
    }
}