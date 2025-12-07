
import com.sunveer.discord.*;
import com.sunveer.game.TriviaGame;
import com.sunveer.game.storage.RedisTriviaGameStorage;
import com.sunveer.game.storage.TriviaGameStorage;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {


    public static void main(String[] args) throws Exception {
        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("DISCORD_TOKEN");

        String channelName = dotenv.get("CHANNEL_NAME");

        TriviaGameStorage tgs = new RedisTriviaGameStorage("192.168.0.90", 1236);
        TriviaGame tg = new TriviaGame(tgs);
        TriviaBot tb = new TriviaBot(token, tg, channelName);
    }
}
