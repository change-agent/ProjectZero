# Project Zero - README #

## What is this repository for? ##

Quick summary

* Project Zero

Version

* Prototype

[Learn Markdown](https://bitbucket.org/tutorials/markdowndemo)

## How do I get set up? ##

### * Summary of set up ###

For initial retrieval of the project, there are a few requirements that are needed to be completed.

1. Firstly, prepare Eclipse for LibGDX development.

2. Secondly, set up Android SDK with appropriate API.

3. Thirdly, pull this repository onto your local machine.

4. Lastly, compile and test the project.



### * Configuration ###

**Configure Eclipse**

Install Eclipse 4.4 (Luna)

With the following extensions from Eclipse marketplace:

* Gradle Integration for Eclipse (4.4) 3.6.0.RELEASE
* Android Development Tools for Eclipse


**Android SDK**

[Android SDK](http://developer.android.com/sdk/installing/index.html), you only need the SDK, not the ADT bundle, which includes Eclipse. 

With the following tools and API:

* Android SDK Tools (Rev. 23.0.2)

* Android SDK Platform-tools (Rev. 20)

* Android SDK Build-tools (Rev. 20)

* Android 4.4.2 (API 19) - SDK Platform

* Android Support Library (Rev. 20)

* Google USB Driver

### * Deployment instructions ###

**Pull repository onto your local machine:**

Windows: Install Git and execute the following command via Git Bash.

Linux: Install Git through the repository and execute the following command via terminal.

Command: *git clone https://*username*@bitbucket.org/tersun/project-zero.git*


**Open the project via Eclipse**

From the Package Explorer view in Eclipse, right click onto blank space and select Import. 

Under Gradle, select Gradle Project and click Next.

Browse for the project and click Build Model.

Select the entire project by checking the checkbox and click Finish.

After the downloading and process of importing the Gradle project has completed the project should be accessible within Eclipse.

** Running the application/project **

Due to the nature of LibGDX, there are currently two methods of testing out the application; as a Desktop application and as a Android application.

From the imported project structure:

* project-zero
* ProjectZero-android
* ProjectZero-core
* ProjectZero-desktop

**For Android application:**

Right click onto ProjectZero-android and select Run As > Android Application. From the Android Device Chooser select a Android device or Android virtual device. And the application should launch.

**For Desktop Application:**

Right click onto ProjectZero-desktop and select Run As > Java Application. From the Select Java Application, select DesktopLauncher and click OK. The application should launch.

## Contribution guidelines ##

* Project overview
* Code contribution
* Other guidelines

## Who do I talk to? ##

* Repo owner or admin
* Other community or team contact