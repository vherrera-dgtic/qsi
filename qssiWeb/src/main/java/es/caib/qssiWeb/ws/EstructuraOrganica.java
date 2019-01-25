package es.caib.qssiWeb.ws;

import javax.jws.WebService;

import javax.jws.WebMethod;


@WebService
public class EstructuraOrganica
{
   public EstructuraOrganica() { }
   
   @WebMethod
   public String Hello()
   {
      return "Hola";
   }
   
}

