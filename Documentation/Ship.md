
# Ship Class

The `Ship` class represents a ship within a maritime-themed game, encapsulating details such as size, name, and position, along with graphical representation through an icon.

## Constructor

- `Ship(int size, String name, String marker, ImageIcon imageIcon)`
  - Initializes a ship with the specified size, name, marker symbol, and image icon.

## Methods

### `boolean isSunk()`
- Checks if the ship is sunk based on the hit points or damage it has received. Returns `true` if the ship has been sunk, `false` otherwise.

### `void hit()`
- Records a hit on the ship, decrementing its resilience or hit points. May influence the `isSunk()` status.

### `String getName()`
- Retrieves the name of the ship.

### `int getSize()`
- Returns the size of the ship.

### `String getMarker()`
- Gets the marker symbol of the ship, used for representing the ship on a game board.

### `ImageIcon getImageIcon()`
- Returns the `ImageIcon` associated with the ship, for graphical representation in a UI.

### `void setPosition(int x, int y)`
- Sets the starting position of the ship on the game board.

### `Point getPosition()`
- Retrieves the starting position of the ship on the game board as a `Point`.

### `void setDirection(Direction direction)`
- Sets the orientation of the ship (e.g., horizontal or vertical).

### `Direction getDirection()`
- Gets the current orientation of the ship.

## Enum `Direction`
- An enumeration to define the possible orientations of a ship, such as `HORIZONTAL` or `VERTICAL`.
