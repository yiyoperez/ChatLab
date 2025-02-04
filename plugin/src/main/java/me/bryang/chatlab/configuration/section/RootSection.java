package me.bryang.chatlab.configuration.section;

import me.bryang.chatlab.configuration.ConfigurationSection;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;
import org.spongepowered.configurate.objectmapping.meta.Comment;

import java.util.Arrays;
import java.util.List;

@ConfigSerializable
public class RootSection extends ConfigurationSection {

	@Comment("Plugin settings")
	public MainSettings mainSettings = new MainSettings();
	@Comment("Private messages section")
	public PrivateMessage privateMessage = new PrivateMessage();
	@Comment("Reply messages section")
	public Reply reply = new Reply();
	@Comment("Chat format section")
	public ChatFormat chatFormat = new ChatFormat();
	@Comment("Ignore messages section")
	public Ignore ignore = new Ignore();

	@ConfigSerializable
	public static class MainSettings{
		@Comment("""
			Enable message if the plugin isn't updated when you enter to the server with a permission.
			Permission: clab.check-update
			""")
		public boolean updateCheckChat = true;
	}

	@ConfigSerializable
	public static class ChatFormat {

		@Comment("Enable option")
		public boolean enabled;

		@Comment("""
			Format when you input a chat message.
			Permission to use tags (MiniMessage tags): clab.tags""")
		public String format = "<green>>> <white><player> <grey>: <white><message>";
	}

	@ConfigSerializable
	public static class PrivateMessage {

		public String fromSender = "<red>[MSG] <dark_gray>| <white>You <dark_gray>»<green> <receptor> <dark_grey>: <white><message>";
		public String toReceptor = "<red>[MSG] <dark_gray>| <green><sender> <dark_gray>» <white>You <dark_grey>: <white><message> <color:#ff8000><click:suggest_command:'/r '><hover:show_text:'Click to reply!'>[R]</hover></click> <color:#D1AE08><click:run_command:'/ignore <sender> '><hover:show_text:'Click to ignore the player!'>[I]</hover></click>";

		public Toggle toggle = new Toggle();

		@ConfigSerializable
		public static class Toggle{

			public String enable = "<red>[MSG] <dark_gray>| <white>Enabled private messages.";
			public String disable = "<red>[MSG] <dark_gray>| <white>Disabled private messages.";
		}
	}

	@ConfigSerializable
	public static class Reply {

		@Comment("Message to notify when the player he's talking to left")
		public String left = "<green>[Chat] <gray>| <white>The player <green><target> <white>you were talking to, has left the server.";
	}

	@ConfigSerializable
	public static class Ignore {

		@Comment("Message when the player input a target to ignore")
		public String ignoredPlayer = "<color:#ff8000>[Ignore] <dark_gray>| <white>You ignored a player. <green>[<player>]<green>";

		@Comment("Message when the player input a target to unignore")
		public String unignoredPlayer = "<color:#ff8000>[Ignore] <dark_gray>| <white>You un-ignored a player. <green>[<player>]<green>";

		@Comment("Message when you see the list of ignored players")
		public SeeIgnoredPlayers seeIgnoredPlayers = new SeeIgnoredPlayers();

		@ConfigSerializable
		public static class SeeIgnoredPlayers {

			@Comment("""
				Message when the player see the list of ignored players
				List of variables:
				- <ignored_players_size>: The size of ignored players.
				- <ignored_players_data>: The list of ignored players.""")
			public List<String> format = Arrays.asList("<red>[Ignore] <white>List of ignored players <green>[<ignored_players_size>]<white>:",
				"<dark_green>>> <green><ignored_players_data>");

			@Comment("Message that replace de list of ignored players data if the player is ignoring anyone")
			public String error = "You aren't ignoring anyone.";
		}
	}

}
