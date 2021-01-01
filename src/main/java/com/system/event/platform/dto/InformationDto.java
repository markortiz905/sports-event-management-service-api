package com.system.event.platform.dto;

import lombok.*;

import java.io.Serializable;

/**
 * @author mark ortiz
 */
@Builder(toBuilder = true)
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class InformationDto implements Serializable {
    @EqualsAndHashCode.Include
    private Long id;
    private String shortName;
    private String searchKeywords;
    private String organizationName;
    private Long startDate;
    private Long endDate;
    private String timeZone;
    private String websiteUrl;

    private String address;
    private String zipCode;
    private String city;
    private String stateOrProvince;
    private String country;

    private String routeDescription;
    private String venueFacilities;
    private String refreshmentStation;
    private String additionalInfo;
    private String freeText;

    private String tags;
    private String linkToRouteMap;
    private String weatherDescription;

    private Double minEntryFee;
    private Double maxEntryFee;
    private String currency;
    private Integer maxParticipants;

    private Boolean chipTimingAvailable;
    private Boolean eventSoldOut;

    private Integer numberOfParticipantsLastYear;
    private String linkToLastYearsResults;
    private String linkToFacebookPage;

    private String publicEmail;
    private String publicTel;
    private String privateEmail;
    private String privateTel;
    private String instagramTags;

    private Long eventId;
}
