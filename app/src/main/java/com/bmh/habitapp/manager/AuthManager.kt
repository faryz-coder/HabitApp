package com.bmh.habitapp.manager

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.bmh.habitapp.R
import com.bmh.habitapp.AuthActivity
import com.bmh.habitapp.MainActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class AuthManager {
    val auth = Firebase.auth

    fun signInWithGoogle(activity: Activity) {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(activity.getString(R.string.web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(activity, gso)
        val signInIntent = googleSignInClient.signInIntent
        activity.startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    fun currentUser(): String? {
        val currentUser = auth.currentUser
        return currentUser?.email
    }

    fun signOut() {
        auth.signOut()
    }

    fun firebaseAuthWithGoogle(activity: Activity, idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser
                    Toast.makeText(activity, "User: ${user?.email}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(activity, MainActivity::class.java)
                    activity.startActivity(intent)
                    activity.finish()
//                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
//                    updateUI(null)
                }
            }
    }

    fun registerAccount(
        activity: AuthActivity,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailed: () -> Unit
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    onSuccess.invoke()
                } else {
                    onFailed.invoke()
                }
            }
    }

    fun signInUserUsingEmailAndPassword(
        activity: AuthActivity,
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFailed: () -> Unit
    ) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(activity) { task ->
                if (task.isSuccessful) {
                    onSuccess.invoke()
                } else {
                    onFailed.invoke()
                }
            }

    }

    fun resetPassword(email: String, onSuccess: () -> Unit, onFailed: () -> Unit) {
        auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                onSuccess.invoke()
            }
            .addOnFailureListener {
                onFailed.invoke()
            }
    }

    companion object {
        private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }

}