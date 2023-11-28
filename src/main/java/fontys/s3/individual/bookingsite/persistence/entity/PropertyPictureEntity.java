package fontys.s3.individual.bookingsite.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Table(name = "property_picture")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PropertyPictureEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "property_id")
    @ManyToOne
    @JsonIgnore
    private PropertyEntity propertyEntity;

    @Column(name = "photo")
    private String photo;




}
