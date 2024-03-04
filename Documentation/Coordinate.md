# Coordinate & CoordinateStatus Classes Documentation

## Coordinate Class

Represents a single coordinate on the game board, holding information about its row, column, and status.

### Constructor

- `Coordinate(int row, int column)`: Initializes a coordinate with specified row and column, setting its status to `UNTOUCHED`.

### Methods

- `boolean isHit()`: Returns `true` if the coordinate's status is `HIT`.
- `boolean isUntouched()`: Returns `true` if the coordinate's status is `UNTOUCHED`.
- `boolean isMissed()`: Returns `true` if the coordinate's status is `MISSED`.
- `void hitCoordinate()`: Sets the coordinate's status to `HIT`.
- `void missCoordinate()`: Sets the coordinate's status to `MISSED`.
- `void resetCoordinate()`: Resets the coordinate's status to `UNTOUCHED`.
- `int getRow()`: Returns the row of the coordinate.
- `int getColumn()`: Returns the column of the coordinate.
- `String getShipModifier()`: Returns a string modifier based on the coordinate's status for ship display.
- `String getBoardModifier()`: Returns a string modifier based on the coordinate's status for board display.

## CoordinateStatus Enum

Defines the possible states of a coordinate on the board.

### Values

- `HIT`: Indicates the coordinate has been hit.
- `MISSED`: Indicates a shot at the coordinate missed any ships.
- `UNTOUCHED`: Indicates the coordinate has not been targeted yet.

These classes work together to represent and manage each cell's state on the game board, allowing for tracking of shots and hits in a battleship game context.
