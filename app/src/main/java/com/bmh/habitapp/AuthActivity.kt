package com.bmh.habitapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bmh.habitapp.manager.AuthManager
import com.bmh.habitapp.manager.BiometricManager
import com.bmh.habitapp.manager.SharePreferencesManager
import com.bmh.habitapp.ui.theme.IndomitableTheme
import com.bmh.habitapp.viewModel.MainViewModel
import com.bmh.navigation.AuthNavGraph
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException

class AuthActivity : FragmentActivity() {
    private lateinit var navController: NavHostController
    private val mainViewModel: MainViewModel by viewModels()
    private lateinit var sharePref: SharePreferencesManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharePref = SharePreferencesManager(this)
        mainViewModel.biometricManager = BiometricManager(this)
        setContent {
            IndomitableTheme {
                navController = rememberNavController()

                AuthNavGraph(navHostController = navController, this)
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                AuthManager().firebaseAuthWithGoogle(this, account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (AuthManager().currentUser()!= null) {
            // check if biometric enabled
            if (sharePref.isBiometricEnabled()) {
                BiometricManager(this).biometricPrompt(this, onSuccess = {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }, onFailed = {
                    AuthManager().signOut()
                })
            } else {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        Log.d("AuthActivity", "onStop")
        this.finish()
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
}