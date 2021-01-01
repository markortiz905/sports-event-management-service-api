package com.system.event.platform.entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author mark ortiz
 */
@Entity
@Table(name = "event_information")
@Builder(toBuilder = true)
@Getter @Setter //mapper needs setter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class Information implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @EqualsAndHashCode.Include
    private Long id;

    @OneToOne(mappedBy = "information")
    @JsonBackReference
    @JsonIgnore
    private Event event;

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
    @Lob
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

    @Override
    public String toString() {
        return "Information{" +
                "id=" + id +
                ", shortName='" + shortName + '\'' +
                ", searchKeywords='" + searchKeywords + '\'' +
                ", organizationName='" + organizationName + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", timeZone='" + timeZone + '\'' +
                ", websiteUrl='" + websiteUrl + '\'' +
                ", address='" + address + '\'' +
                ", zipCode='" + zipCode + '\'' +
                ", city='" + city + '\'' +
                ", stateOrProvince='" + stateOrProvince + '\'' +
                ", country='" + country + '\'' +
                ", routeDescription='" + routeDescription + '\'' +
                ", venueFacilities='" + venueFacilities + '\'' +
                ", refreshmentStation='" + refreshmentStation + '\'' +
                ", additionalInfo='" + additionalInfo + '\'' +
                ", freeText='" + freeText + '\'' +
                ", tags='" + tags + '\'' +
                ", linkToRouteMap='" + linkToRouteMap + '\'' +
                ", weatherDescription='" + weatherDescription + '\'' +
                ", minEntryFee=" + minEntryFee +
                ", maxEntryFee=" + maxEntryFee +
                ", currency='" + currency + '\'' +
                ", maxParticipants=" + maxParticipants +
                ", chipTimingAvailable=" + chipTimingAvailable +
                ", eventSoldOut=" + eventSoldOut +
                ", numberOfParticipantsLastYear=" + numberOfParticipantsLastYear +
                ", linkToLastYearsResults='" + linkToLastYearsResults + '\'' +
                ", linkToFacebookPage='" + linkToFacebookPage + '\'' +
                ", publicEmail='" + publicEmail + '\'' +
                ", publicTel='" + publicTel + '\'' +
                ", privateEmail='" + privateEmail + '\'' +
                ", privateTel='" + privateTel + '\'' +
                ", instagramTags='" + instagramTags + '\'' +
                '}';
    }
}
