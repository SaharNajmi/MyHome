package feature.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.myhome.R

class LoginOrSignUpActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_or_sign_up)

        supportFragmentManager.beginTransaction()
            .apply { replace(R.id.fragmentContainer, LoginFragment()).commit() }

    }
}