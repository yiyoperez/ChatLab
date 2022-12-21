package me.bryang.chatlab.command;

import me.bryang.chatlab.ChatLab;
import me.bryang.chatlab.api.utils.TypeRegistry;
import me.bryang.chatlab.manager.BukkitFileManager;
import me.bryang.chatlab.user.User;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.annotated.annotation.Text;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import team.unnamed.inject.InjectAll;

import javax.inject.Named;

@InjectAll
public class MessageCommand implements CommandClass {

    private BukkitFileManager configFile;
    @Named("messages")
    private BukkitFileManager messagesFile;
    private TypeRegistry<User> users;

    private ChatLab plugin;

    @Command(names = {"msg", "pm", "m", "message", "tell", "w"},
            desc = "Private message command")
    public void messageCommand(@Sender Player sender, @OptArg() Player target,
                               @Text @OptArg() String senderMessage){
        FileConfiguration config = configFile.get();
        FileConfiguration messages = messagesFile.get();

        if (target == null){
            sender.sendMessage(messages.getString("error.no-argument")
                    .replace("%usage%", "/msg <player> <message>"));
            return;
        }

        if (senderMessage.isEmpty()){
            sender.sendMessage(messages.getString("error.no-argument")
                    .replace("%usage%", "/msg <player> <message>"));
            return;
        }

        sender.sendMessage(config.getString("private-messages.from-sender")
                .replace("%target%", target.getName())
                .replace("%message%", senderMessage));

        target.sendMessage(config.getString("private-messages.to-receptor")
                .replace("%sender%", sender.getName())
                .replace("%message%", senderMessage));

        User user = users.get(sender.getUniqueId().toString());
        user.recentMessenger(target.getUniqueId().toString());
    }
}
