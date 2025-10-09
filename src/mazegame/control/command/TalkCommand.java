package mazegame.control.command;

import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;
import mazegame.entity.NonPlayerCharacter;
import mazegame.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Command to handle talking with NPCs. Supports talking to one NPC by name or to everyone present.
 */
public class TalkCommand implements Command {

	private static final Random RNG = new Random(); // random picker for lines

	// friendly phrases
	private static final List<String> FRIENDLY_PLAYER_OPENERS = Arrays.asList(
			"Greetings, friend. I seek allies against Gregor.",
			"Well met. Have you seen any trouble nearby?",
			"Hail. This road grows dangerous! Care to walk together?",
			"I’m looking for good folk with steady hands. Interested?");

	private static final List<String> FRIENDLY_NPC_REPLIES = Arrays.asList(
			"You’re welcome here! Share the fire and your story.",
			"Safe paths to you. I can spare some time to help.",
			"I keep watch for brigands. You’ll be safer if we stick together.",
			"You seemed decent enough. Let’s see what lies ahead.");

	private static final List<String> FRIENDLY_PLAYER_FOLLOWUPS = Arrays.asList(
			"Your words bring hope. Shall we travel together?",
			"I’m grateful. Let’s keep each other sharp.",
			"Then we stand as one. Ready when you are.",
			"Good! Please lead on, and I’ll watch our flank.");

	private static final List<String> FRIENDLY_NPC_ENDERS = Arrays.asList(
			"Then let’s walk as one. Adventure awaits!",
			"I’ll guide you through the rough spots.",
			"Stay close! These lands test the unwary.",
			"Right behind you. Let’s move.");

	// hostile phrases
	private static final List<String> HOSTILE_NPC_REPLIS = Arrays.asList(
			"You’ll bleed for every step you take!",
			"Turn around, fool, while you still can.",
			"I’ve cracked harder skulls before breakfast.",
			"You’ve got courage! Shame it won’t save you.");

	private static final List<String> HOSTILE_PLAYER_FOLLOWUPS = Arrays.asList(
			"If that’s how it is, then steel will decide.",
			"So be it. Draw.",
			"Enough talk. Let’s finish this.",
			"Your threats are air. Mine are iron.");

	private static final List<String> HOSTILE_NPC_ENDERS = Arrays.asList(
			"Enough words. Let’s fight!",
			"I’ll mount your head on the gate.",
			"Try me, and meet your end.",
			"You first, hero.");

	@Override
	public CommandResponse execute(ParsedInput userInput, Player currentPlayer) {
		// no argument means talk to everyone present
		if (userInput.getArguments().isEmpty()) {
			return talkToWholeSet(currentPlayer);
		}

		// talk to a specific NPC by name
		String npcName = (String) userInput.getArguments().get(0); // first token as name
		NonPlayerCharacter currentNpc = getNPC(currentPlayer, npcName); // resolve NPC

		if (currentNpc == null) {
			return new CommandResponse("There is no one with that name to talk to.");
		}

		// branch by hostility
		if (currentNpc.isHostile()) {
			return startHostileConversation(currentPlayer, currentNpc);
		} else {
			return startNonHostileConversation(currentPlayer, currentNpc);
		}
	}

	// resolve NPC by checking location first then player's party
	private NonPlayerCharacter getNPC(Player currentPlayer, String npcName) {
		NonPlayerCharacter npc = currentPlayer.getCurrentLocation().getNpcCollection().get(npcName); // from location
		if (npc != null)
			return npc;

		return currentPlayer.getNpcCollection().get(npcName); // from party
	}

	// talk to all NPCs in the location
	private CommandResponse talkToWholeSet(Player currentPlayer) {
		if (currentPlayer.getCurrentLocation().getNpcCollection().isEmpty()) {
			return new CommandResponse("There is no one here to talk to.");
		}

		// player opening line
		printDialogueLine(currentPlayer.getName(), ": " + pick(FRIENDLY_PLAYER_OPENERS));
		sleepFor(0);

		// hostile NPCs respond
		if (!currentPlayer.getCurrentLocation().getNpcCollection().getNPCsByHostility(true).isEmpty()) {
			currentPlayer.getCurrentLocation().getNpcCollection().getNPCsByHostility(true).forEach(npc -> {
				printDialogueLine(npc.getName(), ": " + pick(HOSTILE_NPC_REPLIS));
				sleepFor(0);
			});
		}

		// friendly NPCs respond
		if (!currentPlayer.getCurrentLocation().getNpcCollection().getNPCsByHostility(false).isEmpty()) {
			currentPlayer.getCurrentLocation().getNpcCollection().getNPCsByHostility(false).forEach(npc -> {
				printDialogueLine(npc.getName(), ": " + pick(FRIENDLY_NPC_REPLIES));
				sleepFor(0);
			});
		}

		return new CommandResponse(""); // dialogue printed to console
	}

	// friendly exchange with one NPC
	private CommandResponse startNonHostileConversation(Player player, NonPlayerCharacter npc) {
		printDialogueLine(player.getName(), ": " + pick(FRIENDLY_PLAYER_OPENERS));
		sleepFor(0);

		String npcLine = pickWithConversation(FRIENDLY_NPC_REPLIES, npc.getConversation()); // use custom flavor if any
		printDialogueLine(npc.getName(), ": " + npcLine);
		sleepFor(0);

		printDialogueLine(player.getName(), ": " + pick(FRIENDLY_PLAYER_FOLLOWUPS));
		sleepFor(0);

		printDialogueLine(npc.getName(), ": " + pick(FRIENDLY_NPC_ENDERS));
		return new CommandResponse("");
	}

	// hostile exchange with one NPC
	private CommandResponse startHostileConversation(Player player, NonPlayerCharacter npc) {
		printDialogueLine(player.getName(), ": " + pick(FRIENDLY_PLAYER_OPENERS));
		sleepFor(0);

		String npcLine = pickWithConversation(HOSTILE_NPC_REPLIS, npc.getConversation()); // use custom flavor if any
		printDialogueLine(npc.getName(), ": " + npcLine);
		sleepFor(0);

		printDialogueLine(player.getName(), ": " + pick(HOSTILE_PLAYER_FOLLOWUPS));
		sleepFor(0);

		printDialogueLine(npc.getName(), ": " + pick(HOSTILE_NPC_ENDERS));
		return new CommandResponse("");
	}

	// pick a random line from a list
	private static String pick(List<String> options) {
		return options.get(RNG.nextInt(options.size()));
	}

	// pick with optional NPC conversation prefix
	private static String pickWithConversation(List<String> base, String conversation) {
		if (conversation == null || conversation.trim().isEmpty()) {
			return pick(base);
		}
		return conversation + pick(base);
	}

	// simple sleep helper
	private void sleepFor(int pauseTimeMs) {
		try {
			Thread.sleep(pauseTimeMs);
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		}
	}

	// print one dialogue line with a slight typewriter effect
	private void printDialogueLine(String speaker, String text) {
		System.out.printf("%-12s", speaker); // left pad speaker name
		for (char c : text.toCharArray()) {
			System.out.print(c);
			sleepFor(5); // per character delay
		}
		System.out.println();
	}
}
