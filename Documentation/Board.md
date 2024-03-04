# Board Class Documentation

The `Board` class represents the game board in a battleship game, managing the grid of coordinates, ship placements, and handling shots.

## Constructor

- `Board()`: Initializes a new board with predefined width and height, creating a grid of coordinates and an empty list for ships.

## Methods

### addShip(Ship ship)
- **Parameters**: `Ship ship` - The ship to be added to the board.
- **Returns**: `boolean` - `true` if the ship is successfully added without overlapping another ship, `false` otherwise.
- **Description**: Attempts to add a ship to the board, checking for collisions with existing ships.

### placeShipsRandomly()
- **Description**: Randomly places a predefined set of ships on the board, ensuring they do not overlap.

### printBoard()
- **Description**: Prints a visual representation of the board to the console, showing ships and shots.

### updateCoordinate(Coordinate c)
- **Parameters**: `Coordinate c` - The coordinate to be updated.
- **Description**: Updates a specific coordinate on the board, typically used after a shot to mark a hit or miss.

### handleShot(Coordinate c)
- **Parameters**: `Coordinate c` - The coordinate being shot at.
- **Returns**: `Coordinate` - The coordinate after applying the shot result (hit or miss).
- **Description**: Processes a shot at the given coordinate, updating the board and ships accordingly.

### allShipsSunk()
- **Returns**: `boolean` - `true` if all ships on the board are sunk, `false` otherwise.
- **Description**: Checks if all ships on the board have been sunk.

### getShipList()
- **Returns**: `Vector<Ship>` - A list of all ships currently placed on the board.
- **Description**: Retrieves the list of ships on the board.

### numberOfHits()
- **Returns**: `int` - The total number of hits on the board.
- **Description**: Counts and returns the total number of hit coordinates on the board.

### removeShip(Ship shipToRemove)
- **Parameters**: `Ship shipToRemove` - The ship to be removed from the board.
- **Returns**: `boolean` - `true` if the ship is successfully removed, `false` otherwise.
- **Description**: Removes a ship from the board.