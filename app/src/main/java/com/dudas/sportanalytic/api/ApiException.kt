package com.dudas.sportanalytic.api

import java.io.IOException

data class ApiException(val code: Int, val body: String) : IOException(body)