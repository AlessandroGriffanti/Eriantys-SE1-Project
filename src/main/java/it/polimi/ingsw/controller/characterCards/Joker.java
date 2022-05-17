package it.polimi.ingsw.controller.characterCards;

import it.polimi.ingsw.model.Creature;

import java.util.ArrayList;

//public class Joker extends Character {

    /** this attribute handles the students on the card */
    //private ArrayList<Creature> studentsOnJoker;

/*
    public Joker(){
        this.price = 1;
        this.studentsOnJoker = new ArrayList<>();
    }
*/
    /** this method handles the "at the beginning of the game..." part of the card. The idea is that, if the joker is one
     * of the 3 character card in the game, we first call this method, at the beginning of the match, and during the match
     * the method effect itself, below, to use the real effect.
     */
    /*
    public void setUpEffect() {
        studentsOnJoker.addAll(getMatch().getBagOfTheMatch().drawStudents(6));
    }

    public void effect(){};
*/

    /** the idea is that the number of times the actions in the effect must be executed will be handled in the controller, so we need
     * @param indexOfPlayer the index of the player using this effect
     * @param indexOfTheStudentOnJoker is the index of the arraylist handling the students on the card and, to each index, it will correspond a creature (the same way as the arraylist in entrance)
     * @param indexOfTheStudentInTheEntrance is the index of the student of the arralist handling the students and, to each student, it will corresponde a creature
     */
    // se non convince l'idea di controllare nel controller quante volte eseguire l'effetto, si può aggiungere un altro
    //parametro che indica il numero di studenti, fino a 3, che si vogliono spostare e fare un ciclo for fino a quel numero.
/*
    public void effect (int indexOfPlayer, int indexOfTheStudentOnJoker, int indexOfTheStudentInTheEntrance ){
        Creature creatureToMoveFromJoker = getStudentsOnJoker().get(indexOfTheStudentOnJoker);
        getMatch().getPlayers().get(indexOfPlayer).getSchoolBoard().getEntrance().addStudent(creatureToMoveFromJoker);


        Creature creatureToMoveFromEntrance = getMatch().getPlayers().get(indexOfPlayer).getSchoolBoard().getEntrance().getStudentsInTheEntrance().get(indexOfTheStudentInTheEntrance);
        getStudentsOnJoker().add(creatureToMoveFromEntrance);
    }

    public ArrayList<Creature> getStudentsOnJoker() {
        return studentsOnJoker;
    }
} */
