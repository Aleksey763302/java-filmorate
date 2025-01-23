package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.*;
import ru.yandex.practicum.filmorate.model.dto.FilmDto;
import ru.yandex.practicum.filmorate.model.dto.UserDto;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.service.GenreService;
import ru.yandex.practicum.filmorate.service.MpaService;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.database.FilmDbStorage;
import ru.yandex.practicum.filmorate.storage.database.GenreDbStorage;
import ru.yandex.practicum.filmorate.storage.database.RatingDbStorage;
import ru.yandex.practicum.filmorate.storage.database.UserDbStorage;
import ru.yandex.practicum.filmorate.storage.database.mappers.film.*;
import ru.yandex.practicum.filmorate.storage.database.mappers.user.FriendsRowMapper;
import ru.yandex.practicum.filmorate.storage.database.mappers.user.ResponseUserRowMapper;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserService.class, UserDbStorage.class, ResponseUserRowMapper.class, FriendsRowMapper.class,
        FilmService.class, FilmDbStorage.class, ResponseFilmRowMapper.class, GenreDbStorage.class, GenreRowMapper.class,
        RatingDbStorage.class, MpaRowMapper.class, LikesRowMapper.class, IDRowMapper.class, GenreService.class, MpaService.class})
class FilmorateApplicationTests {
    private final UserService userService;
    private final FilmService filmService;
    private final GenreService genreService;
    private final MpaService mpaService;

    @Test
    public void testFindUserById() {
        Optional<UserDto> newUser = userService.createUser(getRequest());
        if (newUser.isPresent()) {
            final Integer id = newUser.get().getId();
            assertThat(id).isNotNull();
            Optional<UserDto> userOptional = userService.getUserByID(id);
            assertThat(userOptional)
                    .isPresent()
                    .hasValueSatisfying(user -> {
                                assertThat(user).hasFieldOrPropertyWithValue("id", id);
                                assertThat(user).hasFieldOrPropertyWithValue("login", "dolore");
                                assertThat(user).hasFieldOrPropertyWithValue("name", "Nick Name");
                                assertThat(user).hasFieldOrPropertyWithValue("email", "mail@mail.ru");
                                assertThat(user).hasFieldOrPropertyWithValue("birthday", LocalDate.parse("1946-08-20"));
                            }
                    );
        }
    }

    @Test
    public void testGetAllUsers() {
        userService.createUser(getRequest());
        userService.createUser(getRequest());
        userService.createUser(getRequest());
        userService.createUser(getRequest());
        Optional<List<UserDto>> usersDto = userService.getAllUsers();
        usersDto.ifPresent(users -> assertThat(users).hasSize(4));
    }

    @Test
    public void testAddFriends() {
        Optional<UserDto> userDtoOptional1 = userService.createUser(getRequest());
        Optional<UserDto> userDtoOptional2 = userService.createUser(getRequest());
        if (userDtoOptional1.isPresent() && userDtoOptional2.isPresent()) {
            final int userId1 = userDtoOptional1.get().getId();
            final int userId2 = userDtoOptional2.get().getId();
            userService.addFriend(userId1, userId2);
            Optional<List<UserDto>> friendsUser1 = userService.getFriendsUser(userId1);
            Optional<List<UserDto>> friendsUser2 = userService.getFriendsUser(userId2);
            if (friendsUser2.isPresent() && friendsUser1.isPresent()) {
                List<UserDto> friendsListUser1 = friendsUser1.get();
                assertThat(friendsListUser1).hasSize(0);
                List<UserDto> friendsListUser2 = friendsUser2.get();
                assertThat(friendsListUser2).contains(userDtoOptional1.get());
            }
        }
    }

    @Test
    public void testCreateFilm() {
        Optional<FilmDto> newFilm = filmService.createFilm(getRequestCreateFilm());
        if (newFilm.isPresent()) {
            final Integer id = newFilm.get().getId();
            assertThat(id).isNotNull();
            Optional<FilmDto> filmOptional = filmService.getFilmById(id);

            if (mpaService.getMpaById(1).isPresent() && genreService.getGenreById(2).isPresent()) {
                Mpa mpa = mpaService.getMpaById(1).get();
                List<Genre> genres = new ArrayList<>();
                genres.add(genreService.getGenreById(2).get());
                assertThat(filmOptional)
                        .isPresent()
                        .hasValueSatisfying(film -> {
                                    assertThat(film).hasFieldOrPropertyWithValue("id", id);
                                    assertThat(film).hasFieldOrPropertyWithValue("name", "nisi eiusmod");
                                    assertThat(film).hasFieldOrPropertyWithValue("description", "adipisicing");
                                    assertThat(film).hasFieldOrPropertyWithValue("releaseDate", LocalDate.parse("1967-03-25"));
                                    assertThat(film).hasFieldOrPropertyWithValue("duration", 100);
                                    assertThat(film).hasFieldOrPropertyWithValue("mpa", mpa);
                                    assertThat(film).hasFieldOrPropertyWithValue("genres", genres);
                                }
                        );
            }

        }
    }

    private RequestCreateFilm getRequestCreateFilm() {
        RequestCreateFilm request = new RequestCreateFilm();
        request.setName("nisi eiusmod");
        request.setDescription("adipisicing");
        request.setReleaseDate(LocalDate.parse("1967-03-25"));
        request.setDuration(100);
        ID mpaId = new ID();
        mpaId.setId(1);
        request.setMpa(mpaId);
        List<ID> genresId = new ArrayList<>();
        ID genreId = new ID();
        genreId.setId(2);
        genresId.add(genreId);
        request.setGenres(genresId);
        return request;
    }

    private RequestUser getRequest() {
        RequestUser request = new RequestUser();
        request.setLogin("dolore");
        request.setName("Nick Name");
        request.setEmail("mail@mail.ru");
        request.setBirthday(LocalDate.parse("1946-08-20"));
        return request;
    }
}
