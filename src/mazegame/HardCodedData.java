package mazegame;

import java.util.ArrayList;
import java.util.List;

import mazegame.boundary.IMazeData;

import mazegame.entity.ArmorFactory;
import mazegame.entity.Blacksmith;
import mazegame.entity.Exit;
import mazegame.entity.GameStatus;
import mazegame.entity.Location;
import mazegame.entity.Money;
import mazegame.entity.NonPlayerCharacter;
import mazegame.entity.ShieldFactory;
import mazegame.entity.WeaponFactory;

import mazegame.entity.item.Armor;
import mazegame.entity.item.HealingPotion;
import mazegame.entity.item.Item;
import mazegame.entity.item.MiscellaneousItem;
import mazegame.entity.item.Shield;
import mazegame.entity.item.Weapon;
import mazegame.entity.utility.AgilityTable;
import mazegame.entity.utility.StrengthTable;
import mazegame.entity.utility.WeightLimit;

/**
 * Hard coded data initializer. Builds items, NPCs, locations, exits and lookup
 * tables for a player map.
 */
public class HardCodedData implements IMazeData {
	private Location forestClearing; // starting location reference

	// construct and load all static data
	public HardCodedData() {
		loadWeapons(); // weapon catalog
		loadArmors(); // armor catalog
		loadShields(); // shield catalog
		loadWeightLimits(); // carry capacity table
		loadAgilityModifiers(); // agility modifiers
		loadStrengthModifiers(); // strength modifiers
		createLocations(); // map, exits, items, NPCs
	}

	// return the initial player location
	public Location getStartingLocation() {
		return forestClearing;
	}

	// welcome banner text
	public String getWelcomeMessage() {
		return "Welcome to the Mount Helanous!  Find the banner, reach the castle and defeat Gregor!";
	}

	// build locations, connect exits, add items and NPCs
	private void createLocations() {
		forestClearing = new Location(
				"A quiet forest clearing with a dying campfire and trampled grass. Paths lead out through the trees.",
				"Forest Clearing");

		Location echoingCatacombs = new Location(
				"Endless stone corridors stretch beneath the earth. Torches barely pierce the shadows, and every sound seems to come back twice as loud. The air reeks of dust and bones.",
				"Echoing Catacombs");

		Location oakheartVillage = new Location(
				"A warm and welcoming village built around the trunk of a massive oak tree. Families live in timber cottages, and villagers happily greet travelers who arrive.",
				"Oakheart Village");

		Location whisperingMarsh = new Location(
				"The air is thick and damp, filled with the croak of unseen creatures. Strange mists curl around twisted trees, and travelers swear they hear voices calling their names.",
				"Whispering Marsh");

		Location townSquare = new Location(
				"The bustling heart of Mount Helenis. A notice board, a fountain, and crowds of traders.",
				"Town Square");

		Location innOfTheBoar = new Location(
				"A warm, noisy tavern filled with chatter. A boar crest hangs above the bar. Don't forget your banner!",
				"Inn of the Boar");

		Blacksmith forgeOfHelan = new Blacksmith(
				"A fire-bright forge. Racks of blades and armor line the walls. The smith eyes your gear.",
				"Forge of Helan");

		Blacksmith titansAnvil = new Blacksmith(
				"Compact smithy south to the inn. Practical gear, fair prices, fewer questions.", "The Titan's Anvil");

		Location crystalCave = new Location(
				"The Cave walls sparkle with embedded crystals, casting rainbow reflections across the stone. The beauty is breathtaking, but the dripping of unseen water echoes eerily.",
				"Crystal Cave");

		Location castleDrawbridge = new Location(
				"The looming walls of Gregor’s castle. A raised drawbridge blocks the only entrance.",
				"Castle Drawbridge");

		// connect locations with exits

		// Forest Clearing <-> Whispering Marsh
		forestClearing.getExitCollection().addExit("south",
				new Exit("A packed-dirt road lies to the south into Whispering Marsh.", whisperingMarsh));
		whisperingMarsh.getExitCollection().addExit("north",
				new Exit("The path opens into a quiet forest clearing.", forestClearing));

		// Forest Clearing <-> Echoing Catacombs
		forestClearing.getExitCollection().addExit("west",
				new Exit("A cold draft seeps from a stairwell descending into the catacombs.", echoingCatacombs));
		echoingCatacombs.getExitCollection().addExit("east",
				new Exit("You climb from the catacombs back into a quiet forest clearing.", forestClearing));

		// Forest Clearing <-> Oakheart Village
		forestClearing.getExitCollection().addExit("east",
				new Exit("A well-worn path leads toward a friendly village beneath a great oak.", oakheartVillage));
		oakheartVillage.getExitCollection().addExit("west",
				new Exit("The lane heads back to a quiet forest clearing.", forestClearing));

		// Whispering Marsh <-> Town Square
		whisperingMarsh.getExitCollection().addExit("south",
				new Exit("Town gates and the busy square lie ahead.", townSquare));
		townSquare.getExitCollection().addExit("north",
				new Exit("The road back toward the Whispering Marsh.", whisperingMarsh));

		// Town Square <-> Inn of the Boar
		townSquare.getExitCollection().addExit("east", new Exit("The inn’s lanterns glow invitingly.", innOfTheBoar));
		innOfTheBoar.getExitCollection().addExit("west", new Exit("You step back into the town square.", townSquare));

		// Town Square <-> Forge of Helan [Shop]
		townSquare.getExitCollection().addExit("west",
				new Exit("The ring of hammer on steel comes from the west.", forgeOfHelan));
		forgeOfHelan.getExitCollection().addExit("east",
				new Exit("The square lies just beyond the forge.", townSquare));

		// Inn of the Boar <-> The Titan's Anvil [Shop]
		innOfTheBoar.getExitCollection().addExit("south",
				new Exit("A rear door opens to a yard and smithy.", titansAnvil));
		titansAnvil.getExitCollection().addExit("north", new Exit("A back path leads toward the inn.", innOfTheBoar));

		// Town Square <-> Castle Drawbridge with lock
		Exit townToCastle = new Exit("The road climbs toward the Gregor's castle.", castleDrawbridge);
		townToCastle.setLocked(true); // locked until banner condition
		townSquare.getExitCollection().addExit("southwest", townToCastle);
		castleDrawbridge.getExitCollection().addExit("northeast",
				new Exit("The town square is just near by.", townSquare));

		// Town Square <-> Crystal Cave
		townSquare.getExitCollection().addExit("south",
				new Exit("A narrow path leads toward a glittering cave entrance.", crystalCave));
		crystalCave.getExitCollection().addExit("north",
				new Exit("You follow the tunnel upward and emerge into the bustling town square.", townSquare));

		// Crystal Cave <-> Castle Drawbridge with lock
		Exit caveToCastle = new Exit("A rough stone passage leads toward the looming shape of Gregor's castle.",
				castleDrawbridge);
		caveToCastle.setLocked(true); // locked until banner condition
		crystalCave.getExitCollection().addExit("west", caveToCastle);
		castleDrawbridge.getExitCollection().addExit("east", new Exit(
				"You leave the shadow of the castle and descend into the glittering crystal cave.", crystalCave));

		// place items in locations

		// Forest Clearing
		addItemsToLocation(List.of("nunchaku", "dagger"), List.of("leather"), List.of("shield, small, wooden"),
				forestClearing);

		// Echoing Catacombs
		addItemsToLocation(List.of("waraxe, dwarven", "flail, light", "warhammer"), List.of(), List.of(),
				echoingCatacombs);
		echoingCatacombs.getInventory().setGold(new Money(500)); // stash gold

		// Oakheart Village
		addItemsToLocation(List.of("trident"), List.of(), List.of(), oakheartVillage);

		// Whispering Marsh
		addItemsToLocation(List.of("axe, throwing", "greatclub"), List.of("hide"), List.of(), whisperingMarsh);

		// Town Square
		addItemsToLocation(List.of("rapier", "flail, heavy"), List.of("chainmail"), List.of("shield, small, steel"),
				townSquare);

		// Forge of Helan [Shop]
		addItemsToLocation(List.of("greatsword", "greataxe", "sword, two-bladed", "falchion"),
				List.of("full plate", "half-plate", "banded mail"), List.of("shield, large, steel"), forgeOfHelan);

		// The Titan's Anvil [Shop]
		addItemsToLocation(List.of("halberd", "battleaxe", "longsword", "scimitar"),
				List.of("splint mail", "breastplate"), List.of(), titansAnvil);

		// Inn of the Boar
		addItemsToLocation(List.of("sword, short", "longspear", "guisarme", "handaxe"),
				List.of("studded leather", "scale mail"), List.of("buckler", "shield, large, wooden"), innOfTheBoar);
		innOfTheBoar.getInventory().setGold(new Money(1000)); // gold
		MiscellaneousItem banner = new MiscellaneousItem("banner", 1000, 1); // quest item
		innOfTheBoar.getInventory().addItem(banner);

		// Crystal Cave
		addItemsToLocation(List.of("glaive", "ranseur", "sword, bastard"), List.of("chain shirt"), List.of(),
				crystalCave);

		// Castle Drawbridge
		addItemsToLocation(List.of("scythe"), List.of("padded"), List.of(), castleDrawbridge);

		// add healing potions
		forestClearing.getInventory()
				.addPotion(new HealingPotion("elixir of vitality", "Restores health and boosts stamina temporarily."));
		townSquare.getInventory()
				.addPotion(new HealingPotion("sacred flask", "A blessed potion said to heal wounds instantly."));
		townSquare.getInventory().addPotion(new HealingPotion("moonflower extract",
				"A glowing liquid made from rare moonflowers, heals swiftly under moonlight."));
		innOfTheBoar.getInventory().addPotion(new HealingPotion("dwarven tonic",
				"A strong brew favored by dwarves, restores health and hardens resolve."));
		innOfTheBoar.getInventory().addPotion(new HealingPotion("crystal dewdrop",
				"Collected at dawn from enchanted crystals, restores clarity and health."));
		whisperingMarsh.getInventory().addPotion(new HealingPotion("phoenix feather elixir",
				"Infused with phoenix magic, restores health and renews energy."));
		castleDrawbridge.getInventory().addPotion(new HealingPotion("dragonfruit brew",
				"A fiery red potion brewed from dragonfruit, heals and invigorates."));

		// add NPCs to locations

		// Echoing Catacombs hostile NPCs
		List<NonPlayerCharacter> npcsEchoing = new java.util.ArrayList<>();

		NonPlayerCharacter Shade = new NonPlayerCharacter("Shade", 10, 10, 18,
				"Footsteps… not yours. Turn back while you can.", true);
		addItemsToNPC(java.util.List.of("rapier"), java.util.List.of("chain shirt"), java.util.List.of("buckler"),
				Shade);
		npcsEchoing.add(Shade);

		NonPlayerCharacter Bone = new NonPlayerCharacter("Bone", 12, 10, 15, "The dead are restless—and so am I.",
				true);
		addItemsToNPC(java.util.List.of("scythe"), java.util.List.of("hide"),
				java.util.List.of("shield, small, wooden"), Bone);
		npcsEchoing.add(Bone);

		echoingCatacombs.setNpcs(npcsEchoing, true);

		// Oakheart Village allies
		java.util.List<NonPlayerCharacter> npcsOakheart = new java.util.ArrayList<>();

		NonPlayerCharacter Maeve = new NonPlayerCharacter("Maeve", 15, 12, 16,
				"Welcome, traveler. Rest comes easy under the oak.", false);
		addItemsToNPC(java.util.List.of("dagger"), java.util.List.of("leather"), java.util.List.of("buckler"), Maeve);
		npcsOakheart.add(Maeve);

		NonPlayerCharacter Rowan = new NonPlayerCharacter("Rowan", 22, 13, 17,
				"Road’s clear to the east today. I’ll walk with you if you like.", false);
		addItemsToNPC(java.util.List.of("longspear"), java.util.List.of("studded leather"),
				java.util.List.of("shield, large, wooden"), Rowan);
		npcsOakheart.add(Rowan);

		NonPlayerCharacter Tamsin = new NonPlayerCharacter("Tamsin", 19, 11, 14,
				"Stories keep us safe as steel does—listen close.", false);
		addItemsToNPC(java.util.List.of("nunchaku"), java.util.List.of("padded"), java.util.List.of(), Tamsin);
		npcsOakheart.add(Tamsin);

		oakheartVillage.setNpcs(npcsOakheart, false);

		// Town Square allies
		List<NonPlayerCharacter> npcsTownSquare = new ArrayList<>();

		NonPlayerCharacter Lysa = new NonPlayerCharacter("Lysa", 12, 13, 16,
				"You look like you can handle yourself. Need another support blade?", false);
		addItemsToNPC(List.of("rapier"), List.of("studded leather"), List.of("buckler"), Lysa);
		npcsTownSquare.add(Lysa);

		NonPlayerCharacter Bram = new NonPlayerCharacter("Bram", 14, 11, 18,
				"Gregor’s thugs bully these streets. I’m keen to push back.", false);
		addItemsToNPC(List.of("longsword"), List.of("chain shirt"), List.of("shield, large, steel"), Bram);
		npcsTownSquare.add(Bram);

		NonPlayerCharacter Piper = new NonPlayerCharacter("Piper", 11, 14, 14,
				"If you’re hunting trouble, I’ll watch your flank.", false);
		addItemsToNPC(List.of("sword, short"), List.of("leather"), List.of("shield, small, wooden"), Piper);
		npcsTownSquare.add(Piper);

		townSquare.setNpcs(npcsTownSquare, false);

		// Crystal Cave allies
		List<NonPlayerCharacter> npcsCrystalCave = new ArrayList<>();

		NonPlayerCharacter Quartz = new NonPlayerCharacter("Quartz", 13, 12, 17,
				"These crystals hum when danger’s near. I’ll guide you through.", false);
		addItemsToNPC(List.of("trident"), List.of("breastplate"), List.of("shield, large, steel"), Quartz);
		npcsCrystalCave.add(Quartz);

		NonPlayerCharacter Iris = new NonPlayerCharacter("Iris", 12, 13, 15,
				"Stay close—the echoes play tricks. I’ll keep watch.", false);
		addItemsToNPC(List.of("scimitar"), List.of("chain shirt"), List.of("buckler"), Iris);
		npcsCrystalCave.add(Iris);

		NonPlayerCharacter lostMiner = new NonPlayerCharacter("Edrin", 11, 10, 14,
				"Been chasin’ a gleam that keeps movin’… can’t find the way out.", false);
		addItemsToNPC(List.of("handaxe"), List.of("leather"), List.of(), lostMiner);
		npcsCrystalCave.add(lostMiner);

		crystalCave.setNpcs(npcsCrystalCave, false);

		// Inn of the Boar hostile group
		List<NonPlayerCharacter> npcsInnOfTheBoar = new ArrayList<>();

		NonPlayerCharacter Philip = new NonPlayerCharacter("Philip", 18, 12, 20,
				"You think you can waltz in here and take what's mine? I've crushed tougher fools than you.", true);
		addItemsToNPC(List.of("greatsword"), List.of("full plate"), List.of("shield, large, steel"), Philip);
		npcsInnOfTheBoar.add(Philip);

		NonPlayerCharacter Zyro = new NonPlayerCharacter("Zyro", 14, 13, 12,
				"You picked the wrong inn to cause trouble in.", true);
		addItemsToNPC(List.of("longsword"), List.of("banded mail"), List.of("buckler"), Zyro);
		npcsInnOfTheBoar.add(Zyro);

		NonPlayerCharacter Lurc = new NonPlayerCharacter("Lurc", 17, 9, 10,
				"Philip's word is law here. Time to learn that the hard way.", true);
		addItemsToNPC(List.of("battleaxe"), List.of("splint mail"), List.of("shield, large, wooden"), Lurc);
		npcsInnOfTheBoar.add(Lurc);

		innOfTheBoar.setNpcs(npcsInnOfTheBoar, true);

		// Castle Drawbridge boss group
		List<NonPlayerCharacter> npcsDrawbridge = new ArrayList<>();

		NonPlayerCharacter Brute = new NonPlayerCharacter("Brute", 16, 8, 14,
				"You think you can pass? Not while I breathe.", true);
		addItemsToNPC(List.of("greataxe"), List.of("chainmail"), List.of("shield, large, steel"), Brute);
		npcsDrawbridge.add(Brute);

		NonPlayerCharacter Karg = new NonPlayerCharacter("Karg", 18, 10, 16,
				"Banner? Ha! we'll rip your arms off and sew them to ours.", true);
		addItemsToNPC(List.of("greatsword"), List.of("full plate"), List.of("shield, large, steel"), Karg);
		npcsDrawbridge.add(Karg);

		NonPlayerCharacter Gregor = new NonPlayerCharacter("Gregor", 25, 12, 25,
				"You smell like fear. Come closer so I can enjoy it.", true);
		addItemsToNPC(List.of("halberd"), List.of("splint mail"), List.of("shield, large, wooden"), Gregor);
		npcsDrawbridge.add(Gregor);

		NonPlayerCharacter Rusk = new NonPlayerCharacter("Rusk", 15, 9, 19,
				"Step up, fool; we'll teach you a lesson you won't forget.", true);
		addItemsToNPC(List.of("warhammer"), List.of("banded mail"), List.of("shield, small, steel"), Rusk);
		npcsDrawbridge.add(Rusk);

		NonPlayerCharacter Thal = new NonPlayerCharacter("Thal", 14, 11, 19, "Banner or blood —  choose quickly.",
				true);
		addItemsToNPC(List.of("greatsword"), List.of("half-plate"), List.of("shield, large, steel"), Thal);
		npcsDrawbridge.add(Thal);

		castleDrawbridge.setNpcs(npcsDrawbridge, true);

		// register all locations and shops
		GameStatus.getInstance().addLocation(forestClearing);
		GameStatus.getInstance().addLocation(echoingCatacombs);
		GameStatus.getInstance().addLocation(oakheartVillage);
		GameStatus.getInstance().addLocation(whisperingMarsh);
		GameStatus.getInstance().addLocation(townSquare);
		GameStatus.getInstance().addLocation(innOfTheBoar);
		GameStatus.getInstance().addShop(forgeOfHelan);
		GameStatus.getInstance().addShop(titansAnvil);
		GameStatus.getInstance().addLocation(crystalCave);
		GameStatus.getInstance().addLocation(castleDrawbridge);
	}

	// helper to add items to a location inventory
	private void addItemsToLocation(List<String> weaponList, List<String> armorList, List<String> shieldList,
			Location location) {
		List<Item> itemsForLocation = new ArrayList<>();
		weaponList.stream().filter(weaponLabel -> !weaponLabel.trim().isEmpty())
				.forEach(weaponLabel -> itemsForLocation.add(getWeapon(weaponLabel)));
		armorList.stream().filter(armorLabel -> !armorLabel.trim().isEmpty())
				.forEach(armorLabel -> itemsForLocation.add(getArmor(armorLabel)));
		shieldList.stream().filter(shieldLabel -> !shieldLabel.trim().isEmpty())
				.forEach(shieldLabel -> itemsForLocation.add(getShield(shieldLabel)));
		location.getInventory().addItems(itemsForLocation); // bulk add
	}

	// helper to add items to an NPC inventory
	private void addItemsToNPC(List<String> weaponList, List<String> armorList, List<String> shieldList,
			NonPlayerCharacter npc) {
		List<Item> itemsForNPC = new ArrayList<>();
		weaponList.stream().filter(weaponLabel -> !weaponLabel.trim().isEmpty())
				.forEach(weaponLabel -> itemsForNPC.add(getWeapon(weaponLabel)));
		armorList.stream().filter(armorLabel -> !armorLabel.trim().isEmpty())
				.forEach(armorLabel -> itemsForNPC.add(getArmor(armorLabel)));
		shieldList.stream().filter(shieldLabel -> !shieldLabel.trim().isEmpty())
				.forEach(shieldLabel -> itemsForNPC.add(getShield(shieldLabel)));
		npc.getInventory().addItems(itemsForNPC); // bulk add
	}

	// weapon lookup from factory
	private Weapon getWeapon(String weaponLabel) {
		return WeaponFactory.getInstance().getWeapon(weaponLabel);
	}

	// armor lookup from factory
	private Armor getArmor(String armorLabel) {
		return ArmorFactory.getInstance().getArmor(armorLabel);
	}

	// shield lookup from factory
	private Shield getShield(String shieldLabel) {
		return ShieldFactory.getInstance().getShield(shieldLabel);
	}

	// populate weapon catalog
	private void loadWeapons() {
		WeaponFactory weaponFactory = WeaponFactory.getInstance();
		weaponFactory.addWeapon(new Weapon("dagger", 1, 2, "1d4"));
		weaponFactory.addWeapon(new Weapon("nunchaku", 2, 2, "1d6"));
		weaponFactory.addWeapon(new Weapon("greatclub", 5, 10, "1d10"));
		weaponFactory.addWeapon(new Weapon("longspear", 5, 9, "1d8"));
		weaponFactory.addWeapon(new Weapon("handaxe", 6, 5, "1d6"));
		weaponFactory.addWeapon(new Weapon("axe, throwing", 8, 4, "1d6"));
		weaponFactory.addWeapon(new Weapon("flail, light", 8, 5, "1d8"));
		weaponFactory.addWeapon(new Weapon("glaive", 8, 15, "1d10"));
		weaponFactory.addWeapon(new Weapon("guisarme", 9, 15, "2d4"));
		weaponFactory.addWeapon(new Weapon("sword, short", 10, 3, "1d6"));
		weaponFactory.addWeapon(new Weapon("battleaxe", 10, 7, "1d8"));
		weaponFactory.addWeapon(new Weapon("halberd", 10, 15, "1d10"));
		weaponFactory.addWeapon(new Weapon("ranseur", 10, 15, "2d4"));
		weaponFactory.addWeapon(new Weapon("warhammer", 12, 8, "1d8"));
		weaponFactory.addWeapon(new Weapon("longsword", 15, 4, "1d8"));
		weaponFactory.addWeapon(new Weapon("scimitar", 15, 4, "1d6"));
		weaponFactory.addWeapon(new Weapon("trident", 15, 5, "1d8"));
		weaponFactory.addWeapon(new Weapon("flail, heavy", 15, 20, "1d10"));
		weaponFactory.addWeapon(new Weapon("scythe", 18, 12, "2d4"));
		weaponFactory.addWeapon(new Weapon("rapier", 20, 3, "1d6"));
		weaponFactory.addWeapon(new Weapon("greataxe", 20, 20, "1d12"));
		weaponFactory.addWeapon(new Weapon("waraxe, dwarven", 30, 15, "1d10"));
		weaponFactory.addWeapon(new Weapon("sword, bastard", 35, 10, "1d10"));
		weaponFactory.addWeapon(new Weapon("greatsword", 50, 15, "2d6"));
		weaponFactory.addWeapon(new Weapon("falchion", 75, 16, "2d4"));
		weaponFactory.addWeapon(new Weapon("sword, two-bladed", 100, 15, "2d8"));
	}

	// populate armor catalog
	private void loadArmors() {
		ArmorFactory armorFactory = ArmorFactory.getInstance();
		armorFactory.addArmor(new Armor("padded", 5, 10, 1));
		armorFactory.addArmor(new Armor("leather", 10, 15, 2));
		armorFactory.addArmor(new Armor("studded leather", 25, 20, 3));
		armorFactory.addArmor(new Armor("chain shirt", 100, 25, 4));
		armorFactory.addArmor(new Armor("hide", 15, 25, 3));
		armorFactory.addArmor(new Armor("scale mail", 50, 30, 4));
		armorFactory.addArmor(new Armor("chainmail", 150, 40, 5));
		armorFactory.addArmor(new Armor("breastplate", 200, 30, 5));
		armorFactory.addArmor(new Armor("splint mail", 200, 45, 6));
		armorFactory.addArmor(new Armor("banded mail", 250, 35, 6));
		armorFactory.addArmor(new Armor("half-plate", 600, 50, 7));
		armorFactory.addArmor(new Armor("full plate", 1500, 50, 8));
	}

	// populate shield catalog
	private void loadShields() {
		ShieldFactory shieldFactory = ShieldFactory.getInstance();
		shieldFactory.addShield(new Shield("buckler", 15, 5, 1));
		shieldFactory.addShield(new Shield("shield, small, wooden", 3, 5, 1));
		shieldFactory.addShield(new Shield("shield, small, steel", 9, 6, 1));
		shieldFactory.addShield(new Shield("shield, large, wooden", 7, 10, 2));
		shieldFactory.addShield(new Shield("shield, large, steel", 20, 15, 2));
	}

	// fill strength-based carry capacity
	private void loadWeightLimits() {
		WeightLimit weightLimitTable = WeightLimit.getInstance();
		weightLimitTable.setModifier(1, 6);
		weightLimitTable.setModifier(2, 13);
		weightLimitTable.setModifier(3, 20);
		weightLimitTable.setModifier(4, 26);
		weightLimitTable.setModifier(5, 33);
		weightLimitTable.setModifier(6, 40);
		weightLimitTable.setModifier(7, 46);
		weightLimitTable.setModifier(8, 53);
		weightLimitTable.setModifier(9, 60);
		weightLimitTable.setModifier(10, 66);
		weightLimitTable.setModifier(11, 76);
		weightLimitTable.setModifier(12, 86);
		weightLimitTable.setModifier(13, 100);
		weightLimitTable.setModifier(14, 116);
		weightLimitTable.setModifier(15, 133);
		weightLimitTable.setModifier(16, 153);
		weightLimitTable.setModifier(17, 173);
		weightLimitTable.setModifier(18, 200);
		weightLimitTable.setModifier(19, 233);
		weightLimitTable.setModifier(20, 266);
		weightLimitTable.setModifier(21, 306);
		weightLimitTable.setModifier(22, 346);
		weightLimitTable.setModifier(23, 400);
		weightLimitTable.setModifier(24, 466);
		weightLimitTable.setModifier(25, 533);
		weightLimitTable.setModifier(26, 613);
		weightLimitTable.setModifier(27, 693);
		weightLimitTable.setModifier(28, 800);
		weightLimitTable.setModifier(29, 933);
	}

	// fill agility modifier lookup
	private void loadAgilityModifiers() {
		AgilityTable table = AgilityTable.getInstance();

		table.setModifier(1, -5);
		table.setModifier(2, -5);
		table.setModifier(3, -5);
		table.setModifier(4, -3);
		table.setModifier(5, -3);
		table.setModifier(6, -2);
		table.setModifier(7, -2);
		table.setModifier(8, -1);
		table.setModifier(9, -1);
		table.setModifier(10, 0);
		table.setModifier(11, 0);
		table.setModifier(12, 1);
		table.setModifier(13, 1);
		table.setModifier(14, 2);
		table.setModifier(15, 2);
		table.setModifier(16, 3);
		table.setModifier(17, 3);
		table.setModifier(18, 4);
		table.setModifier(19, 4);
		table.setModifier(20, 6);
		table.setModifier(21, 6);
		table.setModifier(22, 6);
		table.setModifier(23, 6);
		table.setModifier(24, 6);
		table.setModifier(25, 6);
		table.setModifier(26, 8);
		table.setModifier(27, 8);
		table.setModifier(28, 8);
		table.setModifier(29, 8);
		table.setModifier(30, 8);
		table.setModifier(31, 8);
		table.setModifier(32, 11);
	}

	// fill strength modifier lookup
	private void loadStrengthModifiers() {
		StrengthTable table = StrengthTable.getInstance();
		table.setModifier(1, -5);
		table.setModifier(2, -4);
		table.setModifier(3, -4);
		table.setModifier(4, -3);
		table.setModifier(5, -3);
		table.setModifier(6, -2);
		table.setModifier(7, -2);
		table.setModifier(8, -1);
		table.setModifier(9, -1);
		table.setModifier(10, 0);
		table.setModifier(11, 0);
		table.setModifier(12, 1);
		table.setModifier(13, 1);
		table.setModifier(14, 2);
		table.setModifier(15, 2);
		table.setModifier(16, 3);
		table.setModifier(17, 3);
		table.setModifier(18, 4);
		table.setModifier(19, 4);
		table.setModifier(20, 5);
		table.setModifier(21, 5);
		table.setModifier(22, 5);
		table.setModifier(23, 5);
		table.setModifier(24, 5);
		table.setModifier(25, 5);
		table.setModifier(26, 8);
		table.setModifier(27, 8);
		table.setModifier(28, 8);
		table.setModifier(29, 8);
		table.setModifier(30, 10);
		table.setModifier(31, 10);
		table.setModifier(32, 10);
		table.setModifier(33, 10);
		table.setModifier(34, 10);
		table.setModifier(35, 10);
		table.setModifier(36, 12);
		table.setModifier(37, 12);
		table.setModifier(38, 12);
		table.setModifier(39, 12);
		table.setModifier(40, 12);
		table.setModifier(41, 15);
		table.setModifier(42, 15);
		table.setModifier(43, 15);
		table.setModifier(44, 17);
		table.setModifier(45, 17);
		table.setModifier(46, 18);
	}
}
