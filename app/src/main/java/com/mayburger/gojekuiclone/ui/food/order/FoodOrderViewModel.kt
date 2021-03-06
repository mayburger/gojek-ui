package com.mayburger.gojekuiclone.ui.food.order

import android.animation.AnimatorSet
import android.graphics.drawable.AnimatedVectorDrawable
import android.view.animation.DecelerateInterpolator
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.core.animation.doOnEnd
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.hilt.lifecycle.ViewModelInject
import androidx.vectordrawable.graphics.drawable.AnimatedVectorDrawableCompat
import com.mayburger.gojekuiclone.R
import com.mayburger.gojekuiclone.data.DataManager
import com.mayburger.gojekuiclone.models.events.BackEvent
import com.mayburger.gojekuiclone.ui.base.BaseViewModel
import com.mayburger.gojekuiclone.util.AnimationExt.scale
import com.mayburger.gojekuiclone.util.AnimationExt.scaleY
import com.mayburger.gojekuiclone.util.AnimationExt.translationY
import com.mayburger.gojekuiclone.util.ext.ViewUtils.animToY
import com.mayburger.gojekuiclone.util.ext.ViewUtils.dpToPx
import com.mayburger.gojekuiclone.util.ext.ViewUtils.fadeHide
import com.mayburger.gojekuiclone.util.ext.ViewUtils.fadeShow
import com.mayburger.gojekuiclone.util.ext.ViewUtils.flipX
import com.mayburger.gojekuiclone.util.rx.SchedulerProvider
import kotlinx.android.synthetic.main.fragment_food_order.*


class FoodOrderViewModel @ViewModelInject constructor(
        dataManager: DataManager,
        schedulerProvider: SchedulerProvider
) :
        BaseViewModel<FoodOrderNavigator>(dataManager, schedulerProvider) {
    override fun onEvent(obj: Any) {
        when (obj) {
            is BackEvent -> {
                if (obj.obj is FoodOrderFragment) {
                    navigator?.onBackPressed(obj.obj)
                }
            }
        }
    }

    companion object {

        fun FoodOrderFragment.playOrderAnimation() {
            val vaClass = Class.forName("android.animation.ValueAnimator")
            val method = vaClass.getMethod("setDurationScale",Float::class.java)
            method.invoke(null, 0.5f)

            isAnimating = true
            motionLayout.addTransitionListener(object : MotionLayout.TransitionListener {
                override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {

                }

                override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {
                }

                override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                    if (p3 > 0.6) {
                        image.setImageResource(R.drawable.ic_done)
                        val drawable = image.drawable
                        if (drawable is AnimatedVectorDrawableCompat) {
                            drawable.start()
                        } else if (drawable is AnimatedVectorDrawable) {
                            drawable.start()
                        }

                        text.animToY(20f, duration = 500)
                        text.fadeShow(duration = 500, onEnd = {
                            delay(1000) {
                                text.animToY(0f, duration = 500)
                                text.fadeHide(duration = 500)
                            }
                        })

                        AnimatorSet().apply {
                            // Bounce Down
                            play(card.translationY(250f, duration = 1000, interpolator = PathInterpolatorCompat.create(.97f,-0.02f,.58f,1.04f)))
                            play(card.scaleY(0.2f, duration = 300)).after(1000)

                            // Bounce Up
                            play(card.translationY(0f, duration=  1200, interpolator = PathInterpolatorCompat.create(.1f, .98f, .99f, 1f))).after(1000)
                            play(card.scaleY(1f, duration = 500)).after(1200)

                            // Part two
                            play(card.scale(0.6f, duration = 1300)).after(2400)
                            play(card.translationY(270f, duration= 1300,interpolator = PathInterpolatorCompat.create(.97f,-0.02f,.58f,1.04f))).after(2400)

                            // Part three
                            play(card.scaleY(0.4f, duration = 300)).after(3500)
                            play(card.translationY(295f, duration = 300)).after(3500)

                            play(card.scaleY(0.6f, duration = 600,interpolator = DecelerateInterpolator())).after(3800)
                            play(card.translationY(270f, duration = 600, interpolator = DecelerateInterpolator())).after(3800)

                            start()
                        }

                        card.flipX(duration = 700, after = 1000, onFlip = {
                            card.cardElevation = 2f
                            image.setPadding(dpToPx(16), dpToPx(16), dpToPx(16), dpToPx(16))
                            card.setCardBackgroundColor(requireActivity().resources.getColor(R.color.red_200))
                            image.setImageResource(R.drawable.ic_gofood)
                        },endValue = 0.8f)

                        background.fadeHide(duration = 1200, after = 2500, onEnd = {
                            isAnimating = false
                            root.removeView(fireworkBlue)
                            root.removeView(fireworkRed)
                            root.removeView(fireworkYellow)
                            root.removeView(fireworkGreen)
                        })

                        marker_foot.animToY(245f, duration = 0)
                        marker_foot.fadeShow(after = 4000, duration = 300)

                    }
                }

                override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                    fireworkRed.fadeShow(duration = 200)
                    fireworkRed.setColor(resources.getColor(R.color.red_200))
                    fireworkRed.start()
                    fireworkBlue.fadeShow(duration = 200)
                    fireworkBlue.setColor(resources.getColor(R.color.dark_blue_700))
                    fireworkBlue.start()
                    fireworkYellow.fadeShow(duration = 200)
                    fireworkYellow.setColor(resources.getColor(R.color.yellow_200))
                    fireworkYellow.start()
                    fireworkGreen.fadeShow(duration = 200)
                    fireworkGreen.setColor(resources.getColor(R.color.green_200))
                    fireworkGreen.start()
                }
            })
        }

    }
}
