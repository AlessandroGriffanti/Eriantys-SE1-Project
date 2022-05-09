package it.polimi.ingsw.network.messages;

public class LoginMessage extends Message {
    String nicknameOfPlayer;


    /** Added this constructor otherwise the client handler can't create a new Login message without a parameter (line 77) */

    public LoginMessage(){
        this.object = "login";
    }

    public LoginMessage(String nicknameOfPlayer) {
        this.object = "login";
        this.nicknameOfPlayer = nicknameOfPlayer;
    }

    @Override
    public void sentMessage() {

    }

    public void setNicknameOfPlayer(String nicknameOfPlayer) {
        this.nicknameOfPlayer = nicknameOfPlayer;
    }

    public String getNicknameOfPlayer() {
        return nicknameOfPlayer;
    }
}

/*
public class Person {
    public String name;
    public int age;

    public String getName() {
        return name;
    }

    public Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
 */