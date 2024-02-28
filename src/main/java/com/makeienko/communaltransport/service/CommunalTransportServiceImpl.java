package com.makeienko.communaltransport.service;

import com.makeienko.communaltransport.dto.IndividualServiceDto;
import com.makeienko.communaltransport.model.CommonRoute;
import com.makeienko.communaltransport.model.CommunalTransport;
import com.makeienko.communaltransport.repository.CommonRouteRepository;
import com.makeienko.communaltransport.repository.CommunalTransportRepository;
import com.makeienko.communaltransport.repository.IndividualServiceDtoRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("communalTransportService")
@Slf4j
public class CommunalTransportServiceImpl implements CommunalTransportService {

    @Autowired
    private CommunalTransportRepository communalTransportRepository;
    @Autowired
    private IndividualServiceDtoRepository individualServiceDtoRepository;
    @Autowired
    private CommonRouteRepository commonRouteRepository;

    @Override
    public CommonRoute createCommonRoute(Long individualServiceDtoId, Long communalTransportId) {
        IndividualServiceDto individualServiceDto = individualServiceDtoRepository.findById(individualServiceDtoId).orElseThrow(() -> new RuntimeException("IndividualServiceDto not found with id " + individualServiceDtoId));
        CommunalTransport communalTransport = communalTransportRepository.findById(communalTransportId).orElseThrow(() -> new RuntimeException("CommunalTransport not found with id " + communalTransportId));

        CommonRoute commonRoute = new CommonRoute();
        commonRoute.setIndividualRouteStart(individualServiceDto.getStart());
        commonRoute.setCommunalTransportFinish(communalTransport.getToPlace());
        return commonRouteRepository.save(commonRoute);
    }

    @Override
    public CommonRoute getCommonRoute(IndividualServiceDto individualServiceDto, CommunalTransport communalTransport) {
        CommonRoute commonRoute = new CommonRoute();
        commonRoute.setIndividualRouteStart(individualServiceDto.getStart());
        commonRoute.setCommunalTransportFinish(communalTransport.getToPlace());
        return commonRoute;
    }

    @Override
    public CommunalTransport createCommunalRoute(CommunalTransport newRoute) {
        return communalTransportRepository.save(newRoute);
    }
    @Override
    public CommunalTransport getCommunalRoute(String fromPlace, String toPlace) {
        return communalTransportRepository.findByFromPlaceAndToPlace(fromPlace, toPlace);
    }

    @Override
    public List<CommunalTransport> getAllRoutes() {
        return communalTransportRepository.findAll();
    }

    @Override
    public Optional<CommunalTransport> findById(Long id) {
        return communalTransportRepository.findById(id);
    }

    @Override
    public void findBestBetweenTransportAndWalking(Long id, String delayReport, String estimatedDelay) {
        Optional<CommunalTransport> transport = communalTransportRepository.findById(id);
        if(transport.isPresent()) {
            CommunalTransport actualTransport = transport.get();
            actualTransport.setDelayReport(delayReport);
            actualTransport.setEstimatedDelay(estimatedDelay);

//            // Hämta gångtiden från den enskilda transport-API:en
//            Duration walkingTime = getWalkingTime(actualTransport.getFromPlace(), actualTransport.getToPlace());
//
//            // Om förseningen gör att tiden för resan överstiger gångtiden, föreslå en gå-rutt
//            if (estimatedDelay.compareTo(walkingTime) > 0) {
//                actualTransport.setAlternativeRoute("Walking is faster due to delay. Estimated walking time: " + walkingTime);
//                communalTransportRepository.save(actualTransport);
//            }

        } else {
            throw new RuntimeException("No CommunalTransport found with id: " + id);
        }
    }

    @Override
    public List<CommunalTransport> getDelaysAndFaults() {
        return null;
    }

    @Override
    public void markRouteAsFavourite(Long id) {
        Optional<CommunalTransport> transport = communalTransportRepository.findById(id);
        if(transport.isPresent()) {
            CommunalTransport actualTransport = transport.get();
            actualTransport.setIsFavourite(true);
            communalTransportRepository.save(actualTransport);
        } else {
            throw new RuntimeException("No CommunalTransport found with id: " + id);
        }
    }

    @Override
    public void unmarkRouteAsFavourite(Long id) {
        Optional<CommunalTransport> transport = communalTransportRepository.findById(id);
        if(transport.isPresent()) {
            CommunalTransport actualTransport = transport.get();
            actualTransport.setIsFavourite(false);
            communalTransportRepository.save(actualTransport);
        } else {
            throw new RuntimeException("No CommunalTransport found with id: " + id);
        }
    }

    @Override
    public List<CommunalTransport> getFavouriteRoutes() {
        return communalTransportRepository.findByIsFavouriteTrue();
    }

    @Override
    public List<CommunalTransport> findAllWithDelaysAndFaults() {
        return null;
    }

    @Override
    public Optional<CommunalTransport> getRoute(Long routeId) {
        Optional<CommunalTransport> route = communalTransportRepository.findById(routeId);
        return route;
    }

    @Override
    public void updateDelayReport(Long routeId, String delayReport, String estimatedDelay) {
        Optional<CommunalTransport> transport = communalTransportRepository.findById(routeId);
        if(transport.isPresent()) {
            CommunalTransport actualTransport = transport.get();
            actualTransport.setDelayReport(delayReport);
            actualTransport.setEstimatedDelay(estimatedDelay);
            communalTransportRepository.save(actualTransport);
        } else {
            throw new RuntimeException("No CommunalTransport found with id: " + routeId);
        }
    }
}
