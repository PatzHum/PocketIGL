package me.phum.pocketigl.components

import android.animation.Animator
import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewAnimationUtils
import android.widget.FrameLayout
import kotlinx.android.synthetic.main.hotspot_action.view.*
import kotlinx.android.synthetic.main.hotspot_view.view.*
import me.phum.pocketigl.R
import me.phum.pocketigl.components.HotspotButton.Companion.HotspotAction.*

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
    }

    var availableActions: List<HotspotAction> = listOf()

    init {
        View.inflate(context, R.layout.hotspot_view, this)
        expanded_view.visibility = View.GONE
        collapsed_button.setOnClickListener {
            expand()
        }
        expanded_view.setOnClickListener {
            collapse()
        }
    }
    fun expand() {
        actions.removeAllViews()
        if (expanded_view.visibility == View.GONE) {
            expanded_view.visibility = View.VISIBLE
            val cx = expanded_view.width / 2.0
            val cy = cx
            val finalRadius = Math.hypot(cx, cy).toFloat()
            val animator = ViewAnimationUtils.createCircularReveal(expanded_view, cx.toInt(), cy.toInt(), 0f, finalRadius)
            availableActions.forEach {
                val actionView = inflate(context, R.layout.hotspot_action, null)
                actionView.apply {
                    action_name.text = it.asString()
                }
                actions.addView(actionView)
            }
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