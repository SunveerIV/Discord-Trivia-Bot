
import com.sunveer.discord.*;
import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.jetbrains.annotations.NotNull;

public class Main {


    public static void main(String[] args) {
        Dotenv dotenv = Dotenv.load();
        String token = dotenv.get("DISCORD_TOKEN");
        JDA api = JDABuilder.createDefault(token).enableIntents(GatewayIntent.MESSAGE_CONTENT).build();

        String channelName = dotenv.get("CHANNEL_NAME");

        ChatBot bot = new TriviaBot();

        api.addEventListener(new ListenerAdapter() {
            @Override
            public void onMessageReceived(@NotNull MessageReceivedEvent event) {
                if (event.getAuthor().isBot()) return;
                if (!event.getChannel().getName().equals(channelName)) return;

                String message = event.getMessage().getContentRaw();
                String response = bot.getResponse(message);
                event.getChannel().sendMessage(response).queue();
            }
        });
    }
}
