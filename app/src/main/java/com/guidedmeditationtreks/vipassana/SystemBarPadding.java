package com.guidedmeditationtreks.vipassana;

/**
 * Combines system-bar and display-cutout insets into root-view padding.
 * Layout margins (such as the 20dp title margin) stay inside this padding.
 */
public final class SystemBarPadding {

    public static final class Edges {
        public final int left;
        public final int top;
        public final int right;
        public final int bottom;

        public Edges(int left, int top, int right, int bottom) {
            this.left = left;
            this.top = top;
            this.right = right;
            this.bottom = bottom;
        }
    }

    private SystemBarPadding() {
    }

    public static Edges padding(Edges systemBars, Edges displayCutout) {
        return new Edges(
                largerNonNegative(systemBars.left, displayCutout.left),
                largerNonNegative(systemBars.top, displayCutout.top),
                largerNonNegative(systemBars.right, displayCutout.right),
                largerNonNegative(systemBars.bottom, displayCutout.bottom)
        );
    }

    private static int largerNonNegative(int first, int second) {
        return Math.max(0, Math.max(first, second));
    }
}
