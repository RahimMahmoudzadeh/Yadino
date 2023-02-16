package com.rahim.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.rahim.R
import com.rahim.ui.theme.YadinoTheme
import com.rahim.ui.theme.ZIRCON

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Scaffold(topBar = { TopBar(modifier.background(ZIRCON)) }, backgroundColor = Color.White) {
        Column(modifier = modifier.padding(it)) {
            Text(text = "sadaa")
        }
    }
}

@Composable
fun TopBar(modifier: Modifier = Modifier) {
    Row(modifier = modifier) {
        Text(text = stringResource(id = R.string.hello_friend))
    }
}