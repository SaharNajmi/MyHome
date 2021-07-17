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
import org.koin.android.viewmodel.ext.android.viewModel

class ProfileFragment : MyHomeFragment() {
    val viewModel: ProfileViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    private fun checkAuthState() {
        if (viewModel.isSignIn) {
            //visible view
            edtBtn.visibility = View.VISIBLE
            myBannerBtn.visibility = View.VISIBLE
            val a = viewModel.isSignIn
            prf_phone.visibility = View.VISIBLE
            authBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sign_out, 0)
            authBtn.text = getString(R.string.signOut)
            authBtn.setOnClickListener {
                viewModel.signOut()
                checkAuthState()
            }
        } else {
            authBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sign_in, 0)
            authBtn.text = getString(R.string.signIn)
            authBtn.setOnClickListener {
                viewModel.signOut()
                startActivity(Intent(requireContext(), LoginOrSignUpActivity::class.java))
            }

            prf_image.setImageResource(R.drawable.ic_profile)
            prf_phone.visibility = View.GONE
            edtBtn.visibility = View.GONE
            myBannerBtn.visibility = View.GONE
            prf_name.text = getString(R.string.guest_user)

        }
    }

    override fun onResume() {
        super.onResume()
        //در onResume موقع برگشت به اکتیویتی دوباره این تابع فراخوان میشه اما اگه در OnCreate باشه فقط اول برنامه ک صفحه لود بشه فراخوانی میشه
        checkAuthState()
    }

}