package feature.profile

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myhome.R
import common.MyHomeFragment
import feature.login.LoginOrSignUpActivity
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment : MyHomeFragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        authBtn.setOnClickListener {
            startActivity(Intent(requireContext(), LoginOrSignUpActivity::class.java))
        }
    }
}