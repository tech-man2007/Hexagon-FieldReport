package com.hexagon.fieldreport.ui.features.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.hexagon.fieldreport.R
import com.hexagon.fieldreport.ui.components.GlassContainer

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    val isDark = isSystemInDarkTheme()

    val backgroundGradient = if (isDark) {
        Brush.verticalGradient(listOf(Color(0xFF263238), Color(0xFF101416)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFFECEFF1), Color(0xFFB0BEC5)))
    }

    val textColor = if (isDark) Color.White else Color.Black
    val placeholderColor = if (isDark) Color.LightGray else Color.DarkGray
    val constructionAccent = Color(0xFF0000FF)

    val forgotPasswordColor = if (isDark) Color.White else constructionAccent

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient),
        contentAlignment = Alignment.Center
    ) {
        GlassContainer(
            modifier = Modifier.fillMaxWidth(0.85f).padding(16.dp),
            cornerRadius = 24.dp
        ) {
            Column(
                modifier = Modifier.padding(32.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo increased by ~5x for a prominent display
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Company Logo",
                    modifier = Modifier
                        .size(130.dp)
                        .padding(bottom = 16.dp)
                )

                // Hexagon India in Times New Roman (Serif Bold) on line 1
                Text(
                    text = "Hexagon India",
                    color = textColor,
                    fontSize = 26.sp,
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 5.dp)
                )

                // Field Report on line 2
                Text(
                    text = "Field Report",
                    color = textColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Manager ID", color = placeholderColor) },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        focusedBorderColor = constructionAccent,
                        unfocusedBorderColor = textColor.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password", color = placeholderColor) },
                    visualTransformation = PasswordVisualTransformation(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        focusedBorderColor = constructionAccent,
                        unfocusedBorderColor = textColor.copy(alpha = 0.3f)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )

                // Forgot Password Button
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                    TextButton(onClick = { /* TODO: Forgot Password Logic */ }) {
                        Text("Forgot Password?", color = forgotPasswordColor, fontSize = 12.sp)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onLoginSuccess,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = constructionAccent,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth().height(50.dp)
                ) {
                    Text("Secure Login", fontWeight = FontWeight.Bold, color = Color.White)
                }
            }
        }
    }
}