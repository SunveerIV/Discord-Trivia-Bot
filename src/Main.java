
import com.sunveer.discord.*;
import com.sunveer.game.TriviaGame;
import com.sunveer.game.question.APIQuestionCreator;
import com.sunveer.game.storage.RedisTriviaGameStorage;
import com.sunveer.game.storage.TriviaGameStorage;
import com.sunveer.responder.Responder;
import com.sunveer.responder.TriviaBotResponder;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {


    public static void main(String[] args) throws Exception {
        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("DISCORD_TOKEN");

        String channelName = dotenv.get("CHANNEL_NAME");

        String host = dotenv.get("HOST");
        String port = dotenv.get("PORT");

        TriviaGameStorage tgs = new RedisTriviaGameStorage(host, Integer.parseInt(port), new APIQuestionCreator());
        TriviaGame tg = new TriviaGame(tgs);
        Responder tbr = new TriviaBotResponder(tg);
        new Bot(token, tbr, channelName);
    }
}
