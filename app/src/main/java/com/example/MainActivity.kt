package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.DirectionsBus
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.screens.AccountScreen
import com.example.ui.screens.LoginScreen
import com.example.ui.screens.NotificationScreen
import com.example.ui.screens.PaymentScreen
import com.example.ui.screens.TrackingScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.BusViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val viewModel: BusViewModel = viewModel()
                val activeUser by viewModel.activeUser.collectAsState()

                if (activeUser == null) {
                    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                        LoginScreen(
                            viewModel = viewModel,
                            modifier = Modifier.padding(innerPadding)
                        )
                    }
                } else {
                    AppShell(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun GeometricHeader(
    initials: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "CAMPUS TRANSIT",
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 1.5.sp
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = "UniTrans",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Notification Badge Icon
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.outlineVariant,
                        shape = androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.NotificationsActive,
                    contentDescription = "Notifications",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
                // Red badge dot
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .background(androidx.compose.ui.graphics.Color.Red, androidx.compose.foundation.shape.CircleShape)
                        .border(1.5.dp, MaterialTheme.colorScheme.background, androidx.compose.foundation.shape.CircleShape)
                        .align(Alignment.TopEnd)
                )
            }

            // Avatar Initials Circle
            Box(
                modifier = Modifier
                    .size(44.dp)
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = androidx.compose.foundation.shape.CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = initials,
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppShell(viewModel: BusViewModel) {
    val currentTab = viewModel.currentTab
    val activeUser by viewModel.activeUser.collectAsState()

    val initials = remember(activeUser) {
        val name = activeUser?.fullName ?: "Prachi Gadge"
        name.split(" ")
            .filter { it.isNotEmpty() }
            .map { it.first().uppercase() }
            .take(2)
            .joinToString("")
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier.testTag("bottom_navigation_bar"),
                containerColor = MaterialTheme.colorScheme.background,
                tonalElevation = 0.dp
            ) {
                NavigationBarItem(
                    selected = currentTab == AppTab.TRACKING,
                    onClick = { viewModel.selectTab(AppTab.TRACKING) },
                    icon = { Icon(Icons.Filled.DirectionsBus, contentDescription = "Tracking") },
                    label = { Text("Tracker", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("nav_tracking")
                )

                NavigationBarItem(
                    selected = currentTab == AppTab.PAYMENT,
                    onClick = { viewModel.selectTab(AppTab.PAYMENT) },
                    icon = { Icon(Icons.Filled.Payments, contentDescription = "Payments") },
                    label = { Text("Payment", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("nav_payments")
                )

                NavigationBarItem(
                    selected = currentTab == AppTab.NOTIFICATIONS,
                    onClick = { viewModel.selectTab(AppTab.NOTIFICATIONS) },
                    icon = { Icon(Icons.Filled.NotificationsActive, contentDescription = "Alerts") },
                    label = { Text("Alerts", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("nav_alerts")
                )

                NavigationBarItem(
                    selected = currentTab == AppTab.ACCOUNT,
                    onClick = { viewModel.selectTab(AppTab.ACCOUNT) },
                    icon = { Icon(Icons.Filled.AccountCircle, contentDescription = "Profile") },
                    label = { Text("Profile", style = MaterialTheme.typography.labelSmall, fontWeight = FontWeight.Bold) },
                    modifier = Modifier.testTag("nav_profile")
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            GeometricHeader(initials = initials)

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(MaterialTheme.colorScheme.outlineVariant)
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f)
            ) {
                when (currentTab) {
                    AppTab.TRACKING -> TrackingScreen(viewModel = viewModel)
                    AppTab.PAYMENT -> PaymentScreen(viewModel = viewModel)
                    AppTab.NOTIFICATIONS -> NotificationScreen(viewModel = viewModel)
                    AppTab.ACCOUNT -> AccountScreen(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
