package org.tomw.mediautils;

import org.apache.log4j.Logger;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;


/**
 * Package for reading image metadata
 * TODO add tests for reading image metadata
 * @author tomw
 */
public class ImageMetadata {

    private final static Logger LOGGER = Logger.getLogger(ImageMetadata.class.getName());

    public static final String EOL="\n";
    public static final String SPACE=" ";

    public static final String IMAGE_WIDTH = "Width";
    public static final String IMAGE_HEIGHT = "Height";
    public static final String EXIF_FIELDS = "EXIF Fields: ";

    public static final String DATE = "Date: ";
    public static final int DATE_KEY = 0x0132;
//    private static final String pattern = "yyyy:MM:DD hh:mm:ss";
//    private static final DateTimeFormatter formatter
//            = DateTimeFormatter.ofPattern(pattern);

    public static final String CAMERA = "Camera: ";
    public static final int CAMERA_KEY = 0x0110;
    public static final String MANUFACTURER = "Manufacturer: ";
    public static final int MANUFACTURER_KEY = 0x010F;
    public static final String FOCAL_LENGTH = "Focal Length: ";
    public static final int FOCAL_LENGTH_KEY = 0x920A;

    public static final String F_STOP = "F-Stop: ";
    public static final int F_STOP_KEY = 0x829D;

    public static final String EXPOSURE_TIME = "Exposure Time (1 / Shutter Speed): ";
    public static final int EXPOSURE_TIME_KEY = 0x829A;

    public static final String ISO = "ISO Speed Ratings: ";
    public static final int ISO_KEY = 0x8827;

    public static final String SHUTTER_SPEED_APEX = "Shutter Speed Value (APEX): ";
    public static final int SHUTTER_SPEED_APEX_KEY = 0x9201;

    public static final String SHUTTER_SPEED_TIME = "Shutter Speed (Exposure Time): ";
    public static final int SHUTTER_SPEED_TIME_KEY = 0x9201;

    public static final String APERTURE_VALUE = "Aperture Value (APEX): ";
    public static final int APERTURE_VALUE_KEY = 0x9202;

    public static final String ORIENTATION = "Orientation: ";
    public static final int ORIENTATION_KEY = 0x0112;

    public static final String GPS_COORDINATE_X = "GPS Coordinate x ";
    public static final String GPS_COORDINATE_Y = "GPS Coordinate y ";
    public static final String GPS_DATUM = "GPS Datum: ";

    public static final Map<Integer, String> orientationDescription = new HashMap<>();

    static {
        orientationDescription.put(1, "Top, left side (Horizontal / normal)");
        orientationDescription.put(2, "Top, right side (Mirror horizontal)");
        orientationDescription.put(3, "Bottom, right side (Rotate 180)");
        orientationDescription.put(4, "Bottom, left side (Mirror vertical)");
        orientationDescription.put(5, "Left side, top (Mirror horizontal and rotate 270 CW)");
        orientationDescription.put(6, "Right side, top (Rotate 90 CW)");
        orientationDescription.put(7, "Right side, bottom (Mirror horizontal and rotate 90 CW)");
    }

    private javaxt.io.Image image;
    java.util.HashMap<Integer, Object> exif = new HashMap<>();

    public ImageMetadata(File imageFile) {
        this(new javaxt.io.Image(imageFile));
    }

    public ImageMetadata(javaxt.io.Image inputImage) {
        image = inputImage;
        extractExif();
    }

    public ImageMetadata(String imageFileName) {
        this(new File(imageFileName));
    }

    /**
     * extract exif metadata
     */
    private void extractExif() {
        exif = image.getExifTags();
    }

    /**
     * get date the image was taken
     *
     * @return date and time when image was taken
     */
    public LocalDateTime getDate() {
        String dateString = (String) exif.get(DATE_KEY);

        String date = dateString.split(" ")[0];
        String time = dateString.split(" ")[1];
        String newDateTime = date.replace(":", "-") + "T" + time;

        return LocalDateTime.parse(newDateTime, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    public int getExifSize(){
        return exif.size();
    }

    public int getWidth() {
        return image.getWidth();
    }

    public int getHeight() {
        return image.getHeight();
    }

    public String getCamera() {
        return (String) exif.get(CAMERA_KEY);
    }

    public String getManufacturer() {
        return (String) exif.get(MANUFACTURER_KEY);
    }

    public String getFocalLength() {
        return (String) exif.get(FOCAL_LENGTH_KEY);
    }

    public String getFstop() {
        return (String) exif.get(F_STOP_KEY);
    }

    public String getExposureTime() {
        return (String) exif.get(EXPOSURE_TIME_KEY);
    }

    public String getIso() {
        return (String) exif.get(ISO_KEY);
    }

    public String getShutterSpeedApex() {
        return (String) exif.get(SHUTTER_SPEED_APEX_KEY);
    }

    public String getShutterSpeedTime() {
        return (String) exif.get(SHUTTER_SPEED_TIME_KEY);
    }

    public String getApertureValue() {
        return (String) exif.get(APERTURE_VALUE_KEY);
    }

    public int getOrientation() {
        return (Integer) exif.get(ORIENTATION_KEY);
    }

    public String getOrientationDescription() {
        return orientationDescription.get(getOrientation());
    }

    public double[] getGPSCoordinate() {
        return image.getGPSCoordinate();
    }

    public String getGpsDatum() {
        return image.getGPSDatum();
    }

    @Override
    public String toString(){
        String result="";
        result=result+IMAGE_WIDTH+SPACE+this.getWidth()+EOL;
        result=result+IMAGE_HEIGHT+SPACE+this.getHeight()+EOL;
        result=result+EXIF_FIELDS+SPACE+this.getExifSize()+EOL;
        result=result+DATE+SPACE+this.getDate()+EOL;
        result=result+CAMERA+SPACE+this.getCamera()+EOL;
        result=result+MANUFACTURER+SPACE+this.getManufacturer()+EOL;
        result=result+FOCAL_LENGTH+SPACE+this.getFocalLength()+EOL;
        result=result+F_STOP+SPACE+this.getFstop()+EOL;
        result=result+EXPOSURE_TIME+SPACE+this.getExposureTime()+EOL;
        result=result+ISO+SPACE+this.getIso()+EOL;
        result=result+SHUTTER_SPEED_APEX+SPACE+this.getShutterSpeedApex()+EOL;
        result=result+SHUTTER_SPEED_TIME+SPACE+this.getShutterSpeedTime()+EOL;
        result=result+APERTURE_VALUE+SPACE+this.getApertureValue()+EOL;
        result=result+ORIENTATION+SPACE+this.getOrientation()+SPACE+this.getOrientationDescription()+EOL;
        double[] coord = image.getGPSCoordinate();
        if (coord != null) {
            result=result+GPS_COORDINATE_X+SPACE+this.getGPSCoordinate()[0]+EOL;
            result=result+GPS_COORDINATE_Y+SPACE+this.getGPSCoordinate()[1]+EOL;
        }
        result=result+GPS_DATUM+SPACE+this.getGpsDatum()+EOL;
        return result;
    }
}

