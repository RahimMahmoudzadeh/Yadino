package com.rahim.yadino.designsystem.component

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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
import com.rahim.yadino.designsystem.utils.size.FontDimensions
import com.rahim.yadino.designsystem.utils.size.LocalFontSize
import com.rahim.yadino.designsystem.utils.size.LocalSize
import com.rahim.yadino.designsystem.utils.size.LocalSpacing
import com.rahim.yadino.designsystem.utils.size.SizeDimensions
import com.rahim.yadino.designsystem.utils.size.SpaceDimensions
import com.rahim.yadino.designsystem.utils.theme.CornflowerBlueLight
import com.rahim.yadino.designsystem.utils.theme.Periwinkle
import com.rahim.yadino.designsystem.utils.theme.Purple
import com.rahim.yadino.designsystem.utils.theme.PurpleGrey
import com.rahim.yadino.enums.HalfWeekName
import com.rahim.yadino.library.designsystem.R
import com.rahim.yadino.toPersianDigits

val gradientColors = listOf(Purple, PurpleGrey)

@Composable
fun GradientButton(
  text: String,
  gradient: Brush,
  modifier: Modifier = Modifier,
  textSize: TextUnit,
  size: SizeDimensions,
  space: SpaceDimensions,
  shape: Shape = RoundedCornerShape(size.size16),
  onClick: () -> Unit = { },
) {
  Button(
    colors = ButtonDefaults.buttonColors(Color.Transparent),
    contentPadding = PaddingValues(),
    modifier = modifier,
    onClick = { onClick() },
    shape = shape,
  ) {
    Box(
      modifier = Modifier
          .background(gradient)
          .fillMaxWidth()
          .padding(vertical = space.space12),
      contentAlignment = Alignment.Center,
    ) {
      Text(text = text, fontSize = textSize, color = Color.White)
    }
  }
}

@Composable
fun DialogButtonBackground(
  modifier: Modifier = Modifier,
  text: String,
  gradient: Brush,
  size: SizeDimensions,
  space: SpaceDimensions,
  textSize: TextUnit = LocalFontSize.current.fontSize14,
  textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
  onClick: () -> Unit = { },
) {
  Button(
    colors = ButtonDefaults.buttonColors(Color.Transparent),
    contentPadding = PaddingValues(),
    modifier = modifier,
    onClick = { onClick() },
    shape = RoundedCornerShape(size.size12),
  ) {
    Box(
      modifier = Modifier
          .background(gradient)
          .padding(vertical = space.space10)
          .fillMaxWidth(),
      contentAlignment = Alignment.Center,
    ) {
      Text(
        text = text,
        fontSize = textSize,
        style = textStyle,
        modifier = Modifier.padding(horizontal = space.space4),
        color = Color.White,
      )
    }
  }
}

@Composable
fun DialogButtonBorder(
  modifier: Modifier = Modifier,
  space: SpaceDimensions,
  size: SizeDimensions,
  text: String,
  gradient: Brush,
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
    shape = RoundedCornerShape(size.size12),
  ) {
    Box(
      modifier = Modifier
          .border(size.size1, brush = gradient, RoundedCornerShape(size.size12))
          .padding(vertical = space.space10)
          .fillMaxWidth(),
      contentAlignment = Alignment.Center,
    ) {
      Text(
        text = text,
        fontSize = textSize,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(horizontal = space.space4),
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
      verticalArrangement = Arrangement.Center,
    ) {
      CircularProgressIndicator(color = CornflowerBlueLight)
    }
  }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarCenterAlign(
  modifier: Modifier = Modifier,
  size: SizeDimensions,
  title: String,
  openHistory: () -> Unit,
  isShowSearchIcon: Boolean,
  isShowBackIcon: Boolean,
  onClickSearch: () -> Unit,
  onClickBack: () -> Unit,
  onDrawerClick: () -> Unit,
  haveAlarm: Boolean,
) {
  CenterAlignedTopAppBar(
    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(MaterialTheme.colorScheme.onBackground),
    modifier = modifier.shadow(elevation = size.size8),
    title = {
      Text(
        textAlign = TextAlign.Center,
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.SemiBold,
        text = title,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.secondary,
      )
    },
    navigationIcon = {
      Row(
        verticalAlignment = Alignment.CenterVertically,
      ) {
        IconButton(onClick = onDrawerClick) {
          Icon(
            imageVector = ImageVector.vectorResource(R.drawable.ic_more_vert),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onPrimary,
          )
        }
        if (!isShowBackIcon) {
          BadgedBox(
            modifier = Modifier.clickable { openHistory() },
            badge = {
              if (haveAlarm) {
                Badge()
              }
            },
          ) {
            Icon(
              imageVector = ImageVector.vectorResource(R.drawable.ic_notifications),
              contentDescription = "",
              tint = CornflowerBlueLight,
            )
          }
        }
      }
    },
    actions = {
      if (isShowSearchIcon) {
        IconButton(
          onClick = {
            onClickSearch()
          },
        ) {
          Icon(
            painter = painterResource(id = R.drawable.round_search),
            contentDescription = "search",
          )
        }
      } else if (isShowBackIcon) {
        IconButton(
          onClick = {
            onClickBack()
          },
        ) {
          Icon(
            painter = painterResource(id = R.drawable.greater_then),
            contentDescription = "search",
          )
        }
      }
    },
  )
}

@Preview
@Composable
fun GradientButtonPreview() {
  GradientButton(
    text = "شروع",
    space = LocalSpacing.current,
    size = LocalSize.current,
    gradient = Brush.horizontalGradient(gradientColors),
    modifier = Modifier
        .width(150.dp)
        .wrapContentHeight(),
    textSize = 14.sp,
  )
}

@Preview
@Composable
fun DialogButtonBackgroundWrapper() {
  DialogButtonBackground(
    text = "انتخاب",
    size = LocalSize.current,
    space = LocalSpacing.current,
    gradient = Brush.horizontalGradient(gradientColors),
    modifier = Modifier
        .width(150.dp)
        .wrapContentHeight(),
    textSize = 14.sp,
    textStyle = TextStyle(fontWeight = FontWeight.Bold),
    onClick = {},
  )
}

@OptIn(ExperimentalPermissionsApi::class)
fun requestPermissionNotification(
  notificationPermission: PermissionState,
  isGranted: (Boolean) -> Unit,
  permissionState: (PermissionState) -> Unit,
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
    Uri.fromParts("package", context.packageName, null),
  )
  ContextCompat.startActivity(context, intent, null)
}

@Composable
fun ShowSearchBar(
  clickSearch: Boolean,
  searchText: String,
  searchValueText: (String) -> Unit,
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
        ),
      )
    }
  }
}

@Composable
fun EmptyMessage(
  modifier: Modifier = Modifier,
  size: SizeDimensions,
  space: SpaceDimensions,
  fontSize: FontDimensions,
  @StringRes messageEmpty: Int = R.string.not_work_for_day,
  @DrawableRes painter: Int = R.drawable.empty_list_home,
) {
  Image(
    modifier = modifier
        .sizeIn(minHeight = size.size320)
        .fillMaxWidth()
        .fillMaxHeight(0.8f)
        .padding(space.space10),
    alignment = Alignment.Center,
    painter = painterResource(id = painter),
    contentDescription = "empty list home",
  )
  Text(
    text = stringResource(id = messageEmpty),
    modifier = Modifier
        .fillMaxWidth()
        .padding(top = space.space22),
    textAlign = TextAlign.Center,
    fontSize = fontSize.fontSize18,
    color = MaterialTheme.colorScheme.primary,
  )
}

@Composable
fun TimeItems(
  dayNumber: Int,
  nameDay: String,
  isToday: Boolean,
  dayNumberChecked: Int,
  size: SizeDimensions,
  space: SpaceDimensions,
  fontSize: FontDimensions,
  dayCheckedNumber: (day: Int) -> Unit,
) {
  if (dayNumber <= 0 || nameDay.isNullOrEmpty()) return
  if (isToday && dayNumber != dayNumberChecked) {
    Box(
      modifier = Modifier
          .size(size.size46)
          .padding(space.space2)
          .border(
              size.size1,
              brush = Brush.verticalGradient(gradientColors),
              shape = RoundedCornerShape(size.size4),
          ),
      contentAlignment = Alignment.Center,
    ) {
      Text(
        modifier = Modifier.clickable {
          dayCheckedNumber(
            dayNumber,
          )
        },
        text = dayNumber.toString().toPersianDigits(),
        textAlign = TextAlign.Center,
        fontSize = fontSize.fontSize16,
        style = TextStyle(
          brush = Brush.verticalGradient(
            gradientColors,
          ),
        ),
      )
    }
  } else if (nameDay == HalfWeekName.FRIDAY.nameDay && dayNumber != dayNumberChecked) {
    Box(
      modifier = Modifier
          .size(size.size46)
          .padding(space.space2)
          .background(
              color = Periwinkle,
              shape = RoundedCornerShape(size.size4),
          ),
      contentAlignment = Alignment.Center,
    ) {
      Text(
        modifier = Modifier.clickable {
          dayCheckedNumber(
            dayNumber,
          )
        },
        text = dayNumber.toString().toPersianDigits(),
        textAlign = TextAlign.Center,
        fontSize = fontSize.fontSize16,
        color = MaterialTheme.colorScheme.surface,
      )
    }
  } else if (dayNumberChecked == dayNumber) {
    Box(
      modifier = Modifier
        .size(size.size46)
        .padding(space.space2)
          .background(
              brush = Brush.verticalGradient(gradientColors),
              shape = RoundedCornerShape(size.size4),
          ),
      contentAlignment = Alignment.Center,
    ) {
      Text(
        text = dayNumber.toString().toPersianDigits(),
        textAlign = TextAlign.Center,
        fontSize = fontSize.fontSize16,
        color = Color.White,
      )
    }
  } else {
    Box(
      modifier = Modifier
        .size(size.size46)
        .padding(space.space2)
          .background(
              color = MaterialTheme.colorScheme.onTertiaryContainer,
              shape = RoundedCornerShape(size.size4),
          ),
      contentAlignment = Alignment.Center,
    ) {
      Text(
        modifier = Modifier.clickable {
          dayCheckedNumber(
            dayNumber,
          )
        },
        text = dayNumber.toString().toPersianDigits(),
        textAlign = TextAlign.Center,
        fontSize = fontSize.fontSize16,
        color = MaterialTheme.colorScheme.surface,
      )
    }
  }
}
