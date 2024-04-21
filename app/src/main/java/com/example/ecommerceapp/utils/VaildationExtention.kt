package com.example.ecommerceapp.utils

fun String.isVaildEamil():Boolean{
    return android.util.Patterns.EMAIL_ADDRESS.matcher(this).matches()
}