package com.shorturlservice.shorturl.controller

import com.shorturlservice.shorturl.model.ShorterUrl
import com.shorturlservice.shorturl.repository.ShorterRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.boot.web.server.LocalServerPort
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.util.UriComponentsBuilder
import spock.lang.Specification

import java.time.ZonedDateTime


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShorterControllerTest extends Specification {

    @Autowired
    ShorterRepository shorterRepository;
    @Autowired
    private TestRestTemplate restTemplate;
    @LocalServerPort
    private int port;


    def "CreateShortUrl"() {

        given:

        def countUrlsInDB = shorterRepository.findAll().size()
        def baseURI = "http://localhost:$port/"
        def shortUrl = new ShorterUrl()
        shortUrl.setCreatedAt(ZonedDateTime.now())
        shortUrl.setOriginalUrl("https://howtodoinjava.com")
        def URI uri = new URI(baseURI)
        HttpHeaders headers = new HttpHeaders()
        headers.set("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIxbkJjNUx3Z1NocVdGbzkyT2lCUWtkWTQ0SlkxQV9TN1lWcW9ZbWpKdktjIn0.eyJleHAiOjE2Mjc1NjUxMTIsImlhdCI6MTYyNzU2NDgxMiwiYXV0aF90aW1lIjoxNjI3NTY0Nzg4LCJqdGkiOiI1NzVhMTZlMi0zYTY1LTQwNWItODYwNi00ZDMyODVhZmVmYTkiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvYXV0aC9yZWFsbXMvYXBwc2RldmVsb3BlciIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI0YTljNGUxYy1jNWY0LTRjNDUtYTU1ZC1kZjhkYTMwNTE3YTUiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ1cmwtc2hvcnQiLCJzZXNzaW9uX3N0YXRlIjoiM2QyZTRmYzEtODI2Yy00Y2YzLTljNzQtZmQyM2IzZDZlOGIyIiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsImRlZmF1bHQtcm9sZXMtYXBwc2RldmVsb3BlciIsInVtYV9hdXRob3JpemF0aW9uIiwidXNlciJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJTZXJnZXkgUG9seWFrb3YiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJzZXJnZXkxMjMiLCJnaXZlbl9uYW1lIjoiU2VyZ2V5IiwiZmFtaWx5X25hbWUiOiJQb2x5YWtvdiIsImVtYWlsIjoic2VyZ2V5dGIxOTkxQGdtYWlsLmNvbSJ9.owWChg3XrkBrbANfPP_Ob1M32foXLRuLvxw8VzjxTTSgJSwnSclf4HRY8T_YltEik1jXvtE8j10OysSUv2czXROK7zpm3hahX3oqfij83J6sk62pyCQ7ZCSPusgNWpjdrk2HrI2b-v1jRNhw4TeA3zIsrVuI9fpkPuNk3udL2xxHI51Uxp56WfmLSl1c-JBSLNOnZqE8RLKBJj8Pw06U_jYEVxpw92mgyMkti9u6pZs3dpAGADARJS9crUw9Gn-Tr40ROOx3yZ-BZxIg9rVZzY9_eWksDT1QmqncpdxlGm9qeK32IJXrdV_q1usxqiyA0kN1Y-xZk1-eGHgDu97jjg")
        HttpEntity<ShorterUrl> request = new HttpEntity<>(shortUrl, headers)

        when:
        ResponseEntity<ShorterUrl> result = this.restTemplate.postForEntity(uri, request, ShorterUrl.class);

        then:
        result.statusCodeValue == 200
        result.getBody().getOriginalUrl() == "https://howtodoinjava.com"
        countUrlsInDB +1 == shorterRepository.findAll().size()
    }

    def "RedirectShorter"() {
        given:
        HttpHeaders headers = new HttpHeaders()
        headers.set("Authorization", "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICIxbkJjNUx3Z1NocVdGbzkyT2lCUWtkWTQ0SlkxQV9TN1lWcW9ZbWpKdktjIn0.eyJleHAiOjE2Mjc1NjkwMDIsImlhdCI6MTYyNzU2ODcwMiwiYXV0aF90aW1lIjoxNjI3NTY4NjkwLCJqdGkiOiI4YjBjNzk1ZS1hOGExLTRhYzQtYTA1OC1kMmNlNDJjMGU4MjMiLCJpc3MiOiJodHRwOi8vbG9jYWxob3N0OjgwODAvYXV0aC9yZWFsbXMvYXBwc2RldmVsb3BlciIsImF1ZCI6ImFjY291bnQiLCJzdWIiOiI0YTljNGUxYy1jNWY0LTRjNDUtYTU1ZC1kZjhkYTMwNTE3YTUiLCJ0eXAiOiJCZWFyZXIiLCJhenAiOiJ1cmwtc2hvcnQiLCJzZXNzaW9uX3N0YXRlIjoiMjAyYmE3OWYtYzUwNC00ZTg4LThkY2MtNTE5NmRlZTIxMzI2IiwiYWNyIjoiMSIsInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJvZmZsaW5lX2FjY2VzcyIsImRlZmF1bHQtcm9sZXMtYXBwc2RldmVsb3BlciIsInVtYV9hdXRob3JpemF0aW9uIiwidXNlciJdfSwicmVzb3VyY2VfYWNjZXNzIjp7ImFjY291bnQiOnsicm9sZXMiOlsibWFuYWdlLWFjY291bnQiLCJtYW5hZ2UtYWNjb3VudC1saW5rcyIsInZpZXctcHJvZmlsZSJdfX0sInNjb3BlIjoib3BlbmlkIHByb2ZpbGUgZW1haWwiLCJlbWFpbF92ZXJpZmllZCI6ZmFsc2UsIm5hbWUiOiJTZXJnZXkgUG9seWFrb3YiLCJwcmVmZXJyZWRfdXNlcm5hbWUiOiJzZXJnZXkxMjMiLCJnaXZlbl9uYW1lIjoiU2VyZ2V5IiwiZmFtaWx5X25hbWUiOiJQb2x5YWtvdiIsImVtYWlsIjoic2VyZ2V5dGIxOTkxQGdtYWlsLmNvbSJ9.dtdMxU2RyQWekuLGtbJfVBZYjAkbymDM8pPlj0d2zXmax6GdClXAQxyCRX_xW3nOjqy6z_zJVn3GmAIBVOWtEE-7AWiydpr6agG1aWYpaJi7dlbIo_aKl0Owe0TVe1kaHw0ou-724lSP1kIN0aWi6s55iSa2iK4sdsaoTL_UnOAFhUtKqqEGfOegUECVlgUh8qpajkhIv7kg5zYZvvz9Q2W0SiytCM-podLpNGZlgLpe65DeGTqBauhdN-0m98Qq8c6NdcLPkpMAKzP34yULaWIcdWONiIHWbFq4S2IyNT5nyNmU4Zfc6oLr0_qzxw0vrJ5rxkyZLyLv6L-SxN7Bgg")
        HttpEntity<?> request = new HttpEntity<>(headers)
        UriComponentsBuilder builder  = UriComponentsBuilder.fromHttpUrl("http://localhost:$port/hashes/Sj0gMs")
        .queryParam("hash","Sj0gMs");

        when:
        ResponseEntity<?> result = restTemplate.exchange(builder.toUriString(), HttpMethod.GET,request,String.class);
        then:
        result.statusCodeValue == 302

    }


}
