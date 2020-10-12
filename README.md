![photo](media/image_fetcher.png)

# Image Fetcher Application


**Image Fetcher** is a sample Android application, built to demonstrate use of *Modern Android development* tools.



## About
It simply searches images. You can choose image categories from different section or you can enter search string to find the right image for you. It loads searched **Images** data from [Imgur API](https://apidocs.imgur.com/?version=latest) and displays in a Grid format.
The user can tap on the image which opens the next screen where user can see total likes, dislikes, score and description associated with that image.
Application supports lazy loading of orders so users can have seamless data loading experience. User can also apply filter to include viral and non-viral images.
**Implemented Lifecycle aware view holder for order list which respects the lifecycle of row's view components.**


## Built With 
- [Kotlin](https://kotlinlang.org/) - First class and official programming language for Android development.
- [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - For asynchronous operation.
- [Flow](https://kotlin.github.io/kotlinx.coroutines/kotlinx-coroutines-core/kotlinx.coroutines.flow/-flow/) - A cold asynchronous data stream that sequentially emits values and completes normally or with an exception.
- [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Collection of libraries that help you design robust, testable, and maintainable apps.
    - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - Data objects that notify views when the underlying database changes.
    - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - Stores UI-related data that isn't destroyed on UI changes.
    - [Navigation](https://developer.android.com/guide/navigation) - Navigation allows seamless navigation across app.
- [Dependency Injection](https://developer.android.com/training/dependency-injection) 
    - [Hilt-Dagger](https://dagger.dev/hilt/) - Standard library to incorporate Dagger dependency injection into an Android application.
    - [Hilt-ViewModel](https://developer.android.com/training/dependency-injection/hilt-jetpack) - DI for injecting `ViewModel`.
- [Retrofit](https://square.github.io/retrofit/) - A type-safe HTTP client for Android and Java.
- [Material Components for Android](https://github.com/material-components/material-components-android) - Modular and customizable Material Design UI components for Android.
- [MockK](https://mockk.io/) - Mocking library for Kotlin.
- [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) - The Paging Library helps you load and display pages of data from a larger dataset from local storage or over the network.
- [Lottie](https://airbnb.io/lottie/#/) - Animation library for Kotlin.


## Architecture
This app uses [***MVVM (Model View View-Model)***](https://developer.android.com/jetpack/docs/guide#recommended-app-arch) architecture.
![photo](media/arch.png)

The philosophy behind this architecture is to enable separation of concerns and ultimately a very modular application.
This architecture was chosen because it provides the opportunity to separate concerns and implement "SOLID" principles which makes the application more robust and the code base scalable and maintainable.

This due separation of moving parts makes the app more testable with unit tests.


## Quality 
- [LeakCanary](https://square.github.io/leakcanary/) - LeakCanary is a memory leak detection library for Android.
- [StrictMode](https://developer.android.com/reference/android/os/StrictMode) - StrictMode is a developer tool that detects things you might be doing by accident and brings them to your attention so you can fix them.
- [Firebase Test Lab](https://firebase.google.com/docs/test-lab) - Firebase Test Lab is a cloud-based app-testing infrastructure.
- [Firebase Crashlytics](https://firebase.google.com/docs/crashlytics) - Firebase Crashlytics is a lightweight, realtime crash reporter that helps you track, prioritize, and fix stability issues that erode your app quality.
- [Firebase Performance Monitoring](https://firebase.google.com/docs/perf-mon) - Gain insight into your app's performance issues.

## Static code analysis
Used [Sonar Lint](https://www.sonarlint.org/) static code analysis.


## Contact
If you need any help, you can connect with me.

Visit:- [devmanishpatole.com](https://www.devmanishpatole.com/)

## License

```
MIT License

Copyright (c) 2020 Manish Patole

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
