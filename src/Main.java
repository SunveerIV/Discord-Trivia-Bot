
import com.sunveer.discord.*;
import com.sunveer.game.TriviaGame;
import com.sunveer.game.question.APIQuestionCreator;
import com.sunveer.game.storage.RedisTriviaGameStorage;
import com.sunveer.game.storage.TriviaGameStorage;
import com.sunveer.responder.Responder;
import com.sunveer.responder.TriviaBotResponder;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

public class Main {

    private static Dotenv dotenv;

    public static void main(String[] args) throws Exception {
        initializeDotenv();

        String token = dotenv.get("DISCORD_TOKEN");

        String channelName = dotenv.get("CHANNEL_NAME");

        String host = dotenv.get("HOST");
        String port = dotenv.get("PORT");

        String apiNinjaskey = initializeApiNinjasKey();

        TriviaGameStorage tgs = new RedisTriviaGameStorage(host, Integer.parseInt(port), new APIQuestionCreator(apiNinjaskey));
        TriviaGame tg = new TriviaGame(tgs);
        Responder tbr = new TriviaBotResponder(tg);
        new Bot(token, tbr, channelName);
    }

    private static void initializeDotenv() {
        try {
            dotenv = Dotenv.load();
        } catch (DotenvException e) {
            System.err.println("Could not load dotenv. Exiting.");
            System.exit(1);
        }
    }

    private static String initializeApiNinjasKey() {
        String apiKey;
        apiKey = dotenv.get("API_NINJAS_KEY");
        System.out.println("API Ninjas Key Loaded Successfully!");
        if (apiKey == null) {
            System.err.println("API Ninjas Key could not be loaded. Stopping Process.");
            System.exit(1);
        }
        return apiKey;
    }
}
