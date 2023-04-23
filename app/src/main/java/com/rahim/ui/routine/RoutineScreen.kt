package com.rahim.ui.routine

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rahim.R
import com.rahim.data.modle.Rotin.Routine
import com.rahim.ui.home.ItemRoutine
import com.rahim.ui.theme.YadinoTheme
import com.rahim.ui.theme.Zircon
import com.rahim.utils.base.view.TopBarCenterAlign
import com.rahim.utils.base.view.gradientColors

@Composable
fun RoutineScreen(modifier: Modifier = Modifier) {
    var dayChecked by rememberSaveable { mutableStateOf("1") }

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
            ItemsRoutine(it, dayChecked, dayCheckedNumber = {
                dayChecked = it
            })
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
fun ItemsRoutine(
    paddingValues: PaddingValues,
    dayChecked: String,
    dayCheckedNumber: (String) -> Unit
) {
//    Text(text = "اسفند 1400")
//    Row(modifier = Modifier.padding(top = 12.dp)) {
//        IconButton(modifier = Modifier
//            .weight(1f)
//            .padding(top = 17.dp), onClick = {}) {
//            Icon(painterResource(id = R.drawable.less_then), contentDescription = "less then sign")
//        }
//        Spacer(modifier = Modifier.width(12.dp))
//        for (item in items.size - 1 downTo (0)) {
//            Column(
//                horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier
//                    .weight(8f)
//                    .padding(end = 15.dp)
//            ) {
//                Text(
//                    fontSize = 9.sp,
//                    text = items[item]
//                )
//                Box(
//                    modifier = Modifier
//                        .padding(
//                            top = 14.dp,
//                            start = 4.dp
//                        )
//                        .size(29.dp)
//                        .clip(CircleShape)
//                        .background(
//                            brush = if (dayChecked == item.toString()) {
//                                Brush.verticalGradient(
//                                    gradientColors
//                                )
//                            } else Brush.horizontalGradient(
//                                listOf(
//                                    Color.White,
//                                    Color.White
//                                )
//                            )
//                        )
//                ) {
//                    ClickableText(
//                        modifier = Modifier.padding(
//                            top = 8.dp, start =12.dp
//                        ),
//                        onClick = { dayCheckedNumber(item.toString()) },
//                        text = AnnotatedString(item.toString()),
//                        style = TextStyle(
//                            fontSize = 9.sp, fontWeight = FontWeight.Bold,
//                            color = if (dayChecked == item.toString())
//                                (Color.White)
//                            else Color.Black
//                        )
//                    )
//                }
//            }
//        }
//        IconButton(modifier = Modifier
//            .weight(1f)
//            .padding(top = 17.dp), onClick = {}) {
//            Icon(
//                painterResource(id = R.drawable.greater_then),
//                contentDescription = "greater then sign"
//            )
//        }
//    }
//    LazyColumn(
//        modifier = Modifier
//            .fillMaxWidth()
//            .padding(paddingValues),
//        contentPadding = PaddingValues(top = 8.dp)
//    ) {
//        items(
//            items = routine, itemContent = {
//                ItemRoutine(routine = it, onChecked = {
//                    updateRoutine(it)
//                }, openDialogDelete = {
//                    deleteRoutine(it)
//                }, openDialogEdit = {
//                    updateRoutine(it)
//                })
//            }
//        )
//    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, device = Devices.PIXEL_4)
@Composable
fun RoutineScreenPreview() {
    YadinoTheme() {
        RoutineScreen()
    }
}