package com.example.konturtest.ui.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.konturtest.R
import com.example.konturtest.databinding.ProfileFragmentBinding
import com.example.konturtest.db.model.Contact
import kotlinx.android.synthetic.main.profile_fragment.*
import java.util.*

class ProfileFragment : Fragment(), Observer {

    lateinit var contact: Contact
    private var viewModel = ProfileViewModel()
    private lateinit var profileFragmentBinding: ProfileFragmentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setUpObserver(viewModel)
    }

    private fun setUpObserver(observable: Observable) {
        observable.addObserver(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        profileFragmentBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.profile_fragment,
                container,
                false
            )

        return profileFragmentBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        profileFragmentBinding.viewModel = viewModel

        contact = arguments?.getParcelable("contact")!!
        viewModel.contact = contact

        // Opening phone dial by clicking a phonenumber link
        phoneNumber.setOnClickListener {
            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + contact.phone))
            startActivity(intent)
        }
    }

    override fun update(observable: Observable?, arg: Any?) {
        if (observable is ProfileViewModel) {
            val mainViewModel: ProfileViewModel = observable
        }
    }
}
