package io.ankush.kap_mini.service;

import io.ankush.kap_mini.domain.User;
import io.ankush.kap_mini.domain.UserSetting;
import io.ankush.kap_mini.model.UserSettingDTO;
import io.ankush.kap_mini.repos.UserRepository;
import io.ankush.kap_mini.repos.UserSettingRepository;
import io.ankush.kap_mini.util.NotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


@Service
public class UserSettingService {

    private final UserSettingRepository userSettingRepository;
    private final UserRepository userRepository;

    public UserSettingService(final UserSettingRepository userSettingRepository,
            final UserRepository userRepository) {
        this.userSettingRepository = userSettingRepository;
        this.userRepository = userRepository;
    }

    public List<UserSettingDTO> findAll() {
        final List<UserSetting> userSettings = userSettingRepository.findAll(Sort.by("settingID"));
        return userSettings.stream()
                .map(userSetting -> mapToDTO(userSetting, new UserSettingDTO()))
                .toList();
    }

    public UserSettingDTO get(final UUID settingID) {
        return userSettingRepository.findById(settingID)
                .map(userSetting -> mapToDTO(userSetting, new UserSettingDTO()))
                .orElseThrow(NotFoundException::new);
    }

    public UUID create(final UserSettingDTO userSettingDTO) {
        final UserSetting userSetting = new UserSetting();
        mapToEntity(userSettingDTO, userSetting);
        return userSettingRepository.save(userSetting).getSettingID();
    }

    public void update(final UUID settingID, final UserSettingDTO userSettingDTO) {
        final UserSetting userSetting = userSettingRepository.findById(settingID)
                .orElseThrow(NotFoundException::new);
        mapToEntity(userSettingDTO, userSetting);
        userSettingRepository.save(userSetting);
    }

    public void delete(final UUID settingID) {
        userSettingRepository.deleteById(settingID);
    }

    private UserSettingDTO mapToDTO(final UserSetting userSetting,
            final UserSettingDTO userSettingDTO) {
        userSettingDTO.setSettingID(userSetting.getSettingID());
        userSettingDTO.setTheme(userSetting.getTheme());
        userSettingDTO.setLanguage(userSetting.getLanguage());
        userSettingDTO.setExtraConfig(userSetting.getExtraConfig());
        userSettingDTO.setDeleteAt(userSetting.getDeleteAt());
        userSettingDTO.setUserId(userSetting.getUserId() == null ? null : userSetting.getUserId().getUserId());
        return userSettingDTO;
    }

    private UserSetting mapToEntity(final UserSettingDTO userSettingDTO,
            final UserSetting userSetting) {
        userSetting.setTheme(userSettingDTO.getTheme());
        userSetting.setLanguage(userSettingDTO.getLanguage());
        userSetting.setExtraConfig(userSettingDTO.getExtraConfig());
        userSetting.setDeleteAt(userSettingDTO.getDeleteAt());
        final User userId = userSettingDTO.getUserId() == null ? null : userRepository.findById(userSettingDTO.getUserId())
                .orElseThrow(() -> new NotFoundException("userId not found"));
        userSetting.setUserId(userId);
        return userSetting;
    }

}
