package com.oak.bookyourshelf.service.user_details;

import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.repository.user_details.UserDetailsSearchRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.Collections.*;

@Service
@Transactional
public class UserDetailsSearchService {

    final UserDetailsSearchRepository userDetailsSearchRepository;

    public UserDetailsSearchService(UserDetailsSearchRepository userDetailsSearchRepository) {
        this.userDetailsSearchRepository = userDetailsSearchRepository;
    }

    public Iterable<User> listAll() {
        return userDetailsSearchRepository.findAll();
    }

    public User get(int id) {
        return userDetailsSearchRepository.findById(id).get();
    }

    public void delete(int id) {
        userDetailsSearchRepository.deleteById(id);
    }

    public void save(User user) {
        userDetailsSearchRepository.save(user);
    }

    public User getByEmail(String email) {
        return userDetailsSearchRepository.findUserByEmail(email);
    }

    public List<Integer> findAllIds() {
        return userDetailsSearchRepository.findAllIds();
    }

    public Map<String, Integer> sortByKey(Map<String, Integer> searchValues) {
        List sortedKeys;
        sortedKeys = new ArrayList(searchValues.keySet());
        Map<String, Integer> sortedMap = new HashMap<String, Integer>();

        sort(sortedKeys);
        String key = null;
        for (int i = 0; i < sortedKeys.size(); ++i) {
            sortedMap.put(sortedKeys.get(i).toString(), searchValues.get(sortedKeys.get(i).toString()));
        }
        return sortedMap;
    }

    public List<String> sortSearchKey(String sortType, User user) {
        Map<String, Integer> before = new HashMap<String, Integer>(user.getSearchHistory());
        ArrayList<String> values = new ArrayList<String>(before.keySet());
        Map<String, Integer> map = new TreeMap<>();
        List<String> result = new ArrayList<>();
        List<String> reverse = new ArrayList<>();

        for (String s : before.keySet()) {
            map.put(s, before.get(s));
        }
        Map<String,Integer> sorted= sortByValue(before);
        result.addAll(sorted.keySet());

        switch (sortType) {
            case "Search-Value-desc":
                values.sort(Collections.reverseOrder());
                return values;

            case "Search-Value-asc":
                Collections.sort(values);
                return values;

            case "Total-Search-desc":

                for (int i = result.size()-1 ; i>=0; --i)
                    reverse.add(result.get(i));
                return reverse;

            case "Total-Search-asc":

                return result;

            default:
                // total search
                for (int i = result.size()-1 ; i>=0; --i)
                    reverse.add(result.get(i));
                return reverse;
        }
    }


        public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
            List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
            list.sort(Map.Entry.comparingByValue());

            Map<K, V> result = new LinkedHashMap<>();
            for (Map.Entry<K, V> entry : list) {
                result.put(entry.getKey(), entry.getValue());
            }

            return result;
        }

}
