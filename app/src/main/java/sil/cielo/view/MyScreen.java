package sil.cielo.view;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import sil.cielo.database.DataBaseHelper;
import sil.cielo.utility.Constellations;
import sil.cielo.utility.Star;
import sil.cielo.utility.TimeSilvio;


public class MyScreen extends View {

    Canvas canvas;

    HashMap<Long, Star> starMap = new HashMap<Long, Star>();
    private final double DEGTORAD = Math.PI / 180;
    private final double RADTODEG = 180 / Math.PI;

    private Paint circleStar, paintConstellation, circlePaint, textPaint, textPaintRosso;

    double latDegHome, lonDegHome;

    private float translateX = 0f;
    private float translateY = 0f;


    //DATABASE
    DataBaseHelper myDbHelper = null;
    Cursor c;
    private int textHeight;
    private float mScaleFactor = 1.f;

    public MyScreen(Context context) {
        super(context);
        initSkyMapView();
        invalidate();
    }

    public MyScreen(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initSkyMapView();
        invalidate();
    }

    public MyScreen(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initSkyMapView();
        invalidate();
    }


    private void initSkyMapView() {

        //init my latitude and longitude
        //Example: Rome (Italy)
        latDegHome = 41.89;
        lonDegHome = 12.51;

        textPaintRosso = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaintRosso.setColor(Color.RED);
        textPaintRosso.setTextSize(24f);

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setColor(Color.parseColor("#FFFFFF"));
        textPaint.setTextSize(24f);

        textHeight = (int) textPaint.measureText("Y");

        circlePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        circlePaint.setColor(Color.parseColor("#0b0b3b")); //like black
        circlePaint.setStrokeWidth(1);
        circlePaint.setStyle(Paint.Style.FILL_AND_STROKE);

        circleStar = new Paint(Paint.ANTI_ALIAS_FLAG);
        circleStar.setColor(Color.parseColor("#ffff8800")); //orange
        circleStar.setStrokeWidth(1);
        circleStar.setStyle(Paint.Style.FILL_AND_STROKE);

        paintConstellation = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintConstellation.setColor(Color.parseColor("#ff33b5e5")); //blue
        paintConstellation.setStrokeWidth(1);
        paintConstellation.setStyle(Paint.Style.FILL_AND_STROKE);

        double lmstRad = Math.toRadians(TimeSilvio.getLMSTDeg(TimeSilvio.getJDofDateObject(new Date()), lonDegHome));
        double latRad = Math.toRadians(latDegHome);


        myDbHelper = openDB();

        try {
            // get data from cursor
            c = myDbHelper.getAllStarsInDB();
            if (c.moveToFirst()) {
                // Iterate over each cursor.
                do {
                    Star s = new Star();
                    s.setCatalogNumber((long) Double.parseDouble(c.getString(0)));
                    s.setRa(Double.parseDouble(c.getString(1)));
                    s.setDec(Double.parseDouble(c.getString(2)));
                    s.setRaPm(Double.parseDouble(c.getString(3)));
                    s.setDecPm(Double.parseDouble(c.getString(4)));
                    s.setMagnitude(Double.parseDouble(c.getString(5)));
                    s.setSpectralType(c.getString(6));


                    double az = Math.toDegrees(Math.atan2(Math.sin(lmstRad - s.getRa()),
                            Math.cos(lmstRad - s.getRa()) * Math.sin(latRad) -
                                    Math.tan(s.getDec()) * Math.cos(latRad)) + Math.PI);
                    double altitude = Math.toDegrees(Math.asin(Math.sin(latRad) * Math.sin(s.getDec()) +
                            Math.cos(latRad) * Math.cos(s.getDec()) * Math.cos(lmstRad - s.getRa())));
                    s.setAzimuthDeg(az);
                    s.setAltitudeDeg(altitude);
                    if (altitude > 0) {
                        starMap.put(s.getCatalogNumber(), s);
                    }
                } while (c.moveToNext());
            }

        } catch (Exception e) {
            // exception handling
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }

        }

        myDbHelper.close();

    }


    @Override
    protected void onDraw(Canvas canv) {
        canvas = canv;

        float displayWidth = canvas.getWidth();
        float displayHeight = canvas.getHeight();

        canvas.save();
        canvas.scale(mScaleFactor, mScaleFactor);

        if ((translateX * -1) < 0) {
            translateX = 0;
        } else if ((translateX * -1) > (mScaleFactor - 1) * displayWidth) {
            translateX = (1 - mScaleFactor) * displayWidth;
        }

        if (translateY * -1 < 0) {
            translateY = 0;
        } else if ((translateY * -1) > (mScaleFactor - 1) * displayHeight) {
            translateY = (1 - mScaleFactor) * displayHeight;
        }

        canvas.translate(translateX / mScaleFactor, translateY / mScaleFactor);

        int px = getMeasuredWidth() / 2;
        int py = getMeasuredHeight() / 2;

        int radius = Math.min(px, py);
        px = radius;
        py = radius;

        canvas.drawCircle(radius, radius, radius, circlePaint);

        int textWidth = (int) textPaint.measureText("W");
        int cardinalX = px - textWidth / 2 + 10;
        int cardinalY = py - radius + textHeight + 10;

        String letterCompass = "";

        for (int i = 0; i < 4; i++) {

            canvas.save();
            canvas.translate(0, textHeight);

            switch (i) {
                case (0): {
                    letterCompass = "N";
                    break;
                }
                case (1):
                    letterCompass = "E";
                    break;
                case (2):
                    letterCompass = "S";
                    break;
                case (3):
                    letterCompass = "O";
                    break;
            }

            if (letterCompass == "N") {
                canvas.drawText(letterCompass, cardinalX, cardinalY, textPaintRosso);
            } else {
                canvas.drawText(letterCompass, cardinalX, cardinalY, textPaint);
            }


            canvas.rotate(90, px, py);


        }

        drawStars();
        drawConstellationLine();
        canvas.restore();

    }


    public synchronized void drawStars() {

        int dw = canvas.getWidth();
        int dh = canvas.getHeight();
        int d = Math.min(dw, dh);


        if (starMap != null) {

            for (Star s : starMap.values()) {

                double altRad = s.getAltitudeDeg() * DEGTORAD;
                double azRad = s.getAzimuthDeg() * DEGTORAD;

                final float[] xy = getXYFromAltAzi(d, altRad, azRad);

                float size = (float) getDrawSize(s.getMagnitude(), d);

                canvas.drawCircle(xy[0], xy[1], size, circleStar);
                canvas.save();


            }

        }
    }

    public synchronized void drawConstellationLine() {

        int dw = canvas.getWidth();
        int dh = canvas.getHeight();
        int radius = Math.min(dw, dh);

        for (Constellations constellation : Constellations.values()) {
            int sumX = 0;
            int sumY = 0;
            final Set<Long> visitedStars = new HashSet<>();
            for (Constellations.ConstellationSegment segment : constellation.getConstellationSegments()) {
                final Star start = starMap.get(segment.getStart());
                final Star end = starMap.get(segment.getEnd());

                if (start != null && end != null) {

                    final float[] startXy = getXYFromAltAzi(
                            radius,
                            start.getAltitudeDeg() * DEGTORAD,
                            start.getAzimuthDeg() * DEGTORAD);

                    final float[] endXy = getXYFromAltAzi(
                            radius,
                            end.getAltitudeDeg() * DEGTORAD,
                            end.getAzimuthDeg() * DEGTORAD);

                    if (!visitedStars.contains(start.getCatalogNumber())) {
                        visitedStars.add(start.getCatalogNumber());
                        sumX += startXy[0];
                        sumY += startXy[1];
                    }

                    if (!visitedStars.contains(end.getCatalogNumber())) {
                        visitedStars.add(end.getCatalogNumber());
                        sumX += endXy[0];
                        sumY += endXy[1];
                    }

                    canvas.drawLine(startXy[0], startXy[1], endXy[0], endXy[1], paintConstellation);
                }
            }


        }

    }

    /**
     * @param d
     * @param alt in radians
     * @param azi in radians
     * @return
     */

    private float[] getXYFromAltAzi(final double d, final double alt, final double azi) {
        int PAD = 0;
        final double r = d / 2;
        final double radius = r * (Math.PI / 2 - alt) / (Math.PI / 2);
        final double angle = Math.PI / 2 - azi;

        double x = radius * Math.cos(angle);
        final double y = radius * Math.sin(angle);

        final float xcoord = (float) (d / 2 + PAD + x);
        final float ycoord = (float) (d / 2 + PAD - y);

        return new float[]{xcoord, ycoord};
    }

    /**
     * Computes star size based on magnitude and size of the map
     *
     * @param magnitude Magnitude
     * @param d         Diameter of the map circle in pixels
     * @return Star size in pixels
     */
    private double getDrawSize(final double magnitude, final double d) {
        final double scale = d / 280.0;
        if (magnitude < 0) {
            return 2.5 * scale;
        } else if (magnitude >= 0 && magnitude < 1) {
            return 2.0 * scale;
        } else if (magnitude >= 1 && magnitude < 2) {
            return 1.3 * scale;
        } else if (magnitude >= 2 && magnitude < 3) {
            return 0.9 * scale;
        } else if (magnitude >= 3 && magnitude < 4) {
            return 0.5 * scale;
        } else if (magnitude >= 4 && magnitude < 5) {
            return 0.3 * scale;
        } else if (magnitude >= 5 && magnitude < 6) {
            return 0.15 * scale;
        } else if (magnitude >= 6 && magnitude < 7) {
            return 0.1 * scale;
        } else if (magnitude >= 7 && magnitude < 8) {
            return 0.0 * scale;
        }
        return 0;
    }

    public DataBaseHelper openDB() {

        try {
            myDbHelper = new DataBaseHelper(this.getContext());
        } catch (Exception ex) {
        }

        try {
            myDbHelper.createDataBase();
        } catch (IOException e) {
        }

        try {
            myDbHelper.openDataBase();
        } catch (SQLException sqle) {
        }


        return myDbHelper;
    }


}
