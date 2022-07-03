package net.fabricmc.blockfinder.utils;

import net.fabricmc.blockfinder.BlockFinder;

public class Utils {

    public Utils() {}

    public static double getAngle(double yaw, double a, double b) {
        //treating the players position as the origin
        //treating west as the positive x-axis and south as the positive z-axis,
        // "degrees" represents the angle between the positive x-axis and the targeted block
        a *= -1;
        BlockFinder.LOGGER.info("Inside getAngle method");
        double degrees = Math.atan2(b,a) * (180/Math.PI);
        //0 -> 180 corresponds to 0 -> -180 yaw
        if (degrees > 0 && degrees < 90) {
            BlockFinder.LOGGER.info("inside first condition: a = " + a + ", b = " + b + " yaw = " + yaw + ", degrees = " + degrees);
            return (90 - degrees);
        } else if (degrees > 90 && degrees < 180) {
            BlockFinder.LOGGER.info("inside second condition: a = " + a + ", b = " + b + " yaw = " + yaw + ", degrees = " + degrees);
            return (90 - degrees);
        } else if (degrees < 0 && degrees > -90) {
            BlockFinder.LOGGER.info("inside third condition: a = " + a + ", b = " + b + " yaw = " + yaw + ", degrees = " + degrees);
            return (90 + (-degrees));
        } else if (degrees < -90 && degrees > -180) {
            BlockFinder.LOGGER.info("inside fourth condition: a = " + a + ", b = " + b + " yaw = " + yaw + ", degrees = " + degrees);
            degrees = (degrees + 90) * - 1; //sets it to 0-90
            return -(180 - degrees);
        } else {
            BlockFinder.LOGGER.info("inside fifth condition: a = " + a + ", b = " + b + " yaw = " + yaw + ", degrees = " + degrees);
            return Math.round(yaw);
        }
    }

    /*
    Converts the players yaw (which is usually on a -180 to 180 scale) to a 0 to 360 scale
     */
    public static double convertYaw(double yaw) {
        if (yaw < 0) {
            return 360 + yaw;
        } else {
            return yaw;
        }
    }

    //returns 1 | -1
    public static int getIdealYawIncrement(double convertedYaw, double targetDiretion) {
        double i = convertedYaw;
        double j = convertedYaw;
        double positiveArcDegrees = 0;
        double negativeArcDegrees = 0;
        while (Math.abs(i - targetDiretion ) > 5) {
            //BlockFinder.LOGGER.info("i = " + i + " target direction = " + targetDiretion);
            if (i > 360) {
                i = 0;
            } else {
                i += 1;
            }
            positiveArcDegrees += 1;
        }
        while (Math.abs(j - targetDiretion) > 5) {
            //BlockFinder.LOGGER.info("j = " + j + " target direction = " + targetDiretion);
            if (j < 0) {
                j = 360;
            } else {
                j -= 1;
            }
            negativeArcDegrees += 1;
        }
        return (positiveArcDegrees < negativeArcDegrees) ? 1 : -1;
    }
}
