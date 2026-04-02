package com.example.cap

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.graphics.Brush
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.Dp
import kotlinx.coroutines.delay

class UserData {
    var name by mutableStateOf("Margaret")
}

val GlobalUserData = UserData()

@Composable
fun AppLogo(modifier: Modifier = Modifier, iconSize: Dp = 40.dp) {
    val gradientBrush = Brush.linearGradient(
        colors = listOf(Color(0xFF5A9CFF), Color(0xFF42C1D2))
    )
    Box(
        modifier = modifier
            .clip(CircleShape)
            .background(gradientBrush),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.FavoriteBorder,
            contentDescription = "ElderEase Logo",
            tint = Color.White,
            modifier = Modifier.size(iconSize)
        )
    }
}

@Composable
fun ElderEaseApp() {
    var currentScreen by remember { mutableStateOf("splash") }
    var userRole by remember { mutableStateOf("") } // "senior" or "family"

    AnimatedContent(
        targetState = currentScreen,
        transitionSpec = {
            if (targetState == "splash" || initialState == "splash") {
                fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
            } else {
                slideInHorizontally(
                    initialOffsetX = { it },
                    animationSpec = tween(300)
                ) + fadeIn(animationSpec = tween(300)) togetherWith slideOutHorizontally(
                    targetOffsetX = { -it },
                    animationSpec = tween(300)
                ) + fadeOut(animationSpec = tween(300))
            }
        },
        label = "ScreenTransition"
    ) { screen ->
        when (screen) {
            "splash" -> SplashScreen { currentScreen = "onboarding_1" }
            "onboarding_1" -> OnboardingOneScreen(
                onNext = { currentScreen = "onboarding_2" }
            )
            "onboarding_2" -> OnboardingTwoScreen(
                onNext = { currentScreen = "onboarding_3" },
                onSkip = { currentScreen = "welcome" }
            )
            "onboarding_3" -> OnboardingThreeScreen(
                onNext = { currentScreen = "welcome" },
                onSkip = { currentScreen = "welcome" }
            )
            "welcome" -> WelcomeScreen(
                onSeniorSelect = {
                    userRole = "senior"
                    currentScreen = "login"
                },
                onFamilySelect = {
                    userRole = "family"
                    currentScreen = "login"
                }
            )
            "login" -> LoginScreen(
                onLoginSuccess = {
                    if (userRole == "senior") currentScreen = "senior_home" else currentScreen = "family_link"
                },
                onSignUpClick = { currentScreen = "signup" }
            )
            "signup" -> SignupScreen(
                onSignUpSuccess = { name ->
                    GlobalUserData.name = name
                    if (userRole == "senior") currentScreen = "senior_home" else currentScreen = "family_link"
                },
                onBack = { currentScreen = "login" }
            )
            "family_link" -> FamilyLinkScreen(onNext = { currentScreen = "caregiver_home" })
            "senior_home" -> SeniorDashboardScreen(onLogout = { currentScreen = "welcome" })
            "caregiver_home" -> CaregiverDashboardScreen(onLogout = { currentScreen = "welcome" })
            "emergency_contacts" -> EmergencyContactsScreen(
                onNext = { currentScreen = "location_settings" },
                onBack = { currentScreen = "onboarding_2" }
            )
            "location_settings" -> LocationSettingsScreen(
                onComplete = { currentScreen = "home" },
                onBack = { currentScreen = "emergency_contacts" }
            )
        }
    }
}

@Composable
fun SignupScreen(onSignUpSuccess: (String) -> Unit, onBack: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Create Account", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
        Spacer(modifier = Modifier.height(32.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Name", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1E293B))
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                placeholder = { Text("Enter your name") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Email", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1E293B))
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Enter your email") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Password", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1E293B))
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Enter your password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (errorMessage.isNotEmpty()) {
            Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
            Spacer(modifier = Modifier.height(8.dp))
        }

        Button(
            onClick = {
                if (name.isBlank() || email.isBlank() || password.isBlank()) {
                    errorMessage = "Please fill all fields"
                } else {
                    onSignUpSuccess(name)
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
        ) {
            Text("Sign Up", fontSize = 18.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onBack) {
            Text("Back to Login", color = Color(0xFF3B82F6))
        }
    }
}

@Composable
fun SplashScreen(onTimeout: () -> Unit) {
    LaunchedEffect(Unit) {
        delay(2000)
        onTimeout()
    }

    Column(
        modifier = Modifier.fillMaxSize().background(Color(0xFFF9FAFB)),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        AppLogo(
            modifier = Modifier.size(160.dp),
            iconSize = 80.dp
        )
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "ElderEase",
            fontSize = 44.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Helping Seniors Live with Ease",
            fontSize = 20.sp,
            color = Color(0xFF64748B)
        )
    }
}

@Composable
fun WelcomeScreen(onSeniorSelect: () -> Unit, onFamilySelect: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = "Welcome to\nElderEase",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            lineHeight = 40.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Please select your role to\ncontinue",
            fontSize = 16.sp,
            color = Color(0xFF64748B),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(48.dp))

        RoleCard(
            title = "I am a\nSenior",
            subtitle = "Get support\nand stay\nconnected",
            iconType = "senior",
            onSelect = onSeniorSelect
        )
        Spacer(modifier = Modifier.height(24.dp))
        RoleCard(
            title = "I am a\nFamily\nMember /\nCaregiver",
            subtitle = "Monitor and\ncare for your\nloved one",
            iconType = "family",
            onSelect = onFamilySelect
        )
    }
}

@Composable
fun RoleCard(title: String, subtitle: String, iconType: String, onSelect: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onSelect),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(24.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (iconType == "senior") Color(0xFFEFF6FF) else Color(0xFFF0FDF4)),
                contentAlignment = Alignment.Center
            ) {
                if (iconType == "senior") {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = "Senior",
                        tint = Color(0xFF3B82F6),
                        modifier = Modifier.size(40.dp)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Family",
                        tint = Color(0xFF14B8A6),
                        modifier = Modifier.size(40.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.width(24.dp))
            Column {
                Text(text = title, fontSize = 20.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF1E293B), lineHeight = 26.sp)
                Spacer(modifier = Modifier.height(12.dp))
                Text(text = subtitle, fontSize = 14.sp, color = Color(0xFF64748B), lineHeight = 20.sp)
            }
        }
    }
}

@Composable
fun OnboardingOneScreen(onNext: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(Color(0xFFEAF8F0)),
            contentAlignment = Alignment.Center
        ) {
            Text("🛡️", fontSize = 60.sp) // Replace with proper Shield Icon if available, or keep text
        }
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = "Stay Safe with\nEmergency Help",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            lineHeight = 36.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Quick access to emergency\nservices and family contacts.\nHelp is just one tap away.",
            fontSize = 16.sp,
            color = Color(0xFF64748B),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            lineHeight = 24.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.size(24.dp, 8.dp).clip(RoundedCornerShape(4.dp)).background(Color(0xFF3B82F6)))
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFE2E8F0)))
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFE2E8F0)))
        }
        Spacer(modifier = Modifier.height(32.dp))
        Button(
            onClick = onNext,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
        ) {
            Text("Get Started", fontSize = 18.sp)
        }
    }
}

@Composable
fun OnboardingTwoScreen(onNext: () -> Unit, onSkip: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(Color(0xFFE6FAF5)),
            contentAlignment = Alignment.Center
        ) {
            Text("🔔", fontSize = 60.sp) // Bell icon
        }
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = "Get Reminders &\nDaily Support",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            lineHeight = 36.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Never miss your medication or\nappointments. Your AI\ncompanion is here to help.",
            fontSize = 16.sp,
            color = Color(0xFF64748B),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            lineHeight = 24.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFE2E8F0)))
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.size(24.dp, 8.dp).clip(RoundedCornerShape(4.dp)).background(Color(0xFF3B82F6)))
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFE2E8F0)))
        }
        Spacer(modifier = Modifier.height(32.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = onSkip,
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Skip", fontSize = 18.sp, color = Color(0xFF3B82F6))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = onNext,
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
            ) {
                Text("Next >", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun OnboardingThreeScreen(onNext: () -> Unit, onSkip: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF9FAFB))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))
        Box(
            modifier = Modifier
                .size(160.dp)
                .clip(CircleShape)
                .background(Color(0xFFEFF6FF)),
            contentAlignment = Alignment.Center
        ) {
            Text("👥", fontSize = 60.sp) // People icon
        }
        Spacer(modifier = Modifier.height(48.dp))
        Text(
            text = "Stay Connected with\nFamily",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1E293B),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            lineHeight = 36.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Easy video calls and messages\nwith your loved ones. Stay in\ntouch anytime, anywhere.",
            fontSize = 16.sp,
            color = Color(0xFF64748B),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            lineHeight = 24.sp
        )
        Spacer(modifier = Modifier.weight(1f))
        Row(horizontalArrangement = Arrangement.Center, modifier = Modifier.fillMaxWidth()) {
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFE2E8F0)))
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(Color(0xFFE2E8F0)))
            Spacer(modifier = Modifier.width(8.dp))
            Box(modifier = Modifier.size(24.dp, 8.dp).clip(RoundedCornerShape(4.dp)).background(Color(0xFF3B82F6)))
        }
        Spacer(modifier = Modifier.height(32.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = onSkip,
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Skip", fontSize = 18.sp, color = Color(0xFF3B82F6))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = onNext,
                modifier = Modifier.weight(1f).height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
            ) {
                Text("Next >", fontSize = 18.sp)
            }
        }
    }
}

@Composable
fun EmergencyContactsScreen(onNext: () -> Unit, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(24.dp)
    ) {
        ProgressBar(step = 1)
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Emergency Contacts", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Add people to contact in emergencies", fontSize = 16.sp, color = Color(0xFF64748B))
        Spacer(modifier = Modifier.height(32.dp))

        Text(text = "Primary Emergency Contact", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1E293B))
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Phone number") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White)
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Contact Name", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1E293B))
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Contact name (e.g., Daughter)") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White)
        )

        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "+ Add Another Contact", color = Color(0xFF3B82F6), fontWeight = FontWeight.Medium)

        Spacer(modifier = Modifier.weight(1f))
        BottomNavButtons(onBack = onBack, onNext = onNext, nextText = "Next >")
    }
}

@Composable
fun LocationSettingsScreen(onComplete: () -> Unit, onBack: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(24.dp)
    ) {
        ProgressBar(step = 3)
        Spacer(modifier = Modifier.height(32.dp))
        Text(text = "Location Settings", fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Set your country for emergency services", fontSize = 16.sp, color = Color(0xFF64748B))
        Spacer(modifier = Modifier.height(32.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(modifier = Modifier.size(48.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFFE0F2FE)), contentAlignment = Alignment.Center) {
                        Text("🌐")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text("Your Location", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                        Text("This helps us connect you to local emergency services", fontSize = 14.sp, color = Color(0xFF64748B))
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                OutlinedTextField(
                    value = "United States",
                    onValueChange = {},
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Box(
                    modifier = Modifier.fillMaxWidth().background(Color(0xFFDCFCE7), RoundedCornerShape(12.dp)).padding(16.dp)
                ) {
                    Text("Emergency Number: 911", fontSize = 16.sp, color = Color(0xFF166534), fontWeight = FontWeight.Medium)
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        BottomNavButtons(onBack = onBack, onNext = onComplete, nextText = "Complete Setup")
    }
}

@Composable
fun LoginScreen(onLoginSuccess: () -> Unit, onSignUpClick: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))
        AppLogo(
            modifier = Modifier.size(80.dp),
            iconSize = 40.dp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Welcome Back", fontSize = 32.sp, fontWeight = FontWeight.Bold, color = Color(0xFF1E293B))
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Senior Login", fontSize = 18.sp, color = Color(0xFF64748B))
        Spacer(modifier = Modifier.height(48.dp))

        Column(modifier = Modifier.fillMaxWidth()) {
            Text(text = "Email or Phone", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1E293B))
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = email,
                onValueChange = { email = it; errorMessage = "" },
                placeholder = { Text("Enter your email or phone") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White)
            )

            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Password", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = Color(0xFF1E293B))
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = password,
                onValueChange = { password = it; errorMessage = "" },
                placeholder = { Text("Enter your password") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(unfocusedContainerColor = Color.White)
            )

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Forgot Password?", color = Color(0xFF3B82F6), fontWeight = FontWeight.Medium)

            Spacer(modifier = Modifier.height(32.dp))

            if (errorMessage.isNotEmpty()) {
                Text(text = errorMessage, color = Color.Red, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
            }

            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        errorMessage = "Please enter both email and password"
                    } else {
                        onLoginSuccess()
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
            ) {
                Text("Log In", fontSize = 18.sp)
            }

            Spacer(modifier = Modifier.height(48.dp))
            OutlinedButton(
                onClick = onSignUpClick,
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Create New Account", fontSize = 18.sp, color = Color(0xFF3B82F6))
            }
        }
    }
}

@Composable
fun ProgressBar(step: Int) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        for (i in 1..3) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(2.dp))
                    .background(if (i <= step) Color(0xFF3B82F6) else Color(0xFFE2E8F0))
            )
        }
    }
}

@Composable
fun BottomNavButtons(onBack: () -> Unit, onNext: () -> Unit, nextText: String) {
    Row(modifier = Modifier.fillMaxWidth()) {
        OutlinedButton(
            onClick = onBack,
            modifier = Modifier.weight(1f).height(56.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Back", fontSize = 18.sp, color = Color(0xFF3B82F6))
        }
        Spacer(modifier = Modifier.width(16.dp))
        Button(
            onClick = onNext,
            modifier = Modifier.weight(1f).height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3B82F6))
        ) {
            Text(nextText, fontSize = 18.sp)
        }
    }
}
