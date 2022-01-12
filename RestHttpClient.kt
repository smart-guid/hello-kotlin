package com.smartguid.web.api.http

import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.client.RestTemplate


public class RestHttpClient {

    private val retryTemplate: RetryTemplate? = null


    protected operator fun <T> get(
        restTemplate: RestTemplate, url: String?, httpEntity: HttpEntity<*>?,
        responseType: Class<T>?, vararg urlVariables: Any?
    ): T? {
        val responseEntity: ResponseEntity<T> = retryTemplate
            .execute { context ->
                restTemplate.exchange(
                    url!!, HttpMethod.GET, httpEntity,
                    responseType, *urlVariables
                )
            }
        return responseEntity.body
    }

}