package com.simiacryptus.jopenai.exceptions

import java.io.IOException

class RequestOverloadException(message: String = "That model is currently overloaded with other requests.") :
    IOException(message)