package fontys.s3.individual.bookingsite.persistence.repository;

import fontys.s3.individual.bookingsite.persistence.entity.PropertyPictureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PropertyPictureRepository extends JpaRepository<PropertyPictureEntity,Long>
{
    List<PropertyPictureEntity> findByPropertyEntityId(Long propertyId);

    void deleteAllByPropertyEntityId(long propertyId);





}
