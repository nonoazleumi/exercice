package com.example.myapplication.screen.utils

import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.os.Handler
import android.view.View

fun View.fadeIn(duration: Long=350) {
    alpha = 0f
    animate().alpha(1f).setDuration(duration).start()
}

fun View.fadeOut(duration: Long=350) {
    alpha = 1f
    animate().alpha(0f).setDuration(duration).start()
}

fun animationTwoLinesSandwich(viewLineTop:View, viewLineBottom: View, duration: Long=650){
    val viewBottomY = viewLineBottom.y
    viewLineBottom.y = viewLineTop.y
    viewLineBottom.alpha=1f
    viewLineBottom.animateLocationYChange( viewLineTop.y, viewBottomY, duration)
}

fun View.animateLocationYChange(fromY:Float, to:Float, duration: Long=350) {
    val yChange = ObjectAnimator.ofPropertyValuesHolder(this, PropertyValuesHolder.ofFloat("y", fromY, to))
    yChange.duration = duration
    yChange.start()
}

fun animationTwoLinesSandwichClose(viewLineTop:View, viewLineBottom: View, lineCenterY:Float, duration: Long=650){
    viewLineTop.animateLocationYChange(viewLineTop.y, lineCenterY, duration)
    viewLineBottom.animateLocationYChange( viewLineBottom.y, lineCenterY, duration)
    Handler().postDelayed({
        viewLineTop.fadeOut()
        viewLineBottom.fadeOut()
    },650)
}