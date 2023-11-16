package fontys.s3.individual.bookingsite.persistence.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Entity
@Table(name = "property")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    private String description;

    private String name;

    private String location;

    private double pricePerNight;

    @OneToOne
    @JoinColumn(name ="landlord_id")
    private LandLordEntity landLordEntity;
}
