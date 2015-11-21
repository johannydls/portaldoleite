import java.util.ArrayList;
import java.util.List;

import models.*;
import models.dao.GenericDAOImpl;
import play.Application;
import play.GlobalSettings;
import play.Logger;
import play.db.jpa.JPA;


public class Global extends GlobalSettings {

	private static GenericDAOImpl dao = new GenericDAOImpl();
	private List<Disciplina> disciplinas = new ArrayList<>();
	private List<User> usuarios = new ArrayList<>();
	
	@Override
	public void onStart(Application app) {
		Logger.info("Aplicação inicializada...");

		JPA.withTransaction(new play.libs.F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				if(dao.findAllByClassName(Disciplina.class.getName()).size() == 0){
					criaDisciplinaTemasDicas();
				}

				if(dao.findAllByClassName(User.class.getName()).size() == 0) {
					criaUsuarios();
				}
			}
		});
	}
	
	@Override
	public void onStop(Application app){
	    JPA.withTransaction(new play.libs.F.Callback0() {
	    @Override
	    public void invoke() throws Throwable {
	        Logger.info("Aplicação finalizando...");

			disciplinas = dao.findAllByClassName("Disciplina");

	        for (Disciplina disciplina: disciplinas) {
				dao.removeById(Disciplina.class, disciplina.getId());
			}

			usuarios = dao.findAllByClassName("User");

			for (User usuario: usuarios) {
				dao.removeById(User.class, usuario.getId());
			}

	    }}); 
	}
	
	private void criaDisciplinaTemasDicas(){
		//Disciplina: Sistemas de Informação 1
		Disciplina si1 = new Disciplina("Sistemas de Informação 1");
		si1.addTema(new Tema("Análise x Design"));
		Tema a = si1.getTemaByNome("Análise x Design");//Fazer esse padrao para cada tema
		a.setDisciplina(si1);//Aqui add uma disciplina no tema a
		
		si1.addTema(new Tema("Orientação a objetos"));
		Tema a1 = si1.getTemaByNome("Orientação a objetos");//Fazer esse padrao para cada tema
		a1.setDisciplina(si1);
		
		si1.addTema(new Tema("GRASP"));
		Tema a2 = si1.getTemaByNome("GRASP");
		a2.setDisciplina(si1);
		
		si1.addTema(new Tema("GoF"));
		Tema a3 = si1.getTemaByNome("GoF");
		a3.setDisciplina(si1);

		si1.addTema(new Tema("Arquitetura"));
		Tema a4 = si1.getTemaByNome("Arquitetura");
		a4.setDisciplina(si1);

		si1.addTema(new Tema("Play"));
		Tema a5 = si1.getTemaByNome("Play");
		a5.setDisciplina(si1);

		si1.addTema(new Tema("JavaScript"));
		Tema a6 = si1.getTemaByNome("JavaScript");
		a6.setDisciplina(si1);

		si1.addTema(new Tema("HTML / CSS / Bootstrap"));
		Tema a7 = si1.getTemaByNome("HTML / CSS / Bootstrap");
		a7.setDisciplina(si1);

		si1.addTema(new Tema("Heroku"));
		Tema a8 = si1.getTemaByNome("Heroku");
		a8.setDisciplina(si1);

		si1.addTema(new Tema("Labs"));
		Tema a9 = si1.getTemaByNome("Labs");
		a9.setDisciplina(si1);

		si1.addTema(new Tema("Minitestes"));
		Tema a10 = si1.getTemaByNome("Minitestes");
		a10.setDisciplina(si1);

		si1.addTema(new Tema("Projeto"));
		Tema a11 = si1.getTemaByNome("Projeto");
		a11.setDisciplina(si1);
		
		//Disciplina: Lógica Matemática
		Disciplina logica = new Disciplina("Lógica Matemática");
		logica.addTema(new Tema("Lógica Proposicional (LP)"));
		Tema b = logica.getTemaByNome("Lógica Proposicional (LP)");
		b.setDisciplina(logica);

		logica.addTema(new Tema("Semântica (LP)"));
		Tema b1 = logica.getTemaByNome("Semântica (LP)");
		b1.setDisciplina(logica);

		logica.addTema(new Tema("Argumentos (LP)"));
		Tema b2 = logica.getTemaByNome("Argumentos (LP)");
		b2.setDisciplina(logica);

		logica.addTema(new Tema("Tableaux Semântico"));
		Tema b3 = logica.getTemaByNome("Tableaux Semântico");
		b3.setDisciplina(logica);

		logica.addTema(new Tema("Dedução (LP)"));
		Tema b4 = logica.getTemaByNome("Dedução (LP)");
		b4.setDisciplina(logica);

		logica.addTema(new Tema("Formas Normais"));
		Tema b5 = logica.getTemaByNome("Formas Normais");
		b5.setDisciplina(logica);

		logica.addTema(new Tema("SAT Solvers"));
		Tema b6 = logica.getTemaByNome("SAT Solvers");
		b6.setDisciplina(logica);

		logica.addTema(new Tema("Lógica de Predicados (Pred)"));
		Tema b7 = logica.getTemaByNome("Lógica de Predicados (Pred)");
		b7.setDisciplina(logica);

		logica.addTema(new Tema("Semântica (Pred)"));
		Tema b8 = logica.getTemaByNome("Semântica (Pred)");
		b8.setDisciplina(logica);

		logica.addTema(new Tema("Dedução (Pred)"));
		Tema b9 = logica.getTemaByNome("Dedução (Pred)");
		b9.setDisciplina(logica);

		logica.addTema(new Tema("Lógica Temporal"));
		Tema b10 = logica.getTemaByNome("Lógica Temporal");
		b10.setDisciplina(logica);

		logica.addTema(new Tema("Model Checking"));
		Tema b11 = logica.getTemaByNome("Model Checking");
		b11.setDisciplina(logica);

		logica.addTema(new Tema("Alloy"));
		Tema b12 = logica.getTemaByNome("Alloy");
		b12.setDisciplina(logica);

		//Disciplina: Teoria da Computação
		Disciplina tc = new Disciplina("Teoria da Computação");
		tc.addTema(new Tema("Linguagens Regulares"));
		Tema c = tc.getTemaByNome("Linguagens Regulares");
		c.setDisciplina(tc);		

		tc.addTema(new Tema("Autômatos Finitos"));
		Tema c1 = tc.getTemaByNome("Autômatos Finitos");
		c1.setDisciplina(tc);

		tc.addTema(new Tema("Linguagens Livre-de-contexto"));
		Tema c2 = tc.getTemaByNome("Linguagens Livre-de-contexto");
		c2.setDisciplina(tc);

		tc.addTema(new Tema("Autômatos de Pilha"));
		Tema c3 = tc.getTemaByNome("Autômatos de Pilha");
		c3.setDisciplina(tc);

		tc.addTema(new Tema("Máquina de Turing"));
		Tema c4 = tc.getTemaByNome("Máquina de Turing");
		c4.setDisciplina(tc);

		tc.addTema(new Tema("Decidibilidade"));
		Tema c5 = tc.getTemaByNome("Decidibilidade");
		c5.setDisciplina(tc);

		tc.addTema(new Tema("Redução (mapping e Turing)"));
		Tema c6 = tc.getTemaByNome("Redução (mapping e Turing)");
		c6.setDisciplina(tc);

		//Dicas cadastradas
		DicaConselho dica = new DicaConselho();
		dica.setTema(si1.getTemaByNome("Análise x Design"));
		dica.setConselho("Procure sempre fazer uma avaliação");
		dica.setUser("usuario0");
		si1.getTemaByNome("Análise x Design").addDica(dica);
		dica.incrementaConcordancias();
		dica.incrementaConcordancias();
		dica.incrementaConcordancias();

		DicaConselho dica1 = new DicaConselho();
		dica1.setTema(si1.getTemaByNome("Orientação a objetos"));
		dica1.setConselho("Faça uma revisão no seu material de P2.");
		dica1.setUser("usuario1");
		si1.getTemaByNome("Orientação a objetos").addDica(dica1);

		DicaAssunto dica2 = new DicaAssunto();
		dica2.setTema(si1.getTemaByNome("Play"));
		dica2.setAssunto("Não importa o que faça ou quanto estude, você vai sofrer aqui. " +
				"Não existem muitos materiais para aprender o Play, mas comece com a documentação");
		dica2.setUser("usuario2");
		si1.getTemaByNome("Play").addDica(dica2);
		dica2.incrementaConcordancias();

		DicaConselho dica3 = new DicaConselho();
		dica3.setTema(si1.getTemaByNome("Labs"));
		dica3.setConselho("Faça o máximo que puder aqui, envie tudo, mesmo que você ache que não irá valer nada.");
		dica3.setUser("usuario3");
		si1.getTemaByNome("Labs").addDica(dica3);
		dica3.incrementaDiscordancias();

		DicaMaterial dica4 = new DicaMaterial();
		dica4.setTema(si1.getTemaByNome("GoF"));
		dica4.setUrl("http://www.tutorialspoint.com/design_pattern/index.htm");
		dica4.setUser("usuario5");
		si1.getTemaByNome("GoF").addDica(dica4);
		dica4.incrementaConcordancias();
		dica4.incrementaConcordancias();
		dica4.incrementaConcordancias();
		dica4.incrementaConcordancias();
		
		DicaConselho dica5 = new DicaConselho();
		dica5.setTema(logica.getTemaByNome("Alloy"));
		dica5.setConselho("Parece fácil, mas não é. Lembre-se de fazer tudo pensando no Time!");
		dica5.setUser("usuario4");
		logica.getTemaByNome("Alloy").addDica(dica5);
		dica5.incrementaDiscordancias();
		dica5.incrementaDiscordancias();

		DicaDisciplina dica6 = new DicaDisciplina();
		dica6.setNomeDisciplina("Matemática Discreta");
		dica6.setRazao("Essa disciplina aborda algumas partes da lógica proposicional e de predicados. " +
				"Vale a pena dar uma revisada nos materiais de Matemática Discreta.");
		dica6.setTema(logica.getTemaByNome("Lógica Proposicional (LP)"));
		dica6.setUser("usuario8");
		logica.getTemaByNome("Lógica Proposicional (LP)").addDica(dica6);
		dica6.incrementaConcordancias();
		dica6.incrementaConcordancias();
		
		dao.persist(si1);
		dao.persist(logica);
		dao.persist(tc);

		dao.flush();

	}

	private void criaUsuarios() {

		for (int i = 1; i <= 10; i++) {
			String nome = "Usuario " + i;
			String email = "usuario" + i + "@ccc.ufcg.edu.br";
			String nick = "usuario" + i;
			String pass = "12345";

			User usuario = new User(email, pass, nick);
			usuario.setNome(nome);
			dao.persist(usuario);
		}

		dao.flush();
	}
}
