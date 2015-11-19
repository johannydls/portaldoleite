import java.util.ArrayList;
import java.util.List;

import models.Disciplina;
import models.Tema;
import models.User;
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
					criaDisciplinaTemas();
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
	
	private void criaDisciplinaTemas(){
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
		dao.persist(si1);

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
		dao.persist(logica);

		//Disciplina: Teoria da Computação
		Disciplina tc = new Disciplina("Teoria da Computação");
		tc.addTema(new Tema("Linguagens Regulares"));
		tc.addTema(new Tema("Autômatos Finitos"));
		tc.addTema(new Tema("Linguagens Livre-de-contexto"));
		tc.addTema(new Tema("Autômatos de Pilha"));
		tc.addTema(new Tema("Máquina de Turing"));
		tc.addTema(new Tema("Decidibilidade"));
		tc.addTema(new Tema("Redução (mapping e Turing)"));
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
