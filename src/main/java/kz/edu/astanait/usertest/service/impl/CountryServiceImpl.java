package kz.edu.astanait.usertest.service.impl;

import kz.edu.astanait.usertest.annotation.Loggable;
import kz.edu.astanait.usertest.model.Country;
import kz.edu.astanait.usertest.repository.CountryRepository;
import kz.edu.astanait.usertest.service.CountryService;
import lombok.AllArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
@Loggable
@Log4j2
public class CountryServiceImpl implements CountryService {
    private final CountryRepository countryRepository;

    @Override
    public Country findByName(String countryName) {
        Optional<Country> oCountry = countryRepository.findByName(countryName);
        if(oCountry.isEmpty()) {
            log.info("didn't find country");
            return null;
        }
        log.info("found country");
        return oCountry.get();
    }

    @Override
    public Country add(String countryName) {
        log.info("Country pre-save");
        return countryRepository.save(new Country(countryName));
    }
}
