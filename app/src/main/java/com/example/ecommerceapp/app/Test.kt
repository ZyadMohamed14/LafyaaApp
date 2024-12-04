package com.example.ecommerceapp.app

import android.util.Log
import com.example.ecommerceapp.features.dashboard.home.data.products.ProductModel
import com.example.ecommerceapp.features.dashboard.home.data.products.Size
import com.example.ecommerceapp.features.dashboard.home.data.products.Stock
import com.google.firebase.firestore.FirebaseFirestore


/*
    val product = ProductModel(
                id = null, // Firestore will generate an ID
                name = "Hackett - slim fit 5-pocket jeans - men",
                description = "As comfy as they are cool, these sweatpants are ideal for everything – from warm-ups to chillouts.",
                category = "Men's Pants",
                images = listOf(
                    "https://firebasestorage.googleapis.com/v0/b/onsale-7f4cb.appspot.com/o/hacklet3.webp?alt=media&token=454a9eb3-ed81-4736-a993-fd6c9b9a4df6",
                    "https://firebasestorage.googleapis.com/v0/b/onsale-7f4cb.appspot.com/o/hacklet2.webp?alt=media&token=830772ec-6359-4e7c-90bb-696eb7d58de3",
                    "https://firebasestorage.googleapis.com/v0/b/onsale-7f4cb.appspot.com/o/hacklet.webp?alt=media&token=f0598b0e-98aa-4b4c-b7e1-1f56eed711fc",
                ),
                image = "https://firebasestorage.googleapis.com/v0/b/onsale-7f4cb.appspot.com/o/hacklet.webp?alt=media&token=f0598b0e-98aa-4b4c-b7e1-1f56eed711fc",
                price = 500.0,
                rate = 4.8f,
                salePercentage = 40,
                saleType = "mega_sale",
                // colors = listOf("#FFFFFF", "#FFA500", "#000000"),
                sizes = mapOf(
                    "31" to Size(
                        colors = listOf(
                            Stock(10, "#FFFFFF"),
                            Stock(5, "#FFA500"),
                            Stock(8, "#000000")
                        )
                    ),
                    "34" to Size(
                        colors = listOf(
                            Stock(15, "#FFFFFF"),
                            Stock(7, "#FFA500"),
                            Stock(10, "#000000")
                        )
                    ),
                    "38" to Size(
                        colors = listOf(
                            Stock(12, "#FFFFFF"),
                            Stock(6, "#FFA500"),
                            Stock(9, "#000000")
                        )
                    ),
                    "40" to Size(
                        colors = listOf(
                            Stock(9, "#FFFFFF"),
                            Stock(4, "#FFA500"),
                            Stock(11, "#000000")
                        )
                    ),
                    "41" to Size(
                        colors = listOf(
                            Stock(13, "#FFFFFF"),
                            Stock(8, "#FFA500"),
                            Stock(6, "#000000")
                        )
                    )
                )
            )
 */
fun addProductToFirestore(
    product: ProductModel,
    firestore: FirebaseFirestore,
    onSuccess: () -> Unit,
    onFailure: (Exception) -> Unit
) {
    // Reference to the Firestore collection
    val id = firestore.collection("products").document().id
    product.id = id
    val productsCollection = firestore.collection("products")

    // Add the product to Firestore
    productsCollection.add(product)
        .addOnSuccessListener {
            Log.d("Firestore", "Product added successfully with ID: ${it.id}")
            onSuccess()
        }
        .addOnFailureListener { exception ->
            Log.e("Firestore", "Failed to add product: ${exception.message}", exception)
            onFailure(exception)
        }
}

// Function to update sizes in Firebase
fun updateProductSizes(productId: String, firestore: FirebaseFirestore) {
    // Mock stock data for each color and size
    val stockData = listOf(
        Stock(inStock = 10, colorKey = "#FFFFFF"),
        Stock(inStock = 5, colorKey = "#FFA500"),
        Stock(inStock = 15, colorKey = "#000000")
    )

    // Create sizes data
    val sizesData = mapOf(
        "XS" to Size(colors = stockData),
        "S" to Size(colors = stockData),
        "M" to Size(colors = stockData),
        "L" to Size(colors = stockData),
        "XL" to Size(colors = stockData)
    )

    // Reference to the product document in Firestore
    val productRef = firestore.collection("products").document(productId)

    // Update the sizes field
    productRef.update("sizes", sizesData)
        .addOnSuccessListener {
            Log.d("FirebaseUpdate", "Sizes updated successfully for product: $productId")
        }
        .addOnFailureListener { e ->
            Log.e("FirebaseUpdate", "Failed to update sizes for product: $productId", e)
        }
}

fun updateAllProductsSalePercentageToNull(firestore: FirebaseFirestore, onComplete: (Boolean) -> Unit) {
    val productsCollection = firestore.collection("products")

    // Retrieve all documents in the collection
    productsCollection.get()
        .addOnSuccessListener { querySnapshot ->
            val batch = firestore.batch() // Use a batch for efficiency

            for (document in querySnapshot.documents) {
                val docRef = productsCollection.document(document.id)
                batch.update(docRef, "sale_type", "") // Set `sale_percentage` to `null`
            }

            // Commit the batch update
            batch.commit()
                .addOnSuccessListener {
                    Log.d("FirestoreUpdate", "Successfully updated sale_percentage to null for all products.")
                    onComplete(true)
                }
                .addOnFailureListener { exception ->
                    Log.e("FirestoreUpdate", "Failed to update sale_percentage: ${exception.message}", exception)
                    onComplete(false)
                }
        }
        .addOnFailureListener { exception ->
            Log.e("FirestoreQuery", "Failed to retrieve products: ${exception.message}", exception)
            onComplete(false)
        }
}
