package br.com.corretoraImovel.controlle;

import br.com.corretoraImovel.model.Imovel;
import br.com.corretoraImovel.repository.ImovelRepository;
//import br.com.corretoraImovel.services.ImovelService;
import br.com.corretoraImovel.services.ImovelService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.ResponseBuilder;
import jakarta.ws.rs.core.UriBuilder;

import java.net.URI;
import java.sql.SQLException;
import java.util.List;

@Path("/imovel")
public class ImovelResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    //CONSULTA Imovel
    public Response findAll() {

        List<Imovel> retorno = ImovelRepository.consultaTodosimovels();

        ResponseBuilder response = Response.ok();
        response.entity(retorno);

        return response.build();

    }

    //CONSULTA Imovel PELO ID
    @GET
    @Path("/{id}")
    public Response findById(@PathParam("id") Long imovelId) {

        Imovel imovel = ImovelService.findById(imovelId);

        if (imovel != null) {
            ResponseBuilder response = Response.ok();
            response.entity(imovel);
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
    public Response update(@PathParam("id") Long id,  Imovel imovel) throws SQLException {
        Imovel novo = null;
        novo = ImovelService.update(id, imovel);
        return Response.ok(novo).build();
    }

    //INCLUI PROPRIETARIO
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response save(Imovel imovel) {
        Imovel resp = ImovelService.save(imovel);
        if(resp == null) {
            ResponseBuilder response = Response.status(404);
            response.entity("Informe os campos obrigatórios");
            return response.build();
        }
        // @formatter:off
        final URI imovelUri = UriBuilder
                .fromResource(ProprietarioResource.class)
                .path("/imovel/{id}")
                .build(resp.getId());
        // @formatter:on

        ResponseBuilder response = Response.created(imovelUri);

        response.entity(resp);

        return response.build();

    }

    //DELETA IMOVEL
    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long imovelId) throws SQLException {

        if (ImovelService.delete(imovelId)) {
            ResponseBuilder response = Response.status(200);
            response.entity("Registro excluído com sucesso!");
            return response.build();
        } else {
            System.out.println("Não foi possível remover o imovel: " + imovelId);
            ResponseBuilder response = Response.status(404);
            response.entity("Não foi possível remover o imovel!");
            return response.build();
        }
    }

}
