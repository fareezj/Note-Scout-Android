package com.wolf.notescout.util

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

/**
 * Simple item decorator class for adding margins to recyclerview items
 *
 * For sanity's sake, either use the single dimension or the vertical/horizontal dimension initialization or it might behave differently from what you expected
 *
 * @property orientation The orientation of your recyclerview
 * @property topDimen The top margin for each item
 * @property bottomDimen The bottom margin for each item
 * @property leftDimen The left margin for each item
 * @property rightDimen The right margin for each item
 * @property disableFirstAndLast Disables adding margins to the first and last items to allow for the setting of different start/end paddings from the items in the recyclerview XML
 */

class RecyclerViewDecorator(
        orientation: Orientation,
        topDimen: Float,
        bottomDimen: Float,
        leftDimen: Float,
        rightDimen: Float,
        disableFirstAndLast: Boolean = false
) : RecyclerView.ItemDecoration() {

    private var topDimen = 0
    private var leftDimen = 0
    private var rightDimen = 0
    private var bottomDimen = 0
    private var orientation = Orientation.VERTICAL
    private var disableFirstAndLast = false

    enum class Orientation {
        VERTICAL,
        HORIZONTAL,
    }

    constructor(
            orientation: Orientation,
            verticalDimen: Float,
            horizontalDimen: Float,
            disableFirstAndLast: Boolean = false
    ) : this(
            orientation,
            verticalDimen,
            verticalDimen,
            horizontalDimen,
            horizontalDimen,
            disableFirstAndLast
    )

    constructor(
            orientation: Orientation,
            dimen: Float,
            disableFirstAndLast: Boolean = false
    ) : this(
            orientation,
            dimen,
            dimen,
            dimen,
            dimen,
            disableFirstAndLast
    )

    init {
        this.orientation = orientation
        this.disableFirstAndLast = disableFirstAndLast
        this.topDimen = topDimen.toInt()
        this.bottomDimen = bottomDimen.toInt()
        this.leftDimen = leftDimen.toInt()
        this.rightDimen = rightDimen.toInt()
    }

    override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
    ) {
        if (!disableFirstAndLast) {
            outRect.right = rightDimen
            outRect.bottom = bottomDimen

            if (orientation == Orientation.VERTICAL) {
                outRect.left = leftDimen
            } else {
                outRect.top = topDimen
            }

            /**
             * Only add top margin (if vertical) or left margin (if horizontal) to the first item to prevent additive spacing from occurring in-between items
             */
            if (parent.getChildLayoutPosition(view) == 0) {
                if (orientation == Orientation.VERTICAL) {
                    outRect.top = topDimen
                } else {
                    outRect.left = leftDimen
                }
            } else {
                if (orientation == Orientation.VERTICAL) {
                    outRect.top = 0
                } else {
                    outRect.left = 0
                }
            }
        } else {
            /**
             * Do not add bottom margin (if vertical) or right margin (if horizontal) to the last item to make it hug the bottom/right
             */
            if (orientation == Orientation.VERTICAL) {
                outRect.left = leftDimen
                outRect.right = rightDimen
                outRect.bottom = 0
            } else {
                outRect.top = topDimen
                outRect.bottom = bottomDimen
                outRect.right = 0
            }

            /**
             * Do not add top margin (if vertical) or left margin (if horizontal) to the first item to make it hug the top/left
             */
            if (parent.getChildLayoutPosition(view) == 0) {
                if (orientation == Orientation.VERTICAL) {
                    outRect.top = 0
                } else {
                    outRect.left = 0
                }
            } else {
                if (orientation == Orientation.VERTICAL) {
                    outRect.top = topDimen
                } else {
                    outRect.left = leftDimen
                }
            }
        }
    }
}