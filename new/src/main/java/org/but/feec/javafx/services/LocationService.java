package org.but.feec.javafx.services;

import org.but.feec.javafx.api.LocationBasicView;
import org.but.feec.javafx.api.LocationCreateView;
import org.but.feec.javafx.api.LocationEditView;
import org.but.feec.javafx.data.LocationRepository;

import java.util.List;

/**
 * Class representing business logic on top of the Locations
 */
public class LocationService {

    private LocationRepository locationRepository;

    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public List<LocationBasicView> getLocationsBasicView() {
        return locationRepository.getLocationsBasicView();
    }

    public void createLocation(LocationCreateView locationCreateView) {
        // Any additional business logic before saving the location can be added here
        locationRepository.createLocation(locationCreateView);
    }

    public void editLocation(LocationEditView locationEditView) {
        locationRepository.editLocation(locationEditView);
    }

    public void deleteLocationById(Long id) {
        locationRepository.deleteLocationById(id);
    }
}
