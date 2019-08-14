# Refactoring
* Definition - a change(s) made to the internal structure of software to make it: 
easier **to understand and to modify** without changing its **observable behavior**.

## Why
1. Evolving of software - routine maintenance to remain healthy.
2. Economics - Clean/easy to understand code allows fast deliveries - **60-70%** of time in reading.
3. Professionalism - "Any fool can write code that a computer can understand.
Good programmers write code that humans can understand."

## When - if code smells, refactor it.
1. Adding new feature: separation of implementation and refactoring.
2. "Comprehension Refactoring" - Code that is hard to understand - refactoring should start after understanding was reached
3. "Preparatory Refactoring" - Current design is hard to extend.
4. "Litter-Pickup Refactoring".  Many small changes could result much better code - such as active code review.
5. "Planned Refactoring" - no need in perfect world where refactoring is a continue process.

## How
1. Identify under/over-engineered code segment
2. Ensure adequate test - for anything more than basic refactoring - have faith about IDE's provided refactoring.
3. Small steps - local/temp git; goal: making the code better - not perfect. Separate PR's for refactoring and implementation.
4. Rename - **naming is design** - clear description of the intention. 7-stages of naming.  Continuously.
5. When to stop - only refactor what is necessary.

## When Not
1. Databases
2. Interfaces
3. Rewrite - code so messy that a rewrite would be considered cost effective.
4. Close to a deadline

## References
1. [Refactoring: Improving the Design of Existing Code by Martin Fowler]
(https://www.csie.ntu.edu.tw/~r95004/Refactoring_improving_the_design_of_existing_code.pdf)
2. [Naming: Good naming is a process, not a single step]
(http://arlobelshee.com/good-naming-is-a-process-not-a-single-step/)
3. [IntelliJ: Refactoring](https://www.jetbrains.com/help/idea/2016.1/refactoring-source-code.html)


# Test Application
This project is an implementation of Conway's Game of Life with a very crude UI in Java. 

## Seed File Format
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

## Build
execute: **mvn package assembly:single** - the executable jar would be created under the target directory, 
named in the form of 'refactor-[version]-jar-with-dependencies.jar'.

This implementation emphasizes the non-grid approach. Only one list of active cells is maintained.

## Running the Application
### Script syntext to run the UI app
gol [seed file path] [-s] [-w[n]]
* **seed file path** - the complete path of the seed file such as 'src/main/resources/p138.seed'.
* **-s** - start with stepped evolution.  Space key should be used to advance to next generation.
* **-w** - set the speed - or waiting time - of evolution. The default is 200, meaning 200ms.  To make it 
faster, chose a smaller number, such as '-w50'.
The script is expected to be run in the project's home directory, and an executable jar was created prior by 
executing **mvn package assembly:single**.

### Keyboard Commands
* **Ctrl + Space** - toggle between stepped or continued evolutions.
* **Space** - switch into stepped evolution when in continued mode; or trigger a new generation when in
 stepped mode.
* **Esc** - exit the app.