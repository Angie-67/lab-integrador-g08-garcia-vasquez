package com.tecsup.catalogo_pokemon.ui.welcome

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Ink = Color(0xFF171923)
private val Ember = Color(0xFFE51F28)
private val Cream = Color(0xFFFFE8D5)

@Composable
fun WelcomeScreen(
    onIniciarSesion: () -> Unit,
    isLoading: Boolean = false,
    loadingLabel: String = "Verificando sesión…"
) {
    Surface(modifier = Modifier.fillMaxSize(), color = Cream) {
        Box(Modifier.fillMaxSize()) {
            WelcomeLandscape(Modifier.fillMaxSize())
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 28.dp, vertical = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Canvas(Modifier.size(42.dp)) { drawPokeball(this, size) }
                    Text(
                        "POKÉDEX", color = Ink, fontSize = 25.sp,
                        fontWeight = FontWeight.Black, letterSpacing = 2.sp
                    )
                }

                Spacer(Modifier.height(18.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(330.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(Modifier.size(218.dp)) { drawPokeball(this, size, glossy = true) }
                }

                Text(
                    "¡Tu aventura Pokémon", color = Ink, fontSize = 33.sp,
                    fontWeight = FontWeight.Black, fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center, lineHeight = 38.sp
                )
                Text(
                    "comienza aquí!", color = Ember, fontSize = 37.sp,
                    fontWeight = FontWeight.Black, fontStyle = FontStyle.Italic,
                    textAlign = TextAlign.Center, lineHeight = 42.sp
                )
                Canvas(
                    Modifier
                        .padding(top = 3.dp)
                        .widthFraction(0.42f)
                        .height(10.dp)
                ) {
                    val underline = Path().apply {
                        moveTo(0f, size.height * .8f)
                        quadraticTo(size.width * .48f, -size.height, size.width, size.height * .25f)
                    }
                    drawPath(underline, Ember, style = Stroke(width = 3.dp.toPx()))
                }

                Text(
                    "Explora el catálogo, descubre cada Pokémon y guarda tus favoritos.",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    color = Color(0xFF5D4B4D), fontSize = 18.sp,
                    textAlign = TextAlign.Center, lineHeight = 25.sp
                )

                Button(
                    onClick = onIniciarSesion,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 28.dp)
                        .height(72.dp),
                    enabled = !isLoading,
                    shape = RoundedCornerShape(40.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Ember,
                        contentColor = Color.White,
                        disabledContainerColor = Ember.copy(alpha = .7f)
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 7.dp, pressedElevation = 2.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(Modifier.size(22.dp), color = Color.White, strokeWidth = 2.dp)
                        Text(loadingLabel, modifier = Modifier.padding(start = 12.dp), fontSize = 19.sp)
                    } else {
                        Text("Iniciar sesión", fontSize = 23.sp, fontWeight = FontWeight.Bold)
                        Text("   →", fontSize = 28.sp, fontWeight = FontWeight.Medium)
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp, bottom = 140.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Spacer(Modifier.weight(1f).height(1.dp).background(Color(0xFFB78976)))
                    Text(
                        "  Tu próxima aventura te espera  ", color = Color(0xFF5D4B4D),
                        fontSize = 15.sp, fontWeight = FontWeight.SemiBold
                    )
                    Spacer(Modifier.weight(1f).height(1.dp).background(Color(0xFFB78976)))
                }
            }
        }
    }
}

private fun Modifier.widthFraction(fraction: Float) = this.then(
    Modifier.fillMaxWidth(fraction)
)

@Composable
private fun WelcomeLandscape(modifier: Modifier = Modifier) {
    Canvas(modifier) {
        drawRect(Brush.verticalGradient(listOf(Color(0xFFFFE7D4), Color(0xFFFFF2E7), Color(0xFFFFD1AE))))
        val far = Path().apply {
            moveTo(0f, size.height * .89f)
            lineTo(size.width * .22f, size.height * .83f)
            lineTo(size.width * .34f, size.height * .88f)
            lineTo(size.width * .58f, size.height * .80f)
            lineTo(size.width * .77f, size.height * .87f)
            lineTo(size.width, size.height * .82f)
            lineTo(size.width, size.height)
            lineTo(0f, size.height)
            close()
        }
        drawPath(far, Brush.verticalGradient(listOf(Color(0xFFFF9D43), Color(0xFFE84925)), size.height * .8f, size.height))
        val volcano = Path().apply {
            moveTo(size.width * .22f, size.height)
            lineTo(size.width * .35f, size.height * .91f)
            lineTo(size.width * .48f, size.height * .84f)
            lineTo(size.width * .57f, size.height * .86f)
            lineTo(size.width * .73f, size.height * .94f)
            lineTo(size.width * .86f, size.height)
            close()
        }
        drawPath(volcano, Brush.verticalGradient(listOf(Color(0xFFFF7926), Color(0xFF6F1424)), size.height * .83f, size.height))
        val lava = Path().apply {
            moveTo(size.width * .48f, size.height * .85f)
            lineTo(size.width * .51f, size.height * .875f)
            lineTo(size.width * .54f, size.height * .858f)
            lineTo(size.width * .58f, size.height * .90f)
            lineTo(size.width * .55f, size.height * .89f)
            lineTo(size.width * .52f, size.height * .91f)
            lineTo(size.width * .49f, size.height * .875f)
            close()
        }
        drawPath(lava, Color(0xFFFFD13D))
        drawPath(
            Path().apply {
                moveTo(0f, size.height * .96f)
                quadraticTo(size.width * .22f, size.height * .91f, size.width * .34f, size.height)
                lineTo(0f, size.height)
                close()
            }, Color(0xFF481326)
        )
        drawPath(
            Path().apply {
                moveTo(size.width, size.height * .94f)
                quadraticTo(size.width * .78f, size.height * .91f, size.width * .69f, size.height)
                lineTo(size.width, size.height)
                close()
            }, Color(0xFF481326)
        )
    }
}

private fun drawPokeball(
    scope: androidx.compose.ui.graphics.drawscope.DrawScope,
    size: Size,
    glossy: Boolean = false
) = with(scope) {
    val center = Offset(size.width / 2f, size.height / 2f)
    val radius = size.minDimension * .46f
    val ball = androidx.compose.ui.geometry.Rect(center.x - radius, center.y - radius, center.x + radius, center.y + radius)
    drawCircle(Color(0xFF21151B), radius * 1.045f, center)
    drawCircle(Color(0xFFF9F4F0), radius, center)
    val topHalf = Path().apply {
        moveTo(center.x - radius, center.y)
        arcTo(ball, 180f, 180f, false)
        close()
    }
    drawPath(topHalf, Brush.verticalGradient(listOf(Color(0xFFFF302B), Color(0xFFE30B22)), center.y - radius, center.y))
    drawRect(Color(0xFF21151B), Offset(center.x - radius, center.y - size.height * .055f), Size(radius * 2f, size.height * .11f))
    drawCircle(Color(0xFF21151B), radius * .29f, center)
    drawCircle(Brush.verticalGradient(listOf(Color.White, Color(0xFFE5D9D8))), radius * .19f, center)
    if (glossy) {
        drawOval(Color(0x99FFFFFF), topLeft = Offset(center.x - radius * .69f, center.y - radius * .75f), size = Size(radius * .58f, radius * .20f))
        drawOval(Color(0x66FFFFFF), topLeft = Offset(center.x - radius * .25f, center.y - radius * .73f), size = Size(radius * .16f, radius * .07f))
        drawCircle(Color(0xFF160D15), radius, center, style = Stroke(size.minDimension * .025f))
    } else {
        drawCircle(Color(0xFF21151B), radius, center, style = Stroke(size.minDimension * .035f))
    }
}
