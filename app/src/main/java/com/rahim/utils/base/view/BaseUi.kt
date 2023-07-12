package com.rahim.utils.base.view

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.rahim.ui.dialog.DialogAddNote
import com.rahim.ui.dialog.DialogAddRoutine
import com.rahim.ui.theme.Purple
import com.rahim.ui.theme.PurpleGrey
import com.rahim.ui.theme.Zircon
import com.rahim.utils.navigation.Screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

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
        onClick = { onClick() },
        shape = RoundedCornerShape(16)
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(text = text, fontSize = textSize, style = TextStyle(color = Color.White))
        }
    }
}

@Composable
fun DialogButtonBackground(
    text: String,
    gradient: Brush,
    modifier: Modifier = Modifier,
    textSize: TextUnit,
    width: Float,
    height: Dp,
    onClick: () -> Unit = { },
) {
    Button(
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        contentPadding = PaddingValues(),
        modifier = modifier
            .fillMaxWidth(width)
            .height(height),
        onClick = { onClick() },
        shape = RoundedCornerShape(12)
    ) {
        Box(
            modifier = Modifier
                .background(gradient)
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = textSize,
                modifier = Modifier.padding(end = 4.dp, start = 4.dp),
                style = TextStyle(color = Color.White)
            )
        }
    }
}

@Composable
fun DialogButtonBorder(
    text: String,
    gradient: Brush,
    modifier: Modifier = Modifier,
    textSize: TextUnit,
    width: Float,
    height: Dp,
    onClick: () -> Unit = { },
) {
    Button(
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        contentPadding = PaddingValues(),
        modifier = modifier
            .fillMaxWidth(width)
            .height(height),
        onClick = { onClick() },
        shape = RoundedCornerShape(12)
    ) {
        Box(
            modifier = Modifier
                .border(1.dp, brush = gradient, RoundedCornerShape(12))
                .padding(vertical = 10.dp)
                .fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = text,
                fontSize = textSize,
                color = Color.Black,
                modifier = Modifier.padding(end = 4.dp, start = 4.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarRightAlign(modifier: Modifier = Modifier, title: String) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(MaterialTheme.colorScheme.onBackground),
        modifier = modifier.shadow(elevation = 8.dp),
        title = {
            androidx.compose.material.Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                textAlign = TextAlign.End,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(color = MaterialTheme.colorScheme.secondary)
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarCenterAlign(modifier: Modifier = Modifier, title: String) {
    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(MaterialTheme.colorScheme.onBackground),
        modifier = modifier.shadow(elevation = 8.dp),
        title = {
            androidx.compose.material.Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = TextStyle(color = MaterialTheme.colorScheme.secondary)
            )
        }
    )
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

fun calculateMinute(timeHours: String): Int {
    val index = timeHours.indexOf(':')
    return timeHours.subSequence(index.plus(1), timeHours.length).toString().toInt()
}

fun calculateHours(timeHours: String): Int {
    val index = timeHours.indexOf(':')
    return timeHours.subSequence(0, index).toString().toInt()
}

@OptIn(ExperimentalPermissionsApi::class)
fun requestPermissionNotification(
    notificationPermission: PermissionState,
    isGranted: (Boolean) -> Unit,
    permissionState: (PermissionState) -> Unit
) {

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        if (notificationPermission.status.isGranted) {
            isGranted(true)
        } else {
            if (notificationPermission.status.shouldShowRationale) {
                isGranted(false)
            } else {
                permissionState(notificationPermission)
            }
        }
    } else {
        isGranted(true)
    }
}

fun goSettingPermission(context: Context) {
    val intent = Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", context.packageName, null)
    )
    ContextCompat.startActivity(context, intent, null)
}

