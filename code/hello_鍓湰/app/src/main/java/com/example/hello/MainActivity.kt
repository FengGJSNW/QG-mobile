package com.example.hello

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.text.isDigitsOnly

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Screen()
            }
        }
    }
}

@Composable
fun Screen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)  // 行间距
    ) {
        DisplayScreen(text = "0")

        CalculatorRow(24, "", "", "", "f(x)")
        CalculatorRow(24, "C", "±", "%", "÷")
        CalculatorRow(24, "7", "8", "9", "×")
        CalculatorRow(24, "4", "5", "6", "-")
        CalculatorRow(24, "1", "2", "3", "+")
        CalculatorRow(24, "0", ".", "Ans", "=")
    }
}


// 显示屏绘制逻辑
@Composable
fun DisplayScreen(text: String) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp),                          // 显示屏高度
        shape = RoundedCornerShape(24.dp),      // 圆角半径
        tonalElevation = 18.dp
    ) {
        Box(                        // 文字容器盒子
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            contentAlignment = Alignment.BottomEnd
        ) {
            Text(                   // 显示文本逻辑
                text = text,
                fontSize = 36.sp
            )
        }
    }
}

/* 按键绘制逻辑
 *
 * CalculatorRow 用于绘制一行按钮
 * CalculatorButton 用于制定按钮样式
 */
@Composable
fun CalculatorRow(textSize: Int, vararg labels: String) {
    Row( // 横向排布按钮
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)  // 按钮间距
    ) {
        for (label in labels) {
            val type = when (label) {
                "f(x)"  -> CalcButtonType.Function

                "÷", "×", "-", "+", "=",
                "C", "±", "%" -> CalcButtonType.Operator

                "1", "2", "3", "4", "5",
                "6", "7", "8", "9", "0",
                ".", "Ans" -> CalcButtonType.Number

                else -> CalcButtonType.OtherType
            }

            CalculatorButton(
                text = label,
                textSize = textSize,
                type = type,
                modifier = Modifier.weight(1f)    // 按钮平分一行宽度
            )
        }

        if (labels.size < 4) { // 灵活的按键大小设置
            repeat(4 - labels.size) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

enum class CalcButtonType {
    Number,      // 数字、小数点
    Operator,    // + - × ÷ =
    Function,    // C ± % f(x) Ans
    OtherType    // f(x) 引导出的功能键
}

@Composable
fun CalculatorButton(
    text: String,
    textSize: Int,
    type: CalcButtonType,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val containerColor = when (type) { // 按钮底色区分
        CalcButtonType.Number -> MaterialTheme.colorScheme.surfaceVariant
        CalcButtonType.Operator -> MaterialTheme.colorScheme.secondaryContainer
        CalcButtonType.Function -> MaterialTheme.colorScheme.primary
        CalcButtonType.OtherType -> MaterialTheme.colorScheme.primary
    }

    val contentColor = when (type) { // 按钮文字颜色区分
        CalcButtonType.Number -> MaterialTheme.colorScheme.onSurfaceVariant
        CalcButtonType.Operator -> MaterialTheme.colorScheme.onSurfaceVariant
        CalcButtonType.Function -> Color.White
        CalcButtonType.OtherType -> MaterialTheme.colorScheme.onPrimary
    }

    Button(
        onClick = onClick,
        modifier = modifier.aspectRatio(1f), // 尽量保持按钮接近正方形
        shape = RoundedCornerShape(20.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = containerColor,
            contentColor = contentColor
        )
    ) {
        val realTextSize: Int = when {
            text.length >= 3 -> (textSize - 4)
            else -> textSize
        }

        Text(
            text = text,
            fontSize = realTextSize.sp,
            maxLines = 1,
            softWrap = false
        )
    }
}