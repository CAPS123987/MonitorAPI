package me.caps123987.monitorapi.displays;

public enum RenderMode {
    /**
     * Renders the display for all players on the server using displayEntity on server (text is shared)
     */
    ALL_PLAYERS_SHARED_DISPLAY(true,true),//Todo: check this
    /**
     * Renders the display for all players on the server using packets (text is individual)
     */
    ALL_PLAYERS_INDIVIDUAL_DISPLAYS(true,false),
    /**
     * Renders the display for listed players on the server using displayEntity on server (text is shared)
     */
    LISTED_PLAYERS_SHARED_DISPLAY(false,true),//Todo: check this
    /**
     * Renders the display for listed players on the server using packets (text is individual)
     */
    LISTED_PLAYERS_INDIVIDUAL_DISPLAYS(false,false);
    private boolean allPlayers;
    private boolean sharedDisplay;
    RenderMode(boolean allPlayers, boolean sharedDisplay) {
        this.allPlayers = allPlayers;
        this.sharedDisplay = sharedDisplay;
    }

    public boolean isSharedDisplay() {
        return sharedDisplay;
    }

    public boolean isForAllPlayers() {
        return allPlayers;
    }
}
