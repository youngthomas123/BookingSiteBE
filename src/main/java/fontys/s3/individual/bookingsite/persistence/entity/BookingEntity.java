package fontys.s3.individual.bookingsite.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "booking")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name ="property_id")
    @ManyToOne
    private PropertyEntity propertyEntity;

    @JoinColumn(name = "tenant_id")
    @ManyToOne
    private TenantEntity tenantEntity;

    @Column(name = "checkIn")
    @Temporal(TemporalType.DATE) // For java.sql.Date
    private LocalDate checkIn;


    @Column(name = "checkOut")
    @Temporal(TemporalType.DATE) // For java.sql.Date
    private LocalDate checkOut;

}
