package com.vivian.baseproject.widget;

import android.content.Context;
import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

/**
 * 无下划线超链接，使用textColorLink、textColorHighlight分别修改超链接前景色和按下时的颜色
 *
 * @author vivian
 */
public class NoLineClickSpan extends ClickableSpan {
    String text;
    private Context mContext;

    public NoLineClickSpan(String text, Context context) {
        super();
        this.text = text;
        this.mContext = context;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        // ds.setColor(ds.linkColor);
//        ds.setColor(mContext.getResources().getColor(R.color.time_gray));//
        ds.setUnderlineText(false); // 去掉下划线
    }

    @Override
    public void onClick(View widget) {
        // 点击超链接时调用


    }

}
