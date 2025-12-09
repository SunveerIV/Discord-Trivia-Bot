
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

        String apiNinjasKey = initializeApiNinjasKey();
        TriviaGameStorage storage = initializeStorage(apiNinjasKey);
        TriviaGame game = initializeGame(storage);
        Responder responder = initializeResponder(game);

        initializeBot(responder);
    }

    private static void initializeDotenv() {
        try {
            dotenv = Dotenv.load();
        } catch (DotenvException e) {
            throw new RuntimeException("Could not load dotenv. Exiting.", e);
        }
    }

    private static TriviaGameStorage initializeStorage(String apiNinjaskey) {
        String host = dotenv.get("HOST");
        String port = dotenv.get("PORT");

        if (host == null) throw new IllegalArgumentException("IP is null! Check dotenv!");
        if (port == null) throw new IllegalArgumentException("Port is null! Check dotenv!");

        try {
            return new RedisTriviaGameStorage(host, Integer.parseInt(port), new APIQuestionCreator(apiNinjaskey));
        } catch (StorageException e) {
            throw new RuntimeException("Could not start trivia game storage", e);
        }
    }

    private static TriviaGame initializeGame(TriviaGameStorage storage) {
        return new TriviaGame(storage);
    }

    private static Responder initializeResponder(TriviaGame game) {
        return new TriviaBotResponder(game);
    }

    private static String initializeApiNinjasKey() {
        String apiKey = dotenv.get("API_NINJAS_KEY");

        if (apiKey == null) throw new IllegalArgumentException("API Ninjas Key could not be loaded. Stopping Process.");

        System.out.println("API Ninjas Key Loaded Successfully!");
        return apiKey;
    }

    private static void initializeBot(Responder responder) {
        String token = dotenv.get("DISCORD_TOKEN");
        String channelName = dotenv.get("CHANNEL_NAME");

        new Bot(token, responder, channelName);
    }
}
