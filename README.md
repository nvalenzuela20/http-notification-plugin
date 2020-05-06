
# Release 1.0.release       

# http-notification-plugin

This is a simple http notification plugin for rundeck.

## Building
    
    mvn install
    
    Note: for more information, please got to the following URL: <a href="https://maven.apache.org/download.cgi">https://maven.apache.org/download.cgi</a> 

## Install

    download the "Rundeck Launcher"(<a href="http://rundeck.org/downloads.html">http://rundeck.org/downloads.html</a> )

    cp target/http-notification-1.0.release ${rundeck_home}/libext

## Run rundeck

    java -jar rundeck-launcher-3.2.6-20200427.jar
