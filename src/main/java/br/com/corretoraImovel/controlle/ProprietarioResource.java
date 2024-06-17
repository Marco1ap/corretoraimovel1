package br.com.corretoraImovel.controlle;


import br.com.corretoraImovel.model.Proprietario;
import br.com.corretoraImovel.repository.ProprietarioRepository;
import br.com.corretoraImovel.services.ProprietarioService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;
import java.util.List;

//@Valid
@Path("/proprietario")
public class ProprietarioResource {

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	//CONSULTA proprietario
	public Response findAll() {

		List<Proprietario> retorno = ProprietarioRepository.consultaTodosProprietarios();

		ResponseBuilder response = Response.ok();
		response.entity(retorno);

		return response.build();

	}


	//CONSULTA PROPRIETARIO PELO ID
	@GET
	@Path("/{id}")
	public Response findById(@PathParam("id") Long proprietarioId) {

		Proprietario proprietario = ProprietarioService.findById(proprietarioId);

		if (proprietario != null) {
			ResponseBuilder response = Response.ok();
			response.entity(proprietario);
			return response.build();
		} else {
			ResponseBuilder response = Response.status(404);
			return response.build();
		}

	}

	//ALTERA PROPRIETARIO
	@PUT
	@Path("/{id}")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response update(@PathParam("id") Long id, @Valid Proprietario proprietario) {
		Proprietario novo = null;
		novo = ProprietarioService.update(id, proprietario);
		return Response.ok(novo).build();
	}

	//INCLUI PROPRIETARIO
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response save(Proprietario proprietario) {

	Proprietario resp = ProprietarioService.save(proprietario);

		if(resp == null) {
			ResponseBuilder response = Response.status(404);
			response.entity("Informe os campos obrigatórios ou verifique o documento CPF!");
			return response.build();
		}
		// @formatter:off
 		final URI proprietarioUri = UriBuilder
				.fromResource(ProprietarioResource.class)
				.path("/proprietario/{id}")
				.build(resp.getId());
 		// @formatter:on

 		ResponseBuilder response = Response.created(proprietarioUri);

		response.entity(resp);

		return response.build();

	}

	//DELETA PROPRIETARIO
	@DELETE
	@Path("/{id}")
	public Response delete(@PathParam("id") Long proprietarioId) {

		if (ProprietarioService.delete(proprietarioId)) {
			ResponseBuilder response = Response.noContent();
			return response.build();
		} else {
			System.out.println("Não foi possível remover o proprietario: " + proprietarioId);
			ResponseBuilder response = Response.status(404);
			response.entity("Registro excluído com suceso!");
			return response.build();
		}
	}
	
}
