package models;

public class TemaIterator implements Iterator {

    private Tema[] temasList;
    private int posicao = 0;

    public TemaIterator(Tema[] temasList) {
        this.temasList = temasList;
    }

    @Override
    public boolean hasNext() {

        if (posicao >= temasList.length || temasList[posicao] == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Object next() {

        Tema tema = temasList[posicao];
        posicao++;

        return tema;
    }
}
