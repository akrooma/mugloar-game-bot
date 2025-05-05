# mugloar-game-bot
Console application based game bot which tries to reach a specific score in Dragons of Mugloar. The bot uses strategies and actions to perform gameplay related activities. A decision engine evaluates which strategy's action the game bot has to take. Since it is a turn-based game, the bot does one action per gameplay loop.

## Technology stack basics

* Java 17
* Spring Boot
* Gradle

## Project structure
Project folder and file structure.

	main
	├── java
	│   └── com.example.mugloar
	│	    ├── common			# Project wide utilities and constants.
	│	    ├── config			# Application property and bean configurations - API client.
	│	    ├── domain			# Game bot business logic - different domain models, gameplay strategies and actions.
	│	    └── infrastructure		# Technical implementations for enabling business logic - API (and database) integrations.
	├── resources				# Application resources - currently a single configuration file.
    ...

The test folder follows the same folder (package) structure for all unit tests and the corresponding main folder code.

## Running the console application

1. Navigate to project folder
   ```bash
   cd mugloar

2. Build the application
   ```bash
   ./gradlew build

3. Run the application
   ```bash
   ./gradlew bootRun
   
Final game state will be printed to the console once the game bot has finished playing. A message will display if a certain score was reached or the game ended (ran out of lives).

## Running unit tests

1. Navigate to project folder
   ```bash
   cd mugloar

2. Run the tests
   ```bash
   ./gradlew test

## Console application configuration
* The game bot stops playing when the score reaches a specified value. Currently the breakpoint is 1000 points, the value can be changed in `application.properties`, configuration key `game.score-breakpoint`

## What the bot will currently try to do, ordered by importance
1. If the health level is critical and the bot has enough money:
   * try to buy a health potion.
2. If there is an unhandled (not failed) `Sure thing` probability task (message) available:
   * try to complete the task (solve the message).
3. If the bot has enough money for a missing item OR the bot has all the items and enough money for the cheapest item:
   * try to buy the item.
4. If there is an unhandled (not failed) `Piece of cake` or `Walk in the park` probability task (message) available:
   * try to complete the task (solve the message).
5. Fallback action to keep the bot gaming:
   * try to complete the easiest and most rewarding task (message).

## Adding new (or updating existing) actions the game bot can execute
* If necessary, add a new executable action under `domain/game/bot/action`
* Under `domain/bot/strategy` add a new strategy, which would describe when to execute which action. The strategy can return an empty action to indicate that this strategy's action has to be skipped.
* Since the game allows for a single action per turn, then the bot will execute the first possible action based on strategy importance. Evaluatable strategies and their evaluation order is defined inside `StrategyConstants.STRATEGY_ORDER`.
* Currently the default action is to attempt to complete the easiest (according to probability) and most rewarding task (message).

## Comments about the solution
* Not all code is covered by unit tests. I tried to cover most packages by implementing tests for at least one class to give a rough idea of how the tests would look like.
* Exception handling can be improved to cover unexpected errors and to handle expected errors (410 game over response from game API) better.
* With the current strategies and actions and a higher `game.score-breakpoint` value the bot should average a score of 4500 points before running out of lives.