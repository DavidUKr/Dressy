package com.example.dressyui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import coil.compose.rememberAsyncImagePainter
import com.example.dressyui.ui.theme.DressyUITheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DressyUITheme {
                MainScreen()
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var showGeneratedImages by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            selectedImageUri = uri
        }
    )

    val sampleImages = listOf(
        R.drawable.outfit1,
        R.drawable.outfit2,
        R.drawable.outfit3
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.dressy_logo),
                            contentDescription = "App Logo",
                            modifier = Modifier.size(30.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Welcome to Dressy!",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color(0xFFFEC5BB),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            context.startActivity(Intent(context, LoginActivity::class.java))
                        }
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.user_icon),
                            contentDescription = "Profile",
                            modifier = Modifier.size(30.dp),
                            contentScale = ContentScale.Crop
                        )
                    }

                    var menuExpanded by remember { mutableStateOf(false) }

                    IconButton(onClick = { menuExpanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "Menu"
                        )
                    }
                    DropdownMenu(
                        expanded = menuExpanded,
                        onDismissRequest = { menuExpanded = false }
                    ) {
                        DropdownMenuItem(
                            text = { Text("Your Outfits") },
                            onClick = {
                                menuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Category") },
                            onClick = {
                                menuExpanded = false
                            }
                        )
                        DropdownMenuItem(
                            text = { Text("Style") },
                            onClick = {
                                menuExpanded = false
                            }
                        )
                    }
                }
            )
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF8EDEB))
                    .padding(padding),
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                ) {
                    Text(
                        text = "Select Image",
                        fontSize = 27.sp,
                        color = Color(0xFFFEC5BB),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Image(
                        painter = if (selectedImageUri != null) {
                            rememberAsyncImagePainter(selectedImageUri)
                        } else {
                            painterResource(id = R.drawable.image_placeholder)
                        },
                        contentDescription = "Selected Image",
                        modifier = Modifier
                            .size(170.dp)
                            .clickable { imagePickerLauncher.launch("image/*") },
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { showGeneratedImages = true },
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ) {
                        Text(text = "GENERATE", fontSize = 16.sp)
                    }

                    if (showGeneratedImages) {
                        GeneratedImagesSection(
                            sampleImages = sampleImages,
                            context = context
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun GeneratedImagesSection(sampleImages: List<Int>, context: android.content.Context) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Generated Outfits",
            fontSize = 20.sp,
            color = Color(0xFFFEC5BB),
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            sampleImages.forEach { imageRes ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = "Generated Outfit",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 16.dp)
                            .background(Color.Gray),
                        contentScale = ContentScale.Crop
                    )

                    Button(
                        onClick = {
                            saveImageToGallery(context, imageRes)
                            Toast.makeText(context, "Image saved!", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier
                    ) {
                        Text(text = "Save")
                    }
                }
            }
        }
    }
}

fun saveImageToGallery(context: android.content.Context, imageRes: Int) {
    val drawable = context.resources.getDrawable(imageRes, null)
    val bitmap = (drawable as android.graphics.drawable.BitmapDrawable).bitmap

    val savedUri = MediaStore.Images.Media.insertImage(
        context.contentResolver,
        bitmap,
        "outfit",
        "Generated outfit by Dressy"
    )

    if (savedUri == null) {
        Toast.makeText(context, "Failed to save image", Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    DressyUITheme {
        MainScreen()
    }
}
