# Honor Risk Detect Kit Sample Code (Android)
[![Apache-2.0](https://img.shields.io/badge/license-Apache-blue)](http://www.apache.org/licenses/LICENSE-2.0)
[![Open Source Love](https://img.shields.io/static/v1?label=Open%20Source&message=%E2%9D%A4%EF%B8%8F&color=green)](https://developer.hihonor.com/demos/)
[![Java Language](https://img.shields.io/badge/language-java-green.svg)](https://www.java.com/en/)

English | [中文](README_ZH.md)

## Contents

 * [Introduction](#Introduction)
 * [Preparations](#Preparations)
 * [Environment Requirements](#Environment-Requirements)
 * [Hardware Requirements](#Hardware-Requirements)
 * [Installation](#Installation)
 * [Technical Support](#Technical Support)
 * [License](#License)

## Introduction

In this sample code, you will use the created demo project to call APIs of Risk Detect Kit. Through the demo project, you will:
1.	Touch Detection: Detect the behavior of fake users simulating touch screens.
2.	Device Health Detection：Comprehensive detection of system health status such as system file tampering, ROOT, and kernel damage.
3.	Fraud App Detection in pre-order usages: Detect whether the preceding application on the path of pulling up the application is a fraudulent application.
4.	Anti-fraud Detection: Detect fraud risk behaviors on the device and return characteristic factors for comprehensive judgment based on the developer's in-application characteristics. The anti-fraud detection capability provides developers’ applications with a reference value for users’ fraud risks and identifies the possibility of users’ fraud risks.
5.	Application Identification: Use a business certificate stored in the cloud that is not associated with the device to sign the application installation package information. The application verifies the signature result and compares the information to achieve application integrity verification.

For more information, please refer to [Service Introduction](https://test.developer.honor.com/cn/docs/11011/guides/introduction).

## Environment Requirements

Android targetSdkVersion 29 or later and JDK 1.8.211 or later are recommended.

## Hardware Requirements

A computer (desktop or laptop) running Windows 10 or Windows 7

A Honor phone with a USB data cable, which is used for debugging

## Preparations
1.	Register as a Honor developer.
2.	Create an app and start APIs.
3.	Import your demo project to Android Studio (Chipmunk | 2021.2.1) or later. Download the **mcs-services.json** file of the app from [Honor Developer Site](https://developer.honor.com/en/), and add the file to the app-level directory of your project. Generate a signing certificate fingerprint, add the certificate file to your project, and add the configuration to the *build.gradle* file. For details, please refer to the [integration preparations](https://developer.hihonor.com/xxx).


## Installation
* Method 1: Compile and build the APK in Android Studio. Then, install the APK on your phone and debug it.
* Method 2: Generate the APK in Android Studio. Use the Android Debug Bridge (ADB) tool to run the **adb install {*YourPath/YourApp.apk*}** command to install the APK on your phone and debug it.

## Technical Support

If you have any questions about the sample code, try the following:
- Visit [Stack Overflow](https://stackoverflow.com/questions/tagged/honor-developer-services?tab=Votes), submit your questions, and tag them with `honor-developer-services`. Honor experts will answer your questions.

If you encounter any issues when using the sample code, submit your [issues](https://github.com/HONORDevelopers/Risk-Detect-demo/issues) or submit a [pull request](https://github.com/HONORDevelopers/Risk-Detect-demo/pulls). 

## License
The sample code is licensed under [Apache License 2.0](http://www.apache.org/licenses/LICENSE-2.0).