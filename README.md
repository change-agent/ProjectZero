### Imported from [Bitbucket](https://bitbucket.org/dbmasters/project-zero-group-10)
- - - 
# Project Zero - README #

## What is this repository for? ##

Quick summary

* Project Zero -- Android game (Group 10)

Version

* Prototype

## How do I get set up? ##

### * Summary of set up ###

For initial retrieval of the project, there are a few requirements that are needed to be completed.

1. Firstly, prepare Eclipse for LibGDX development.

2. Secondly, set up Android SDK with appropriate API.

3. Thirdly, pull this repository onto your local machine.

4. Import this project as a gradle project. See deployment instructions below.

5. If building on windows: 
    1. Right click "ProjectZero - core" in the package explorer. 
    2. Select "Build Path > Configure Build Path" then go to "Libraries"
    3. Click "Add JARs..." and go to core "ProjectZero-core/lib" and select all the files in this directory.
    4. Click "OK" and then "OK" again at the top level.

6. If building for android:
    1. Right click "ProjectZero - core" in the package explorer. 
    2. Select "Build Path > Configure Build Path" then go to "Libraries"
    3. Click "Add JARs..." and go to core "ProjectZero-core/lib" and select all the files in this directory.
    4. Click "OK" and then "OK" again at the top level.
    5. Click over to "Order and export" and make sure each of the newly added jar files are ticked. Click "OK" to save.

7. Lastly, compile and test the project. If on windows right click the desktop project folder and run as java application. If on android, right click the android project folder and run as android application. You'll need to set up your phone in developer mode or have a virtual device running from eclipse.

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

Command: *git clone https://*username*@bitbucket.org/tersun/project-zero-group-10.git*


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

## Testing Framework ##

From the imported project structure:

* project-zero
* ProjectZero-android
* ProjectZero-core
* ProjectZero-desktop

**For Desktop Application:**

Right click onto ProjectZero-desktop and select Run As > Java Application. From the Select Java Application, select TestDesktopLauncher and click OK. The test cases should run and the results will be outputted to the Eclipse console.

## Contribution guidelines ##

* Project overview
* Code contribution
* Other guidelines

## Who do I talk to? ##

* Repo owner or admin
* Other community or team contact
