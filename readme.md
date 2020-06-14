[![Build Status](https://travis-ci.com/petruki/switcher-client.svg?branch=master)](https://travis-ci.com/petruki/switcher-client)
[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=switcher-client-java&metric=alert_status)](https://sonarcloud.io/dashboard?id=switcher-client-java)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.petruki/switcher-client.svg?label=Maven%20Central)](https://search.maven.org/search?q=g:%22com.github.petruki%22%20AND%20a:%22switcher-client%22)
[![Slack: Switcher-HQ](https://img.shields.io/badge/slack-@switcher/hq-blue.svg?logo=slack)](https://switcher-hq.slack.com/)

![Switcher API: Java Client: Cloud-based Feature Flag API](https://github.com/petruki/switcherapi-assets/blob/master/logo/switcherapi_java_client.png)

# About
Client Java for working with Switcher-API.
https://github.com/petruki/switcher-api

- Able to work offline using a snapshot file downloaded from your remote Switcher-API Domain.
- Silent mode automatically enables a contingent sub-process in case of connectivity issues.
- Built-in mock implementation for automated testing.
- Easy to setup. Switcher Context is responsible to manage all the complexity between your application and API.

# Usage

## Install  
- Using the source code `mvn clean install`
- Adding as a dependency - Maven
```xml
<dependency>
  <groupId>com.github.petruki</groupId>
  <artifactId>switcher-client</artifactId>
  <version>1.0.1</version>
</dependency>
```	

## Context properties
The context map properties stores all information regarding connectivity and strategy settings. These constants can be accessed using *SwitcherContextParam*.

```java
properties.put(SwitcherContextParam.URL, "https://switcher-load-balance.herokuapp.com");
properties.put(SwitcherContextParam.APIKEY, "API_KEY");
properties.put(SwitcherContextParam.DOMAIN, "MyDomain");
properties.put(SwitcherContextParam.COMPONENT, "MyApp");
properties.put(SwitcherContextParam.ENVIRONMENT, "default");
properties.put(SwitcherContextParam.SILENT_MODE, true);
properties.put(SwitcherContextParam.RETRY_AFTER, "5s");
properties.put(SwitcherContextParam.SNAPSHOT_LOCATION, "/src/resources");

SwitcherFactory.buildContext(properties, false);
Switcher switcher = SwitcherFactory.getSwitcher("FEATURE01");
switcher.isItOn();
```

- URL: Endpoint of your Swither-API.
- APIKEY: Switcher-API key generated after creating a domain.
- DOMAIN: Domain name.
- COMPONENT: Application name.
- ENVIRONMENT: Environment name. Production environment is named as 'default'.
- SILENT_MODE: (boolean) Activate contingency in case of some problem with connectivity with the API.
- RETRY_AFTER: Time given to the module to re-establish connectivity with the API - e.g. 5s (s: seconds - m: minutes - h: hours)
- SNAPSHOT_LOCATION: Set the folder location where snapshot files will be saved.
- SNAPSHOT_AUTO_LOAD: (boolean) Set the module to automatically download the snapshot configuration.

## Executing
There are a few different ways to call the API using the java library.
Here are some examples:

1. **No parameters**
Invoking the API can be done by obtaining the switcher object and calling *isItOn*. It can also be forced to call another key any time you want.

```java
Switcher switcher = SwitcherFactory.getSwitcher("FEATURE01");
switcher.isItOn();
//or
switcher.isItOn("FEATURE01");
```

2. **Strategy validation - preparing input**
Loading information into the switcher can be made by using *prepareEntry*, in case you want to include input from a different place of your code. Otherwise, it is also possible to include everything in the same call.

```java
List<Entry> entries = new ArrayList<>();
entries.add(new Entry(Entry.DATE, "2019-12-10"));
entries.add(new Entry(Entry.DATE, "2020-12-10"));

switcher.prepareEntry(entries);
switcher.isItOn();
//or
switcher.isItOn(entries);
```

3. **Strategy validation - all-in-one execution**
All-in-one method is fast and include everything you need to execute a complex call to the API. Stack inputs changing the last parameter to *true* in case you need to add more values to the strategy validator.

```java
switcher.isItOn("FEATURE01", new Entry(Entry.NETWORK, "10.0.0.3"), false);
```

## Offline settings
You can also force the Switcher library to work offline. In this case, the snapshot location must be set up and the context re-built using the offline flag.

```java
properties.put(SwitcherContextParam.SNAPSHOT_LOCATION, "/src/resources");
SwitcherFactory.buildContext(properties, true);

Switcher switcher = SwitcherFactory.getSwitcher("FEATURE01");
switcher.isItOn();
```

## Built-in mock feature
Write automated tests using this built-in mock mechanism to guide your test scenario according to what you want to test.
</br>*SwitcherExecutor* implementation has 2 methods that can make mock tests easier. Use assume to force a value to a switcher and forget to reset its original state.

```java
Switcher switcher = SwitcherFactory.getSwitcher("FEATURE01");

SwitcherExecutor.assume("FEATURE01", false);
switcher.isItOn(); // 'false'

SwitcherExecutor.forget("FEATURE01");
switcher.isItOn(); // Now, it's going to return the result retrieved from the API or the Snaopshot file
```

# Version Log
- 1.0.2: 
    - Improved performance when loading snapshot file.
    - Snapshot file auto load when updated.
    - Re-worked built-in mock implementation
- 1.0.1: Security patch - Log4J has been updated
- 1.0.0: Working release