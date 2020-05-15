package com.example.konturtest.ui.main

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import androidx.core.os.bundleOf
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.konturtest.MainActivity
import com.example.konturtest.R
import com.example.konturtest.databinding.MainFragmentBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.main_fragment.*
import java.util.*


class MainFragment : Fragment(), Observer {

    val contactsListAdapter = ContactsListAdapter()
    private lateinit var mainFragmentBinding: MainFragmentBinding
    private var viewModel = MainViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        setUpObserver(viewModel)
    }

    private fun setUpObserver(observable: Observable) {
        observable.addObserver(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mainFragmentBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.main_fragment,
                container,
                false
            )

        return mainFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        mainFragmentBinding.viewModel = viewModel

        contactsList.adapter = contactsListAdapter
        contactsList.layoutManager = LinearLayoutManager(context)
        contactsList.addItemDecoration(
            DividerItemDecoration(
                context,
                DividerItemDecoration.VERTICAL
            )
        )

        val errorDispose = viewModel.errorMessages.subscribe {
            Snackbar.make(requireView(), it, Snackbar.LENGTH_LONG).show()
        }

        viewModel.getAllContacts()

        val dispose = viewModel.selectedContact.subscribe({ contact ->
            val contactBundle = bundleOf("contact" to contact)
            view?.findNavController()
                ?.navigate(R.id.action_mainFragment_to_profileFragment, contactBundle)
        }, {
            Log.e("Error", it.message.toString())
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)

        // Initialize top appbar search
        menu.clear()
        inflater.inflate(R.menu.menu_search, menu)
        val searchView =
            SearchView((context as MainActivity).supportActionBar?.themedContext ?: context)
        menu.findItem(R.id.action_search).apply {
            setShowAsAction(MenuItem.SHOW_AS_ACTION_COLLAPSE_ACTION_VIEW or MenuItem.SHOW_AS_ACTION_IF_ROOM)
            actionView = searchView
        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                contactsListAdapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(query: String): Boolean {
                contactsListAdapter.filter.filter(query)
                return false
            }
        })
    }

    override fun update(observable: Observable?, arg: Any?) {
        if (observable is MainViewModel) {
            val contactsAdapter: ContactsListAdapter =
                mainFragmentBinding.contactsList.adapter as ContactsListAdapter
            val mainViewModel: MainViewModel = observable
            contactsAdapter.setData(mainViewModel.totalContacts)
            contactsAdapter.mainViewModel = mainViewModel
        }
    }
}
