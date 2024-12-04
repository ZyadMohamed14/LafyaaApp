package com.example.ecommerceapp.features.dashboard.account

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Payment
import androidx.compose.material.icons.outlined.ShoppingBag
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.ecommerceapp.core.routing.Routes
import com.example.ecommerceapp.core.theme.ColorsManager
import com.example.ecommerceapp.core.theme.productTitleStyle


@Composable
fun AccountScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = "Account", style = productTitleStyle)
        Spacer(modifier = Modifier.height(32.dp))
        Divider(color = ColorsManager.NeutralLight, thickness = 2.dp)
        Spacer(modifier = Modifier.height(16.dp))
        AccountItem(icon = Icons.Filled.Person2, title = "Profile") {
            navController.navigate(Routes.ProfileScreen)
        }
        Spacer(modifier = Modifier.height(32.dp))
        AccountItem(icon = Icons.Outlined.ShoppingBag, title = "Orders") {
            navController.navigate(Routes.OrderScreen)
        }
        Spacer(modifier = Modifier.height(32.dp))
        AccountItem(icon = Icons.Outlined.LocationOn, title = "Address") {
            navController.navigate(Routes.AddressScreen)
        }
        Spacer(modifier = Modifier.height(32.dp))
        AccountItem(icon = Icons.Outlined.Payment, title = "Payment")
    }
}


@Composable
fun AccountItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit = {}
) {
    Row(modifier = Modifier.clickable { onClick() },verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon
            ,
            contentDescription = "person" ,
            tint = ColorsManager.PrimaryColor,
            modifier = Modifier.size(35.dp)
            )
        Spacer(modifier = Modifier.width(8.dp))
        Text(title,style = productTitleStyle.copy(fontSize = 12.sp))

    }
}