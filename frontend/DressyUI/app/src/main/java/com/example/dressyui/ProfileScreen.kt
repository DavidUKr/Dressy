package com.example.dressyui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(title = { Text(
                    text = "Profile",
                    style = MaterialTheme.typography.titleLarge,
                    color = Color(0xFFE27239),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 10.dp)) },
                )
        },bottomBar = {
            BottomNavigationBar(navController)
        }

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Avatar",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary

                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = "User Name", style = MaterialTheme.typography.headlineSmall)
                    Text(text = "user.name@gmail.com", style = MaterialTheme.typography.bodyMedium)
                }
            }


            Button(
                onClick = {
                    navController.navigate("editProfile")
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Edit Profile")
            }

            // Preferences Section
            Text(text = "Preferences", style = MaterialTheme.typography.headlineSmall)
            PreferencesSwitch("Push notifications", true)
            PreferencesSwitch("Face ID", true)
        }
    }
}
@Composable
fun BottomProfileNavigationBar(navController: NavController) {
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
                    // Avoid multiple copies of the same destination
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
            selected = true,
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

@Composable
fun PreferencesSwitch(label: String, defaultState: Boolean) {
    var state by remember { mutableStateOf(defaultState) }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label)
        Switch(
            checked = state,
            onCheckedChange = { state = it }
        )
    }
}
