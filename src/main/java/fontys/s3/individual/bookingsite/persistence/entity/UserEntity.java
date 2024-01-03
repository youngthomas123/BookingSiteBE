package fontys.s3.individual.bookingsite.persistence.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotBlank
    @Length(min = 2, max = 20)
    @Column(name = "username", unique = true)
    private String username;

    @NotBlank
    @Column(name = "password")
    private String password;

    @Column(name = "email")
    @Length(min = 2, max = 30)
    @Email
    private String email;

    @Column(name = "phone_number")
    @Length(min = 3, max = 15)
    private String phoneNumber;

    @Column(name = "birthday")
    private LocalDate birthday;

    @Length(min = 0, max = 200)
    @Column(name = "bio")
    private String bio;

    @Column(name = "date_created")
    @CreationTimestamp
    private LocalDateTime dateCreated;

    @Column(name = "type")
    @Length(max = 10)
    private String type;

    @Column(name = "profile_pic")
    private String profilePic;


}
