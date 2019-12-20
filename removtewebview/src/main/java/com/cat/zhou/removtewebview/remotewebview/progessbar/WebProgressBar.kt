package com.cat.zhou.removtewebview.remotewebview.progessbar

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import com.cat.zhou.removtewebview.utils.WebUtils


class WebProgressBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), BaseProgressSpec {

    /**
     * 进度条颜色
     */
    private var mColor: Int = 0
    /**
     * 进度条的画笔
     */
    private var mPaint: Paint? = null
    /**
     * 进度条动画
     */
    private var mAnimator: Animator? = null
    /**
     * 控件的宽度
     */
    private var mTargetWidth = 0

    /**
     * 标志当前进度条的状态
     */
    private var TAG = 0

    private var currentProgress = 0f


    private var target = 0f

    private val mAnimatorUpdateListener = ValueAnimator.AnimatorUpdateListener { animation ->
        val t = animation.animatedValue as Float
        this@WebProgressBar.currentProgress = t
        this@WebProgressBar.invalidate()
    }

    private val mAnimatorListenerAdapter = object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator) {
            doEnd()
        }
    }

    init {

        init(context, attrs, defStyleAttr)

    }

    private fun init(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {

        mPaint = Paint()
        mColor = Color.parseColor("#c15d3e")

        mPaint!!.isAntiAlias = true
        mPaint!!.color = mColor
        mPaint!!.isDither = true
        mPaint!!.strokeCap = Paint.Cap.SQUARE

        mTargetWidth = context.resources.displayMetrics.widthPixels
        WEB_PROGRESS_DEFAULT_HEIGHT = WebUtils.dipToPx(context, 2.5f)

    }

    fun setColor(color: Int) {
        this.mColor = color
        mPaint!!.color = color
    }

    fun setColor(color: String) {
        this.setColor(Color.parseColor(color))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val wMode = View.MeasureSpec.getMode(widthMeasureSpec)
        var w = View.MeasureSpec.getSize(widthMeasureSpec)

        val hMode = View.MeasureSpec.getMode(heightMeasureSpec)
        var h = View.MeasureSpec.getSize(heightMeasureSpec)

        if (wMode == View.MeasureSpec.AT_MOST) {
            w =
                if (w <= context.resources.displayMetrics.widthPixels) w else context.resources.displayMetrics.widthPixels
        }
        if (hMode == View.MeasureSpec.AT_MOST) {
            h = WEB_PROGRESS_DEFAULT_HEIGHT
        }
        this.setMeasuredDimension(w, h)

    }

    override fun onDraw(canvas: Canvas) {

    }

    override fun dispatchDraw(canvas: Canvas) {
        canvas.drawRect(
            0f,
            0f,
            currentProgress / 100 * java.lang.Float.valueOf(this.width.toFloat()),
            this.height.toFloat(),
            mPaint!!
        )
    }

    override fun show() {

        if (visibility == View.GONE) {
            this.visibility = View.VISIBLE
            currentProgress = 0f
            startAnim(false)
        }

    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        this.mTargetWidth = measuredWidth
        val screenWidth = context.resources.displayMetrics.widthPixels
        if (mTargetWidth >= screenWidth) {
            CURRENT_MAX_DECELERATE_SPEED_DURATION = MAX_DECELERATE_SPEED_DURATION
            CURRENT_MAX_UNIFORM_SPEED_DURATION = MAX_UNIFORM_SPEED_DURATION
        } else {
            //取比值
            val rate = this.mTargetWidth / java.lang.Float.valueOf(screenWidth.toFloat())
            CURRENT_MAX_UNIFORM_SPEED_DURATION = (MAX_UNIFORM_SPEED_DURATION * rate).toInt()
            CURRENT_MAX_DECELERATE_SPEED_DURATION = (MAX_DECELERATE_SPEED_DURATION * rate).toInt()

        }
    }

    fun setProgress(progress: Float) {
        if (visibility == View.GONE) {
            visibility = View.VISIBLE
        }
        if (progress < 95f)
            return
        if (TAG != FINISH) {
            startAnim(true)
        }
    }

    override fun hide() {
        TAG = FINISH
    }


    private fun startAnim(isFinished: Boolean) {


        val v = (if (isFinished) 100 else 95).toFloat()


        if (mAnimator != null && mAnimator!!.isStarted) {
            mAnimator!!.cancel()
        }
        currentProgress = if (currentProgress == 0f) 0.00000001f else currentProgress

        if (!isFinished) {
            val mAnimator = ValueAnimator.ofFloat(currentProgress, v)
            val residue = 1f - currentProgress / 100 - 0.05f
            mAnimator.interpolator = LinearInterpolator()
            mAnimator.duration = (residue * CURRENT_MAX_UNIFORM_SPEED_DURATION).toLong()
            mAnimator.addUpdateListener(mAnimatorUpdateListener)
            mAnimator.start()
            this.mAnimator = mAnimator
        } else {

            var segment95Animator: ValueAnimator? = null
            if (currentProgress < 95f) {
                segment95Animator = ValueAnimator.ofFloat(currentProgress, 95.0f)
                val residue = 1f - currentProgress / 100f - 0.05f
                segment95Animator!!.interpolator = LinearInterpolator()
                segment95Animator.duration =
                    (residue * CURRENT_MAX_DECELERATE_SPEED_DURATION).toLong()
                segment95Animator.interpolator = DecelerateInterpolator()
                segment95Animator.addUpdateListener(mAnimatorUpdateListener)
            }


            val mObjectAnimator = ObjectAnimator.ofFloat(this, "alpha", 1f, 0f)
            mObjectAnimator.duration = DO_END_ANIMATION_DURATION.toLong()
            val mValueAnimatorEnd = ValueAnimator.ofFloat(95f, 100f)
            mValueAnimatorEnd.duration = DO_END_ANIMATION_DURATION.toLong()
            mValueAnimatorEnd.addUpdateListener(mAnimatorUpdateListener)

            var mAnimatorSet = AnimatorSet()
            mAnimatorSet.playTogether(mObjectAnimator, mValueAnimatorEnd)

            if (segment95Animator != null) {
                val mAnimatorSet1 = AnimatorSet()
                mAnimatorSet1.play(mAnimatorSet).after(segment95Animator)
                mAnimatorSet = mAnimatorSet1
            }
            mAnimatorSet.addListener(mAnimatorListenerAdapter)
            mAnimatorSet.start()
            mAnimator = mAnimatorSet
        }

        TAG = STARTED
        target = v

    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()

        /**
         * animator cause leak , if not cancel;
         */
        if (mAnimator != null && mAnimator!!.isStarted) {
            mAnimator!!.cancel()
            mAnimator = null
        }
    }

    private fun doEnd() {
        if (TAG == FINISH && currentProgress == 100f) {
            visibility = View.GONE
            currentProgress = 0f
            this.alpha = 1f
        }
        TAG = UN_START
    }

    override fun reset() {
        currentProgress = 0f
        if (mAnimator != null && mAnimator!!.isStarted)
            mAnimator!!.cancel()
    }

    override fun setProgress(newProgress: Int) {
        setProgress(java.lang.Float.valueOf(newProgress.toFloat()))

    }

    companion object {

        /**
         * 默认匀速动画最大的时长
         */
        val MAX_UNIFORM_SPEED_DURATION = 5 * 1000
        /**
         * 默认加速后减速动画最大时长
         */
        val MAX_DECELERATE_SPEED_DURATION = 600
        /**
         * 结束动画时长 ， Fade out 。
         */
        val DO_END_ANIMATION_DURATION = 300

        /**
         * 当前匀速动画最大的时长
         */
        private var CURRENT_MAX_UNIFORM_SPEED_DURATION = MAX_UNIFORM_SPEED_DURATION
        /**
         * 当前加速后减速动画最大时长
         */
        private var CURRENT_MAX_DECELERATE_SPEED_DURATION = MAX_DECELERATE_SPEED_DURATION
        val UN_START = 0
        val STARTED = 1
        val FINISH = 2

        /**
         * 默认的高度
         */
        var WEB_PROGRESS_DEFAULT_HEIGHT = 3
    }
}
