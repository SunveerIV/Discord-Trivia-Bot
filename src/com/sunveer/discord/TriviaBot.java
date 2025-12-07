package com.sunveer.discord;

import com.sunveer.game.TriviaGame;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

public class TriviaBot extends ListenerAdapter {

    private final String channelName;

    public TriviaBot(TriviaGame tg, String channelName) {
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
