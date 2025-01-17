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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.dressyui.AuthManager.getToken
import com.example.dressyui.ui.theme.DressyUITheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream


class Home : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun AppNavigation(isLoggedIn: Boolean) {
    val navController = rememberNavController()
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    val bottomNavRoutes = listOf("main", "your_outfits", "profile")

    Scaffold(
        bottomBar = {
            if (navController.currentBackStackEntry?.destination?.route in bottomNavRoutes) {
                BottomNavigationBar(navController)
            }
        }
    ) {
        NavHost(
            navController = navController,
            startDestination = if (isLoggedIn) "main" else "landing",
            modifier = Modifier.fillMaxSize()
        ) {
            composable("landing") {
                LandingScreen(
                    onNavigateToLogin = { navController.navigate("login") },
                    onNavigateToSignup = { navController.navigate("signup") }
                )
            }

            composable("login") {
                var isLoading by remember { mutableStateOf(false) }
                var errorMessage by remember { mutableStateOf("") }
                val context = LocalContext.current

                LoginScreen(
                    onLoginClick = { username, password ->
                        if (username.isBlank() || password.isBlank()) {
                            errorMessage = "Username and password cannot be empty"
                            return@LoginScreen
                        }

                        isLoading = true
                        errorMessage = ""

                        AuthManager.loginUser(
                            context = context,
                            username = username,
                            password = password,
                            callback = object : AuthManager.AuthCallback {
                                override fun onSuccess(token: String) {
                                    isLoading = false
                                    navController.navigate("main") {
                                        popUpTo("login") { inclusive = true }
                                    }
                                }

                                override fun onFailure(error: String) {
                                    isLoading = false
                                    errorMessage = error
                                }
                            }
                        )
                    },
                    onSignUpClick = { navController.navigate("signup") },
                    isLoading = isLoading,
                    errorMessage = errorMessage
                )
            }

            composable("signup") {
                val context = LocalContext.current
                SignUpScreen(
                    isLoading = isLoading,
                    errorMessage = errorMessage,
                    onSignUpClick = { username, email, password ->
                        isLoading = true
                        errorMessage = ""

                        AuthManager.signupUser(
                            context = context,
                            username = username,
                            email = email,
                            password = password,
                            callback = object : AuthManager.AuthCallback {
                                override fun onSuccess(message: String) {
                                    isLoading = false
                                    Toast.makeText(
                                        context,
                                        "Signup Successful: $message",
                                        Toast.LENGTH_LONG
                                    ).show()

                                    navController.navigate("login") {
                                        popUpTo("signup") { inclusive = true }
                                    }
                                }

                                override fun onFailure(errorMessage: String) {
                                    isLoading = false
                                    //this@composable.errorMessage = errorMessage
                                }
                            }
                        )
                    },
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }


            composable("main") {
                MainScreen(navController = navController)
            }

            composable("your_outfits") {
                YourOutfitsScreen(navController = navController)
            }

            composable("profile") {
                ProfileScreen(navController = navController)
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(navController: NavController) {
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var generatedImage by remember { mutableStateOf<Bitmap?>(null) }
    var userPrompt by remember { mutableStateOf("") }
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }
    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent(),
        onResult = { uri: Uri? -> selectedImageUri = uri }
    )

    var selectedStyle by remember { mutableStateOf("Casual") }
    val styles = listOf("Casual", "Formal", "Classic", "Chic")

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
        containerColor = Color(0xFFF8EDEB),
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
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Select Style",
                        fontSize = 23.sp,
                        color = Color(0xFFE27239),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.padding(horizontal = 16.dp)
                    ) {
                        items(styles) { style ->
                            Button(
                                onClick = { selectedStyle = style },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (selectedStyle == style) Color(0xFFE27239) else Color(0xFFa7a7a7),
                                    contentColor = Color.White
                                )
                            ) {
                                Text(text = style)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Enter a Prompt",
                        fontSize = 23.sp,
                        color = Color(0xFFE27239),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    TextField(
                        value = userPrompt,
                        onValueChange = { userPrompt = it },
                        label = { Text("Tell us what you want") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                            .height(50.dp)
                            .clip(RoundedCornerShape(12.dp)),
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White,
                            focusedIndicatorColor = Color(0xFFE27239),
                            focusedLabelColor = Color(0xFFE27239),
                            cursorColor = Color(0xFFE27239)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Select Image",
                        fontSize = 23.sp,
                        color = Color(0xFFE27239),
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(20.dp))

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
                        onClick = { imagePickerLauncher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFFE27239),
                            contentColor = Color.White
                        )
                    ) {
                        Text("Pick Image")
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    selectedImageUri?.let { uri ->
                        Button(
                            onClick = {
                                isLoading = true
                                context.contentResolver.openInputStream(uri)?.use { inputStream ->
                                    val originalBitmap = BitmapFactory.decodeStream(inputStream)
                                    val resizedBitmap = resizeBitmap(originalBitmap, 512, 512)
                                    val base64Image = encodeImageToBase64(resizedBitmap)

                                    sendImageGenerationRequest(
                                        context,
                                        base64Image,
                                        selectedStyle,
                                        userPrompt
                                    ) { responseBitmap ->
                                        generatedImage = responseBitmap
                                        isLoading = false
                                    }
                                } ?: run {
                                    isLoading = false
                                    Toast.makeText(context, "Failed to load image.", Toast.LENGTH_SHORT).show()
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFE27239),
                                contentColor = Color.White
                            )
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(20.dp),
                                    color = Color.White
                                )
                            } else {
                                Text("Generate Image")
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    generatedImage?.let { bitmap ->
                        Text("Generated Image:")
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "Generated Image",
                            modifier = Modifier.size(200.dp)
                        )
                    }
                }
            }
        }
    )
}

fun encodeImageToBase64(bitmap: Bitmap): String {
    val byteArrayOutputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream)
    val byteArray = byteArrayOutputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.NO_WRAP)
}
fun sendImageGenerationRequest(
    context: Context,
    base64Image: String,
    selectedStyle: String,
    userPrompt: String,
    onResult: (Bitmap?) -> Unit
) {
    val token = getToken(context)
    if (token.isNullOrEmpty()) {
        Log.e("Auth", "Token missing or invalid.")
        return
    }

    val apiService = ApiClient.getRetrofitWithToken(token).create(ApiService::class.java)

    CoroutineScope(Dispatchers.IO).launch {
        try {
            val request = ImageGenerationRequest(
                input_image = base64Image,
                style = selectedStyle,
                user_prompt = userPrompt,
                results_count = 1
            )

            val response = com.example.dressyui.apiService.generateImage(request, "Bearer $token")

            val base64ImageResponse = response.generated_image
            val decodedBytes = Base64.decode(base64ImageResponse, Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)

            withContext(Dispatchers.Main) {
                onResult(bitmap)
            }
        } catch (e: Exception) {
            Log.e("ImageGeneration", "Error: ${e.message}")
            withContext(Dispatchers.Main) {
                onResult(null)
                Toast.makeText(context, "Failed to generate image: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}


fun resizeBitmap(bitmap: Bitmap, width: Int, height: Int): Bitmap {
    return Bitmap.createScaledBitmap(bitmap, width, height, true)
}

@Composable
fun BottomNavigationBar(navController: NavController) {
    val currentRoute = navController.currentBackStackEntry?.destination?.route
    val context = LocalContext.current

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview()
    ) { bitmap ->
        if (bitmap != null) {
            Toast.makeText(context, "Picture taken!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "Camera canceled", Toast.LENGTH_SHORT).show()
        }
    }

    NavigationBar(
        containerColor = Color.White,
        contentColor = Color(0xFFFEC5BB)
    ) {
        NavigationBarItem(
            selected = currentRoute == "main",
            onClick = {
                navController.navigate("main") {
                    popUpTo("main") { inclusive = true }
                    launchSingleTop = true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Home",
                    tint = if (currentRoute == "main") Color(0xFFFEC5BB) else Color.Gray
                )
            },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = currentRoute == "your_outfits",
            onClick = {
                navController.navigate("your_outfits") {
                    popUpTo("main") { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Your Outfits",
                    tint = if (currentRoute == "your_outfits") Color(0xFFFEC5BB) else Color.Gray
                )
            },
            label = { Text("Favorites") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {
                cameraLauncher.launch(null)
            },
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
            selected = currentRoute == "profile",
            onClick = {
                navController.navigate("profile") {
                    popUpTo("main") { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Profile",
                    tint = if (currentRoute == "profile") Color(0xFFFEC5BB) else Color.Gray
                )
            },
            label = { Text("Profile") }
        )
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
