# AutoCrashReporter

ACR is an android library to simplify crash detection while your app is under development, app tester/user can easily send the crash report to developer that will help him to fix the issue.

##Demonstration
![ACR Demo](/assets/acr_demo.gif)

##Dependency
```
- Add the dependencies to your app level build.gradle file:

####Gradle
```gradle
repositories {
   maven {
       url  "http://dl.bintray.com/ideal/maven"
   }
}

dependencies {
    compile 'com.devs:acr:1.0.0'
}
```
####Maven
```xml
<dependency>
<groupId>com.devs</groupId>
<artifactId>acr</artifactId>
<version>1.0.0</version>
<type>pom</type>
</dependency>
```
```
##Usage
- Inside your Application:
```java
@Override
public void onCreate() {
     super.onCreate();
     AutoErrorReporter.get(this)
          .setEmailAddresses("yourdeveloper@gmail.com")
          .setEmailSubject("Auto Crash Report")
          .start();
}

```

## License
```
Copyright 2016 Deven Singh

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