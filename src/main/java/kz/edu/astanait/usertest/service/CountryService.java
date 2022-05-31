package kz.edu.astanait.usertest.service;

import kz.edu.astanait.usertest.model.Country;

public interface CountryService {
    Country findByName(String countryName);

    Country add(String countryName);
}
