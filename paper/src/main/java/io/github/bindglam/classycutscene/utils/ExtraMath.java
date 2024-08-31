package io.github.bindglam.classycutscene.utils;

import org.joml.Quaternionf;

public final class ExtraMath {
    public static float lerp(float a, float b, float f) {
        return a * (1.0f - f) + (b * f);
    }

    public static Quaternionf fromYawPitch(float yaw, float pitch, float roll) {
        float cy = (float) Math.cos(Math.toRadians(yaw) * 0.5);
        float sy = (float) Math.sin(Math.toRadians(yaw) * 0.5);
        float cp = (float) Math.cos(Math.toRadians(pitch) * 0.5);
        float sp = (float) Math.sin(Math.toRadians(pitch) * 0.5);
        float cr = (float) Math.cos(Math.toRadians(roll) * 0.5);
        float sr = (float) Math.sin(Math.toRadians(roll) * 0.5);

        float w = cr * cp * cy + sr * sp * sy;
        float x = sr * cp * cy - cr * sp * sy;
        float y = cr * sp * cy + sr * cp * sy;
        float z = cr * cp * sy - sr * sp * cy;

        return new Quaternionf(x, y, z, w);
    }

    public static float[] toEulerAngles(Quaternionf quaternion) {
        float[] angles = new float[3];

        // Roll (x-axis rotation)
        float sinr_cosp = 2 * (quaternion.w * quaternion.x + quaternion.y * quaternion.z);
        float cosr_cosp = 1 - 2 * (quaternion.x * quaternion.x + quaternion.y * quaternion.y);
        angles[2] = (float) Math.atan2(sinr_cosp, cosr_cosp);

        // Pitch (y-axis rotation)
        float sinp = 2 * (quaternion.w * quaternion.y - quaternion.z * quaternion.x);
        if (Math.abs(sinp) >= 1)
            angles[1] = (float) Math.copySign(Math.PI / 2, sinp); // use 90 degrees if out of range
        else
            angles[1] = (float) Math.asin(sinp);

        // Yaw (z-axis rotation)
        float siny_cosp = 2 * (quaternion.w * quaternion.z + quaternion.x * quaternion.y);
        float cosy_cosp = 1 - 2 * (quaternion.y * quaternion.y + quaternion.z * quaternion.z);
        angles[0] = (float) Math.atan2(siny_cosp, cosy_cosp);

        // Convert to degrees
        for (int i = 0; i < 3; i++) {
            angles[i] = (float) Math.toDegrees(angles[i]);
        }

        return angles;
    }
}
