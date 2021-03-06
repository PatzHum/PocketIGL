package me.phum.pocketigl.components

import android.animation.Animator
import android.content.Context
import android.support.annotation.DrawableRes
import android.support.annotation.IdRes
import android.support.v4.content.ContextCompat
import android.util.AttributeSet
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.hotspot_action.view.*
import kotlinx.android.synthetic.main.hotspot_view.view.*
import me.phum.pocketigl.R
import me.phum.pocketigl.components.HotspotButton.Companion.HotspotAction.*
import android.util.TypedValue
import kotlin.math.exp


/**
 * © PocketIGL 2018
 */
class HotspotButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    companion object {
        enum class HotspotAction {
            SMOKE,
            GRENADE,
            MOLLY,
            FLASH,
            ATTACK,
            DEFEND,
            PLANT
        }

        val HOTSPOTS_DEFAULT = listOf(SMOKE, GRENADE, MOLLY, FLASH, ATTACK, DEFEND)
        val HOTSPOTS_PLANT_SITE = listOf(PLANT, SMOKE, GRENADE, MOLLY, FLASH, ATTACK, DEFEND)
        @JvmStatic
        fun HotspotAction.asString() : String {
            return when (this) {
                HotspotButton.Companion.HotspotAction.SMOKE -> "Smoke"
                HotspotButton.Companion.HotspotAction.GRENADE -> "Grenade"
                HotspotButton.Companion.HotspotAction.MOLLY -> "Molotov"
                HotspotButton.Companion.HotspotAction.FLASH -> "Flash"
                HotspotButton.Companion.HotspotAction.ATTACK -> "Push"
                HotspotButton.Companion.HotspotAction.DEFEND -> "Hold"
                HotspotButton.Companion.HotspotAction.PLANT -> "Plant"
            }
        }

        @JvmStatic
        @DrawableRes
        fun HotspotAction.getDrawable() : Int {
            return when (this) {

                HotspotButton.Companion.HotspotAction.SMOKE -> R.drawable.baseline_cloud_white_18dp
                HotspotButton.Companion.HotspotAction.GRENADE -> R.drawable.nade
                HotspotButton.Companion.HotspotAction.MOLLY -> R.drawable.molly
                HotspotButton.Companion.HotspotAction.FLASH -> R.drawable.flash
                HotspotButton.Companion.HotspotAction.ATTACK -> R.drawable.push
                HotspotButton.Companion.HotspotAction.DEFEND -> R.drawable.hold
                HotspotButton.Companion.HotspotAction.PLANT -> R.drawable.plant
            }
        }
    }

    var availableActions: List<HotspotAction> = listOf()
        set(value) {
            field = value
            actions.removeAllViews()
            availableActions.forEach {
                val actionView = inflate(context, R.layout.hotspot_action, null)
                actionView.apply {
                    action_name.text = it.asString()
                    val bg = ContextCompat.getDrawable(context, it.getDrawable())
                    action_image.setImageDrawable(bg)
                    setOnClickListener {
                        this@HotspotButton.collapsed_button.background = bg
                        collapse()
                    }
                }
                actions.addView(actionView)
            }
        }

    init {
        View.inflate(context, R.layout.hotspot_view, this)
        expanded_view.visibility = View.GONE
        collapsed_button.setOnClickListener {
            expand()
        }
        setOnClickListener {
            expand()
        }
        expanded_view.setOnClickListener {
            collapse()
        }
        minimumWidth = dp(30)
        minimumHeight = dp (30)
    }

    fun dp (v : Int) : Int {
        val r = resources
        val px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, v.toFloat(), r.displayMetrics)
        return px.toInt()
    }
    fun expand() {
        if (expanded_view.visibility == View.GONE) {
            expanded_view.visibility = View.VISIBLE
            val cx = expanded_view.width / 2.0
            val cy = cx
            val finalRadius = Math.hypot(cx, cy).toFloat()
            val animator = ViewAnimationUtils.createCircularReveal(expanded_view, cx.toInt(), cy.toInt(), 0f, finalRadius)
            animator.duration = 100L
            animator.start()
            collapsed_button.visibility = View.GONE
        }
    }
    fun collapse() {
        if (expanded_view.visibility != View.GONE) {
            val cx = expanded_view.width / 2.0
            val cy = expanded_view.height / 2.0
            val startRadius = Math.hypot(cx, cy).toFloat()
            val animator = ViewAnimationUtils.createCircularReveal(expanded_view, cx.toInt(), cy.toInt(), startRadius, 0f)
            animator.duration = 100L
            animator.addListener(object : Animator.AnimatorListener {
                override fun onAnimationRepeat(p0: Animator?) {
                }

                override fun onAnimationEnd(p0: Animator?) {
                    expanded_view.visibility = View.GONE
                    collapsed_button.visibility = View.VISIBLE
                }

                override fun onAnimationCancel(p0: Animator?) {
                }

                override fun onAnimationStart(p0: Animator?) {
                }

            })
            animator.start()
        }
    }
}