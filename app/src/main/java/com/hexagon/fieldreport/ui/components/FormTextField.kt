package com.hexagon.fieldreport.ui.components

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun FormTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    minLines: Int = 1,
    isError: Boolean = false
) {
    val isDark = isSystemInDarkTheme()
    val textColor = if (isDark) Color.White else Color.Black
    val borderColor = if (isDark) Color.White.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.3f)

    // FIX: Cyan for Dark Mode, Pure Blue for Light Mode
    val accentColor = if (isDark) Color.Cyan else Color(0xFF0000FF)
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        minLines = minLines,
        isError = isError,
        shape = MaterialTheme.shapes.medium,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = textColor,
            unfocusedTextColor = textColor,
            focusedBorderColor = accentColor,
            unfocusedBorderColor = borderColor,
            focusedLabelColor = accentColor,
            unfocusedLabelColor = textColor.copy(alpha = 0.7f),
            errorBorderColor = Color.Red,
            errorLabelColor = Color.Red
        )
    )
}