package fontys.s3.individual.bookingsite.persistence.FakeRepositoryImp;

import fontys.s3.individual.bookingsite.persistence.UserRepository;
import fontys.s3.individual.bookingsite.persistence.entity.UserEntity;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
@Repository
public class FakeUserRepositoryImp implements UserRepository
{
    private static long NEXT_ID = 1;

    private final List<UserEntity> savedUsers;

    public FakeUserRepositoryImp()
    {
        this.savedUsers = new ArrayList<>();

        UserEntity a = UserEntity.builder()
                .id(1L)
                .email("a@gmail.com")
                .username("a")
                .password("password")
                .build();
        UserEntity b = UserEntity.builder()
                .id(2L)
                .email("b@gmail.com")
                .username("b")
                .password("12456")
                .build();

        savedUsers.add(a);
        savedUsers.add(b);

    }

    @Override
    public List<UserEntity> findAll()
    {
        return Collections.unmodifiableList(savedUsers);

    }
}
