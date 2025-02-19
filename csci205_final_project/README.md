# CSCI 205 - Software Engineering and Design
Bucknell University
Lewisburg, PA
### Course Info
Instructor: Professor Romano  
Semester: Spring 2024
## Team Information
#### Team Name: Connect 4
Casey King: 2026, CSE, Scrum Master  
Jonas Scott: 2026, CSE  
Owen Reilly: 2026, CS  
Mikey Myro: 2026, CSE, Project Manager
## Project Summary
Our project will be a modified clone of the New York Times game “Connections” with 
4 levels of difficulty (easy, medium, hard, extreme). Connections presents the 
player with a 4 by 4 grid of squares, each containing one word. The objective of 
the game is to find groups of four words that are in the same category. The player, 
using their mouse, selects 4 squares, then presses a submit button to check a guess. 
The game is over when either all 4 categories are guessed correctly, or 4 guesses 
are exhausted and the player loses.

Upon starting the application, an interface will pop up and the user can click on 
one of the 4 difficulty levels to select. Selecting a level will launch them into 
a game of connections. Once the game is over, the user will be taken back to the 
level selection screen. For each of the 4 levels, there will be one board of set 
words in set spots. This is to make each game more cohesive, as the positioning and 
respective categories in each game can make the game more or less challenging and 
clever.
*****
## How to run it
*****
Our project will be a modified clone of the New York Times game “Connections” with 
5 levels of difficulty (easy, medium, hard, extreme, Hollywood). Connections presents the player 
with a 4 by 4 grid of squares, each containing one word or image. The objective of the game is 
to find groups of four words/images that are in the same category. The player, using their mouse, 
selects 4 squares, then presses a submit button to check a guess. The user also has access to a 
shuffle button to reshuffle the words. Additionally, the user has access to a return button to return
to the home screen should they want to play a different level. The user also will see a text pop-up 
telling them if they are only one tile away from getting a category correct.The game is over when 
either all 4 categories are guessed correctly, or 4 guesses are exhausted and the player loses. 

Upon starting the application, an interface will pop up and the user can click on one of 
the 5 difficulty levels to select. Selecting a level will launch them into a game of 
connections. Once the game is over, the user will be taken back to the level selection screen. 
For each of the 4 levels, there will be one board of set words in set spots. This is to make 
each game more cohesive, as the positioning and respective categories in each game can make the 
game more or less challenging and clever.



## Package Structure
****
We followed a standard MVC package structure. We have a model package containing all of our logic for our
program to run. This package contains all the stuff with the intricacies and details of the board. We have 
our ConnectionsMain class in our org package that is there to start the program and open the initial home 
screen window. Our view package contains a lot of functionality in the ConnectionsView class and also contains
the controller class. The controller class contains all functionality for switching between screens and 
the bindings for the buttons. We have a resources package containing two different CSS files we used for 
styling, one for the home screen and one for the gameplay screen. Finally, we have our test package containing 
all necessary JUnit tests. 

### Video URL: https://drive.google.com/file/d/1YaXpaJU_iNMpkE3Z3V0fNuwSTUqKZHjT/view?t=1