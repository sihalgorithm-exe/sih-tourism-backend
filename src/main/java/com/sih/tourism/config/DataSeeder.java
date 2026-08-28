package com.sih.tourism.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.sih.tourism.entity.BudgetLevel;
import com.sih.tourism.entity.Destination;
import com.sih.tourism.entity.FoodPlace;
import com.sih.tourism.entity.Hotel;
import com.sih.tourism.entity.ShoppingPlace;
import com.sih.tourism.entity.TransportOption;
import com.sih.tourism.repository.DestinationRepository;
import com.sih.tourism.repository.FoodPlaceRepository;
import com.sih.tourism.repository.HotelRepository;
import com.sih.tourism.repository.ShoppingPlaceRepository;
import com.sih.tourism.repository.TransportOptionRepository;

@Component
public class DataSeeder implements CommandLineRunner {

    private final DestinationRepository destinationRepository;
    private final HotelRepository hotelRepository;
    private final FoodPlaceRepository foodPlaceRepository;
    private final ShoppingPlaceRepository shoppingPlaceRepository;
    private final TransportOptionRepository transportOptionRepository;

    @Autowired
    public DataSeeder(DestinationRepository destinationRepository, HotelRepository hotelRepository,
                       FoodPlaceRepository foodPlaceRepository, ShoppingPlaceRepository shoppingPlaceRepository,
                       TransportOptionRepository transportOptionRepository) {
        this.destinationRepository = destinationRepository;
        this.hotelRepository = hotelRepository;
        this.foodPlaceRepository = foodPlaceRepository;
        this.shoppingPlaceRepository = shoppingPlaceRepository;
        this.transportOptionRepository = transportOptionRepository;
    }

   

    private void seedDestinations() {
        Destination d1 = new Destination();
        d1.setName("Araku Valley");
        d1.setDescription("Scenic hill station known for coffee plantations and tribal culture.");
        d1.setCategory("nature");
        d1.setBudgetLevel(BudgetLevel.MEDIUM);
        d1.setLatitude(18.3273);
        d1.setLongitude(82.8770);
        d1.setPopularityScore(3.5);
        d1.setCity("Araku");
        d1.setImgUrl("/images/destinations/ara.jpg");

        Destination d2 = new Destination();
        d2.setName("Undavalli Caves");
        d2.setDescription("Ancient rock-cut cave temple, a hidden gem for heritage lovers.");
        d2.setCategory("heritage");
        d2.setBudgetLevel(BudgetLevel.LOW);
        d2.setLatitude(16.5386);
        d2.setLongitude(80.5772);
        d2.setPopularityScore(2.8);
        d2.setCity("Vijayawada");
        d2.setImgUrl("/images/destinations/undava.jpg");

        Destination d3 = new Destination();
        d3.setName("Kondapalli Fort");
        d3.setDescription("Lesser-known 14th-century fort with panoramic valley views.");
        d3.setCategory("heritage");
        d3.setBudgetLevel(BudgetLevel.LOW);
        d3.setLatitude(16.6144);
        d3.setLongitude(80.5241);
        d3.setPopularityScore(2.2);
        d3.setCity("Vijayawada");
        d3.setImgUrl("/images/destinations/kondap.jpg");

        Destination d4 = new Destination();
        d4.setName("Kolleru Lake");
        d4.setDescription("One of the largest freshwater lakes in India, a bird-watcher's hidden gem.");
        d4.setCategory("nature");
        d4.setBudgetLevel(BudgetLevel.LOW);
        d4.setLatitude(16.6167);
        d4.setLongitude(81.2000);
        d4.setPopularityScore(2.0);
        d4.setCity("Eluru");
        d4.setImgUrl("/images/destinations/koll.jpg");

        Destination d5 = new Destination();
        d5.setName("Amaravati Stupa");
        d5.setDescription("Historic Buddhist site with an archaeological museum.");
        d5.setCategory("heritage");
        d5.setBudgetLevel(BudgetLevel.LOW);
        d5.setLatitude(16.5730);
        d5.setLongitude(80.3572);
        d5.setPopularityScore(3.0);
        d5.setCity("Amaravati");
        d5.setImgUrl("/images/destinations/amstupa.jpg");

        destinationRepository.saveAll(java.util.List.of(d1, d2, d3, d4, d5));
    }

    private void seedHotels() {
        Hotel h1 = new Hotel();
        h1.setName("Hotel Ilapuram");
        h1.setDescription("Budget-friendly stay near the city center.");
        h1.setBudgetLevel(BudgetLevel.LOW);
        h1.setLatitude(16.5062);
        h1.setLongitude(80.6480);
        h1.setCity("Vijayawada");
        h1.setRating(3.6);
        h1.setImgUrl("/images/hotels/ilapur.jpg");

        Hotel h2 = new Hotel();
        h2.setName("Novotel Vijayawada");
        h2.setDescription("Upscale hotel with river views.");
        h2.setBudgetLevel(BudgetLevel.HIGH);
        h2.setLatitude(16.5158);
        h2.setLongitude(80.6296);
        h2.setCity("Vijayawada");
        h2.setRating(4.5);
        h2.setImgUrl("/images/hotels/novotel.jpg");

        Hotel h3 = new Hotel();
        h3.setName("Araku Valley Resort");
        h3.setDescription("Mid-range resort surrounded by coffee estates.");
        h3.setBudgetLevel(BudgetLevel.MEDIUM);
        h3.setLatitude(18.3300);
        h3.setLongitude(82.8800);
        h3.setCity("Araku");
        h3.setRating(4.0);
        h3.setImgUrl("/images/hotels/arakures.jpg");

        hotelRepository.saveAll(java.util.List.of(h1, h2, h3));
    }

     @Override
    public void run(String... args) {
        // DEVELOPMENT SAMPLE DATA - only seeds if tables are empty, so it's safe to restart repeatedly.
        if (destinationRepository.count() == 0) {
            seedDestinations();
        }
        if (hotelRepository.count() == 0) {
            seedHotels();
        }
        if (foodPlaceRepository.count() == 0) {
            seedFoodPlaces();
        }
        if (shoppingPlaceRepository.count() == 0) {
            seedShoppingPlaces();
        }
        if (transportOptionRepository.count() == 0) {
            seedTransportOptions();
        }

        updateImageUrls();
    }

    private void updateImageUrls() {

    var destinations = destinationRepository.findAll();
    destinations.forEach(d -> {
        switch (d.getName()) {
            case "Araku Valley" ->
                    d.setImgUrl("/images/destinations/ara.jpg");
            case "Undavalli Caves" ->
                    d.setImgUrl("/images/destinations/undava.jpg");
            case "Kondapalli Fort" ->
                    d.setImgUrl("/images/destinations/kondap.jpg");
            case "Kolleru Lake" ->
                    d.setImgUrl("/images/destinations/koll.jpg");
            case "Amaravati Stupa" ->
                    d.setImgUrl("/images/destinations/amstupa.jpg");
        }
    });
    destinationRepository.saveAll(destinations);

    var hotels = hotelRepository.findAll();
    hotels.forEach(h -> {
        switch (h.getName()) {
            case "Hotel Ilapuram" ->
                    h.setImgUrl("/images/hotels/ilapur.jpg");
            case "Novotel Vijayawada" ->
                    h.setImgUrl("/images/hotels/novotel.jpg");
            case "Araku Valley Resort" ->
                    h.setImgUrl("/images/hotels/arakures.jpg");
        }
    });
    hotelRepository.saveAll(hotels);

    var foodPlaces = foodPlaceRepository.findAll();
    foodPlaces.forEach(f -> {
        switch (f.getName()) {
            case "Minerva Coffee Shop" ->
                    f.setImgUrl("/images/food/min.jpg");
            case "Southern Spice" ->
                    f.setImgUrl("/images/food/south.jpg");
        }
    });
    foodPlaceRepository.saveAll(foodPlaces);

    var shoppingPlaces = shoppingPlaceRepository.findAll();
    shoppingPlaces.forEach(s -> {
        switch (s.getName()) {
            case "Kondapalli Toys Market" ->
                    s.setImgUrl("/images/shopping/kondtoys.jpg");
            case "MG Road Market" ->
                    s.setImgUrl("/images/shopping/mgroad.jpg");
        }
    });
    shoppingPlaceRepository.saveAll(shoppingPlaces);
}

    private void seedFoodPlaces() {
        FoodPlace f1 = new FoodPlace();
        f1.setName("Minerva Coffee Shop");
        f1.setDescription("Popular South Indian breakfast spot.");
        f1.setCuisineType("South Indian");
        f1.setBudgetLevel(BudgetLevel.LOW);
        f1.setLatitude(16.5100);
        f1.setLongitude(80.6300);
        f1.setCity("Vijayawada");
        f1.setRating(4.2);
        f1.setImgUrl("/images/food/min.jpg");

        FoodPlace f2 = new FoodPlace();
        f2.setName("Southern Spice");
        f2.setDescription("Fine dining with Andhra specialties.");
        f2.setCuisineType("Andhra");
        f2.setBudgetLevel(BudgetLevel.HIGH);
        f2.setLatitude(16.5070);
        f2.setLongitude(80.6470);
        f2.setCity("Vijayawada");
        f2.setRating(4.6);
        f2.setImgUrl("/images/food/south.jpg");

        foodPlaceRepository.saveAll(java.util.List.of(f1, f2));
    }

    private void seedShoppingPlaces() {
        ShoppingPlace s1 = new ShoppingPlace();
        s1.setName("Kondapalli Toys Market");
        s1.setDescription("Traditional wooden toy craft market.");
        s1.setCategory("handicrafts");
        s1.setBudgetLevel(BudgetLevel.LOW);
        s1.setLatitude(16.6150);
        s1.setLongitude(80.5250);
        s1.setCity("Vijayawada");
        s1.setImgUrl("/images/shopping/kondtoys.jpg");

        ShoppingPlace s2 = new ShoppingPlace();
        s2.setName("MG Road Market");
        s2.setDescription("Bustling local market for clothing and souvenirs.");
        s2.setCategory("local market");
        s2.setBudgetLevel(BudgetLevel.MEDIUM);
        s2.setLatitude(16.5060);
        s2.setLongitude(80.6440);
        s2.setCity("Vijayawada");
        s2.setImgUrl("/images/shopping/mgroad.jpg");

        shoppingPlaceRepository.saveAll(java.util.List.of(s1, s2));
    }

    private void seedTransportOptions() {
        TransportOption t1 = new TransportOption();
        t1.setName("Pandit Nehru Bus Station");
        t1.setDescription("Main city bus hub with routes across Vijayawada.");
        t1.setType("bus");
        t1.setCity("Vijayawada");
        t1.setLatitude(16.5165);
        t1.setLongitude(80.6220);

        TransportOption t2 = new TransportOption();
        t2.setName("Vijayawada Auto Stand - Bus Stand Road");
        t2.setDescription("Local auto-rickshaw stand for short trips.");
        t2.setType("auto");
        t2.setCity("Vijayawada");
        t2.setLatitude(16.5140);
        t2.setLongitude(80.6180);

        transportOptionRepository.saveAll(java.util.List.of(t1, t2));
    }
}
