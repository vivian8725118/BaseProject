package com.vivian.baseproject.widget.textextend;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.MovementMethod;
import android.text.style.ClickableSpan;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by caifangmao on 15/4/7.
 */
public class SHTextView extends View {

	private CharSequence mText;
	private Spannable mSpannable;

	private int mTextColor;
	private float mTextSize;

	private StaticLayout mLayout;

	private MovementMethod mMovement;

	private TextPaint mTextPaint;

	public SHTextView(Context context) {
		this(context, null);
	}

	public SHTextView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SHTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mTextSize = 15;

		mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

		TextView tempTv = new TextView(context, attrs);

		mTextSize = tempTv.getTextSize();
		mTextColor = tempTv.getTextColors().getDefaultColor();

		tempTv = null;

		mTextPaint.setTextSize(mTextSize);
		mTextPaint.setColor(mTextColor);

		if (mText == null)
			mText = "";
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);

		int width;
		int height;

		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else {
			width = (int) FloatMath.ceil(Layout.getDesiredWidth(mText, mTextPaint));

			if (widthMode == MeasureSpec.AT_MOST) {
				width = Math.min(width, widthSize);
			}

			width += getPaddingLeft() + getPaddingRight();
		}

		int want = width - getPaddingLeft() - getPaddingRight();

		if (mLayout == null) {
			createLayout(want);
		} else {
			boolean layoutChanged = mLayout.getWidth() != want;
			boolean widthChanged = want > mLayout.getWidth();

			if (layoutChanged) {
				if (widthChanged) {
					mLayout.increaseWidthTo(want);
				} else {
					createLayout(want);
				}
			}
		}

		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			height = mLayout.getHeight();

			if (heightMode == MeasureSpec.AT_MOST) {
				height = Math.min(height, heightSize);
			}
		}

		setMeasuredDimension(width, height);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();
		if (action == MotionEvent.ACTION_UP || action == MotionEvent.ACTION_DOWN && mText instanceof Spanned) {
			int x = (int) event.getX();
			int y = (int) event.getY();

			x -= this.getPaddingLeft();
			y -= this.getPaddingTop();

			x += this.getScrollX();
			y += this.getScrollY();

			int line = mLayout.getLineForVertical(y);
			int off = mLayout.getOffsetForHorizontal(line, x);

			ClickableSpan[] link = ((Spanned) mText).getSpans(off, off, ClickableSpan.class);

			if (link.length != 0) {
				if (action == MotionEvent.ACTION_UP) {
					link[0].onClick(this);
				} else if (action == MotionEvent.ACTION_DOWN) {
					if (mText instanceof Spannable) {
						Spannable buffer = (Spannable) mText;
						Selection.setSelection(buffer, buffer.getSpanStart(link[0]), buffer.getSpanEnd(link[0]));
					}
				}

				return true;
			} else {
				if (mText instanceof Spannable) {
					Spannable buffer = (Spannable) mText;
					Selection.removeSelection(buffer);
				}
			}
		}

		return super.onTouchEvent(event);
	}

	public void setText(CharSequence text) {
		this.mText = text == null ? "" : text;

		if (mLayout != null) {
			checkRelayout();
		}
	}

	public void setText(int strId) {
		String text = getContext().getResources().getString(strId);
		setText(text);
	}

	private void createLayout(int want) {
		mLayout = new StaticLayout(mText, 0, mText.length(), mTextPaint, want, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
	}

	private void checkRelayout() {
		if (mText != null) {
			if (getLayoutParams().width != ViewGroup.LayoutParams.WRAP_CONTENT) {

				int oldHeight = mLayout.getHeight();
				int want = mLayout.getWidth();

				createLayout(want);

				if (getLayoutParams().height != ViewGroup.LayoutParams.WRAP_CONTENT && getLayoutParams().height != ViewGroup.LayoutParams.MATCH_PARENT) {
					invalidate();
					return;
				}

				if (getLayoutParams().height == oldHeight) {
					invalidate();
					return;
				}

				requestLayout();
				invalidate();
			} else {
				mLayout = null;
				requestLayout();
				invalidate();
			}
		}
	}

	public void setTextColor(int color) {
		this.mTextColor = color;

		mTextPaint.setColor(this.mTextColor);

		if (mSpannable != null) {
			setText(mText);
		}

		invalidate();
	}

	public void setTextSize(int size) {
		setTextSize(size, TypedValue.COMPLEX_UNIT_SP);
	}

	public void setTextSize(int size, int unit) {
		mTextSize = (int) TypedValue.applyDimension(unit, size, getContext().getResources().getDisplayMetrics());

		mTextPaint.setTextSize(mTextSize);

		if (mSpannable != null) {
			setText(mText);
		}

		requestLayout();
		invalidate();
	}

	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		canvas.save();
		canvas.translate(getPaddingLeft(), getPaddingTop());
		mLayout.draw(canvas);
		canvas.restore();
	}
}
