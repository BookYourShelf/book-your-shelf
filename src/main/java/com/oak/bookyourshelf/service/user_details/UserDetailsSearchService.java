package com.oak.bookyourshelf.service.user_details;


import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.repository.user_details.UserDetailsSearchRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

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

    public Map<String,Integer> sortByKey(Map<String,Integer> searchValues){
        List sortedKeys;
        sortedKeys = new ArrayList(searchValues.keySet());
        Map<String,Integer> sortedMap=new HashMap<String, Integer>();

        Collections.sort(sortedKeys);
        String key = null;
        for(int i =0 ;i<sortedKeys.size();++i)
        {
            sortedMap.put(sortedKeys.get(i).toString(),searchValues.get(sortedKeys.get(i).toString()));
        }
        return sortedMap;
    }

    public Page<String> findPaginated(Pageable pageable,User user) {
        List<String> keys = new ArrayList<>(user.getSearchHistory().keySet());
        int pageSize = pageable.getPageSize();
        int currentPage = pageable.getPageNumber();
        int startItem = currentPage * pageSize;
        List<String > list;

        if (keys.size() < startItem) {
            list = Collections.emptyList();
        } else {
            int toIndex = Math.min(startItem + pageSize, keys.size());
            list = keys.subList(startItem, toIndex);
        }

        Page<String> searchPage
                = new PageImpl<String>(list, PageRequest.of(currentPage, pageSize), keys.size());

        return searchPage;
    }


}
