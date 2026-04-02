package com.example.cap

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
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

data class HealthRecord(
    val title: String,
    val date: String,
    val bp: String,
    val hr: String,
    val statusText: String,
    val isImproved: Boolean
)

data class CaregiverReminder(
    val id: Int,
    val title: String,
    val time: String,
    val repeat: String,
    var isDone: Boolean,
    var isActive: Boolean
)

val LocalHealthRecords = mutableStateListOf<HealthRecord>(
    HealthRecord("Regular Check-up", "March 13, 2026", "120/80", "72 bpm", "Improved", true),
    HealthRecord("Follow-up Visit", "March 10, 2026", "125/82", "75 bpm", "Stable", false)
)

val LocalCaregiverReminders = mutableStateListOf<CaregiverReminder>(
    CaregiverReminder(1, "Morning medication", "9:00 AM", "Daily", true, true),
    CaregiverReminder(2, "Doctor appointment", "2:00 PM", "Once", false, true),
    CaregiverReminder(3, "Evening medication", "6:00 PM", "Daily", false, true),
    CaregiverReminder(4, "Walk for 15 minutes", "10:00 AM", "Daily", false, true)
)

@Composable
fun FamilyLinkScreen(onNext: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        ProgressBar(step = 3)
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Connect with Family", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Link your account with family\nmembers", fontSize = 16.sp, color = Color(0xFF64748B), textAlign = TextAlign.Center)
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(2.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFEFF6FF)), contentAlignment = Alignment.Center) {
                        Text("🔗", fontSize = 24.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Family Link Code", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text("Share this code with\nyour senior family\nmember", fontSize = 14.sp, color = Color(0xFF64748B))
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Box(
                    modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Color(0xFFF8FAFC)).padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("A8F 4K2", fontSize = 40.sp, color = Color(0xFF3B82F6), fontWeight = FontWeight.Bold, letterSpacing = 2.sp)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Your family link code", fontSize = 14.sp, color = Color(0xFF64748B))
                    }
                }
            }
        }
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
        ) {
            Text("Next >", fontSize = 18.sp)
        }
    }
}

@Composable
fun CaregiverDashboardScreen(onLogout: () -> Unit) {
    var selectedTab by remember { mutableStateOf("Dashboard") }
    var activeChat by remember { mutableStateOf<String?>(null) }
    var showAddHealth by remember { mutableStateOf(false) }

    if (activeChat != null) {
        CaregiverChatScreen(contactName = activeChat!!, onBack = { activeChat = null })
    } else if (showAddHealth) {
        CaregiverAddHealthScreen(onBack = { showAddHealth = false })
    } else {
        Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Dashboard") },
                    label = { Text("Dashboard") },
                    selected = selectedTab == "Dashboard",
                    onClick = { selectedTab = "Dashboard" },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF3B82F6), selectedTextColor = Color(0xFF3B82F6))
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Health") },
                    label = { Text("Health") },
                    selected = selectedTab == "Health",
                    onClick = { selectedTab = "Health" },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF3B82F6), selectedTextColor = Color(0xFF3B82F6))
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Notifications, contentDescription = "Reminders") },
                    label = { Text("Reminders") },
                    selected = selectedTab == "Reminders",
                    onClick = { selectedTab = "Reminders" },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF3B82F6), selectedTextColor = Color(0xFF3B82F6))
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Email, contentDescription = "Chat") },
                    label = { Text("Chat") },
                    selected = selectedTab == "Chat",
                    onClick = { selectedTab = "Chat" },
                    colors = NavigationBarItemDefaults.colors(selectedIconColor = Color(0xFF3B82F6), selectedTextColor = Color(0xFF3B82F6))
                )
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize().background(Color(0xFFF8FAFC))) {
            when (selectedTab) {
                "Dashboard" -> CaregiverHomeTab(onAddHealth = { showAddHealth = true }, onChatClick = { activeChat = GlobalUserData.name }, onLogout = onLogout)
                "Health" -> CaregiverHealthTab(onAddHealth = { showAddHealth = true })
                "Reminders" -> CaregiverRemindersTab()
                "Chat" -> CaregiverChatScreen(contactName = GlobalUserData.name, onBack = { selectedTab = "Dashboard" })
            }
        }
    }
    }
}

@Composable
fun CaregiverHomeTab(onAddHealth: () -> Unit, onChatClick: () -> Unit, onLogout: () -> Unit) {
    LazyColumn(modifier = Modifier.fillMaxSize().padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
        item {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Column {
                    Text("Caregiver Dashboard", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                    Text("Friday, March 13, 2026", fontSize = 16.sp, color = Color(0xFF64748B))
                }
                IconButton(onClick = onLogout) {
                    @Suppress("DEPRECATION")
                    Icon(Icons.Default.ExitToApp, contentDescription = "Log Out", tint = Color.Red)
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth().clickable { },
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFEFF6FF))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(64.dp).clip(CircleShape).background(Color(0xFF3B82F6)), contentAlignment = Alignment.Center) {
                                Text(GlobalUserData.name.firstOrNull()?.toString() ?: "M", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text("${GlobalUserData.name} Smith", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                                Text("Mother • Age 72", fontSize = 14.sp, color = Color(0xFF64748B))
                            }
                        }
                        Surface(shape = RoundedCornerShape(16.dp), color = Color(0xFFDCFCE7)) {
                            Text("Active", fontSize = 12.sp, color = Color(0xFF15803D), modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("Good", fontSize = 18.sp, color = Color(0xFF15803D), fontWeight = FontWeight.Bold)
                            Text("Overall Status", fontSize = 12.sp, color = Color(0xFF64748B))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("85%", fontSize = 18.sp, color = Color(0xFF3B82F6), fontWeight = FontWeight.Bold)
                            Text("Reminder Rate", fontSize = 12.sp, color = Color(0xFF64748B))
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("2", fontSize = 18.sp, color = Color(0xFF3B82F6), fontWeight = FontWeight.Bold)
                            Text("Messages", fontSize = 12.sp, color = Color(0xFF64748B))
                        }
                    }
                }
            }
        }

        item {
            Text("Quick Actions", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                Card(
                    modifier = Modifier.weight(1f).clickable { onChatClick() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Email, contentDescription = "Send Message", tint = Color(0xFF3B82F6))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Send Message", fontSize = 14.sp, color = Color(0xFF1E293B))
                    }
                }
                Card(
                    modifier = Modifier.weight(1f).clickable { onAddHealth() },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Column(modifier = Modifier.padding(16.dp).fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Add, contentDescription = "Add Health Record", tint = Color(0xFF3B82F6))
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Add Health Record", fontSize = 14.sp, color = Color(0xFF1E293B))
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Today's Reminders", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                Text("View All", fontSize = 14.sp, color = Color(0xFF3B82F6))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    LocalCaregiverReminders.take(3).forEachIndexed { index, reminder ->
                        val statusText = if (reminder.isDone) "Done" else "Pending"
                        val statusColor = if (reminder.isDone) Color(0xFF10B981) else Color(0xFF64748B)
                        val statusBg = if (reminder.isDone) Color(0xFFD1FAE5) else Color(0xFFF1F5F9)
                        val dotColor = if (reminder.isDone) Color(0xFF10B981) else Color(0xFF3B82F6)
                        
                        ReminderRow(
                            dotColor = dotColor, 
                            title = reminder.title, 
                            time = reminder.time, 
                            statusText = statusText, 
                            statusColor = statusColor, 
                            statusBg = statusBg,
                            onClick = { 
                                val idx = LocalCaregiverReminders.indexOf(reminder)
                                if (idx != -1) {
                                    LocalCaregiverReminders[idx] = reminder.copy(isDone = !reminder.isDone)
                                }
                            }
                        )
                        if (index < 2) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF1F5F9))
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Health Summary", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                Text("View Details", fontSize = 14.sp, color = Color(0xFF3B82F6))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    val latestRecord = LocalHealthRecords.firstOrNull()
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("Blood Pressure", fontSize = 14.sp, color = Color(0xFF64748B))
                            Text("${latestRecord?.bp ?: "120/80"} mmHg", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
                        }
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.KeyboardArrowUp, contentDescription = null, tint = Color(0xFF10B981), modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Normal", color = Color(0xFF10B981), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                        }
                    }
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = Color(0xFFF1F5F9))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                        Column {
                            Text("Last Check-up", fontSize = 14.sp, color = Color(0xFF64748B))
                            Text(latestRecord?.date ?: "March 10, 2026", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1E293B))
                        }
                        Icon(Icons.Default.Info, contentDescription = null, tint = Color(0xFF64748B))
                    }
                }
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF2F2))
            ) {
                Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(Color(0xFFFEE2E2)), contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.Warning, contentDescription = "Emergency", tint = Color(0xFFEF4444))
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Emergency Settings", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B))
                        Text("Manage emergency contacts", fontSize = 14.sp, color = Color(0xFF64748B))
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun ReminderRow(dotColor: Color, title: String, time: String, statusText: String, statusColor: Color, statusBg: Color, onClick: () -> Unit = {}) {
    Row(modifier = Modifier.fillMaxWidth().clickable { onClick() }, horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(dotColor))
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1E293B))
                Text(time, fontSize = 14.sp, color = Color(0xFF64748B))
            }
        }
        Surface(shape = RoundedCornerShape(16.dp), color = statusBg) {
            Text(statusText, color = statusColor, fontSize = 12.sp, modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaregiverRemindersTab() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Manage Reminders", fontWeight = FontWeight.Bold) },
                actions = {
                    Button(
                        onClick = { },
                        modifier = Modifier.padding(end = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("+ Add")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF8FAFC))
            )
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Text("Active Reminders", fontSize = 18.sp, color = Color(0xFF1E293B))
            }
            items(LocalCaregiverReminders.size) { idx ->
                val reminder = LocalCaregiverReminders[idx]
                val isDone = reminder.isDone
                val isActive = reminder.isActive

                Card(
                    modifier = Modifier.fillMaxWidth().clickable {
                        LocalCaregiverReminders[idx] = reminder.copy(isDone = !reminder.isDone)
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = if (isDone) Color(0xFFF0FDF4) else Color.White)
                ) {
                    Row(modifier = Modifier.padding(16.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(if(isDone) Color(0xFFDCFCE7) else Color(0xFFEFF6FF)), contentAlignment = Alignment.Center) {
                                Icon(if(isDone) Icons.Default.Check else Icons.Default.Notifications, contentDescription = null, tint = if(isDone) Color(0xFF15803D) else Color(0xFF3B82F6))
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(reminder.title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1E293B))
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text("🕒 ${reminder.time}", fontSize = 12.sp, color = Color(0xFF64748B))
                                    Spacer(modifier = Modifier.width(16.dp))
                                    Text(if(isDone) "✅ Done" else "🔄 ${reminder.repeat}", fontSize = 12.sp, color = if(isDone) Color(0xFF15803D) else Color(0xFF64748B))
                                }
                            }
                        }
                        Switch(
                            checked = isActive,
                            onCheckedChange = { 
                                LocalCaregiverReminders[idx] = reminder.copy(isActive = it)
                            },
                            colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF3B82F6))
                        )
                    }
                }
            }
            item {
                Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(16.dp)).background(Color.White).padding(16.dp)) {
                    Text("💡 Reminders will be sent to ${GlobalUserData.name}'s phone at the scheduled time", textAlign = TextAlign.Center, color = Color(0xFF1E293B))
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaregiverHealthTab(onAddHealth: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Column {
                        Text("Health Monitoring", fontWeight = FontWeight.Bold)
                        Text("${GlobalUserData.name} Smith", fontSize = 14.sp, color = Color(0xFF64748B))
                    }
                },
                actions = {
                    Button(
                        onClick = onAddHealth,
                        modifier = Modifier.padding(end = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6)),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text("+ Add")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF8FAFC))
            )
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                val latestRecord = LocalHealthRecords.firstOrNull()
                val currentBp = latestRecord?.bp ?: "120/80"
                val currentHr = latestRecord?.hr?.replace(" bpm", "") ?: "72"
                Text("Current Vitals", fontSize = 18.sp, color = Color(0xFF1E293B))
                Spacer(modifier = Modifier.height(16.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    VitalsCard(modifier = Modifier.weight(1f), icon = "❤️", value = currentBp, unit = "Blood\nPressure", color = Color(0xFFFEE2E2))
                    VitalsCard(modifier = Modifier.weight(1f), icon = "📈", value = currentHr, unit = "Heart\nRate", color = Color(0xFFE0F2FE))
                    VitalsCard(modifier = Modifier.weight(1f), icon = "💧", value = "98", unit = "Blood\nSugar", color = Color(0xFFE0F2FE))
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Text("Health History", fontSize = 18.sp, color = Color(0xFF1E293B))
            }
            items(LocalHealthRecords.size) { index ->
                val record = LocalHealthRecords[index]
                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp), colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Row {
                                Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(if(record.isImproved) Color(0xFFEAF8F0) else Color(0xFFEFF6FF)), contentAlignment = Alignment.Center) {
                                    Text(if(record.isImproved) "↗️" else "−", fontSize = 16.sp, color = if(record.isImproved) Color.Unspecified else Color(0xFF3B82F6))
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(record.title, fontSize = 18.sp, fontWeight = FontWeight.Medium)
                                    Text(record.date, color = Color(0xFF64748B))
                                }
                            }
                            Surface(shape = RoundedCornerShape(16.dp), color = if(record.isImproved) Color(0xFFEAF8F0) else Color(0xFFEFF6FF)) {
                                Text(record.statusText, color = if(record.isImproved) Color(0xFF15803D) else Color(0xFF3B82F6), modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)).background(Color(0xFFF8FAFC)).padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Blood\nPressure", color = Color(0xFF64748B), fontSize = 14.sp)
                                    Text(record.bp, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("Heart Rate", color = Color(0xFF64748B), fontSize = 14.sp)
                                    Text(record.hr, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                }
                            }
                        }
                        if (index == 0) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("All vitals normal. Continue current medication.", color = Color(0xFF64748B))
                        } else if (index == 1) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("Blood pressure slightly elevated. Monitor closely.", color = Color(0xFF64748B))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VitalsCard(modifier: Modifier, icon: String, value: String, unit: String, color: Color) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = Color.White), shape = RoundedCornerShape(16.dp)) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(color), contentAlignment = Alignment.Center) {
                Text(icon)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
            Text(unit, fontSize = 12.sp, color = Color(0xFF64748B), textAlign = TextAlign.Center)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaregiverAddHealthScreen(onBack: () -> Unit) {
    var date by remember { mutableStateOf("March 29, 2026") }
    var title by remember { mutableStateOf("") }
    var bp by remember { mutableStateOf("") }
    var hr by remember { mutableStateOf("") }
    var statusText by remember { mutableStateOf("Improved") }
    var isImproved by remember { mutableStateOf(true) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Add Health Record") },
                navigationIcon = {
                    IconButton(onClick = onBack) { Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF8FAFC))
            )
        }
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues).padding(16.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
            item {
                Text("📅 Record Date", color = Color(0xFF1E293B))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = date, onValueChange = { date = it }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), readOnly = false)
            }
            item {
                Text("📄 Diagnosis / Visit Type", color = Color(0xFF1E293B))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = title, onValueChange = { title = it }, placeholder = { Text("e.g. Regular Check-up") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            }
            item {
                Text("Vital Signs", fontSize = 18.sp, color = Color(0xFF1E293B))
                Spacer(modifier = Modifier.height(16.dp))
                Text("Blood Pressure (mmHg)", color = Color(0xFF64748B), fontSize = 14.sp)
                OutlinedTextField(value = bp, onValueChange = { bp = it }, placeholder = { Text("e.g., 120/80") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Text("Heart Rate (bpm)", color = Color(0xFF64748B), fontSize = 14.sp)
                OutlinedTextField(value = hr, onValueChange = { hr = it }, placeholder = { Text("e.g., 72") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                Spacer(modifier = Modifier.height(12.dp))
                Text("Blood Sugar Level (mg/dL)", color = Color(0xFF64748B), fontSize = 14.sp)
                OutlinedTextField(value = "", onValueChange = {}, placeholder = { Text("e.g., 98") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp))
            }
            item {
                Text("Doctor's Notes / Comments", color = Color(0xFF1E293B))
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = "", onValueChange = {}, placeholder = { Text("Enter any additional notes...") }, modifier = Modifier.fillMaxWidth().height(120.dp), shape = RoundedCornerShape(12.dp), maxLines = 4)
            }
            item {
                Text("Status", fontSize = 18.sp, color = Color(0xFF1E293B))
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = isImproved, onClick = { isImproved = true; statusText = "Improved" })
                    Text("Improved")
                    Spacer(modifier = Modifier.width(16.dp))
                    RadioButton(selected = !isImproved, onClick = { isImproved = false; statusText = "Stable" })
                    Text("Stable / Needs Attention")
                }
            }
            item {
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        if (title.isNotBlank() && bp.isNotBlank() && hr.isNotBlank()) {
                            LocalHealthRecords.add(0, HealthRecord(
                                title = title,
                                date = date,
                                bp = bp,
                                hr = "$hr bpm",
                                statusText = statusText,
                                isImproved = isImproved
                            ))
                            onBack()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
                ) {
                    Text("Done (Save Health Record)", fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaregiverChatScreen(contactName: String, onBack: () -> Unit) {
    var messageText by remember { mutableStateOf("") }
    var messages by remember { mutableStateOf(listOf(
        ChatMessage("Good morning Sarah!", true),
        ChatMessage("Good morning Mom! How are you feeling today?", false),
        ChatMessage("I'm doing well, thank you!", true),
        ChatMessage("That's great to hear! Did you take your morning medication?", false),
        ChatMessage("Yes, I took it at 9 AM as scheduled.", true)
    )) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(0xFF3B82F6)), contentAlignment = Alignment.Center) {
                            Text(contactName.firstOrNull()?.toString() ?: "M", color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("$contactName Smith", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                            Text("● Online", color = Color(0xFF15803D), fontSize = 12.sp)
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = {}) { Icon(Icons.Default.Phone, contentDescription = "Call") }
                    // IconButton(onClick = {}) { Icon(Icons.Default.VideoCall, contentDescription = "Video Call") }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        bottomBar = {
            Surface(color = Color.White, shadowElevation = 8.dp) {
                Column {
                    Row(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Surface(shape = RoundedCornerShape(16.dp), color = Color(0xFFF8FAFC), modifier = Modifier.clickable {}) {
                            Text("How are you feeling?", modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), fontSize = 14.sp)
                        }
                        Surface(shape = RoundedCornerShape(16.dp), color = Color(0xFFF8FAFC), modifier = Modifier.clickable {}) {
                            Text("Did you take your", modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp), fontSize = 14.sp)
                        }
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedTextField(
                            value = messageText,
                            onValueChange = { messageText = it },
                            modifier = Modifier.weight(1f),
                            placeholder = { Text("Type a message...") },
                            shape = RoundedCornerShape(24.dp),
                            colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color(0xFFF8FAFC))
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier.size(48.dp).clip(CircleShape).background(Color(0xFF3B82F6)).clickable {
                                if (messageText.isNotBlank()) {
                                    val newMsg = messageText
                                    messages = messages + ChatMessage(newMsg, true) // Message from caregiver
                                    messageText = ""
                                    coroutineScope.launch { delay(100); listState.animateScrollToItem(messages.size - 1) }
                                }
                            },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Send", tint = Color.White)
                        }
                    }
                }
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            state = listState,
            contentPadding = PaddingValues(16.dp)
        ) {
            items(messages.size) { index ->
                val msg = messages[index]
                val isSenior = !msg.isFromUser // isFromUser = caregiver in this context, or vice-versa? Let's say false is senior, true is caregiver, wait, message says "isFromUser", meaning it is from the senior in SeniorAIChatScreen, but here it's Caregiver. I will adjust the logic.
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    horizontalArrangement = if (isSenior) Arrangement.Start else Arrangement.End
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(
                                    topStart = 16.dp,
                                    topEnd = 16.dp,
                                    bottomStart = if (isSenior) 4.dp else 16.dp,
                                    bottomEnd = if (isSenior) 16.dp else 4.dp
                                ))
                                .background(if (isSenior) Color.White else Color(0xFF3B82F6))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = msg.text,
                                color = if (isSenior) Color(0xFF1E293B) else Color.White,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
