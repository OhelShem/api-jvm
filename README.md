[![Kotlin](https://img.shields.io/badge/kotlin-1.0.0-blue.svg)](http://kotlinlang.org) [![License Apache](https://img.shields.io/badge/License-Apache%202.0-red.svg)](http://www.apache.org/licenses/LICENSE-2.0)

Ohel Shem API for JVM
=====================
Ohel Shem (Hebrew: אהל שם‎) is an Israeli high school located in the city of Ramat Gan. 
The school has about 1,550 students studying in 45 classes, from ninth to twelfth grade, and about 160 teachers and 40 workers.

![Ohel Shem Logo](http://i.imgur.com/Yy1Z5aX.png)

## Ohel Shem :heart: OSS
This project is part of 'Ohel Shem OSS', our attempt at providing open standard for modern school.

## Maven dependency
**Step 1. Add this repository to your build file**
```groovy
repositories {
	    maven {
            url "http://dl.bintray.com/ohelshem/maven" 
        }
	}
```
**Step 2. Add the dependency in the form**
```groovy
dependencies {
	    compile 'com.ohelshem:api:0.2.0'
	}
```


## Api

### Prerequisites
In order to use the API, you should implement few interfaces that the API is based on:

### `ColorProvider`
A change by itself doesn't have a color. Instead, a filter is being applied on the change
to set its color.

The API provide a default color provider, with receives a default Color and a mapping between 
name and a color. It will use `Contains` with the String.

```java
ApiFactory.defaultColorProvider(defaultColor: Int, filters: List<Pair<Int, String>> | Map<String, Int>)

```

## Usage
First, create the provider:

```java
ApiProvider apiProvider = ApiFactory.create(colorProvider)
```

Second, call the `update()` method:

```java
apiProvider.update(new AuthData(userId, userPassword), lastUpdateTime, new ApiCallback() {
    @Override
       protected void onResult(Result<ApiParser.ParsedData, Exception> result) {
                                                                       
       }
});
```

The [Result library](https://github.com/kittinunf/Result) is a tiny framework for modelling success/failure of operations.

```java
ParsedData data = result.get() // null if error
Exception exception = result.component2() // null if valid

// Looks much better in Kotlin. Sorry :)
```


## Android support
In order to use this with Android, you need to add a gradle dependency for `fuel-android` module.

```groovy
repositories {
    jcenter()
}

dependencies {
    compile 'com.github.kittinunf.fuel:fuel-android:1.0.0'
}
```

This library is based on a library called [Fuel](https://github.com/kittinunf/Fuel) which need a plugin to work on Android as intended.

## Kotlin support
This library was written fully in [Kotlin](https://kotlinlang.org/). Kotlin is Statically typed programming language
for the JVM, Android and the browser with 100% interoperable with Java™.

```kotlin
val apiProvider = ApiFactory.create(colorProvider)

apiProvider(AuthData(userId, userPassword), lastUpdateTime) { result ->
    result.success {
        // Only called on success
    }
    result.failure {
        // only called on failure
    }
}
```

# License

```
Copyright 2016 Yoav Sternberg

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
