package br.com.corretoraImovel.controlle;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

import org.glassfish.grizzly.http.util.HttpStatus;

import br.com.corretoraImovel.model.Pessoa;
import br.com.corretoraImovel.repository.VisitanteRepository;
import br.com.corretoraImovel.services.VisitanteService;
import jakarta.json.JsonObject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.UriBuilder;

@Valid
@Path("/visitantes")
public class VisitanteResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	//CONSULTA VISITANTE
	public Response findAll() {
	
		List<Pessoa> retorno = VisitanteRepository.consultaTodosVisitantes();
		
		ResponseBuilder response = Response.ok();
		response.entity(retorno);
		
		return response.build();
		
	}
	
	//CONSULTA VISITANTE PELO ID
	@GET
	@Path("/{id}")
	public Response findById(@PathParam("id") Long visitanteId) {
		
		Pessoa visitante = VisitanteService.findById(visitanteId);

		if (visitante != null) {
			ResponseBuilder response = Response.ok();
			response.entity(visitante);
			return response.build();
		} else {
			ResponseBuilder response = Response.status(404);
			return response.build();
		}

	}

	//ALTERA VISITANTE
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") Long id, @Valid Pessoa visitante) {
		Pessoa novo = null;
		novo = VisitanteService.update(id, visitante);
		return Response.ok(novo).build();
	}

	//INCLUI VISITANTE
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response save(Pessoa visitante) {
	//	public Response save(@Valid Pessoa visitante) {
		
		//Pessoa resp = VisitanteService.save(visitantee);
		Pessoa resp = VisitanteService.save(visitante);
		if(resp == null) {		
			ResponseBuilder response = Response.status(404);
			response.entity("Informe os campos obrigatórios ou verifique o documento CPF.!");
			return response.build();
		}
		// @formatter:off
 		final URI visitanteUri = UriBuilder
				.fromResource(VisitanteResource.class)
				.path("/visitante/{id}")
				.build(resp.getId());
 		// @formatter:on

 		ResponseBuilder response = Response.created(visitanteUri);
	
		response.entity(resp);

		return response.build();

	}
	
	//DELETA VISITANTE
	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") Long visitanteId) throws SQLException {
	
		if (VisitanteService.delete(visitanteId)) {
			ResponseBuilder response = Response.status(204);
			response.entity("Registro excluído com sucesso!");
			return response.build();
		} else {
			System.out.println("Não foi possível remover o visitante: " + visitanteId);
			ResponseBuilder response = Response.status(404);
			response.entity("Não foi possível remover o visitante!");
			return response.build();
		}
	}
	
	
}
