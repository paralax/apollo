package io.logz.apollo.controllers;

import com.google.common.collect.ImmutableMap;
import io.logz.apollo.common.HttpStatus;
import io.logz.apollo.dao.ServiceDao;
import io.logz.apollo.database.ApolloMyBatis;
import io.logz.apollo.database.ApolloMyBatis.ApolloMyBatisSession;
import io.logz.apollo.models.Service;
import org.rapidoid.annotation.Controller;
import org.rapidoid.annotation.GET;
import org.rapidoid.annotation.POST;
import org.rapidoid.annotation.PUT;
import org.rapidoid.http.Req;
import org.rapidoid.security.annotation.LoggedIn;

import java.util.List;
import java.util.Map;

import static io.logz.apollo.common.ControllerCommon.assignJsonResponseToReq;

/**
 * Created by roiravhon on 12/20/16.
 */
@Controller
public class ServiceController {

    @LoggedIn
    @GET("/service")
    public List<Service> getAllServices() {
        try (ApolloMyBatisSession apolloMyBatisSession = ApolloMyBatis.getSession()) {
            ServiceDao serviceDao = apolloMyBatisSession.getDao(ServiceDao.class);
            return serviceDao.getAllServices();
        }
    }

    @LoggedIn
    @GET("/service/{id}")
    public Service getService(int id) {
        try (ApolloMyBatisSession apolloMyBatisSession = ApolloMyBatis.getSession()) {
            ServiceDao serviceDao = apolloMyBatisSession.getDao(ServiceDao.class);
            return serviceDao.getService(id);
        }
    }

    @LoggedIn
    @POST("/service")
    public void addService(String name, String deploymentYaml, String serviceYaml, Req req) {
        try (ApolloMyBatisSession apolloMyBatisSession = ApolloMyBatis.getSession()) {
            ServiceDao serviceDao = apolloMyBatisSession.getDao(ServiceDao.class);
            Service newService = new Service();

            newService.setName(name);
            newService.setDeploymentYaml(deploymentYaml);
            newService.setServiceYaml(serviceYaml);

            serviceDao.addService(newService);
            assignJsonResponseToReq(req, HttpStatus.CREATED, newService);
        }
    }

    @LoggedIn
    @PUT("/service/{id}")
    public void updateService(int id, String name, String deploymentYaml, String serviceYaml, Req req) {
        try (ApolloMyBatisSession apolloMyBatisSession = ApolloMyBatis.getSession()) {
            ServiceDao serviceDao = apolloMyBatisSession.getDao(ServiceDao.class);
            Service service = serviceDao.getService(id);

            if (service == null) {
                Map<String, String> message = ImmutableMap.of("message", "Service not found");
                assignJsonResponseToReq(req, HttpStatus.NOT_FOUND, message);
                return;
            }

            service.setName(name);
            service.setDeploymentYaml(deploymentYaml);
            service.setServiceYaml(serviceYaml);

            serviceDao.updateService(service);
            assignJsonResponseToReq(req, HttpStatus.OK, service);
        }
    }

}
