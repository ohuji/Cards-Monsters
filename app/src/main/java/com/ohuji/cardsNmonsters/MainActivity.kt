package com.ohuji.cardsNmonsters

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.ohuji.cardsNmonsters.database.CardsNMonstersDatabase
import com.ohuji.cardsNmonsters.screens.augmented_reality.GameLogicViewModel
import com.ohuji.cardsNmonsters.screens.collectables.CollectablesViewModel
import com.ohuji.cardsNmonsters.screens.deck_building.DeckViewModel
import com.ohuji.cardsNmonsters.screens.maps.MapViewModel
import com.ohuji.cardsNmonsters.ui.theme.CardsMonstersTheme
import com.ohuji.cardsNmonsters.utils.NavigationHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val deckViewModel: DeckViewModel by viewModels()
    private val collectablesViewModel: CollectablesViewModel by viewModels()
    private val gameLogicViewModel: GameLogicViewModel by viewModels()
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private val mapViewModel: MapViewModel by viewModels()
    private val database by lazy { CardsNMonstersDatabase.getInstance(this)}

    override fun onDestroy() {
        super.onDestroy()
        database.databaseWriteExecutor.shutdown()
    }

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                mapViewModel.getDeviceLocation(fusedLocationProviderClient)
            }
        }

    private fun askPermissions() = when {
        ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED -> {
            mapViewModel.getDeviceLocation(fusedLocationProviderClient)
        }
        else -> {
            requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        askPermissions()
        setContent {
            CardsMonstersTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    NavigationHost(deckViewModel, collectablesViewModel, gameLogicViewModel,
                    mapViewModel = mapViewModel,
                    setupClusterManager = mapViewModel::setupClusterManager,
                    calculateZoneViewCenter = mapViewModel::calculateZoneLatLngBounds,
                    fusedLocationProviderClient = fusedLocationProviderClient
                    )
                }
            }
        }
    }
}