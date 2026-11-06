package com.guidedmeditationtreks.vipassana;

import android.view.View;
import android.view.Window;

import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;

public final class EdgeToEdge {

    private EdgeToEdge() {
    }

    public static void enable(Window window) {
        WindowCompat.setDecorFitsSystemWindows(window, false);
    }

    public static void applySystemBarPadding(View root) {
        ViewCompat.setOnApplyWindowInsetsListener(root, (view, windowInsets) -> {
            Insets systemBars = windowInsets.getInsets(WindowInsetsCompat.Type.systemBars());
            Insets displayCutout = windowInsets.getInsets(WindowInsetsCompat.Type.displayCutout());
            SystemBarPadding.Edges padding = SystemBarPadding.padding(
                    new SystemBarPadding.Edges(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom),
                    new SystemBarPadding.Edges(displayCutout.left, displayCutout.top, displayCutout.right, displayCutout.bottom)
            );
            view.setPadding(padding.left, padding.top, padding.right, padding.bottom);
            return new WindowInsetsCompat.Builder(windowInsets)
                    .setInsets(WindowInsetsCompat.Type.systemBars(), Insets.NONE)
                    .setInsets(WindowInsetsCompat.Type.displayCutout(), Insets.NONE)
                    .build();
        });
        ViewCompat.requestApplyInsets(root);
        root.post(() -> ViewCompat.requestApplyInsets(root));
    }
}
