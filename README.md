
## Description

This project is a trivia game that uses the JDA (**J**ava **D**iscord **A**PI)
to create a modular discord bot.

The rules are simple: Everything is allowed.  
This includes:
- AI
- Google
- Your brain
- Other people's answers

On submission, if you were the **first** to answer, you get 2 points.  
If you were the **second** to answer, you get 1 point.

### Requirements
- You must have a Discord Bot API Key
- You must have an [API Ninjas Key](https://api-ninjas.com/)
- You must have Git LFS installed

### Dotenv setup
You **must** include the following items in your .env file for the bot to be functional

- DISCORD_TOKEN - Your Discord Bot API Key
- CHANNEL_NAME - The channel you want the bot to respond in
- API_NINJAS_KEY - Your [API Ninjas Key](https://api-ninjas.com/)
- HOST - Redis Database IP Address
- PORT - Redis Database Port

## System Diagram

![UML](assets/UML.png)

Each color represents its own distinct package.  

The QuestionCreator interface returns a new question.
If the QuestionCreator **is a** MockQuestionCreator, it will return the same question every time.
If the QuestionCreator **is an** APIQuestionCreator, it will get a new random question from a web API.

The TriviaGameStorage interface is meant to be modular, meaning you can implement a different storage method down the line

TriviaGame is where the core game logic is stored.  
The Scores class is a helper class that holds all scores for when getScores() is called on TriviaGameStorage.  

The Responder interface simply takes a String message, and returns a String response


## Data Storage

![UML](assets/RDB_Diagram.png)

This is a diagram showing how the game's data is stored in Redis.

**SCORE_KEY** holds **total** scores  
**CURRENT_GAME_KEY** holds the string of the key for the current game  
**CURRENT_QUESTION_TEXT_KEY** holds the string of the current question (if there is one)  
**CURRENT_ANSWER_TEXT_KEY** holds the string of the current answer (if there is one)  

## Usage

Here is a list of commands you can run in discord:  
`!help` - Shows all commands you can use.  
`!rules` - Shows all rules for the game.  
`!sub` - Submits an answer for the question.  
`!start` - Starts a game if one isn't already running.  
`!score` - States your individual overall score.  
`!leaderboard` - Shows the top 10 leaderboard for overall score.  