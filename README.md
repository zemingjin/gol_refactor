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

**Script syntext to run the UI app**
gol [seed file path] [-s] [-w[n]]
* **seed file path** - the complete path of the seed file such as 'src/main/resources/p138.seed'.
* **-s** - start with stepped evolution.  Space key should be used to advance to next generation.
* **-w** - set the speed - or waiting time - of evolution. The default is 200, meaning 200ms.  To make it 
faster, chose a smaller number, such as '-w50'.
The script is expected to be run in the project's home directory, and an executable jar was created prior by 
executing **mvn package assembly:single**.

**Keyboard Commands**
* **Ctrl + Space** - toggle between stepped or continued evolutions.
* **Space** - switch into stepped evolution when in continued mode; or trigger a new generation when in
 stepped mode.
* **Esc** - exit the app.

