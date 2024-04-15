package com.example.ecommerceapp

import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.example.ecommerceapp.data.datasource.repository.user.UserPreferencesRepositoryImpl
import com.example.ecommerceapp.ui.AuthActivity
import com.example.ecommerceapp.ui.user.UserViewModel
import com.example.ecommerceapp.ui.user.UserViewModelFactory
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(UserPreferencesRepositoryImpl(this@MainActivity))
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        initSplashScreen()
        super.onCreate(savedInstanceState)
        lifecycleScope.launch(Main) {
            val isLoggedIn = userViewModel.isUserLoggedIn().first()
            if (isLoggedIn) {
                setContentView(R.layout.activity_main)
            } else {
                userViewModel.setIsLoggedIn(userLoginState = false)
                goToAuthActivity()
            }
        }
    }

    private fun goToAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        startActivity(intent)
    }

    private fun initSplashScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            installSplashScreen()
            // Add a callback that's called when the splash screen is animating to the
            // app content.
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                // Create your custom animation.
                val slideUp = ObjectAnimator.ofFloat(
                    splashScreenView, View.TRANSLATION_Y, 0f, -splashScreenView.height.toFloat()
                )
                slideUp.interpolator = AnticipateInterpolator()
                slideUp.duration = 1000L

                // Call SplashScreenView.remove at the end of your custom animation.
                slideUp.doOnEnd { splashScreenView.remove() }

                // Run your animation.
                slideUp.start()
            }
        } else {
            setTheme(R.style.Theme_Ecommerceapp)
        }
    }

}
/*
  <com.google.android.material.textview.MaterialTextView
                android:id="@+id/textView4"
                android:layout_width="wrap_content"
                android:layout_height="wrap_content"
                android:layout_marginTop="16dp"
                android:fontFamily="@font/poppins_bold"
                android:letterSpacing="0.05"
                android:lineSpacingExtra="8sp"
                android:text="@string/welcome_to_lafyuu"
                android:textAlignment="center"
                style="@style/TitleTextStyle"
                app:layout_constraintEnd_toEndOf="@+id/imageView"
                app:layout_constraintStart_toStartOf="@+id/imageView"
                app:layout_constraintTop_toBottomOf="@+id/imageView" />

            <com.google.android.material.textview.MaterialTextView
                android:id="@+id/textView"
                android:layout_width="wrap_content"
                android:layout_height="22dp"
                android:layout_marginTop="8dp"
                android:fontFamily="@font/poppins"
                android:letterSpacing="0.3"
                android:lineSpacingExtra="10sp"
                android:text="Signin to continue"
                android:textAlignment="center"
                android:textColor="#9098B1"
                android:textSize="12sp"
                android:textStyle="normal"
                app:layout_constraintEnd_toEndOf="@+id/textView4"
                app:layout_constraintStart_toStartOf="@+id/textView4"
                app:layout_constraintTop_toBottomOf="@+id/textView4" />
 */
