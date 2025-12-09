
import com.sunveer.discord.*;
import com.sunveer.game.TriviaGame;
import com.sunveer.game.question.APIQuestionCreator;
import com.sunveer.game.storage.RedisTriviaGameStorage;
import com.sunveer.game.storage.StorageException;
import com.sunveer.game.storage.TriviaGameStorage;
import com.sunveer.responder.Responder;
import com.sunveer.responder.TriviaBotResponder;
import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

public class Main {

    private static Dotenv dotenv;

    public static void main(String[] args) {
        initializeDotenv();
        String apiNinjaskey = initializeApiNinjasKey();
        TriviaGameStorage storage = initializeStorage(apiNinjaskey);
        String token = dotenv.get("DISCORD_TOKEN");

        String channelName = dotenv.get("CHANNEL_NAME");

        TriviaGame tg = new TriviaGame(storage);
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

    private static TriviaGameStorage initializeStorage(String apiNinjaskey) {
        String host = dotenv.get("HOST");
        String port = dotenv.get("PORT");

        if (host == null) throw new RuntimeException("IP is null! Check dotenv!");
        if (port == null) throw new RuntimeException("Port is null! Check dotenv!");

        try {
            return new RedisTriviaGameStorage(host, Integer.parseInt(port), new APIQuestionCreator(apiNinjaskey));
        } catch (StorageException e) {
            throw new RuntimeException("Could not start trivia game storage", e);
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
