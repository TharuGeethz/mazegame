package mazegame.control;

import mazegame.boundary.IMazeClient;
import mazegame.boundary.IMazeData;
import mazegame.entity.Location;
import mazegame.entity.Player;
import mazegame.entity.utility.WeightLimit;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class DungeonMasterTest {

    private IMazeClient mockClient;
    private IMazeData mockData;
    private DungeonMaster dungeonMaster;
    private CommandHandler mockHandler;

    @BeforeEach
    void setUp() {
        WeightLimit weightLimitTable = WeightLimit.getInstance();
        weightLimitTable.setModifier(10, 66);

        mockClient = mock(IMazeClient.class);
        mockHandler = mock(CommandHandler.class);
        mockData = mock(IMazeData.class);
        dungeonMaster = new DungeonMaster(mockData, mockClient);
    }

    // Tests for printWelcome()
    @Test
    void printWelcome_shouldDisplayWelcomeMessageFromGameData() {
        when(mockData.getWelcomeMessage()).thenReturn("Welcome adventurer!");
        dungeonMaster.printWelcome();
        verify(mockClient).playerMessage("Welcome adventurer!");
    }

    @Test
    void printWelcome_shouldCallGetWelcomeMessageOnce() {
        when(mockData.getWelcomeMessage()).thenReturn("Test welcome");
        dungeonMaster.printWelcome();
        verify(mockData, times(1)).getWelcomeMessage();
    }

    @Test
    void printWelcome_shouldNotThrowExceptionIfMessageNull() {
        when(mockData.getWelcomeMessage()).thenReturn(null);
        assertDoesNotThrow(() -> dungeonMaster.printWelcome());
    }

    // Tests for setupPlayer()
    @Test
    void setupPlayer_shouldCreatePlayerAndSetLocationAndGold() {
        Location startLocation = new Location("Test Description", "Start");
        when(mockClient.getReply(anyString())).thenReturn("Hero");
        when(mockData.getStartingLocation()).thenReturn(startLocation);

        dungeonMaster.setupPlayer();

        verify(mockClient).playerMessage(contains("Welcome Hero"));
        verify(mockClient).playerMessage(contains("You find yourself looking at"));
        verify(mockClient).playerMessage(contains("Test Description"));
    }

    @Test
    void setupPlayer_shouldCallGetReplyWithPrompt() {
        when(mockClient.getReply(anyString())).thenReturn("Hero");
        when(mockData.getStartingLocation()).thenReturn(new Location("Test", "Start"));
        dungeonMaster.setupPlayer();

        verify(mockClient).getReply("What name do you choose to be known by?");
    }

    @Test
    void setupPlayer_shouldNotThrowExceptionIfNoReply() {
        when(mockClient.getReply(anyString())).thenReturn(null);
        when(mockData.getStartingLocation()).thenReturn(new Location("Description", "Start"));
        assertDoesNotThrow(() -> dungeonMaster.setupPlayer());
    }

    // Tests for handlePlayerTurn()
    @Test
    void handlePlayerTurn_shouldReturnTrueWhenGameContinues() throws Exception {

        when(mockClient.getCommand()).thenReturn("move");
        when(mockData.getStartingLocation()).thenReturn(new Location("Test", "Start"));
        dungeonMaster.setupPlayer();
        when(mockHandler.processTurn(anyString(), any(Player.class)))
                .thenReturn(new CommandResponse("You move north", false));

        boolean result = invokeHandlePlayerTurn();

        assertTrue(result);
    }

    @Test
    void handlePlayerTurn_shouldReturnFalseWhenGameFinishes() throws Exception {
        when(mockClient.getCommand()).thenReturn("quit");
        when(mockData.getStartingLocation()).thenReturn(new Location("Test", "Start"));
        when(mockHandler.processTurn(anyString(), any(Player.class)))
                .thenReturn(new CommandResponse("Goodbye!", true));

        dungeonMaster.setupPlayer();
        boolean result = invokeHandlePlayerTurn();

        assertFalse(result);

    }

    // Helper to call private method via reflection
    private boolean invokeHandlePlayerTurn() throws Exception {
        var method = DungeonMaster.class.getDeclaredMethod("handlePlayerTurn");
        method.setAccessible(true);
        return (boolean) method.invoke(dungeonMaster);
    }
}
