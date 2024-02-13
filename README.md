# BattleShip
This is a project for Object Oriented Programming and GUI Development and Salisbury University.

# Design Ideas
- Classes
    - ship class that each type of ship (patrol, battle, submarine) can inherit from
    - board class (where the players ships are)
    - enemy board class (where the player has shots - white and red pegs)
    - player class (player will have 5 ships, a board and an enemy board)
    - game class that takes a player network connection to the opponent and starts the game loop 
    - game controller - bind the GUI events to methods on the classes
    - game view - the interface
- Build order
    - I think it would be best to start to build a simple cli version to get all the classes, rules and networking ironed out and then throw the gui on top of it
    - 

# GUI design ideas
- I like the green radar theme from this online [game](https://www.battleshiponline.org)
- I think a [retro radar theme](https://www.youtube.com/watch?v=RQMxzxR3X8c) would be cool

