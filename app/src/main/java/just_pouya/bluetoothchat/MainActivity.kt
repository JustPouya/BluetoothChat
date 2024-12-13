package just_pouya.bluetoothchat

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import dagger.hilt.android.AndroidEntryPoint
import just_pouya.bluetoothchat.nav.Screen
import just_pouya.bluetoothchat.ui.theme.BluetoothChatTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val BLUETOOTH_PERMISSIONS = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        listOf(
            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
    } else {
        listOf(
            Manifest.permission.BLUETOOTH,
            Manifest.permission.BLUETOOTH_ADMIN
        )
    }

    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val navController = rememberNavController()
            val permissionsState = rememberMultiplePermissionsState(
                permissions = BLUETOOTH_PERMISSIONS
            )
            LaunchedEffect(Unit) {
                if (!permissionsState.allPermissionsGranted) {
                    permissionsState.launchMultiplePermissionRequest()
                }
            }
            BluetoothChatTheme {

                when {
                    permissionsState.allPermissionsGranted -> {
                        MainGraph(navController)
                    }
                    permissionsState.shouldShowRationale -> {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {
                            Text("We Need Bluetooth Permissions To Continue")
                            Button(onClick = { permissionsState.launchMultiplePermissionRequest() }) {
                                Text("Request")
                            }
                        }
                    }
                    else -> {

                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = "Ok.")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun MainGraph(navController: NavHostController) {
    NavHost(navController, startDestination = Screen.MainScreen) {
        composable<Screen.ChatScreen> { backStackEntry ->
            val contactName:String = (backStackEntry.toRoute() as Screen.ChatScreen).contactName
            ChatScreen(
                contactName=contactName,
                onBackClicked = {
                    navController.popBackStack()
                }
            )
        }
        composable<Screen.MainScreen> {
            MainScreen(
                onNavigateToChatScreen = {name,macAddress->
                    navController.navigate(
                        route = Screen.ChatScreen(name,macAddress)
                    )
                }
            )
        }
    }
}

@Composable
fun ChatScreen(contactName: String, onBackClicked: () -> Boolean) {

}

@Composable
fun MainScreen(onNavigateToChatScreen: (String, String) -> Unit) {

}
