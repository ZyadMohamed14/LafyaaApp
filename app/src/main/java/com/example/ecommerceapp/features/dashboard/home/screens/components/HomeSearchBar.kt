package com.example.ecommerceapp.features.dashboard.home.screens.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.example.ecommerceapp.R
import com.example.ecommerceapp.features.dashboard.home.screens.HomeViewModel
import com.example.ecommerceapp.core.theme.ColorsManager

@Composable
fun SearchBar(
    searchQuery: String = "",
    onConfirmSearch: () -> Unit={},
    onSearchBarClick: () -> Unit={},
    onValueChange: (String) -> Unit,
) {
    var isFocused by remember { mutableStateOf(false) }
    val interactionSource = remember { MutableInteractionSource() }
    OutlinedTextField(
        value = searchQuery,
        onValueChange = { onValueChange(it) },
        interactionSource = interactionSource,
        leadingIcon = {
            Icon(
                painterResource(id = R.drawable.ic_search),
                tint = ColorsManager.PrimaryColor,
                contentDescription = "Search"
            )
        },
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Done // Make sure imeAction is set to Done
        ),
        keyboardActions = KeyboardActions(
            onGo = { onConfirmSearch() },    // Executes when "Go" is pressed
            onDone = { onConfirmSearch() }   // Executes when "Done" is pressed
        ),
        placeholder = {
            Text(text = stringResource(R.string.search_products))
        },
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = ColorsManager.NeutralGrey,
            unfocusedTextColor = ColorsManager.NeutralGrey,
            cursorColor = ColorsManager.PrimaryColor,
            focusedBorderColor = ColorsManager.PrimaryColor,
            unfocusedBorderColor = ColorsManager.NeutralLight,
        ),
        modifier = Modifier
           // .fillMaxWidth(0.9f)
            .padding(8.dp)
            .onFocusChanged { focusState ->
                if (focusState.isFocused && !isFocused) {
                    isFocused = true
                    onSearchBarClick()
                }
            }
    )
}
