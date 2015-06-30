package com.vivian.baseproject.widget.popwindow;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;


public class SelectPicPopupWindow extends PopupWindow {
	private LinearLayout btn_take_photo, btn_pick_photo, btn_cancel;
	private View mMenuView;

	public SelectPicPopupWindow(Context context, OnClickListener itemsOnClick) {
		super(context);
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//		mMenuView = inflater.inflate(R.layout.bt_pop_window_take_photo, null);
//		btn_take_photo = (LinearLayout) mMenuView.findViewById(R.id.take_photo_layout);
//		btn_pick_photo = (LinearLayout) mMenuView.findViewById(R.id.choose_from_gallery_layout);
//		btn_cancel = (LinearLayout) mMenuView.findViewById(R.id.cancel_layout);
//		// 取消按钮
//		btn_cancel.setOnClickListener(new OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				// 销毁弹出框
//				dismiss();
//			}
//		});
		// 设置按钮监听
		btn_pick_photo.setOnClickListener(itemsOnClick);
		btn_take_photo.setOnClickListener(itemsOnClick);
		// 设置SelectPicPopupWindow的View
		this.setContentView(mMenuView);
		// 设置SelectPicPopupWindow弹出窗体的宽
		this.setWidth(LayoutParams.FILL_PARENT);
		// 设置SelectPicPopupWindow弹出窗体的高
		this.setHeight(LayoutParams.WRAP_CONTENT);
		// 设置SelectPicPopupWindow弹出窗体可点击
		this.setFocusable(true);
		// 设置SelectPicPopupWindow弹出窗体动画效果
//		this.setAnimationStyle(R.style.mystyle);
		// 实例化一个ColorDrawable颜色为半透明
		ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
		// 设置SelectPicPopupWindow弹出窗体的背景
		this.setBackgroundDrawable(dw);
		// mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
		mMenuView.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
//
//				int height = mMenuView.findViewById(R.id.pop_layout).getTop();
//				int y = (int) event.getY();
//				if (event.getAction() == MotionEvent.ACTION_UP) {
//					if (y < height) {
//						dismiss();
//					}
//				}
				return true;
			}
		});
	}

	public LinearLayout getBtn_take_photo() {
		return btn_take_photo;
	}

	public void setBtn_take_photo(LinearLayout btn_take_photo) {
		this.btn_take_photo = btn_take_photo;
	}

	public LinearLayout getBtn_pick_photo() {
		return btn_pick_photo;
	}

	public void setBtn_pick_photo(LinearLayout btn_pick_photo) {
		this.btn_pick_photo = btn_pick_photo;
	}

	public LinearLayout getBtn_cancel() {
		return btn_cancel;
	}

	public void setBtn_cancel(LinearLayout btn_cancel) {
		this.btn_cancel = btn_cancel;
	}

}
