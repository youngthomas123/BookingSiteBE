package fontys.s3.individual.bookingsite.persistence.entity;
import jakarta.persistence.*;
@Entity
@Table(name = "landlord")
@PrimaryKeyJoinColumn(name = "landlord_id")
public class LandLordEntity extends UserEntity
{
    private boolean isSuperHost;
}
