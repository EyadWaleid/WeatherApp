package com.example.weatherapp.screens.onboard

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.weatherapp.R
import com.example.weatherapp.utils.routes.Route
import kotlinx.coroutines.launch


@Composable
fun OnBoard(modifier: Modifier = Modifier , navHostController: NavHostController) {
    val scope = rememberCoroutineScope()
    var screenSize by remember { mutableStateOf(Size.Zero) }
    val photos = listOf(
        R.drawable.onboardone,
        R.drawable.onboardtwo,
        R.drawable.onboardthree
    )
    val descriptions = listOf(
        "Get hyper-local weather updates\n" +
                "with minute-by-minute accuracy.",
        "Receive custom alerts for severe\n" +
                "weather conditions in your area.",
        "Save and monitor weather for all your\n" +
                "favorite places around the globe."
    )
    val title = listOf(
        "Precision Forecasts",
        "Stay Informed",
        "Track Everywhere"
    )
    val pagerState = rememberPagerState(
        pageCount = { 3 },
        initialPage = 0
    )
    BoxWithConstraints {
        val width = constraints.maxWidth.toFloat()
        val height = constraints.maxHeight.toFloat()
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.radialGradient(
                        colorStops = arrayOf(
                            0f to Color(0xFF1E3A8A),
                            1f to Color(0xFF101922)
                        ),
                        center = Offset(width, 0f),
                        radius = width * 2.5f
                    )
                )
        ) {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .wrapContentSize()

            ) { page ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .wrapContentSize()
                        .fillMaxWidth()
                        .padding(24.dp)
                )
                {
                    Image(

                        painter = painterResource(photos[page]),
                        contentDescription = "",
                        Modifier
                            .size(300.dp)
                            .drawBehind {
                                if (page == 0) {
                                    drawIntoCanvas { canvas ->
                                        val paint = Paint().apply {
                                            asFrameworkPaint().apply {
                                                isAntiAlias = true
                                                color = android.graphics.Color.TRANSPARENT
                                                setShadowLayer(
                                                    30f,
                                                    0f,
                                                    0f,
                                                    android.graphics.Color.argb(
                                                        76,
                                                        255,
                                                        200,
                                                        0
                                                    )
                                                )
                                            }
                                        }
                                        canvas.drawCircle(
                                            center = center,
                                            radius = 300f,
                                            paint = paint
                                        )

                                    }

                                }

                            },
                    )
                    Spacer(modifier = Modifier.height(50.dp))
                    Text(
                        title[page],
                        color = colorResource(R.color.whiteBlue),
                        style = MaterialTheme.typography.displayMedium
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        descriptions[page],
                        color = colorResource(R.color.greyBlueLight),
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Light)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            PageIndicator(currentPage = pagerState.currentPage, modifier = Modifier.padding(60.dp))
            if (pagerState.currentPage == 0) {
                Button(
                    onClick = {
                        scope.launch {
                             val  next =pagerState.currentPage+1
                            pagerState.scrollToPage(next)
                        }

                    },
                    modifier = Modifier
                        .width((width * 0.3).dp)
                        .height((height * 0.025).dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color(0xffFFFFFF).copy(0.1f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xff137FEC).copy(0.1f)
                    )
                )
                {
                    Text(
                        "Get Started",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = colorResource(R.color.whiteBlue)
                    )
                }
                Spacer(modifier = Modifier.height(40.dp))
                TextButton(onClick = {
                    navHostController.popBackStack()
                    navHostController.navigate(Route.HomeScreen)
                }) {
                    Text(
                        "Skip Intro",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = colorResource(R.color.greyBlueDark)
                    )

                }
            } else {
                Button(
                    onClick = {
                      if(pagerState.currentPage==1){
                          scope.launch {
                              val  next =pagerState.currentPage+1
                              pagerState.scrollToPage(next)
                          }
                          return@Button
                      }
                        navHostController.popBackStack()
                        navHostController.navigate(Route.HomeScreen)
                    },
                    modifier = Modifier
                        .width((width * 0.3).dp)
                        .height((height * 0.025).dp),
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color(0xffFFFFFF).copy(0.1f)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(R.color.blue)
                    )
                )
                {
                    Text(
                        "Next",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = colorResource(R.color.whiteBlue)
                    )
                }
            }
        }
    }
}
@Composable
fun PageIndicator(currentPage: Int, modifier: Modifier = Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
    ) {
        repeat(3) {
            SingleIndecator(isSelected = it == currentPage);
        }

    }
}
@Composable
fun SingleIndecator(isSelected: Boolean) {
    val widthInd = animateDpAsState(if (isSelected) 36.dp else 15.dp)
    Box(
        modifier = Modifier
            .padding(2.dp)
            .width(widthInd.value)
            .height(15.dp)
            .clip(CircleShape)
            .background(color = if (isSelected) colorResource(R.color.blue) else colorResource(R.color.greyBlue))
    )
}
