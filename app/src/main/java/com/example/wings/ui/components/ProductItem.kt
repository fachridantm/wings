package com.example.wings.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.wings.R
import com.example.wings.ui.theme.Shapes
import com.example.wings.ui.theme.WingsTheme
import com.example.wings.utils.convertToPercent
import com.example.wings.utils.convertToRupiah
import com.example.wings.utils.totalPrice

@Composable
fun ProductItem(
    image: Int,
    title: String,
    price: Int,
    disc: Double,
    modifier: Modifier = Modifier,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier
            .background(Color.LightGray)
            .padding(8.dp)
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(170.dp)
                .clip(Shapes.medium)
        )
        Text(
            text = title,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.subtitle1.copy(
                fontWeight = FontWeight.ExtraBold
            )
        )
        if (disc > 0.0) {
            Row {
                Text(
                    text = price.convertToRupiah(),
                    color = MaterialTheme.colors.secondary,
                    style = MaterialTheme.typography.subtitle2,
                    textDecoration = TextDecoration.LineThrough,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = disc.convertToPercent(),
                    color = MaterialTheme.colors.error,
                    style = MaterialTheme.typography.subtitle2,
                    fontSize = 12.sp,
                )
            }
        }
        Text(
            text = price.totalPrice(disc).convertToRupiah(),
            color = MaterialTheme.colors.secondary,
            style = MaterialTheme.typography.subtitle2,
        )
    }
}

@Composable
@Preview(showBackground = true)
fun ProductItemPreview() {
    WingsTheme {
        ProductItem(R.drawable.giv_biru, "Giv Biru", 11000, 0.0)
    }
}