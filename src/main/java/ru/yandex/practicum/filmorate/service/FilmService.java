package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.util.*;

@Service
@RequiredArgsConstructor
public class FilmService {
    private final FilmStorage filmStorage;
    private final Map<Film, List<User>> likes = new HashMap<>();

    public void addLike(final Film film , final User user){
        updateTable();
        List<User> users = likes.get(film);
        if(!users.contains(user)){
            users.add(user);
            likes.put(film,users);
        }
    }

    public void deleteLike(final Film film , final User user){
        List<User> users = likes.get(film);
        users.remove(user);
    }

    public Collection<Film> getPopularFilms(){
        updateTable();
        TreeSet<Film> sortedFilms = new TreeSet<>(new Comparator<Film>() {
            int likes1;
            int likes2;
            @Override
            public int compare(Film o1, Film o2) {
                likes1 = likes.get(o1).size();
                likes2 = likes.get(o2).size();
                return likes1 - likes2;
            }
        });
        sortedFilms.addAll(likes.keySet());
        return sortedFilms.stream().limit(10).toList();
    }

    private void updateTable() {
        Collection<Film> getList = filmStorage.getAllFilms();
        Map<Film, List<User>> newFilms = new HashMap<>();
        getList.stream().filter(film -> !newFilms.containsKey(film))
                .forEach(film -> newFilms.put(film,new ArrayList<>()));
        likes.putAll(newFilms);
    }
}
