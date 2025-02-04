package me.bryang.chatlab.command;

import me.bryang.chatlab.UpdateCheckHandler;
import me.bryang.chatlab.configuration.ConfigurationContainer;
import me.bryang.chatlab.configuration.section.MessageSection;
import me.bryang.chatlab.configuration.section.RootSection;
import me.bryang.chatlab.message.MessageManager;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder;
import org.bukkit.entity.Player;
import team.unnamed.inject.InjectAll;

import javax.inject.Named;

@Command(
	names = {"clab", "chatlab"},
	desc = "Command to manage the plugin.")
@InjectAll
public class MainCommand implements CommandClass {

	private ConfigurationContainer<RootSection> configurationContainer;
	private ConfigurationContainer<MessageSection> messageContainer;
	private UpdateCheckHandler updateChecker;
	private MessageManager messageManager;
	@Named("plugin-version")
	private String pluginVersion;

	@Command(names = {""})
	public void executeMainSubCommand(@Sender Player sender) {
		messageManager.sendMessage(sender, """
					<blue>ChatLab: <white>Main plugin command.
					<dark_grey>- <white>/clab reload <dark_grey>: <green>Reload the plugin.
					<dark_grey>- <white>/clab check-update <dark_grey>: <green>Check update.
					<dark_grey>- <white>/clab re-check <dark_grey>: <green>Send a request if has a new update.
					<dark_grey>- <white>/clab about <dark_grey>: <green>See about the plugin.""");

	}
	@Command(names = "about")
	public void executeAboutSubCommand(@Sender Player sender) {

		messageManager.sendMessage(sender, """
            <green>ChatLab <dark_gray>- <white>Chat plugin.<white>
            <blue><version>
            <green>>> <white>Source code: <green><u><click:open_url:'https://github.com/devblook/chatlab>[GitHub]</click></u>""",
			Placeholder.unparsed("version", pluginVersion));

	}

	@Command(names = "reload", permission = "clab.reload")
	public void executeReloadSubCommand(@Sender Player sender) {

		configurationContainer.reload();
		messageContainer.reload();
		messageManager.sendMessage(sender, "<blue>[ChatLab] <dark_grey>| <white>Plugin has been reloaded.");

	}

	@Command(names = "check-update", permission = "clab.check-update")
	public void executeCheckUpdateSubCommand(@Sender Player sender){

		if (!updateChecker.requestSuccess()){
			messageManager.sendMessage(sender, "<green>Checking update!");
			updateChecker
				.request()
				.thenAccept(action -> checkUpdate(sender));
			return;

		}
		checkUpdate(sender);

	}

	@Command(names = "re-check", permission = "clab.check-update")
	public void executeReCheckUpdateSubCommand(@Sender Player sender){

		messageManager.sendMessage(sender, "<green>Checking update!");
		updateChecker
			.request()
			.thenAccept(action -> checkUpdate(sender));
	}

	private void checkUpdate(Player sender){
		if (!updateChecker.updated()){
			messageManager.sendMessage(sender, """
						<green>Checked! <white>The plugin has a new update.
						<dark_green>>> <green><u><click:open_url:'https://github.com/devblook/chatlab/releases/tag/%s'>Click here</click></u> <white>to download the plugin."""
				.formatted(updateChecker.lastVersion()));
		}else{
			messageManager.sendMessage(sender, "<green>Checked! <white>You have the latest update!");
		}

	}

}
