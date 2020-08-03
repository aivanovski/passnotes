package com.ivanovsky.passnotes.util

import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import kotlin.reflect.KClass
import kotlin.reflect.full.isSubclassOf

fun View.setVisible(isVisible: Boolean) {
    this.visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun <T : View> ViewGroup.filterViewTreeByClass(type: KClass<T>): List<T> {
    val filteredViews = mutableListOf<T>()

    for (view in children.toList()) {
        if (view::class == type || view::class.isSubclassOf(type)) {
            @Suppress("UNCHECKED_CAST")
            filteredViews.add(view as T)
        }
        if (view is ViewGroup) {
            filteredViews.addAll(view.filterViewTreeByClass(type))
        }
    }

    return filteredViews
}

fun ViewGroup.filterViewTree(predicate: (view: View) -> Boolean): List<View> {
    val filteredViews = mutableListOf<View>()

    for (view in children.toList()) {
        if (predicate.invoke(view)) {
            filteredViews.add(view)
        }
        if (view is ViewGroup) {
            filteredViews.addAll(view.filterViewTree(predicate))
        }
    }

    return filteredViews
}

