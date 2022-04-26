package com.example.myhome.feature.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.myhome.R
import com.example.myhome.common.MyHomeFragment
import com.example.myhome.common.Result
import com.example.myhome.common.showMessage
import com.example.myhome.databinding.FragmentLoginBinding
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : MyHomeFragment() {
    private lateinit var binding: FragmentLoginBinding
    private val viewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.signUpLinkBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, SignUpFragment())
            }.commit()
        }

        //login
        binding.loginBtn.setOnClickListener {
            if (binding.phoneEt.text.toString().trim().isNotEmpty() &&
                binding.passwordEt.text.toString().trim().isNotEmpty()
            )
                viewModel.login(binding.phoneEt.text.toString(), binding.passwordEt.text.toString())
            else
                context?.showMessage("لطفا تمامی فیلدها را پر کنید")
        }

        viewModel.loginResult.observe(requireActivity()) { result ->
            when (result) {
                is Result.Success -> {
                    setProgress(false)

                    if (result.data.state) {
                        activity?.showMessage("ورود با موفقیت انجام شد")
                        findNavController().navigate(LoginOrSignUpFragmentDirections.actionLoginOrSignUpToProfile())
                    } else
                        activity?.showMessage("شماره موبایل یا رمز عبور اشتباه است")

                }
                is Result.Loading -> {
                    setProgress(true)
                }

                is Result.Error -> {
                    setProgress(false)
                    context?.showMessage("مشکل در اتصال به اینترنت")
                }
            }
        }

    }
}