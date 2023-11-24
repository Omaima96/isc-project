package com.isc.authentication.repository;

import com.isc.authentication.model.Access;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessRepository extends JpaRepository<Access, Long> {

    @Query("SELECT a from Access a where a.email like '%'||:filter||'%'  or a.phone_number like '%'||:filter||'%'  or a.surname like '%'||:filter||'%' ")
    Page<Access> findByPhoneNumberOrSurnameOrEmail(@Param("filter") String filter, Pageable pageable);
}