package com.smartguid.web.api.github

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import kotlin.system.measureTimeMillis

@RestController
class GitHubController {

    @GetMapping("/test")
    fun index(): ApiModel {
        return ApiModel(1, "Chris")
    }

    @GetMapping("/repositories")
    fun repositories(): Array<GitHubRepositoryApiModel?>? {
        val url = "https://api.github.com/users/smart-guid/repos"
        println(url)
        var data : Array<GitHubRepositoryApiModel?>?
        val time = measureTimeMillis {
            data = try {
                val response = RestTemplate().getForEntity(url, Array<GitHubRepositoryApiModel?>::class.java)
                response.body
                //response.statusCode
            } catch (e: RestClientException) {
                //ToDo: log...
                null
            }
        }

        println("Elapsed ms: $time")

        return data
        //ToDo: log...

//        return Arrays.stream(data)
//           .map(GitHubRepositoryApiModel::getName)
//          .collect(Collectors.toList())
    }

//    500 Internal Server Error
//    502 Bad Gateway
//    503 Service Unavailable
//    504 Gateway Timeout

}