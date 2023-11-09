package fontys.s3.individual.bookingsite.persistence.entity;

import jakarta.persistence.*;


@Entity
@Table(name = "tenant")
@PrimaryKeyJoinColumn(name = "tenant_id")
public class TenantEntity extends UserEntity
{

}
