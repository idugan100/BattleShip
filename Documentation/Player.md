
# Player Class Documentation

The `Player` class represents a player in a board game, handling player actions, board state, and interactions during the game. It manages both the player's board and the enemy's board state, supporting operations like shooting at the enemy, handling incoming shots, checking for loss conditions, and managing ship placements.

## Constructor

- `Player()`: Initializes a new instance of the `Player` class, setting up new board instances for both the player and the enemy.

## Methods

### shoot(Coordinate c)
- **Parameters**: `Coordinate c` - The coordinate to shoot at on the enemy's board.
- **Description**: Sends a coordinate to the networked opponent and updates the enemy board's state based on the shot.

### getShotAt(Coordinate c)
- **Parameters**: `Coordinate c` - The coordinate where the player is shot at.
- **Description**: Handles incoming shots by updating the player's board state and potentially sending the result over the network.

### has_lost()
- **Returns**: `boolean` - Returns `true` if all ships on the player's board have been sunk; otherwise, returns `false`.
- **Description**: Checks if the player has lost the game by verifying if all ships on their board are sunk.

### add_ships_cli()
- **Description**: Facilitates the addition of ships to the player's board via CLI. It initializes ships with predefined types and icons, then places them on the board, handling any collisions.

### place_ship_handle_collisions(Ship ship)
- **Parameters**: `Ship ship` - The ship to be placed on the player's board.
- **Description**: Attempts to place a ship on the player's board. If a collision with existing ships occurs, it prompts the user to reposition the ship until a valid position is found.