package fontys.s3.individual.bookingsite.persistence.repository;

import fontys.s3.individual.bookingsite.persistence.entity.PropertyEntity;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity,Long>
{
    //a method to get the paginated filtered properties
    @Query(value = "SELECT * FROM property WHERE id NOT IN (SELECT property_id FROM booking WHERE " +
            "(check_in <= :checkin AND check_out >= :checkout) " +
            "OR (check_in BETWEEN :checkin AND :checkout) " +
            "OR (check_out BETWEEN :checkin AND :checkout)) " +
            "AND location LIKE %:location%", nativeQuery = true)
    Page<PropertyEntity> findPaginatedByLocationAndAvailability(String location,String checkin, String checkout, Pageable pageable);





    //a method to get total number of filtered properties
    @Query(value = "SELECT count(*) as count FROM property WHERE id NOT IN (SELECT property_id FROM booking WHERE " +
            "(check_in <= :checkin AND check_out >= :checkout) " +
            "OR (check_in BETWEEN :checkin AND :checkout) " +
            "OR (check_out BETWEEN :checkin AND :checkout)) " +
            "AND location LIKE %:location%", nativeQuery = true)
    int getTotalCountOfFilteredProperties(String location,String checkin, String checkout);
}
