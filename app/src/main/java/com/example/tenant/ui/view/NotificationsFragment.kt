package com.example.tenant.ui.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tenant.R
import com.example.tenant.ui.viewmodel.MainActivityViewModel

class NotificationsFragment : Fragment() {
    private lateinit var notificationRecyclerView: RecyclerView
    private lateinit var mainActivityViewModel: MainActivityViewModel

    lateinit var notificationsAdapter: NotificationsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_notifications, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        notificationRecyclerView = view.findViewById(R.id.notificationRecycler)
        (activity as MainActivity).supportActionBar?.title = "Уведомления"
        mainActivityViewModel = (activity as MainActivity).mainActivityViewModel
        mainActivityViewModel.getAllNotifications()
        initRecycler()
        observeNotification()
    }

    private fun initRecycler(){
        notificationsAdapter = NotificationsAdapter()
        notificationRecyclerView.adapter = notificationsAdapter
        notificationRecyclerView.layoutManager = LinearLayoutManager(context)
    }

    private fun observeNotification(){
        mainActivityViewModel.notificationsLiveData.observe(viewLifecycleOwner, Observer{
            it?.let {list->
                if(list.isEmpty())
                    Log.i("not", "is")
                notificationsAdapter.notifications.submitList(list)

                for(notification in list){
                    if(!notification.isChecked){
                        notification.isChecked = true
                        mainActivityViewModel.addNotification(notification)
                    }
                    else
                        break
                }

                (activity as MainActivity).notificationsWereChecked()
            }
        })
    }
}