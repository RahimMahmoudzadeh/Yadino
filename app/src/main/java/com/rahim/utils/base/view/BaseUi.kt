package com.rahim.utils.base.view

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.rahim.R
import com.rahim.ui.theme.CornflowerBlueLight
import com.rahim.ui.theme.Purple
import com.rahim.ui.theme.PurpleGrey
import com.rahim.utils.resours.Resource
import timber.log.Timber

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
            Text(text = text, fontSize = textSize, color = Color.White)
        }
    }
}

@Composable
fun DialogButtonBackground(
    text: String,
    gradient: Brush,
    modifier: Modifier = Modifier,
    textSize: TextUnit,
    textStyle: TextStyle = TextStyle(),
    onClick: () -> Unit = { },
) {

    Button(
        colors = ButtonDefaults.buttonColors(Color.Transparent),
        contentPadding = PaddingValues(),
        modifier = modifier,
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
                style = textStyle,
                modifier = Modifier.padding(end = 4.dp, start = 4.dp),
                color = Color.White
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
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(end = 4.dp, start = 4.dp),
            )
        }
    }
}

@Composable
fun CircularProgressAnimated(isShow: Boolean) {
    if (isShow) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(color = CornflowerBlueLight)
        }
    }
}

@Composable
fun <T>ProcessRoutineAdded(
    response: Resource<T?>?,
    context: Context,
    closeDialog: (responseData:T?) -> Unit
) {
    Timber.tag("routineAdd")
        .d("ProcessRoutineAdded ->${if (response is Resource.Success) "success" else if (response is Resource.Error) "fail" else "loading"}")
    response?.let {
        when (response) {
            is Resource.Loading -> {
                CircularProgressAnimated(true)
            }

            is Resource.Success -> {
                CircularProgressAnimated(false)
                closeDialog(response.data)
            }

            is Resource.Error -> {
                CircularProgressAnimated(false)
                ShowToastShort(response.message, context)
                closeDialog(response.data)
            }
        }
    }
}

@Composable
fun ShowToastShort(message: String?, context: Context) {
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Bottom) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarCenterAlign(modifier: Modifier = Modifier, title: String, onClickSearch: () -> Unit) {

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(MaterialTheme.colorScheme.onBackground),
        modifier = modifier.shadow(elevation = 8.dp),
        title = {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 38.dp),
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.secondary
            )
        },
        actions = {
            IconButton(onClick = {
                onClickSearch()
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.round_search),
                    contentDescription = "search"
                )
            }
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

@Preview
@Composable
fun DialogButtonBackgroundWrapper() {
    DialogButtonBackground(
        "انتخاب",
        gradient = Brush.horizontalGradient(gradientColors),
        modifier = Modifier
            .width(150.dp)
            .wrapContentHeight(),
        textSize = 14.sp,
        textStyle = TextStyle(fontWeight = FontWeight.Bold),
        onClick = {}
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

@Composable
fun ShowStatusBar(isShow: Boolean) {
    rememberSystemUiController().apply {
        isStatusBarVisible = isShow
        isNavigationBarVisible = isShow
        isSystemBarsVisible = isShow
    }
}

@Composable
fun ShowSearchBar(
    clickSearch: Boolean,
    searchText: String,
    searchValueText: (String) -> Unit
) {
    AnimatedVisibility(visible = clickSearch) {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
            TextField(
                modifier = Modifier
                    .fillMaxWidth(),
                label = { Text(text = stringResource(id = R.string.search_hint)) },
                value = searchText,
                onValueChange = { searchValueText(it) },
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedContainerColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedIndicatorColor = PurpleGrey,
                    focusedIndicatorColor = Purple,
                    disabledIndicatorColor = Color.Transparent,
                )
            )
        }
    }
}


