package es.caib.qssiEJB.service;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.annotation.security.RolesAllowed;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.log4j.Logger;

import com.itextpdf.text.Document;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.pdf.PdfWriter;

import com.itextpdf.tool.xml.XMLWorkerHelper;

import es.caib.plugins.arxiu.api.ContingutArxiu;
import es.caib.plugins.arxiu.api.DocumentEstat;
import es.caib.plugins.arxiu.api.DocumentExtensio;
import es.caib.plugins.arxiu.api.DocumentFormat;
import es.caib.plugins.arxiu.api.ExpedientEstat;
import es.caib.plugins.arxiu.api.ExpedientMetadades;
import es.caib.plugins.arxiu.api.IArxiuPlugin;
import es.caib.plugins.arxiu.caib.ArxiuPluginCaib;
import es.caib.qssiEJB.entity.Expedient;
import es.caib.qssiEJB.interfaces.ExpedientServiceInterface;

/**
 * Servei (EJB) per a l'entitat Expedient
 * @author [u97091] Antoni Juanico soler
 * data 18/09/2018
 */

@Stateless
@RolesAllowed({"tothom", "QSSI_USUARI", "QSSI_GESTOR", "QSSI_ADMIN"}) // Si tothom -> sobren els altres rols
public class ExpedientService implements ExpedientServiceInterface {

	private final static Logger LOGGER = Logger.getLogger(EscritService.class);

	@PersistenceContext(unitName="qssiDB_PU")
	EntityManager em;
	
	private String strError = new String("");
	private boolean resultat;
	
	@PostConstruct
	public void init() {
		LOGGER.info("Proxy a init: "+this.em);
	}
	
	private boolean getPrimaryKey(Integer any, Expedient e) {
		
				
		try
		{
			// La idea és obtenir el següent valor de la seqüència qsi_expedient_seq_ANY
			// NOTA, Toni Juanico, 20/06/2019 amb la finalitat d'evitar repeticions en els
			// números de seqüència la idea és fer un bloqueig, llegir el núm. incrementar-lo i actualitzar-lo
			// això amb JPA 2.0 és pot fer mitjançant la següent sentència
			// SequenciaExpedient se = em.find(SequenciaExpedient.class,any,LockModeType.PESSIMISTIC_WRITE);
			// però amb JPA 1.0 no existeix aquesta signatura i per tant ho fem directament amb query's damunt la base de dades (createQueryString);
			// Recordem que a JBoss 5.2 tenim JPA 1.0, JSP 2.1, EJB 3.0, etc.
						
			String strQuery = "select valor from QSI_SEQUENCIA_EXPEDIENT where id_sequencia =" + any +" for update";
			
			LOGGER.info("Obtenim clau primaria " + e.getEmail() + " query: " + strQuery );
			
			Query myQuery = em.createNativeQuery(strQuery);

			Integer val = (Integer) myQuery.getSingleResult();
			
			LOGGER.info("Obtinguda: " + val);
			
			if (val != null) 
			{
				e.setId((any * 100000)+ val);
				LOGGER.info("Calculada clau primaria expedient: " + e.getId());
				
				val = val + 1;
				strQuery = "update QSI_SEQUENCIA_EXPEDIENT set valor=" + val + " where id_sequencia=" + any;
				myQuery = em.createNativeQuery(strQuery);
				myQuery.executeUpdate();
				
				return true;
			}
			else
			{
				// No existeix la seqüència per l'any donat
				LOGGER.info("No existeix la seqüència per l'any donat");
				return false;
			}
			
		}
		catch(Exception ex)
		{
			LOGGER.info("Error obtenint la seqüència: " + ex.toString());
			return false;
		}
	}
	
	private boolean generateSequence(long any) {
		try {
		
			String queryStringCreatePK = new String("INSERT INTO qsi_sequencia_expedient (id_sequencia, valor) values (:num_any, 1)");
			Query queryCreatePK = em.createNativeQuery(queryStringCreatePK);
			queryCreatePK.setParameter("num_any", any);
			queryCreatePK.executeUpdate();
			LOGGER.info("Generada seqüència nova: qsi_expedient_seq_" + any);
			return true;
		}
		catch (Exception ex) {
			LOGGER.info("error generant nova seqüència: " + ex.toString());
			return false;
		}	
	}
	
	@Override
	public void addExpedient(Expedient e) {
		
		LOGGER.info("in addExpedient, estat entity manager: " + em.toString());
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(e.getDataentrada());
		Integer any = calendar.get(Calendar.YEAR);
		
		boolean obtingudaPK = getPrimaryKey(any,e);
		
		if (obtingudaPK)
		{
			try {
				// Afegim l'expedient
				em.persist(e);
				LOGGER.info("Inserit expedient");
				this.resultat = true;
			}
			catch(Exception ex) {
				LOGGER.error(ex);
				this.resultat = false;
				this.strError = ex.toString();
			}
		}
		else
		{
			// Si no hem pogut obtenir la clau primària molt probablement no existeix la seqüència, 
			// estem a un any nou i s'ha de generar la seqüència
			if (generateSequence(any)){
				obtingudaPK = getPrimaryKey(any,e);
				if (obtingudaPK)
				{
					try {
						// Afegim l'expedient
						em.persist(e);
						LOGGER.info("Inserit expedient");
						this.resultat = true;
					}
					catch(Exception ex) {
						LOGGER.error(ex);
						this.resultat = false;
						this.strError = ex.toString();
					}
				}
			}
			else
			{
				LOGGER.info("No s'ha pogut generar la seqüència per a obtenir la clau primària: ");
				this.resultat = false;
			}
		}
	}

	@Override
	public Expedient getExpedient(Integer id_expedient) {
		try
		{
			LOGGER.info("in getExpedient, estat entity manager: " + em.toString());
			Expedient e = em.find(Expedient.class, id_expedient);
			this.resultat = true;
			return e;
		}
		catch (Exception ex)
		{
			LOGGER.error(ex);
			this.resultat = false;
			this.strError = ex.toString();
			return null;
		}
	}
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Expedient> getLlista_Expedients(TipusCerca tc) {
		
		ArrayList<Expedient> l = new ArrayList<Expedient>();
		
		String queryString = new String();
		String estats_cercats;
		
		switch (tc)
		{
			case PENDENTS_ASSIGNAR_PER_CENTRE:
				estats_cercats = ExpedientServiceInterface.EstatExpedient.ASSIGNAT_EQUIP_FILTRATGE.getValue() + ", " + 
						          ExpedientServiceInterface.EstatExpedient.ASSIGNAT_RESPONSABLE_CONSELLERIA.getValue();
				
				queryString = new String("select e from Expedient e where e.id_estat in (" + estats_cercats + ") order by id_centre, id_subcentre, id_expedient");
				break;
			case PENDENTS_ASSIGNAR_PER_ESTAT:
				estats_cercats = ExpedientServiceInterface.EstatExpedient.ASSIGNAT_EQUIP_FILTRATGE.getValue() + ", " + 
						          ExpedientServiceInterface.EstatExpedient.ASSIGNAT_RESPONSABLE_CONSELLERIA.getValue();
				
				queryString = new String("select e from Expedient e where e.id_estat in (" + estats_cercats + ") order by id_estat");
				break;
			case PENDENTS_RESPOSTA: // Ojo, aquest tipus de cerca l'hem de clarificar!!!!!, Toni Juanico, 09/07/2019
				estats_cercats = ExpedientServiceInterface.EstatExpedient.ASSIGNAT_TRAMITADOR.getValue() + " ";
				queryString = new String("select e from Expedient e where e.id_estat in (" + estats_cercats + ") order by data_entrada");
				break;
			case REBUTJADES:
				estats_cercats = ExpedientServiceInterface.EstatExpedient.REBUTJADA.getValue() + " ";
				queryString = new String("select e from Expedient e where e.id_estat in (" + estats_cercats + ") order by data_entrada");
				break;
			case FINALITZADES:
				estats_cercats = ExpedientServiceInterface.EstatExpedient.FINALITZADA.getValue() + " ";
				queryString = new String("select e from Expedient e where e.id_estat in (" + estats_cercats + ") order by data_entrada");
				break;
			case TOTES_PER_CENTRE:
				queryString = new String("select e from Expedient e order by id_centre, id_subcentre, data_entrada");
				break;
			case TOTES_PER_ESTAT:
				queryString = new String("select e from Expedient e order by id_estat");
				break;
			default:
				queryString = null;
				
		}
		
		try
		{
						
			LOGGER.info("in getLlista_Expedients, estat entity manager: " + em.toString());
			LOGGER.info("Query seleccionada: " + queryString);
			
			l = (ArrayList<Expedient>) em.createQuery(queryString).getResultList();
			
			LOGGER.info("em operation done ");
			this.resultat = true;
			
		}
		catch (Exception ex)
		{
			LOGGER.error(ex);
			this.resultat = false;
			this.strError = ex.toString(); 
			
		}
		
		return l;
	}

	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Expedient> getLlista_Expedients_assignats_usuari(String usuari) {
		
		ArrayList<Expedient> l = new ArrayList<Expedient>();
		
		String queryString = new String("select e from Expedient e where e.usuari_assignat = :u order by id_expedient");
		try
		{
						
			LOGGER.info("in getLlista_Expedients_assignats_usuari, estat entity manager: " + em.toString());
			Query q = em.createQuery(queryString);
			q.setParameter("u", usuari);
			l = (ArrayList<Expedient>) q.getResultList();
			
			LOGGER.info("em operation done ");
			this.resultat = true;
			
		}
		catch (Exception ex)
		{
			LOGGER.error(ex);
			this.resultat = false;
			this.strError = ex.toString(); 
			
		}
		
		return l;
	}
	
	@Override
	public AccioExpedient[] getAccionsDisponiblesExpedient (EstatExpedient e) {
		AccioExpedient[] llista_accions = null;
		
		switch (e)
		{
		case ASSIGNAT_EQUIP_FILTRATGE: 
			llista_accions = new AccioExpedient[2];
			AccioExpedient accio1 = ExpedientServiceInterface.AccioExpedient.ASSIGNAR_CONSELLERIA;
			AccioExpedient accio2 = ExpedientServiceInterface.AccioExpedient.REBUTJAR;
			llista_accions[0] = accio1;
			llista_accions[1] = accio2;
			
			break;
		case ASSIGNAT_RESPONSABLE_CONSELLERIA:
			llista_accions = new AccioExpedient[3];
			AccioExpedient accio3 = ExpedientServiceInterface.AccioExpedient.ASSIGNAR_TRAMITADOR;
			AccioExpedient accio4 = ExpedientServiceInterface.AccioExpedient.RETORNAR_EQUIP_FILTRATGE;
			AccioExpedient accio5 = ExpedientServiceInterface.AccioExpedient.REBUTJAR;
			
			llista_accions[0] = accio3;
			llista_accions[1] = accio4;
			llista_accions[2] = accio5;
			break;
		case ASSIGNAT_TRAMITADOR:
			llista_accions = new AccioExpedient[3];
			AccioExpedient accio6 = ExpedientServiceInterface.AccioExpedient.TRAMITAR_RESPOSTA;
			AccioExpedient accio7 = ExpedientServiceInterface.AccioExpedient.RETORNAR_RESPONSABLE_CONSELLERIA;
			AccioExpedient accio8 = ExpedientServiceInterface.AccioExpedient.REBUTJAR;
			
			llista_accions[0] = accio6;
			llista_accions[1] = accio7;
			llista_accions[2] = accio8;
			break;
		case FINALITZADA:
			llista_accions = null;
			break;
		case REBUTJADA:
			llista_accions = null;
			break;
		default:
			llista_accions = null;
		}
		
        return llista_accions;
	}
	
	@Override
	public void assignarCentreExpedient(Integer id_expedient, Integer id_centre) {
		
		LOGGER.info("in assignarCentreExpedient, estat entity manager: " + em.toString());
		
		String queryString = new String("select id_centre FROM qsi_expedient where id_expedient = :id_expedient");
		String updateQueryString;
		
		
		try {
			
			Query query = em.createNativeQuery(queryString);
			query.setParameter("id_expedient", id_expedient);
			Integer id_centre_anterior = (Integer) query.getSingleResult();
			LOGGER.info("centre anterior: " + id_centre_anterior + ", centre nou: " + id_centre);
			
			if (id_centre_anterior.equals(id_centre))
			{
				// La unitat de filtratge no ha canviat el centre seleccionat inicialment
				// Només canviam l'estat de l'expedient	
				LOGGER.info("kiki");
				updateQueryString = new String("UPDATE qsi_expedient set id_centre = :id_centre, id_estat = :estat where id_expedient = :id_expedient");
			}
			else
			{
				// La unitat de filtratge ha canviat el centre seleccionat inicialment
				// Canviam el centre, posem el subcentre a null i canviam l'estat
				LOGGER.info("bubu");
				updateQueryString = new String("UPDATE qsi_expedient set id_centre = :id_centre, id_subcentre = null, id_estat= :estat where id_expedient = :id_expedient");
			}
			
			Query updateQuery = em.createNativeQuery(updateQueryString);
			updateQuery.setParameter("id_expedient", id_expedient);
			updateQuery.setParameter("id_centre", id_centre);
			updateQuery.setParameter("estat", ExpedientServiceInterface.EstatExpedient.ASSIGNAT_RESPONSABLE_CONSELLERIA.getValue());
			
			updateQuery.executeUpdate();	
			
			this.resultat = true;	
		}
		catch(Exception ex)
		{
			this.strError = ex.toString();
			this.resultat = false;
		}
		
	}
	
	@Override
	public void assignarTramitador(Integer id_expedient, Integer id_subcentre,  String unitat_organica, String usuari) {
		
		LOGGER.info("in assignarTramitador, estat entity manager: " + em.toString());
		String queryString;
		
		queryString = new String("UPDATE qsi_expedient set id_subcentre = :id_subcentre, unitat_organica = :unitat_organica, usuari_assignat = :usuari_assignat, id_estat= :estat where id_expedient = :id_expedient");
		
		try {
			
			Query query = em.createNativeQuery(queryString);
			query.setParameter("id_expedient", id_expedient);
			query.setParameter("id_subcentre", id_subcentre);
			query.setParameter("unitat_organica", unitat_organica);
			query.setParameter("usuari_assignat", usuari);
			query.setParameter("estat", ExpedientServiceInterface.EstatExpedient.ASSIGNAT_TRAMITADOR.getValue());
			
			query.executeUpdate();
			this.resultat = true;	
		}
		catch(Exception ex)
		{
			this.strError = ex.toString();
			this.resultat = false;
		}
	}
	
	private boolean assignarDadesArxiuCAIB(Integer id_expedient, String id_expedient_arxiu_caib, String id_document_arxiu_caib) {
		LOGGER.info("in assignarDadesArxiuCAIB, estat entity manager: " + em.toString());
		String queryString;
		
		queryString = new String("UPDATE qsi_expedient set id_expedient_arxiu_caib = :id_expedient_arxiu_caib, id_document_arxiu_caib = :id_document_arxiu_caib where id_expedient = :id_expedient");
		
		try {
			
			Query query = em.createNativeQuery(queryString);
			query.setParameter("id_expedient", id_expedient);
			query.setParameter("id_expedient_arxiu_caib", id_expedient_arxiu_caib);
			query.setParameter("id_document_arxiu_caib", id_document_arxiu_caib);
			//query.setParameter("estat", ExpedientServiceInterface.EstatExpedient.FINALITZADA.getValue());
			
			query.executeUpdate();
			return true;	
		}
		catch(Exception ex)
		{
			this.strError = ex.toString();
			return false;
		}
	}
	
	@Override
	public void desarRespostaExpedient(Integer id_expedient, String text_resposta)
	{
		LOGGER.info("in desarRespostaExpedient, estat entity manager: " + em.toString());
		String queryString;
		
		queryString = new String("UPDATE qsi_expedient set text_resposta = :text_resposta, data_resposta = :data_resposta where id_expedient = :id_expedient");
		
		try {
			Date data_resposta = new Date();
			Query query = em.createNativeQuery(queryString);
			query.setParameter("id_expedient", id_expedient);
			query.setParameter("text_resposta", text_resposta);
			query.setParameter("data_resposta", data_resposta);
			
			query.executeUpdate();
			this.resultat = true;	
		}
		catch(Exception ex)
		{
			this.strError = ex.toString();
			this.resultat = false;
		}
	}
	
	@Override
	public void tancarExpedient(Integer id_expedient)
	{
		LOGGER.info("in tancarExpedient, estat entity manager: " + em.toString());
				
		try {
			
			// Obtenir expedient
			Expedient e = this.getExpedient(id_expedient); 
			if (this.resultat)
			{
				// Generar pdf
				LOGGER.info("dintre_0");
				FileOutputStream myStream = new FileOutputStream("result.pdf");
				Document document = new Document(PageSize.A4,25f, 25f, 25f, 25f);
				PdfWriter myWriter = PdfWriter.getInstance(document, myStream);
				
				String HTMLResposta = new String("");
				
				HTMLResposta = HTMLResposta.concat("<html>");
				HTMLResposta = HTMLResposta.concat("<head><style>");
				HTMLResposta = HTMLResposta.concat("h1 {color:blue;}");
				HTMLResposta = HTMLResposta.concat("body {font-size:12px;}");
				HTMLResposta = HTMLResposta.concat("</style></head>");
				HTMLResposta = HTMLResposta.concat("<body>");
				HTMLResposta = HTMLResposta.concat("<h1>Expedient QSSI-" + e.getId() + "<br/></h1>");
				HTMLResposta = HTMLResposta.concat("<table cellspacing='5' cellpadding='5' border='0'>");
				HTMLResposta = HTMLResposta.concat("<tr>");
				HTMLResposta = HTMLResposta.concat("<td style='width:57%;valign:top'>");
				// Dades generals
				HTMLResposta = HTMLResposta.concat("<h2>Dades generals<hr/></h2>");
				HTMLResposta = HTMLResposta.concat("<b>Assumpte:</b> " + e.getAssumpte() + "<br/>");
				HTMLResposta = HTMLResposta.concat("<b>Centre gestor:</b> " + e.getCentre().getNom() + "<br/>");
				HTMLResposta = HTMLResposta.concat("<b>Servei:</b> " + e.getSubcentre().getNom() + "<br/>");
				HTMLResposta = HTMLResposta.concat("<b>Unitat orgànica:</b> " + e.getUnitatOrganica() + "<br/>");
				HTMLResposta = HTMLResposta.concat("<b>Tipus d'escrit:</b> " + e.getEscrit().getNom()+ "<br/>");
				HTMLResposta = HTMLResposta.concat("<b>Matèria:</b> " + e.getMateria().getNom() + "<br/>");
				HTMLResposta = HTMLResposta.concat("<b>Motiu:</b> " + e.getMotiu().getNom() + "<br/>");
				HTMLResposta = HTMLResposta.concat("<b>Tipus de queixa:</b> " + e.getQueixa().getNom() + "<br/>");
				HTMLResposta = HTMLResposta.concat("<b>Tipus d'entrada:</b> " + e.getEntrada().getNom() + "<br/>");
				HTMLResposta = HTMLResposta.concat("<b>Data d'entrada:</b> " + e.getDataentrada() + "<br/>");
				HTMLResposta = HTMLResposta.concat("<b>Data de resposta:</b> " + e.getDataresposta() + "<br/>");
				HTMLResposta = HTMLResposta.concat("</td>");
				HTMLResposta = HTMLResposta.concat("<td style='width:6%'>&nbsp;&nbsp;</td>");
				
				// Dades personals
				HTMLResposta = HTMLResposta.concat("<td style='width:37%;valign:top'>");
				HTMLResposta = HTMLResposta.concat("<h2>Dades personals<hr/></h2>");
				HTMLResposta = HTMLResposta.concat("<b>Llinatges:</b> " + e.getLlinatge1() + " " + e.getLlinatge2() + "<br/>");
				HTMLResposta = HTMLResposta.concat("<b>Identificació:</b> " + e.getNumidentificacio() + "&nbsp;(" +  e.getIdentificacio().getNom() + ")<br/>");
				HTMLResposta = HTMLResposta.concat("<b>Telèfon:</b> " + e.getTelefon()+ "<br/>");
				HTMLResposta = HTMLResposta.concat("<b>Llengua:</b> " + e.getIdioma().getNom()+ "<br/>");
				HTMLResposta = HTMLResposta.concat("<b>Correu electrònic:</b> " + e.getEmail() + "<br/>");
				HTMLResposta = HTMLResposta.concat("<b>Mètodo de resposta:</b> " + e.getViaContestacio() + "<br/>");
				
				if (e.getViaContestacio().equals("postal") )
				{
					HTMLResposta = HTMLResposta.concat("<b>Carrer:</b>" + e.getDireccio() + ", núm. "+ e.getNumero() +", " + e.getPis()+ ", codi postal: " + e.getCodipostal() + "<br/>");	
					HTMLResposta = HTMLResposta.concat("<b>Municipi:</b>" + e.getMunicipi().getNom() + "<br/>");
					HTMLResposta = HTMLResposta.concat("<b>Provincia / Regió:</b>" + e.getMunicipi().getProvincia().getNom() + "<br/>");
				}
				HTMLResposta = HTMLResposta.concat("</td>");
				
				HTMLResposta = HTMLResposta.concat("</tr>");
				HTMLResposta = HTMLResposta.concat("<tr><td colspan='3' style='height:15px'>&nbsp;&nbsp;</td></tr>");
				
				// Petició i resposta
				HTMLResposta = HTMLResposta.concat("<tr><td colspan='3'>");
				HTMLResposta = HTMLResposta.concat("<h2>Petició<hr/></h2>");
				HTMLResposta = HTMLResposta.concat(e.getTextPeticio() + "<br/>");
				HTMLResposta = HTMLResposta.concat("</td></tr>");
				HTMLResposta = HTMLResposta.concat("<tr><td colspan='3' style='height:15px'>&nbsp;&nbsp;</td></tr>");
				HTMLResposta = HTMLResposta.concat("<tr><td colspan='3'>");
				HTMLResposta = HTMLResposta.concat("<h2>Resposta<hr/></h2>");
				HTMLResposta = HTMLResposta.concat(e.getTextResposta()+ "<br/>");
				HTMLResposta = HTMLResposta.concat("</td></tr>");
				HTMLResposta = HTMLResposta.concat("</table>");
				HTMLResposta = HTMLResposta.concat("</body>");
				HTMLResposta = HTMLResposta.concat("</html>");
				
				document.open();
				
				Image imghead = Image.getInstance("logo_conselleria.jpg");
				imghead.scaleAbsolute(150,130);
				document.add(imghead);
				
				Reader targetReader = new StringReader(HTMLResposta);
				XMLWorkerHelper.getInstance().parseXHtml(myWriter, document, targetReader);
				
				document.close();
				
				// Enviar a arxiu
				this.resultat = this.enviar_arxiu(e.getId(), e.getNumidentificacio(), e.getIdDocumentArxiuCAIB(), e.getIdDocumentArxiuCAIB());
				
				// Enviar a usuari
				
			}
			
		}
		catch(Exception ex)
		{
			this.strError = ex.toString();
			this.resultat = false;
		}
	}
	
		
	private boolean enviar_arxiu(Integer expedient_id, String identificacio_interessat, String id_expedient_arxiu_caib, String id_document_arxiu_caib)
	{
		IArxiuPlugin arxiuPlugin;
		List<String> organ;
		List<String> interessat;
		
		String codi_sia = "208132"; // TODO: verificar codi SIA, Toni Juanico, 29/07/2019
		String serie_documental = "S0001"; // TODO: verificar sèrie documental, Toni Juanico, 29/07/2019
		String dir3 = "A04026980"; // TODO: dir3, verificar
		
		
		Properties properties = new Properties();
		boolean resultat_enviar_arxiu = false;
		
		try {
			LOGGER.info("Dins enviar_arxiu");
			properties.load(ExpedientService.class.getClassLoader().getResourceAsStream("META-INF/arxiuCAIB.properties"));
			LOGGER.info("Carregam propietats: " + properties.getProperty("plugin.arxiu.caib.usuari"));
			arxiuPlugin = new ArxiuPluginCaib("",properties);
			organ = new ArrayList<String>();
			organ.add(dir3);
			interessat = new ArrayList<String>();
			interessat.add(identificacio_interessat);
			
			// Crear o recuperar expedient existent
			if (id_expedient_arxiu_caib!=null)
			{
				if (id_document_arxiu_caib!=null)
				{
					// Modificar document
					es.caib.plugins.arxiu.api.Document documentPerModificar = nouDocument(dir3, expedient_id,
							                                                              DocumentEstat.ESBORRANY, 
							                                                              DocumentFormat.PDF, 
							                                                              DocumentExtensio.PDF,
							                                                              "application/pdf",
							                                                              "QSSI_" + expedient_id + ".pdf", organ);
					documentPerModificar.setIdentificador(id_document_arxiu_caib);
					ContingutArxiu documentModificat = arxiuPlugin.documentModificar(documentPerModificar);
					LOGGER.info("Document modificat: " + documentModificat.getIdentificador());
					resultat_enviar_arxiu = true;
				}
				else
				{
					// Crear document
					es.caib.plugins.arxiu.api.Document documentPerCrear = nouDocument(dir3, expedient_id, 
							                                                          DocumentEstat.ESBORRANY, 
							                                                          DocumentFormat.PDF, 
							                                                          DocumentExtensio.PDF,
							                                                          "application/pdf",
							                                                          "QSSI_" + expedient_id + ".pdf", organ);
					ContingutArxiu documentCreat = arxiuPlugin.documentCrear(documentPerCrear,id_expedient_arxiu_caib);
					LOGGER.info("Document creat: " + documentCreat.getIdentificador());
					resultat_enviar_arxiu = true;
				}
			}
			else
			{
				// Crear expedient
				LOGGER.info("Crear expedient");
				es.caib.plugins.arxiu.api.Expedient expedientPerCrear = nouExpedient(dir3, expedient_id, codi_sia,serie_documental,organ, interessat);
				ContingutArxiu expedientCreat = arxiuPlugin.expedientCrear(expedientPerCrear);
								
				LOGGER.info("expedient creat amd identificador: " + expedientCreat.getIdentificador());
				
				// Crear document
				LOGGER.info("Crear document");
				es.caib.plugins.arxiu.api.Document documentPerCrear = nouDocument(dir3, expedient_id, 
                        DocumentEstat.ESBORRANY, 
                        DocumentFormat.PDF, 
                        DocumentExtensio.PDF,
                        "application/pdf",
                        "QSSI_" + expedient_id + ".pdf", organ);
				ContingutArxiu documentCreat = arxiuPlugin.documentCrear(documentPerCrear,expedientCreat.getIdentificador());
				LOGGER.info("Document creat: " + documentCreat.getIdentificador());
				
				resultat_enviar_arxiu = this.assignarDadesArxiuCAIB(expedient_id, expedientCreat.getIdentificador(), documentCreat.getIdentificador());
			}
			
			return resultat_enviar_arxiu;
			
		} 
		catch (Exception ex) {
			this.strError = ex.getMessage();
			return false;
		}
		
	}
	
	private es.caib.plugins.arxiu.api.Expedient nouExpedient(String dir3, Integer expedient_id, String codi_sia, String serie_documental, List<String> organ, List<String> interessat) {
		String nomExp = "ES_" + dir3 + "_2019_EXP_QSSI_" + expedient_id;
		final  es.caib.plugins.arxiu.api.Expedient expedient = new es.caib.plugins.arxiu.api.Expedient();
		expedient.setNom(nomExp);
		final ExpedientMetadades metadades = new ExpedientMetadades();
		metadades.setOrgans(organ);
		metadades.setDataObertura(new Date());
		metadades.setClassificacio(codi_sia); // Toni Juanico, 29/07/2019, recorda codi SIA
		metadades.setEstat(ExpedientEstat.OBERT); // Toni Juanico, 30/07/2019, recorda que la versió imprimible només la podràs fer en estat TANCAT!
		metadades.setInteressats(interessat);
		metadades.setSerieDocumental(serie_documental);
		expedient.setMetadades(metadades);
		return expedient;
	}

	private es.caib.plugins.arxiu.api.Document nouDocument(String dir3, Integer expedient_id, 
			es.caib.plugins.arxiu.api.DocumentEstat estat,
			es.caib.plugins.arxiu.api.DocumentFormat format,
			es.caib.plugins.arxiu.api.DocumentExtensio extensio,
			String tipusMime, String contingutResource, List<String> organ) throws IOException 
	{
		Calendar calOne = Calendar.getInstance();
		int year = calOne.get(Calendar.YEAR);
		String any = String.valueOf(year);
		
		es.caib.plugins.arxiu.api.Document document = new es.caib.plugins.arxiu.api.Document();
		document.setNom("ES_" + dir3 + "_"+ any +"_QSSI_" + expedient_id);
		es.caib.plugins.arxiu.api.DocumentMetadades documentMetadades = new es.caib.plugins.arxiu.api.DocumentMetadades();
		documentMetadades.setOrigen(es.caib.plugins.arxiu.api.ContingutOrigen.CIUTADA);
		documentMetadades.setOrgans(organ);
		documentMetadades.setDataCaptura(new Date());
		documentMetadades.setEstatElaboracio(es.caib.plugins.arxiu.api.DocumentEstatElaboracio.ORIGINAL);
		documentMetadades.setTipusDocumental(es.caib.plugins.arxiu.api.DocumentTipus.ALTRES);
		documentMetadades.setFormat(format);
		documentMetadades.setExtensio(extensio);
		document.setMetadades(documentMetadades);
		document.setEstat(estat);
		es.caib.plugins.arxiu.api.DocumentContingut contingut = new es.caib.plugins.arxiu.api.DocumentContingut();
		contingut.setArxiuNom(contingutResource);
		contingut.setTipusMime(tipusMime);
		
		FileInputStream myStream = new FileInputStream("result.pdf");
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
	    byte[] buf = new byte[1024];
	    try {
	    	for (int readNum; (readNum = myStream.read(buf)) != -1;) {
	    		bos.write(buf, 0, readNum); //no doubt here is 0
	        }
	    } catch (IOException ex) {
	    	LOGGER.info("Error llegint pdf per posar a l'arxiu CAIB" + ex.toString());
	    }
	    byte[] bytes = bos.toByteArray();
		contingut.setContingut(bytes);
		document.setContingut(contingut);
		return document;
	}

	@Override
	public boolean getResultat() {return this.resultat;	}

	@Override
	public String getError() { return this.strError; }

}
