package com.example.wings.ui.screen.profile

import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.example.wings.LoginActivity
import com.example.wings.MainActivity
import com.example.wings.R
import com.example.wings.domain.model.Login
import com.example.wings.ui.theme.WingsTheme
import com.example.wings.utils.LinkText
import com.example.wings.utils.LinkTextData

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.profile_photo),
            contentDescription = stringResource(R.string.profile_photo),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(200.dp)
                .clip(CircleShape)
        )
        Text(
            text = "Fachridan Tio Mu'afa",
            style = MaterialTheme.typography.body1.copy(
                fontWeight = FontWeight.ExtraBold
            ),
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)

        )
        LinkText(
            linkTextData = listOf(
                LinkTextData(
                    text = "fachridantm@gmail.com",
                    tag = "email",
                    annotation = "mailto:fachridantm@gmail.com",
                    onClick = {
                        Log.d("Link text", "${it.tag} ${it.item}")
                    },
                ),
            ),
            context = context,
            modifier = Modifier.padding(8.dp),
        )
        Button(
            onClick = {
                val activity = context as? MainActivity
                val intent = Intent(context, LoginActivity::class.java)
                context.startActivity(intent)
                activity?.finish()
            },
            modifier = Modifier
                .padding(24.dp)
                .height(48.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.logout),
                style = MaterialTheme.typography.button,
                modifier = Modifier.align(Alignment.CenterVertically
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    WingsTheme {
        ProfileScreen()
    }
}