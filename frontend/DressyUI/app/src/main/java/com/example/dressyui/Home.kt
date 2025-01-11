package com.example.dressyui

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Base64
import android.util.Log
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
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.dressyui.ui.theme.DressyUITheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


class Home : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Retrieve login status from SharedPreferences
        val sharedPref = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)

        Log.d("HomeActivity", "isLoggedIn: $isLoggedIn")

        setContent {
            DressyUITheme {
                AppNavigation(isLoggedIn = isLoggedIn)
            }
        }
    }
}

@Composable
fun AppNavigation(isLoggedIn: Boolean) {
    val navController = rememberNavController()
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    NavHost(
        navController = navController,
        startDestination = if (isLoggedIn) "main" else "landing"
    ) {
        composable("landing") {
            LandingScreen(
                onNavigateToLogin = { navController.navigate("login") },
                onNavigateToSignup = { navController.navigate("signup") }
            )
        }

        composable("login") {
            // Pass the state values for loading and error message to the LoginScreen
            LoginScreen(
                onLoginClick = { username, password ->
                    // Handle login logic
                    val loginSuccessful = true // Replace with actual login logic
                    if (loginSuccessful) {
                        navController.navigate("main") {
                            popUpTo("login") { inclusive = true } // Clear login from back stack
                        }
                    } else {
                        errorMessage = "Login failed" // Show error message on failure
                    }
                },
                onSignUpClick = { navController.navigate("signup") },
                isLoading = isLoading,
                errorMessage = errorMessage
            )
        }

        composable("signup") {
            SignUpScreen(
                isLoading = isLoading, // Manage state here
                errorMessage = errorMessage,
                onSignUpClick = { username, email, password ->
                    // Simulate signup logic here
                    val signupSuccessful = true // Replace with actual signup logic
                    if (signupSuccessful) {
                        navController.navigate("login") {
                            popUpTo("signup") { inclusive = true } // Clear signup from back stack
                        }
                    } else {
                        errorMessage = "Signup failed" // Show error message
                    }
                },
                onBackClick = {
                    navController.popBackStack() // Go back to the landing screen
                }
            )
        }

        composable("main") {
            MainScreen(navController = navController)
        }

        // Other Screens (e.g., Profile, Your Outfits)
        composable("your_outfits") {
            YourOutfitsScreen(navController = navController)
        }
        composable("profile") {
            ProfileScreen(navController = navController)
        }
    }
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var generatedImage by remember { mutableStateOf<Bitmap?>(null) }
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> selectedImageUri = uri }
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
                            color = Color(0xFFE27239),
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(top = 10.dp)
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { navController.navigate("login") }) {
                        Image(
                            painter = painterResource(id = R.drawable.user_icon),
                            contentDescription = "Profile",
                            modifier = Modifier.size(30.dp),
                            contentScale = ContentScale.Crop
                        )
                    }


                }
            )
        },
        bottomBar = {
            BottomNavigationBar(navController = navController)
        },
        content = { padding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF8EDEB))
                    .padding(padding)
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 20.dp)
                ) {
                    Text(
                        text = "Select Image",
                        fontSize = 27.sp,
                        color = Color(0xFFE27239),
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

                    Button(onClick = { imagePickerLauncher.launch("image/*") }) {
                        Text("Pick Image")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    selectedImageUri?.let { uri ->
                        Button(onClick = {
                            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                                val bitmap = BitmapFactory.decodeStream(inputStream)
                                val base64Image = encodeImageToBase64(bitmap)
                                sendImageGenerationRequest(context, base64Image) { responseBitmap ->
                                    generatedImage = responseBitmap
                                }
                            }
                        }) {
                            Text("Generate Image")
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Display generated image if available
                    generatedImage?.let { bitmap ->
                        Text("Generated Image:")
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = null,
                            modifier = Modifier.size(200.dp)
                        )
                    }
                }
            }
        }
    )
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = Color(0xFFFEC5BB)
    ) {
        NavigationBarItem(
            selected = navController.currentBackStackEntry?.destination?.route == "main",
            onClick = {
                navController.navigate("main") {
                    // Avoid multiple copies of the same destination
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = if (navController.currentBackStackEntry?.destination?.route == "main")
                        Color(0xFFFEC5BB) else Color.Gray
                )
            },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = navController.currentBackStackEntry?.destination?.route == "your_outfits",
            onClick = {
                navController.navigate("your_outfits") {
                    popUpTo(navController.graph.startDestinationId) { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Your Outfits",
                    tint = if (navController.currentBackStackEntry?.destination?.route == "your_outfits")
                        Color(0xFFFEC5BB) else Color.Gray
                )
            },
            label = { Text("Favorites") }
        )
        NavigationBarItem(
            selected = false,
            onClick = { /* Navigate to Camera */ },
            icon = {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Camera",
                    tint = Color.Gray
                )
            },
            label = { Text("Camera") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {  navController.navigate("profile")  },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = Color.Gray
                )
            },
            label = { Text("Profile") }
        )
    }
}

// Function to encode Bitmap to Base64
fun encodeImageToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

// Function to send image generation request
fun sendImageGenerationRequest(context: Context, base64Image: String, onResult: (Bitmap) -> Unit) {
    val sharedPref = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
    val token = sharedPref.getString("auth_token", null)

    if (token.isNullOrEmpty()) {
        Log.e("Auth", "Token missing or invalid.")
        return
    }
    Log.d("Auth", "Token: $token")
    Log.d("Request", "Sending request with base64 image of size: ${base64Image.length}")


    val request = ImageGenerationRequest(
        input_image = base64Image,
        style = "classic rock",
        user_prompt = "Casual",
        results_count = 1
    )

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val response = apiService.generateImage(request, "Bearer $token")
            val decodedBytes = Base64.decode(response.generated_image, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

            withContext(Dispatchers.Main) {
                onResult(bitmap)
            }
        } catch (e: Exception) {
            Log.e("ImageGeneration", "Error: ${e.message}")
        }
    }
}



@Composable
fun GeneratedImagesSection(generatedOutfits: List<String>, context: Context) {
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
            generatedOutfits.forEach { imageUrl ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    AsyncImage(
                        model = imageUrl,
                        contentDescription = "Generated Outfit",
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = 16.dp)
                            .background(Color.Gray),
                        contentScale = ContentScale.Crop
                    )

                    Button(
                        onClick = {
                            Toast.makeText(context, "Save feature not implemented yet!", Toast.LENGTH_SHORT).show()
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


fun saveImageToGallery(context: Context, imageRes: Int) {
    val drawable = context.resources.getDrawable(imageRes, null)
    val bitmap = (drawable as BitmapDrawable).bitmap

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
    val navController = rememberNavController()
    DressyUITheme {
        MainScreen(navController = navController)
    }
}
