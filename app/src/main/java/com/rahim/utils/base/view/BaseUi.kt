package com.rahim.utils.base.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rahim.ui.theme.Purple
import com.rahim.ui.theme.PurpleGrey

val gradientColors = listOf(Purple, PurpleGrey)

@Composable
fun GradientButton(
    text: String,
    gradient: Brush,
    modifier: Modifier = Modifier,
    textSize: TextUnit,
    onClick: () -> Unit = { },
) {
    Button(
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        contentPadding = PaddingValues(),
        modifier = modifier,
        onClick = { onClick() }
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, fontSize = textSize)
        }
    }
}

@Preview
@Composable
fun GradientButtonPreview() {
    GradientButton(
        text = "شروع",
        gradient = Brush.horizontalGradient(gradientColors),
        modifier = Modifier
            .width(150.dp)
            .wrapContentHeight(),
        textSize = 14.sp
    )
}
