package com.example.ecommerceapp.features.dashboard.account.order.ui.component

import android.graphics.Paint
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ecommerceapp.core.theme.BodyTextNormalRegular
import com.example.ecommerceapp.core.theme.ColorsManager

@Composable
fun OrderProgressState(currentStep: String) {
    var currentStepInt: Int=0
    val deliveredSteps = listOf("Created", "Picked","Out for Delivery", "Delivered")
    val cancelledSteps = listOf("Created", "Picked","Out for Delivery", "Cancelled")
    val steps = mutableListOf<String>()

    when(currentStep){
        "Created", "Picked", "Out for Delivery", "Delivered" -> {
            steps.addAll(deliveredSteps)
            currentStepInt = steps.indexOf(currentStep)
        }
        "Cancelled" -> {
            steps.addAll(cancelledSteps)
            currentStepInt = steps.indexOf(currentStep)

        }
    }
    Log.d("sdjsdjsdosdsdosods",steps.toString())


    var lineColor = ColorsManager.PrimaryColor // init value
    var circleColor = ColorsManager.PrimaryColor // init value
    val activeColor = ColorsManager.PrimaryColor // Static green for active
    val inactiveColor = ColorsManager.NeutralLight // Static light gray for inactive
    val cancelColor = ColorsManager.PrimaryRed
    val textActiveColor = Color.Black
    val textInactiveColor = Color.Gray
    val circleRadius = 20.dp
    val lineThickness = 4.dp

    val circleRadiusPx = with(LocalDensity.current) { circleRadius.toPx() }
    val lineThicknessPx = with(LocalDensity.current) { lineThickness.toPx() }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)

    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp,end = 15.dp)
                .height(80.dp)
        ) {
            val totalWidth = size.width
            val stepSpacing = totalWidth / (steps.size - 1)

            // Draw connecting lines
            for (i in 0 until steps.size - 1) {
                val startX = i * stepSpacing
                val endX = (i + 1) * stepSpacing
                val centerY = size.height / 2

                when(steps[i]){
                    "Created", "Picked", "Out for Delivery", "Delivered" ->{
                        if(i<currentStepInt){
                            lineColor = activeColor
                            circleColor = activeColor
                        }else{
                            lineColor = inactiveColor
                            circleColor = inactiveColor
                        }

                    }
                    "Cancelled" -> {
                        lineColor = cancelColor
                        circleColor = cancelColor
                    }

                }

                drawLine(
                    color = lineColor,
                    start = Offset(startX + circleRadiusPx, centerY),
                    end = Offset(endX - circleRadiusPx, centerY),
                    strokeWidth = lineThicknessPx
                )
            }

            // Draw circles
            steps.forEachIndexed { index, _ ->
                val centerX = index * stepSpacing
                val centerY = size.height / 2

                when(steps[index]){
                    "Created", "Picked", "Out for Delivery", "Delivered" ->{
                        circleColor = if(index<=currentStepInt){

                            activeColor
                        }else{

                            inactiveColor
                        }

                    }
                    "Cancelled" -> {

                        circleColor = cancelColor
                    }

                }
                drawCircle(
                    color = circleColor,
                    radius = circleRadiusPx,
                    center = Offset(centerX, centerY)
                )

                // Draw check icon
                //  if (index <= currentStep) {
                drawContext.canvas.nativeCanvas.apply {
                    val paint = Paint().apply {
                        color =  android.graphics.Color.WHITE
                        textSize = circleRadiusPx * 1.2f
                        textAlign = Paint.Align.CENTER
                        isFakeBoldText = true
                    }
                    drawText(
                        if(steps[index]!="Cancelled") "✓" else "x",
                        centerX,
                        centerY + (paint.textSize / 3),
                        paint
                    )
                }
                //  }
            }
        }

        // Text labels below
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            steps.forEachIndexed { index, step ->
                Text(
                    text = step,
                    style = BodyTextNormalRegular,
                    color = if (index <= currentStepInt) textActiveColor else textInactiveColor,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}