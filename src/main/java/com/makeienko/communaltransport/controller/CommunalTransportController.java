package com.makeienko.communaltransport.controller;

import com.makeienko.communaltransport.model.CommonRoute;
import com.makeienko.communaltransport.model.CommunalTransport;
import com.makeienko.communaltransport.request.DelayUpdateRequest;
import com.makeienko.communaltransport.request.MarkAsFavouriteRequest;
import com.makeienko.communaltransport.service.CommunalTransportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/communal-transport")
public class CommunalTransportController {

    @Autowired
    private CommunalTransportService communalTransportService;

    @PostMapping("/create")
    public ResponseEntity<CommunalTransport> createCommunalRoute(@RequestBody CommunalTransport newRoute) {
        CommunalTransport createdRoute = communalTransportService.createCommunalRoute(newRoute);
        return ResponseEntity.ok(createdRoute);
    }

    @GetMapping("/{routeId}")
    public ResponseEntity<CommunalTransport> getRouteById(@PathVariable Long routeId) {
        Optional<CommunalTransport> route = communalTransportService.getRoute(routeId);
        return route.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.noContent().build());
    }

    @GetMapping("/getCommunalRoute")
    public ResponseEntity<CommunalTransport> getCommunalRouteFromPlaceToPlace(@RequestParam String fromPlace, @RequestParam String toPlace) {
        CommunalTransport result = communalTransportService.getCommunalRoute(fromPlace, toPlace);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/getAllRoutes")
    public ResponseEntity<List<CommunalTransport>> getAllRoutes() {
        List<CommunalTransport> routes = communalTransportService.getAllRoutes();
        return routes != null && !routes.isEmpty() ? ResponseEntity.ok(routes) : ResponseEntity.noContent().build();
    }

    @GetMapping("/getDelaysAndFaults")
    public ResponseEntity<List<CommunalTransport>> getDelaysAndFaults() {
        List<CommunalTransport> transportsWithDelaysAndFaults = communalTransportService.findAllWithDelaysAndFaults();
        if (!transportsWithDelaysAndFaults.isEmpty()) {
            return new ResponseEntity<>(transportsWithDelaysAndFaults, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getDelaysAndFaults/{routeId}")
    public ResponseEntity<String> getDelaysAndFaultsById(@PathVariable("routeId") Long id) {
        try {
            Optional<CommunalTransport> transport = communalTransportService.findById(id);
            if (transport.isPresent()) {
                CommunalTransport actualTransport = transport.get();
                String delayReport = actualTransport.getDelayReport();
                String estimatedDelay = actualTransport.getEstimatedDelay();
                return new ResponseEntity<>("Delay Report: " + delayReport + ", Estimated Delay: " + estimatedDelay, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("No CommunalTransport found with id: " + id, HttpStatus.NOT_FOUND);
            }
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getFavoriteRoutes")
    public ResponseEntity<List<CommunalTransport>> getFavouriteRoutes() {
        List<CommunalTransport> transportsWithFavoriteRoutes = communalTransportService.getFavouriteRoutes();
        if(!transportsWithFavoriteRoutes.isEmpty()) {
            return new ResponseEntity<>(transportsWithFavoriteRoutes, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }
    @PostMapping("/markRouteAsFavourite")
    public ResponseEntity<Void> markRouteAsFavourite(@RequestBody MarkAsFavouriteRequest request) {
        if (request.getIsFavourite()) {
            communalTransportService.markRouteAsFavourite(request.getRouteId());
        } else {
            communalTransportService.unmarkRouteAsFavourite(request.getRouteId());
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/updateDelayReport/{routeId}")
    public ResponseEntity<Void> updateDelayReport(@PathVariable Long routeId, @RequestBody DelayUpdateRequest request) {
        try {
            communalTransportService.updateDelayReport(routeId, request.getDelayReport(), request.getEstimatedDelay());
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/{individualServiceDtoId}/commonRoute/{communalTransportId}")
    public CommonRoute createCommonRoute(@PathVariable Long individualServiceDtoId, @PathVariable Long communalTransportId) {
        return communalTransportService.createCommonRoute(individualServiceDtoId, communalTransportId);
    }
}
