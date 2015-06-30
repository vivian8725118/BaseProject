package com.vivian.baseproject.utils;

import android.app.Dialog;

public class DialogUtil {
	private static Dialog dialog;

	public static Dialog getDialog() {
		return dialog;
	}

	public void show() {
		if (dialog != null)
			dialog.show();
	}

	public void dismiss() {
		if (dialog != null && dialog.isShowing()) {
			dialog.dismiss();
		}
	}

}
