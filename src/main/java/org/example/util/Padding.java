package org.example.util;

public class Padding {

    public static int displayWidth(String s) {

        if (s == null) {
            return 0;
        }

        int width = 0;
        for (
                int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c <= 0x007F) {              // ASCII
                width += 1;
            } else if (c >= 0xAC00 && c <= 0xD7A3) { // 한글
                width += 2;
            } else {
                width += 1; // 기타 문자
            }
        }
        return width;
    }

    public static String padRight(String s, int totalWidth) {
        int currentWidth = displayWidth(s);
        int pad = totalWidth - currentWidth;

        if (pad <= 0) {
            return s;
        }
        return s + " ".repeat(pad);
    }

    public static String padLeft(String s, int totalWidth) {
        int pad = totalWidth - displayWidth(s);
        return " ".repeat(Math.max(0, pad)) + s;
    }
}
