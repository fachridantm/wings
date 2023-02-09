package com.example.wings.ui.screen.cart

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wings.R
import com.example.wings.domain.di.Injection
import com.example.wings.domain.model.CartState
import com.example.wings.ui.ViewModelFactory
import com.example.wings.ui.common.StateHolder
import com.example.wings.ui.components.CartItem
import com.example.wings.ui.components.CheckoutButton
import com.example.wings.utils.convertToRupiah

@Composable
fun CartScreen(
    viewModel: CartViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.provideRepository()
        )
    ),
    onCheckoutButtonClicked: (String) -> Unit,
) {
    viewModel.stateCart.collectAsState(initial = StateHolder.Loading).value.let { state ->
        when (state) {
            is StateHolder.Loading -> {
                viewModel.getAddedProduct()
            }
            is StateHolder.Success -> {
                if (state.data.product.isEmpty()) {
                    EmptyCart()
                } else {
                    CartContent(
                        state.data,
                        onProductCountChanged = { productId, count ->
                            viewModel.updateProduct(productId, count)
                        },
                        onCheckoutButtonClicked = onCheckoutButtonClicked
                    )
                }
            }
            is StateHolder.Error -> {}
        }
    }
}

@Composable
fun EmptyCart() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.cart_empty),
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun CartContent(
    state: CartState,
    onProductCountChanged: (id: Long, count: Int) -> Unit,
    onCheckoutButtonClicked: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val checkoutMessage = stringResource(R.string.checkout_messcage, state.totalPrice.convertToRupiah())
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        TopAppBar(backgroundColor = MaterialTheme.colors.surface) {
            Text(
                text = stringResource(R.string.menu_cart),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                fontWeight = FontWeight.ExtraBold,
                fontSize = 20.sp,
                textAlign = TextAlign.Center
            )
        }
        CheckoutButton(
            text = stringResource(R.string.total_checkout, state.totalPrice.convertToRupiah()),
            enabled = state.product.isNotEmpty(),
            onClick = {
                onCheckoutButtonClicked(checkoutMessage)
            },
            modifier = Modifier.padding(16.dp)
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            items(state.product, key = { it.product.id }) { item ->
                CartItem(
                    productId = item.product.id,
                    image = item.product.image,
                    title = item.product.title,
                    price = item.product.price * item.count,
                    disc = item.product.disc,
                    count = item.count,
                    onProductCountChanged = onProductCountChanged,
                )
                Divider()
            }
        }
    }
}