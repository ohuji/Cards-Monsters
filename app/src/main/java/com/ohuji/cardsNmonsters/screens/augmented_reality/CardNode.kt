package com.ohuji.cardsNmonsters.screens.augmented_reality

import android.content.Context
import androidx.lifecycle.Lifecycle
import io.github.sceneview.math.Position
import io.github.sceneview.node.ViewNode

open class CardNode(context: Context, lifecycle: Lifecycle?, layout: String, x: Float, y: Float) :
    ViewNode() {

    init {
        isSelectable = false
        position = Position(x = x, y = y, z = -2.0f)
        when (layout) {
            "test" -> loadView(context, lifecycle, com.ohuji.cardsNmonsters.R.layout.test_card_node_layout)
        }
    }

}