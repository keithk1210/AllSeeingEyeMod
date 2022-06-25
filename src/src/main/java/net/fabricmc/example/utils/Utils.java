package net.fabricmc.example.utils;

import net.fabricmc.example.ExampleMod;

public class Utils {

    public Utils() {}

    public static double getAngle(double yaw, double a, double b) {
        ExampleMod.LOGGER.info("Inside getAngle method");
        double degrees = Math.atan2(b,a) * (180/Math.PI);
        //0 -> 180 corresponds to 0 -> -180 yaw
        if (degrees > 0 && degrees < 90) {
            ExampleMod.LOGGER.info("inside first condition: a = " + a + ", b = " + b + " yaw = " + yaw + ", degrees = " + degrees);
            return degrees - 90;
        } else if (degrees > 90 && degrees < 180) {
            ExampleMod.LOGGER.info("inside second condition: a = " + a + ", b = " + b + " yaw = " + yaw + ", degrees = " + degrees);
            return degrees-90;
        } else if (degrees < 0 && degrees > -90) {
            ExampleMod.LOGGER.info("inside third condition: a = " + a + ", b = " + b + " yaw = " + yaw + ", degrees = " + degrees);
            return degrees - 90;
        } else if (degrees < -90 && degrees > -180) {
            return degrees + 180;
        } else {
            ExampleMod.LOGGER.info("inside fourth condition: a = " + a + ", b = " + b + " yaw = " + yaw + ", degrees = " + degrees);
            return Math.round(yaw);
        }
    }
}
