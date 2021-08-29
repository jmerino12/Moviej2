package com.jmb.data.source

interface LocationDataSource {
    suspend fun findLastRegion(): String?
}