package es.caib.qssiWeb.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GetNota extends HttpServlet {


	private static final long serialVersionUID = 1L;
	
	private String resultat_message = new String();
	private String str_error = new String();
	
	public void doGet (HttpServletRequest req, HttpServletResponse res) throws IOException {
		
		String resultat_superior = new String();
		String resultat_tecnic = new String();
		
        // Enviam missatge
		try {
			resultat_superior = getNota("http://oposicions.caib.es/llista.jsp?lang=ca&codnav=3303010101&tipus=lt_llista_resum_notes","CFS","3","19,391");
			resultat_tecnic = getNota("http://oposicions.caib.es/llista.jsp?lang=ca&codnav=3303020501&tipus=lt_llista_resum_notes","CFT", "2","15,558");	
		}
		catch (Exception ex) {
			str_error = "Hem obtingut un error: " + ex.toString();
		}
		
        
		/* Generam la pàgina de sortida */
        PrintWriter pw = res.getWriter();
        
		pw.print("<html>");
		pw.print("<head>");
		pw.print("<meta http-equiv=\"refresh\" content=\"60\">");
		pw.print("</head>");
		pw.print("<body>");
		pw.print("<h1>Comprovador</h1>");
		pw.print("La darrera actualització fou: " + new Date().toString());
		pw.print("<div>Resultat CFS: " + resultat_superior + "</div>");
		pw.print("<div>Resultat CFT: " + resultat_tecnic + "</div>");
		pw.print("<div>Enviament de missatge: " + this.resultat_message + "</div>");
		pw.print("<div>" + str_error + "</div>");
		pw.print("</body>");
		pw.print("</html>");
	}
	
	private String getNota(String string_url, String cos, String posicio, String nota) throws IOException
	{
		
		String resultat = new String();
		boolean posicio_canviada = false;
		boolean nota_canviada = false;
		String nova_posicio = new String();
		String nova_nota = new String();
		
		/* Obtenim la pàgina web */
		URL url = new URL(string_url);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		
		con.setConnectTimeout(5000);
		con.setReadTimeout(5000);
		con.setRequestMethod("GET");
		con.getResponseCode();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		
		/* Cercam la informació desitjada */
		String patternString = "<tr>(.*?)</tr>";
        Pattern pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(content);
        resultat = "Informació no trobada";
        while (matcher.find())
        {
        	String tr_exam = matcher.group();
        	posicio_canviada = false;
        	nota_canviada = false;
        	nova_posicio = "";
        	nova_nota = "";
        	if (tr_exam.indexOf("JUANICO") != -1) {
        		// Tractam les columnes
        		patternString = "<td(?: [^>]*)?>(.*?)</td>";
        		pattern = Pattern.compile(patternString, Pattern.CASE_INSENSITIVE);
        		matcher = pattern.matcher(tr_exam);
        		int i = 0;
        		while (matcher.find())
        		{
        			String td_exam = matcher.group();
        			td_exam = cleanUpToken(td_exam);
        			switch (i) {
        				case 0:
        					resultat = "Posició: <b><span style='font-size:16px'>" + td_exam + "</span></b>";
        					if (!td_exam.equals(posicio))
        					{
        						posicio_canviada = true;
        					}
        					nova_posicio = td_exam;	
        					break;
        				case 1:
        					resultat = resultat + " Nom: " + td_exam ;
        					break;
        				case 2:
        					resultat = resultat + " " + td_exam ;
        					break;
        				case 3:
        					resultat = resultat + " " + td_exam;
        					break;
        				case 4:
        					resultat = resultat + " DNI: " + td_exam ;
        					break;
        				case 5:
        					resultat = resultat + " Examens aprovats: " + td_exam ;
        					break;
        				case 6:
        					resultat = resultat + " Nota: " + td_exam ;
        					if (!td_exam.equals(nota))
        					{
        						nota_canviada = true;
        					}
        					nova_nota = td_exam;
        					break;
        			default: 
        			}
        			i++;
        		}
        		if (posicio_canviada || nota_canviada) {
        			String element_canviat = new String();
        			if (posicio_canviada)
        				element_canviat = " Posicio canviada";
        			if (nota_canviada)
        				element_canviat = element_canviat + " Nota canviada";
        				
        			this.resultat_message = SendMessage("Canvis al web " + cos + element_canviat + " posicio: " + nova_posicio + " nota: " + nova_nota);
        			System.out.println("Missagte enviat, cos: " + cos + ", posicio: " + posicio + ", nova_posicio: " + nova_posicio + ", nota: " + nota + ", nova_nota: " + nova_nota);
        		}
        		
        		
        	}
        }
        return resultat;
         
	}
	
	private String cleanUpToken(String token) {
		String resultat = new String(token);
		resultat = resultat.replace("&nbsp;","");
		resultat = resultat.replaceAll("<td>","");
		resultat = resultat.replaceAll("</td>","");
		return resultat;
	}
	private String SendMessage(String message) throws IOException
	{
		/* Obtenim la pàgina web */
		String urlString = "https://api.telegram.org/bot%s/sendMessage?chat_id=%s&text=%s";

		String apiToken = "848731486:AAED32jVlOPx5Um7GCgnSWuOZieEB5FSey4";
		String chatId = "@cfs_informatica_caib";
		
		urlString = String.format(urlString, apiToken, chatId, message);
				
		System.out.println("Construim url: " + urlString);
		
		URL url = new URL(urlString);
		SSLSocketFactory socketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			
		
		
		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("rproxy2.caib.es", 3128));
		//System.setProperty("java.net.useSystemProxies", "true");
		
		HttpsURLConnection con = (HttpsURLConnection) url.openConnection(proxy);
		
		System.out.println("Obrim la connexió: ");
		
		con.setSSLSocketFactory(socketFactory);
		con.setConnectTimeout(3000);
		con.setReadTimeout(3000);
		con.setRequestMethod("POST");
		con.setInstanceFollowRedirects(true);
		
		//int status = con.getResponseCode();
		//System.out.println("Codi error obtingut "+ status);
		
		System.out.println("Llegim... ");
		
		BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
		String inputLine;
		StringBuffer content = new StringBuffer();
		while ((inputLine = in.readLine()) != null) {
			content.append(inputLine);
		}
		in.close();
		
		System.out.println("Tornem... ");
		return content.toString();
	}
	
}
