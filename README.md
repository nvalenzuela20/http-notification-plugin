
# Release 3.2.6-20200430        

# http-notification-plugin

This is a simple http notification plugin for rundeck.

## Building
    
    mvn install
    
    target/http-notification-0.0.1-SNAPSHOT

    Note: for more information got to the following URL: https://maven.apache.org/download.cgi

## Install

    download the "Rundeck Launcher"(http://rundeck.org/downloads.html)

    cp target/http-notification-3.2.6-20200430 ${rundeck_home}/libext

## Run rundeck

    java -jar rundeck-launcher-2.11.9.jar