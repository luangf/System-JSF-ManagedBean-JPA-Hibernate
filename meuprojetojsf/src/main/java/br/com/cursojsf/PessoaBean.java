package br.com.cursojsf;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;

import com.google.gson.Gson;

import br.com.dao.DaoGeneric;
import br.com.entidades.Cidades;
import br.com.entidades.Estados;
import br.com.entidades.Pessoa;
import br.com.jpautil.JPAUtil;
import br.com.repository.IDaoPessoa;
import br.com.repository.IDaoPessoaImpl;

@ViewScoped
@ManagedBean(name = "pessoaBean")
public class PessoaBean {

	private Pessoa pessoa = new Pessoa();
	private DaoGeneric<Pessoa> daoGeneric = new DaoGeneric<Pessoa>();
	private List<Pessoa> pessoas = new ArrayList<Pessoa>();

	private IDaoPessoa iDaoPessoa = new IDaoPessoaImpl();

	private List<SelectItem> estados;
	private List<SelectItem> cidades;

	public void pesquisaCep(AjaxBehaviorEvent event) {
		try {
			// caso venha sem nada da erro
			URL url = new URL("https://viacep.com.br/ws/" + pessoa.getCep() + "/json/");
			URLConnection connection = url.openConnection(); // consumo
			InputStream is = connection.getInputStream(); // demora mais pq entra la na internet...
			BufferedReader br = new BufferedReader(new InputStreamReader(is, "UTF-8"));

			String cep = ""; // linha do JSON
			StringBuilder jsonCep = new StringBuilder(); // string final com os dados

			while ((cep = br.readLine()) != null) {
				jsonCep.append(cep);
			}

			// Objeto Pessoa aux
			Pessoa gsonPessoaAux = new Gson().fromJson(jsonCep.toString(), Pessoa.class);

			pessoa.setCep(gsonPessoaAux.getCep()); // n precisa pq ja tinha setado, mas por precaução...
			pessoa.setLogradouro(gsonPessoaAux.getLogradouro());
			pessoa.setComplemento(gsonPessoaAux.getComplemento());
			pessoa.setBairro(gsonPessoaAux.getBairro());
			pessoa.setLocalidade(gsonPessoaAux.getLocalidade());
			pessoa.setUf(gsonPessoaAux.getUf());
			pessoa.setIbge(gsonPessoaAux.getIbge());
			pessoa.setGia(gsonPessoaAux.getGia());
			pessoa.setDdd(gsonPessoaAux.getDdd());
			pessoa.setSiafi(gsonPessoaAux.getSiafi());

			System.out.println(gsonPessoaAux);
		} catch (Exception e) {
			e.printStackTrace();
			mostrarMsg("Erro ao consultar o CEP");
		}
	}

	public String salvar() {
		pessoa = daoGeneric.merge(pessoa); // salva(persiste)/atualiza e retorna o obj da entidade/classe
		carregarPessoas();
		mostrarMsg("Cadastrado com sucesso!");
		return "";
	}

	private void mostrarMsg(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		context.addMessage(null, message);
	}

	public String novo() {
		pessoa = new Pessoa();
		return "";
	}

	public String limpar() {
		pessoa = new Pessoa();
		return "";
	}

	public String remover() {
		daoGeneric.deletarPorId(pessoa);
		pessoa = new Pessoa();
		carregarPessoas();
		mostrarMsg("Removido com sucesso!");
		return "";
	}

	@PostConstruct
	public void carregarPessoas() {
		pessoas = daoGeneric.getListEntity(Pessoa.class);
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	public DaoGeneric<Pessoa> getDaoGeneric() {
		return daoGeneric;
	}

	public void setDaoGeneric(DaoGeneric<Pessoa> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}

	public List<Pessoa> getPessoas() {
		return pessoas;
	}

	public void setPessoas(List<Pessoa> pessoas) {
		this.pessoas = pessoas;
	}

	public String logar() {
		Pessoa pessoaUser = iDaoPessoa.consultarUsuario(pessoa.getLogin(), pessoa.getSenha());

		if (pessoaUser != null) {
			FacesContext context = FacesContext.getCurrentInstance();
			ExternalContext externalContext = context.getExternalContext();
			externalContext.getSessionMap().put("usuarioLogado", pessoaUser);

//			HttpServletRequest req = (HttpServletRequest) externalContext.getRequest();
//			HttpSession session = req.getSession();
//			session.setAttribute("usuarioLogado", pessoaUser);

			return "primeirapagina.jsf";
		}

		return "index.jsf";
	}

	public String deslogar() {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		externalContext.getSessionMap().remove("usuarioLogado");

		// seção do usuario
		HttpServletRequest httpServletRequest = (HttpServletRequest) context.getCurrentInstance().getExternalContext()
				.getRequest();

		httpServletRequest.getSession().invalidate();

		return "index.jsf";
	}

	public boolean permiteAcesso(String acesso) {
		FacesContext context = FacesContext.getCurrentInstance();
		ExternalContext externalContext = context.getExternalContext();
		Pessoa pessoaUser = (Pessoa) externalContext.getSessionMap().get("usuarioLogado");

		return pessoaUser.getPerfilUser().equals(acesso);
	}

	public List<SelectItem> getEstados() {
		estados = iDaoPessoa.listaEstados();
		return estados;
	}

	public void setEstados(List<SelectItem> estados) {
		this.estados = estados;
	}

	public void carregaCidades(AjaxBehaviorEvent event) {
		// todo componente tem uma classe java q representa ele
		Estados estado = (Estados) ((HtmlSelectOneMenu) event.getSource()).getValue();

		if (estado != null) {

			pessoa.setEstadoSelecionado(estado); // para ser retornado para tela
			// estados_id do banco pode ser escrivo: estados.id, ja q representa a tabela
			// HQL
			List<Cidades> cidades = JPAUtil.getEntityManager()
					.createQuery("from Cidades where estados.id=" + estado.getId()).getResultList();

			List<SelectItem> selectItemsCidade = new ArrayList<SelectItem>();

			for (Cidades cidade : cidades) {
				selectItemsCidade.add(new SelectItem(cidade, cidade.getNome()));
			}
			// setter
			setCidades(selectItemsCidade);
		}
	}

	public void editar() {
		if (pessoa.getCidadeSelecionada() != null) {
			Estados estado = pessoa.getCidadeSelecionada().getEstados();
			pessoa.setEstadoSelecionado(estado);

			List<Cidades> cidades = JPAUtil.getEntityManager()
					.createQuery("from Cidades where estados.id=" + estado.getId()).getResultList();

			List<SelectItem> selectItemsCidade = new ArrayList<SelectItem>();

			for (Cidades cidade : cidades) {
				selectItemsCidade.add(new SelectItem(cidade, cidade.getNome()));
			}
			// setter
			setCidades(selectItemsCidade);
		}
	}

	public List<SelectItem> getCidades() {
		return cidades;
	}

	public void setCidades(List<SelectItem> cidades) {
		this.cidades = cidades;
	}

}
