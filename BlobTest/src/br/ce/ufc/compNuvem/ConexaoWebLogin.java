package br.ce.ufc.compNuvem;

import com.google.gson.Gson;
import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

public class ConexaoWebLogin {
	Gson gson = new Gson();
	public boolean confirmarUser(String senha, String email){
		Client client = Client.create();
		String output= "";
		WebResource webResource = client
				.resource("http://100.66.20.46:8080/WebServiceREST/rest/server/getUsuario/"+email);
		try{
			ClientResponse response = webResource.accept("application/json")
					.get(ClientResponse.class);
	
			if (response.getStatus() != 200) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}
			output = response.getEntity(String.class);
		}catch(Exception e){
			return false;
		}
		
		Usuario user = gson.fromJson(output, Usuario.class);
		System.out.println("Output from Server .... \n");
		System.out.println(user.toString());
		if(user.getSenha().equals(senha))
			return true;
		return false;
	}
	public boolean cadastratUser(Usuario usuario){
		Client client = Client.create();
		System.out.println(usuario.toString());
		WebResource webResource = client
				.resource("http://100.66.20.46:8080/WebServiceREST/rest/server/inserir");

		String input = gson.toJson(usuario);
		try{
			ClientResponse response = webResource.type("application/json")
					.post(ClientResponse.class, input);
	
			if (response.getStatus() != 201) {
				throw new RuntimeException("Failed : HTTP error code : "
						+ response.getStatus());
			}
	
			System.out.println("Output from Server .... \n");
			String output = response.getEntity(String.class);
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;
	}
}
