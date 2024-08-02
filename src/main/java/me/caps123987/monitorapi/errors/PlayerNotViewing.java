package me.caps123987.monitorapi.errors;

public class PlayerNotViewing extends Exception{
    public PlayerNotViewing(){
        super("This player is not viewing the display! try adding it using InteractiveDisplay#addViewer(Player) or setting the display mode on creation to something else");
    }
}
