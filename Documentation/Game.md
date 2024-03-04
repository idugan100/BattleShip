# Game Class Documentation

The `Game` class encapsulates the main game logic and flow for a networked board game, managing the game state, player actions, and network communication.

## Constructor

- `Game()`: Initializes a new game by creating a `Player` instance, setting initial turn and game over states, and establishing a network connection to the server.

## Methods

### shoot(Coordinate c)
- **Parameters**: `Coordinate c` - The coordinate to shoot at on the enemy's board.
- **Returns**: `Coordinate` - The shot coordinate, updated with the result of the shot (hit or miss).
- **Description**: Sends a shooting action to the server, receives the result of the shot (hit or miss), and updates the player's view of the enemy board accordingly.

### getShot()
- **Returns**: `Coordinate` - The coordinate where the player is shot at, including the result of the shot (hit or miss).
- **Description**: Waits for and processes an incoming shot from the opponent. It updates the player's board based on the shot and sends the result back to the opponent.

### hasWon()
- **Returns**: `boolean` - Returns `true` if the player has successfully hit all enemy ships, otherwise `false`.
- **Description**: Checks if the player has won the game by verifying if the total number of hits on the enemy board equals the total number of hits needed to win.

### hasLost()
- **Returns**: `boolean` - Returns `true` if all ships on the player's board have been sunk, otherwise `false`.
- **Description**: Checks if the player has lost the game by verifying if all ships on their board are sunk.