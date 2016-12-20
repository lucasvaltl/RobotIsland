#Robot Island v.1.0 README

A game created by Geraint Ballinger and Lucas Valtl.

##TABLE OF CONTENTS

* Basic Info
* Requirements
* Getting Started
* Game Instructions
* Copyright

##Basic Info
Robot Island is a game where you use your arrow keys to move a robot around a racetrack and achieve the fastest time possible without running out of battery. 
It was initially created to fulfill the coursework requirements of the Introductory Programming course we are both taking as part of our Msc in Computer Science at UCL. 
However, it evolved into a fully working racing game, and we hope you have just as much fun playing it as we had creating it. Any contributions to this project are more than welcome.  
The project is under MIT license, see the license.txt file for details.

##Requirements

To run the Robot Island game, you must have the [Java SE Runtime Environment 8](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html) (version 8u111) or the Java Development Kit 8 installed.

For best performance, it is recommended to have more than 2GB of ram.

##Getting Started

Clone/Download the repository if you have not done so already. Hit that green button to do so. There are two ways to play the game:

The easy way: Just move the RobotIsland.jar (in the “easy installation” folder) file to your preferred location, double click it and have fun.

The hard way: Import the entire folder (the “hared but flexible installation” folder) into your IDE of choice (Eclipse definitely works, we can’t guarantee for anything else). The main class can be found in the main folder and is called driver.java. 
Running this class will enable you to play the game. This is a more complex way, but you will be able to change the code and tweak your robot to your liking. 
In addition, you can turn on dev mode, giving you more infos on the robot. Have some fun, maybe you’ll even contribute to this repository?


##Game Instructions

The main goal of the game is to move a differential drive robot anti-clockwise along a racetrack and achieve the fastest time possible without running out of battery.  
The robot is controlled using the up, down, left, and right arrow keys, which accelerate and rotate the robot accordingly.  
Every movement will cause the robot to lose battery, which may be recharged at the recharging station at the top of the map to prolong gameplay.
After recording a new lap, it is possible to get a ‘ghost’ of the high score by switching time trial mode on.  
This will cause a new robot to spawn every new lap and provide visual feedback on the speed of the current lap attempt.
If you are running the game from source, you can change the robot type by changing line 309 in uk.ac.ucl.robotisland.main from:

	robotType = “fast”;
to: 

	robotType = “slow”;


##Copyright

All of material below was used as is, no changes were made.

Soundtrack: Chibi ninja by [Eric Skiff](http://ericskiff.com/music/) is licensed under [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/).

Other sound effects:  Shut_Down1, Space_Alert3, Emerge4, Emerge9, Sweep1, Power_Up3  by [Morten](http://www.downloadfreesound.com/author/morten/) are licensed under [CC BY 1.0](https://creativecommons.org/licenses/by/1.0/). 

Font used in Game Logo: [“pixelated”](http://fontstruct.com/fontstructions/show/426637) by “Greenma201” is licensed under [CC BY SA 3.0](http://creativecommons.org/licenses/by-sa/3.0/).
