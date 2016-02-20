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
	    compile 'com.ohelshem:api:0.1.1'
	}
```


## Api

### Prerequisites
In order to use the API, you should implement few interfaces that the API is based on:

### `ApiDatabase`
The API uses this class in order to store all the data.

* `serverUpdateDate: long` - The date value in the server on last update.
* `changesDate: long` - The date that the saved changes are valid for.
* `updateDate: long` - The date value in the client on last update.

___

* `userData: UserData` - User's private info provided by the API.
* `timetable: Hour[][]` - The user's timetable.

___

* `changes: List<Change>` - The changes for the User's layer for the day.
* `tests: List<Test>` - User's tests for the year.
* `messages: List<Message>` - The messages the user has received.

**Note:** All dates are in Epoch time.

### `ColorProvider`
A change by itself doesn't have a color. Instead, a filter is being applied on the change
to set its color.

The API provide a default color provider, with receives a default Color and a mapping between 
name and a color. It will use `Contains` with the String.

```java
ApiFactory.defaultColorProvider(defaultColor: Int, filters: List<Pair<Int, String>> | Map<String, Int>)

```

## Create the controller
First, create the controller:

```java
ApiController apiController = ApiFactory.create(apiDatabase, colorProvider)
apiController.setNetworkAvailabilityProvider(new Function0<Boolean>() {
            @Override
            public Boolean invoke() {
                return true; // Check if network is available, true for available
            }
});
```

Second, attach a callback:

```java
apiController.setCallback(id, new ApiController.Callback() {
            @Override
            public void onSuccess(@NotNull List<? extends ApiController.Api> apis) {
                // the list contains all the apis that were updated.
                // enum Api { Changes, Messages, Tests, Timetable, UserData }
            }

            @Override
            public void onFail(@NotNull UpdateError error) {

            }
        });
```

`Id` is an int, id for the callback.

**Note:** in order to read the data, work with `ApiDatabase`. `ApiController` store its data there automatically.

Third, set the user data:

```java
apiController.setAuthData(new AuthData(userId, userPassword));
```

Now, Just call `apiController.update()`.

**Note:** If you want to update for login, it is better to use `apiController.login()` which also clear the server update time.

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
val apiController = ApiFactory.create(apiDatabase, colorProvider)

apiController.setNetworkAvailabilityProvider { true }
apiController[callbackId] = object : Callback { /* Implement it */ }
apiController.authData = AuthData(userId, userPassword)

apiController.update()
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
