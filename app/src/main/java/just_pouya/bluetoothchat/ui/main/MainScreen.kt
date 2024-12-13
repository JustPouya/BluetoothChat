package just_pouya.bluetoothchat.ui.main

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import just_pouya.bluetoothchat.entities.DeviceApp


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    onNavigateToChatScreen: (String, String) -> Unit,
    viewModel: MainViewModel = hiltViewModel()
) {
    val isBluetoothAvailable by viewModel.isBluetoothAvailable
    val devices by viewModel.device.collectAsStateWithLifecycle()
    val discoveredDevices by viewModel.discoveredDevices.collectAsStateWithLifecycle()
    val pairedDevices by viewModel.pairedDevices
    val connectionSucceeded by viewModel.connectionSucceeded.collectAsStateWithLifecycle()

    var showChooseThemeDialog by remember { mutableStateOf(false) }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    val enableBtIntent = remember {
        Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
    }
    val resultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        viewModel.isBluetoothEnabled.value = (result.resultCode == Activity.RESULT_OK)
    }

    SideEffect {
        viewModel.makeServerSocket()
    }

    LaunchedEffect(connectionSucceeded){
        if(connectionSucceeded.first.isNotBlank()){
            onNavigateToChatScreen.invoke(connectionSucceeded.first,connectionSucceeded.second)
            viewModel.resetConnectionSucceeded()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            MainTopBar(scrollBehavior)
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    viewModel.getPairedDevices()
                    showChooseThemeDialog = true
                },
            ) {
                Icon(Icons.Filled.Add, "Add Device")
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            if (isBluetoothAvailable) {
                if (showChooseThemeDialog) {
                    DiscoveryDialog(
                        onDismissRequest = { showChooseThemeDialog = false },
                        onItemClicked = { macAddress ->
                            showChooseThemeDialog = false
                            if (viewModel.isBluetoothEnabled.value) {
                                viewModel.connect(macAddress)
                            } else {
                                resultLauncher.launch(enableBtIntent)
                            }

                        },
                        discoveredDevices = discoveredDevices,
                        onStartDiscoveryRequest = { viewModel.startDiscovery() },
                        onStopDiscoveryRequest = { viewModel.stopDiscovery() },
                        pairedDevices = pairedDevices
                    )
                }

                DevicesList(
                    devices = devices
                ) { macAddress ->
                    if (viewModel.isBluetoothEnabled.value) {
                        viewModel.connect(macAddress)
                    } else {
                        resultLauncher.launch(enableBtIntent)
                    }
                }

            } else {
                Text(text = "Bluetooth is not available on yor device.")
            }
        }
    }
}

@Composable
fun DevicesList(modifier: Modifier=Modifier, devices: List<DeviceApp>, onItemClicked: (String) -> Unit) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        items(devices) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .clickable { onItemClicked.invoke(it.macAddress) },
                text = it.name
            )
            HorizontalDivider()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainTopBar(scrollBehavior: TopAppBarScrollBehavior) {
    TopAppBar(
        title = {
            Text("BluetoothChat", fontWeight = FontWeight.Bold)
        },
        scrollBehavior = scrollBehavior
    )
}

@Composable
fun DiscoveryDialog(
    onItemClicked: (String) -> Unit,
    onDismissRequest: () -> Unit,
    onStartDiscoveryRequest: () -> Unit,
    onStopDiscoveryRequest: () -> Unit,
    discoveredDevices: List<DeviceApp>,
    pairedDevices: List<DeviceApp>
) {

    DisposableEffect(Unit) {
        onStartDiscoveryRequest.invoke()

        onDispose { onStopDiscoveryRequest.invoke() }
    }

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxHeight(0.7f)
        ) {
            Box {
                Column(verticalArrangement = Arrangement.Center) {

                    Text(modifier = Modifier.padding(16.dp), text = "Paired Devices")

                    LazyColumn(contentPadding = PaddingValues(16.dp)) {
                        items(pairedDevices) { device ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onItemClicked.invoke(device.macAddress) },
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = device.name)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(modifier = Modifier.padding(16.dp), text = "Discovered Devices")

                    LazyColumn(contentPadding = PaddingValues(16.dp)) {
                        items(discoveredDevices) { device ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onItemClicked.invoke(device.macAddress) },
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(text = device.name)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}
