package br.com.cursojsf;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import br.com.dao.DaoGeneric;
import br.com.entidades.Pet;

@ViewScoped
@ManagedBean(name = "petBean")
public class PetBean {

	private Pet pet = new Pet();
	private DaoGeneric<Pet> daoGeneric = new DaoGeneric<Pet>();
	private List<Pet> pets = new ArrayList<Pet>();

	@PostConstruct
	public void carregaPets() {
		pets = daoGeneric.getListEntity(Pet.class);
	}

	private void mostrarMsg(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		context.addMessage(null, message);
	}

	public String novo() {
		pet = new Pet(); // nova linha no BD, proximo id
		mostrarMsg("Pronto para cadastrar novo registro!");
		return "";
	}

	public String limpar() {
		pet = new Pet(); // msm linha pq n envia form, ai so volta a zero por precaução
		return "";
	}

	public String salvar() {
		pet = daoGeneric.merge(pet);
		mostrarMsg("Salvo com sucesso!");
		return "";
	}

	public String deletar() {
		daoGeneric.deletarPorId(pet);
		pet = new Pet();
		mostrarMsg("Removido com sucesso!");
		return "";
	}

	public DaoGeneric<Pet> getDaoGeneric() {
		return daoGeneric;
	}

	public void setDaoGeneric(DaoGeneric<Pet> daoGeneric) {
		this.daoGeneric = daoGeneric;
	}

	public List<Pet> getPets() {
		return pets;
	}

	public void setPets(List<Pet> pets) {
		this.pets = pets;
	}

	public Pet getPet() {
		return pet;
	}

	public void setPet(Pet pet) {
		this.pet = pet;
	}

}
