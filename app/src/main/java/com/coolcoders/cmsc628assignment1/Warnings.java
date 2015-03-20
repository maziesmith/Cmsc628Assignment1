package com.coolcoders.cmsc628assignment1;

public class Warnings {

    /** Use static constants to define error types */
    public static int OK=0; /** Indicates that no errors were detected. */
    public static int NO_SENSORS=1; /** Indicates that no SensorManager was created. */
    public static int NO_ACCELEROMETER=2; /** Indicates that no accelerometer was detected. */
    public static int NO_GYROSCOPE=3; /** Indicates that no gyroscope was detected. */

    public static String Message(int code) {
        String strout = "Sensors okay.";
        if (code == NO_GYROSCOPE) {
            strout =  "WARNING: Missing Gyroscope!";
        } else if (code == NO_ACCELEROMETER) {
            strout = "WARNING: Missing Accelerometer!";
        } else if (code == NO_SENSORS) {
            strout = "WARNING: Missing Required Sensors!";
        }
        return strout;
    }
}
