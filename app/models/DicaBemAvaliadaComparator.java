package models;

import java.util.Comparator;

public class DicaBemAvaliadaComparator implements Comparator<Dica> {

    @Override
    public int compare(Dica dica, Dica outraDica) {

        if(dica.getIndiceConcordanciaDouble() > outraDica.getIndiceConcordanciaDouble()){
            return -1;
        } else if(dica.getIndiceConcordanciaDouble() < outraDica.getIndiceConcordanciaDouble()){
            return 1;
        } else{
            return 0;
        }

    }
}
