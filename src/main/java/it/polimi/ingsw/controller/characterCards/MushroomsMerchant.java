package it.polimi.ingsw.controller.characterCards;


import it.polimi.ingsw.controller.Controller;
import it.polimi.ingsw.model.Creature;
import it.polimi.ingsw.network.messages.clientMessages.CharacterDataMessage;
import it.polimi.ingsw.network.messages.serverMessages.AckCharactersMessage;

public class MushroomsMerchant extends Character {
    Creature creatureChosen;

    public MushroomsMerchant(Controller controller){
        this.controller = controller;
        this.price = 3;
    }

    /**
     * This method controls if the character can be used (in this case the card can always be used
     * because there are no restrictions)
     * @return always true
     */
    @Override
    public boolean checkCharacterAvailability() {
        return true;
    }

    public void effect(CharacterDataMessage request){
        increasePrice();

        creatureChosen = request.getCreature();
        controller.getCharactersManager().setMushroomsMerchantActive(true);

        int coinReserve = controller.getMatch().getCoinsReserve();
        AckCharactersMessage ack = new AckCharactersMessage(request.getSender_ID(), "mushroomsMerchant", coinReserve);
        ack.setCreature(creatureChosen);

        int coinsOfPlayer = controller.getMatch().getPlayerByID(request.getSender_ID()).getCoinsOwned();
        ack.setPlayerCoins(coinsOfPlayer);
        controller.sendMessageAsBroadcast(ack);
    }

    public Creature getCreatureChosen() {
        return creatureChosen;
    }
}
