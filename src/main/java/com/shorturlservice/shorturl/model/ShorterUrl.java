package com.shorturlservice.shorturl.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "short_urls")
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class ShorterUrl {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Column
    private String hash;
    @Column(name = "original_url")
    private String originalUrl;
    @Column(name = "created_at", columnDefinition = "TIMESTAMP")
    private ZonedDateTime createdAt;
}
