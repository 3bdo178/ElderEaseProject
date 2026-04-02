package com.example.cap

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.border
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun SeniorDashboardScreen(onLogout: () -> Unit) {
    var selectedTab by remember { mutableStateOf("Home") }
    var activeFamilyChat by remember { mutableStateOf<String?>(null) } // Add state for active family chat

    if (activeFamilyChat != null) {
        FamilyChatScreen(contactName = activeFamilyChat!!, onBack = { activeFamilyChat = null })
        return
    }

    if (selectedTab == "EmergencyCountdown") {
        EmergencyCountdownScreen(onCancel = { selectedTab = "Home" })
        return
    }

    if (selectedTab == "AIChat") {
        SeniorAIChatScreen(onBack = { selectedTab = "Home" })
        return
    }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = selectedTab == "Home",
                    onClick = { selectedTab = "Home" },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF3B82F6),
                        selectedTextColor = Color(0xFF3B82F6),
                        indicatorColor = Color(0xFFEFF6FF)
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Reminders") },
                    label = { Text("Reminders") },
                    selected = selectedTab == "Reminders",
                    onClick = { selectedTab = "Reminders" },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF3B82F6),
                        selectedTextColor = Color(0xFF3B82F6),
                        indicatorColor = Color(0xFFEFF6FF)
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Email, contentDescription = "Family") },
                    label = { Text("Family") },
                    selected = selectedTab == "Family",
                    onClick = { selectedTab = "Family" },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF3B82F6),
                        selectedTextColor = Color(0xFF3B82F6),
                        indicatorColor = Color(0xFFEFF6FF)
                    )
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile") },
                    selected = selectedTab == "Profile",
                    onClick = { selectedTab = "Profile" },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF3B82F6),
                        selectedTextColor = Color(0xFF3B82F6),
                        indicatorColor = Color(0xFFEFF6FF)
                    )
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize().background(Color(0xFFF8FAFC))) {
            when (selectedTab) {
                "Home" -> HomeTab(onAIChatClick = { selectedTab = "AIChat" }, onEmergencyClick = { selectedTab = "EmergencyCountdown" }, onLogout = onLogout)
                "Reminders" -> RemindersTab()
                "Family" -> FamilyTab(onChatClick = { contactName -> activeFamilyChat = contactName })
                "Profile" -> ProfileTab(onLogout = onLogout)
            }
        }
    }
}

@Composable
fun HomeTab(onAIChatClick: () -> Unit, onEmergencyClick: () -> Unit, onLogout: () -> Unit) {
    val coroutineScope = rememberCoroutineScope()
    var showMoodCard by remember { mutableStateOf(true) }
    var selectedMood by remember { mutableStateOf<String?>(null) }
    
    var homeReminder1Done by remember { mutableStateOf(false) }
    var homeReminder2Done by remember { mutableStateOf(false) }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                Column {
                    Text("Good afternoon,", fontSize = 28.sp, color = Color(0xFF1E293B))
                    Text(GlobalUserData.name, fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                    Text("Friday, March 13", fontSize = 16.sp, color = Color(0xFF64748B))
                }
                IconButton(onClick = onLogout) {
                    @Suppress("DEPRECATION")
                    Icon(Icons.Default.ExitToApp, contentDescription = "Log Out", tint = Color.Red)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            
            Button(
                onClick = onEmergencyClick, 
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444)),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.fillMaxWidth().height(60.dp)
            ) {
                Text("! Emergency SOS", color = Color.White, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.height(16.dp))
            
            AnimatedVisibility(visible = showMoodCard) {
                Column {
                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("How are you feeling today?", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                                listOf("😊", "😐", "😔", "😴").forEach { emoji ->
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier
                                            .size(56.dp)
                                            .clip(CircleShape)
                                            .background(if (selectedMood == emoji) Color(0xFFE2E8F0) else Color.Transparent)
                                            .clickable {
                                                if (selectedMood == null) {
                                                    selectedMood = emoji
                                                    coroutineScope.launch {
                                                        delay(400)
                                                        showMoodCard = false
                                                    }
                                                }
                                            }
                                    ) {
                                        Text(emoji, fontSize = 40.sp)
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
            
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFFEFF6FF)), contentAlignment = Alignment.Center) {
                            Text("🔔")
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Today's Reminders", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                            val pending = listOf(homeReminder1Done, homeReminder2Done).count { !it }
                            Text("$pending tasks pending", fontSize = 14.sp, color = Color(0xFF64748B))
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    HomeReminderItem(
                        text = "Take morning medication - 9:00 AM",
                        color = Color(0xFF92400E),
                        bgColor = Color(0xFFFEF3C7),
                        isDone = homeReminder1Done,
                        onDone = { homeReminder1Done = true }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HomeReminderItem(
                        text = "Doctor appointment - 2:00 PM",
                        color = Color(0xFF1E40AF),
                        bgColor = Color(0xFFEFF6FF),
                        isDone = homeReminder2Done,
                        onDone = { homeReminder2Done = true }
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            
            Card(modifier = Modifier.fillMaxWidth().clickable { onAIChatClick() }, shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(Color(0xFFEFF6FF)), contentAlignment = Alignment.Center) {
                        Text("✨")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("AI Companion", fontSize = 18.sp, fontWeight = FontWeight.Medium)
                        Text("Ask me anything!", fontSize = 14.sp, color = Color(0xFF64748B))
                    }
                }
            }
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun HomeReminderItem(text: String, color: Color, bgColor: Color, isDone: Boolean, onDone: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isDone) Color(0xFFDCFCE7) else bgColor)
            .clickable(enabled = !isDone, onClick = onDone)
            .padding(12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(if (isDone) "✓ " else "• ", color = if (isDone) Color(0xFF15803D) else color, fontWeight = FontWeight.Bold)
            Text(text, color = if (isDone) Color(0xFF15803D) else color)
        }
    }
}

@Composable
fun RemindersTab() {
    var reminder1Done by remember { mutableStateOf(true) }
    var reminder2Done by remember { mutableStateOf(false) }
    var reminder3Done by remember { mutableStateOf(false) }
    
    // Add states for snoozed times
    var time2 by remember { mutableStateOf("2:00 PM") }
    var time3 by remember { mutableStateOf("6:00 PM") }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("My Reminders", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            Text("Friday, March 13", fontSize = 16.sp, color = Color(0xFF64748B))
            Spacer(modifier = Modifier.height(24.dp))
            
            ReminderItemCard("Take morning medication", "9:00 AM", reminder1Done, onDone = { reminder1Done = true }, onSnooze = {})
            Spacer(modifier = Modifier.height(16.dp))
            ReminderItemCard(
                "Doctor appointment", 
                time2, 
                reminder2Done, 
                onDone = { reminder2Done = true },
                onSnooze = { time2 = "Delayed (2:15 PM)" }
            )
            Spacer(modifier = Modifier.height(16.dp))
            ReminderItemCard(
                "Take evening medication", 
                time3, 
                reminder3Done, 
                onDone = { reminder3Done = true },
                onSnooze = { time3 = "Delayed (6:30 PM)" }
            )
        }
    }
}

@Composable
fun ReminderItemCard(title: String, time: String, isDone: Boolean, onDone: () -> Unit, onSnooze: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(if (isDone) Color(0xFFDCFCE7) else Color(0xFFEFF6FF)), contentAlignment = Alignment.Center) {
                    Text(if (isDone) "✓" else "🕒", color = if (isDone) Color(0xFF15803D) else Color(0xFF3B82F6))
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(title, fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1E293B))
                    Text(time, fontSize = 14.sp, color = if (time.contains("Delayed")) Color(0xFFD97706) else Color(0xFF64748B))
                }
            }
            if (!isDone) {
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth()) {
                    Button(modifier = Modifier.weight(1f).height(48.dp), onClick = onDone, shape = RoundedCornerShape(8.dp), colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))) {
                        Text("Done")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    OutlinedButton(modifier = Modifier.weight(1f).height(48.dp), onClick = onSnooze, shape = RoundedCornerShape(8.dp)) {
                        Text("Snooze")
                    }
                }
            }
        }
    }
}

@Composable
fun FamilyTab(onChatClick: (String) -> Unit) {
    var unread1 by remember { mutableStateOf(2) }
    var unread2 by remember { mutableStateOf(0) }
    var unread3 by remember { mutableStateOf(0) }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Text("Messages", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            Text("Stay connected with family", fontSize = 16.sp, color = Color(0xFF64748B))
            Spacer(modifier = Modifier.height(24.dp))
            
            MessageItemCard("S", "Sarah", "(Daughter)", "Hi Mom! How are you feeling today?", "5 min ago", unread1, onClick = { 
                unread1 = 0
                onChatClick("Sarah") 
            })
            Spacer(modifier = Modifier.height(16.dp))
            MessageItemCard("J", "John", "(Son)", "Call me when you're free", "1 hour ago", unread2, onClick = { 
                unread2 = 0
                onChatClick("John") 
            })
            Spacer(modifier = Modifier.height(16.dp))
            MessageItemCard("D", "Dr. Williams", "", "See you at 2 PM today", "Yesterday", unread3, onClick = { 
                unread3 = 0
                onChatClick("Dr. Williams") 
            })
        }
    }
}

@Composable
fun MessageItemCard(initial: String, name: String, relation: String, msgs: String, time: String, unreadCount: Int, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }, 
        shape = RoundedCornerShape(16.dp), 
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(56.dp).clip(CircleShape).background(Color(0xFF3B82F6)), contentAlignment = Alignment.Center) {
                Text(initial, color = Color.White, fontSize = 24.sp, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(name, fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1E293B))
                    if (relation.isNotEmpty()) {
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(relation, fontSize = 14.sp, color = Color(0xFF64748B))
                    }
                }
                Text(msgs, fontSize = 15.sp, color = Color(0xFF64748B), maxLines = 1)
            }
            Column(horizontalAlignment = Alignment.End) {
                Text(time, fontSize = 12.sp, color = Color(0xFF64748B))
                if (unreadCount > 0) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(modifier = Modifier.size(24.dp).clip(CircleShape).background(Color.Red), contentAlignment = Alignment.Center) {
                        Text(unreadCount.toString(), color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
fun ProfileTab(onLogout: () -> Unit) {
    var isEditingName by remember { mutableStateOf(false) }
    var newName by remember { mutableStateOf(GlobalUserData.name) }

    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("My Profile", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                    Text("Settings and preferences", fontSize = 16.sp, color = Color(0xFF64748B))
                }
                IconButton(onClick = onLogout) {
                    @Suppress("DEPRECATION")
                    Icon(Icons.Default.ExitToApp, contentDescription = "Log Out", tint = Color.Red)
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            
            Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(64.dp).clip(RoundedCornerShape(16.dp)).background(Color(0xFF3B82F6)), contentAlignment = Alignment.Center) {
                        Text(GlobalUserData.name.take(1).uppercase(), color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        if (isEditingName) {
                            OutlinedTextField(
                                value = newName,
                                onValueChange = { newName = it },
                                singleLine = true,
                                modifier = Modifier.fillMaxWidth()
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row {
                                Button(onClick = { 
                                    if (newName.isNotBlank()) GlobalUserData.name = newName
                                    isEditingName = false 
                                }) { Text("Save") }
                                Spacer(modifier = Modifier.width(8.dp))
                                OutlinedButton(onClick = { isEditingName = false }) { Text("Cancel") }
                            }
                        } else {
                            Text("${GlobalUserData.name} Smith", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                            Text("${GlobalUserData.name.lowercase()}.smith@email.com", fontSize = 14.sp, color = Color(0xFF64748B))
                            Text("Edit profile", fontSize = 14.sp, color = Color(0xFF3B82F6), modifier = Modifier.clickable { isEditingName = true }.padding(top = 4.dp))
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Text("Settings", fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color(0xFF64748B))
            Spacer(modifier = Modifier.height(12.dp))
            
            ProfileSettingItem("🌐", "Language", "English")
            Spacer(modifier = Modifier.height(12.dp))
            ProfileSettingItem("T", "Font Size", "Large")
            Spacer(modifier = Modifier.height(12.dp))
            ProfileSettingItem("📞", "Emergency Contacts", "4 contacts")
            Spacer(modifier = Modifier.height(12.dp))
            ProfileSettingItem("🛡️", "Country / Region", "United States")
        }
    }
}

@Composable
fun ProfileSettingItem(icon: String, title: String, subtitle: String) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
        Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(40.dp).clip(RoundedCornerShape(8.dp)).background(Color(0xFFEFF6FF)), contentAlignment = Alignment.Center) {
                    Text(icon, fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(title, fontSize = 18.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1E293B))
                    Text(subtitle, fontSize = 14.sp, color = Color(0xFF64748B))
                }
            }
            Text(">", fontSize = 20.sp, color = Color(0xFF94A3B8), fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun EmergencyCountdownScreen(onCancel: () -> Unit, onCountdownFinish: () -> Unit = {}) {
    var countdown by remember { mutableIntStateOf(3) }
    val redColor = Color(0xFFE53935)
    
    LaunchedEffect(countdown) {
        if (countdown > 0) {
            delay(1000)
            countdown -= 1
        } else {
            onCountdownFinish()
            // Optional: Action after countdown, like showing a map or actually "calling". Right now we just stop.
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(redColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.weight(1f))
        
        Box(
            modifier = Modifier
                .size(160.dp)
                .background(Color.White, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .border(6.dp, redColor, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "!",
                    fontSize = 56.sp,
                    fontWeight = FontWeight.Bold,
                    color = redColor
                )
            }
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Text(
            text = countdown.toString(),
            fontSize = 80.sp,
            color = Color.White
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Text(
            text = "Calling Emergency\nServices",
            fontSize = 28.sp,
            color = Color.White,
            textAlign = TextAlign.Center,
            lineHeight = 36.sp
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .clickable { onCancel() },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Cancel",
                    tint = redColor,
                    modifier = Modifier
                        .align(Alignment.CenterStart)
                        .padding(start = 24.dp)
                        .size(32.dp)
                )
                Text(
                    text = "Cancel",
                    color = redColor,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FamilyChatScreen(contactName: String, onBack: () -> Unit) {
    var messageText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf(
        ChatMessage("Hi! I'm here if you needed me.", false)
    )) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(contactName, fontWeight = FontWeight.Bold) },
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
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    IconButton(
                        onClick = {
                            if (messageText.isNotBlank()) {
                                messages = messages + java.util.Collections.singletonList(ChatMessage(messageText, true))
                                messageText = ""
                                coroutineScope.launch {
                                    delay(100)
                                    listState.animateScrollToItem(messages.size - 1)
                                }
                            }
                        },
                        modifier = Modifier
                            .size(50.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF3B82F6))
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White)
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
