package feature.profile

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.myhome.R
import common.BASE_URL
import common.MyHomeFragment
import common.MyHomeSingleObserver
import data.UserInformation
import feature.login.LoginOrSignUpActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.fragment_profile.*
import okhttp3.MultipartBody
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import services.ImageLoadingService

class ProfileFragment : MyHomeFragment() {

    val viewModel: ProfileViewModel by viewModel()
    val imageLoadingService: ImageLoadingService by inject()
    val compositeDisposable = CompositeDisposable()

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
            prf_phone.visibility = View.VISIBLE

            //button sign out
            authBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sign_out, 0)
            authBtn.text = getString(R.string.signOut)
            authBtn.setOnClickListener {
                viewModel.signOut()
                checkAuthState()
            }

            //get user
            viewModel.getUser(viewModel.phoneNumber)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(object : MyHomeSingleObserver<UserInformation>(compositeDisposable) {
                    override fun onSuccess(t: UserInformation) {
                        prf_phone.text = t.phone
                        prf_name.text = t.username
                        if (t.image != "")
                            imageLoadingService.load(prf_image, "${BASE_URL}${t.image}")
                        else
                            prf_image.setImageResource(R.drawable.ic_profile)
                    }
                })

        } else {
            //un visible view
            prf_image.setImageResource(R.drawable.ic_profile)
            prf_phone.visibility = View.GONE
            edtBtn.visibility = View.GONE
            myBannerBtn.visibility = View.GONE
            prf_name.text = getString(R.string.guest_user)

            //button sign in
            authBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sign_in, 0)
            authBtn.text = getString(R.string.signIn)
            authBtn.setOnClickListener {
                viewModel.signOut()
                startActivity(
                    Intent(
                        requireContext(),
                        LoginOrSignUpActivity::class.java
                    )
                )
            }

        }
    }

    override fun onResume() {
        super.onResume()
        //در onResume موقع برگشت به اکتیویتی دوباره این تابع فراخوان میشه اما اگه در OnCreate باشه فقط اول برنامه ک صفحه لود بشه فراخوانی میشه
        checkAuthState()
    }

}