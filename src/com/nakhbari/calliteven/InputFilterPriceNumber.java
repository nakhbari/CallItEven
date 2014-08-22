package com.nakhbari.calliteven;

import android.text.InputFilter;
import android.text.Spanned;

public class InputFilterPriceNumber implements InputFilter{

	private long min, max;
	
	public InputFilterPriceNumber(long min, long max){
		this.min = min;
		this.max = max;
	}
	
    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {   
        try {
            long input = Long.parseLong(dest.toString() + source.toString());
            if (isInRange(min, max, input))
                return null;
        } catch (NumberFormatException nfe) { }     
        return "";
    }

    private boolean isInRange(long a, long b, long c) {
        return b > a ? c >= a && c <= b : c >= b && c <= a;
    }
}