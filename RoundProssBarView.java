package com.huichang.qrcode.tools;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.OvershootInterpolator;

import androidx.annotation.Nullable;

import com.huichang.qrcode.R;

public class RoundProssBarView extends View {

	//圆环颜色
	private int ringColor = 0xfffff000;

	//圆环进度颜色
	private int ringProgressColor = 0xffff0000;

	//圆环宽度
	private int ringWidth = 10;

	//字体大小
	private int textSize = 40;

	//字体颜色
	private int textColor = 0xff0000ff;

	//当前进度
	private int currentProgress = 0;

	//最大进度
	private int maxProgress = 100;

	//得到控件的宽度
	private int width;

	//画笔对象
	private Paint paint;


	public RoundProssBarView(Context context) {
		super(context);

	}

	public RoundProssBarView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
		//得到资源数组
		TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RingProgressView);
		//圆环
		ringColor = typedArray.getColor(R.styleable.RingProgressView_ringColor, ringColor);
		//圆环进度
		ringProgressColor = typedArray.getColor(R.styleable.RingProgressView_ringProgressColor, ringProgressColor);
		//圆环宽度
		ringWidth = (int) typedArray.getDimension(R.styleable.RingProgressView_ringWidth, ringWidth);
		//字体大小
		textSize = (int) typedArray.getDimension(R.styleable.RingProgressView_textSize, textSize);
		//字体颜色
		textColor = typedArray.getColor(R.styleable.RingProgressView_textColor, textColor);
		//当前进度
		currentProgress = typedArray.getInt(R.styleable.RingProgressView_currentProgress, currentProgress);
		//最大进度
		maxProgress = typedArray.getColor(R.styleable.RingProgressView_maxProgress, maxProgress);

		typedArray.recycle();


		paint = new Paint();
		//抗锯齿
		paint.setAntiAlias(true);
	}

	//测量
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		width = getMeasuredWidth();
	}


	//绘制

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		//1、计算圆心左边及半径
		float centerX = width / 2;
		float centerY = width / 2;
		float radius = width / 2 - ringWidth / 2;

		//2、画圆环
		paint.setStyle(Paint.Style.STROKE);
		paint.setStrokeWidth(ringWidth);
		paint.setColor(ringColor);
		canvas.drawCircle(centerX, centerY, radius, paint);

		//画圆弧
		RectF rectF = new RectF(ringWidth / 2, ringWidth / 2, width - ringWidth / 2, width - ringWidth / 2);
		paint.setColor(ringProgressColor);
		paint.setStrokeCap(Paint.Cap.ROUND);
		canvas.drawArc(rectF, 270, currentProgress * 360 / maxProgress, false, paint);

		//画文字
		String text = currentProgress * 100 / maxProgress + "%";
		paint.setColor(textColor);
		paint.setTextSize(textSize);
		paint.setStyle(Paint.Style.FILL);
		//重新设置宽度为0
		paint.setStrokeWidth(0);
		//得到指定文本边界的指定大小
		Rect bounds = new Rect();
		paint.getTextBounds(text, 0, text.length(), bounds);
		canvas.drawText(text, width / 2 - bounds.width() / 2, width / 2 + bounds.height() / 2, paint);


	}

	/* 布局 */

	@Override
	protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}

	public int getCurrentProgress() {
		return currentProgress;
	}


	public void setCurrentProgress(int currentProgress) {
		this.currentProgress = currentProgress;
	}

	public int getMaxProgress() {
		return maxProgress;
	}

	public void setMaxProgress(int maxProgress) {
		this.maxProgress = maxProgress;
		invalidate();
	}

	public void setProgressAnimotion(int mprogress, long animotionTime) {
		if (animotionTime <= 0) {
			setMaxProgress(mprogress);
		} else {
			ValueAnimator animator = ValueAnimator.ofInt(currentProgress, mprogress);
			animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
				@Override
				public void onAnimationUpdate(ValueAnimator valueAnimator) {
					currentProgress = (int) animator.getAnimatedValue();
					invalidate();
				}
			});
			animator.setInterpolator(new DecelerateInterpolator());
			animator.setDuration(animotionTime);
			animator.start();
		}
	}
}
