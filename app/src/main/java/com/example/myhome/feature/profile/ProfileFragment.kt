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
import com.example.myhome.databinding.FragmentProfileBinding
import com.example.myhome.services.ImageLoadingService
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class ProfileFragment : MyHomeFragment() {
    private lateinit var binding: FragmentProfileBinding
    private val viewModel: UserViewModel by viewModel()
    private val imageLoadingService: ImageLoadingService by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.user.observe(requireActivity()) { user ->
            binding.userPhoneNumber.text = user.phone
            binding.userName.text = user.username

            if (user.image != "")
                imageLoadingService.load(binding.userImage, "${BASE_URL}${user.image}")
            else
                binding.userImage.setImageResource(R.drawable.ic_profile)
        }

    }

    private fun checkAuthState() {
        if (viewModel.isSignIn) {
            binding.editBtn.visibility = View.VISIBLE
            binding.userBannerBtn.visibility = View.VISIBLE
            binding.userPhoneNumber.visibility = View.VISIBLE

            binding.authBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sign_out, 0)
            binding.authBtn.text = getString(R.string.signOut)
            binding.authBtn.setOnClickListener {
                viewModel.signOut()
                checkAuthState()
            }

            binding.editBtn.setOnClickListener {
                findNavController().navigate(
                    ProfileFragmentDirections.actionProfileToEditProfile(
                        viewModel.user.value!!
                    )
                )
            }

            binding.userBannerBtn.setOnClickListener {
                findNavController().navigate(ProfileFragmentDirections.actionProfileToUserBannerFragment())
            }

        } else {
            binding.editBtn.visibility = View.GONE
            binding.userBannerBtn.visibility = View.GONE
            binding.userPhoneNumber.visibility = View.GONE

            binding.userImage.setImageResource(R.drawable.ic_profile)
            binding.userName.text = getString(R.string.guest_user)

            binding.authBtn.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_sign_in, 0)
            binding.authBtn.text = getString(R.string.signIn)
            binding.authBtn.setOnClickListener {
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