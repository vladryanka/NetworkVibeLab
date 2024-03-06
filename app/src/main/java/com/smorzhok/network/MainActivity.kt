package com.smorzhok.network

//import com.smorzhok.network.data.messages
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.smorzhok.network.data.Message
import com.smorzhok.network.data.index
import com.smorzhok.network.data.link
import com.smorzhok.network.data.messages
import com.smorzhok.network.ui.theme.NetworkTheme
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NetworkTheme {
                NavHost(navController, "main") {
                    composable("main") {
                        MessageListScreen {
                            navController.navigate("second")
                        }
                    }
                    composable("second") {
                        SecondScreen {
                            navController.navigate("main") {
                                popUpTo("main")
                                {
                                    inclusive = true
                                }
                                GlobalScope.launch {
                                    sendDataToApi(messages)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun SecondScreen(onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.fillMaxSize()
    ) {

        ChangeMessage(messages[index].title, messages[index].body, Modifier, onClick)
        Spacer(Modifier.weight(1f))

    }

}

@Composable
fun ChangeMessage(
    title: String,
    body: String, modifier: Modifier, onClick: () -> Unit
) {
    var text by remember { mutableStateOf("Hello") }
    Column(
        modifier = Modifier
            .padding(8.dp)
            .animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioNoBouncy,
                    stiffness = Spring.StiffnessMedium
                )
            )
    ) {
        Text(
            text = title,
            modifier = Modifier
                .padding(top = dimensionResource(R.dimen.padding_small))
                .fillMaxSize()
                .align(Alignment.CenterHorizontally)
        )

        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Label") }
        )
        Button(onClick = { onClick() }){}
    }
}

suspend fun sendDataToApi(messages: List<Message>) {
    val dataToSend = messages

    // Отправка POST-запроса
    val response = apiService.sendData(dataToSend)
}


@Composable
fun MessageListScreen(onClick: () -> Unit) {

    val connection = URL(link).openConnection() as HttpURLConnection
    val data = connection.inputStream.bufferedReader().readText()
    val listType = object : TypeToken<List<Message>>() {}.type
    messages = Gson().fromJson(data, listType)

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()

    ) {
        itemsIndexed(
            messages
        ) { index, item ->
            MessageItem(
                message = item,
                modifier = Modifier
                    .padding(dimensionResource(R.dimen.padding_small)), onClick, index
            )
        }
    }
}

@Composable
fun MessageItem(
    message: Message,
    modifier: Modifier = Modifier,
    onClick: () -> Unit, index1: Int
) {

    Surface(
        color = Color.White,
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
        ) {
            MessageInformation(
                message.title, message.id, modifier,
                onClick, index1
            )
            Spacer(Modifier.weight(1f))

        }
    }
}

@Composable
fun MessageInformation(
    body: String,
    id: Int,
    modifier: Modifier = Modifier,
    onClick: () -> Unit, index1: Int
) {
    Card(
        modifier = modifier.clickable(onClick = {
            onClick()
            index = index1
        })
    ) {
        Column(modifier = modifier) {
            Text(
                text = body,
                modifier = Modifier
                    .padding(top = dimensionResource(R.dimen.padding_small))
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = id.toString(),
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.CenterHorizontally)
            )

        }
    }
}

@Composable
fun SecondScreen(
    message: Message,
    modifier: Modifier = Modifier

) {
    Column {
        Text(
            message.title, style = MaterialTheme.typography.h4,
            modifier = Modifier.fillMaxSize()
        )
        Text(message.body, style = MaterialTheme.typography.h4)
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    NetworkTheme {
        //NetworkApp()
    }
}
