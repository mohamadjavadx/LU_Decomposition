package com.mohamadjavadx.views

import Jama.Matrix
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.mohamadjavadx.views.ui.theme.ViewsTheme
import kotlin.math.roundToLong

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val minRow = 2
        val mSize = mutableStateOf(minRow)
        val result = mutableStateOf("0")

        setContent {
            ViewsTheme {

                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val focusManager = LocalFocusManager.current

                    val configuration = LocalConfiguration.current
                    val screenWidth = configuration.screenWidthDp
                    val selSize = 40.dp
                    val paddingSize = 4.dp

                    var maxRow = 0
                    var space = screenWidth.dp
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
                            Spacer(modifier = Modifier.size(20.dp))
                            Text(
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth(),
                                text = "LU محاسبه دترمینان ماتریس با روش تجزیه"
                            )
                            Spacer(modifier = Modifier.size(20.dp))
                            LazyColumn(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center
                            ) {
                                items(count = mSize.value + 1) { i ->
                                    LazyRow(contentPadding = PaddingValues(vertical = paddingSize)) {
                                        items(count = mSize.value + 1) { j ->
                                            if (i == mSize.value || j == mSize.value) {
                                                if (i == j) {
                                                    Column {
                                                        FilledIconButton(
                                                            modifier = Modifier
                                                                .padding(horizontal = paddingSize)
                                                                .size(selSize),
                                                            onClick = {
                                                                if (j + 1 < maxRow) {
                                                                    mSize.value = mSize.value + 1
                                                                }
                                                            },
                                                        ) {
                                                            Text(text = "+")
                                                        }

                                                        Spacer(modifier = Modifier.height(4.dp))

                                                        FilledIconButton(
                                                            modifier = Modifier
                                                                .padding(horizontal = paddingSize)
                                                                .size(selSize),
                                                            onClick = {
                                                                if (j - 1 >= minRow) {
                                                                    mSize.value = mSize.value - 1
                                                                }
                                                            }
                                                        ) {
                                                            Text(text = "-")
                                                        }
                                                    }
                                                } else {
                                                    Box(
                                                        modifier = Modifier
                                                            .padding(horizontal = paddingSize)
                                                            .drawWithContent {
                                                                drawRoundRect(
                                                                    color = Color.DarkGray,
                                                                    style = dashedStroke(size.minDimension)
                                                                )
                                                            }
                                                            .size(selSize),
                                                    )
                                                }
                                            } else {
                                                val interactionSource =
                                                    remember { MutableInteractionSource() }
                                                val isFocused =
                                                    interactionSource.collectIsFocusedAsState()
                                                Box(
                                                    modifier = Modifier
                                                        .padding(horizontal = paddingSize)
                                                        .background(
                                                            color = if (isFocused.value) {
                                                                MaterialTheme.colorScheme.surfaceVariant
                                                            } else {
                                                                MaterialTheme.colorScheme.surface
                                                            },
                                                        )
                                                        .border(
                                                            width = 1.dp,
                                                            color = if (isFocused.value) {
                                                                MaterialTheme.colorScheme.primary
                                                            } else {
                                                                Color.DarkGray
                                                            },
                                                        )
                                                        .size(selSize),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    BasicTextField(
                                                        value = textStates[i][j].value,
                                                        onValueChange = {
                                                            val newText = it.text.filterIndexed { index, c ->
                                                                (c.isDigit() || c == '-') && (index != 0 || c != '0')
                                                            }
                                                            textStates[i][j].value = it.copy(
                                                                text = newText
                                                            )
                                                        },
                                                        interactionSource = interactionSource,
                                                        singleLine = true,
                                                        keyboardOptions = KeyboardOptions(
                                                            keyboardType = KeyboardType.Number,
                                                            imeAction = ImeAction.Next,
                                                            autoCorrect = false,
                                                        ),
                                                        textStyle = TextStyle(
                                                            textAlign = TextAlign.Center
                                                        ),
                                                        modifier = Modifier.onFocusChanged { focusState ->
                                                            if (focusState.isFocused) {
                                                                val currentState =
                                                                    textStates[i][j].value
                                                                textStates[i][j].value =
                                                                    currentState.copy(
                                                                        selection = TextRange(
                                                                            start = 0,
                                                                            end = currentState.text.length
                                                                        )
                                                                    )
                                                            }
                                                        }
                                                    )
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                            Row(modifier = Modifier.fillMaxWidth()) {
                                Spacer(modifier = Modifier.width(50.dp))
                                OutlinedButton(
                                    modifier = Modifier
                                        .padding(horizontal = paddingSize)
                                        .weight(1f),
                                    onClick = {
                                        focusManager.moveFocus(FocusDirection.Previous)
                                    },
                                ) {
                                    Text(text = "previous")
                                }
                                OutlinedButton(
                                    modifier = Modifier
                                        .padding(horizontal = paddingSize)
                                        .weight(1f),
                                    onClick = {
                                        focusManager.moveFocus(FocusDirection.Next)
                                    },
                                ) {
                                    Text(text = "next")
                                }
                                Spacer(modifier = Modifier.width(50.dp))
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Column(modifier = Modifier.padding(16.dp)) {
                                ElevatedButton(
                                    onClick = {
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
                                                                                text = "0"
                                                                            )
                                                                        0.0
                                                                    } else {
                                                                        textStates[iIndex][jIndex].value =
                                                                            textStates[iIndex][jIndex].value.copy(
                                                                                text = j.value.text
                                                                            )
                                                                        j.value.text.toDouble()
                                                                    }
                                                                }
                                                        }
                                                )
                                            )
                                            result.value = matrix.det().roundToLong().toString()
                                        }.onFailure {
                                            result.value = "failed"
                                        }
                                    }
                                ) {
                                    Text(text = "cal LUDecomposition")
                                }
                                ElevatedButton(
                                    onClick = {
                                        textStates.forEach {
                                            it.forEach {
                                                it.value = it.value.copy(text = "")
                                            }
                                        }
                                    }
                                ) {
                                    Text(text = "reset")
                                }
                                Spacer(modifier = Modifier.size(4.dp))
                                Text(text = "det= ${result.value}")
                                Spacer(modifier = Modifier.size(20.dp))
                            }
                        }

                    }
                }
            }
        }
    }
}

fun convertToListOfArrays(input: List<List<Double>>): Array<DoubleArray> {
    val result = Array(input.size) { index ->
        input[index].toDoubleArray()
    }
    return result
}

fun dashedStroke(minDimension: Float): Stroke {
    val dashInterval = minDimension * 0.1f
    return Stroke(
        width = 2f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(dashInterval, dashInterval), 0f)
    )
}

