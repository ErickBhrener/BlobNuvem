package br.ce.ufc.compNuvem;

import javax.faces.bean.ManagedBean;

@ManagedBean(name="cadastroBean")
public class CadastroBean {
	private Usuario usuario;
	private ConexaoWebLogin cwl;
	public CadastroBean() {
		System.out.println("Cadastro de Usuario");
		usuario = new Usuario();
		cwl = new ConexaoWebLogin();
	}
	
	public String cadastrar(){
		try{
			System.out.println("No cadastro bean: " + usuario.toString());
			if(cwl.cadastratUser(usuario))
				System.out.println("Usuario cadastrado");
		}catch(Exception e){
			e.printStackTrace();
			return "/insere.xhtml";
		}
		return "/login.xhtml?faces-redirect=true";
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
	
}
