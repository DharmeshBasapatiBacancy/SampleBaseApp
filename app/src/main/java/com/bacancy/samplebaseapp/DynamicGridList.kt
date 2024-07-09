package com.bacancy.samplebaseapp

import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView

class DynamicGridList(private val context: Context, private val parentView: ViewGroup) {

    private fun createDynamicLinearLayout(itemNames: List<String>): LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            itemNames.forEachIndexed { index, name ->
                addView(createDynamicRowItem(context, name, index == 0, index == itemNames.size - 1))
                if (index != itemNames.size - 1) addView(createDynamicDivider(context))
            }
        }
    }

    private fun createDynamicRowItem(
        context: Context,
        text: String,
        isFirstItem: Boolean,
        isLastItem: Boolean
    ): LinearLayout {
        val rowItemLayout = LayoutInflater.from(context).inflate(R.layout.your_item_layout, parentView, false) as LinearLayout

        rowItemLayout.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        ).apply {
            weight = 1.0f
        }

        rowItemLayout.gravity = when {
            isFirstItem -> Gravity.START
            isLastItem -> Gravity.END
            else -> Gravity.CENTER
        }

        rowItemLayout.findViewById<TextView>(R.id.tvType).text = text

        return rowItemLayout
    }

    private fun createDynamicDivider(context: Context): View {
        val inflater = LayoutInflater.from(context)
        val rowItemLayout = inflater.inflate(R.layout.custom_divider, parentView, false) as LinearLayout
        return rowItemLayout
    }

    fun setupDynamicViews(dataList: List<String>) {

        val firstList = dataList.take(2)
        val secondList = dataList.drop(2).take(3)
        val remainingList = dataList.drop(5)

        if (firstList.isNotEmpty()) {
            parentView.addView(createDynamicLinearLayout(firstList))
        }

        if (secondList.isNotEmpty()) {
            parentView.addView(createDynamicLinearLayout(secondList))
        }

        remainingList.chunked(3).forEach {
            parentView.addView(createDynamicLinearLayout(it))
        }
    }
}