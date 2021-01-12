package com.itransition.util

//this class to wrap a Network Responses to Differentiate an Error and Succsess response
//sealed mean that only mentioned 3 classes could enharite it(kind of abstract class)
sealed class Resource<T>(
    //the body of response
    val data: T? = null,
    //error message for example
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T> : Resource<T>()
}