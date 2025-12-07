
import com.sunveer.discord.*;
import com.sunveer.game.TriviaGame;
import com.sunveer.game.storage.RedisTriviaGameStorage;
import com.sunveer.game.storage.TriviaGameStorage;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class Main {


    public static void main(String[] args) throws Exception {
        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("DISCORD_TOKEN");
        JDA api = JDABuilder.createDefault(token).enableIntents(GatewayIntent.MESSAGE_CONTENT).build();

        String channelName = dotenv.get("CHANNEL_NAME");

        TriviaGameStorage tgs = new RedisTriviaGameStorage("192.168.0.90", 1236);
        TriviaGame tg = new TriviaGame(tgs);
        TriviaBot tb = new TriviaBot(tg, channelName);

        api.addEventListener(tb);
    }
}
