package com.example.week4.Soal1cars

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.week4.R

// ---------- Warna ----------
private val Orange = Color(0xFFFF5A36)
private val BgColor = Color(0xFFF5F1EE)
private val TextDark = Color(0xFF111827)
private val TextGray = Color(0xFF8A8A8A)

// ---------- Model ----------
data class Car(
    val id: Int,
    val name: String,
    val fuel: String,            // PETROL / PHEV / HYBRID
    val transmission: String,    // Auto / Manual
    val year: Int,
    val mileage: String,
    val price: String,
    val location: String,
    val description: String,
    val colorName: String,       // nama warna, mis. "Hitam"
    val colorValue: Color,       // warna untuk titik dan placeholder
    val imageRes: Int? = null,   // isi dengan R.drawable.xxx kalau asset sudah ada
    val isFavorite: Boolean = false
)

val sampleCars = listOf(
    Car(1, "Toyota Zenix Hybrid", "HYBRID", "Auto", 2023, "20,000 km", "Rp 470.000.000", "Jakarta",
        "Toyota Kijang Innova Zenix Hybrid adalah MPV premium dengan mesin hybrid 2.0L yang irit, kabin luas, dan fitur Toyota Safety Sense lengkap.",
        "Hitam", Color(0xFF1C1C1E),
        imageRes = R.drawable.toyota_zenix_hybrid,
        isFavorite = true),
    Car(2, "Toyota Camry", "PETROL", "Auto", 2019, "45,000 km", "Rp 520.000.000", "Surabaya",
        "Sedan eksekutif Toyota yang nyaman dan senyap, dengan mesin 2.5L bertenaga serta interior mewah untuk perjalanan jauh.",
        "Silver", Color(0xFFC0C0C8)
        , imageRes = R.drawable.toyota_camry
    ),
    Car(3, "Toyota Innova Reborn", "DIESEL", "Auto", 2020, "60,000 km", "Rp 350.000.000", "Bandung",
        "MPV 7 penumpang favorit keluarga Indonesia. Mesin diesel 2.4L bertenaga dan irit, perawatan mudah, dan nilai jual kembali tinggi.",
        "Hitam", Color(0xFF1C1C1E),
        imageRes = R.drawable.toyota_innova_reborn,
        isFavorite = true),
    Car(4, "Wuling Air EV", "EV", "Auto", 2023, "12,000 km", "Rp 180.000.000", "Semarang",
        "Mobil listrik kompak yang lincah untuk dalam kota. Tanpa BBM, biaya operasional sangat rendah, dan mudah parkir.",
        "Putih", Color(0xFFFFFFFF)
        , imageRes = R.drawable.wuling_air_ev
    ),
    Car(5, "Toyota Fortuner VRZ", "DIESEL", "Auto", 2022, "35,000 km", "Rp 560.000.000", "Yogyakarta",
        "SUV tangguh 7 penumpang dengan mesin diesel 2.8L bertenaga, suspensi nyaman, dan fitur lengkap untuk jalan raya maupun medan berat.",
        "Hitam", Color(0xFF1C1C1E)
        , imageRes = R.drawable.toyota_fortuner_vrz
    )
)

private fun fuelLabel(fuel: String): String = when (fuel) {
    "PETROL" -> "Petrol"
    "DIESEL" -> "Diesel"
    "HYBRID" -> "Hybrid"
    "EV" -> "Electric"
    else -> fuel
}

private fun fuelColors(fuel: String): Pair<Color, Color> = when (fuel) {
    "PHEV" -> Color(0xFFE9E1FA) to Color(0xFF5B3FB5)
    "HYBRID" -> Color(0xFFD9F1EE) to Color(0xFF127A70)
    "EV" -> Color(0xFFDDF4E0) to Color(0xFF1E8E3E)
    else -> Color(0xFFFFEBCF) to Color(0xFFE8890C) // PETROL
}

// ---------- Entry point soal 1 ----------
@Composable
fun Soal1Screen() {
    val cars = remember { mutableStateListOf(*sampleCars.toTypedArray()) }
    var selectedId by remember { mutableStateOf<Int?>(null) }

    val toggleFavorite: (Int) -> Unit = { id ->
        val i = cars.indexOfFirst { it.id == id }
        if (i >= 0) cars[i] = cars[i].copy(isFavorite = !cars[i].isFavorite)
    }

    val selected = cars.firstOrNull { it.id == selectedId }
    BackHandler(enabled = selected != null) { selectedId = null }

    if (selected == null) {
        CarListScreen(cars, onToggle = toggleFavorite, onClick = { selectedId = it.id })
    } else {
        CarDetailScreen(selected, onToggle = { toggleFavorite(selected.id) }, onBack = { selectedId = null })
    }
}

// ---------- List ----------
@Composable
fun CarListScreen(cars: List<Car>, onToggle: (Int) -> Unit, onClick: (Car) -> Unit) {
    Column(Modifier.fillMaxSize().background(BgColor).statusBarsPadding()) {
        Row(
            Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 16.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            Column(Modifier.weight(1f)) {
                Box(Modifier.width(32.dp).height(3.dp).background(Orange))
                Spacer(Modifier.height(8.dp))
                Text("On Sale", fontSize = 28.sp, fontWeight = FontWeight.ExtraBold, color = TextDark)
                Text("Find your perfect ride", fontSize = 12.sp, color = TextGray)
            }
            Surface(
                shape = RoundedCornerShape(50),
                color = Orange.copy(alpha = 0.08f),
                border = BorderStroke(1.dp, Orange.copy(alpha = 0.4f))
            ) {
                Row(Modifier.padding(horizontal = 10.dp, vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Favorite, null, tint = Orange, modifier = Modifier.size(12.dp))
                    Spacer(Modifier.width(4.dp))
                    Text("${cars.count { it.isFavorite }} saved", fontSize = 11.sp, color = Orange, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        // Scroll ke bawah
        LazyColumn(
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(cars, key = { it.id }) { car ->
                CarCard(car, onToggle = { onToggle(car.id) }, onClick = { onClick(car) })
            }
        }
    }
}

// ---------- Card (component) ----------
@Composable
fun CarCard(car: Car, onToggle: () -> Unit, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(Modifier.padding(10.dp)) {
            Box(Modifier.size(96.dp).clip(RoundedCornerShape(14.dp))) {
                CarImage(car, Modifier.fillMaxSize())
                FuelBadge(car.fuel, Modifier.align(Alignment.BottomStart).padding(5.dp))
            }
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f).height(96.dp), verticalArrangement = Arrangement.SpaceBetween) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(car.name, Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 15.sp, color = TextDark, maxLines = 1)
                    FavoriteButton(car.isFavorite, onToggle, Modifier.size(32.dp))
                }
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    InfoChip(car.transmission)
                    InfoChip(car.colorName, dot = car.colorValue)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(6.dp).clip(CircleShape).background(Orange))
                    Spacer(Modifier.width(4.dp))
                    Text("${car.year}", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextDark)
                    Spacer(Modifier.weight(1f))
                    Text(car.mileage, fontSize = 11.sp, color = TextGray)
                }
            }
        }
    }
}

@Composable
private fun ColorDot(color: Color) {
    Box(Modifier.size(9.dp).clip(CircleShape).background(color)
        .border(1.dp, Color(0xFFBDB6B0), CircleShape))
}

@Composable
private fun InfoChip(text: String, dot: Color? = null) {
    Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFF1EEEB)) {
        Row(Modifier.padding(horizontal = 8.dp, vertical = 3.dp), verticalAlignment = Alignment.CenterVertically) {
            if (dot != null) { ColorDot(dot); Spacer(Modifier.width(4.dp)) }
            Text(text, fontSize = 10.sp, color = TextGray)
        }
    }
}

@Composable
private fun FavoriteButton(isFav: Boolean, onToggle: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = onToggle, modifier = modifier) {
        Icon(
            if (isFav) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = "Favorite",
            tint = if (isFav) Orange else Color(0xFFB5B5B5)
        )
    }
}

@Composable
private fun FuelBadge(fuel: String, modifier: Modifier = Modifier) {
    val (bg, fg) = fuelColors(fuel)
    Surface(modifier, shape = RoundedCornerShape(6.dp), color = bg) {
        Text(fuel, Modifier.padding(horizontal = 6.dp, vertical = 2.dp), fontSize = 9.sp, fontWeight = FontWeight.ExtraBold, color = fg)
    }
}

@Composable
private fun CarImage(car: Car, modifier: Modifier) {
    if (car.imageRes != null) {
        Image(painterResource(car.imageRes), car.name, modifier, contentScale = ContentScale.Crop)
    } else {
        Box(modifier.background(car.colorValue), contentAlignment = Alignment.Center) {
            Text("🚗", fontSize = 36.sp)
        }
    }
}

// ---------- Detail ----------
@Composable
fun CarDetailScreen(car: Car, onToggle: () -> Unit, onBack: () -> Unit) {
    Column(Modifier.fillMaxSize().background(BgColor)) {
        Column(Modifier.weight(1f).verticalScroll(rememberScrollState())) {
            Box {
                CarImage(car, Modifier.fillMaxWidth().height(280.dp))
                IconButton(
                    onClick = onBack,
                    modifier = Modifier.statusBarsPadding().padding(12.dp).size(40.dp)
                        .clip(CircleShape).background(Color.White)
                ) { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back") }
                FuelBadge(car.fuel, Modifier.align(Alignment.BottomStart).padding(start = 16.dp, bottom = 12.dp))
            }

            Column(Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(car.name, Modifier.weight(1f), fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = TextDark)
                    FavoriteButton(car.isFavorite, onToggle)
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(Modifier.size(6.dp).clip(CircleShape).background(Orange))
                    Spacer(Modifier.width(6.dp))
                    Text(car.location, fontSize = 12.sp, color = TextGray)
                }
                Spacer(Modifier.height(14.dp))
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = Orange.copy(alpha = 0.08f),
                    border = BorderStroke(1.dp, Orange.copy(alpha = 0.4f))
                ) {
                    Text(car.price, Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
                        color = Orange, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }

                SectionLabel("SPECIFICATIONS")
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SpecItem("YEAR", "${car.year}", Modifier.weight(1f))
                    SpecItem("MILEAGE", car.mileage, Modifier.weight(1f))
                }
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SpecItem("TRANSMISSION", car.transmission, Modifier.weight(1f))
                    SpecItem("FUEL TYPE", fuelLabel(car.fuel), Modifier.weight(1f))
                }
                Spacer(Modifier.height(10.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    SpecItem("COLOR", car.colorName, Modifier.weight(1f), dot = car.colorValue)
                    Spacer(Modifier.weight(1f))
                }

                SectionLabel("ABOUT THIS CAR")
                Text(car.description, fontSize = 13.sp, lineHeight = 20.sp, color = TextGray)
            }
        }

        // Bottom bar
        Row(
            Modifier.fillMaxWidth().navigationBarsPadding().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedIconButton(
                onClick = onToggle,
                modifier = Modifier.size(48.dp),
                shape = RoundedCornerShape(14.dp),
                border = BorderStroke(1.dp, Orange.copy(alpha = 0.5f))
            ) {
                Icon(
                    if (car.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    null, tint = Orange
                )
            }
            Button(
                onClick = { /* TODO: hubungi penjual */ },
                modifier = Modifier.weight(1f).height(48.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Orange)
            ) { Text("Contact Seller", fontWeight = FontWeight.Bold) }
        }
    }
}

@Composable
private fun SectionLabel(text: String) {
    Spacer(Modifier.height(22.dp))
    Text(text, fontSize = 10.sp, letterSpacing = 1.sp, fontWeight = FontWeight.SemiBold, color = TextGray)
    Spacer(Modifier.height(10.dp))
}

@Composable
private fun SpecItem(label: String, value: String, modifier: Modifier = Modifier, dot: Color? = null) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFEDE7E2))
    ) {
        Column(Modifier.padding(12.dp)) {
            Text(label, fontSize = 9.sp, letterSpacing = 0.8.sp, color = TextGray)
            Spacer(Modifier.height(6.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (dot != null) { ColorDot(dot); Spacer(Modifier.width(6.dp)) }
                Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextDark)
            }
        }
    }
}