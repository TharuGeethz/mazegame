package mazegame.control;

import mazegame.entity.Location;
import mazegame.entity.NonPlayerCharacter;
import mazegame.entity.Player;
import mazegame.entity.item.Weapon;
import mazegame.entity.utility.AgilityTable;
import mazegame.entity.utility.StrengthTable;
import mazegame.entity.utility.WeightLimit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CombatSessionTest {

    private Player player;
    private Location arena;

    private NonPlayerCharacter hostile1;
    private NonPlayerCharacter hostile2;
    private NonPlayerCharacter ally1;

    private CombatSession session;

    @BeforeEach
    void setUp() {
        WeightLimit weightLimitTable = WeightLimit.getInstance();
        weightLimitTable.setModifier(10, 66);

        AgilityTable table = AgilityTable.getInstance();
        table.setModifier(10, 0);

        arena = new Location("simple arena", "Arena");
        player = new Player("Hero", 18, 10, 20); // strong for good STR mod
        player.setCurrentLocation(arena);

        // Give player a weapon
        Weapon longsword = new Weapon("longsword", 15, 4, "1d8");
        player.getInventory().addItem(longsword);
        player.equipItem("longsword");

        // Strength table
        StrengthTable.getInstance().setModifier(18, 4);

        // Hostiles with low AC (easy to hit) and weapons
        hostile1 = new NonPlayerCharacter("Bandit", 10, 10, 10, "Grr", true);
        hostile1.getInventory().addItem(new Weapon("dagger", 1, 1, "1d4"));

        hostile2 = new NonPlayerCharacter("Cutpurse", 10, 10, 10, "Hiss", true);
        hostile2.getInventory().addItem(new Weapon("dagger", 1, 1, "1d4"));

        arena.getNpcCollection().put("bandit", hostile1);
        arena.getNpcCollection().put("cutpurse", hostile2);

        // Ally with weapon
        ally1 = new NonPlayerCharacter("Luna", 12, 10, 12, "Here to help", false);
        ally1.getInventory().addItem(new Weapon("sword, short", 10, 3, "1d6"));
        player.getNpcCollection().put("luna", ally1);

        session = new CombatSession(player);
    }

    // Tests for resolveSingleRound()
    @Test
    void resolveSingleRound_whenNoHostiles_returnsNoEnemiesMessage() {
        arena.getNpcCollection().clear(); // no hostiles
        String log = session.resolveSingleRound();
        assertTrue(log.contains("There are no hostile NPCs here."));
    }

    @Test
    void resolveSingleRound_playerDead_returnsCannotFight() {
        player.setLifePoints(0);
        String log = session.resolveSingleRound();
        assertTrue(log.contains("You cannot fight"));
    }

    @Test
    void resolveSingleRound_runsBothPhasesAndAdvancesRoundOrEnds() {
        String log = session.resolveSingleRound();
        assertTrue(log.contains("===== Round 1 ====="));
        assertTrue(log.contains("— Player Party Phase —"));
        assertTrue(log.contains("— Enemy Party Phase —") || session.isOver());
    }

    // Tests for advanceCombatUntilPlayerTurn()
    @Test
    void advanceCombatUntilPlayerTurn_stopsAtYourTurnPrompt() {
        String log = session.advanceCombatUntilPlayerTurn();
        // Either combat ended very quickly or we reached the "your turn" prompt
        assertTrue(session.isAwaitingPlayerAction() || log.contains("Victory") || log.contains("defeated"));
        if (session.isAwaitingPlayerAction()) {
            assertTrue(log.contains("It’s your turn!"));
        }
    }

    @Test
    void advanceCombatUntilPlayerTurn_handlesEmptyAlliesGracefully() {
        player.getNpcCollection().clear();
        String log = session.advanceCombatUntilPlayerTurn();
        assertTrue(session.isAwaitingPlayerAction() || session.isOver() || log.contains("No one left to act"));
    }

    // Tests for playerAttackAndAdvance(String)
    @Test
    void playerAttackAndAdvance_beforeYourTurn_advancesUntilTurn() {
        // Not awaiting yet
        String first = session.playerAttackAndAdvance("bandit");
        // Either advanced to turn or combat ended
        assertTrue(session.isAwaitingPlayerAction() || session.isOver() || first.contains("Round"));
    }

    @Test
    void playerAttackAndAdvance_withInvalidName_picksRandomTargetAndContinues() {
        session.advanceCombatUntilPlayerTurn(); // reach player turn
        String log = session.playerAttackAndAdvance("no-such-name");
        assertTrue(log.contains("random target") || log.contains("Enemy Party Phase") || session.isOver());
    }

    // Tests for playerSkipTurnAndAdvance(String)

    @Test
    void playerSkipTurnAndAdvance_withoutAwaiting_advancesUntilTurn() {
        String log = session.playerSkipTurnAndAdvance("Not your turn yet");
        assertTrue(session.isAwaitingPlayerAction() || session.isOver() || log.contains("Round"));
    }

    @Test
    void playerSkipTurnAndAdvance_canChainToNextPrompt() {
        session.advanceCombatUntilPlayerTurn();
        String log = session.playerSkipTurnAndAdvance("skip");
        // After enemy phase, session may advance to next prompt or end
        assertTrue(log.contains("— Enemy Party Phase —") || log.contains("Victory") || log.contains("defeated"));
    }

    // Tests for isOver()
    @Test
    void isOver_trueWhenAllEnemiesDefeated() {
        arena.getNpcCollection().clear();
        assertTrue(session.isOver());
    }

    @Test
    void isOver_trueWhenPlayerAndAlliesDefeated() {
        player.setLifePoints(0);
        player.getNpcCollection().clear();
        assertTrue(session.isOver());
    }

    @Test
    void isOver_falseWhenBothSidesHaveCombatants() {
        assertFalse(session.isOver());
    }
}
