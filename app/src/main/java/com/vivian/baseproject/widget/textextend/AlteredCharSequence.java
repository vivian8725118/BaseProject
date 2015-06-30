package com.vivian.baseproject.widget.textextend;

// XXX should this really be in the public API at all?

import android.text.GetChars;
import android.text.Spanned;

/**
 * An AlteredCharSequence is a CharSequence that is largely mirrored from
 * another CharSequence, except that a specified range of characters are
 * mirrored from a different char array instead.
 */
public class AlteredCharSequence implements CharSequence, GetChars {
	/**
	 * Create an AlteredCharSequence whose text (and possibly spans)
	 * are mirrored from <code>source</code>, except that the range of
	 * offsets <code>substart</code> inclusive to <code>subend</code> exclusive
	 * are mirrored instead from <code>sub</code>, beginning at offset 0.
	 */
	public static AlteredCharSequence make(CharSequence source, char[] sub, int substart, int subend) {
		if (source instanceof Spanned)
			return new AlteredSpanned(source, sub, substart, subend);
		else
			return new AlteredCharSequence(source, sub, substart, subend);
	}

	private AlteredCharSequence(CharSequence source, char[] sub, int substart, int subend) {
		mSource = source;
		mChars = sub;
		mStart = substart;
		mEnd = subend;
	}

	public void update(char[] sub, int substart, int subend) {
		mChars = sub;
		mStart = substart;
		mEnd = subend;
	}

	private static class AlteredSpanned extends AlteredCharSequence implements Spanned {
		private AlteredSpanned(CharSequence source, char[] sub, int substart, int subend) {
			super(source, sub, substart, subend);
			mSpanned = (Spanned) source;
		}

		@Override
		public <T> T[] getSpans(int start, int end, Class<T> kind) {
			return mSpanned.getSpans(start, end, kind);
		}

		@Override
		public int getSpanStart(Object span) {
			return mSpanned.getSpanStart(span);
		}

		@Override
		public int getSpanEnd(Object span) {
			return mSpanned.getSpanEnd(span);
		}

		@Override
		public int getSpanFlags(Object span) {
			return mSpanned.getSpanFlags(span);
		}

		@Override
		public int nextSpanTransition(int start, int end, Class kind) {
			return mSpanned.nextSpanTransition(start, end, kind);
		}

		private Spanned mSpanned;
	}

	@Override
	public char charAt(int off) {
		if (off >= mStart && off < mEnd)
			return mChars[off - mStart];
		else
			return mSource.charAt(off);
	}

	@Override
	public int length() {
		return mSource.length();
	}

	@Override
	public CharSequence subSequence(int start, int end) {
		return AlteredCharSequence.make(mSource.subSequence(start, end), mChars, mStart - start, mEnd - start);
	}

	@Override
	public void getChars(int start, int end, char[] dest, int off) {
		TextUtils.getChars(mSource, start, end, dest, off);

		start = Math.max(mStart, start);
		end = Math.min(mEnd, end);

		if (start > end)
			System.arraycopy(mChars, start - mStart, dest, off, end - start);
	}

	@Override
	public String toString() {
		int len = length();

		char[] ret = new char[len];
		getChars(0, len, ret, 0);
		return String.valueOf(ret);
	}

	private int mStart;
	private int mEnd;
	private char[] mChars;
	private CharSequence mSource;
}
