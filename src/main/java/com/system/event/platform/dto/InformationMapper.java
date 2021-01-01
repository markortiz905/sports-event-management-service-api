package com.system.event.platform.dto;

import com.system.event.platform.entities.Event;
import com.system.event.platform.entities.Information;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * @author mark ortiz
 */
@Component
public final class InformationMapper {
    public Information toEntity(InformationDto dto) {
        if (dto == null) return null;
        return Information.builder()
                .id(dto.getId())
                .shortName(dto.getShortName())
                .searchKeywords(dto.getSearchKeywords())
                .organizationName(dto.getOrganizationName())
                .startDate(dto.getStartDate())
                .endDate(dto.getEndDate())
                .timeZone(dto.getTimeZone())
                .websiteUrl(dto.getWebsiteUrl())
                .address(dto.getAddress())
                .zipCode(dto.getZipCode())
                .city(dto.getCity())
                .stateOrProvince(dto.getStateOrProvince())
                .country(dto.getCountry())
                .routeDescription(dto.getRouteDescription())
                .venueFacilities(dto.getVenueFacilities())
                .refreshmentStation(dto.getRefreshmentStation())
                .additionalInfo(dto.getAdditionalInfo())
                .freeText(dto.getFreeText())
                .tags(dto.getTags())
                .linkToRouteMap(dto.getLinkToRouteMap())
                .weatherDescription(dto.getWeatherDescription())
                .minEntryFee(dto.getMinEntryFee())
                .maxEntryFee(dto.getMaxEntryFee())
                .currency(dto.getCurrency())
                .maxParticipants(dto.getMaxParticipants())
                .chipTimingAvailable(dto.getChipTimingAvailable())
                .eventSoldOut(dto.getEventSoldOut())
                .numberOfParticipantsLastYear(dto.getNumberOfParticipantsLastYear())
                .linkToLastYearsResults(dto.getLinkToLastYearsResults())
                .linkToFacebookPage(dto.getLinkToFacebookPage())
                .publicEmail(dto.getPublicEmail())
                .publicTel(dto.getPublicTel())
                .privateEmail(dto.getPrivateEmail())
                .privateTel(dto.getPrivateTel())
                .instagramTags(dto.getInstagramTags())
                .build();
    }
    public InformationDto toDto(Information entity) {
        if (entity == null) return null;
        return InformationDto.builder()
                .id(entity.getId())
                .shortName(entity.getShortName())
                .searchKeywords(entity.getSearchKeywords())
                .organizationName(entity.getOrganizationName())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .timeZone(entity.getTimeZone())
                .websiteUrl(entity.getWebsiteUrl())
                .address(entity.getAddress())
                .zipCode(entity.getZipCode())
                .city(entity.getCity())
                .stateOrProvince(entity.getStateOrProvince())
                .country(entity.getCountry())
                .routeDescription(entity.getRouteDescription())
                .venueFacilities(entity.getVenueFacilities())
                .refreshmentStation(entity.getRefreshmentStation())
                .additionalInfo(entity.getAdditionalInfo())
                .freeText(entity.getFreeText())
                .tags(entity.getTags())
                .linkToRouteMap(entity.getLinkToRouteMap())
                .weatherDescription(entity.getWeatherDescription())
                .minEntryFee(entity.getMinEntryFee())
                .maxEntryFee(entity.getMaxEntryFee())
                .currency(entity.getCurrency())
                .maxParticipants(entity.getMaxParticipants())
                .chipTimingAvailable(entity.getChipTimingAvailable())
                .eventSoldOut(entity.getEventSoldOut())
                .numberOfParticipantsLastYear(entity.getNumberOfParticipantsLastYear())
                .linkToLastYearsResults(entity.getLinkToLastYearsResults())
                .linkToFacebookPage(entity.getLinkToFacebookPage())
                .publicEmail(entity.getPublicEmail())
                .publicTel(entity.getPublicTel())
                .privateEmail(entity.getPrivateEmail())
                .privateTel(entity.getPrivateTel())
                .instagramTags(entity.getInstagramTags())
                .eventId(Optional.ofNullable(entity.getEvent()).orElse(Event.emptyEvent()).getId())
                .build();
    }
}
