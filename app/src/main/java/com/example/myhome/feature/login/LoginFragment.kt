package com.example.myhome.feature.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.myhome.R
import com.example.myhome.common.MyHomeSingleObserver
import com.example.myhome.common.asyncNetworkRequest
import com.example.myhome.data.model.State
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_login.*
import org.koin.android.viewmodel.ext.android.viewModel

class LoginFragment : Fragment() {

    private val compositeDisposable = CompositeDisposable()
    private val viewModel: AuthViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        signUpLinkBtn.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().apply {
                replace(R.id.fragmentContainer, SignUpFragment())
            }.commit()
        }

        loginBtn.setOnClickListener {
            viewModel.login(phoneEt.text.toString(), passwordEt.text.toString())
                .asyncNetworkRequest()
                .subscribe(object : MyHomeSingleObserver<State>(compositeDisposable) {
                    override fun onSuccess(t: State) {
                        if (t.state)
                            requireActivity().finish()
                        else
                            Toast.makeText(
                                requireContext(),
                                "!شماره موبایل یا رمز عبور اشتباه است",
                                Toast.LENGTH_SHORT
                            ).show()
                    }
                })
        }
    }

    override fun onStop() {
        super.onStop()
        compositeDisposable.clear()
    }
}