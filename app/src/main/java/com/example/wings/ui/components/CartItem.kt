package com.example.wings.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
fun CartItem(
    productId: Long,
    image: Int,
    title: String,
    price: Int,
    disc: Double,
    count: Int,
    onProductCountChanged: (id: Long, count: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(image),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(90.dp)
                .clip(Shapes.small)
                .padding(8.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .weight(1.0f)
        ) {
            Text(
                text = title,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.subtitle1.copy(
                    fontWeight = FontWeight.ExtraBold
                )
            )
            if (disc > 0.0) {
                Row(modifier = Modifier.padding(top = 12.dp)) {
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
                modifier = Modifier.padding(top = 8.dp)
            )
        }
        ProductCounter(
            orderId = productId,
            orderCount = count,
            onProductIncreased = { onProductCountChanged(productId, count + 1) },
            onProductDecreased = { onProductCountChanged(productId, count - 1) },
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Composable
@Preview(showBackground = true)
fun CartItemPreview() {
    WingsTheme {
        CartItem(
            1, R.drawable.soklin_pewangi, "So Klin Pewangi", 15000, 0.1, 0,
            onProductCountChanged = { _, _ -> },
        )
    }
}