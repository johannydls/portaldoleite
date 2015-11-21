package controllers;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import models.*;
import models.dao.GenericDAOImpl;
import play.Logger;
import play.data.DynamicForm;
import play.data.Form;
import play.db.jpa.Transactional;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;

public class Application extends Controller {
	private static final int MAX_DENUNCIAS = 3;
	private static GenericDAOImpl dao = new GenericDAOImpl();
	
	@Transactional
	@Security.Authenticated(Secured.class)
    public static Result index() {
		List<Disciplina> disciplinas = dao.findAllByClassName(Disciplina.class.getName());
		List<User> usuarios = dao.findAllByClassName(User.class.getName());	
		List<Dica> dicas = dao.findAllByClassName(Dica.class.getName());	
        return ok(views.html.index.render(disciplinas, usuarios, dicas));
    }
	
	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result tema(long id) {
		List<Disciplina> listaDisciplina = dao.findAllByClassName(Disciplina.class.getName());
		Tema tema = dao.findByEntityId(Tema.class, id);
		if(tema == null){
			return erro();
		}
		return ok(views.html.tema.render(listaDisciplina, tema));
	}
	
	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result disciplina(long id) {
		List<Disciplina> listaDisciplina = dao.findAllByClassName(Disciplina.class.getName());
		Disciplina disciplina = dao.findByEntityId(Disciplina.class, id);
		if(disciplina == null){
			return erro();
		}
		return ok(views.html.disciplina.render(listaDisciplina, disciplina, false));
	}
	
	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result disciplinaErro(Disciplina disciplina) {
		List<Disciplina> listaDisciplina = dao.findAllByClassName(Disciplina.class.getName());
		return ok(views.html.disciplina.render(listaDisciplina, disciplina, true));
	}
	
	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result metadica(long id) {
		List<Disciplina> listaDisciplina = dao.findAllByClassName(Disciplina.class.getName());
		MetaDica metadica = dao.findByEntityId(MetaDica.class, id);
		Disciplina disciplina = metadica.getDisciplina();
		if(disciplina == null || metadica == null){
			return erro();
		}
		
		return ok(views.html.metadica.render(listaDisciplina, disciplina, metadica));
	}
	
	@Transactional
	public static Result erro(){
		List<Disciplina> disciplinas = dao.findAllByClassName(Disciplina.class.getName());
		return ok(views.html.erro.render(disciplinas));
	}
	
	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result cadastrarDica(long idTema) {
		
		DynamicForm filledForm = Form.form().bindFromRequest();
		
		Map<String,String> formMap = filledForm.data();
		
		//long idTema = Long.parseLong(formMap.get("idTema"));
		
		Tema tema = dao.findByEntityId(Tema.class, idTema);
		String userName = session("username");
		
		if (filledForm.hasErrors()) {
			return tema(idTema);
		} else {
			String tipoKey = formMap.get("tipo");
			switch (tipoKey) {
				case "assunto":
					String assunto = formMap.get("assunto");
					DicaAssunto dicaAssunto = new DicaAssunto(assunto);
					
					tema.addDica(dicaAssunto);
					dicaAssunto.setTema(tema);
					dicaAssunto.setUser(userName);
					dao.persist(dicaAssunto);				
					break;
				case "conselho":
					String conselho = formMap.get("conselho");
					DicaConselho dicaConselho = new DicaConselho(conselho);
					
					tema.addDica(dicaConselho);
					dicaConselho.setTema(tema);
					dicaConselho.setUser(userName);
					dao.persist(dicaConselho);				
					break;
				case "disciplina":
					String disciplinas = formMap.get("disciplinas");
					String razao = formMap.get("razao");
					
					DicaDisciplina dicaDisciplina = new DicaDisciplina(disciplinas, razao);
					
					tema.addDica(dicaDisciplina);
					dicaDisciplina.setTema(tema);
					dicaDisciplina.setUser(userName);
					dao.persist(dicaDisciplina);
					break;
				case "material":
					String url = formMap.get("url");
					DicaMaterial dicaMaterial = new DicaMaterial(url);
									
					tema.addDica(dicaMaterial);
					dicaMaterial.setTema(tema);
					dicaMaterial.setUser(userName);
					dao.persist(dicaMaterial);				
					break;
				default:
					break;
			}
			
			dao.merge(tema);
			
			dao.flush();			
			
			return redirect(routes.Application.tema(idTema));
		}
	}
	
	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result avaliarDificuldadeTema(long idTema) {
		DynamicForm filledForm = Form.form().bindFromRequest();
		if (filledForm.hasErrors()) {
			return tema(idTema);
		} else {
			Map<String, String> formMap = filledForm.data();
			int dificuldade = Integer.parseInt(formMap.get("dificuldade"));	
			String userLogin = session("login");
			Tema tema = dao.findByEntityId(Tema.class, idTema);
			
			//Tema tema = dao.findByEntityId(Tema.class, id)(Tema) dao.findByAttributeName("Tema", "name", nomeTema).get(0);
			tema.incrementarDificuldade(userLogin, dificuldade);
			dao.merge(tema);
			dao.flush();
			return redirect(routes.Application.tema(idTema));
		}
	}
	
	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result addDiscordanciaEmDica(long idDica) {
		DynamicForm filledForm = Form.form().bindFromRequest();
		
		Dica dica = dao.findByEntityId(Dica.class, idDica);
		
		if (filledForm.hasErrors()) {
			return tema(dica.getTema().getId());
		} else {
			Map<String, String> formMap = filledForm.data();
			String username = session("username");
			String login = session("login");
			String discordancia = formMap.get("discordancia");
			
			dica.addUsuarioQueVotou(login);
			dica.addUserCommentary(username, discordancia);
			dica.incrementaDiscordancias();
			dao.merge(dica);
			dao.flush();
			
			return redirect(routes.Application.tema(dica.getTema().getId()));
		}
	}
	
	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result upVoteDica(long idDica) {
		Dica dica = dao.findByEntityId(Dica.class, idDica);
		String login = session("login");
		if(!dica.wasVotedByUser(login)){
			dica.addUsuarioQueVotou(login);
			dica.incrementaConcordancias();
			dao.merge(dica);
			dao.flush();
		}
		
		return redirect(routes.Application.tema(dica.getTema().getId()));
	}

	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result addDiscordanciaEmMetaDica(long idMetaDica) {
		DynamicForm filledForm = Form.form().bindFromRequest();
		
		MetaDica metaDica = dao.findByEntityId(MetaDica.class, idMetaDica);
		
		if (filledForm.hasErrors()) {
			return disciplina(metaDica.getDisciplina().getId());
		} else {
			Map<String, String> formMap = filledForm.data();
			String username = session("username");
			String login = session("login");
			String discordancia = formMap.get("discordancia");
			
			metaDica.addUsuarioQueVotou(login);
			metaDica.addUserCommentary(username, discordancia);
			metaDica.incrementaDiscordancias();
			dao.merge(metaDica);
			dao.flush();
			
			return redirect(routes.Application.disciplina(metaDica.getDisciplina().getId()));
		}
	}
	
	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result upVoteMetaDica(long idMetaDica) {
		MetaDica metaDica = dao.findByEntityId(MetaDica.class, idMetaDica);
		String login = session("login");
		if(!metaDica.wasVotedByUser(login)){
			metaDica.addUsuarioQueVotou(login);
			metaDica.incrementaConcordancias();
			dao.merge(metaDica);
			dao.flush();
		}
		return redirect(routes.Application.disciplina(metaDica.getDisciplina().getId()));
	}
	
	/**
	 * Action para o cadastro de uma metadica em uma disciplina.
	 * 
	 * @param idDisciplina
	 *           O id da {@code Disciplina}.
	 * @return
	 *           O Result do POST, redirecionando para a página da Disciplina caso o POST
	 *           tenha sido concluído com sucesso.
	 */
	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result cadastrarMetaDica(long idDisciplina) {
		DynamicForm filledForm = Form.form().bindFromRequest();
		
		Map<String,String> formMap = filledForm.data();
		
		String comment = formMap.get("comentario");
		
		Disciplina disciplina = dao.findByEntityId(Disciplina.class, idDisciplina);
		String userName = session("username");
		
		if (filledForm.hasErrors()) {
			return disciplinaErro(disciplina);
		} else {
			MetaDica metaDica = new MetaDica(disciplina, userName, comment);			
			
			Map<String,String[]> map = request().body().asFormUrlEncoded();
			
			String[] checkedDicas = map.get("dica");
			String[] checkedMetaDicas = map.get("metadica");
			
			
			if(checkedDicas == null && checkedMetaDicas == null){
				return disciplinaErro(disciplina);
			}
			
			if(checkedDicas != null){
				List<String> listaIdDicas = Arrays.asList(checkedDicas);

				for (String id : listaIdDicas) {
					Long idDica = Long.parseLong(id);
					
					Dica checkedDica = dao.findByEntityId(Dica.class, idDica);

					if(checkedDica != null){
						metaDica.addDica(checkedDica);
						checkedDica.addMetaDica(metaDica);
						dao.merge(checkedDica);
					}
				}
			}
			if(checkedMetaDicas != null){
				List<String> listaIdMetaDicas = Arrays.asList(checkedMetaDicas);

				for (String id : listaIdMetaDicas) {
					Long idMetaDica = Long.parseLong(id);
					
					MetaDica checkedMetaDica = dao.findByEntityId(MetaDica.class, idMetaDica);

					if(checkedMetaDica != null){
						metaDica.addMetaDica(checkedMetaDica);
						dao.merge(checkedMetaDica);
					}
				}
			}
			
			disciplina.addMetaDica(metaDica);
			
			dao.persist(metaDica);
			dao.merge(disciplina);
			dao.flush();
			
			return redirect(routes.Application.disciplina(metaDica.getDisciplina().getId()));
		}
	}
	
	/**
	 * Action usada para a denúncia de uma Dica considerada como imprópria pelo usuário.
	 * 
	 * @param idDica
	 *           O id da {@code Dica} denunciada.
	 * @return
	 *           O Result do POST, redirecionando para a página do {@code Tema} caso o POST
	 *           tenha sido concluído com sucesso.
	 */
	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result denunciarDica(Long idDica) {
		Dica dica = dao.findByEntityId(Dica.class, idDica);
		
		String login = session("login");
		if (!dica.wasFlaggedByUser(login)) {
			dica.addUsuarioFlag(login);
			dica.incrementaFlag();
			
			if (dica.getFlag() == MAX_DENUNCIAS) {
				dao.removeById(Dica.class, idDica);
				
				for (MetaDica metadica : dica.getMetaDicas()) {
					metadica.getDicasAdicionadas().remove(dica);
					dao.merge(metadica);
				}
			} else {
				dao.merge(dica);
			}
		} else {
			flash("fail", "Usuário já denunciou a dica.");
		}
		
		dao.flush();
		
		return redirect(routes.Application.tema(dica.getTema().getId()));
	}
	
	/**
	 * Action usada para a denúncia de uma MetaDica considerada como imprópria pelo usuário.
	 * 
	 * @param idDica
	 *           O id da {@code MetaDica} denunciada.
	 * @return
	 *           O Result do POST, redirecionando para a página da {@code Disciplina} caso o POST
	 *           tenha sido concluído com sucesso.
	 */
	@Transactional
	@Security.Authenticated(Secured.class)
	public static Result denunciarMetaDica(Long idMetaDica) {
		MetaDica metaDica = dao.findByEntityId(MetaDica.class, idMetaDica);
		
		String login = session("login");
		if (!metaDica.wasFlaggedByUser(login)) {
			metaDica.addUsuarioFlag(login);
			metaDica.incrementaFlag();
			
			if (metaDica.getFlag() == MAX_DENUNCIAS) {
				dao.removeById(MetaDica.class, idMetaDica);
			} else {
				dao.merge(metaDica);
			}
		} else {
			flash("fail", "Usuário já denunciou a dica.");
		}
		
		dao.flush();
		
		return redirect(routes.Application.disciplina(metaDica.getDisciplina().getId()));
	}
}