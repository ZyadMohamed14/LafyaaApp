package com.example.ecommerceapp.core.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ecommerceapp.core.theme.ColorsManager
import com.example.ecommerceapp.core.theme.Poppins


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "",
    icon: @Composable (() -> Unit)? = null,
    isPassword: Boolean = false ,
    isError: Boolean = false,
    errorMessage: String = ""

) {
    var passwordVisibility by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = { onValueChange(it) },
        modifier = modifier
            .fillMaxWidth()
            .padding(10.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            cursorColor = ColorsManager.PrimaryColor, // Primary color for cursor
            focusedBorderColor = if (isError) ColorsManager.PrimaryRed else ColorsManager.PrimaryColor, // Change border color on error
            unfocusedBorderColor = if (isError) ColorsManager.PrimaryRed else ColorsManager.Gray400

        ),
        label = { if (label.isNotEmpty()) Text(text = label, style = EditTextHintStyle) },
        leadingIcon = icon,
        placeholder = { Text(text = value,style = EditTextHintStyle) },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text
        ),
        isError = isError,
        visualTransformation = if (isPassword && !passwordVisibility) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        trailingIcon = if (isPassword) {
            {
                Icon(
                    imageVector = if (passwordVisibility) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                    contentDescription = if (passwordVisibility) "Hide password" else "Show password",
                    modifier = Modifier.clickable { passwordVisibility = !passwordVisibility }
                )
            }
        } else {
            null
        }
    )
    if (isError && errorMessage.isNotEmpty()) {
        Text(
            text = errorMessage,
            color =ColorsManager.PrimaryColor,
            style = EditTextHintStyle,
            modifier = Modifier.padding(start = 16.dp, top = 4.dp)
        )
    }
}


val EditTextHintStyle = TextStyle(
    fontFamily = Poppins,
    fontSize = 14.sp,
    color = ColorsManager.NeutralGrey, // Example for hint color (neutral grey)
    lineHeight = 20.sp,
    fontWeight = FontWeight.Normal
)

