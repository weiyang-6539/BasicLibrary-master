package com.github.android.common.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;

import androidx.annotation.IntRange;

/**
 * Created by *** on 2019-09-14.
 */
public class DecimalInputFilter implements InputFilter {
    private int intBit = 1;
    private int decBit = 2;

    public DecimalInputFilter() {
    }

    public DecimalInputFilter(@IntRange(from = 1) int intBit, int decBit) {
        setIntDecBit(intBit, decBit);
    }

    public void setIntDecBit(@IntRange(from = 1) int intBit, int decBit) {
        this.intBit = intBit;
        this.decBit = decBit;

        createRegex();
    }

    private String regex;

    private void createRegex() {
        StringBuilder builder = new StringBuilder("[0-9]{1,")
                .append(intBit)
                .append("}");
        if (decBit != 0)
            builder.append("+(.[0-9]{1,")
                    .append(decBit)
                    .append("})?$");
        else
            builder.append("?$");

        regex = builder.toString();
    }

    @Override
    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
        //删除时不验证
        if (TextUtils.isEmpty(source))
            return "";

        //首位不能输入点
        if (TextUtils.equals(source, "."))
            return dstart == 0 ? "" : ".";

        String target = String.valueOf(dest.subSequence(0, dstart))
                + source + dest.subSequence(dend, dest.length());

        //首位为0时,不能再输入0
        if (target.startsWith("00"))
            return "";

        if (!target.matches(regex))
            return "";
        return null;
    }
}
