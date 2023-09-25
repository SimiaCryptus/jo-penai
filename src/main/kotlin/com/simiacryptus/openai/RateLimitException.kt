package com.simiacryptus.openai

class RateLimitException(
    val org: String?,
    val limit: Int,
    val delay: Long
) : AIServiceException("Rate limit exceeded: $org, limit: $limit, delay: $delay")
