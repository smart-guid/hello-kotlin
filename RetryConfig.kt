package com.smartguid.web.api.http

import org.springframework.classify.Classifier
import org.springframework.http.HttpStatus
import org.springframework.retry.RetryPolicy
import org.springframework.retry.policy.ExceptionClassifierRetryPolicy
import org.springframework.retry.policy.NeverRetryPolicy
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.retry.policy.SimpleRetryPolicy
import org.springframework.retry.support.RetryTemplate

public class RetryConfig {
    private val simpleRetryPolicy: SimpleRetryPolicy = SimpleRetryPolicy(MAX_RETRY_ATTEMPTS)
    private val neverRetryPolicy: NeverRetryPolicy = NeverRetryPolicy()

    fun retryTemplate(): RetryTemplate {
        val retryTemplate = RetryTemplate()
        val policy = ExceptionClassifierRetryPolicy()
        policy.setExceptionClassifier(configureStatusCodeBasedRetryPolicy())
        retryTemplate.setRetryPolicy(policy)
        return retryTemplate
    }

    private fun configureStatusCodeBasedRetryPolicy(): Classifier<Throwable, RetryPolicy> {
        return Classifier<Throwable, RetryPolicy> { throwable ->
            if (throwable is HttpStatusCodeException) {
                getRetryPolicyForStatus(throwable.statusCode)
            }
            simpleRetryPolicy
        }
    }

    private fun getRetryPolicyForStatus(httpStatus: HttpStatus): RetryPolicy {
        return when (httpStatus) {
            HttpStatus.BAD_GATEWAY,
            HttpStatus.SERVICE_UNAVAILABLE,
            HttpStatus.INTERNAL_SERVER_ERROR,
            HttpStatus.GATEWAY_TIMEOUT -> simpleRetryPolicy
            else -> neverRetryPolicy
        }
    }

    companion object {
        private const val MAX_RETRY_ATTEMPTS = 2
    }
}