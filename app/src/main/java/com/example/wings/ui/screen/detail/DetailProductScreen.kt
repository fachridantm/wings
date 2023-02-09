package com.example.wings.ui.screen.detail

import androidx.annotation.DrawableRes
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.LightGray
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wings.R
import com.example.wings.domain.di.Injection
import com.example.wings.ui.ViewModelFactory
import com.example.wings.ui.common.StateHolder
import com.example.wings.ui.components.CheckoutButton
import com.example.wings.ui.components.ProductCounter
import com.example.wings.ui.theme.WingsTheme
import com.example.wings.utils.convertToPercent
import com.example.wings.utils.convertToRupiah
import com.example.wings.utils.totalPrice

@Composable
fun DetailProductScreen(
    productId: Long,
    viewModel: DetailProductViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.provideRepository()
        )
    ),
    navigateBack: () -> Unit,
    navigateToCart: () -> Unit,
) {
    viewModel.stateDetail.collectAsState(initial = StateHolder.Loading).value.let { stateHolder ->
        when (stateHolder) {
            is StateHolder.Loading -> {
                viewModel.getProductById(productId)
            }
            is StateHolder.Success -> {
                val data = stateHolder.data
                DetailProductContent(
                    data.product.image,
                    data.product.title,
                    data.product.desc,
                    data.product.price,
                    data.product.disc,
                    data.count,
                    onBackClick = navigateBack,
                    onAddToCart = { count ->
                        viewModel.addToCart(data.product, count)
                        navigateToCart()
                    }
                )
            }
            is StateHolder.Error -> {}
        }
    }
}

@Composable
fun DetailProductContent(
    @DrawableRes image: Int,
    title: String,
    desc: String,
    price: Int,
    disc: Double,
    count: Int,
    onBackClick: () -> Unit,
    onAddToCart: (count: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var totalPrice by rememberSaveable { mutableStateOf(0) }
    var orderCount by rememberSaveable { mutableStateOf(count) }

    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) {
            Box {
                Image(
                    painter = painterResource(image),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .height(400.dp)
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp))
                        .padding(16.dp)
                )
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(R.string.back),
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { onBackClick() }
                )
            }
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h5.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                )
                if (disc > 0.0) {
                    Row(modifier = Modifier.padding(top = 8.dp)){
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
                    style = MaterialTheme.typography.subtitle1.copy(
                        fontWeight = FontWeight.ExtraBold
                    ),
                    color = MaterialTheme.colors.secondary,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = desc,
                    style = MaterialTheme.typography.body2,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(LightGray))
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            ProductCounter(
                1,
                orderCount,
                onProductIncreased = { orderCount++ },
                onProductDecreased = { if (orderCount > 0) orderCount-- },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
            )
            totalPrice = price.totalPrice(disc) * orderCount
            CheckoutButton(
                text = stringResource(R.string.add_to_cart, totalPrice.convertToRupiah()),
                enabled = orderCount > 0,
                onClick = {
                    onAddToCart(orderCount)
                }
            )
        }
    }
}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun DetailContentPreview() {
    WingsTheme {
        DetailProductContent(
            R.drawable.soklin_pewangi,
            "So Klin Pewangi",
            "Dimension: 13cm x 10cm",
            15000,
            0.1,
            1,
            onBackClick = {},
            onAddToCart = {}
        )
    }
}
