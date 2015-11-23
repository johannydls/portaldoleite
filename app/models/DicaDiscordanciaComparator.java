package models;

import java.util.Comparator;

public class DicaDiscordanciaComparator implements Comparator<Dica>{

	@Override
	public int compare(Dica dica, Dica outraDica) {
		
		if(dica.getDiscordancias()>outraDica.getDiscordancias()){
			return -1;
		} else if(dica.getDiscordancias()<outraDica.getDiscordancias()){
			return 1;
		} else{
			return 0;
		}
		  
	}

}
