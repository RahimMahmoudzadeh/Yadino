package com.rahim.ui.dialog

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.rahim.R
import com.rahim.ui.theme.YadinoTheme
import com.rahim.utils.base.view.DialogButtonBackground
import com.rahim.utils.base.view.gradientColors

@Composable
fun DialogUpdateVersion(
    isForce: Boolean,
    @StringRes messageUpdate: Int = R.string.update_app_version,
    @StringRes successBtn: Int = R.string.update,
    onUpdate: () -> Unit,
    onDismiss: () -> Unit,
    onDismissRequest: () -> Unit
) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
        Dialog(onDismissRequest =onDismissRequest) {
            Surface(
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.background
            ) {
                Column(modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp)) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.mipmap.ic_launcher_foreground),
                            contentDescription = "app icon"
                        )
                        Text(
                            text = stringResource(id = messageUpdate),
                            textAlign = TextAlign.Center,
                            style = TextStyle(color = MaterialTheme.colorScheme.primary),
                            fontSize = 16.sp
                        )
                    }
                    Row(
                        modifier = Modifier.padding(top = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DialogButtonBackground(
                            text = stringResource(id = successBtn),
                            gradient = Brush.verticalGradient(
                                gradientColors
                            ),
                            textSize = 12.sp,
                            modifier = Modifier
                                .fillMaxWidth(0.3f)
                                .height(35.dp),
                            onClick = onUpdate,
                           textStyle =  MaterialTheme.typography.bodyMedium
                        )
                        if (!isForce) {
                            TextButton(
                                modifier = Modifier.padding(start = 8.dp),
                                onClick = onDismiss
                            ) {
                                Text(
                                    fontSize = 12.sp,
                                    text = stringResource(id = R.string.cancel),
                                    style =MaterialTheme.typography.bodyMedium.copy(brush = Brush.verticalGradient(gradientColors))
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(name = "dark", uiMode = Configuration.UI_MODE_NIGHT_YES)
@Preview(name = "light")
@Preview(name = "device size", device = Devices.PIXEL_4_XL)
@Composable
fun PreviewDialogUpdateVersionLight() {
    YadinoTheme {
        DialogUpdateVersion(
            messageUpdate = R.string.update_app_version,
            successBtn = R.string.update,
            isForce = false,
            onUpdate = {},
            onDismiss = {},
            onDismissRequest = {})
    }
}