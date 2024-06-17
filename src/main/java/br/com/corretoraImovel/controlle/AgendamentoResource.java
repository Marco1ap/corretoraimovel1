package br.com.corretoraImovel.controlle;

import br.com.corretoraImovel.model.Agendamento;
import br.com.corretoraImovel.repository.AgendamentoRepository;
import br.com.corretoraImovel.services.AgendamentoService;
import jakarta.validation.Valid;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@Valid
@Path("/agendamentos")
public class AgendamentoResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //CONSULTA AGENDAMENTO
    public Response findAll() {

        List<Agendamento> retorno = AgendamentoRepository.consultaTodosAgendamentos();

        Response.ResponseBuilder response = Response.ok();
        response.entity(retorno);

        return response.build();

    }

    //CONSULTA AGENDAMENTO PELO ID
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long agendamentoId) throws SQLException {

        Agendamento agendamento = AgendamentoService.findById(agendamentoId);

        if (agendamento != null) {
            Response.ResponseBuilder response = Response.ok();
            response.entity(agendamento);
            return response.build();
        } else {
            Response.ResponseBuilder response = Response.status(404);
            return response.build();
        }

    }

    //ALTERA AGENDAMENTO
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, Agendamento agendamento) throws SQLException {
        Agendamento novo = null;
        novo = AgendamentoService.update(id, agendamento);

        if(novo == null) {
            Response.ResponseBuilder response = Response.status(404);
            response.entity("Informe os campos obrigatórios ou verifique o documento CPF.!");
            return response.build();

        }
        return Response.ok(novo).build();
    }

    //INCLUI AGENDAMENTO
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(Agendamento agendamento) {

        Agendamento resp = AgendamentoService.save(agendamento);
        if(resp == null) {
            Response.ResponseBuilder response = Response.status(404);
            response.entity("Informe os campos obrigatórios ou verifique o documento CPF.!");
            return response.build();
        }
        // @formatter:off
        final URI agendamentoUri = UriBuilder
                .fromResource(AgendamentoResource.class)
                .path("/agendamento/{id}")
                .build(resp.getId());
        // @formatter:on

        Response.ResponseBuilder response = Response.created(agendamentoUri);

        response.entity(resp);

        return response.build();

    }

    //DELETA AGENDAMENTO
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long agendamentoId) throws SQLException {

        if (AgendamentoService.delete(agendamentoId)) {
            Response.ResponseBuilder response = Response.status(204);
            response.entity("Registro excluído com sucesso!");
            return response.build();
        } else {
            System.out.println("Não foi possível remover o agendamento: " + agendamentoId);
            Response.ResponseBuilder response = Response.status(404);
            response.entity("Não foi possível remover o agendamento!");
            return response.build();
        }
    }


}