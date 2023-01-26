package br.com.converter;

import java.io.Serializable;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;

import br.com.entidades.Estados;
import br.com.jpautil.JPAUtil;

@FacesConverter(forClass = Estados.class, value="estadoConverter")
public class EstadoConverter implements Converter, Serializable{

	@Override //retorna o objeto inteiro
	public Object getAsObject(FacesContext context, UIComponent component, String idEstado) {
		
		EntityManager entityManager=JPAUtil.getEntityManager();
		EntityTransaction entityTransaction=entityManager.getTransaction();
		entityTransaction.begin();
		
		Estados estados=(Estados) entityManager.find(Estados.class, Long.parseLong(idEstado));
		
		return estados;
	}

	@Override // retorna apenas o id/código em String
	public String getAsString(FacesContext context, UIComponent component, Object estado) {
		if(estado == null) {
			return null;
		}
		if(estado instanceof Estados) {
			return ((Estados) estado).getId().toString();
		}else {
			return estado.toString();
		}
		
	}

}
