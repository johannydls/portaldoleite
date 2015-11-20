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
		si1.addTema(new Tema("Orientação a objetos"));
		si1.addTema(new Tema("GRASP"));
		si1.addTema(new Tema("GoF"));
		si1.addTema(new Tema("Arquitetura"));
		si1.addTema(new Tema("Play"));
		si1.addTema(new Tema("JavaScript"));
		si1.addTema(new Tema("HTML / CSS / Bootstrap"));
		si1.addTema(new Tema("Heroku"));
		si1.addTema(new Tema("Labs"));
		si1.addTema(new Tema("Minitestes"));
		si1.addTema(new Tema("Projeto"));

		//Disciplina: Lógica Matemática
		Disciplina logica = new Disciplina("Lógica Matemática");
		logica.addTema(new Tema("Lógica Proposicional (LP)"));
		logica.addTema(new Tema("Semântica (LP)"));
		logica.addTema(new Tema("Argumentos (LP)"));
		logica.addTema(new Tema("Tableaux Semântico"));
		logica.addTema(new Tema("Dedução (LP)"));
		logica.addTema(new Tema("Formas Normais"));
		logica.addTema(new Tema("SAT Solvers"));
		logica.addTema(new Tema("Lógica de Predicados (Pred)"));
		logica.addTema(new Tema("Semântica (Pred)"));
		logica.addTema(new Tema("Dedução (Pred)"));
		logica.addTema(new Tema("Lógica Temporal"));
		logica.addTema(new Tema("Model Checking"));
		logica.addTema(new Tema("Alloy"));

		//Disciplina: Teoria da Computação
		Disciplina tc = new Disciplina("Teoria da Computação");
		tc.addTema(new Tema("Linguagens Regulares"));
		tc.addTema(new Tema("Autômatos Finitos"));
		tc.addTema(new Tema("Linguagens Livre-de-contexto"));
		tc.addTema(new Tema("Autômatos de Pilha"));
		tc.addTema(new Tema("Máquina de Turing"));
		tc.addTema(new Tema("Decidibilidade"));
		tc.addTema(new Tema("Redução (mapping e Turing)"));

		//Dicas cadastradas
		DicaConselho dica = new DicaConselho();
		dica.setTema(si1.getTemaByNome("Análise x Design"));
		dica.setConselho("Procure sempre fazer uma avaliação");
		dica.setUser("usuario0");
		si1.getTemaByNome("Análise x Design").addDica(dica);

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

		DicaConselho dica3 = new DicaConselho();
		dica3.setTema(si1.getTemaByNome("Labs"));
		dica3.setConselho("Faça o máximo que puder aqui, envie tudo, mesmo que você ache que não irá valer nada.");
		dica3.setUser("usuario3");
		si1.getTemaByNome("Labs").addDica(dica3);

		DicaMaterial dica4 = new DicaMaterial();
		dica4.setTema(si1.getTemaByNome("GoF"));
		dica4.setUrl("http://www.tutorialspoint.com/design_pattern/index.htm");
		dica4.setUser("usuario5");
		si1.getTemaByNome("GoF").addDica(dica4);

		DicaConselho dica5 = new DicaConselho();
		dica5.setTema(logica.getTemaByNome("Alloy"));
		dica5.setConselho("Parece fácil, mas não é. Lembre-se de fazer tudo pensando no Time!");
		dica5.setUser("usuario4");
		logica.getTemaByNome("Alloy").addDica(dica5);

		DicaDisciplina dica6 = new DicaDisciplina();
		dica6.setNomeDisciplina("Matemática Discreta");
		dica6.setRazao("Essa disciplina aborda algumas partes da lógica proposicional e de predicados. " +
				"Vale a pena dar uma revisada nos materiais de Matemática Discreta.");
		dica6.setTema(logica.getTemaByNome("Lógica Proposicional (LP)"));
		dica6.setUser("usuario8");
		logica.getTemaByNome("Lógica Proposicional (LP)").addDica(dica6);

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
