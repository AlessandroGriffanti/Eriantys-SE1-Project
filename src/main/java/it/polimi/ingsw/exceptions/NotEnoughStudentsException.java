package it.polimi.ingsw.exceptions;

import it.polimi.ingsw.model.Match;

public class NotEnoughStudentsException extends RuntimeException {
    private Match match;

    public NotEnoughStudentsException(int desiredQuantity, int total){
        super("You are trying to draw " + desiredQuantity + ", but there are only " + total);
        notifyException();
    }

    private void notifyException(){
        System.out.println("GAME OVER: no more students to draw!");
        System.out.println("<The match must end>");
    }
}
