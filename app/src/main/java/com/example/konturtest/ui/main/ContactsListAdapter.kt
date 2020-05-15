package com.example.konturtest.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.konturtest.R
import com.example.konturtest.databinding.ListItemContactBinding
import com.example.konturtest.db.model.Contact
import java.util.*
import kotlin.collections.ArrayList


class ContactsListAdapter : RecyclerView.Adapter<ContactsListAdapter.ItemViewHolder>(), Filterable {

    var contacts = mutableListOf<Contact>()
    var contactsFiltered = contacts
    var mainViewModel = MainViewModel()

    private fun getObjForPosition(position: Int) = contactsFiltered[position]

    fun setData(data: MutableList<Contact>) {
        contacts = data
        contactsFiltered = contacts
        notifyDataSetChanged()
    }

    override fun getItemCount() = contactsFiltered.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.list_item_contact, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getObjForPosition(position), position)
    }

    inner class ItemViewHolder(private val binding: ListItemContactBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(contact: Contact, position: Int) {
            with(binding) {
                this.viewModel = mainViewModel
                this.contact = contact
                this.position = position
                executePendingBindings()
            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(charSequence: CharSequence): FilterResults {
                val charString = charSequence.toString()
                contactsFiltered = if (charString.isEmpty()) {
                    contacts
                } else {
                    val filteredList: MutableList<Contact> = ArrayList()
                    for (row in contacts) {
                        if (row.name.toLowerCase(Locale.ROOT)
                                .contains(charString.toLowerCase(Locale.ROOT))
                            || row.phone.replace("\\D".toRegex(), "").contains(charSequence)
                            || row.height.toString().contains(charSequence)
                        ) {
                            filteredList.add(row)
                        }
                    }
                    filteredList
                }
                val filterResults = FilterResults()
                filterResults.values = contactsFiltered
                return filterResults
            }

            override fun publishResults(
                charSequence: CharSequence,
                filterResults: FilterResults
            ) {
                @Suppress("UNCHECKED_CAST")
                contactsFiltered = filterResults.values as MutableList<Contact>
                mainViewModel.filteredContacts = contactsFiltered
                notifyDataSetChanged()
            }
        }
    }
}