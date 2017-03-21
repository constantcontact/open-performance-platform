package com.opp.service;

import com.opp.dao.LoadSlaTestGroupDao;
import com.opp.domain.LoadSlaTestGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by jhermida on 10/12/16.
 */
@Service
public class LoadSlaTestGroupService {

    @Autowired
    private LoadSlaTestGroupDao loadSlaTestGroupDao;

    public int insert(LoadSlaTestGroup loadSlaTestGroup) {
        return loadSlaTestGroupDao.insert(loadSlaTestGroup);
    }

}
