package com.moringaschool.myproperty.api;

import com.moringaschool.myproperty.models.Property;
import com.moringaschool.myproperty.models.PropertyManager;
import com.moringaschool.myproperty.models.Tenant;
import com.moringaschool.myproperty.models.Unit;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiCalls {

    @POST("/propertyManager")
    Call<PropertyManager> addManager(
            @Body PropertyManager manager
    );

    @POST("/property")
    Call<Property> addProperty(
            @Body Property property
    );

    @GET("/property")
    Call<List<Property>> getProperties();

    @GET("/managerProperties/{name}")
    Call<List<Property>> getManagerProperties(
            @Path("name") String managerName
    );

    //add a tenant
    @POST("/tenant")
    Call<Tenant> addTenant(
            @Body Tenant tenant
    );

    @POST("/unit")
    Call<Unit> addUnit(
            @Body Unit unit
    );

    @GET("/units/{name}")
    Call<List<Unit>> propertyUnits(
            @Path("name") String propertyName
    );

}
