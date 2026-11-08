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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WelcomeScreen(
    onIniciarSesion: () -> Unit,
    isLoading: Boolean = false,
    loadingLabel: String = "Verificando sesión…"
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 26.dp, vertical = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                PokeballMark(Modifier.size(38.dp))
                Text(
                    text = "POKÉDEX",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 2.sp,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

            Spacer(Modifier.height(30.dp))

            val orbRed = MaterialTheme.colorScheme.primary
            val orbYellow = MaterialTheme.colorScheme.tertiary
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .clip(RoundedCornerShape(32.dp))
                    .background(MaterialTheme.colorScheme.secondaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Canvas(Modifier.fillMaxSize()) {
                    drawCircle(
                        color = orbRed.copy(alpha = 0.08f),
                        radius = size.minDimension * 0.39f,
                        center = Offset(size.width * 0.18f, size.height * 0.18f)
                    )
                    drawCircle(
                        color = orbYellow.copy(alpha = 0.2f),
                        radius = size.minDimension * 0.22f,
                        center = Offset(size.width * 0.88f, size.height * 0.82f)
                    )
                }
                PokeballMark(Modifier.size(154.dp))
            }

            Spacer(Modifier.height(28.dp))
            Text(
                text = "Tu aventura Pokémon empieza aquí",
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Explora el catálogo, descubre cada Pokémon y guarda tus favoritos.",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )

            Button(
                onClick = onIniciarSesion,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 30.dp)
                    .height(54.dp),
                enabled = !isLoading,
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(loadingLabel, modifier = Modifier.padding(start = 12.dp))
                } else {
                    Text("Iniciar sesión", fontWeight = FontWeight.Bold)
                    Text("  →", fontSize = 20.sp)
                }
            }

            Text(
                text = "Tu próxima aventura te espera",
                modifier = Modifier.padding(top = 16.dp),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun PokeballMark(modifier: Modifier = Modifier) {
    val red = MaterialTheme.colorScheme.primary
    val dark = MaterialTheme.colorScheme.onBackground
    Canvas(modifier) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val radius = size.minDimension * 0.46f
        drawCircle(color = Color.White, radius = radius, center = center)
        val topHalf = Path().apply {
            moveTo(center.x - radius, center.y)
            arcTo(
                rect = androidx.compose.ui.geometry.Rect(
                    center.x - radius, center.y - radius,
                    center.x + radius, center.y + radius
                ),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 180f,
                forceMoveTo = false
            )
            close()
        }
        drawPath(path = topHalf, color = red)
        drawRect(
            color = dark,
            topLeft = Offset(center.x - radius, center.y - size.height * 0.055f),
            size = Size(radius * 2f, size.height * 0.11f)
        )
        drawCircle(color = dark, radius = radius * 0.28f, center = center)
        drawCircle(color = Color.White, radius = radius * 0.18f, center = center)
        drawCircle(color = dark, radius = radius, center = center, style = Stroke(width = size.minDimension * 0.035f))
    }
}
