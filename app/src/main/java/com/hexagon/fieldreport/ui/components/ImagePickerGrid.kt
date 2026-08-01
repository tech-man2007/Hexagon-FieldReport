package com.hexagon.fieldreport.ui.components

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

data class SitePhoto(
    val uri: Uri? = null,
    val caption: String = ""
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ImagePickerGrid(
    photos: List<SitePhoto>,
    onAddPhotoClick: () -> Unit,
    onPhotoClick: (Int) -> Unit,
    onCaptionChange: (index: Int, newCaption: String) -> Unit,
    maxPhotos: Int = 10
) {
    val isDarkTheme = isSystemInDarkTheme()

    // FIX: Force neutral colors instead of relying on the purple Material defaults
    val addPhotoBgColor = if (isDarkTheme) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.05f)
    val addPhotoIconColor = if (isDarkTheme) Color.White else Color.Black

    // Explicit text and accent colors for the OutlinedTextField
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val accentColor = if (isDarkTheme) Color.Cyan else Color(0xFF0000FF)

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (photos.size < maxPhotos) {
            item {
                Card(
                    onClick = onAddPhotoClick,
                    modifier = Modifier
                        .width(140.dp)
                        .height(200.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = addPhotoBgColor // Uses our neutral gray now
                    )
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            Icons.Default.Add,
                            contentDescription = "Add Photo",
                            modifier = Modifier.size(36.dp),
                            tint = addPhotoIconColor // Forces neutral icon color
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Add Photo",
                            style = MaterialTheme.typography.labelLarge,
                            color = addPhotoIconColor
                        )
                        Text(
                            "${photos.size}/$maxPhotos",
                            style = MaterialTheme.typography.labelSmall,
                            color = addPhotoIconColor.copy(alpha = 0.6f)
                        )
                    }
                }
            }
        }

        itemsIndexed(photos) { index, photo ->
            Column(modifier = Modifier.width(200.dp)) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(12.dp))
                        // FIX: Uses strict neutral black/white alpha instead of generic "Gray"
                        .background(if (isDarkTheme) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.05f))
                        .clickable { onPhotoClick(index) },
                    contentAlignment = Alignment.Center
                ) {
                    if (photo.uri != null) {
                        AsyncImage(
                            model = photo.uri,
                            contentDescription = "Selected Site Image",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    } else {
                        Text(
                            "Tap to select image",
                            color = if (isDarkTheme) Color.LightGray else Color.DarkGray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = photo.caption,
                    onValueChange = { newCaption -> onCaptionChange(index, newCaption) },
                    placeholder = { Text("Enter caption...", color = textColor.copy(alpha = 0.6f)) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodySmall.copy(color = textColor),
                    // Forces the text field to use neutral borders and correct text colors based on the theme
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor,
                        unfocusedBorderColor = if (isDarkTheme) Color.White.copy(alpha = 0.3f) else Color.Black.copy(alpha = 0.3f),
                        focusedBorderColor = accentColor,
                        cursorColor = accentColor
                    )
                )
            }
        }
    }
}