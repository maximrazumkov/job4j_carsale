package ru.job4j.carsale.store;

import ru.job4j.carsale.models.*;

import java.util.List;

public interface Store {
    List<Advertisement> findAllAdvertisements();
    List<Brand> findAllBrands();
    List<Model> findModelByBrandId(Integer brandId);
    List<User> findUserByLogin(String login);
    Integer createAdvertisement(Advertisement advertisement);
    void updateAdvertisement(Advertisement advertisement);
    Integer createUser(User user);
    Advertisement findAdvertisementById(Integer advertisementId);
    List<Status> findAllStatus();
}
