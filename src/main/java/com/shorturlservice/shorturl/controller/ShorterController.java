package com.shorturlservice.shorturl.controller;


import com.shorturlservice.shorturl.model.CodeGenerator;
import com.shorturlservice.shorturl.model.ShorterUrl;
import com.shorturlservice.shorturl.repository.ShorterRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.net.URLDecoder;
import java.security.Principal;
import java.time.ZonedDateTime;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping
@CrossOrigin
public
class ShorterController {
    Logger logger = LoggerFactory.getLogger(ShorterController.class.getSimpleName());

    private final ShorterRepository repository;
    private CodeGenerator codeGenerator;
    @Value("${shorter.length}")
    private Integer shorterLength;

    @Autowired
    public ShorterController(final ShorterRepository repository) {
        this.repository = repository;
        this.codeGenerator = new CodeGenerator();
    }

    @PostMapping(path = "/", consumes = APPLICATION_JSON_VALUE)
    public ShorterUrl createShortUrl(@RequestBody ShorterUrl shorter) {
        String hash = codeGenerator.generate(shorterLength);
        logger.info(hash);
        if (shorter != null) {
            String shorterString = URLDecoder.decode(shorter.getOriginalUrl());
            logger.info(shorterString);
            shorter = new ShorterUrl(null, hash, shorterString, ZonedDateTime.now());
            return repository.save(shorter);
        } else {
            return null;
        }
    }

    @GetMapping(path = "/{hash}")
    public ResponseEntity redirectShorter(@PathVariable("hash") String hash) {

        logger.info(hash);
        ShorterUrl shorter = repository.findByHash(hash);
        if (shorter != null) {
            HttpHeaders headers = new HttpHeaders();
            headers.add("Location", shorter.getOriginalUrl());
            return new ResponseEntity<String>(headers, HttpStatus.FOUND);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping(path =  "/callback")
    public ResponseEntity getAll() {

        return ResponseEntity.ok(repository.findAll());
    }

    @DeleteMapping(path = "/delete/all")
    public void  deleteAll(){
        repository.deleteAll();
    }

}
