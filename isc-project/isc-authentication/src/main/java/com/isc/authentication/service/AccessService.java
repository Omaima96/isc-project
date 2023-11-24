package com.isc.authentication.service;

import com.isc.authentication.model.Access;
import com.isc.authentication.repository.AccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Objects;


@Service
public class AccessService {

    @Autowired
    private AccessRepository accessRepository;


    public Page<Access> getAllAccessi(String filter, Pageable pageable) {

        if (Objects.nonNull(filter)) {
            return accessRepository.findByPhoneNumberOrSurnameOrEmail(filter, pageable);
        } else {
            return accessRepository.findAll(pageable);
        }
    }
}
