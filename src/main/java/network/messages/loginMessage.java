package network.messages;

public class loginMessage extends message {
    String nicknameOfPlayer;

    public void setNicknameOfPlayer(String nicknameOfPlayer) {
        this.nicknameOfPlayer = nicknameOfPlayer;
    }

    public String getNicknameOfPlayer() {
        return nicknameOfPlayer;
    }

    public loginMessage() {
        this.object = "login";
        this.nicknameOfPlayer = new String();   //non necessario
    }

    @Override
    public void sentMessage() {

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