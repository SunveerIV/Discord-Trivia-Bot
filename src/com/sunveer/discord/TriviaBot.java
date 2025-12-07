package com.sunveer.discord;

import com.sunveer.game.TriviaGame;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

public class TriviaBot extends ListenerAdapter {

    private final JDA jda;
    private final TriviaGame game;
    private final String channelName;

    public TriviaBot(String token, TriviaGame game, String channelName) {
        this.jda = JDABuilder.createDefault(token).enableIntents(GatewayIntent.MESSAGE_CONTENT).build();
        this.jda.addEventListener(this);
        this.game = game;
        this.channelName = channelName;
    }

    private String response(String message) {
        if (message.toLowerCase().startsWith("hi")) {
            return "hello";
        }
        return "Invalid message!";
    }

    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.getChannel().getName().equals(channelName)) return;

        String message = event.getMessage().getContentRaw();
        String response = response(message);
        event.getChannel().sendMessage(response).queue();
    }
}
