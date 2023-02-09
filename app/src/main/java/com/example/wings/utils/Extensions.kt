package com.example.wings.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import com.example.wings.R
import com.google.android.material.textfield.TextInputLayout
import java.text.NumberFormat
import java.util.*

fun Int.convertToRupiah(): String {
    val localID = Locale("in", "ID")
    val formatter = NumberFormat.getCurrencyInstance(localID)
    return formatter.format(this).dropLast(3)
}

fun Double.convertToPercent(): String {
    val percent = this * 100
    return "$percent%"
}

fun Int.totalPrice(disc: Double): Int {
    return (this - (this * disc)).toInt()
}

fun String.showMessage(context: Context) {
    Toast.makeText(context, this, Toast.LENGTH_SHORT).show()
}

data class LinkTextData(
    val text: String,
    val tag: String? = null,
    val annotation: String? = null,
    val onClick: ((str: AnnotatedString.Range<String>) -> Unit)? = null,
)

@Composable
fun LinkText(
    linkTextData: List<LinkTextData>,
    context: Context,
    modifier: Modifier = Modifier,
) {
    val annotatedString = createAnnotatedString(linkTextData)

    ClickableText(
        text = annotatedString,
        style = MaterialTheme.typography.body1,
        onClick = { offset ->
            linkTextData.forEach { annotatedStringData ->
                if (annotatedStringData.tag != null && annotatedStringData.annotation != null) {
                    if(annotatedStringData.annotation.startsWith("mailto:")) {
                        val mail = annotatedStringData.annotation.replaceFirst("mailto:", "")
                        val intent = Intent(Intent.CATEGORY_APP_EMAIL).apply {
                            type = "text/plain"
                            putExtra(Intent.EXTRA_EMAIL, mail)
                        }
                        context.startActivity(
                            Intent.createChooser(
                                intent,
                                context.getString(R.string.send_email)
                            )
                        )
                    }
                    annotatedString.getStringAnnotations(
                        tag = annotatedStringData.tag,
                        start = offset,
                        end = offset,
                    ).firstOrNull()?.let {
                        annotatedStringData.onClick?.invoke(it)
                    }
                }
            }
        },
        modifier = modifier,
    )
}

@Composable
private fun createAnnotatedString(data: List<LinkTextData>): AnnotatedString {
    return buildAnnotatedString {
        data.forEach { linkTextData ->
            if (linkTextData.tag != null && linkTextData.annotation != null) {
                pushStringAnnotation(
                    tag = linkTextData.tag,
                    annotation = linkTextData.annotation,
                )
                withStyle(
                    style = SpanStyle(
                        color = MaterialTheme.colors.primary,
                        textDecoration = TextDecoration.Underline,
                    ),
                ) {
                    append(linkTextData.text)
                }
                pop()
            } else {
                append(linkTextData.text)
            }
        }
    }
}

fun TextInputLayout.showError(isError: Boolean, message: String? = null) {
    if (isError) {
        isErrorEnabled = false
        error = null
        isErrorEnabled = true
        error = message
    } else {
        isErrorEnabled = false
        error = null
    }
}