package kz.edu.astanait.usertest.service;

import kz.edu.astanait.usertest.model.Country;
import kz.edu.astanait.usertest.repository.CountryRepository;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Log
public class CountryService {
    private final CountryRepository countryRepository;

    public Country findByName(String countryName) {
        Optional<Country> oCountry = countryRepository.findByName(countryName);
        if(oCountry.isEmpty()) {
            log.info("didn't find country");
            return null;
        }
        log.info("found country");
        return oCountry.get();
    }

    public Country add(String countryName) {
        return countryRepository.save(new Country(countryName));
    }
}
