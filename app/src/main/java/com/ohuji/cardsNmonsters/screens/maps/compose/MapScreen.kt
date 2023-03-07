package com.ohuji.cardsNmonsters.screens.maps.compose

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.*
import com.google.maps.android.ktx.model.polygonOptions
import com.ohuji.cardsNmonsters.R
import com.ohuji.cardsNmonsters.screens.augmented_reality.ShowDialog
import com.ohuji.cardsNmonsters.screens.collectables.CollectablesViewModel
import com.ohuji.cardsNmonsters.screens.deck_building.DeckViewModel
import com.ohuji.cardsNmonsters.screens.maps.MapViewModel
import com.ohuji.cardsNmonsters.screens.maps.clusters.ZoneClusterItem
import com.ohuji.cardsNmonsters.screens.maps.clusters.ZoneClusterManager
import kotlinx.coroutines.launch
import kotlin.random.Random

var selectedDeckId: Long = 0

/**
 * A Composable that displays a map view and shows nearby monsters to the user.
 *
 * @param mapViewModel the ViewModel for the map screen
 * @param navController the NavController that handles navigation
 * @param setupClusterManager a function that sets up a [ZoneClusterManager] for the GoogleMap
 * @param calculateZoneViewCenter a function that calculates the LatLngBounds for the map view
 * @param fusedLocationProviderClient the FusedLocationProviderClient that provides location services
 * @param deckViewModel the ViewModel for the user's decks
 * @param monsterViewModel the ViewModel for the fightable monsters
 */
@SuppressLint("PotentialBehaviorOverride")
@Composable
fun MapScreen(
    mapViewModel: MapViewModel = viewModel(),
    navController: NavController,
    setupClusterManager: (Context, GoogleMap) -> ZoneClusterManager,
    calculateZoneViewCenter: () -> LatLngBounds,
    fusedLocationProviderClient: FusedLocationProviderClient,
    deckViewModel: DeckViewModel,
    monsterViewModel: CollectablesViewModel,
) {

    /**
     * The number of decks the user has. If there are no decks, a warning dialog is displayed.
     */
    val deckCheck = deckViewModel.getAllDecks().observeAsState().value?.size
    var showNoDeckAlert by remember { mutableStateOf(false) }

    /**
     * Dismisses the no deck warning dialog and navigates to the deck building screen.
     */
    fun noDeckDialogDismiss() {
        showNoDeckAlert = false
        navController.navigate("deck_building_screen")
    }

    /**
     * Starts the fight with the monster and navigates to the augmented reality screen.
     *
     * @param monsterId the ID of the monster to fight
     * @param deckId the ID of the deck to use
     */
    fun fightMonsterDismiss(monsterId: Long, deckId: Long) {
        navController.navigate("ar_screen/${monsterId}/${deckId}")
    }

    if (deckCheck?.let { it < 1 } == true) {
        showNoDeckAlert = true
    }

    val state by remember {
        mapViewModel.state
    }

    val location by remember {
        mapViewModel.getDevicePreciseLocation(fusedLocationProviderClient)
    }

    var fightMonster by remember {
        mutableStateOf(false)
    }

    var fightMonsterNoNearby by remember {
        mutableStateOf(true)
    }

    val monsterMaxId = monsterViewModel.getAllMonsters().observeAsState().value?.size

    /**
     * A list of all the LatLngs of all the monsters on the map
     */
    var arrOfMonstersLatLng: List<List<LatLng>> =
        state.clusterItems.map { it.polygonOptions.points }

    // When the user's location changes, remove all the current cluster items from the map and generate 10 new monsters
    LaunchedEffect(location) {
        if (location == LatLng(0.0, 0.0)) {
            return@LaunchedEffect
        }

        if (!fightMonsterNoNearby) {
            fightMonsterNoNearby = true
        }

        mapViewModel.removeClusterItems()

        for (i in 1..10) {
            val newMonster = mapViewModel.generateLatLng(
                location,
                Random.nextDouble(0.0, 1500.0),
                Random.nextDouble(0.0, 365.0)
            )
            val cluster = ZoneClusterItem(
                id = "id",
                title = "",
                snippet = "",
                polygonOptions = polygonOptions {
                    add(newMonster)
                }
            )
            mapViewModel.addClusterItem(cluster)
        }
        arrOfMonstersLatLng = state.clusterItems.map { it.polygonOptions.points }
    }


    /**
     * Checks if [fightMonster] is true, and if so, looks for a monster within a 100m radius of the user's location.
     * After scan button is clicked, displays an alert dialog with a message indicating the monster's name and type, and a "Fight" button.
     * If the "Fight" button is clicked, calls the [fightMonsterDismiss] function with the ID of the found monster and the ID of the selected deck.
     * If no monster is found, displays an alert dialog with a message indicating that no monsters were found and instructing the user to move closer to a monster.
     *
     * @param fightMonster Boolean indicating if the player is currently searching for a monster to fight.
     * @param monsterMaxId The maximum ID of the available monsters.
     * @param monsterViewModel The ViewModel containing the data for the available monsters.
     * @param arrOfMonstersLatLng A 2D list of LatLng objects representing the locations of the available monsters.
     * @param location The LatLng object representing the user's current location.
     * @param fightMonsterNoNearby Boolean indicating if a monster was found within the 100m radius of the user's location.
     * @param selectedDeckId The ID of the selected deck.
     */
    if (fightMonster) {
        val randomId = if (monsterMaxId != null) (0..monsterMaxId).random() else 5
        val monster = monsterViewModel.findMonsterById(randomId.toLong()).observeAsState().value

        for (list in arrOfMonstersLatLng) {
            for (latLng in list) {
                val result = FloatArray(1)
                Location.distanceBetween(
                    location.latitude,
                    location.longitude,
                    latLng.latitude,
                    latLng.longitude,
                    result
                )
                if (result[0] < 100.0 && location != LatLng(0.0, 0.0)) {
                    fightMonsterNoNearby = false
                    val message =
                        "${stringResource(R.string.map_alert1)} ${monster?.monsterName ?: "Skeleton"}! \n${
                            stringResource(
                                R.string.map_alert2
                            )
                        } ${monster?.monsterElement ?: "Dark"}!"
                    ShowAlertFound(
                        deckViewModel = deckViewModel,
                        title = stringResource(R.string.map_alert_title),
                        message = message,
                        buttons = listOf(
                            "Fight" to {
                                fightMonsterDismiss(
                                    monster?.monsterId ?: 5,
                                    deckId = selectedDeckId
                                )
                            },
                        )
                    ) { fightMonster = false }
                }
            }
        }

        if (fightMonsterNoNearby) {
            ShowAlertNotFound(
                title = stringResource(R.string.map_no_alert_title),
                message = stringResource(R.string.map_no_alert_message),
                onDismiss = { fightMonsterNoNearby = false }
            )
        }

    }

    /**
     * This sets up the Google Map view and its properties. It adds the markers to the map
     * and animates the camera to show the cluster of monsters/markers when they are present. It also
     * shows a button to scan for monsters.
     *
     * @param state The current state of the map view, including the last known location and the list of cluster items.
     * @param fightMonster A boolean flag that is set to true when the user clicks on the "Scan for monsters" button.
     */
    val mapProperties = MapProperties(
        isMyLocationEnabled = state.lastKnownLocation != null,
    )
    val cameraPositionState = rememberCameraPositionState()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            properties = mapProperties,
            cameraPositionState = cameraPositionState
        ) {
            val context = LocalContext.current
            val scope = rememberCoroutineScope()

            MapEffect(state.clusterItems) { map ->
                if (state.clusterItems.isNotEmpty()) {
                    val clusterManager = setupClusterManager(context, map)

                    map.setOnCameraIdleListener(clusterManager)
                    map.setOnMarkerClickListener(clusterManager)

                    state.clusterItems.forEach { clusterItem ->
                        map.addPolygon(clusterItem.polygonOptions)
                    }

                    map.setOnMapLoadedCallback {
                        if (state.clusterItems.isNotEmpty()) {
                            scope.launch {
                                cameraPositionState.animate(
                                    update = CameraUpdateFactory.newLatLngBounds(
                                        calculateZoneViewCenter(),
                                        0
                                    ),
                                )
                            }
                        }
                    }
                }
            }
        }

        Box(
            modifier = Modifier
                .padding(10.dp)
                .padding(bottom = 40.dp)
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        ) {
            OutlinedButton(
                onClick = { fightMonster = true },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = androidx.compose.material3.MaterialTheme.colorScheme.primary,
                    contentColor = androidx.compose.material3.MaterialTheme.colorScheme.onSurfaceVariant
                ),
                border = BorderStroke(
                    2.dp,
                    androidx.compose.material3.MaterialTheme.colorScheme.background
                ),
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 35.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    modifier = Modifier.padding(end = 10.dp)
                )

                Text(text = stringResource(R.string.map_scan_button))
            }
        }
    }

    if (showNoDeckAlert) {
        ShowDialog(
            title = stringResource(R.string.map_no_deck_title),
            message = stringResource(R.string.map_no_deck_message),
            onDismiss = { noDeckDialogDismiss() }
        )
    }
}

/**
 * Composable that shows an alert dialog when a monster is found. The dialog displays the details of the monster
 * and allows the user to choose to fight the monster or dismiss it.
 *
 * @param deckViewModel a ViewModel that provides access to the data of the decks
 * @param title a String that represents the title of the alert dialog
 * @param message a String that represents the message to be displayed in the alert dialog
 * @param buttons a List of pairs, where each pair consists of a String that represents the label of the button,
 * and a lambda function that handles the logic for the button when it is clicked
 * @param onDismiss a lambda function that handles the logic for dismissing the alert dialog
 */
@Composable
fun ShowAlertFound(
    deckViewModel: DeckViewModel,
    title: String,
    message: String,
    buttons: List<Pair<String, () -> Unit>>,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { androidx.compose.material3.Text(title) },
        text = {
            Column(modifier = Modifier.padding(10.dp)) {
                androidx.compose.material3.Text(message, fontSize = 16.sp)
            }
            Column(modifier = Modifier.padding(top = 60.dp)) {
                DropDownMenu(deckViewModel = deckViewModel)
            }
        },
        confirmButton = {
            androidx.compose.material3.Button(onClick = buttons[0].second) {
                androidx.compose.material3.Text(stringResource(R.string.map_fight))
            }
        },
        dismissButton = {
            androidx.compose.material3.Button(
                onClick = onDismiss
            ) {
                androidx.compose.material3.Text(stringResource(R.string.map_run))
            }
        },
    )
}

/**
 * Composable that shows an alert dialog when no monsters are found nearby. The dialog displays a message
 * that informs the user that no monsters are nearby and allows the user to dismiss the dialog.
 *
 * @param title a String that represents the title of the alert dialog
 * @param message a String that represents the message to be displayed in the alert dialog
 * @param onDismiss a lambda function that handles the logic for dismissing the alert dialog
 */

@Composable
fun ShowAlertNotFound(
    title: String,
    message: String,
    onDismiss: () -> Unit
) {
    androidx.compose.material3.AlertDialog(
        onDismissRequest = onDismiss,
        title = { androidx.compose.material3.Text(title) },
        text = { androidx.compose.material3.Text(message) },
        confirmButton = {
            androidx.compose.material3.Button(onClick = onDismiss) {
                androidx.compose.material3.Text("OK")
            }
        }
    )
}


/**
 * Composable that displays a drop-down menu that allows the user to select a deck.
 *
 * @param deckViewModel a ViewModel that provides access to the data of the decks
 */
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun DropDownMenu(deckViewModel: DeckViewModel) {
    val deckList = deckViewModel.getAllDecks().observeAsState().value
    val deckOptions = deckList?.map { deck -> deck.deckName }?.toTypedArray()

    var selectedItem by remember {
        mutableStateOf(deckList?.get(0)?.deckName ?: "No deck created")
    }

    var expanded by remember {
        mutableStateOf(false)
    }

    // the box
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {
        // text field
        Box(
            modifier = Modifier.padding(30.dp)
        ) {
            TextField(
                value = selectedItem,
                textStyle = TextStyle(color = Color(0xFFC8CAD5)),
                onValueChange = {},
                readOnly = true,
                label = {
                    Text(
                        text = stringResource(R.string.map_deck_dropdown),
                        style = TextStyle(color = Color(0xFFC8CAD5)),
                        fontWeight = FontWeight.Bold
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded
                    )
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors()
            )
        }

        // menu
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            deckOptions?.forEach { selectedOption ->
                // menu item
                DropdownMenuItem(onClick = {
                    selectedItem = selectedOption
                    selectedDeckId =
                        deckList.find { deck -> deck.deckName == selectedOption }?.deckId ?: 1
                    expanded = false
                }) {
                    Text(text = selectedOption)
                }
            }
        }
    }
}


