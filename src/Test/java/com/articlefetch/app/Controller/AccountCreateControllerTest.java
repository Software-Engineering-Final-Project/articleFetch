package com.articlefetch.app.Controller;

import com.articlefetch.app.Busniess.Exceptions.AccountNotFoundException;
import com.articlefetch.app.Busniess.Exceptions.DuplicateEntryException;
import com.articlefetch.app.Busniess.Service.AccountService;
import com.articlefetch.app.Controller.JacksonModels.Account;
import com.articlefetch.app.Controller.JacksonModels.AccountCreate;
import com.articlefetch.app.Controller.JacksonModels.Category;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.core.IsNull;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;


import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.core.Is.is;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebMvcTest(AccountController.class)
class AccountCreateControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private AccountService service;

    @Test
    void getAllAccounts() throws Exception {
        Account a1 = new Account("jschappel",  "Joshua",
                "Schappel", "j@shu.edu", 1,  null, "Images/default_user.png", true);

        List<Account> accountCreateList = Arrays.asList(a1);

        given(service.getAllAccounts()).willReturn(accountCreateList);

        mvc.perform(get("/account/allAccounts")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].username", is("jschappel")))
                .andExpect(jsonPath("$[0].first_name", is("Joshua")))
                .andExpect(jsonPath("$[0].last_name", is("Schappel")))
                .andExpect(jsonPath("$[0].email", is("j@shu.edu")))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].status", is(true)));
    }

    @Test
    void getAccount() throws Exception {
        Account a1 = new Account("jschappel",  "Joshua",
                "Schappel", "j@shu.edu", 1,  null, "Images/default_user.png", true);

        given(service.getAccount(1)).willReturn(a1);

        mvc.perform(get("/account/getAccount?id=1")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['username']", is("jschappel")))
                .andExpect(jsonPath("$['first_name']", is("Joshua")))
                .andExpect(jsonPath("$['last_name']", is("Schappel")))
                .andExpect(jsonPath("$['email']", is("j@shu.edu")))
                .andExpect(jsonPath("$['id']", is(1)))
                .andExpect(jsonPath("$['status']", is(true)));
    }

    @Test
    void getAccount_without_params() throws Exception {
        mvc.perform(get("/account/getAccount")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("id parameter is missing")));
    }

    @Test
    void getAccount_with_invalid_id() throws Exception {
        given(service.getAccount(100)).willThrow(new AccountNotFoundException(100));
        mvc.perform(get("/account/getAccount?id=100")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("User with id: 100 does not exist")));
    }

    @Test
    void createAccount() throws Exception {
        AccountCreate a1 = new AccountCreate("jschappel", "password", "Joshua",
                "Schappel", "j@shu.edu", null, true);

        when(service.createAccount(any())).thenReturn(1);

        mvc.perform(put("/account/create")
                .content(asJsonString(a1))
                .contentType(APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$['id']", is("1")));
    }

    @Test
    void createAccount_with_out_params() throws Exception {
        mvc.perform(put("/account/create")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void create_duplicate_account() throws Exception {
        AccountCreate a1 = new AccountCreate("jschappel", "password", "Joshua",
                "Schappel", "j@shu.edu", null, true);

        doThrow(DuplicateEntryException.class).when(service).createAccount(a1);

        mvc.perform(put("/account/create")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void reactivateAccount() throws Exception {

        doNothing().when(service).reactivateAccount(1);

        mvc.perform(put("/account/reactivate?id=1")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$['message']", is("Success")));
    }

    @Test
    void reactivateAccount_with_invalid_id() throws Exception {

        doThrow(new AccountNotFoundException(100)).when(service).reactivateAccount(100);

        mvc.perform(put("/account/reactivate?id=100")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['message']", is("User with id: 100 does not exist")));
    }

    @Test
    void reactivateAccount_with_no_params() throws Exception {
        mvc.perform(put("/account/reactivate")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void deactivateAccount() throws Exception {

        doNothing().when(service).deactivateAccount(1);

        mvc.perform(put("/account/deactivate?id=1")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$['message']", is("Success")));
    }

    @Test
    void deactivateAccount_with_invalid_id() throws Exception {

        doThrow(new AccountNotFoundException(100)).when(service).deactivateAccount(100);

        mvc.perform(put("/account/deactivate?id=100")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$['message']", is("User with id: 100 does not exist")));
    }

    @Test
    void deactivateAccount_with_no_params() throws Exception {
        mvc.perform(put("/account/deactivate")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getCategories() throws Exception {
        List<Category> list = new ArrayList<>();
        list.add(new Category(1, "Education", "Stuff"));
        list.add(new Category(2, "Research", "More stuff"));

        given(service.getStarredCategories(1)).willReturn(list);

        mvc.perform(get("/account/getCategories?id=1")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Education")))
                .andExpect(jsonPath("$[0].description", is("Stuff")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Research")))
                .andExpect(jsonPath("$[1].description", is("More stuff")));
    }

    @Test
    void getCategories_with_invalid_account() throws Exception {
        given(service.getStarredCategories(20)).willThrow(new AccountNotFoundException(20));

        mvc.perform(get("/account/getCategories?id=20")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void  getCategories_with_no_params() throws Exception {
        mvc.perform(get("/account/getCategories")
                .contentType(APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    void addCategories() throws Exception {
        List<Category> list = new ArrayList<>();
        list.add(new Category(1, "Education", "Stuff"));
        list.add(new Category(2, "Research", "More stuff"));

        given(service.addStarredCategories(anyList(), anyInt())).willReturn(list);

        mvc.perform(post("/account/addCategories?id=1")
                .contentType(APPLICATION_JSON)
                .content(asJsonString(list)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("Education")))
                .andExpect(jsonPath("$[0].description", is("Stuff")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].name", is("Research")))
                .andExpect(jsonPath("$[1].description", is("More stuff")));

    }

    @Test
    void addCategories_with_invalid_account() throws Exception {
        List<Category> list = new ArrayList<>();
        list.add(new Category(1, "Education", "Stuff"));
        list.add(new Category(2, "Research", "More stuff"));

        given(service.addStarredCategories(anyList(), anyInt())).willThrow(new AccountNotFoundException(20));

        mvc.perform(post("/account/addCategories?id=20")
                .contentType(APPLICATION_JSON)
                .content(asJsonString(list)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void removeCategories() throws Exception {
        List<Category> list = new ArrayList<>();
        list.add(new Category(1, "Education", "Stuff"));

        List<Category> list2 = new ArrayList<>();
        list2.add(new Category(2, "Research", "More stuff"));

        given(service.removeStarredCategories(anyList(), anyInt())).willReturn(list2);

        mvc.perform(post("/account/removeCategories?id=1")
                .contentType(APPLICATION_JSON)
                .content(asJsonString(list)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].name", is("Research")))
                .andExpect(jsonPath("$[0].description", is("More stuff")));
    }

    @Test
    void removeCategories_with_invalid_account() throws Exception {
        List<Category> list = new ArrayList<>();
        list.add(new Category(1, "Education", "Stuff"));
        list.add(new Category(2, "Research", "More stuff"));

        given(service.removeStarredCategories(anyList(), anyInt())).willThrow(new AccountNotFoundException(20));

        mvc.perform(post("/account/removeCategories?id=20")
                .contentType(APPLICATION_JSON)
                .content(asJsonString(list)))
                .andExpect(status().isBadRequest());
    }


    // Helper function that converts a Jackson Object to a json string
    private static String asJsonString(Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}