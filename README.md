This project contains a Java implementation of Conway's Game of Life with a very crude UI. 
The application takes one parameter, the path to the seed file.

The format of the seed file could be like the followings:
00100
01110
01010
01110
00100


where 1 indicates a living cell.

execute: **mvn package assembly:single** - the executable jar would be created under the target directory, 
named in the form of 'gameoflife-[version]-jar-with-dependencies.jar'.