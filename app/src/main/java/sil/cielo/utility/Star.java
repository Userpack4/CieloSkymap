package sil.cielo.utility;


public class Star {

    /**
     * Catalog Number
     */
    private Long catalogNumber;

    /**
     * Right ascension
     */
    private Double ra;

    /**
     * Right ascension proper motion
     */
    private Double raPm;

    /**
     * Declination
     */
    private Double dec;

    /**
     * Declination proper motion
     */
    private Double decPm;

    /**
     * Magnitude
     */
    private Double magnitude;

    /**
     * Spectral type
     */
    private String spectralType;

    /**
     * Altitude
     */
    private Double altitudeDeg;

    /**
     * Azimuth
     */
    private Double azimuthDeg;

    public Long getCatalogNumber() {
        return catalogNumber;
    }

    public void setCatalogNumber(Long catalogNumber) {
        this.catalogNumber = catalogNumber;
    }

    public Double getRa() {
        return ra;
    }

    public void setRa(Double ra) {
        this.ra = ra;
    }

    public Double getRaPm() {
        return raPm;
    }

    public void setRaPm(Double raPm) {
        this.raPm = raPm;
    }

    public Double getDec() {
        return dec;
    }

    public void setDec(Double dec) {
        this.dec = dec;
    }

    public Double getDecPm() {
        return decPm;
    }

    public void setDecPm(Double decPm) {
        this.decPm = decPm;
    }

    public Double getMagnitude() {
        return magnitude;
    }

    public void setMagnitude(Double magnitude) {
        this.magnitude = magnitude;
    }

    public String getSpectralType() {
        return spectralType;
    }

    public void setSpectralType(String spectralType) {
        this.spectralType = spectralType;
    }

    public Double getAltitudeDeg() {
        return altitudeDeg;
    }

    public void setAltitudeDeg(Double altitude) {
        this.altitudeDeg = altitude;
    }

    public Double getAzimuthDeg() {
        return azimuthDeg;
    }

    public void setAzimuthDeg(Double azimuth) {
        this.azimuthDeg = azimuth;
    }
}
