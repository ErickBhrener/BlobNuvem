package br.ce.ufc.compNuvem;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.primefaces.model.UploadedFile;

import com.microsoft.windowsazure.serviceruntime.RoleEnvironment;
import com.microsoft.windowsazure.services.blob.client.BlobContainerPermissions;
import com.microsoft.windowsazure.services.blob.client.BlobContainerPublicAccessType;
import com.microsoft.windowsazure.services.blob.client.CloudBlob;
import com.microsoft.windowsazure.services.blob.client.CloudBlobClient;
import com.microsoft.windowsazure.services.blob.client.CloudBlobContainer;
import com.microsoft.windowsazure.services.blob.client.CloudBlockBlob;
import com.microsoft.windowsazure.services.blob.client.ListBlobItem;
import com.microsoft.windowsazure.services.core.storage.CloudStorageAccount;
import com.microsoft.windowsazure.services.core.storage.StorageException;

@ManagedBean(name = "fileBean")
@RequestScoped
@SessionScoped
public class Primeiro {
	public static final String storageConnectionString = "DefaultEndpointsProtocol=https;"
			+ "AccountName=portalvhds2m1whwq7vws75;"
			+ "AccountKey=fz9EhABF5GpDT1iudGOqhCjTV1gNrE+MuRoPkCt4ThCe0gTSxyPEVjhtJylMpt71FqTb62uaFY89LGeibES+ug==";
	private CloudStorageAccount storageAccount;
	private CloudBlobClient blobClient;
	private CloudBlobContainer container;
	private CloudBlob blobItem;
	private UploadedFile file;
	private List<CloudBlob> listar = new ArrayList<CloudBlob>();

	public Primeiro() {
		this.list();
	}

	public CloudBlobContainer conection(String nomeContainer) {
		try {
			storageAccount = CloudStorageAccount.parse(storageConnectionString);
			blobClient = storageAccount.createCloudBlobClient();
			// System.out.println("Antes do Setcontainer");
			this.setContainer(this.getBlobClient().getContainerReference(nomeContainer));
			// System.out.println("Depois do Setcontainer");

		} catch (java.security.InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (StorageException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return this.container;
	}

	public void upload() {
		FacesContext context = FacesContext.getCurrentInstance();
		if (getFile().getFileName().isEmpty() || !getFile().getFileName().endsWith(".mp3")) {
			System.out.println(getFile().getContentType());
			context.addMessage(null, new FacesMessage("Erro","Escolha um arquivo e que seja mp3"));
		} else {
			System.out.println(getFile().getFileName().endsWith(".mp3"));
			CloudBlockBlob blob = null;
			try {
				System.out.println(file.getFileName());
				blob = conection("audio").getBlockBlobReference(file.getFileName());
				blob.upload(file.getInputstream(), file.getSize());

			} catch (URISyntaxException e) {
				System.out.println(e.getMessage());
			} catch (StorageException e) {
				System.out.println(e.getMessage());
			} catch (IOException e) {
				System.out.println(e.getMessage());
			}
			context.addMessage(null, new FacesMessage("Sucesso","Arquivo importado com sucesso"));
		}

	}

	public void permissao() {
		// Create a permissions object
		BlobContainerPermissions containerPermissions = new BlobContainerPermissions();

		// Include public access in the permissions object
		containerPermissions
				.setPublicAccess(BlobContainerPublicAccessType.CONTAINER);

		// Set the permissions on the container
		try {
			this.getContainer().uploadPermissions(containerPermissions);
		} catch (StorageException e1) {
			// TODO Auto-generated catch block
			System.out.println(e1.getMessage());
		}

	}

	public void list() {

		// Loop over blobs within the container and output the URI to each of
		// them
		for (ListBlobItem blobItem : conection("audio").listBlobs()) {
			permissao();
			if (blobItem instanceof CloudBlob) {
				CloudBlob blob = (CloudBlob) blobItem;
				try {

					listar.add(blob);
					System.out.println(blob.getQualifiedUri());
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				} catch (StorageException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				}
			}

		}
	}

	public String deletar() {
		try {
			if (this.blobItem.deleteIfExists()) {
				System.out.println("Deletado com sucesso");
			} else
				System.out.println("Não Deletado devido a não existir");
		} catch (StorageException e) {
			System.out.println(e.getMessage());
		}
		return "lista.xhtml";
	}

	public String download() {
		OutputStream fileOut = null;
		for (ListBlobItem blobItem : conection("audio").listBlobs()) {
			permissao();
			if (blobItem instanceof CloudBlob) {
				CloudBlob blob = (CloudBlob) blobItem;
				try {
					System.out.println(blob.getName());
					fileOut = new FileOutputStream(blob.getName());
					this.blobItem.download(fileOut);
					System.out.println("Depois");
					listar.add(blob);
					System.out.println(blob.getQualifiedUri());
				} catch (URISyntaxException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				} catch (StorageException e) {
					// TODO Auto-generated catch block
					System.out.println(e.getMessage());
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			}
		return "lista.xhtml";
	}

	public List<CloudBlob> getListar() {
		return listar;
	}

	public void setListar(List<CloudBlob> listar) {
		this.listar = listar;
	}

	public CloudBlob getBlobItem() {
		return blobItem;
	}

	public void setBlobItem(CloudBlob blobItem) {
		this.blobItem = blobItem;
	}

	public CloudBlobContainer getContainer() {
		return container;
	}

	public void setContainer(CloudBlobContainer container) {
		this.container = container;
		try {
			// System.out.println("Antes do create");
			this.container.createIfNotExist();
			// System.out.println("Depois do create");
		} catch (StorageException e) {
			System.out.println(e.getMessage());
		}
	}

	public CloudStorageAccount getStorageAccount() {
		return storageAccount;
	}

	public void setStorageAccount(CloudStorageAccount storageAccount) {
		this.storageAccount = storageAccount;
	}

	public CloudBlobClient getBlobClient() {
		return blobClient;
	}

	public void setBlobClient(CloudBlobClient blobClient) {
		this.blobClient = blobClient;
	}

	public String storageConnectionString() {
		return RoleEnvironment.getConfigurationSettings().get(
				"StorageConnectionString");
	}

	public UploadedFile getFile() {
		return file;
	}

	public void setFile(UploadedFile file) {
		this.file = file;
	}

}
