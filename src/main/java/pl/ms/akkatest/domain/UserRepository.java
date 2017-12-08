package pl.ms.akkatest.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.RestResource;

import java.util.Collection;

/*
 * Created by Marcin on 2017-10-31 12:55
 */
@RepositoryRestResource
public interface UserRepository extends JpaRepository<User, Long> {

    @RestResource(path = "by-name")
    Collection<User> findByName(@Param("name") String name);

}
