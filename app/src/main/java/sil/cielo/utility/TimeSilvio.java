package sil.cielo.utility;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class TimeSilvio {


    public static double getJDofDateObject(Date d) {
        Calendar tcal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        tcal.setTime(d);
        int year = tcal.get(Calendar.YEAR);
        int month = tcal.get(Calendar.MONTH) + 1; // Note: zero based!
        int day = tcal.get(Calendar.DAY_OF_MONTH);
        int hour = tcal.get(Calendar.HOUR_OF_DAY);
        int minute = tcal.get(Calendar.MINUTE);
        int second = tcal.get(Calendar.SECOND);


        return jday(year, month, day, hour, minute, second);
    }


    private static double jday(
            int year, int mon, int day, int hr, int minute, double sec//,
    ) {
        double jd;
        jd = 367.0 * year -
                Math.floor((7 * (year + Math.floor((mon + 9) / 12.0))) * 0.25) +
                Math.floor(275 * mon / 9.0) +
                day + 1721013.5 +
                ((sec / 60.0 + minute) / 60.0 + hr) / 24.0;  // ut in days

        return jd;
    }  // end jday


    /**
     * the local mean sidereal time (LMST)
     * WIKI https://en.wikipedia.org/wiki/Hour_angle
     *
     * @param jd
     * @param latDeg
     * @return
     */

    public static double getLMSTDeg(double jd, double latDeg) {
        double d = Math.toDegrees(ThetaG_JD(jd)) + latDeg;
        if (d < 0) {
            d = d + 360;
        } else if (d > 360) {
            d = d - 360;
        }

        return d;
    }


    /**
     * Greenwich Mean Sidereal Time
     * (Reference:  The 1992 Astronomical Almanac, page B6.)
     *
     * @param jd the input is the Julian Date of the time of interest
     * @return the GMST in radians
     */

    private static double ThetaG_JD(double jd) {
        double UT, TU, GMST;
        UT = frac(jd + 0.5);
        jd = jd - UT;
        TU = (jd - 2451545.0) / 36525;
        GMST = 24110.54841 + TU * (8640184.812866 + TU * (0.093104 - TU * 6.2E-6));
        GMST = moduloInPascal(GMST + 86400.0 * 1.00273790934 * UT, 86400.0);
        return (2 * Math.PI * (GMST / 86400.0));
    }


    private static double frac(final double num) {
        return num - (long) num;
    }

    private static double moduloInPascal(final double arg1, final double arg2) {
        //return arg1 mod arg2 like pascal
        if (arg2 == 0 || arg2 < 0) {
            try {
                //nothing;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (arg2 == 1) {
            return 0;
        } else {
            return (arg1 % arg2 + arg2) % arg2;
        }
        return 0;
    }

}
