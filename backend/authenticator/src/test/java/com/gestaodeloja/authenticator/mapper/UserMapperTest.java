package com.gestaodeloja.authenticator.mapper;

import com.gestaodeloja.authenticator.domain.User;
import com.gestaodeloja.authenticator.domain.enums.UserRole;
import com.gestaodeloja.authenticator.dto.request.CreateUserRequestDto;
import com.gestaodeloja.authenticator.dto.response.CreateUserResponseDto;
import com.gestaodeloja.authenticator.fixture.UserFixture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class UserMapperTest {

    private final UserMapper mapper = new UserMapperImpl();

    @Test
    void deveMapearUserParaCreateUserResponseDto() {

        User user = UserFixture.criaUser(1L, "joao.pedro", "123456", null, true, null, null);

        CreateUserResponseDto result = mapper.toResponseDto(user);

        assertThat(result.id()).isEqualTo(1L);
        assertThat(result.userName()).isEqualTo("joao.pedro");
        assertThat(result.role()).isNull();
        assertThat(result.enabled()).isTrue();
        assertThat(result.created()).isNull();
    }

    @Test
    void deveRetornarNullAoMapearUserParaCreateUserResponseDto() {
        assertThat(mapper.toResponseDto(null)).isNull();
    }

    @Test
    void deveMapearCreateUserRequestDtoParaUser() {

        CreateUserRequestDto dto = UserFixture.criaCreateUserRequestDto("joao.pedro", "123456", UserRole.USER);

        User result = mapper.toRequestEntity(dto);

        assertThat(result.getUsername()).isEqualTo("joao.pedro");
        assertThat(result.getRole()).isEqualTo(UserRole.USER);
    }

    @Test
    void deveRetornarNullAoMapearCreateUserRequestDtoParaUser() {
        assertThat(mapper.toRequestEntity(null)).isNull();
    }
}
