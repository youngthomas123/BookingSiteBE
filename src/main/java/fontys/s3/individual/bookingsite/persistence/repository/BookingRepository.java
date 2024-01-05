package fontys.s3.individual.bookingsite.persistence.repository;

import fontys.s3.individual.bookingsite.persistence.entity.BookingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity,Long>
{
    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN TRUE ELSE FALSE END FROM BookingEntity b WHERE b.checkIn > :today OR b.checkOut > :today")
    boolean existsBookingsAfterToday(@Param("today") LocalDate today);

    @Query("SELECT CASE WHEN COUNT(b) > 0 THEN true ELSE false END " +
            "FROM BookingEntity b " +
            "WHERE b.propertyEntity.id = :propertyId " +
            "AND (" +
            "(b.checkIn BETWEEN :start AND :end) OR " +
            "(b.checkOut BETWEEN :start AND :end) OR " +
            "(b.checkIn <= :start AND b.checkOut >= :end)" +
            ")")
    boolean hasConflictingBookings(@Param("propertyId") Long propertyId,
                                   @Param("start") LocalDate start,
                                   @Param("end") LocalDate end);


}
