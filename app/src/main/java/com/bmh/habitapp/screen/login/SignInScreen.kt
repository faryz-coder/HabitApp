package com.bmh.habitapp.screen.login

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.bmh.habitapp.AuthActivity
import com.bmh.habitapp.MainActivity
import com.bmh.habitapp.R
import com.bmh.habitapp.manager.AuthManager
import com.bmh.habitapp.screen.Screen
import kotlinx.coroutines.launch

@Composable
fun SignInScreen(navHostController: NavHostController, authActivity: AuthActivity) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Body(navHostController, signInWithGoogle = {
            AuthManager().signInWithGoogle(authActivity)
        }, authActivity = authActivity)
    }
}


@Composable
fun Body(
    navHostController: NavHostController,
    signInWithGoogle: () -> Unit,
    authActivity: AuthActivity,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxHeight()) {
        Column(
            modifier = modifier
                .fillMaxHeight(0.4F)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = modifier.fillMaxHeight()
            ) {
                AppLogo()
            }
        }
        Column(modifier = modifier.fillMaxHeight()) {
            Box(modifier.fillMaxHeight(), contentAlignment = Alignment.BottomCenter) {
                SignInForm(navHostController, signInWithGoogle, authActivity)
            }
        }
    }
}


@Composable
fun SignInForm(
    navHostController: NavHostController,
    signInWithGoogle: () -> Unit,
    authActivity: AuthActivity,
    modifier: Modifier = Modifier
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordHidden by rememberSaveable { mutableStateOf(true) }
    val context = LocalContext.current
    var loader = remember {
        mutableStateOf(false)
    }

    val padding = 17.dp
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(padding),
        modifier = modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = modifier.fillMaxWidth(),
            visualTransformation =
            if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                IconButton(onClick = { passwordHidden = !passwordHidden }) {
                    val visibilityIcon =
                        if (passwordHidden) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                    // Please provide localized description for accessibility services
                    val description = if (passwordHidden) "Show password" else "Hide password"
                    Icon(imageVector = visibilityIcon, contentDescription = description)
                }
            }
        )
        ClickableText(name = "Forgot Password?")
        if (loader.value) {
            LinearProgressIndicator(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant
            )
        }
        LoginButton {
            if (email.isNotEmpty() && password.isNotEmpty()) {
                loader.value = true
                AuthManager().signInUserUsingEmailAndPassword(
                    activity = authActivity,
                    email = email,
                    password = password,
                    onSuccess = {
                        val context = navHostController.context
                        val intent = Intent(context, MainActivity::class.java)
                        context.startActivity(intent)
                    },
                    onFailed = {
                        Toast.makeText(context, "Login failed!", Toast.LENGTH_SHORT).show()
                        loader.value = false
                    }
                )
            }


//            (context as ComponentActivity).finish()
        }
        Spacer(modifier = Modifier.size(padding))
        NormalText()
        GoogleButton {
            signInWithGoogle.invoke()
        }
        DontHaveAccount {
            navHostController.navigate(
                route = Screen.SignUp.route
            )
        }
    }
}

@Composable
fun DontHaveAccount(onCLick: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "Don't have an account?")
        TextButton(onClick = onCLick) {
            Text(text = "Sign Up")
        }
    }
}


@Composable
fun NormalText() {
    Text(text = "Or Connect Using")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClickableText(name: String, modifier: Modifier = Modifier) {
    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    Text(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                showBottomSheet = true
            },
        text = name,
        textAlign = TextAlign.End
    )
    BottomSheetForgotPassword(showBottomSheet) {
        showBottomSheet = false
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomSheetForgotPassword(showBottomSheet: Boolean, onDismiss: () -> Unit) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    var email by rememberSaveable {
        mutableStateOf("")
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            sheetState = sheetState,
            onDismissRequest = onDismiss
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .padding(horizontal = 20.dp)
                    .padding(bottom = 20.dp),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Reset Password", fontWeight = FontWeight.Bold)
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = email,
                    maxLines = 1,
                    onValueChange = { email = it },
                    label = { Text(text = "Email") })
                Button(modifier = Modifier.fillMaxWidth(), onClick = {
                    if (email.isEmpty()) {
                        Toast.makeText(context, "Please Enter Email", Toast.LENGTH_SHORT).show()
                    } else {
                        // Submit Request to reset
                        AuthManager().resetPassword(email,
                            onSuccess = {
                                Toast.makeText(context, "Success!, Check your Inbox", Toast.LENGTH_SHORT).show()
                                scope.launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    onDismiss.invoke()
                                }
                            }},
                            onFailed = {
                                Toast.makeText(context, "Failed!, Check your Email", Toast.LENGTH_SHORT).show()
                            }
                        )

                    }
                }) {
                    Text(text = "RESET")
                }
            }
        }
    }
}

@Composable
fun LoginButton(modifier: Modifier = Modifier, onCLick: () -> Unit) {
    Button(
        onClick = onCLick,
        modifier = modifier
            .width(200.dp)
            .height(60.dp)
    ) {
        Text(text = "LOG IN", fontWeight = FontWeight.Bold)
    }
}

@Composable
fun GoogleButton(modifier: Modifier = Modifier, onCLick: () -> Unit) {
    Card(
        modifier = modifier.size(width = 200.dp, height = 60.dp),
        onClick = onCLick
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Text(text = "Google", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun AppLogo(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Image(painter = painterResource(id = R.drawable.habitiq_logo), contentDescription = "")
    }
}

@Composable
@Preview(showBackground = true)
fun SignInScreenPreview() {
    SignInScreen(navHostController = rememberNavController(), AuthActivity())
}