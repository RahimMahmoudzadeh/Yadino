package com.rahim.ui.dialog

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rahim.R
import com.rahim.ui.theme.Purple
import com.rahim.ui.theme.PurpleGrey
import com.rahim.ui.theme.YadinoTheme
import com.rahim.ui.welcome.Welcome
import com.rahim.utils.base.view.DialogButton

@OptIn(ExperimentalMaterial3Api::class, ExperimentalTextApi::class)
@Composable
fun DialogAddRoutine(
    modifier: Modifier = Modifier,
    isOpen: Boolean,
    openDialog: () -> Unit
) {
    val text = rememberSaveable { mutableStateOf("") }
    val gradientColors = listOf(Purple, PurpleGrey)

    if (isOpen) {
        AlertDialog(modifier = modifier
            .fillMaxWidth()
            .padding(0.dp),
            onDismissRequest = {
                openDialog()
            }
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(percent = 6)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        text = "ایجاد کار جدید",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = TextStyle(brush = Brush.verticalGradient(gradientColors))
                    )
                    OutlinedTextField(
                        modifier = Modifier.padding(top = 26.dp),
                        value = text.value,
                        onValueChange = { text.value = it },
                        label = { Text(stringResource(id = R.string.name)) },
                        placeholder = { Text(text = stringResource(id = R.string.name_hint_text_filed_routine)) },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Purple,
                            focusedLabelColor = Purple
                        )
                    )
                    OutlinedTextField(
                        modifier = Modifier.padding(top = 20.dp),
                        value = text.value,
                        onValueChange = { text.value = it },
                        label = { Text("Label") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Purple,
                            focusedLabelColor = Purple
                        )
                    )
                    OutlinedTextField(
                        modifier = Modifier.padding(top = 20.dp),
                        value = text.value,
                        onValueChange = { text.value = it },
                        label = { Text("Label") },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            focusedBorderColor = Purple,
                            focusedLabelColor = Purple,
                            cursorColor = Purple
                        )
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                    Row(modifier = Modifier.fillMaxWidth(1f)) {
                        DialogButton(
                            text = stringResource(id = R.string.confirmation),
                            gradient = Brush.verticalGradient(gradientColors),
                            modifier = Modifier,
                            textSize = 14.sp,
                            onClick = openDialog
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        TextButton(onClick = openDialog) {
                            Text(text = stringResource(id = R.string.cancel))
                        }
                    }

                }
            }
        }
    }

}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF, device = Devices.PIXEL_4)
@Composable
private fun WelcomePreview1() {
    YadinoTheme() {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            DialogAddRoutine(isOpen = true) {
            }
        }
    }
}