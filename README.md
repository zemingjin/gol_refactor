This project contains a Java implementation of Conway's Game of Life with a very crude UI. 
The application takes one parameter, the path to the seed file.

The format of the seed file could be like the followings:
```
#P 5|5
..O
.OOO
.O.O
.OOO
..O
```
the grid size is 5x5 and 'O' - the capital 'o' - indicates a living cell.

execute: **mvn package assembly:single** - the executable jar would be created under the target directory, 
named in the form of 'gameoflife-[version]-jar-with-dependencies.jar'.

This implementation emphasizes the non-grid approach. Only one list of active cells is maintained.
