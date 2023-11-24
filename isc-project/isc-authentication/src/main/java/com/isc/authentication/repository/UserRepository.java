package com.isc.authentication.repository;


import com.isc.authentication.model.User;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email) throws UsernameNotFoundException;

    public Optional<User> findByEmailAndEnabled(String email, boolean enabled);

    public Optional<User> findByUuid(String uuid);

    public Optional<User> findByRegistryIdAndEnabled(Long idRegistry, boolean enabled);

    @Query("SELECT a FROM User a WHERE  a.email like '%'||:filter||'%'  or a.name like '%'||:filter||'%'  or a.surname like '%'||:filter||'%' ")
    Page<User> searchByEmail(@Param("filter") String filter, Pageable pageable);

}
