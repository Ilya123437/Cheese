This is where I will store my workflow and
tasks that I still need to work on

UserHandler.java
Handles user registration
- register(ctx) — reads the request, calls UserService.register(), returns the result.

SessionHandler.java
Handles authentication
- login(ctx) — reads credentials and calls UserService.login().
- logout(ctx) — extracts the auth token and calls UserService.logout().

GameHandler.java
Handles game operations
- listGames(ctx) — calls GameService.listGames().
- createGame(ctx) — reads the game name and calls GameService.createGame().
- joinGame(ctx) — reads the player color and game ID and calls GameService.joinGame().
  
ClearHandler.java
Handles database reset
- clear(ctx) — calls your clear service and returns the appropriate response.
