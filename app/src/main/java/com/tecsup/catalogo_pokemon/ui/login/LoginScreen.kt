package com.tecsup.catalogo_pokemon.ui.login

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val LoginRed = Color(0xFFE52D3D)
private val LoginInk = Color(0xFF252B35)
private val LoginSoftRed = Color(0xFFFFE7E9)

@Composable
fun LoginScreen(
    isLoading: Boolean,
    errorMessage: String?,
    onIniciarSesion: (correo: String, contrasena: String) -> Unit,
    loadingLabel: String = "Ingresando…",
    onVolver: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var correo by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF8F5FA)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 26.dp, vertical = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(Modifier.fillMaxWidth().height(42.dp), contentAlignment = Alignment.CenterStart) {
                if (onVolver != null) {
                    TextButton(
                        onClick = onVolver,
                        enabled = !isLoading,
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 4.dp, vertical = 6.dp)
                    ) {
                        Text("‹  Volver", color = LoginRed, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                    }
                }
            }

            Spacer(Modifier.height(28.dp))
            Box(
                modifier = Modifier
                    .size(76.dp)
                    .clip(CircleShape)
                    .background(LoginSoftRed),
                contentAlignment = Alignment.Center
            ) {
                Canvas(Modifier.size(49.dp)) { drawLoginPokeball(size) }
            }
            Text(
                text = "Pokédex",
                modifier = Modifier.padding(top = 18.dp),
                color = LoginInk,
                fontSize = 31.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-.5).sp
            )
            Text(
                text = "Inicia sesión para continuar",
                modifier = Modifier.padding(top = 5.dp, bottom = 32.dp),
                color = Color(0xFF777781),
                fontSize = 16.sp
            )

            Text(
                "CORREO ELECTRÓNICO",
                modifier = Modifier.fillMaxWidth().padding(start = 3.dp, bottom = 8.dp),
                color = Color(0xFF626570), fontSize = 12.sp,
                fontWeight = FontWeight.Bold, letterSpacing = 1.sp
            )
            OutlinedTextField(
                value = correo,
                onValueChange = { correo = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("nombre@correo.com") },
                leadingIcon = { Text("✉", color = LoginRed, fontSize = 18.sp) },
                singleLine = true,
                enabled = !isLoading,
                shape = RoundedCornerShape(15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LoginRed,
                    unfocusedBorderColor = Color(0xFFE2DEE5),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = LoginInk,
                    unfocusedTextColor = LoginInk
                ),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email, imeAction = ImeAction.Next)
            )

            Spacer(Modifier.height(20.dp))
            Text(
                "CONTRASEÑA",
                modifier = Modifier.fillMaxWidth().padding(start = 3.dp, bottom = 8.dp),
                color = Color(0xFF626570), fontSize = 12.sp,
                fontWeight = FontWeight.Bold, letterSpacing = 1.sp
            )
            OutlinedTextField(
                value = contrasena,
                onValueChange = { contrasena = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Ingresa tu contraseña") },
                leadingIcon = { Text("●", color = LoginRed, fontSize = 14.sp) },
                singleLine = true,
                enabled = !isLoading,
                shape = RoundedCornerShape(15.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = LoginRed,
                    unfocusedBorderColor = Color(0xFFE2DEE5),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedTextColor = LoginInk,
                    unfocusedTextColor = LoginInk
                ),
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { onIniciarSesion(correo, contrasena) })
            )

            errorMessage?.let { error ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFFFFE9E9))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("!", color = LoginRed, fontWeight = FontWeight.Bold)
                    Text(
                        error,
                        modifier = Modifier.padding(start = 9.dp),
                        color = Color(0xFF9E2832), fontSize = 14.sp
                    )
                }
            }

            Button(
                onClick = { onIniciarSesion(correo, contrasena) },
                modifier = Modifier.fillMaxWidth().padding(top = 28.dp).height(56.dp),
                enabled = !isLoading,
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = LoginRed),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 3.dp)
            ) {
                if (isLoading) {
                    CircularProgressIndicator(Modifier.size(20.dp), strokeWidth = 2.dp, color = Color.White)
                    Text(loadingLabel, modifier = Modifier.padding(start = 10.dp), fontWeight = FontWeight.Bold)
                } else {
                    Text("Iniciar sesión", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }

            Text(
                "Tu aventura Pokémon te espera",
                modifier = Modifier.fillMaxWidth().padding(top = 26.dp, bottom = 28.dp),
                color = Color(0xFF88828B), fontSize = 13.sp,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun androidx.compose.ui.graphics.drawscope.DrawScope.drawLoginPokeball(size: Size) {
    val center = Offset(size.width / 2f, size.height / 2f)
    val radius = size.minDimension * .46f
    val circle = androidx.compose.ui.geometry.Rect(center.x - radius, center.y - radius, center.x + radius, center.y + radius)
    drawCircle(Color(0xFF23212A), radius, center)
    drawCircle(Color.White, radius * .91f, center)
    val upper = Path().apply {
        moveTo(center.x - radius * .91f, center.y)
        arcTo(circle, 180f, 180f, false)
        close()
    }
    drawPath(upper, LoginRed)
    drawRect(Color(0xFF23212A), Offset(center.x - radius, center.y - size.height * .055f), Size(radius * 2f, size.height * .11f))
    drawCircle(Color(0xFF23212A), radius * .27f, center)
    drawCircle(Color.White, radius * .17f, center)
    drawCircle(Color(0xFF23212A), radius, center, style = Stroke(size.minDimension * .035f))
}
