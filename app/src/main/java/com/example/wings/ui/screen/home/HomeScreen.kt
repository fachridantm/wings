package com.example.wings.ui.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.wings.domain.di.Injection
import com.example.wings.ui.ViewModelFactory
import com.example.wings.ui.common.StateHolder
import com.example.wings.ui.components.SearchBar
import com.example.wings.R
import com.example.wings.domain.model.Product
import com.example.wings.ui.components.ProductItem

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateToDetail: (Long) -> Unit,
) {
    val query by viewModel.query

    viewModel.stateHome.collectAsState(initial = StateHolder.Loading).value.let { stateHolder ->
        when (stateHolder) {
            is StateHolder.Loading -> {
                viewModel.getAllProduct()
            }
            is StateHolder.Success -> {
                Column {
                    SearchBar(
                        query = query,
                        onQueryChange = viewModel::findProduct,
                        modifier = Modifier
                            .background(color = MaterialTheme.colors.primary)
                    )
                    if (stateHolder.data.isEmpty()) {
                        NotFound()
                    } else {
                        HomeContent(
                            products = stateHolder.data,
                            modifier = modifier,
                            navigateToDetail = navigateToDetail,
                        )
                    }
                }
            }
            is StateHolder.Error -> {}
        }
    }
}

@Composable
fun NotFound() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = stringResource(R.string.not_found),
            fontSize = 18.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun HomeContent(
    products: List<Product>,
    modifier: Modifier = Modifier,
    navigateToDetail: (Long) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier.testTag("ProductList")
    ) {
        items(products, key = {it.product.id}) { data ->
            ProductItem(
                image = data.product.image,
                title = data.product.title,
                price = data.product.price,
                disc = data.product.disc,
                modifier = Modifier.clickable {
                    navigateToDetail(data.product.id)
                }
            )
        }
    }
}