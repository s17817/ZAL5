package rest;

import domain.Comment;
import domain.Product;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/products")
@Stateless
public class ProductsResources {

    @PersistenceContext
    EntityManager em;

    /*@GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> getAll()
    {
        return em.createNamedQuery("product.all", Product.class).getResultList();
    }*/

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Product> getAllProducts(@QueryParam("from") int priceFrom,
                                        @QueryParam("to") int priceTo,
                                        @QueryParam("name") String name,
                                        @QueryParam("category") String category) {
        if (name != null)
        {
            return em.createNamedQuery("product.findByName", Product.class)
                    .setParameter("productName", name)
                    .getResultList();
        }
        if(category.equals("Graphic Cards") || category.equals("Motherboards") || category.equals("Hard Disks") || category.equals("RAM")) {
            return em.createNamedQuery("product.findByCategory", Product.class)
                    .setParameter("productCategory", category)
                    .getResultList();
        }
        if (priceFrom>0 && priceTo>0)
        {
            return em.createNamedQuery("product.findByPrice", Product.class)
                    .setParameter("priceFrom", priceFrom)
                    .setParameter("priceTo", priceTo)
                    .getResultList();
        }
        return em.createNamedQuery("product.all", Product.class).getResultList();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response Add(Product product){
        em.persist(product);
        return Response.ok(product.getId()).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@PathParam("id") int id) {
        Product result = em.createNamedQuery("product.id", Product.class)
                .setParameter("productId", id)
                .getSingleResult();
        if (result == null) {
            return Response.status(404).build();
        }
        return Response.ok(result).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, Product p){
        Product result = em.createNamedQuery("product.id", Product.class)
                .setParameter("productId", id)
                .getSingleResult();
        if(result==null)
            return Response.status(404).build();
        result.setName(p.getName());
        result.setPrice(p.getPrice());
        result.setCategory(p.getCategory());
        em.persist(result);
        return Response.ok().build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") int id){
        Product result = em.createNamedQuery("product.id", Product.class)
                .setParameter("productId", id)
                .getSingleResult();
        if(result==null)
            return Response.status(404).build();
        em.remove(result);
        return Response.ok().build();
    }

    @GET
    @Path("/{productId}/comments")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Comment> getComments(@PathParam("productId") int productId){
        Product result = em.createNamedQuery("product.id", Product.class)
                .setParameter("productId", productId)
                .getSingleResult();
        if(result==null)
            return null;
        return result.getComments();
    }

    @POST
    @Path("/{id}/comments")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addComment(@PathParam("id") int productId, Comment comment){
        Product result = em.createNamedQuery("product.id", Product.class)
                .setParameter("productId", productId)
                .getSingleResult();
        if(result==null)
            return Response.status(404).build();
        result.getComments().add(comment);
        comment.setProduct(result);
        em.persist(comment);
        return Response.ok().build();
    }
}
