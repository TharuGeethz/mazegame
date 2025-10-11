package mazegame.control.command;

import mazegame.control.CombatSession;
import mazegame.control.CommandResponse;
import mazegame.control.ParsedInput;

import mazegame.entity.Location;
import mazegame.entity.NonPlayerCharacter;
import mazegame.entity.Player;

import mazegame.entity.utility.WeightLimit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


class JoinPartyCommandTest {
    @BeforeEach
    void setUp() {
        WeightLimit weightLimitTable = WeightLimit.getInstance();
        weightLimitTable.setModifier(10, 66);
        weightLimitTable.setModifier(20, 266);
    }

    @Test
    void whenInCombat_shouldRejectRecruiting() {
        Location loc = new Location("Arena sands", "Arena");
        Player hero = new Player("Hero");

        NonPlayerCharacter maeve = new NonPlayerCharacter(
                "Maeve", 15, 12, 16, "Welcome, traveler.", false);

        //put NPC in location
        loc.getNpcCollection().put("maeve", maeve);

        hero.setCurrentLocation(loc);

        // mock isOver()
        CombatSession mockSession = mock(CombatSession.class);
        when(mockSession.isOver()).thenReturn(false);

        hero.setCombatSession(mockSession);

        JoinPartyCommand cmd = new JoinPartyCommand();
        ParsedInput input = new ParsedInput("join", new ArrayList<>());

        CommandResponse resp = cmd.execute(input, hero);

        assertEquals("You can't recruit allies while in combat!", resp.getMessage());
    }

    @Test
    void whenNamedNpcAlreadyInParty_shouldSayAlreadyJoined() {
        Location loc = new Location("Village green", "Oakheart");
        Player hero = new Player("Hero");
        hero.setCurrentLocation(loc);

        // NPC exists
        NonPlayerCharacter maeve = new NonPlayerCharacter(
                "Maeve", 15, 12, 16, "Welcome, traveler.", false);

        // Simulate already in player's party
        hero.getNpcCollection().put("maeve", maeve);

        JoinPartyCommand cmd = new JoinPartyCommand();
        ParsedInput input = new ParsedInput("join", new ArrayList<>(List.of("Maeve")));

        CommandResponse resp = cmd.execute(input, hero);

        assertEquals("You have already joined Maeve", resp.getMessage());
    }

    @Test
    void whenNoName_given_joinAllEligible_friendlyJoins_hostileRefuses_andCollectionsUpdate() {
        Location loc = new Location("Crystal echoes", "Crystal Cave");
        Player hero = new Player("Hero");
        hero.setCurrentLocation(loc);


        NonPlayerCharacter quartz = new NonPlayerCharacter(
                "Quartz", 13, 12, 17, "I’ll guide you through.", false);
        NonPlayerCharacter snarl = new NonPlayerCharacter(
                "Snarl", 16, 10, 20, "You look tasty.", true);


        Map<String, NonPlayerCharacter> npcsHere = hero.getCurrentLocation().getNpcCollection();
        npcsHere.put("quartz", quartz);
        npcsHere.put("snarl", snarl);

        JoinPartyCommand cmd = new JoinPartyCommand();
        ParsedInput input = new ParsedInput("join", new ArrayList<>()); // no args → recruit everyone possible

        CommandResponse resp = cmd.execute(input, hero);
        String msg = resp.getMessage();


        assertTrue(msg.contains("Quartz has joined your party."),
                "Friendly should join and appear in the message.");
        assertTrue(msg.contains("Snarl snarls and refuses to join you."),
                "Hostile should refuse and appear in the message.");


        // Friendly moved from location to player's party
        assertTrue(hero.getNpcCollection().hasNPC("Quartz"),
                "Friendly NPC should be in player's party after joining.");
        assertFalse(hero.getCurrentLocation().getNpcCollection().containsKey("quartz"),
                "Friendly NPC should be removed from the location map.");

        // Hostile remains at the location and not in player's party
        assertFalse(hero.getNpcCollection().hasNPC("Snarl"),
                "Hostile NPC should not be added to player's party.");
        assertTrue(hero.getCurrentLocation().getNpcCollection().containsKey("snarl"),
                "Hostile NPC should remain at the location.");
    }
}
