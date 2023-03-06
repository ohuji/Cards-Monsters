package com.ohuji.cardsNmonsters

import android.app.Application
import com.ohuji.cardsNmonsters.screens.augmented_reality.GameLogicViewModel
import junit.framework.TestCase.assertEquals
import org.junit.Test

class GameLogicViewModelTest {

    @Test
    fun testGetStartingHealth() {
        val viewModel = GameLogicViewModel(Application())

        val dragonId = 1L
        val griffinId = 2L
        val mammothId = 3L
        val mountainLionId = 4L
        val skeletonId = 5L
        val seaVikingId = 6L
        val youngDragonId = 7L

        assertEquals(2000, viewModel.getStartingHealth(dragonId))
        assertEquals(1200, viewModel.getStartingHealth(griffinId))
        assertEquals(1500, viewModel.getStartingHealth(mammothId))
        assertEquals(800, viewModel.getStartingHealth(mountainLionId))
        assertEquals(700, viewModel.getStartingHealth(skeletonId))
        assertEquals(750, viewModel.getStartingHealth(seaVikingId))
        assertEquals(1000, viewModel.getStartingHealth(youngDragonId))

        assertEquals(50, viewModel.getStartingHealth(-1L))
    }
}
