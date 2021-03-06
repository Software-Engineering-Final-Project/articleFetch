package com.articlefetch.app.DataAccess.Repository;

import com.articlefetch.app.Busniess.Exceptions.ArticleNotFoundException;
import com.articlefetch.app.Controller.JacksonModels.Article;
import com.articlefetch.app.DataAccess.ModelDomain.ArticleEntity;
import org.junit.BeforeClass;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
public class ArticleRepositoryTest {

    @Mock
    ArticleRepository articleDAO;

    @BeforeClass
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void findByID() {
        ArticleEntity articleE = new ArticleEntity()
                .create(1, "Bla Bla Bla", "Schappel",
                        "True Things", 1, "");

        when(articleDAO.findById(1)).thenReturn(java.util.Optional.ofNullable(articleE));
        assertEquals(articleE, articleDAO.findById(1).get());
    }

    @Test
    void findById_when_id_is_not_present() {
        when(articleDAO.findById(2)).thenThrow(ArticleNotFoundException.class);
        assertThrows(ArticleNotFoundException.class, () -> {
            articleDAO.findById(2);
        });
    }

    @Test
    void findAll() {
        ArticleEntity articleE1 = new ArticleEntity()
                .create(1, "Bla Bla Bla", "Schappel",
                        "True Things", 1, "");

        ArticleEntity articleE2 = new ArticleEntity()
                .create(2, "Bla Bla Blaaaa", "Schappelll",
                        "True Thingsss", 2, "");

        List<ArticleEntity> articleList = new ArrayList<>();
        articleList.add(articleE1);
        articleList.add(articleE2);

        when(articleDAO.findAll()).thenReturn(articleList);

        assertEquals(articleList, articleDAO.findAll());
    }

    @Test
    void findAll_when_table_is_empty() {
        List<ArticleEntity> articleList = new ArrayList<>();
        when(articleDAO.findAll()).thenReturn(articleList);

        assertEquals(articleList, articleDAO.findAll());
    }

    @Test
    void find_existing_conflicts_no_conflict() {
        List<ArticleEntity> articleList = new ArrayList<>();
        when(articleDAO.findExistingConflicts("Josh", "jschappel.com"))
                .thenReturn(articleList);

        assertTrue(articleDAO.findExistingConflicts("Joshua", "jschappel.com").isEmpty());
    }

    @Test
    void find_existing_conflicts_with_conflict() {
        ArticleEntity articleE1 = new ArticleEntity()
                .create(1, "Bla Bla Bla", "Schappel",
                        "True Things", 1, "");

        List<ArticleEntity> accountList = new ArrayList<>();
        accountList.add(articleE1);


        when(articleDAO.findExistingConflicts("Joshua", "jschappel.com")).thenReturn(accountList);

        assertTrue(!articleDAO.findExistingConflicts("Joshua", "jschappel.com").isEmpty());
    }

    @Test
    void find_article_by_name() {
        ArticleEntity articleE1 = new ArticleEntity()
                .create(1, "Bla Bla Bla", "Schappel",
                        "Josh", 1, "");


        when(articleDAO.findArticleByname("Josh")).thenReturn(Optional.of(articleE1));

        ArticleEntity e = articleDAO.findArticleByname("Josh").get();
        assertEquals(e.getArticleTitle(), articleE1.getArticleTitle());
        assertEquals(e.getArticleDesc(), articleE1.getArticleDesc());
        assertEquals(e.getArticleAuthors(), articleE1.getArticleAuthors());
        assertEquals(e.getStaredArticles_id(), articleE1.getStaredArticles_id());
        assertEquals(e.getfk_category_id(), articleE1.getfk_category_id());
    }

    @Test
    void find_article_bu_name_that_does_not_exist() {
        when(articleDAO.findArticleByname("Josh")).thenThrow(ArticleNotFoundException.class);

        assertThrows(ArticleNotFoundException.class, () -> {
            articleDAO.findArticleByname("Josh");
        });
    }


}
