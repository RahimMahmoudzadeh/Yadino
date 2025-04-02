package com.rahim.yadino.navigation.component

import androidx.activity.compose.BackHandler
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.rahim.yadino.designsystem.component.gradientColors
import com.rahim.yadino.designsystem.theme.CornflowerBlueLight
import com.rahim.yadino.designsystem.theme.YadinoTheme
import com.rahim.yadino.library.navigation.R
import com.rahim.yadino.navigation.component.DrawerItemType.RateToApp
import com.rahim.yadino.navigation.component.DrawerItemType.ShareWithFriends
import com.rahim.yadino.navigation.component.DrawerItemType.Theme
import kotlinx.coroutines.launch

sealed interface DrawerItemType {
  val title: Int
  val iconRes: Int

  data class Theme(
    @StringRes override val title: Int,
    @DrawableRes override val iconRes: Int,
  ) : DrawerItemType

  data class ShareWithFriends(
    @StringRes override val title: Int,
    @DrawableRes override val iconRes: Int,
  ) : DrawerItemType

  data class RateToApp(
    @StringRes override val title: Int,
    @DrawableRes override val iconRes: Int,
  ) : DrawerItemType
}

val yadinoDrawerItems = listOf(
  ShareWithFriends(R.string.drawer_item_share_with_ferinds, R.drawable.share),
  RateToApp(R.string.drawer_item_rate_to_app, R.drawable.star),
  Theme(R.string.drawer_item_theme, R.drawable.color_lens),
)

@Composable
fun YadinoNavigationDrawer(
  modifier: Modifier = Modifier,
  drawerState: DrawerState,
  itemHeight: Dp = 50.dp,
  drawerWidth: Dp = 240.dp,
  headerHeight: Dp = 150.dp,
  onItemClick: (DrawerItemType) -> Unit = {},
  isDarkTheme: Boolean = false,
  gesturesEnabled: Boolean = true,
  content: @Composable () -> Unit,
) {
  val scope = rememberCoroutineScope()
  if (drawerState.isOpen) {
    BackHandler {
      scope.launch {
        drawerState.close()
      }
    }
  }
  ModalNavigationDrawer(
    modifier = modifier,
    drawerState = drawerState,
    drawerContent = {
      ModalDrawerSheet(
        modifier = Modifier.width(drawerWidth),
        drawerContainerColor = MaterialTheme.colorScheme.background,
        drawerContentColor = MaterialTheme.colorScheme.onSurface,
        windowInsets = WindowInsets(0),
      ) {
        YadinoDrawerHeader(
          modifier = Modifier
              .height(headerHeight)
              .fillMaxWidth()
              .drawBehind {
                  drawRect(
                      brush = Brush.linearGradient(
                          colors = gradientColors,
                          start = Offset(0f, size.height),
                          end = Offset(size.width, 0f),
                      ),
                  )
              }
              .statusBarsPadding(),
          greetingTitle = R.string.hello_friend,
          iconRes = com.rahim.yadino.library.designsystem.R.drawable.img_app_wekup,
        )
        Spacer(Modifier.height(12.dp))
        yadinoDrawerItems.forEach { yadinoDrawerItem ->
          YadinoDrawerItem(
            modifier = Modifier
                .padding(NavigationDrawerItemDefaults.ItemPadding)
                .height(itemHeight)
                .clickable { onItemClick(yadinoDrawerItem) },
            title = yadinoDrawerItem.title,
            iconRes = yadinoDrawerItem.iconRes,
            rightSlot = if (yadinoDrawerItem is Theme) {
              {
                ThemeSwitch(
                  isDark = isDarkTheme,
                  onChange = { onItemClick(yadinoDrawerItem) },
                )
              }
            } else {
              null
            },
          )
        }
      }
    },
    gesturesEnabled = gesturesEnabled,
    content = content,
  )
}

@Composable
private fun ThemeSwitch(
  isDark: Boolean = false,
  onChange: (Boolean) -> Unit,
) {
  val thumbIconRes = if (isDark) {
    R.drawable.brightness_2_24
  } else {
    R.drawable.brightness_high_24
  }

  Switch(
    checked = isDark,
    onCheckedChange = onChange,
    thumbContent = {
      Icon(
        painter = painterResource(id = thumbIconRes),
        contentDescription = null,
        modifier = Modifier.size(SwitchDefaults.IconSize),
      )
    },
    colors = SwitchDefaults.colors(
      checkedTrackColor = CornflowerBlueLight,
      checkedThumbColor = Color.White,
      checkedIconColor = CornflowerBlueLight,
    ),
  )
}

@Composable
private fun YadinoDrawerHeader(
  modifier: Modifier = Modifier,
  @StringRes greetingTitle: Int,
  @DrawableRes iconRes: Int,
) {
  Box(modifier = modifier) {
    Image(
      painter = painterResource(id = iconRes),
      contentDescription = null,
      modifier = Modifier
          .align(Alignment.TopEnd)
          .size(
              72.dp,
          ),
    )

    Column(
      horizontalAlignment = Alignment.CenterHorizontally,
      modifier = Modifier
          .align(Alignment.Center)
          .padding(top = 16.dp, end = 16.dp),
    ) {
      Text(
        text = stringResource(id = greetingTitle),
        style = MaterialTheme.typography.labelSmall,
        color = Color.White,
      )
    }
  }
}

@Composable
private fun YadinoDrawerItem(
  modifier: Modifier = Modifier,
  @StringRes title: Int,
  @DrawableRes iconRes: Int,
  rightSlot: @Composable (() -> Unit)? = null,
) {
  Row(
    modifier = modifier,
    verticalAlignment = Alignment.CenterVertically,
  ) {
    Icon(
      painter = painterResource(id = iconRes),
      contentDescription = null,
      modifier = Modifier.padding(horizontal = 12.dp),
      tint = Color.Unspecified,
    )
    Text(text = stringResource(id = title), color = MaterialTheme.colorScheme.onSecondaryContainer)
    Spacer(modifier = Modifier.weight(1f))
    rightSlot?.run {
      this()
      Spacer(modifier = Modifier.width(12.dp))
    }
  }
}

@Composable
@Preview
private fun YadinoNavDrawerPreview() {
  YadinoTheme {
    YadinoNavigationDrawer(
      drawerState = rememberDrawerState(initialValue = DrawerValue.Open),
    ) {}
  }
}
