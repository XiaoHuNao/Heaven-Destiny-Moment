package com.xiaohunao.heaven_destiny_moment.common.utils;

import org.joml.Vector3f;

public class ColorUtils {
    public static Vector3f colorToVector3f(int color) {
        float r = ((color >> 16) & 0xFF) / 255.0f;
        float g = ((color >> 8) & 0xFF) / 255.0f;
        float b = (color & 0xFF) / 255.0f;
        return new Vector3f(r, g, b);
    }
    public static int vector3fToColor(Vector3f vector3f) {
        int r = (int) (vector3f.x() * 255.0f);
        int g = (int) (vector3f.y() * 255.0f);
        int b = (int) (vector3f.z() * 255.0f);
        return r << 16 | g << 8 | b;
    }
}