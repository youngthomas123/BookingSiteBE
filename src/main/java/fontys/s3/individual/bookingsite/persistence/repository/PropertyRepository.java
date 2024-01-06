package fontys.s3.individual.bookingsite.persistence.repository;

import fontys.s3.individual.bookingsite.persistence.entity.PropertyEntity;

import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity,Long>
{
    //a method to get the paginated filtered properties
    //0 = false, 1 = true

    @Query(value = "SELECT * FROM property WHERE id NOT IN (SELECT property_id FROM booking WHERE " +
            "(check_in <= :checkin AND check_out >= :checkout) " +
            "OR (check_in BETWEEN :checkin AND :checkout) " +
            "OR (check_out BETWEEN :checkin AND :checkout)) " +
            "AND location LIKE %:location% " +
            "AND is_enlisted = 1", nativeQuery = true)
    Page<PropertyEntity> findPaginatedByLocationAndAvailability(String location,String checkin, String checkout, Pageable pageable);



    List<PropertyEntity> findByUserEntityId(Long userId);

    //to check if landlord actually owns the property
    Optional<PropertyEntity> findByIdAndUserEntity(Long propertyId, UserEntity userEntity);










}
