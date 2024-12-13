package just_pouya.bluetoothchat.ui.chat

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import just_pouya.bluetoothchat.R
import just_pouya.bluetoothchat.entities.MessageApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(
    contactName: String,
    onBackClicked: () -> Unit,
    viewModel: ChatViewModel = hiltViewModel()
) {

    val context=LocalContext.current

    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val isConnected by viewModel.isConnected.collectAsStateWithLifecycle()
    val toastMessage by viewModel.toastMessage.collectAsStateWithLifecycle()

    var textState by rememberSaveable(stateSaver = TextFieldValue.Saver) {
        mutableStateOf(TextFieldValue())
    }
    val scrollBehavior =
        TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    LaunchedEffect(toastMessage) {
        if (toastMessage!=null){
            Toast.makeText(context,toastMessage,Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopBar(
                scrollBehavior = scrollBehavior,
                title = contactName,
                isConnected = isConnected,
                onConnectClicked = {viewModel.reConnect()},
                onBackClicked = onBackClicked
            )
        },

        ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            MessagesList(
                Modifier
                    .weight(1f)
                    .fillMaxSize(), messages
            )

            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = textState,
                onValueChange = { textState = it },
                trailingIcon = {
                    if (textState.text.isNotBlank())
                        IconButton(
                            onClick = {
                                viewModel.sendMessage(textState.text)
                                textState = TextFieldValue()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.Send,
                                contentDescription = "send"
                            )
                        }
                },
                label = { Text(text = "Message") }
            )
        }
    }
}

@Composable
fun MessagesList(modifier: Modifier = Modifier, messages: List<MessageApp>) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(12.dp),
        reverseLayout = true,
    ) {
        items(messages) { item ->
            MessageItem(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp), item
            )
        }
    }
}

@Composable
fun MessageItem(modifier: Modifier = Modifier, item: MessageApp) {
    Row(
        modifier = modifier,
        horizontalArrangement = if (item.itsMe) Arrangement.End else Arrangement.Start
    ) {
        Card(
            shape = RoundedCornerShape(
                topStart = if (item.itsMe) MaterialTheme.shapes.medium.topStart else CornerSize(0.dp),
                topEnd = if (item.itsMe) CornerSize(0.dp) else MaterialTheme.shapes.medium.topEnd,
                bottomStart = MaterialTheme.shapes.medium.bottomStart,
                bottomEnd = MaterialTheme.shapes.medium.bottomEnd
            ),
            colors = if (item.itsMe) CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondaryContainer
            ) else CardDefaults.cardColors()
        ) {
            Text(
                text = item.messageTxt,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(12.dp)
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    scrollBehavior: TopAppBarScrollBehavior,
    title: String,
    isConnected: Boolean,
    onConnectClicked: () -> Unit,
    onBackClicked: () -> Unit,
) {
    TopAppBar(
        title = {
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(title, style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.padding(vertical = 4.dp))
                Text(
                    if (isConnected) "Connected" else "Disconnected",
                    style = MaterialTheme.typography.labelSmall
                )
            }
        },
        scrollBehavior = scrollBehavior,
        navigationIcon = {
            IconButton(onClick = onBackClicked) {
                Icon(
                    imageVector = Icons.AutoMirrored.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            if (!isConnected)
                IconButton(onClick = onConnectClicked) {
                    Icon(
                        imageVector = ImageVector.vectorResource(id = R.drawable.connect_24),
                        contentDescription = "Connect"
                    )
                }
        }
    )
}