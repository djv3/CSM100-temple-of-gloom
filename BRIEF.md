![University of London](.guides/img/logo.png "The University of London")

## Module: Software Design and Programming
## Coursework: April to September 2023 study session
## Submission Deadline: Please see the entry under the “Assessment” section on Moodle.
Please note, when the due date is reached, the version you have submitted last, will be considered as your final submission and it will be the version that is marked.
Once the due date has passed, it will not be possible for you to submit a different version of your assessment. Therefore, you must ensure you have submitted the correct version of your assessment which you wish to be marked, by the due date.

## Objectives
The aims of this coursework assignment are to:

- learn how to understand and extend an existing code base,
- focus on the essential (and relevant) packages within that code base,
- work with packages,
- become accustomed to the features of modern Java,
- to see the use of Java concurrency via the threading model,
- to see the workings of a Java GUI application,
- to work in teams,
- to use git actions, and
- draw together the main learning objectives from this module.

The code you write is relatively small, but the amount you have to read is quite large.
We will be running additional webinars to help you with this assignment as we understand that it is complex, but you will learn a great deal from completing the task.

## Overview
In this assignment, you will help the explorer and Professor of Finance, Jeremy Hunt, claim the Orb of Lots in the Temple of Gloom. You will help him explore an unknown cavern under the temple, claim the orb, and escape before the entrance collapses. There will be great rewards for those who help Jeremy fill his pockets with gold on the way out.
There are two principal phases to this assignment:

- the exploration phase
- the escape phase.

Each of which involves writing a different method in Java. We explain these phases in detail after presenting some additional introductory material.

## The exploration phase
On the way to the Orb, the cavern’s layout is unknown. You know only the status of the tile on which you are standing and those immediately around you and perhaps those that you remember. Your goal is to make it to the
Orb in as few steps as possible.
Searching for the Orb during the exploration phase
This is not a blind search, as you will know the distance to the Orb. This is equal to the number of tiles on the shortest path to the Orb, if there were not any walls or obstacles in the way.
You will develop the solution to this phase in the method explore() in the class Explorer within the package student. There is no time limit on the number of steps you can take, but you will receive a higher score for finding the Orb in fewer steps. To pick up the Orb, return from this method once you have arrived on its tile. Returning when Jeremy is not on the Orb tile will cause an exception to be thrown, halting the game.
When writing the method explore(), you are given an ExplorationState object to learn about your environment. Every time you move, this object automatically changes to reflect the new location of the explorer. This object includes the following methods:

```java
long getCurrentLocation(): return a unique identifier for the tile the explorer is currently on.
```
```java
int getDistanceToTarget(): return the distance from the explorer’s current location to the Orb.
```
```java
Collection<NodeStatus> getNeighbours(): return information about the tiles to which the explorer can move from their current location.
```
```java
void moveTo(long id): move the explorer to the tile with ID id. This fails if that tile is not adjacent to the current location.
```

Notice that the method `getNeighbours()` returns a collection of NodeStatus objects. This is simply an object that contains, for each neighbour, the ID corresponding to that neighbour, as well as that neighbour’s distance from the Orb.

You can examine the documentation for this class for more information on how to use NodeStatus objects. A suggested first implementation that will always find the Orb but likely as won’t receive a significant bonus score is a depth-first search. More advanced solutions might incorporate the distance of tiles from the Orb.

## The escape phase

After picking up the Orb, the walls of the cavern shift and a new layout is generated — ouch! In addition, piles of gold fall onto the ground. Luckily, underneath the Orb is a map, revealing the full cavern to you. However, the stress of the moving walls has compromised the integrity of the cavern, beginning a time limit after which the ceiling will collapse, crushing Jeremy! Additionally, picking up the Orb activated the traps and puzzles of the cavern, causing different edges of the graph to have different weights.

### Collecting gold during the escape phase

The goal of the escape phase is to make it to the cavern’s entrance before it collapses. However, a score component is based on two additional factors:

- The amount of gold that you pick up during the escape phase
- Your score from the exploration phase.

Your score will simply be the amount of gold picked up times the score from the exploration phase.

You will write your solution code to this part of the problem in a method `escape()` in the class Explorer in the package student.

To escape, simply return from this method while standing on the entrance tile. Returning while at any other position or failing to return before time runs out will cause the game to end and result in a score of 0.

An important point to clarify is that time during this phase is not defined as the CPU time your solution takes to compute but rather the number of steps the explorer takes: the time remaining decrements regardless of how long you spent deciding which move to make. Because of this, you can be guaranteed to always be given enough time to escape the cavern should you take the shortest path out. Note that there are differing amounts of gold on the different tiles. Picking up gold on the tile you are standing on takes no time.

When writing this method, you are given an `EscapeState` object to learn about your environment. Every time you move, this object will automatically change to reflect the new location of the explorer. This object includes the following methods:

```java
Node getCurrentNode(): return the Node corresponding to the explorer’s location.
```
```java
Node getExit(): return the Node corresponding to the exit to the cavern (the destination).
```
```java
Collection<Node> getVertices(): return a collection of all traversable nodes in the graph.
```
```java
int getTimeRemaining(): return the number of steps the explorer has left before the ceiling collapses.
```
```java
void moveTo(Node n): move the explorer to node n. this will fail if the given node is not adjacent to the explorer’s current location. Calling this function will decrement the time remaining.
```
```java
void pickUpGold(): Collect all gold on the current tile. This will fail if there is no gold on the current tile or if it has already been collected.
```

Class `Node` (and the corresponding class `Edge`) has methods that are likely to be useful. Look at the documentation for these classes to learn what additional methods are available. A good starting point is to write an implementation that will always escape the cavern before time runs out. From there, you can consider trying to pick up gold to optimize your score using more advanced techniques.
However, the most important part is always that your solution successfully escapes the cavern — if you improve on your solution, make sure that it never fails to escape in time!

## The provided code

The provided code base should be present in this repository. The difference between this and previous worksheets/coursework is that we have only given you the class files (don’t try to reverse engineer the source code, as that will ruin the point of the exercise and won’t gain you anything anyway). You should see four packages:

- game
- gui
- student
- main

The student package is where you will be writing your code.
The program can be run from two classes within the main package. Running the main method from the class `TXTmain` executes the program in headless mode (without a GUI); running it from `GUImain` runs it with an accompanying display, which may help debug. Each of these runs a single map on a random seed by default. If you run the program before any solution code is written, you should see the explorer stand still, and an error message tells you that you returned `fromexplore()` without having found the Orb. It would be best if you started by trying this to check that the code compiles correctly.
Two optional flags can be used to run the program in different ways:


`-n <count>`: 
    runs the program multiple times. This option is available only in headless mode and is ignored if run with the GUI. The output will still be written to the console for each map so you know how well you did, and an average score will be provided at the end. This helps run your solution many times and compare different solutions on many maps.

`-s <seed>`:
    runs the program with a predefined seed. This allows you to test your solutions on particular maps that can be challenging or that you might be failing on and thus is quite helpful for debugging. This can be used both with the GUI and in headless mode.

For instance, to run the program 100 times in headless mode, write:

```bash
java main.TXTmain -n 100
```

To run the program once with a seed of 1050, write:

```bash
java main.GUImain -s 1050
```

You may combine these flags, but if you run the program more than once and provide a seed, it will run the program on the same map (the map generated by that seed) every time.

## The GUI
When running your program (except in headless mode), you are presented with a GUI where you can watch the explorer making moves. When the GUI is running, each call to moveTo() blocks until the corresponding move completes on the GUI — that is, when you make a call to moveTo(), that call will not return, and consequently, your code will not continue running until the corresponding animation on the GUI has been completed. Therefore, running your code in headless mode will generally complete faster than running it with the GUI.

You can use the slider on the right side of the GUI to increase or decrease the speed of the explorer. Increasing the speed will make the animation finish faster. Reducing the rate might help debug and better understand what exactly your solution is doing. Also, a timer displays the number of steps remaining during the escape phase (both as a number and a percentage). A Print Seed button lets you print the seed to the console to easily copy and paste it into the program arguments to retry your solution on a challenging map. You can also see the bonus and the number of coins collected, followed by the final score computed as the product. The bonus multiplier begins at 1.3 and slowly decreases as you take more and more steps during the explore stage (after which it is fixed), while the number of coins increases as you collect them during the escape phase. Finally, click on any square in the map to see more detailed information on the right, including its row and column, the type of tile, and the amount of gold on that square.

## The deliverables

The assignment must follow the usual add/commit cycle. We have already made one “commit” to your repository. You do not need to push the repository to a remote repository.

You should aim to have at least one SCRUM meeting a week (even if that is an asynchronous meeting) and this should be clearly documented on your [Trello board](https://trello.com/invite/b/qzjsGNLr/ATTI670fd31a1be6981160f2722f536d7bda268459E6/coursework-two-temple-of-gloom). 

The meeting could be recorded and also included in your submission.

You must include the appropriate documentation, including links to your Slack group/Discord server, and your Trello board.

Your classes, documentation, unit tests, and CI/CD process are all part of your submission. (You do not have to use CI/CD if you consider it unnecessary.)

You should include full javadoc for your classes (most of which is provided for you anyway).

You are encouraged to have everything ready well in advance to avoid any last-minute problems (e.g., CODIO may be down for maintenance).


## Additional grading criteria
In addition to the standard grading criteria this assignment will be graded according to its:

- ability to fulfill the requirements
- the simplicity, clarity, and the generality of the code (including succinct but illustrative comments and JavaDoc)
- compliance with good practices of coding and version control as outlined during the module (e.g., committing often and in small pieces
- of descriptive commit messages, committing only source code and not binary or .class files)

The vast majority of points on this assignment will come from a correct solution that always finds the Orb and escapes before the time runs out, so your priority should be to ensure that your code always does this successfully. To receive full credit for the assignment, your solution must also get a reasonably high score (achieved by optimizing the bonus multiplier in the explore phase and collecting as many coins as possible in the escape phase), so you should spend some time thinking about ways to optimize your solution.

While the time your code takes to decide its next move does not factor into the number of steps taken or the time remaining and consequently does not affect your score, we cannot wait for your code forever and so must impose a timeout when grading your code. When running in headless mode, your code should take no longer than roughly ten seconds to complete any single map.

Solutions that take significantly longer may be treated as if they did not complete and will likely receive low grades.

```important
Using Java Reflection mechanisms is strictly forbidden and will result in significant penalties.
```

Another key aspect of your submission is how well your team worked together.

## Credits

Thanks go out to Eric Perdew, Ryan Pindulic, and Ethan Cecchetti from the Department of Computer Science at Cornell for the basis of this coursework.
