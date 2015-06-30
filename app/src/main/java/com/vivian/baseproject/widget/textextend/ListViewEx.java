package com.vivian.baseproject.widget.textextend;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ListView;

public class ListViewEx extends ListView{

	public ListViewEx(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public ListViewEx(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	/**
	 * 设置不滚动
	 */
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
	
    @Override  
    public boolean dispatchTouchEvent(MotionEvent ev) {  
        if(ev.getAction() == MotionEvent.ACTION_MOVE){     
            return true;   
        }   
        return super.dispatchTouchEvent(ev);  
    }  
}
