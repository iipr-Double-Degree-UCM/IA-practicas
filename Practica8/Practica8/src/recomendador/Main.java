package recomendador;

/* Ejemplo de programa java que invoca la ejecuci�n del motor de reglas jess con el programa diet.clp*/
// De esta forma se podr�a realizar la entrada y salida de datos en Java y el razonamiento en Jess
import java.util.InputMismatchException;
import java.util.Iterator;
import java.util.Scanner;

import jess.Deffacts;
import jess.Fact;
import jess.JessException;
import jess.RU;
import jess.Rete;
import jess.Value;

public class Main { 
	
	
	public static void main(String[] args) throws JessException {
		
		
			Rete miRete;		
			miRete = new Rete();

			String ficheroReglas = "practica8.clp";	
			System.out.println("Cargando las reglas");
			try
			{
				Value v = miRete.batch(ficheroReglas);
				System.out.println("Value " + v);
			} catch (JessException je0) {
				System.out.println("Error de lectura en " + ficheroReglas);
				je0.printStackTrace();
			}
					
				
			// Crea un deffacts y a�ade un hecho siguiendo la template usuario definida en diet.clp
			// En una aplicaci�n real, los datos de usuario se leer�an en java y se
			// a�adir�an as� al programa jess
			
			Deffacts deffacts = new Deffacts("DatosJava", null, miRete);
			Fact f = new Fact("persona", miRete);
			Scanner sc = new Scanner(System.in);
			
			String s;
			int i;
			boolean correcto = false;
			while (!correcto){
				System.out.print("Nombre: ");
				try{
					s = sc.nextLine();
					f.setSlotValue("nombre", new Value(s, RU.SYMBOL));
					correcto = true;
				}catch(Exception e){
					System.out.println("Mal ");
				}
			}
			correcto = false;
			while (!correcto){
				System.out.print("Edad: ");
				if (sc.hasNextInt()){
					i = sc.nextInt();	
					if(i>=0){
						f.setSlotValue("edad", new Value(i, RU.INTEGER));
						correcto = true;
					}else
						System.out.println("Debes tener edad positiva.");
				}else{
					System.out.println("Mal puesta la edad.");
					sc.nextLine();
				}
			}
			correcto = false;
			while (!correcto){
				System.out.print("Presupuesto: ");
				if (sc.hasNextInt()){
					i = sc.nextInt();
					if(i>=0){
						f.setSlotValue("presupuesto", new Value(i, RU.INTEGER));
						correcto = true;
						sc.nextLine();
						
					}else{
						System.out.println("Debes tener presupuesto positivo.");
					}
				}else{
					System.out.println("Mal puesta la edad idiota.");
					sc.nextLine();
				}				
			}

			correcto = false;
			while (!correcto){
				System.out.print("Intereses: ");
				try{
					s = sc.nextLine();
					f.setSlotValue("intereses", new Value(s, RU.SYMBOL));
					correcto = true;
				}catch(Exception e){
					System.out.println("Mal, por qu� tocas? ");
					e.printStackTrace();
				}
			}
			//Los intereses hay que hacerlos como explica en la diapositiva
			//de JavaConJess, ya que son un multislot, se tratan con un vector.
			//Puedes ir preguntando uno a uno por todos los intereses y que se responda s/n
			//O puedes hacer que los introduzca manualmente hasta que ponga END o lo que sea.
			
			correcto = false;
			while (!correcto){
				System.out.print("D�as: ");
				if (sc.hasNextInt()){
					i = sc.nextInt();
					if(i>=0){
						f.setSlotValue("dias", new Value(i, RU.INTEGER));
						correcto = true;
					}else{
						System.out.println("Debe ser un n�mero positivo de d�as.");
					}
				}else{
					System.out.println("Mal puesta la edad idiota.");
					sc.nextLine();
				}
			}

			deffacts.addFact(f);
			miRete.addDeffacts(deffacts);
			
			miRete.reset();
			
			// A continuaci�n se ejecuta el motor jess
			// Si el programa jess no tiene m�dulos basta con hacer run()
			
			//miRete.run();

			/* Si el programa jess tiene m�dulos hay que poner el foco sucesivamente en cada uno de ellos, 
			   en el orden adecuado, y a continuaci�n un run() por cada m�dulo	*/
			  
			 
			   miRete.setFocus("usuario");
			   miRete.run();
			   miRete.setFocus("ciudad");
			   miRete.run();
			   miRete.setFocus("destino");
			   miRete.run();
			   miRete.setFocus("logistica");
			   miRete.run();
			 

			
			// Se listan los hechos que hay en la memoria de trabajo
			
			listaHechos(miRete);
			extraeHechos(miRete);

			// Para parar el motor de reglas
			miRete.halt();
			sc.close();
		
	}


	public static void listaHechos(Rete miRete) {
		// Obtiene e imprime la lista de hechos
		Iterator<Fact> iterador; 
		iterador = miRete.listFacts();
		
		System.out.println("Lista de hechos:");
		while (iterador.hasNext()) {
			System.out.println(iterador.next());
		}
	}

	public static void extraeHechos(Rete miRete) {
		// Ejemplo de c�mo podemos seleccionar s�lo los hechos de la template usuario
		// Y de esos hechos quedarnos s�lo con el slot edad e imprimir su valor
		Iterator<Fact> iterador; 
		iterador = miRete.listFacts();
		Fact f;
		Value ed;
		while (iterador.hasNext()) {
			f = iterador.next();
			if (f.getName().equals("MAIN::persona"))
			{
				try {
					ed = f.getSlotValue("edad");
					System.out.println("edad " + ed);
				} catch (JessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} }                           
		}
	}
	

}