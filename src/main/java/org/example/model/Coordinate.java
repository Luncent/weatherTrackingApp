package org.example.model;

import java.math.BigDecimal;

//hi
public final class Coordinate {
    private final BigDecimal latitude;
    private final BigDecimal longitude;

    public Coordinate(BigDecimal latitude, BigDecimal longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public BigDecimal getLatitude() {
        return this.latitude;
    }

    public BigDecimal getLongitude() {
        return this.longitude;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof Coordinate)) return false;
        final Coordinate other = (Coordinate) o;
        final Object this$latitude = this.getLatitude();
        final Object other$latitude = other.getLatitude();
        if (this$latitude == null ? other$latitude != null : !this$latitude.equals(other$latitude)) return false;
        final Object this$longitude = this.getLongitude();
        final Object other$longitude = other.getLongitude();
        if (this$longitude == null ? other$longitude != null : !this$longitude.equals(other$longitude)) return false;
        return true;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $latitude = this.getLatitude();
        result = result * PRIME + ($latitude == null ? 43 : $latitude.hashCode());
        final Object $longitude = this.getLongitude();
        result = result * PRIME + ($longitude == null ? 43 : $longitude.hashCode());
        return result;
    }

    public String toString() {
        return "Coordinate(latitude=" + this.getLatitude() + ", longitude=" + this.getLongitude() + ")";
    }
}
