package com.example.arfashion.presentation.services

import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestBuilder = chain.request().newBuilder()
        //todo: get accesstoken from pref here

        //fixme: this is hard code
        val accessToken =
            "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJkYXRhIjoie1wiX2lkXCI6XCI2MGJlMjEwZDYwZmM4MTAwMTUzYzA2NzRcIixcIm5hbWVcIjpcIlbDtSBUaGFuaCBQaG9uZ1wiLFwiZW1haWxcIjpcInBob25ndnRAYWxpdmUudm5cIixcInBob25lXCI6bnVsbCxcImF2YXRhclwiOm51bGwsXCJnZW5kZXJcIjpudWxsLFwiYmlydGhkYXlcIjpudWxsLFwicmVmcmVzaF90b2tlblwiOntcInRva2VuXCI6bnVsbCxcImlhdFwiOm51bGwsXCJleHBcIjpudWxsfSxcImFjY291bnRfc3RhdHVzXCI6XCJibG9ja1wiLFwiY2FydFwiOntcIl9pZFwiOlwiNjBiZTIxMGQ2MGZjODEwMDE1M2MwNjczXCIsXCJ0b3RhbFwiOjB9LFwiZmF2b3VyaXRlc1wiOltdLFwiYWRkcmVzc1wiOltdfSIsImlhdCI6MTYyMzA3MzE0NCwiZXhwIjoxNjI1NjY1MTQ0fQ.66qERoEY6lD1FMyGEgu9hW-Z4iKqQhid4d7LLHzA_PA"
        requestBuilder.addHeader("Authorization", "Bearer $accessToken")

        return chain.proceed(requestBuilder.build())
    }

}