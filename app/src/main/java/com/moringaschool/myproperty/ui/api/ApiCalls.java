package com.moringaschool.myproperty.ui.api;

import com.moringaschool.myproperty.ui.models.Property;
import com.moringaschool.myproperty.ui.models.PropertyManager;
import com.moringaschool.myproperty.ui.models.Tenant;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiCalls {

    @POST("/propertymanager")
    Call<PropertyManager> addManager(
            @Body PropertyManager manager
    );

    @POST("/property")
    Call<Property> addProperty(
            @Body Property property
    );

    @GET("/property")
    Call<List<Property>> getProperties();

    @GET("/managerProperties/{managerName}")
    Call<List<Property>> getManagerProperties(
            @Path("managerName") String managerName
    );

    //add a tenant
    @POST("/tenant")
    Call<Tenant> addTenant(
            @Body Tenant tenant
    );

}
