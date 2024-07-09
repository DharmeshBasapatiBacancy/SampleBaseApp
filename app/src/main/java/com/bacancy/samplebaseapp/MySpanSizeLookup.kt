package com.bacancy.samplebaseapp

import androidx.recyclerview.widget.GridLayoutManager

class MySpanSizeLookup: GridLayoutManager.SpanSizeLookup() {
    override fun getSpanSize(position: Int): Int {
        return when(position) {
            0, 1, 2 -> 1 // First row: 3 items, each spanning 1 column
            3, 4 -> 2 // Second and third rows: 2 items each, spanning 2 columns
            5, 6 -> 1 // Second and third rows: 2 items each, spanning 2 columns
            else -> 1 // Default span size
        }
    }
}