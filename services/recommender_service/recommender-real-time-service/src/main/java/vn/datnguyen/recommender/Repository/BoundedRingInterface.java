package vn.datnguyen.recommender.Repository;

import com.datastax.oss.driver.api.core.cql.SimpleStatement;

public interface BoundedRingInterface {
    SimpleStatement createRowIfNotExists();
    SimpleStatement addNewBoundedRing(int ringId, int centreId, double lbRange, double ubRange);
}
