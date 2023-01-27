package br.com.cursojsf;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlSelectOneMenu;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import javax.xml.bind.DatatypeConverter;

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

	private Part arquivoFoto; //Part, classe auxiliar para fazer upload temporario java
	
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

	public void registraLog() {
		System.out.println("Registrando Log");
	}
	
	public String salvar() throws IOException{
		byte[] imagemByte=getByte(arquivoFoto.getInputStream());
		pessoa.setFotoIconBase64Original(imagemByte); //salva img original
		//transformar em bufferimage, tipo manipulavel
		BufferedImage bufferedImage=ImageIO.read(new ByteArrayInputStream(imagemByte));
		//descobrir tipo da imagem
		int type=bufferedImage.getType()==0 ? BufferedImage.TYPE_INT_ARGB : bufferedImage.getType();
		int largura=200;
		int altura=200;
		//criar miniatura
		BufferedImage resizedImage=new BufferedImage(largura, altura, type);
		Graphics2D g=resizedImage.createGraphics();
		g.drawImage(bufferedImage, 0, 0, largura, altura, null);
		g.dispose();
		//escrever novamente a imagem em tamanho menor
		ByteArrayOutputStream baos=new ByteArrayOutputStream();
		String extensao=arquivoFoto.getContentType().split("\\/")[1]; //image/png
		ImageIO.write(resizedImage, extensao, baos);
		
		String miniImagem="data:"+arquivoFoto.getContentType()+";base64,"+DatatypeConverter.printBase64Binary(baos.toByteArray());
		
		//processar imagem
		pessoa.setFotoIconBase64(miniImagem);
		pessoa.setExtensao(extensao);
		
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

	public void setArquivoFoto(Part arquivoFoto) {
		this.arquivoFoto = arquivoFoto;
	}
	
	public Part getArquivoFoto() {
		return arquivoFoto;
	}
	
	//Converte IS(InputStream) para Array de Bytes
	private byte[] getByte(InputStream is) throws IOException{
		int len;
		int size=1024;
		byte[] buf=null;
		if(is instanceof ByteArrayInputStream) {
			size=is.available();
			buf=new byte[size];
			len=is.read(buf, 0, size);  
		}else {
			ByteArrayOutputStream bos=new ByteArrayOutputStream();
			buf=new byte[size];
			while((len=is.read(buf, 0, size)) != -1) {
				bos.write(buf, 0, len);
			}
			buf=bos.toByteArray();
		}
		return buf;
	}
	
	public void download() throws IOException {
		Map<String, String> params=FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
		String fileDownloadId=params.get("fileDownloadId");
		
		Pessoa pessoa=daoGeneric.consultar(Pessoa.class, fileDownloadId);
		
		HttpServletResponse response=(HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		response.addHeader("Content-Disposition", "attachment; filename=download."+pessoa.getExtensao());
		response.setContentType("application/octet-stream");
		response.setContentLength(pessoa.getFotoIconBase64Original().length);
		response.getOutputStream().write(pessoa.getFotoIconBase64Original());
		response.getOutputStream().flush();
		FacesContext.getCurrentInstance().responseComplete();
	}
	
	public void mudancaDeValor(ValueChangeEvent event) {
		System.out.println("Valor antigo: "+event.getOldValue());
		System.out.println("Valor novo: "+event.getNewValue());
	}
	
}
