package com.simiacryptus.jopenai.exceptions

class RateLimitException(
    val org: String?,
    val limit: Int,
    val delay: Long
) : AIServiceException("Rate limit exceeded: $org, limit: $limit, delay: $delay")
