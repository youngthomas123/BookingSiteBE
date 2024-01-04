package fontys.s3.individual.bookingsite.persistence.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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


    @JoinColumn(name ="landlord_id")
    @ManyToOne
    @JsonIgnore
    private UserEntity userEntity;

    private String mainPhoto;

    private boolean isEnlisted;



}
