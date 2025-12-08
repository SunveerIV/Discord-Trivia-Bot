package com.sunveer.discord;

import com.sunveer.responder.Responder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.requests.GatewayIntent;

import java.util.List;

public class Bot extends ListenerAdapter {

    private final JDA jda;
    private final Responder responder;
    private final String channelName;

    public Bot(String token, Responder responder, String channelName) {
        this.jda = JDABuilder.createDefault(token).enableIntents(GatewayIntent.MESSAGE_CONTENT).build();
        this.jda.addEventListener(this);
        this.responder = responder;
        this.channelName = channelName;
    }

    @Override
    public void onReady(ReadyEvent event) {
        List<TextChannel> channels = event.getJDA().getTextChannelsByName(channelName, true);

        if (channels.isEmpty()) {
            System.out.println("No channel found with that name.");
            return;
        }

        for (TextChannel channel : channels) {
            channel.sendMessage(responder.initialPrompt()).queue();
        }
    }


    @Override
    public void onMessageReceived(MessageReceivedEvent event) {
        if (event.getAuthor().isBot()) return;
        if (!event.getChannel().getName().equals(channelName)) return;

        String message = event.getMessage().getContentRaw();
        String response = responder.response(message, event.getAuthor().getName());
        event.getChannel().sendMessage(response).queue();
    }
}
