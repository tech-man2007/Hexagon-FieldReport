package com.hexagon.fieldreport.ui.features.dailyreport

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.hexagon.fieldreport.ui.components.FormTextField
import com.hexagon.fieldreport.ui.components.ImagePickerGrid
import com.hexagon.fieldreport.ui.components.SitePhoto
import java.io.File
import java.io.FileOutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DailyReportScreen() {
    var materialsReceived by remember { mutableStateOf("") }
    var siteLogistics by remember { mutableStateOf("") }
    var siteChanges by remember { mutableStateOf("") }
    var redFlags by remember { mutableStateOf("") }
    var sitePhotos by remember { mutableStateOf(listOf<SitePhoto>()) }
    var selectedImageIndex by remember { mutableIntStateOf(-1) }
    var showSourceDialog by remember { mutableStateOf(false) }

    var currentDateTime by remember { mutableStateOf("Fetching time...") }
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm a")
        currentDateTime = LocalDateTime.now().format(formatter)
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null && selectedImageIndex != -1) {
            try {
                val file = File(context.cacheDir, "camera_img_${System.currentTimeMillis()}.jpg")
                val stream = FileOutputStream(file)
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, stream)
                stream.flush()
                stream.close()
                val uri = Uri.fromFile(file)

                val updatedList = sitePhotos.toMutableList()
                updatedList[selectedImageIndex] = updatedList[selectedImageIndex].copy(uri = uri)
                sitePhotos = updatedList
                selectedImageIndex = -1
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            cameraLauncher.launch(null)
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null && selectedImageIndex != -1) {
            val updatedList = sitePhotos.toMutableList()
            updatedList[selectedImageIndex] = updatedList[selectedImageIndex].copy(uri = uri)
            sitePhotos = updatedList
            selectedImageIndex = -1
        }
    }

    val isDarkTheme = isSystemInDarkTheme()
    val textColor = if (isDarkTheme) Color.White else Color.Black
    val accentColor = if (isDarkTheme) Color.Cyan else Color(0xFF0000FF)
    val cardBgColor = if (isDarkTheme) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.05f)

    Scaffold(
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(0.dp),
        topBar = {
            TopAppBar(
                title = { Text("New Daily Report", color = textColor) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp),
            contentPadding = PaddingValues(bottom = 120.dp)
        ) {

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(accentColor.copy(alpha = 0.2f))
                        .padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text("Current Timestamp", fontSize = 12.sp, color = textColor.copy(alpha = 0.8f))
                        Text(currentDateTime, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textColor)
                    }
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = "Locked Time",
                        tint = accentColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Text(
                    text = "Site Pictures",
                    color = textColor,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                ImagePickerGrid(
                    photos = sitePhotos,
                    onAddPhotoClick = {
                        sitePhotos = sitePhotos + SitePhoto()
                    },
                    onPhotoClick = { index ->
                        selectedImageIndex = index
                        showSourceDialog = true
                    },
                    onCaptionChange = { index, newCaption ->
                        val updatedList = sitePhotos.toMutableList()
                        updatedList[index] = updatedList[index].copy(caption = newCaption)
                        sitePhotos = updatedList
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Text(
                    text = "Voice Logs",
                    color = textColor,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                VoiceRecordCard(title = "Today's Summary", isDark = isDarkTheme, textColor = textColor, accentColor = accentColor, cardBgColor = cardBgColor)
                Spacer(modifier = Modifier.height(8.dp))
                VoiceRecordCard(title = "Tomorrow's Plan", isDark = isDarkTheme, textColor = textColor, accentColor = accentColor, cardBgColor = cardBgColor)

                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                FormTextField(
                    label = "Materials Received",
                    value = materialsReceived,
                    onValueChange = { materialsReceived = it },
                    minLines = 3
                )

                FormTextField(
                    label = "Site Instructions & Labor",
                    value = siteLogistics,
                    onValueChange = { siteLogistics = it },
                    minLines = 3
                )

                FormTextField(
                    label = "Design Changes & Constraints",
                    value = siteChanges,
                    onValueChange = { siteChanges = it },
                    minLines = 2
                )

                FormTextField(
                    label = "Red Flags / Urgent Issues",
                    value = redFlags,
                    onValueChange = { redFlags = it },
                    minLines = 2,
                    isError = redFlags.isNotEmpty()
                )

                Spacer(modifier = Modifier.height(32.dp))
            }

            item {
                Button(
                    onClick = { /* TODO: Submit Logic */ },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = accentColor,
                        contentColor = if (isDarkTheme) Color.Black else Color.White
                    )
                ) {
                    Text("Submit Report", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (showSourceDialog) {
        AlertDialog(
            onDismissRequest = { showSourceDialog = false },
            title = { Text("Select Photo Source") },
            text = { Text("Capture a live photo using the camera or choose an existing image from the gallery.") },
            confirmButton = {
                TextButton(onClick = {
                    showSourceDialog = false
                    val hasCameraPermission = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                    if (hasCameraPermission) {
                        cameraLauncher.launch(null)
                    } else {
                        cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                    }
                }) {
                    Text("Camera")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showSourceDialog = false
                    galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                }) {
                    Text("Gallery")
                }
            }
        )
    }
}

@Composable
private fun VoiceRecordCard(
    title: String,
    isDark: Boolean,
    textColor: Color,
    accentColor: Color,
    cardBgColor: Color
) {
    val context = LocalContext.current
    var recordingState by remember { mutableIntStateOf(0) } // 0: Ready, 1: Recording, 2: Recorded (Playable)
    var mediaRecorder by remember { mutableStateOf<MediaRecorder?>(null) }
    var mediaPlayer by remember { mutableStateOf<MediaPlayer?>(null) }
    var audioFilePath by remember { mutableStateOf<String?>(null) }

    val audioPermission = Manifest.permission.RECORD_AUDIO
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val file = File(context.cacheDir, "voice_${title.hashCode()}.3gp")
            audioFilePath = file.absolutePath
            try {
                mediaRecorder = MediaRecorder().apply {
                    setAudioSource(MediaRecorder.AudioSource.MIC)
                    setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                    setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                    setOutputFile(audioFilePath)
                    prepare()
                    start()
                }
                recordingState = 1
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            mediaRecorder?.release()
            mediaPlayer?.release()
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(cardBgColor)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textColor)

            val statusText = when (recordingState) {
                0 -> "Tap to record audio"
                1 -> "Recording in progress..."
                else -> "Audio recorded (Tap to play)"
            }
            val statusColor = if (recordingState == 1) Color.Red else textColor.copy(alpha = 0.6f)

            Text(statusText, fontSize = 12.sp, color = statusColor)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            if (recordingState == 2) {
                IconButton(
                    onClick = {
                        mediaPlayer?.stop()
                        mediaPlayer?.release()
                        mediaPlayer = null
                        recordingState = 0
                    },
                    modifier = Modifier
                        .size(40.dp)
                        .background(textColor.copy(alpha = 0.1f), CircleShape)
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Re-record Audio",
                        tint = textColor,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
            }

            val iconTint = if (recordingState == 1) Color.Red else accentColor
            val iconBg = if (recordingState == 1) Color.Red.copy(alpha = 0.15f) else accentColor.copy(alpha = 0.15f)

            IconButton(
                onClick = {
                    when (recordingState) {
                        0 -> {
                            val hasPermission = ContextCompat.checkSelfPermission(context, audioPermission) == PackageManager.PERMISSION_GRANTED
                            if (hasPermission) {
                                val file = File(context.cacheDir, "voice_${title.hashCode()}.3gp")
                                audioFilePath = file.absolutePath
                                try {
                                    mediaRecorder = MediaRecorder().apply {
                                        setAudioSource(MediaRecorder.AudioSource.MIC)
                                        setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
                                        setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                                        setOutputFile(audioFilePath)
                                        prepare()
                                        start()
                                    }
                                    recordingState = 1
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            } else {
                                permissionLauncher.launch(audioPermission)
                            }
                        }
                        1 -> {
                            try {
                                mediaRecorder?.apply {
                                    stop()
                                    release()
                                }
                                mediaRecorder = null
                                recordingState = 2
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        2 -> {
                            audioFilePath?.let { path ->
                                try {
                                    mediaPlayer?.release()
                                    mediaPlayer = MediaPlayer().apply {
                                        setDataSource(path)
                                        prepare()
                                        start()
                                    }
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                },
                modifier = Modifier
                    .size(48.dp)
                    .background(iconBg, CircleShape)
            ) {
                val currentIcon = when (recordingState) {
                    0 -> Icons.Default.Add
                    1 -> Icons.Default.Close // Core icon replacement for Stop
                    else -> Icons.Default.PlayArrow
                }

                Icon(
                    imageVector = currentIcon,
                    contentDescription = "Voice Log Action",
                    tint = iconTint
                )
            }
        }
    }
}