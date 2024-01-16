package fontys.s3.individual.bookingsite.persistence.repository;

import fontys.s3.individual.bookingsite.persistence.entity.PropertyEntity;

import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity,Long>
{
    //a method to get the paginated filtered properties





    @Query("SELECT p FROM PropertyEntity p " +
            "WHERE p.id NOT IN " +
            "(SELECT b.propertyEntity.id FROM BookingEntity b " +
            "WHERE " +
            "(:checkin BETWEEN b.checkIn AND b.checkOut " +
            "OR :checkout BETWEEN b.checkIn AND b.checkOut " +
            "OR b.checkIn BETWEEN :checkin AND :checkout " +
            "OR b.checkOut BETWEEN :checkin AND :checkout)) " +
            "AND p.location LIKE %:location% " +
            "AND p.isEnlisted = true")
    Page<PropertyEntity> findPaginatedByLocationAndAvailability(
            @Param("location") String location,
            @Param("checkin") LocalDate checkin,
            @Param("checkout") LocalDate checkout,
            Pageable pageable);




    List<PropertyEntity> findByUserEntityId(Long userId);

    //to check if landlord actually owns the property
    Optional<PropertyEntity> findByIdAndUserEntity(Long propertyId, UserEntity userEntity);


    @Modifying
    @Query("UPDATE PropertyEntity p SET p.isEnlisted = false WHERE p.userEntity = :targetUserEntity")
    void setAllPropertiesNotEnlistedForUser(@Param("targetUserEntity") UserEntity userEntity);















}
