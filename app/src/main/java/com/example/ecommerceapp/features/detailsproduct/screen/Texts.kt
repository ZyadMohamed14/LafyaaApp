//package com.example.ecommerceapp.features.detailsproduct.screen
//
//import android.util.Log
//import com.example.ecommerceapp.ui.detailsproduct.data.ColorStock
//import com.example.ecommerceapp.ui.detailsproduct.data.ProductModel
//import com.example.ecommerceapp.ui.detailsproduct.data.Size
//import com.google.firebase.firestore.FirebaseFirestore
//
//val producsImages = listOf(
//    "https://firebasestorage.googleapis.com/v0/b/onsale-7f4cb.appspot.com/o/airmax270(4).jpg?alt=media&token=59368014-c0ec-4d31-8641-436a3b19897e",
//    "https://firebasestorage.googleapis.com/v0/b/onsale-7f4cb.appspot.com/o/airmax270(3).jpg?alt=media&token=6bfd94ac-f84e-4abb-890c-448a6a065a7e",
//    "https://firebasestorage.googleapis.com/v0/b/onsale-7f4cb.appspot.com/o/airmax270(2).jpg?alt=media&token=05fac0de-d164-4a04-9a82-f58c5630b701",
//    "https://firebasestorage.googleapis.com/v0/b/onsale-7f4cb.appspot.com/o/airmax270(1).jpg?alt=media&token=f2bb9a2b-cc6b-4296-ad90-35b65a05cac3",
//    "https://firebasestorage.googleapis.com/v0/b/onsale-7f4cb.appspot.com/o/Nike%20Air%20Max%20270%20React%20ENG.png?alt=media&token=d95fe052-8aab-4408-8ee3-0bddb9342f26"
//)
//val colors = emptyList<String>()
//
//fun addData() {
//    val firestore = FirebaseFirestore.getInstance()
//    val productRef = firestore.collection("products").document() // Auto-generated ID
//    val productId = productRef.id // Get the generated document ID
//    val productData = ProductModel(
//        id = productId,
//        name = "Nike Air Max 270 React (GS) Big Kids' Shoes",
//        description = "DESCRIPTION\n" +
//                "Nike Air Max 270 React (GS) Big Kids' Shoes React Blue Void-Magic Ember\n" +
//                "Cross Training, Walking, Running & Jogging\n" +
//                "Rubber\n" +
//                "Lace Up\n" +
//                "100% Authentic\n" +
//                "Imported\n" +
//                "Style Number: bq0103-402\n" +
//                "Condition: New",
//        category = "Men's Shoes", // Example
//        images = producsImages,
//        image= "https://firebasestorage.googleapis.com/v0/b/onsale-7f4cb.appspot.com/o/Nike%20Air%20Max%20270%20React%20ENG.png?alt=media&token=d95fe052-8aab-4408-8ee3-0bddb9342f26",
//        price = 120.0,
//        rate = 4.8f,
//        salePercentage = 40,
//        saleType = "mega_sale",
//        colors = listOf("#223263", "#40BFFF", "#FB7181"),
//        sizes = mapOf(
//            "36" to Size(
//                colors = listOf(
//                    ColorStock(colorKey = "#223263", inStock = 15),
//                    ColorStock(colorKey = "#40BFFF", inStock = 13),
//                    ColorStock(colorKey = "#FB7181", inStock = 14)
//                )
//            ),
//            "37" to Size(
//                colors = listOf(
//                    ColorStock(colorKey = "#223263", inStock = 10),
//                    ColorStock(colorKey = "#40BFFF", inStock = 8),
//                    ColorStock(colorKey = "#FB7181", inStock = 9)
//                )
//            ),
//            "38" to Size(
//                colors = listOf(
//                    ColorStock(colorKey = "#223263", inStock = 10),
//                    ColorStock(colorKey = "#40BFFF", inStock = 8),
//                    ColorStock(colorKey = "#FB7181", inStock = 9)
//                )
//            )
//        ),
//
//
//        )
//
//
//    // Adding the data to Firestore
//    productRef.set(productData)
//        .addOnSuccessListener {
//            Log.d("Firestore", "Product successfully written with ID: $productId")
//        }
//        .addOnFailureListener { e ->
//            Log.w("Firestore", "Error writing product", e)
//        }
//}