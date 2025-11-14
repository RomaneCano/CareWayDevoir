package com.example.myapplication.ihm.screens

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.Call
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import com.example.myapplication.R
import com.example.myapplication.ihm.BottomNavItem
import com.example.myapplication.model.ActionButtonData
import com.example.myapplication.model.CompletedTripInfo
import com.example.myapplication.model.PendingRequestInfo
import com.example.myapplication.model.TransporterInfo
import com.example.myapplication.model.UpcomingTripInfo


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreenView(
    selectedTabIndex: Int,
    onTabSelected: (Int) -> Unit,
    onQrCodeClicked: () -> Unit,
    onNavigateToTripDetails: (UpcomingTripInfo) -> Unit
) {
    var internalSelectedItemIndex by remember { mutableIntStateOf(selectedTabIndex) }
    LaunchedEffect(selectedTabIndex) {
        internalSelectedItemIndex = selectedTabIndex
    }

    val bottomNavDisplayItems = listOf(
        BottomNavItem.Home,
        BottomNavItem.Search,
        BottomNavItem.History,
        BottomNavItem.Profile
    )
    val currentContentItem = bottomNavDisplayItems[internalSelectedItemIndex]

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            if (currentContentItem != BottomNavItem.History && currentContentItem != BottomNavItem.Profile) {
                TopAppBar(
                    title = { /* No title */ },
                    navigationIcon = {
                        if (internalSelectedItemIndex != 0) {
                            IconButton(onClick = { onTabSelected(0) }) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Retour"
                                )
                            }
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onQrCodeClicked,
                modifier = Modifier.offset(y = 28.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Settings /* TODO: Change to QR icon */, "QR Code", tint = Color.White)
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            BottomAppBar {
                bottomNavDisplayItems.forEachIndexed { index, item ->
                    if (index == 2) { // Add spacer before the 3rd item (History)
                        Spacer(Modifier.weight(0.5f)) // Visual spacer for FAB
                    }
                    NavigationBarItem(
                        selected = internalSelectedItemIndex == index,
                        onClick = {
                            internalSelectedItemIndex = index
                            onTabSelected(index)
                        },
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentContentItem) {
                BottomNavItem.Home -> HomeScreenContent()
                BottomNavItem.Search -> SearchScreenNoResultsView()
                BottomNavItem.History -> HistoryScreen(onNavigateToTripDetails = onNavigateToTripDetails)
                BottomNavItem.Profile -> ProfileScreen() // Correctly calling ProfileScreen
            }
        }
    }
}

@Composable
fun LocalListDivider() {
    HorizontalDivider(
        modifier = Modifier.padding(horizontal = 16.dp),
        thickness = 0.8.dp,
        color = Color.LightGray.copy(alpha = 0.5f)
    )
}

@Composable
fun ProfileRowItem(
    icon: ImageVector,
    text: String,
    trailingText: String? = null,
    onClickAction: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .then(if (onClickAction != null) Modifier.clickable(onClick = onClickAction) else Modifier)
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            modifier = Modifier.size(24.dp),
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.weight(1f))
        trailingText?.let {
            Text(
                text = it,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun ProfileSectionCard(content: @Composable ColumnScope.() -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column { content() }
    }
}

@Composable
fun ProfileScreen(onClick: () -> Unit = {})
{

}

@Composable
fun HistoryScreen(onNavigateToTripDetails: (UpcomingTripInfo) -> Unit) {
    val tabTitles = listOf("Trajets à venir", "Trajets effectués", "Demandes en attente")
    val pagerState = rememberPagerState { tabTitles.size }
    val coroutineScope = rememberCoroutineScope()

    Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = pagerState.currentPage) {
            tabTitles.forEachIndexed { index, title ->
                Tab(
                    selected = pagerState.currentPage == index,
                    onClick = { coroutineScope.launch { pagerState.animateScrollToPage(index) } },
                    text = { Text(title) }
                )
            }
        }
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) { page ->
            when (page) {
                0 -> UpcomingTripsPage(onNavigateToTripDetails = onNavigateToTripDetails)
                1 -> CompletedTripsPage()
                2 -> PendingRequestsPage()
            }
        }
    }
}

@Composable
fun UpcomingTripCard(
    tripInfo: UpcomingTripInfo,
    onNavigateToTripDetails: (UpcomingTripInfo) -> Unit
) {
    val cardColor = Color(0xFF4285F4)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .clickable { onNavigateToTripDetails(tripInfo) },
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                tripInfo.profileImagePainter?.let {
                    Image(
                        painter = it,
                        contentDescription = tripInfo.driverName,
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(Color.White),
                        contentScale = ContentScale.Crop
                    )
                } ?: Icon(
                    imageVector = tripInfo.profilePicVector,
                    contentDescription = tripInfo.driverName,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    tint = cardColor
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(tripInfo.driverName, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                    Text(tripInfo.transportDetails, color = Color.White, fontSize = 14.sp)
                }
                if (tripInfo.isGroup) {
                    Icon(
                        imageVector = Icons.Filled.Person,
                        contentDescription = "Groupe",
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.DateRange, contentDescription = "Date", tint = Color.White, modifier = Modifier.size(16.dp))
                Text(tripInfo.date, color = Color.White, fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Icon(Icons.Filled.Check, contentDescription = "Time", tint = Color.White, modifier = Modifier.size(16.dp))
                Text(tripInfo.time, color = Color.White, fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onNavigateToTripDetails(tripInfo) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = cardColor
                )
            ) {
                Text("Détails")
            }
        }
    }
}

@Composable
fun UpcomingTripsPage(onNavigateToTripDetails: (UpcomingTripInfo) -> Unit) {
    val sampleTrips = listOf(
        UpcomingTripInfo(
            id = "1",
            driverName = "Jean D.",
            transportDetails = "Ambu81, VSL",
            date = "Lundi 23 juin 2025",
            time = "11:00 - 11:30",
            routeDetails = "6 Rue Barclay > Hôpital de Castres",
            profileImagePainter = null,
            profilePicVector = Icons.Filled.AccountCircle
        ),
        UpcomingTripInfo(
            "2",
            "Olivier C.",
            "Ambu81, VSL",
            "Lundi 23 juin 2025",
            "17:45 - 18:30",
            "Lieu A > Lieu B",
            isGroup = true
        ),
        UpcomingTripInfo(
            "3",
            "Anne L.",
            "UrgVSL, VSL",
            "Mardi 24 juin 2025",
            "09:00 - 09:45",
            "Lieu C > Lieu D",
            profilePicVector = Icons.Filled.Person
        )
    )
    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(placeholderText = "Rechercher un transporteur ou une date")
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(sampleTrips) { trip ->
                UpcomingTripCard(tripInfo = trip, onNavigateToTripDetails = onNavigateToTripDetails)
            }
        }
    }
}

@Composable
fun CompletedTripCard(tripInfo: CompletedTripInfo) {
    val cardColor = Color(0xFF34A853)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = tripInfo.profilePic,
                    contentDescription = tripInfo.driverName,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    tint = cardColor
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(tripInfo.driverName, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                    Text(tripInfo.transportDetails, color = Color.White, fontSize = 14.sp)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.DateRange, contentDescription = "Date", tint = Color.White, modifier = Modifier.size(16.dp))
                Text(tripInfo.date, color = Color.White, fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Icon(Icons.Filled.Check, contentDescription = "Time", tint = Color.White, modifier = Modifier.size(16.dp))
                Text(tripInfo.time, color = Color.White, fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* TODO: Handle Details Click */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = cardColor
                )
            ) {
                Text("Détails")
            }
        }
    }
}

@Composable
fun CompletedTripsPage() {
    val sampleTrips = listOf(
        CompletedTripInfo(
            "1",
            "Hélène A.",
            "Taxi Care, taxi conventionné",
            "Lundi 9 juin 2025",
            "16:30 - 17:00"
        ),
        CompletedTripInfo(
            "2",
            "Hélène A.",
            "Taxi Care, taxi conventionné",
            "Mercredi 7 mai 2025",
            "08:30 - 09:00"
        )
    )
    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(placeholderText = "Rechercher un transporteur ou une date")
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(sampleTrips) { trip ->
                CompletedTripCard(tripInfo = trip)
            }
        }
    }
}

@Composable
fun PendingRequestCard(requestInfo: PendingRequestInfo) {
    val cardColor = Color(0xFFFFB74D)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.Top) {
                Icon(
                    imageVector = requestInfo.profilePic,
                    contentDescription = requestInfo.transporterName,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color.White),
                    tint = cardColor
                )
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(requestInfo.transporterName, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 16.sp)
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Phone, contentDescription = "Phone", tint = Color.White, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(requestInfo.transporterPhone, color = Color.White, fontSize = 14.sp)
                    }
                }
                Text(requestInfo.requestAge, color = Color.White, fontSize = 12.sp, textAlign = TextAlign.End)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Filled.DateRange, contentDescription = "Date", tint = Color.White, modifier = Modifier.size(16.dp))
                Text(requestInfo.date, color = Color.White, fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp))
                Spacer(modifier = Modifier.width(16.dp))
                Icon(Icons.Filled.Check, contentDescription = "Time", tint = Color.White, modifier = Modifier.size(16.dp))
                Text(requestInfo.time, color = Color.White, fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { /* TODO: Handle Details Click */ },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = cardColor
                )
            ) {
                Text("Détails")
            }
        }
    }
}

@Composable
fun PendingRequestsPage() {
    val sampleRequests = listOf(
        PendingRequestInfo(
            "1",
            "UrgVSL",
            "05 56 00 01 02",
            "Lundi 30 juin 2025",
            "10:15",
            "Depuis 2h",
            profilePic = Icons.Filled.AccountCircle
        ),
        PendingRequestInfo(
            "2",
            "Taxi Care",
            "06 01 02 03 00",
            "Jeudi 17 juillet 2025",
            "16:45 - 17:20",
            "Depuis 4j"
        )
    )
    Column(modifier = Modifier.fillMaxSize()) {
        SearchBar(placeholderText = "Rechercher un transporteur ou une date")
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(sampleRequests) { request ->
                PendingRequestCard(requestInfo = request)
            }
        }
    }
}

@Composable
fun TripDetailsScreen(
    tripInfo: UpcomingTripInfo,
    onBack: () -> Unit,
    bottomNavDisplayItems: List<BottomNavItem>,
    selectedBottomNavIndex: Int,
    onBottomNavItemSelected: (Int) -> Unit,
    onQrCodeClicked: () -> Unit
) {
    val scrollState = rememberScrollState()
    val cardColor = Color(0xFF4285F4)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        floatingActionButton = {
            FloatingActionButton(
                onClick = onQrCodeClicked,
                modifier = Modifier.offset(y = 28.dp),
                containerColor = MaterialTheme.colorScheme.primary,
                shape = CircleShape
            ) {
                Icon(Icons.Filled.Settings, "QR Code", tint = Color.White) // TODO: Change to QR icon
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            BottomAppBar {
                bottomNavDisplayItems.forEachIndexed { index, item ->
                    if (index == 2) { Spacer(Modifier.weight(0.5f)) }
                    NavigationBarItem(
                        selected = selectedBottomNavIndex == index,
                        onClick = { onBottomNavItemSelected(index) },
                        icon = { Icon(item.icon, contentDescription = item.title) },
                        label = { Text(item.title) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor)
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                        .verticalScroll(scrollState)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        IconButton(onClick = onBack) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Retour", tint = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Contacter le transporteur", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                        Spacer(modifier = Modifier.weight(1f))
                        IconButton(onClick = { /* TODO: Call Action */ }) {
                            Icon(Icons.Filled.Phone, "Appeler", tint = Color.White)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        tripInfo.profileImagePainter?.let {
                            Image(
                                painter = it,
                                contentDescription = tripInfo.driverName,
                                modifier = Modifier.size(56.dp).clip(CircleShape),
                                contentScale = ContentScale.Crop
                            )
                        } ?: Icon(
                            imageVector = tripInfo.profilePicVector,
                            contentDescription = tripInfo.driverName,
                            modifier = Modifier.size(56.dp).clip(CircleShape).background(Color.White),
                            tint = cardColor
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(tripInfo.driverName, fontWeight = FontWeight.Bold, color = Color.White, fontSize = 20.sp)
                            Text(tripInfo.transportDetails, color = Color.White, fontSize = 14.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.DateRange, contentDescription = "Date", tint = Color.White, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(tripInfo.date, color = Color.White, fontSize = 14.sp)
                        Spacer(modifier = Modifier.width(16.dp))
                        Icon(Icons.Filled.Check, contentDescription = "Time", tint = Color.White, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(tripInfo.time, color = Color.White, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Rounded.LocationOn, contentDescription = "Route", tint = Color.White, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(tripInfo.routeDetails, color = Color.White, fontSize = 14.sp)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                            .background(Color.LightGray, RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("[Map Placeholder]", color = Color.DarkGray)
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Button(
                        onClick = onQrCodeClicked,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = cardColor)
                    ) {
                        Text("QR codes", fontSize = 16.sp)
                    }
                }
            }
        }
    }
}

@Composable
fun SearchScreenNoResultsView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        SearchBar(placeholderText = "Rechercher")
        Spacer(modifier = Modifier.height(64.dp))
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = "Aucun résultat",
            modifier = Modifier.size(100.dp),
            tint = Color.LightGray
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(text = "Aucun résultat", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Nous n'avons pas trouvé de transporteur correspondant à votre recherche.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = Color.Gray
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrCodeScreen(onBackClicked: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                        Text("Votre", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                        Text("QR Code", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Retour")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = MaterialTheme.colorScheme.onSurface,
                    navigationIconContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(24.dp)) {
                Spacer(modifier = Modifier.height(16.dp))
                val annotatedText = buildAnnotatedString {
                    append("A faire scanner par le ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("transporteur") }
                    append("\nau ")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) { append("départ") }
                    append(" du trajet.")
                }
                Text(text = annotatedText, textAlign = TextAlign.Center, style = MaterialTheme.typography.bodyLarge)
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.75f)
                        .aspectRatio(1f)
                        .background(Color(0xFFF0F0F0), RoundedCornerShape(16.dp))
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.qr_code),
                        contentDescription = "QR Code Placeholder",
                        modifier = Modifier.size(100.dp)
                    ) // TODO: Replace with actual QR code
                }
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 24.dp)
                    .clickable { /* TODO: Handle "Prochains QR codes" */ }
            ) {
                Text("Prochains QR codes", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.width(6.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
            }
        }
    }
}

@Composable
fun GreetingHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "Bonjour", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Marie Dubois", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        }
        Icon(
            imageVector = Icons.Filled.AccountCircle,
            contentDescription = "Profile Picture",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
        )
    }
}

@Composable
fun NotificationCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFE0EFFF)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.AccountCircle,
                contentDescription = "Jean D.",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color.LightGray)
            )
            Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
                Text(text = "Jean D.", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = "Ambu81, VSL", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.DateRange, contentDescription = "Date", modifier = Modifier.size(16.dp), tint = Color.Gray)
                    Text(text = "Lundi 23 juin 2025", fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp), color = Color.Gray)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Filled.Check, contentDescription = "Time", modifier = Modifier.size(16.dp), tint = Color.Gray)
                    Text(text = "11:00 - 12:00", fontSize = 12.sp, modifier = Modifier.padding(start = 4.dp), color = Color.Gray)
                }
            }
            IconButton(onClick = { /* TODO: Handle click */ }) {
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Details")
            }
        }
    }
}

@Composable
fun SearchBar(placeholderText: String) {
    var searchQuery by remember { mutableStateOf("") }
    OutlinedTextField(
        value = searchQuery,
        onValueChange = { searchQuery = it },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        placeholder = { Text(placeholderText) },
        leadingIcon = { Icon(Icons.Filled.Search, contentDescription = "Search Icon") },
        shape = RoundedCornerShape(24.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            cursorColor = MaterialTheme.colorScheme.primary,
            focusedContainerColor = Color(0xFFF0F0F0),
            unfocusedContainerColor = Color(0xFFF0F0F0),
            disabledContainerColor = Color(0xFFE0E0E0)
        )
    )
}

@Composable
fun ActionButtonsRow() {
    val buttons = listOf(
        ActionButtonData(Icons.Outlined.Call, "Réservation"),
        ActionButtonData(Icons.Outlined.Notifications, "Notification"),
        ActionButtonData(Icons.Outlined.Email, "Document"),
        ActionButtonData(Icons.Outlined.AccountBox, "Transaction")
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        buttons.forEach { buttonData ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .weight(1f)
                    .clickable { /* TODO: Action click */ }
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color(0xFFF0F0F0),
                    modifier = Modifier.size(56.dp)
                ) {
                    Icon(
                        imageVector = buttonData.icon,
                        contentDescription = buttonData.label,
                        modifier = Modifier.padding(14.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = buttonData.label, fontSize = 11.sp, textAlign = TextAlign.Center)
            }
        }
    }
}

@Composable
fun TransporterItemCard(transporter: TransporterInfo) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.Top) {
            val imagePainter = transporter.imageRes?.let { painterResource(id = it) }
            if (imagePainter != null) {
                Image(
                    painter = imagePainter,
                    contentDescription = transporter.name,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    Icons.Rounded.AccountCircle,
                    contentDescription = transporter.name,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                        .background(Color.LightGray)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = transporter.name, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text(text = transporter.phone, fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.height(8.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.Star, contentDescription = "Rating", tint = Color(0xFFFFC107), modifier = Modifier.size(16.dp))
                    Text(text = " ${transporter.rating} (${transporter.reviews} avis)", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(start = 4.dp))
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.DateRange, contentDescription = "Availability", tint = Color.Gray, modifier = Modifier.size(16.dp))
                    Text(text = " ${transporter.availability}", fontSize = 12.sp, color = Color.Gray, modifier = Modifier.padding(start = 4.dp))
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Rounded.LocationOn, contentDescription = "Distance", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(16.dp))
                Text(text = " ${transporter.distance}", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, modifier = Modifier.padding(start = 4.dp))
            }
        }
    }
}

@Composable
fun TransportersNearbySection() {
    val sampleTransporter = TransporterInfo(
        "1",
        "UrgVSL",
        "05 56 00 01 02",
        "1.2 KM",
        "4,8",
        "120",
        "Ouvert de 8h à 19h"
    )
    val sampleTransporter2 =
        TransporterInfo("2", "Taxi Care", "01 23 45 67 89", "2.5 KM", "4.5", "90", "Ouvert 24/7")

    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 16.dp)) {
        Text(text = "Transporteurs à proximité", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, modifier = Modifier.padding(bottom = 8.dp))
        TransporterItemCard(transporter = sampleTransporter)
        TransporterItemCard(transporter = sampleTransporter2)
    }
}

@Composable
fun HomeScreenContent() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
    ) {
        GreetingHeader()
        NotificationCard()
        SearchBar(placeholderText = "Rechercher un transporteur, une date, un lieu...")
        ActionButtonsRow()
        TransportersNearbySection()
    }
}

@Composable
fun ScreenContent(name: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Contenu pour $name")
    }
}
