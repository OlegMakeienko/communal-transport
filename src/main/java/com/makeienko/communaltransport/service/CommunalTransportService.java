package com.makeienko.communaltransport.service;

import com.makeienko.communaltransport.dto.IndividualServiceDto;
import com.makeienko.communaltransport.model.CommonRoute;
import com.makeienko.communaltransport.model.CommunalTransport;

import java.util.List;
import java.util.Optional;

public interface CommunalTransportService {

    CommonRoute createCommonRoute(Long individualServiceDtoId, Long communalTransportId);

    CommonRoute getCommonRoute(IndividualServiceDto individualServiceDto, CommunalTransport communalTransport);
    CommunalTransport createCommunalRoute(CommunalTransport newRoute);
    CommunalTransport getCommunalRoute(String fromPlace, String toPlace);
    List<CommunalTransport> getAllRoutes();
    Optional<CommunalTransport> findById(Long id);

    void findBestBetweenTransportAndWalking(Long id, String delayReport, String estimatedDelay);

    List<CommunalTransport> getDelaysAndFaults();
    void markRouteAsFavourite(Long id);
    List<CommunalTransport> getFavouriteRoutes();
    void unmarkRouteAsFavourite(Long id);

    List<CommunalTransport> findAllWithDelaysAndFaults();

    Optional<CommunalTransport> getRoute(Long routeId);

    void updateDelayReport(Long routeId, String delayReport, String estimatedDelay);

}
