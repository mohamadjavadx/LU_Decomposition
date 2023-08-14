package com.mohamadjavadx.views

import Jama.Matrix
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.ExperimentalTextApi
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mohamadjavadx.views.ui.theme.ViewsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val mSize = mutableStateOf(2)
        val result = mutableStateOf("0.0")

        setContent {
            ViewsTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {


                    val configuration = LocalConfiguration.current
                    val screenSize =
                        Math.min(configuration.screenWidthDp, configuration.screenHeightDp)
                    val selSize = 40.dp
                    val paddingSize = 4.dp


                    var maxRow = 0
                    var space = screenSize.dp
                    while (space > selSize + (paddingSize * 2)) {
                        space -= (selSize + (paddingSize * 2))
                        maxRow++
                    }

                    val textStates: List<List<MutableState<TextFieldValue>>> =
                        remember(maxRow) {
                            List(maxRow + 1) {
                                List(maxRow + 1) {
                                    mutableStateOf(TextFieldValue())
                                }
                            }
                        }

                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center,
                    ) {

                        Column {
                            Text(text = "LU محاسبه دترمینان ماتریس با روش تجزیه")
                            Spacer(modifier = Modifier.size(20.dp))
                            LazyColumn {
                                items(count = mSize.value + 1) { i ->
                                    LazyRow(contentPadding = PaddingValues(vertical = paddingSize)) {
                                        items(count = mSize.value + 1) { j ->
                                            if (i == mSize.value || j == mSize.value) {
                                                if (i == j) {
                                                    FilledIconButton(
                                                        modifier = Modifier
                                                            .padding(horizontal = paddingSize)
                                                            .size(selSize),
                                                        onClick = {
                                                            if (j + 1 != maxRow) {
                                                                mSize.value = mSize.value + 1
                                                            }
                                                        }
                                                    ) {
                                                        Text(text = "+")
                                                    }
                                                } else {
                                                    Box(
                                                        modifier = Modifier
                                                            .padding(horizontal = paddingSize)
                                                            .border(
                                                                width = 1.dp,
                                                                color = Color.Gray,
                                                            )
                                                            .size(selSize),
                                                    )
                                                }
                                            } else {
                                                Box(
                                                    modifier = Modifier
                                                        .padding(horizontal = paddingSize)
                                                        .background(color = Color.LightGray)
                                                        .border(
                                                            width = 1.dp,
                                                            color = Color.DarkGray,
                                                        )
                                                        .size(selSize),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    BasicTextField(
                                                        value = textStates[i][j].value,
                                                        onValueChange = {
                                                            textStates[i][j].value = it
                                                        },
                                                        singleLine = true,
                                                        keyboardOptions = KeyboardOptions(
                                                            keyboardType = KeyboardType.Number,
                                                            imeAction = ImeAction.Next,
                                                            autoCorrect = false,
                                                        ),
                                                        textStyle = TextStyle(
                                                            textAlign = TextAlign.Center
                                                        )
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.size(20.dp))
                            ElevatedButton(onClick = {
                                runCatching {
                                    val matrix = Matrix(
                                        convertToListOfArrays(
                                            textStates.slice(0 until mSize.value)
                                                .mapIndexed { iIndex, i ->
                                                    i.slice(0 until mSize.value)
                                                        .mapIndexed { jIndex, j ->
                                                            if (j.value.text.isBlank()) {
                                                                textStates[iIndex][jIndex].value =
                                                                    textStates[iIndex][jIndex].value.copy(
                                                                        text = "0.0"
                                                                    )
                                                                0.0
                                                            } else {
                                                                textStates[iIndex][jIndex].value =
                                                                    textStates[iIndex][jIndex].value.copy(
                                                                        text = j.value.text.toDouble()
                                                                            .toString()
                                                                    )
                                                                j.value.text.toDouble()
                                                            }
                                                        }
                                                }
                                        )
                                    )
                                    result.value = matrix.det().toString()
                                }.onFailure {
                                    result.value = "failed"
                                }
                            }) {
                                Text(text = "cal LUDecomposition")
                            }
                            ElevatedButton(onClick = {
                                mSize.value = 2
                                textStates.forEach {
                                    it.forEach {
                                        it.value = it.value.copy(text = "")
                                    }
                                }
                            }) {
                                Text(text = "reset")
                            }
                            Spacer(modifier = Modifier.size(20.dp))
                            Text(text = "det= ${result.value}")
                        }

                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalTextApi::class)
@Composable
fun Clock() {


    val hourDialTextMeasurer = rememberTextMeasurer()
    val dateTextMeasurer = rememberTextMeasurer()


    Canvas(
        modifier = Modifier
            .size(200.dp, 200.dp),
        onDraw = {

            val faceWidth = 200.dp.toPx()

            val faceRadius = faceWidth / 2

            val facePadding = 8.dp.toPx()


            val shaftRadius = faceWidth / 36


            val indexWidth = 6.dp.toPx()

            val hourIndexSize = Size(
                width = indexWidth,
                height = indexWidth
            )

            val minuteIndexSize = Size(
                width = 1.dp.toPx(),
                height = indexWidth
            )

            val indexColor = Color(0xFF849A84)


            val hourDialRectangle = Size(
                width = indexWidth,
                height = indexWidth * 2
            )

            val hourDialTextConstraintSize = Size(
                width = 20.dp.toPx(),
                height = 20.dp.toPx()
            )

            val hourDialTextStyle = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Cursive,
                fontSize = 18.sp
            )


            val dateConstraintPadding = hourDialTextConstraintSize.width * 0.1f
            val dateLocation = 3
            val dateConstraintSize = Size(
                width = hourDialTextConstraintSize.width + dateConstraintPadding,
                height = hourDialTextConstraintSize.height * 0.9f
            )
            val dateTextStyle = TextStyle(
                color = Color.White,
                fontWeight = FontWeight.SemiBold,
                fontFamily = FontFamily.Cursive,
                fontSize = 18.sp
            )

            // background
            drawCircle(
                color = Color.Black,
                radius = faceRadius,
                center = size.center,
            )

            // shaft
            drawCircle(
                color = Color.White,
                radius = shaftRadius,
                center = size.center,
            )
            drawCircle(
                color = Color.LightGray,
                radius = shaftRadius * 0.75f,
                center = size.center,
            )


            repeat(12) { index ->
                val rotateDegrees = (360 / 12f) * index
                rotate(
                    degrees = rotateDegrees,
                    pivot = size.center,
                ) {

                    // hourIndex
                    drawRoundRect(
                        color = indexColor,
                        topLeft = Offset(
                            x = center.x - (hourIndexSize.width / 2),
                            y = facePadding
                        ),
                        size = hourIndexSize,
                        cornerRadius = CornerRadius(
                            hourIndexSize.width * 0.25f,
                            hourIndexSize.width * 0.25f
                        )
                    )

                    // Dial

                    val hourDialPadding = facePadding + (hourIndexSize.width * 2)

                    // hourDialTriangle
                    if (index == 0) {
                        val dialTriangleWidth = indexWidth * 3
                        val dialTriangleHalfWidth = dialTriangleWidth / 2f
                        val dialTriangleHeight = hourDialTextConstraintSize.height

                        Path().apply {
                            moveTo(
                                center.x - dialTriangleHalfWidth,
                                hourDialPadding
                            )
                            lineTo(
                                center.x + dialTriangleHalfWidth,
                                hourDialPadding
                            )
                            lineTo(
                                center.x,
                                hourDialPadding + dialTriangleHeight
                            )
                            close()

                            drawPath(
                                path = this,
                                color = indexColor,
                                style = Fill
                            )
                        }
                    }

                    // hourDialRectangle
                    if (index % 3 == 0 && index != 0) {
                        drawRoundRect(
                            color = Color.White,
                            topLeft = Offset(
                                x = center.x - (hourDialRectangle.width / 2),
                                y = hourDialPadding
                            ),
                            size = hourDialRectangle,
                            cornerRadius = CornerRadius(
                                x = hourIndexSize.width * 0.25f,
                                y = hourIndexSize.width * 0.25f
                            ),
                        )
                    }

                    // hourDialText

                    val hourDialText = AnnotatedString(index.toString())

                    val hourDialTextSize = hourDialTextMeasurer.measure(
                        text = hourDialText,
                        style = hourDialTextStyle
                    ).size

                    val hourDialTextConstraintOffset = Offset(
                        x = center.x - (hourDialTextConstraintSize.width / 2),
                        y = hourDialPadding
                    )

                    val hourDialTextOffset = hourDialTextConstraintOffset + Offset(
                        x = (hourDialTextConstraintSize.width - hourDialTextSize.width) / 2,
                        y = (hourDialTextConstraintSize.height - hourDialTextSize.height) / 2
                    )

                    val hourDialTextCenterOffset = hourDialTextConstraintOffset + Offset(
                        hourDialTextConstraintSize.width / 2f,
                        hourDialTextConstraintSize.height / 2f,
                    )

                    rotate(
                        degrees = -rotateDegrees,
                        pivot = hourDialTextCenterOffset
                    ) {
                        if (index % 3 != 0) {
                            drawText(
                                textMeasurer = hourDialTextMeasurer,
                                text = hourDialText,
                                topLeft = hourDialTextOffset,
                                style = hourDialTextStyle,
                            )
                        }
                    }

                    // date

                    val dateConstraintOffset = Offset(
                        x = center.x - (dateConstraintSize.width / 2),
                        y = hourDialPadding + dateConstraintPadding
                    )

                    val dateConstraintCenterOffset = dateConstraintOffset + Offset(
                        dateConstraintSize.width / 2f,
                        dateConstraintSize.height / 2f,
                    )

                    if (index == dateLocation) {
                        rotate(
                            degrees = -rotateDegrees,
                            pivot = dateConstraintCenterOffset
                        ) {
                            drawRoundRect(
                                color = Color.White,
                                topLeft = dateConstraintOffset,
                                size = dateConstraintSize,
                                cornerRadius = CornerRadius(
                                    x = dateConstraintSize.width * 0.1f,
                                    y = dateConstraintSize.width * 0.1f
                                ),
                            )
                        }
                    }

                }
            }

            // minuteIndex
            repeat(60) { index ->
                if (index % 5 != 0) {
                    rotate(
                        degrees = (360 / 60f) * index,
                        pivot = size.center,
                    ) {
                        drawRoundRect(
                            color = indexColor,
                            topLeft = Offset(
                                center.x - (minuteIndexSize.width / 2),
                                facePadding
                            ),
                            size = minuteIndexSize,
                            cornerRadius = CornerRadius(
                                minuteIndexSize.width * 0.25f,
                                minuteIndexSize.width * 0.25f,
                            )
                        )
                    }
                }
            }
        }
    )

}


fun convertToListOfArrays(input: List<List<Double>>): Array<DoubleArray> {
    val result = Array(input.size) { index ->
        input[index].toDoubleArray()
    }
    return result
}