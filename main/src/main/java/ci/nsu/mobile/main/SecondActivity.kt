    package ci.nsu.mobile.main
    import android.os.Bundle
    import androidx.activity.ComponentActivity
    import androidx.activity.compose.setContent
    import androidx.compose.foundation.layout.*
    import androidx.compose.material.icons.Icons
    import androidx.compose.material.icons.filled.ArrowBack
    import androidx.compose.material3.*
    import androidx.compose.runtime.Composable
    import androidx.compose.ui.Alignment
    import androidx.compose.ui.Modifier
    import androidx.compose.ui.unit.dp

    class SecondActivity : ComponentActivity() {
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)


            val receivedText = intent.getStringExtra("EXTRA_TEXT") ?: "Нет данных"

            setContent {
                MaterialTheme {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        SecondScreen(
                            receivedText = receivedText,
                            onBackClick = { finish() }  // ← Возврат назад
                        )
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    fun SecondScreen(
        receivedText: String,
        onBackClick: () -> Unit
    ) {

        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("Второй экран") },
                    navigationIcon = {
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Назад"
                            )
                        }
                    }
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
                    text = "Полученные данные:",
                    style = MaterialTheme.typography.titleLarge
                )

                Spacer(modifier = Modifier.height(16.dp))


                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text = receivedText,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            }
        }
    }
