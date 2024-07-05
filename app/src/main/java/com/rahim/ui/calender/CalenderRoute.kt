package com.rahim.ui.calender

import android.icu.text.DateFormatSymbols
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLineComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisTickComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottomAxis
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStartAxis
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.core.cartesian.CartesianChart
import com.patrykandpatrick.vico.core.cartesian.axis.AxisItemPlacer
import com.patrykandpatrick.vico.core.cartesian.axis.AxisPosition
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.CartesianValueFormatter
import com.patrykandpatrick.vico.core.cartesian.data.ChartValues
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.common.component.LineComponent
import com.rahim.R
import com.rahim.data.modle.data.TimeDate
import com.rahim.ui.theme.WildSand
import com.rahim.utils.base.view.DialogButtonBackground
import com.rahim.utils.base.view.TimeItems
import com.rahim.utils.base.view.gradientColors
import com.rahim.utils.enums.HalfWeekName
import com.rahim.utils.extention.calculateMonthName
import java.util.Locale

@Composable
fun CalenderRoute(
    modifier: Modifier = Modifier,
    viewModel: CalenderViewModel = hiltViewModel(),
) {
    val times by viewModel.times.collectAsStateWithLifecycle()
    CalenderScreen(
        modifier,
        times,
        viewModel.currentMonth,
        viewModel.currentYear,
        viewModel.currentDay,
        onMonthChecked = viewModel::getTimesMonth
    )
}

@Composable
private fun CalenderScreen(
    modifier: Modifier = Modifier,
    times: List<TimeDate>,
    currentMonth: Int,
    currentYear: Int,
    currentDay: Int,
    onMonthChecked: (Int, Int) -> Unit,
) {
    var monthClicked by rememberSaveable { mutableIntStateOf(currentMonth) }
    var yearClicked by rememberSaveable { mutableIntStateOf(currentYear) }
    var dayClicked by rememberSaveable { mutableIntStateOf(currentDay) }

    Column(modifier = modifier) {
        Column(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 35.dp, start = 40.dp, end = 40.dp)
                .clip(RoundedCornerShape(percent = 6))
                .shadow(1.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .background(brush = Brush.verticalGradient(gradientColors))
                    .padding(vertical = 10.dp, horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = {
                    monthClicked += 1
                    if (monthClicked > 12) {
                        monthClicked = 1
                        yearClicked += 1
                    }
                    dayClicked = if (monthClicked == currentMonth) dayClicked else 1
                    onMonthChecked(yearClicked, monthClicked)
                }) {
                    Icon(
                        tint = Color.White,
                        painter = painterResource(id = R.drawable.less_then),
                        contentDescription = "less then sign"
                    )
                }
                Text(
                    modifier = Modifier
                        .padding(top = 12.dp)
                        .fillMaxWidth(0.5f),
                    text = "$yearClicked ${monthClicked.calculateMonthName()}",
                    color = Color.White,
                    textAlign = TextAlign.Center
                )
                IconButton(onClick = {
                    monthClicked -= 1
                    if (monthClicked < 1) {
                        monthClicked = 12
                        yearClicked -= 1
                    }
                    dayClicked = if (monthClicked == currentMonth) dayClicked else 1
                    onMonthChecked(yearClicked, monthClicked)
                }) {
                    Icon(
                        painterResource(id = R.drawable.greater_then),
                        contentDescription = "greater then sign",
                        tint = Color.White
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp, horizontal = 18.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                val days = listOf(
                    HalfWeekName.SATURDAY.nameDay,
                    HalfWeekName.SUNDAY.nameDay,
                    HalfWeekName.MONDAY.nameDay,
                    HalfWeekName.TUESDAY.nameDay,
                    HalfWeekName.WEDNESDAY.nameDay,
                    HalfWeekName.THURSDAY.nameDay,
                    HalfWeekName.FRIDAY.nameDay
                )
                days.reversed().forEach {
                    when (it) {
                        days.first() -> {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(end = 12.dp)
                            )
                        }

                        days.last() -> {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.padding(start = 12.dp)
                            )
                        }

                        else -> {
                            Text(
                                text = it,
                                color = MaterialTheme.colorScheme.primary,
                            )
                        }
                    }

                }
            }
            CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                LazyVerticalGrid(
                    modifier = Modifier.padding(start = 10.dp, end = 10.dp, bottom = 10.dp),
                    columns = GridCells.Fixed(7),
                    verticalArrangement = Arrangement.SpaceBetween,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    userScrollEnabled = false
                ) {
                    items(times) {
                        TimeItems(
                            it,
                            dayClicked,
                            monthClicked,
                            yearClicked,
                            dayCheckedNumber = { yer, month, day ->
                                yearClicked = yer
                                monthClicked = month
                                dayClicked = day
                            })
                    }
                }
            }
        }
        Column(
            modifier = Modifier
                .padding(top = 12.dp, bottom = 10.dp, start = 30.dp, end = 40.dp)
        ) {
            val modelProducer = remember { CartesianChartModelProducer.build() }
            LaunchedEffect(Unit) {
                modelProducer.tryRunTransaction {
                    columnSeries {
                        series(
                            4,
                            12,
                            8,
                            16
                        )
                    }
                }
            }
            val i= arrayListOf("r","a","b")
            val bottomAxisValueFormatter = CartesianValueFormatter { x, _, _ ->
                i[x.toInt() % i.size]
            }
            CartesianChartHost(
                rememberCartesianChart(
                    rememberColumnCartesianLayer(),
                    startAxis = rememberStartAxis(),
                    bottomAxis = rememberBottomAxis(
                       valueFormatter = bottomAxisValueFormatter,
                        titleComponent = rememberAxisLabelComponent(),
                        label = rememberAxisLabelComponent(color = Blue),
                        axis = rememberAxisLineComponent(color = Blue)
                    )
                ),
                modelProducer,
            )
        }
    }
}