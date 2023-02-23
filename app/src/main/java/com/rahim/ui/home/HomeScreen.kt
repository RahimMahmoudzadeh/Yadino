package com.rahim.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Scaffold
import androidx.compose.material.Shapes
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rahim.R
import com.rahim.ui.theme.YadinoTheme
import com.rahim.ui.theme.ZIRCON

@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Scaffold(
        topBar = {
            TopBar(
                modifier
                    .background(ZIRCON)
            )
        },
        backgroundColor = Color.White
    ) {
        Column(modifier = modifier) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    text = "1402/1/1",
                    fontSize = 18.sp
                )
                Text(
                    text = stringResource(id = R.string.list_work_day),
                    fontSize = 18.sp
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {

                Image(
                    modifier = Modifier
                        .sizeIn(minHeight = 320.dp)
                        .fillMaxWidth(),
                    painter = painterResource(id = R.drawable.empty_list_home),
                    contentDescription = "empty list home"
                )
                Text(
                    text = stringResource(id = R.string.not_work_for_day),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp),
                    textAlign = TextAlign.Center, fontSize = 18.sp
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        modifier = modifier.background(ZIRCON),
        title = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                textAlign = TextAlign.End,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                text = stringResource(id = R.string.hello_friend),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    )
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, device = Devices.PIXEL_4)
@Composable
fun HomeScreenPreview() {
    YadinoTheme() {
        HomeScreen()
    }
}