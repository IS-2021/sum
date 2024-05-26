package org.example.sumatyw_backend.ingredients;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.MariaDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class IngredientRepositoryTest {

    @Container
    @ServiceConnection
    static MariaDBContainer<?> mariaDB = new MariaDBContainer<>(DockerImageName.parse("mariadb:10.5.5"));

    @Autowired
    private IngredientRepository ingredientRepository;

    @AfterEach
    void tearDown() {
        ingredientRepository.deleteAll();
    }

    @Test
    void connectionTest(){
        assertThat(mariaDB.isCreated()).isTrue();
        assertThat(mariaDB.isRunning()).isTrue();
    }

    @Test
    void findByNameAndType_ReturnsIngredient_IfGivenIngredientExists() {
        // given
        Ingredient ingr = Ingredient.builder().name("beer").type("drinks").build();
        ingr = ingredientRepository.save(ingr);

        // when
        Optional<Ingredient> foundIngredient = ingredientRepository.findByNameAndType(ingr.getName(), ingr.getType());

        // then
        assertThat(foundIngredient.isPresent()).isTrue();
        assertThat(foundIngredient.get().getName()).isEqualTo(ingr.getName());
        assertThat(foundIngredient.get().getType()).isEqualTo(ingr.getType());
    }

    @Test
    void findByNameAndType_ReturnsEmptyOptional_IfGivenIngredientDoesNotExist() {
        // given
        Ingredient ingr = Ingredient.builder().name("beer").type("drinks").build();
        ingr = ingredientRepository.save(ingr);

        // when
        Optional<Ingredient> foundIngredient = ingredientRepository.findByNameAndType("dirt", ingr.getType());

        // then
        assertThat(foundIngredient.isPresent()).isFalse();
    }
}
