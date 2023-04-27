package juegosBaratos;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;

public class ObjetoPersist {
	public ArrayList<String> juegos;
	public HashMap<String, Double> mjuegos;
	
	public ObjetoPersist() {
		juegos = new ArrayList<String>();
		mjuegos=new HashMap<String, Double>();
	}
	
	public void guardaObj() {
		try {
	          FileOutputStream fos=new FileOutputStream("C:\\Developer\\JuegosBaratos\\namesListData");
	          ObjectOutputStream oos=new ObjectOutputStream(fos);
	          oos.writeObject(this.mjuegos);
	  
	          oos.close();
	  	      fos.close();
	  
	            System.out.println("namesList serialized");
	        }
	        catch (IOException ioe) {
	            ioe.printStackTrace();
	        }
	}
	
	public void recuperaObj() {
		 try
		 {
			 InputStream file = new FileInputStream("C:\\Developer\\JuegosBaratos\\namesListData");
			 ObjectInputStream ois = new ObjectInputStream(file);
			 
			 this.mjuegos=(HashMap<String, Double>) ois.readObject();
			 
			 file.close();
			 ois.close();
			 
		 }
		 catch(Exception ioe)
		 {
			 ioe.printStackTrace();
		 }		
	}
	
	public void cambiaPrecio() {
		
		System.out.println("cambiar el precio");
	}
}

