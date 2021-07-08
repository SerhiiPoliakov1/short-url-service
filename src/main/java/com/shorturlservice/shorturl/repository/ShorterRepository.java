package com.shorturlservice.shorturl.repository;

import com.shorturlservice.shorturl.model.ShorterUrl;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShorterRepository extends JpaRepository<ShorterUrl,Long> {

    ShorterUrl findByHash(String hash);
}
