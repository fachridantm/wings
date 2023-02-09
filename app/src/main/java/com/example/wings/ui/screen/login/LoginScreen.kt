package com.example.wings.ui.screen.login

import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wings.LoginActivity
import com.example.wings.MainActivity
import com.example.wings.R
import com.example.wings.domain.di.Injection
import com.example.wings.ui.ViewModelFactory
import com.example.wings.ui.common.StateHolder
import com.example.wings.ui.components.OutlinedTextFieldValidation
import com.example.wings.utils.showMessage

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.provideRepository()
        )
    ),
) {
    val context = LocalContext.current
    val activity = context as? LoginActivity
    viewModel.stateLogin.collectAsState(initial = StateHolder.Loading).value.let { state ->
        when (state) {
            is StateHolder.Loading -> {
                LoginContent(
                    onLoginClick = { email, password ->
                        viewModel.loginUser(email, password)
                    }
                )
            }
            is StateHolder.Success -> {
                val intent = Intent(context, MainActivity::class.java)
                context.startActivity(intent)
                activity?.finish()
                state.data.message.showMessage(context)
            }
            is StateHolder.Error -> {
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
                activity?.finish()
                state.errorMessage.showMessage(context)
            }
        }
    }
}

@Composable
fun LoginContent(
    modifier: Modifier = Modifier,
    onLoginClick: (String, String) -> Unit,
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var errorEmail by remember { mutableStateOf("") }
    var errorPassword by remember { mutableStateOf("") }
    var passwordVisibility: Boolean by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.login),
            style = MaterialTheme.typography.h4.copy(
                fontWeight = FontWeight.ExtraBold,
            ),
            modifier = Modifier.padding(top = 24.dp)
        )
        Text(
            text = stringResource(R.string.enter_email_and_password),
            style = MaterialTheme.typography.body1
        )
        OutlinedTextFieldValidation(
            value = email,
            label = { Text(text = stringResource(R.string.email)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            onValueChange = { newInput ->
                email = newInput
            },
            error = errorEmail,
            singleLine = true,
            modifier = Modifier
                .padding(top = 8.dp)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )
        OutlinedTextFieldValidation(
            value = password,
            label = { Text(text = stringResource(R.string.password)) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { newInput ->
                password = newInput
            },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            trailingIcon = {
                val image =
                    if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisibility) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                    Icon(imageVector = image, description)
                }
            },
            error = errorPassword,
            singleLine = true,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
        )

        Button(
            onClick = {
                errorEmail = if (email.isEmpty()) {
                    context.getString(R.string.email_cannot_be_empty)
                } else {
                    if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        context.getString(R.string.invalid_email_format)
                    } else {
                        ""
                    }
                }
                errorPassword = if (password.isEmpty()) {
                    context.getString(R.string.password_cannot_be_empty)
                } else {
                    ""
                }
                if (errorEmail.isEmpty() && errorPassword.isEmpty()) {
                    onLoginClick(email, password)
                }
            },
            modifier = Modifier
                .padding(24.dp)
                .height(48.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.login),
                style = MaterialTheme.typography.button,
                modifier = Modifier.align(
                    Alignment.CenterVertically
                )
            )
        }

    }
}
