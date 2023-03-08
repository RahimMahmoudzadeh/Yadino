package com.rahim.ui.note

import android.os.Build
import androidx.annotation.RequiresApi
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
import com.rahim.data.modle.note.NoteModel
import com.rahim.ui.theme.YadinoTheme
import com.rahim.ui.theme.Zircon
import com.rahim.utils.base.view.TopBarCenterAlign

@Composable
fun NoteScreen(modifier: Modifier = Modifier) {

    Scaffold(
        modifier = modifier.background(Zircon),
        topBar = {
            TopBarCenterAlign(
                modifier, stringResource(id = R.string.notes)
            )
        }, backgroundColor = Color.White
    ) {
        EmptyNote(it)
//            ItemsHome(it)

    }
}

@Composable
fun EmptyNote(paddingValues: PaddingValues) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .padding(end = 16.dp, start = 16.dp, top = 25.dp)
    ) {

        Image(
            modifier = Modifier
                .sizeIn(minHeight = 320.dp)
                .fillMaxWidth(),
            painter = painterResource(id = R.drawable.empty_note),
            contentDescription = "empty list home"
        )
        Text(
            text = stringResource(id = R.string.not_note),
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 32.dp),
            textAlign = TextAlign.Center,
            fontSize = 18.sp
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemsNote(paddingValues: PaddingValues) {
    var noteName = rememberSaveable { mutableStateOf("") }

    val note =
        remember {
            listOf(
                NoteModel(
                    0,
                    "rahim",
                    "adasdadsadadadfsdfsdfsjkfdsljldkjfglkjdhglkjdfhglkjdfhglkjdffgljkdfgldjkfgldkjgdlkjgdhlkjghjcbnklvbvcmn",
                    "1402/1/1",
                    false,
                    0
                ),
                NoteModel(
                    0,
                    "rahim",
                    "adasdadsadadadfsdfsdfsjkfdsljldkjfglkjdhglkjdfhglkjdfhglkjdffgljkdfgldjkfgldkjgdlkjgdhlkjghjcbnklvbvcmn",
                    "1402/1/1",
                    false,
                    0
                ),
                NoteModel(
                    0,
                    "rahim",
                    "adasdadsadadadfsdfsdfsjkfdsljldkjfglkjdhglkjdfhglkjdfhglkjdffgljkdfgldjkfgldkjgdlkjgdhlkjghjcbnklvbvcmn",
                    "1402/1/1",
                    false,
                    0
                )
            )
        }
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(paddingValues).padding(end = 16.dp, start = 16.dp, top = 25.dp),
        contentPadding = PaddingValues(top = 25.dp)
    ) {
        items(
            items = note, itemContent = {
                ItemListNote(noteModel = it, onChecked = {

                }, noteName = {
                    noteName.value = it
                })
            }
        )
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true, backgroundColor = 0xFFFFFF, device = Devices.PIXEL_4)
@Composable
fun NoteScreenPreview() {
    YadinoTheme() {
        NoteScreen()
    }
}