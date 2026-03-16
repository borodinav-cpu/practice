package ci.nsu.mobile.main

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MainScreen()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var textFieldValue by remember { mutableStateOf("") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Laboratory 3") }
            )
        },
        bottomBar = {
            BottomNavigationBar(
                navController = navController,
                currentRoute = currentRoute ?: Screen.Home.route
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Intent переход на SecondActivity",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = textFieldValue,
                onValueChange = { textFieldValue = it },
                label = { Text("Введите текст для передачи") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val intent = Intent(context, SecondActivity::class.java).apply {
                        putExtra("EXTRA_TEXT", textFieldValue)
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Перейти на второй экран")
            }

            Spacer(modifier = Modifier.height(32.dp))

            Divider()

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Навигация через BottomBar:",
                style = MaterialTheme.typography.titleSmall
            )

            Text(
                text = "👇 Используйте нижнее меню",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}