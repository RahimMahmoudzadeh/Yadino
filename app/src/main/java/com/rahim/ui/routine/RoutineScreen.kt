package com.rahim.ui.routine

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rahim.R
import com.rahim.data.modle.Rotin.Routine
import com.rahim.ui.home.ItemHome
import com.rahim.ui.theme.YadinoTheme
import com.rahim.ui.theme.Zircon
import com.rahim.utils.base.view.TopBarCenterAlign

@Composable
fun RoutineScreen(modifier: Modifier = Modifier) {

    Scaffold(
        modifier = modifier.background(Zircon),
        topBar = {
            TopBarCenterAlign(
                modifier, stringResource(id = R.string.list_routine)
            )
        }, backgroundColor = Color.White
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(end = 16.dp, start = 16.dp, top = 25.dp)
        ) {
//            EmptyRoutine(it)
            ItemsRoutine(it)
        }
    }
}

@Composable
fun EmptyRoutine(paddingValues: PaddingValues) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
    ) {

        Image(
            modifier = Modifier
                .sizeIn(minHeight = 320.dp)
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.routine_empty),
            contentDescription = "empty list home"
        )
        Text(
            text = stringResource(id = R.string.not_routine),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 26.dp),
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
    }
}

@Composable
fun ItemsRoutine(paddingValues: PaddingValues) {
    var routineName = rememberSaveable { mutableStateOf("") }
    val items = listOf("شنبه", "یکشنبه", "دوشنبه", "سه شنبه", "چهار شنبه", "پنج شنبه", "جمعه")
    val routine =
        remember {
            listOf(
                Routine("قراره کاری", null, null, null, null, null, false, null),
                Routine("قراره کاری2", null, null, null, null, null, true, null),
                Routine("قراره کاری3", null, null, null, null, null, false, null)
            )
        }
    Text(text = "اسفند 1400")
    Row(modifier = Modifier.padding(end = 6.dp,top=16.dp)) {
        for (item in items.size-1 downTo (0)) {
            Text(modifier = Modifier.padding(start = 14.dp),text = items[item])
        }
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues),
        contentPadding = PaddingValues(top = 25.dp)
    ) {
        items(
            items = routine, itemContent = {
                ItemHome(routine = it, onChecked = {

                }, routineName = {
                    routineName.value = it
                })
            }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, device = Devices.PIXEL_4)
@Composable
fun RoutineScreenPreview() {
    YadinoTheme() {
        RoutineScreen()
    }
}