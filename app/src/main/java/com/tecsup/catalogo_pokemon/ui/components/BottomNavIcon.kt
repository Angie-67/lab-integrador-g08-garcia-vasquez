package com.tecsup.catalogo_pokemon.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

enum class BottomNavIconType {
    DASHBOARD,
    CATALOGO,
    FAVORITOS,
    PERFIL
}

@Composable
fun BottomNavIcon(
    type: BottomNavIconType,
    color: Color,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(24.dp)) {
        val strokeWidth = 1.8.dp.toPx()
        val roundedStroke = Stroke(width = strokeWidth)

        when (type) {
            BottomNavIconType.DASHBOARD -> {
                val cell = 7.5.dp.toPx()
                val gap = 3.dp.toPx()
                val start = 2.dp.toPx()
                val radius = CornerRadius(1.5.dp.toPx())
                for (row in 0..1) {
                    for (column in 0..1) {
                        drawRoundRect(
                            color = color,
                            topLeft = Offset(
                                x = start + column * (cell + gap),
                                y = start + row * (cell + gap)
                            ),
                            size = Size(cell, cell),
                            cornerRadius = radius,
                            style = roundedStroke
                        )
                    }
                }
            }

            BottomNavIconType.CATALOGO -> {
                val book = Path().apply {
                    moveTo(4.dp.toPx(), 3.dp.toPx())
                    lineTo(10.dp.toPx(), 4.dp.toPx())
                    quadraticTo(12.dp.toPx(), 4.5.dp.toPx(), 12.dp.toPx(), 6.dp.toPx())
                    lineTo(12.dp.toPx(), 21.dp.toPx())
                    quadraticTo(12.dp.toPx(), 19.5.dp.toPx(), 10.dp.toPx(), 19.dp.toPx())
                    lineTo(4.dp.toPx(), 18.dp.toPx())
                    close()
                    moveTo(20.dp.toPx(), 3.dp.toPx())
                    lineTo(14.dp.toPx(), 4.dp.toPx())
                    quadraticTo(12.dp.toPx(), 4.5.dp.toPx(), 12.dp.toPx(), 6.dp.toPx())
                    lineTo(12.dp.toPx(), 21.dp.toPx())
                    quadraticTo(12.dp.toPx(), 19.5.dp.toPx(), 14.dp.toPx(), 19.dp.toPx())
                    lineTo(20.dp.toPx(), 18.dp.toPx())
                    close()
                }
                drawPath(path = book, color = color, style = roundedStroke)
            }

            BottomNavIconType.FAVORITOS -> {
                val heart = Path().apply {
                    moveTo(12.dp.toPx(), 21.dp.toPx())
                    cubicTo(10.dp.toPx(), 19.dp.toPx(), 3.dp.toPx(), 14.dp.toPx(), 3.dp.toPx(), 8.dp.toPx())
                    cubicTo(3.dp.toPx(), 3.dp.toPx(), 9.dp.toPx(), 2.dp.toPx(), 12.dp.toPx(), 7.dp.toPx())
                    cubicTo(15.dp.toPx(), 2.dp.toPx(), 21.dp.toPx(), 3.dp.toPx(), 21.dp.toPx(), 8.dp.toPx())
                    cubicTo(21.dp.toPx(), 14.dp.toPx(), 14.dp.toPx(), 19.dp.toPx(), 12.dp.toPx(), 21.dp.toPx())
                }
                drawPath(path = heart, color = color, style = roundedStroke)
            }

            BottomNavIconType.PERFIL -> {
                drawCircle(
                    color = color,
                    radius = 3.5.dp.toPx(),
                    center = Offset(12.dp.toPx(), 7.dp.toPx()),
                    style = roundedStroke
                )
                val shoulders = Path().apply {
                    moveTo(4.dp.toPx(), 21.dp.toPx())
                    lineTo(4.dp.toPx(), 19.dp.toPx())
                    cubicTo(
                        4.dp.toPx(), 15.dp.toPx(),
                        7.5.dp.toPx(), 13.dp.toPx(),
                        12.dp.toPx(), 13.dp.toPx()
                    )
                    cubicTo(
                        16.5.dp.toPx(), 13.dp.toPx(),
                        20.dp.toPx(), 15.dp.toPx(),
                        20.dp.toPx(), 19.dp.toPx()
                    )
                    lineTo(20.dp.toPx(), 21.dp.toPx())
                }
                drawPath(path = shoulders, color = color, style = roundedStroke)
            }
        }
    }
}
