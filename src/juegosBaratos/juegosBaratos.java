package juegosBaratos;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;

//import org.openqa.selenium.support.ui.ExpectedConditions;
//import org.openqa.selenium.support.ui.WebDriverWait;

import io.netty.handler.ssl.OpenSslCertificateCompressionAlgorithm;

import java.util.*;
import java.util.Map.Entry;

import javax.swing.JOptionPane;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import org.openqa.selenium.*;
//import org.openqa.selenium.JavascriptExecutor;
//import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
//import java.lang.*;
public class juegosBaratos {

	public static void main(String[] args) throws InterruptedException {
		// TODO Auto-generated method stub
		

		//En listaFinal se registran los nombres y precios que voy a mostrar al final
		Map <String, List<String>> listaFinal = new HashMap<String, List<String>>();
		
		
		ChromeOptions option = new ChromeOptions();
        option.addArguments("--remote-allow-origins=*");
        
		//Inicializar selenium
		System.setProperty("webdriver.chrome.driver", "C:/Developer/Chromedriver/chromedriver.exe");
		ChromeDriver driver =  new ChromeDriver(option);
		
		//Recojo el precio a partir del cual no debo mostrar el juego al final
		int maximo = Integer.parseInt(JOptionPane.showInputDialog("Precio maximo"));
		
		driver.get("https://www.allkeyshop.com/blog/list/Juan-Mendez2/336592/");
		String tabla = "/html/body/div[3]/div/div[1]/div[1]/table";
		WebElement table= driver.findElement(By.xpath(tabla));
		
		//Lista de elementos web <td> que son cada fila de la WishList
		List<WebElement> tableColumnTD = table.findElements(By.tagName("td"));
		
		ArrayList<String> juegosEnWL = new ArrayList<String>();
		
		
		for (WebElement juegoEnWL : driver.findElement(By.className("akswl-list-display")).findElements(By.tagName("a")))
		{	
			try {
				juegoEnWL.findElement(By.tagName("img"));
			} catch (Exception e)
			{
				if (!juegoEnWL.getText().contains("€"))
					juegosEnWL.add(juegoEnWL.getText());
			}
		}
	
		
		//System.exit(maximo);
		////[class='game-name']
		int scroll = 0;
		for(int i=1;i<tableColumnTD.size();i+=5)
		{	
			String juegoConcreto;
			table= driver.findElement(By.xpath(tabla));
			tableColumnTD = table.findElements(By.tagName("td"));

			WebElement juego=tableColumnTD.get(i);
			String strJuego=juego.getText();
			
			Actions act = new Actions(driver);
			WebElement enlaceJuego=juego.findElement(By.tagName("a"));
						
			Thread.sleep(2000);
			
			JavascriptExecutor jse = (JavascriptExecutor)driver;
			
			//jse.executeScript("arguments[0].scrollIntoView(true); window.scrollBy(0, -window.innerHeight / 4);", enlaceJuego);
			((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);", enlaceJuego);
			
			Thread.sleep(1000);
			((JavascriptExecutor)driver).executeScript("window.scrollBy(0,-100);");
			
			Thread.sleep(1000);
			try {

				act.moveToElement(enlaceJuego).click().perform();
			} catch (Exception e) {
				System.out.println("te pillé" + e);
				System.exit(0);
			}
			
			Thread.sleep(2000);
			
			List<WebElement> listaOfertas = driver.findElements(By.xpath("//div[@class='offers-table-row x-offer']"));
			
			int total=listaOfertas.size();
			
			int j=0;
			boolean satisfecho=false;
			
			while(!satisfecho && j<total)
			{
				WebElement ofertaActual = listaOfertas.get(j);
				
				WebElement nombreTiendaActual = ofertaActual.findElement(By.tagName("div")).findElement(By.tagName("div"));
				
				WebElement precioActual = null;
				
				try
				{
					precioActual = ofertaActual.findElements(By.tagName("div")).get(28);
				}
				catch (IndexOutOfBoundsException ex)
				{
					precioActual = ofertaActual.findElements(By.tagName("div")).get(27);
				}
				
				WebElement plataformaActual = ofertaActual.findElements(By.tagName("span")).get(4);
				String nombrePlatActual = plataformaActual.getAttribute("class").toString();
				
				String strTiendaActual=nombreTiendaActual.getText();
				String strPrecioActual=precioActual.getText();
				
				double precioFormateado= Double.parseDouble(strPrecioActual.split("€")[0]);
								
				if (nombrePlatActual.contains("steam") && !strTiendaActual.equals("G2A Plus") && precioFormateado < maximo)
				{
					
					List<String> lstOfertasTienda;
					
					juegoConcreto=strJuego+"#"+strPrecioActual;

					if (listaFinal.containsKey(strTiendaActual))
					{
						lstOfertasTienda = listaFinal.get(strTiendaActual);
					}
					else
					{
						lstOfertasTienda = new ArrayList<>();
					}	

					lstOfertasTienda.add(juegoConcreto);
					satisfecho=true;
					listaFinal.put(strTiendaActual, lstOfertasTienda);
					
				}
				
				//System.out.println("Nombre de tienda astuale: " + nombreTiendaActual.getText());
				//System.out.println("Presio astuale: " + precioActual.getText());
				//System.out.println("Plataformas astuale: " + plataformaActual.getAttribute("class"));
				
				if (precioFormateado > maximo)
					satisfecho=true;
				
				j++;

			}
			
			
			/*
			 * JavascriptExecutor jse = (JavascriptExecutor)driver;
			jse.executeScript("window.history.go(-1)");
			*/
			
			driver.navigate().to("https://www.allkeyshop.com/blog/list/Juan-Mendez2/336592/");

			//System.out.println("aqui tendria que haber vuelto");
			
			//comentar-descomentar para tratar solo un juego
			//break;
		}
		
		//driver.findElement(null);
		driver.close();
		
		//INICIO persistencia
		ObjetoPersist op = new ObjetoPersist();

		op.recuperaObj();
		
		HashMap<String,Double> copia = (HashMap<String, Double>) op.mjuegos.clone();
		
		for (String juegoComprobar : copia.keySet())
		{
			if (juegosEnWL.contains(juegoComprobar))
				System.out.println("El juego "+juegoComprobar+" sigue estando en la WL");
			else
			{
				System.out.println("El juego "+juegoComprobar+" ya no está en la WL. Lo quitamos del objeto persistente");
				op.mjuegos.remove(juegoComprobar);
			}
		}
		
		for (String juegoComprobar : op.mjuegos.keySet())
		{
			System.out.println(juegoComprobar);
		}
		
		
		
		Map<String, Double> contenidoObjetoP =new HashMap<String, Double>();

		//descomentar cuando quiera limpiar
		//op.juegos.clear();
		//op.mjuegos.clear();
		//descomentar cuando quiera interrumpir
		//System.exit(0);

		try {
			for (String s: op.juegos)
				{
					contenidoObjetoP.put(s.split("#")[0], Double.parseDouble(s.split("#")[1].split("€")[0]));
				}
		}
		catch (ArrayIndexOutOfBoundsException a)
		{
			System.out.println("No tenemos juegos guardados");
		}
		//FIN persistencia
		
		String juegoAhora=null;
		Double precioAhora=0.0;
		
		/*A partir de aquí recorremos la lista de juegos con el precio minimo y menor
		 *  del indicado. 
		 */
		for (Entry<String, List<String>> entry : listaFinal.entrySet())
		{
			System.out.println(entry.getKey());
			
			List<String> l = entry.getValue();
			l.sort((s1,s2)->(((s1.split("#")[1]).split("€")[0]).compareTo((s2.split("#")[1]).split("€")[0])));
						
			/*for (String juego : entry.getValue())
			{
				System.out.println(juego);
				
			}*/
			
			
			for (String juego : l)
			{
				juegoAhora=juego.split("#")[0];
				precioAhora=Double.parseDouble(juego.split("#")[1].split("€")[0]);
				//System.out.println(juego);
				
				if (op.mjuegos.containsKey(juegoAhora))
				{
					Double minimoActual=op.mjuegos.get(juegoAhora);
					
					if (precioAhora<minimoActual)
					{
						op.mjuegos.put(juegoAhora, precioAhora);
						System.out.println(juego + "<-- NUEVO MÁS BAJO");
					}
					else if (precioAhora.equals(minimoActual))
					{
						System.out.println(juego + "<-- el más bajo pero viene de largo");
					}
					else
					{
						System.out.println(juego);
					}
				}
				else
				{
					op.mjuegos.put(juegoAhora, precioAhora);
					System.out.println(juego + "<-- recién añadido");
				}

			}	
			
			System.out.println("- - - - - - - - ");
		}
		
		op.guardaObj();
		
	}

}