package models;

public class UserIterator implements Iterator {

    private User[] usersList;
    private int posicao = 0;

    public UserIterator(User[] usersList) {

        this.usersList = usersList;

    }

    @Override
    public boolean hasNext() {

        if (posicao >= usersList.length || usersList[posicao] == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Object next() {

        User user = usersList[posicao];
        posicao++;

        return user;
    }
}
