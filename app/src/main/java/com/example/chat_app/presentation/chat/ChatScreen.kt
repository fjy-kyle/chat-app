package com.example.chat_app.presentation.chat

import android.graphics.Paint.Align
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.room.RoomWarnings
import com.example.chat_app.R
import com.example.chat_app.domain.model.Message
import io.ktor.utils.io.*
import kotlinx.coroutines.flow.collectLatest

@Composable
fun ChatScreen(
    username: String?,
    viewModel: ChatViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    LaunchedEffect(key1 = true) {
        viewModel.toastEvent.collectLatest { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }
    val lifecycleOwner = LocalLifecycleOwner.current
    DisposableEffect(key1 = lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_START) {
                viewModel.connectToChat()
            } else if(event == Lifecycle.Event.ON_STOP) {
                viewModel.disconnect()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    val state = viewModel.state.value

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(start = 6.dp, top = 0.dp, end = 6.dp)
                .fillMaxWidth(),
            reverseLayout = true
        ){
            items(state.messages) { message ->
                val isOwnMessage = message.username == username
                MessageItem(isOwnMessage, message)
            }
        }
        InputBar(viewModel)

    }
}

@Composable
fun MessageItem(isOwnMessage: Boolean, message: Message) {
    Box(
        contentAlignment = if (isOwnMessage) {
            Alignment.CenterEnd
        } else Alignment.CenterStart,
        modifier = Modifier.fillMaxWidth()
    ){
        Row(modifier = Modifier.padding(8.dp)){
            ConstraintLayout {
                val (avatar, messageText) = createRefs()
                Image(
                    painter = painterResource(id = R.drawable.ic_launcher_background),
                    contentDescription = "头像",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .border(1.5.dp, MaterialTheme.colors.secondaryVariant, CircleShape)
                        .constrainAs(avatar) {
                            if (isOwnMessage) {
                                start.linkTo(messageText.end, margin = 5.dp)
                            } else {
                                end.linkTo(messageText.start, margin = 5.dp)
                            }
                        }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier
                        .padding(horizontal = 4.dp)
                        .constrainAs(messageText) {
                            if (isOwnMessage) {
                                end.linkTo(avatar.start, margin = 5.dp)
                            } else {
                                start.linkTo(avatar.end, margin = 5.dp)
                            }
                        }
                ) {
                    val messageTextColor = if (isOwnMessage) Color.White else Color.Black
                    val componentAlignment = if (isOwnMessage) Alignment.End else Alignment.Start
                    Row(
                        modifier = Modifier
                            .align(componentAlignment)
                    ) {
                        ConstraintLayout() {
                            val (usernameText, formattedTimeText) = createRefs()
                            Text(
                                text = message.username,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colors.secondaryVariant,
                                modifier = Modifier.constrainAs(usernameText) {
                                    if (isOwnMessage) {
                                        start.linkTo(formattedTimeText.end, margin = 10.dp)
                                    } else {
                                        end.linkTo(formattedTimeText.start, margin = 10.dp)
                                    }
                                }
                            )
                            Text(
                                text = message.formattedTime,
                                color = Color.LightGray,
                                modifier = Modifier.constrainAs(formattedTimeText) {
                                    if (isOwnMessage) {
                                        end.linkTo(usernameText.start, margin = 10.dp)
                                    } else {
                                        start.linkTo(usernameText.end, margin = 10.dp)
                                    }
                                },
                            )
                        }
                    }

                    Surface(
                        shape = RoundedCornerShape(15.dp),
                        elevation = 1.dp,
                        modifier = Modifier
                            .padding(1.dp)
                            .defaultMinSize(50.dp, 40.dp)
                            .sizeIn(minHeight = 40.dp, minWidth = 50.dp, maxWidth = 200.dp)
                            .align(componentAlignment),
                        color = if (isOwnMessage) Color(98, 0, 238) else Color.White
                    ) {
                        Text(
                            text = message.text,
                            color = messageTextColor,
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Left,
                            modifier = Modifier
                                .padding(start = 5.dp,end = 5.dp,top = 8.dp, bottom = 8.dp)
                                .wrapContentSize(),
                        )
                    }
                }
            }
        }
    }
    Spacer(modifier = Modifier.height(4.dp))
}



@Composable
fun InputBar(viewModel: ChatViewModel){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(247, 247, 247))
    ) {
        TextField(
            value = viewModel.messageText.value,
            onValueChange = viewModel::onMessageChange,
            placeholder = {
                Text(text = "")
            },
            shape = RoundedCornerShape(15.dp),
            modifier = Modifier
                .weight(1f)
                .padding(10.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )
        )
        IconButton(onClick = viewModel::sendMessage) {
            Icon(
                imageVector = Icons.Default.Send,
                contentDescription = "send"
            )
        }
    }
}