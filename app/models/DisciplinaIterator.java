package models;

public class DisciplinaIterator implements Iterator {

    private Disciplina[] disciplinasList;
    private int posicao = 0;

    public DisciplinaIterator(Disciplina[] disciplinasList) {
        this.disciplinasList = disciplinasList;
    }

    @Override
    public boolean hasNext() {

        if (posicao >= disciplinasList.length || disciplinasList[posicao] == null) {
            return false;
        } else {
            return true;
        }
    }

    @Override
    public Object next() {

        Disciplina disciplina = disciplinasList[posicao];
        posicao++;

        return disciplina;
    }
}
