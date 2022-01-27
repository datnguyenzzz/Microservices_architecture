package vn.datnguyen.recommender.Handlers;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import vn.datnguyen.recommender.Models.Rating;
import vn.datnguyen.recommender.Repositories.ClientRatingRepository;

@Service
public class QueryServicesImpl implements QueryServices {
    
    private ClientRatingRepository clientRatingRepository;

    @Autowired
    public QueryServicesImpl(ClientRatingRepository clientRatingRepository) {
        this.clientRatingRepository = clientRatingRepository;
    }

    @Override
    public Optional<Rating> findRatingById(String id) {
        return clientRatingRepository.findById(id);
    }

    @Override
    public List<Rating> findAllRating() {
        return (List<Rating>) clientRatingRepository.findAll();
    }
}