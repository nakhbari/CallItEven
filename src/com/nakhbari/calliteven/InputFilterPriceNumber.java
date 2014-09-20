package com.nakhbari.calliteven;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterPriceNumber implements InputFilter {

	private String testRegex;

	public InputFilterPriceNumber(String decimalSep) {
		testRegex = "(^\\d{0,7}$)|(^\\d{0,7}\\.\\d{0,2}$)";
	}

	@Override
	public CharSequence filter(CharSequence source, int start, int end,
			Spanned dest, int dstart, int dend) {
		String s = source.toString();
		String d = dest.toString();

		String r = d.substring(0, dstart) + s.substring(start, end)
				+ d.substring(dend);

		if (r.matches(testRegex)) {
			return null;
		} else {
			return "";
		}
	}
}