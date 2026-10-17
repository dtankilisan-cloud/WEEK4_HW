package com.example.week4.Soal2roster

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---------- Warna ----------
private val Bg = Color(0xFF0C0C14)
private val CardBg = Color(0xFF15151F)
private val Border = Color(0xFF2A2A3A)
private val Gold = Color(0xFFFFB300)
private val Muted = Color(0xFF8A8AA0)
private val HpColor = Color(0xFFFF3B55)
private val MpColor = Color(0xFF1E88E5)
private val BarTrack = Color(0xFF0A0A10)

// ---------- Model ----------
data class Hero(
    val name: String,
    val heroClass: String,
    val rarity: String,
    val cp: Int,
    val level: Int,
    val hp: Int, val maxHp: Int,
    val mp: Int, val maxMp: Int,
    val atk: Int, val def: Int, val crit: String, val spd: Int,
    val skillName: String,
    val skillDesc: String,
    val accent: Color,           // warna class
    val avatar: String,          // placeholder emoji
    val imageRes: Int? = null,   // isi dengan R.drawable.xxx kalau asset sudah ada
    val inSquad: Boolean = false
)

private val heroes = listOf(
    Hero("Alistair", "High Templar", "MYTHIC", 5600, 50, 1650, 1800, 650, 800, 380, 540, "22%", 105,
        "Wrath of the Heavens",
        "Calls down a celestial hammer, stunning all enemies for 3 seconds and taunting survivors.",
        Color(0xFFFFB300), "🛡️"),
    Hero("Theresa", "Divine Oracle", "MYTHIC", 5480, 49, 1100, 1200, 2100, 2400, 210, 300, "15%", 98,
        "Sanctuary Bloom",
        "Creates a healing field that restores 8% max HP per second to all allies for 6 seconds.",
        Color(0xFF2ECC71), "🔮"),
    Hero("Vespera", "Shadowblade", "MYTHIC", 5350, 48, 890, 950, 410, 600, 620, 190, "48%", 160,
        "Umbral Flurry",
        "Vanishes and strikes the weakest enemy five times, each hit with a high crit chance.",
        Color(0xFFD500F9), "🗡️"),
    Hero("Elaria", "Archmage", "MYTHIC", 5120, 47, 450, 600, 1850, 2000, 700, 120, "30%", 110,
        "Arcane Cataclysm",
        "Unleashes a storm of arcane energy dealing massive area damage over 4 seconds.",
        Color(0xFF26C6DA), "🧙", inSquad = true),
    Hero("Brogan", "Berserker", "EPIC", 4700, 45, 2100, 2300, 120, 300, 560, 260, "35%", 92,
        "Blood Rage",
        "Gains attack speed and lifesteal as HP drops. Cannot be stunned while active.",
        Color(0xFFFF5252), "🪓"),
    Hero("Kael", "Berserker", "EPIC", 4400, 44, 1500, 2000, 80, 250, 520, 240, "28%", 90,
        "Savage Leap",
        "Leaps onto a target dealing heavy damage and slowing nearby enemies.",
        Color(0xFFFF5252), "⚔️")
)

private val filters = listOf("All (14)", "Berserker (2)", "Archmage (3)", "Shadowblade (2)", "Templar (1)", "Oracle (2)")

// ---------- Screen ----------
@Composable
fun Soal2Screen() {
    var selectedFilter by remember { mutableStateOf(0) }

    // Seluruh layar satu LazyColumn supaya bisa di-scroll ke bawah
    LazyColumn(
        modifier = Modifier.fillMaxSize().background(Bg),
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 24.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Spacer(Modifier.statusBarsPadding()) }
        item { Header() }
        item { SquadPanel() }
        item { SearchBar() }
        item {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Filter: horizontal scroll + bisa ditekan, warna berubah
                LazyRow(Modifier.weight(1f), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(filters.size) { i ->
                        FilterChip(filters[i], selected = i == selectedFilter) { selectedFilter = i }
                    }
                }
                Spacer(Modifier.width(8.dp))
                Surface(shape = RoundedCornerShape(8.dp), color = CardBg, border = BorderStroke(1.dp, Gold.copy(alpha = 0.6f))) {
                    Text("Sort: Power", Modifier.padding(horizontal = 10.dp, vertical = 7.dp),
                        color = Gold, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
        items(heroes, key = { it.name }) { HeroCard(it) }
    }
}

@Composable
private fun Header() {
    Row(Modifier.fillMaxWidth().padding(top = 12.dp), verticalAlignment = Alignment.CenterVertically) {
        Column(Modifier.weight(1f)) {
            Text("Party Roster", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
            Text("Guild Vanguard • 14 Heroes Available", color = Muted, fontSize = 12.sp)
        }
        Surface(shape = RoundedCornerShape(50), color = Gold.copy(alpha = 0.12f), border = BorderStroke(1.dp, Gold)) {
            Row(Modifier.padding(horizontal = 10.dp, vertical = 5.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, null, tint = Gold, modifier = Modifier.size(12.dp))
                Spacer(Modifier.width(4.dp))
                Text("Expedition", color = Gold, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun SquadPanel() {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        border = BorderStroke(1.dp, Gold.copy(alpha = 0.4f))
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(Modifier.size(7.dp).clip(CircleShape).background(Color(0xFF2ECC71)))
                Spacer(Modifier.width(6.dp))
                Text("EXPEDITION SQUAD (3/4)", Modifier.weight(1f), color = Muted, fontSize = 10.sp,
                    fontWeight = FontWeight.Bold, letterSpacing = 0.8.sp)
                Text("⚔ 14750 CP", color = Gold, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
            }
            Spacer(Modifier.height(10.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                listOf(heroes[0], heroes[1], heroes[3]).forEach {
                    Box(
                        Modifier.size(48.dp).clip(RoundedCornerShape(10.dp)).background(Bg)
                            .then(Modifier), contentAlignment = Alignment.Center
                    ) {
                        Surface(Modifier.fillMaxSize(), shape = RoundedCornerShape(10.dp), color = Bg,
                            border = BorderStroke(2.dp, it.accent)) {
                            Box(contentAlignment = Alignment.Center) { Text(it.avatar, fontSize = 22.sp) }
                        }
                    }
                    Spacer(Modifier.width(8.dp))
                }
                Surface(Modifier.size(48.dp), shape = RoundedCornerShape(10.dp), color = Color.Transparent,
                    border = BorderStroke(1.dp, Border)) {
                    Box(contentAlignment = Alignment.Center) { Icon(Icons.Default.Add, null, tint = Muted) }
                }
                Spacer(Modifier.weight(1f))
                Button(
                    onClick = { /* logic tidak perlu berfungsi */ },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Gold, contentColor = Color.Black)
                ) { Text("Deploy ⚡", fontWeight = FontWeight.Bold) }
            }
        }
    }
}

@Composable
private fun SearchBar() {
    Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(14.dp), color = CardBg, border = BorderStroke(1.dp, Border)) {
        Row(Modifier.padding(horizontal = 14.dp, vertical = 14.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Search, null, tint = Muted, modifier = Modifier.size(18.dp))
            Spacer(Modifier.width(10.dp))
            Text("Search by hero name, class, or rarity...", color = Muted, fontSize = 13.sp)
        }
    }
}

@Composable
private fun FilterChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = if (selected) Gold.copy(alpha = 0.15f) else CardBg,
        border = BorderStroke(1.dp, if (selected) Gold else Border)
    ) {
        Text(label, Modifier.padding(horizontal = 14.dp, vertical = 7.dp),
            color = if (selected) Gold else Color(0xFFB8B8CC), fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
    }
}

// ---------- Card (component) ----------
@Composable
fun HeroCard(hero: Hero) {
    var expanded by remember { mutableStateOf(false) }
    val borderColor = if (hero.inSquad) Gold else Border

    Card(
        onClick = { expanded = !expanded },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = CardBg),
        border = BorderStroke(1.dp, borderColor),
        modifier = Modifier.fillMaxWidth().animateContentSize()
    ) {
        Column(Modifier.padding(12.dp)) {
            // Baris badge + CP
            Row(verticalAlignment = Alignment.CenterVertically) {
                Tag(hero.rarity, HpColor)
                Spacer(Modifier.width(6.dp))
                Tag(hero.heroClass, hero.accent)
                Spacer(Modifier.weight(1f))
                Text("⚔ ${hero.cp} CP", color = Gold, fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                if (hero.inSquad) {
                    Spacer(Modifier.width(6.dp))
                    Tag("IN SQUAD", Gold)
                }
            }
            Spacer(Modifier.height(10.dp))

            // Avatar + nama + bar
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    Modifier.size(72.dp).clip(RoundedCornerShape(12.dp)).background(Bg),
                    contentAlignment = Alignment.Center
                ) {
                    if (hero.imageRes != null) {
                        Image(painterResource(hero.imageRes), hero.name, Modifier.fillMaxSize(), contentScale = ContentScale.Crop)
                    } else {
                        Text(hero.avatar, fontSize = 34.sp)
                    }
                    Surface(Modifier.align(Alignment.BottomEnd).padding(3.dp), shape = RoundedCornerShape(4.dp),
                        color = Color.Black.copy(alpha = 0.75f)) {
                        Text("Lvl ${hero.level}", Modifier.padding(horizontal = 4.dp, vertical = 1.dp),
                            color = Color.White, fontSize = 9.sp, fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(Modifier.width(14.dp))
                Column(Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(hero.name, Modifier.weight(1f), color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Icon(
                            if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            null, tint = Muted
                        )
                    }
                    Spacer(Modifier.height(4.dp))
                    StatBar("HP", hero.hp, hero.maxHp, HpColor)
                    Spacer(Modifier.height(4.dp))
                    StatBar("MP", hero.mp, hero.maxMp, MpColor)
                }
            }

            // Dropdown (bonus)
            AnimatedVisibility(expanded) {
                Column {
                    Spacer(Modifier.height(12.dp))
                    HorizontalDivider(color = Border)
                    Spacer(Modifier.height(12.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        StatBox("ATK", "${hero.atk}", Color(0xFFFF7043), Modifier.weight(1f))
                        StatBox("DEF", "${hero.def}", Color(0xFF9E9EB8), Modifier.weight(1f))
                        StatBox("CRIT", hero.crit, Color(0xFFFFB300), Modifier.weight(1f))
                        StatBox("SPD", "${hero.spd}", Color(0xFF26C6DA), Modifier.weight(1f))
                    }
                    Spacer(Modifier.height(10.dp))

                    Surface(Modifier.fillMaxWidth(), shape = RoundedCornerShape(10.dp), color = Bg, border = BorderStroke(1.dp, Border)) {
                        Column(Modifier.padding(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Star, null, tint = Gold, modifier = Modifier.size(13.dp))
                                Spacer(Modifier.width(6.dp))
                                Text(hero.skillName, color = Gold, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                            Spacer(Modifier.height(4.dp))
                            Text(hero.skillDesc, color = Muted, fontSize = 11.sp, lineHeight = 16.sp)
                        }
                    }
                    Spacer(Modifier.height(10.dp))

                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        ActionButton("Heal (+250 HP)", Color(0xFF2ECC71), Modifier.weight(1f))
                        ActionButton("Add to Squad", Gold, Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

@Composable
private fun Tag(text: String, color: Color) {
    Surface(shape = RoundedCornerShape(4.dp), color = color.copy(alpha = 0.15f), border = BorderStroke(1.dp, color.copy(alpha = 0.7f))) {
        Text(text, Modifier.padding(horizontal = 6.dp, vertical = 2.dp), color = color, fontSize = 9.sp, fontWeight = FontWeight.Bold)
    }
}

/** Panjang bar bergantung pada value / maxValue. */
@Composable
private fun StatBar(label: String, value: Int, max: Int, color: Color) {
    val fraction = (value.toFloat() / max).coerceIn(0f, 1f)
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, Modifier.width(26.dp), color = Muted, fontSize = 10.sp, fontWeight = FontWeight.Bold)
        Column(Modifier.weight(1f)) {
            Box(Modifier.fillMaxWidth().height(8.dp).clip(RoundedCornerShape(50)).background(BarTrack)) {
                Box(Modifier.fillMaxWidth(fraction).fillMaxHeight().clip(RoundedCornerShape(50)).background(color))
            }
            Text("$value / $max", Modifier.fillMaxWidth(), color = Muted, fontSize = 10.sp, textAlign = TextAlign.End)
        }
    }
}

@Composable
private fun StatBox(label: String, value: String, labelColor: Color, modifier: Modifier = Modifier) {
    Surface(modifier, shape = RoundedCornerShape(10.dp), color = Bg, border = BorderStroke(1.dp, Border)) {
        Column(Modifier.padding(vertical = 8.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, color = labelColor, fontSize = 9.sp, fontWeight = FontWeight.Bold)
            Text(value, color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
        }
    }
}

@Composable
private fun ActionButton(text: String, color: Color, modifier: Modifier = Modifier) {
    Surface(
        onClick = { /* logic tidak perlu berfungsi */ },
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        color = color.copy(alpha = 0.12f),
        border = BorderStroke(1.dp, color)
    ) {
        Text(text, Modifier.padding(vertical = 10.dp), color = color, fontSize = 12.sp,
            fontWeight = FontWeight.Bold, textAlign = TextAlign.Center)
    }
}