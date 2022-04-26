package com.example.myhome.feature.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.myhome.R
import com.example.myhome.databinding.FragmentLoginOrSignUpBinding
import com.example.myhome.feature.main.MainActivity

class LoginOrSignUpFragment : Fragment() {
    private lateinit var binding: FragmentLoginOrSignUpBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginOrSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        activity?.supportFragmentManager?.beginTransaction()
            ?.apply { replace(R.id.fragmentContainer, LoginFragment()).commit() }

        //hideBottom Navigation
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.hideBottomNavigation()
        }
    }

    override fun onStop() {
        super.onStop()
        //show BottomNavigation
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)!!.showBottomNavigation()
        }
    }
}