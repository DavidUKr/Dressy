package com.example.dressyui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
    val context = LocalContext.current
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? ->
            selectedImageUri = uri
        }
    )
    var menuExpanded by remember { mutableStateOf(false) }
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
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp)
                ) {

                    Text(
                        text = "Select Image",
                        fontSize = 27.sp,
                        color = Color(0xFFFEC5BB),
                        fontWeight = FontWeight.Bold
                    )

                    // Display the selected image or a placeholder if none selected
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

                    Button(
                        onClick = { /* Action to generate */ },
                        modifier = Modifier.fillMaxWidth(0.5f)
                    ) {
                        Text(text = "GENERATE",  fontSize = 16.sp)
                    }
                }
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun MainScreenPreview() {
    DressyUITheme {
        MainScreen()
    }
}
