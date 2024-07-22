package com.rahim.utils.base.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.LocalView
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
import androidx.core.graphics.luminance
import androidx.core.view.WindowCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.PermissionState
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.shouldShowRationale
import com.rahim.R
import com.rahim.data.modle.data.TimeDate
import com.rahim.ui.theme.BalticSea
import com.rahim.ui.theme.CornflowerBlueLight
import com.rahim.ui.theme.Periwinkle
import com.rahim.ui.theme.Purple
import com.rahim.ui.theme.PurpleGrey
import com.rahim.ui.theme.Zircon
import com.rahim.utils.enums.HalfWeekName
import com.rahim.utils.extention.errorMessage
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
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
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
fun <T> ProcessRoutineAdded(
    response: Resource<T?>?,
    context: Context,
    closeDialog: (responseData: T?) -> Unit
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
                ShowToastShort(response.message?.errorMessage(context), context)
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
fun TopBarCenterAlign(
    modifier: Modifier = Modifier,
    title: String,
    openHistory: () -> Unit,
    isShowSearchIcon: Boolean,
    isShowBackIcon: Boolean,
    onClickSearch: () -> Unit,
    onClickBack: () -> Unit,
    onDrawerClick: () -> Unit
) {

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(MaterialTheme.colorScheme.onBackground),
        modifier = modifier.shadow(elevation = 8.dp),
        title = {
            Text(
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold,
                text = title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                color = MaterialTheme.colorScheme.secondary
            )
        },
        navigationIcon = {
            Row {
                IconButton(onClick = onDrawerClick) {
                    Icon(
                        imageVector = Icons.Rounded.MoreVert,
                        contentDescription = null,
                    )
                }
                if (!isShowBackIcon) {
                    IconButton(onClick = { openHistory() }) {
                        Icon(
                            imageVector = Icons.Rounded.Notifications,
                            contentDescription = "",
                            tint = CornflowerBlueLight,
                        )
                    }
                }


            }

        },
        actions = {
            if (isShowSearchIcon) {
                IconButton(onClick = {
                    onClickSearch()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.round_search),
                        contentDescription = "search"
                    )
                }
            } else if (isShowBackIcon) {
                IconButton(onClick = {
                    onClickBack()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.greater_then),
                        contentDescription = "search"
                    )
                }
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

@Composable
fun EmptyMessage(
    modifier: Modifier = Modifier,
    @StringRes messageEmpty: Int = R.string.not_work_for_day,
    @DrawableRes painter: Int = R.drawable.empty_list_home
) {
    Image(
        modifier = modifier
            .sizeIn(minHeight = 320.dp)
            .fillMaxWidth()
            .fillMaxHeight(0.8f)
            .padding(10.dp),
        alignment = Alignment.Center,
        painter = painterResource(id = painter),
        contentDescription = "empty list home"
    )
    Text(
        text = stringResource(id = messageEmpty),
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 22.dp),
        textAlign = TextAlign.Center,
        fontSize = 18.sp,
        color = MaterialTheme.colorScheme.primary
    )
}

@Composable
fun TimeItems(
    timeDate: TimeDate,
    dayNumberChecked: Int,
    monthNumberChecked: Int,
    yerNumberChecked: Int,
    dayCheckedNumber: (yer: Int, month: Int, day: Int) -> Unit,
) {
    if (timeDate.dayNumber <= 0 || timeDate.nameDay.isNullOrEmpty()) return
    if (timeDate.isToday && timeDate.dayNumber != dayNumberChecked) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .padding(2.dp)
                .border(
                    1.dp,
                    brush = Brush.verticalGradient(gradientColors),
                    shape = RoundedCornerShape(4.dp)
                ), contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.clickable {
                    dayCheckedNumber(
                        timeDate.yerNumber,
                        timeDate.monthNumber,
                        timeDate.dayNumber
                    )
                },
                text = timeDate.dayNumber.toString(),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                style = TextStyle(
                    brush = Brush.verticalGradient(
                        gradientColors
                    )
                )
            )
        }
    } else if (timeDate.nameDay == HalfWeekName.FRIDAY.nameDay && timeDate.dayNumber != dayNumberChecked) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .padding(2.dp)
                .background(
                    color = Periwinkle,
                    shape = RoundedCornerShape(4.dp),
                ), contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.clickable {
                    dayCheckedNumber(
                        timeDate.yerNumber,
                        timeDate.monthNumber,
                        timeDate.dayNumber
                    )
                },
                text = timeDate.dayNumber.toString(),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.surface,
            )
        }
    } else if (timeDate.dayNumber == dayNumberChecked && timeDate.yerNumber == yerNumberChecked && timeDate.monthNumber == monthNumberChecked) {
        Box(
            modifier = Modifier
                .size(46.dp)
                .padding(2.dp)
                .background(
                    brush = Brush.verticalGradient(gradientColors),
                    shape = RoundedCornerShape(4.dp),
                ), contentAlignment = Alignment.Center
        ) {
            Text(
                text = timeDate.dayNumber.toString(),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color.White
            )
        }
    } else {
        Box(
            modifier = Modifier
                .size(46.dp)
                .padding(2.dp)
                .background(
                    color = MaterialTheme.colorScheme.onTertiaryContainer,
                    shape = RoundedCornerShape(4.dp),
                ), contentAlignment = Alignment.Center
        ) {
            Text(
                modifier = Modifier.clickable {
                    dayCheckedNumber(
                        timeDate.yerNumber,
                        timeDate.monthNumber,
                        timeDate.dayNumber
                    )
                },
                text = timeDate.dayNumber.toString(),
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.surface
            )
        }
    }
}


