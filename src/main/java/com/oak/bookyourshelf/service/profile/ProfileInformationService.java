package com.oak.bookyourshelf.service.profile;

import com.oak.bookyourshelf.model.User;
import com.oak.bookyourshelf.repository.profile.ProfileInformationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@Service
@Transactional
public class ProfileInformationService {
    final ProfileInformationRepository profileInformationRepository;

    public ProfileInformationService(ProfileInformationRepository profileInformationRepository) {
        this.profileInformationRepository = profileInformationRepository;
    }

    public User get(int id) {
        return profileInformationRepository.findById(id).get();
    }

    public void delete(int id) {
        profileInformationRepository.deleteById(id);
    }

    public void save(User user) {
        profileInformationRepository.save(user);
    }

    public User getByEmail (String email){return profileInformationRepository.findUserByEmail(email);}

    public boolean findSearchValue(String searchValue, User user)
    {
        for (Map.Entry<String, Integer> entry : user.getSearchHistory().entrySet()){
            if(entry.getKey().equals(searchValue))
                return true;

        }
        return false;
    }
}
