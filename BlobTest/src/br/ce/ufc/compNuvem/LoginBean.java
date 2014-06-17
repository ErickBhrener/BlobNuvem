package br.ce.ufc.compNuvem;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

@ManagedBean
public class LoginBean implements Serializable {

	private static final long serialVersionUID = 9108781020220176401L;
	private String nome;
	private String senha;
	
	private ConexaoWebLogin cwl;
	public LoginBean() {
		cwl = new ConexaoWebLogin();
		System.out.println("construtor login bean");
		HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		if (session != null) {
			session.invalidate();
		}
	}
	
	public String login() {
		HttpSession session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(false);
		if (cwl.confirmarUser(senha, nome)) {
			if (session == null) {
				session = (HttpSession)FacesContext.getCurrentInstance().getExternalContext().getSession(true);
			}
			session.setAttribute("nome", nome);
			return "/index.xhtml?faces-redirect=true";
		} else {
			if (session != null) {
				session.invalidate();
			}
		}
		return "/login.xhtml";
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
}

