/*
 *
 */
package com.system.event.platform.entities;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author mark ortiz
 */
public enum DistanceUnit {
    @JsonProperty("km")
    Metric("km"),
    @JsonProperty("mi")
    Imperial("mi");

    public static final double KM_MI_RATIO = 1.609344;

    public final String abbr;

    DistanceUnit(String abbr) {
        this.abbr = abbr;
    }

    public double convert(double value, DistanceUnit to) {
        if (this == to) {
            return value;
        }
        if (to == Metric) {
            return value * KM_MI_RATIO;
        }
        return value / KM_MI_RATIO;
    }

}
