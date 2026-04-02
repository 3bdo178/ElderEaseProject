package com.example.cap

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import androidx.compose.foundation.lazy.rememberLazyListState
import kotlinx.coroutines.delay
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

data class ChatMessage(val text: String, val isFromUser: Boolean)

// Singleton client to avoid instance recreation overhead
private val okHttpClient = OkHttpClient()

// API key stored in a separate constant variable as requested
private const val OPENAI_API_KEY = "YOUR_OPENAI_API_KEY" // TODO: Paste your actual OpenAI API Key here

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeniorAIChatScreen(onBack: () -> Unit) {
    var messageText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf(
        ChatMessage("Hello ${GlobalUserData.name}! I'm your AI Companion. How can I help you today?", false)
    )) }
    var isLoading by remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    val listState = rememberLazyListState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("AI Companion", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color(0xFF1E293B)
                )
            )
        },
        bottomBar = {
            Surface(
                color = Color.White,
                shadowElevation = 8.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = messageText,
                        onValueChange = { messageText = it },
                        placeholder = { Text("Type a message...") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(24.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Color(0xFF3B82F6),
                            unfocusedBorderColor = Color(0xFFE2E8F0)
                        ),
                        enabled = !isLoading
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (messageText.isNotBlank() && !isLoading) {
                                val userText = messageText
                                messages = messages + ChatMessage(userText, true)
                                messageText = ""
                                isLoading = true
                                
                                // Scroll to bottom when user sends a message
                                coroutineScope.launch {
                                    delay(100)
                                    listState.animateScrollToItem(messages.size - 1)
                                }
                                
                                coroutineScope.launch {
                                    fetchOpenAIResponse(userText) { aiResponse ->
                                        messages = messages + ChatMessage(aiResponse, false)
                                        isLoading = false
                                        // Scroll to bottom when AI replies
                                        coroutineScope.launch {
                                            delay(100)
                                            listState.animateScrollToItem(messages.size - 1)
                                        }
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(if (isLoading) Color.Gray else Color(0xFF3B82F6))
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8FAFC))
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(messages) { msg ->
                ChatBubble(message = msg)
            }
        }
    }
}

private suspend fun fetchOpenAIResponse(userMessage: String, onResult: (String) -> Unit) {
    withContext(Dispatchers.IO) {
        val url = "https://api.openai.com/v1/chat/completions"

        val jsonBody = JSONObject().apply {
            put("model", "gpt-4o-mini") // Updated to use gpt-4o-mini
            put("messages", JSONArray().apply {
                put(JSONObject().apply {
                    put("role", "system")
                    put("content", "You are a helpful and kind AI companion. Keep answers easy to read, warm, and concise.")
                })
                put(JSONObject().apply {
                    put("role", "user")
                    put("content", userMessage)
                })
            })
        }

        val request = Request.Builder()
            .url(url)
            .addHeader("Authorization", "Bearer $OPENAI_API_KEY")
            .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
            .build()

        okHttpClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                onResult("Error: Could not connect to AI. Please check your connection.")
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    if (!response.isSuccessful) {
                        onResult("Error: AI is currently unavailable or API key is missing. (${response.code})")
                        return
                    }
                    try {
                        val bodyString = response.body?.string()
                        if (bodyString != null) {
                            val jsonObject = JSONObject(bodyString)
                            val choices = jsonObject.getJSONArray("choices")
                            if (choices.length() > 0) {
                                val msg = choices.getJSONObject(0).getJSONObject("message").getString("content")
                                onResult(msg.trim())
                            } else {
                                onResult("Error: No response from AI.")
                            }
                        }
                    } catch (e: Exception) {
                        onResult("Error parsing AI response.")
                    }
                }
            }
        })
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val backgroundColor = if (message.isFromUser) Color(0xFF3B82F6) else Color.White
    val textColor = if (message.isFromUser) Color.White else Color(0xFF1E293B)
    val alignment = if (message.isFromUser) Alignment.CenterEnd else Alignment.CenterStart
    val shape = if (message.isFromUser) {
        RoundedCornerShape(20.dp, 20.dp, 4.dp, 20.dp)
    } else {
        RoundedCornerShape(20.dp, 20.dp, 20.dp, 4.dp)
    }

    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = alignment
    ) {
        Surface(
            color = backgroundColor,
            shape = shape,
            shadowElevation = if (message.isFromUser) 0.dp else 2.dp,
            modifier = Modifier.widthIn(max = 280.dp)
        ) {
            Text(
                text = message.text,
                color = textColor,
                fontSize = 16.sp,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}
