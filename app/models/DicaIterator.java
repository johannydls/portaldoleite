package models;

public class DicaIterator implements Iterator{

	Dica[] dicaList;
	int posicao = 0;
	
	public DicaIterator(Dica[] dicaList) {

		this.dicaList = dicaList;
	
	}
	
	@Override
	public boolean hasNext() {
		
		if (posicao >= dicaList.length || dicaList[posicao] == null) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public Object next() {
		
		Dica dica = dicaList[posicao];
		posicao++;
		
		return dica;
	}

}
