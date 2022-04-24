package com.example.myhome.feature.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.observe
import androidx.navigation.fragment.findNavController
import com.example.myhome.R
import com.example.myhome.common.Constants.BASE_URL
import com.example.myhome.common.MyHomeFragment
import com.example.myhome.services.ImageLoadingService
import kotlinx.android.synthetic.main.fragment_profile.*
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class ProfileFragment : MyHomeFragment() {

    private val viewModel: UserViewModel by viewModel()
    private val imageLoadingService: ImageLoadingService by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //get user info
        viewModel.user.observe(requireActivity()) { user ->
            user_phone_number.text = user.phone
            user_name.text = user.username

            if (user.image != "")
                imageLoadingService.load(user_image, "${BASE_URL}${user.image}")
            else
                user_image.setImageResource(R.drawable.ic_profile)
        }

    }

    private fun checkAuthState() {
        if (viewModel.isSignIn) {
            //visible items edit profile
            edit_btn.visibility = View.VISIBLE
            user_banner_btn.visibility = View.VISIBLE
            user_phone_number.visibility = View.VISIBLE

            //sign out
            auth_btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sign_out, 0)
            auth_btn.text = getString(R.string.signOut)
            auth_btn.setOnClickListener {
                viewModel.signOut()
                checkAuthState()
            }

            //edit user
            edit_btn.setOnClickListener {
                findNavController().navigate(
                    ProfileFragmentDirections.actionProfileToEditProfile(
                        viewModel.user.value!!
                    )
                )
            }

            //go UserBanner
            user_banner_btn.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileToUserBannerFragment())
            }

        } else {
            //hide items edit profile
            edit_btn.visibility = View.GONE
            user_banner_btn.visibility = View.GONE
            user_phone_number.visibility = View.GONE

            user_image.setImageResource(R.drawable.ic_profile)
            user_name.text = getString(R.string.guest_user)

            //sign in
            auth_btn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sign_in, 0)
            auth_btn.text = getString(R.string.signIn)
            auth_btn.setOnClickListener {
                viewModel.signOut()
                findNavController().navigate(ProfileFragmentDirections.actionProfileToLoginOrSignUp())
            }
        }
    }

    override fun onResume() {
        super.onResume()
        checkAuthState()
    }
}